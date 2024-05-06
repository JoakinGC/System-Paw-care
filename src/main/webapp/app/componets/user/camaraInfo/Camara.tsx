import React, { useEffect, useRef, useState } from "react";
import modeloEntrenado from '../../../model.json'
import * as tf from '@tensorflow/tfjs';


declare global {
  interface Window {
    tf: any;
  }
}

const CamaraUso = () => {
  const videoRef = useRef(null);
  const canvasRef = useRef(null);
  const otroCanvasRef = useRef(null);

  const [resultado, setResultado] = useState("");
  let modelo = null;
  let currentStream = null;
  let facingMode = "user";

  useEffect(() => {
    const cargarModelo = async () => {
      console.log("Cargando modelo...");
      try {
        const modelTopology = modeloEntrenado.modelTopology;
        const weightsManifest = modeloEntrenado.weightsManifest;
        modelo = await tf.loadLayersModel(
            tf.io.fromMemory(modelTopology, weightsManifest)
        );
        console.log("Modelo cargado exitosamente");
      } catch (error) {
        console.error("Error al cargar el modelo:", error);
        throw error;
      }
    };
    
    cargarModelo();
    mostrarCamara();

    return () => {
      if (currentStream) {
        currentStream.getTracks().forEach((track) => {
          track.stop();
        });
      }
    };
  }, []);

  const mostrarCamara = () => {
    const opciones = {
      audio: false,
      video: {
        width: 400,
        height: 400,
      },
    };

    navigator.mediaDevices
      .getUserMedia(opciones)
      .then((stream) => {
        currentStream = stream;
        videoRef.current.srcObject = currentStream;
        procesarCamara();
        predecir();
      })
      .catch((err) => {
        alert("No se pudo utilizar la camara :(");
        console.log(err);
      });
  };

  const cambiarCamara = () => {
    if (currentStream) {
      currentStream.getTracks().forEach((track) => {
        track.stop();
      });
    }

    facingMode = facingMode === "user" ? "environment" : "user";

    const opciones = {
      audio: false,
      video: {
        facingMode: facingMode,
        width: 400,
        height: 400,
      },
    };

    navigator.mediaDevices
      .getUserMedia(opciones)
      .then((stream) => {
        currentStream = stream;
        videoRef.current.srcObject = currentStream;
      })
      .catch((err) => {
        console.log("Oops, hubo un error", err);
      });
  };

  const procesarCamara = () => {
    const canvas = canvasRef.current;
    const video = videoRef.current;
    const ctx = canvas.getContext("2d");

    ctx.drawImage(video, 0, 0, 400, 400, 0, 0, 400, 400);
    setTimeout(procesarCamara, 20);
  };

  const predecir = () => {
    if (modelo !== null) {
      const canvas = canvasRef.current;
      const otroCanvas = otroCanvasRef.current;
      const ctx = canvas.getContext("2d");
      const ctx2 = otroCanvas.getContext("2d");
  
      // Procesar la imagen antes de la predicción
      resample_single();
  
      // Convertir la imagen a escala de grises
      const imgData = ctx2.getImageData(0, 0, 100, 100);
  
      // Preparar los datos de entrada para el modelo
      const arr = [];
      let arr100 = [];
      for (let p = 0; p < imgData.data.length; p += 4) {
        const gris = imgData.data[p] / 255;
        arr100.push([gris]);
        if (arr100.length === 100) {
          arr.push(arr100);
          arr100 = [];
        }
      }
      const tensor = tf.tensor4d([arr]);
  
      // Realizar la predicción
      const resultadoPrediccion = modelo.predict(tensor).dataSync();
      const respuesta = resultadoPrediccion <= 0.5 ? "Gato" : "Perro";
      setResultado(respuesta);
    }
  
    setTimeout(predecir, 150);
  };
  

  const resample_single = () => {
    const canvas = canvasRef.current;
    const otroCanvas = otroCanvasRef.current;
  
    const width_source = canvas.width;
    const height_source = canvas.height;
    const width = 100;
    const height = 100;
  
    const ratio_w = width_source / width;
    const ratio_h = height_source / height;
    const ratio_w_half = Math.ceil(ratio_w / 2);
    const ratio_h_half = Math.ceil(ratio_h / 2);
  
    const ctx = canvas.getContext("2d");
    const ctx2 = otroCanvas.getContext("2d");
    const img = ctx.getImageData(0, 0, width_source, height_source);
    const img2 = ctx2.createImageData(width, height);
    const data = img.data;
    const data2 = img2.data;
  
    for (let j = 0; j < height; j++) {
      for (let i = 0; i < width; i++) {
        const x2 = (i + j * width) * 4;
        let weight = 0;
        let weights = 0;
        let weights_alpha = 0;
        let gx_r = 0;
        let gx_g = 0;
        let gx_b = 0;
        let gx_a = 0;
        const center_y = (j + 0.5) * ratio_h;
        const yy_start = Math.floor(j * ratio_h);
        const yy_stop = Math.ceil((j + 1) * ratio_h);
        for (let yy = yy_start; yy < yy_stop; yy++) {
          const dy = Math.abs(center_y - (yy + 0.5)) / ratio_h_half;
          const center_x = (i + 0.5) * ratio_w;
          const w0 = dy * dy; //pre-calc part of w
          const xx_start = Math.floor(i * ratio_w);
          const xx_stop = Math.ceil((i + 1) * ratio_w);
          for (let xx = xx_start; xx < xx_stop; xx++) {
            const dx = Math.abs(center_x - (xx + 0.5)) / ratio_w_half;
            const w = Math.sqrt(w0 + dx * dx);
            if (w >= 1) {
              //pixel too far
              continue;
            }
            //hermite filter
            weight = 2 * w * w * w - 3 * w * w + 1;
            const pos_x = 4 * (xx + yy * width_source);
            //alpha
            gx_a += weight * data[pos_x + 3];
            weights_alpha += weight;
            //colors
            if (data[pos_x + 3] < 255) weight = weight * data[pos_x + 3] / 250;
            gx_r += weight * data[pos_x];
            gx_g += weight * data[pos_x + 1];
            gx_b += weight * data[pos_x + 2];
            weights += weight;
          }
        }
        data2[x2] = gx_r / weights;
        data2[x2 + 1] = gx_g / weights;
        data2[x2 + 2] = gx_b / weights;
        data2[x2 + 3] = gx_a / weights_alpha;
      }
    }
  
    ctx2.putImageData(img2, 0, 0);
  };
  


  return (
    <div>
      <div className="container mt-5">
        <div className="row">
          <div className="col-12 col-md-4 offset-md-4 text-center">
            <video
              ref={videoRef}
              playsInline
              autoPlay
              style={{ width: "1px" }}
            ></video>
            <button
              className="btn btn-primary mb-2"
              onClick={cambiarCamara}
            >
              Cambiar cámara
            </button>
            <canvas
              ref={canvasRef}
              width="400"
              height="400"
              style={{ maxWidth: "100%" }}
            ></canvas>
            <canvas
              ref={otroCanvasRef}
              width="150"
              height="150"
              style={{ display: "none" }}
            ></canvas>
            <div id="resultado">{resultado}</div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CamaraUso;
