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
  console.log(modeloEntrenado)
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
      const otroCanvas = otroCanvasRef.current;
      const canvas = canvasRef.current;
      const ctx2 = otroCanvas.getContext("2d");
      const ctx = canvas.getContext("2d");

      const canvasWidth = canvas.width;
      const canvasHeight = canvas.height;

      console.log(canvas);
      const imgData = ctx2.getImageData(0, 0, canvasWidth, canvasHeight);
      // Hacer la predicción
     // Preparación de los datos de entrada para el modelo
     const arr = [];

     for (let i = 0; i < 100; i++) {
         const row = [];
         for (let j = 0; j < 100; j++) {
             const pixelIndex = (i * 100 + j) * 4; // Cálculo del índice del píxel
             const rojo = imgData.data[pixelIndex] / 255;
             const verde = imgData.data[pixelIndex + 1] / 255;
             const azul = imgData.data[pixelIndex + 2] / 255;
             const gris = (rojo + verde + azul) / 3;
             row.push(gris);
         }
         arr.push(row);
     }
     
     // Convertimos el arreglo en un TypedArray plano
      const flatArr = arr.flat();


      // Creamos el tensor 4D
      const tensor = tf.tensor4d(flatArr, [1, 100, 100, 1]);



      const resultadoPrediccion = modelo.predict(tensor).dataSync();


      let respuesta;
      if (resultadoPrediccion[0] <= 0.5) {
        respuesta = "Gato";
      } else {
        respuesta = "Perro";
      }

      console.log(respuesta);
      setResultado(respuesta);
    }

    setTimeout(predecir, 150);
  };

  return (
    <div>
      <div className="container mt-5">
        <div className="row">
          <div className="col-12 col-md-4 offset-md-4 text-center">
            {resultado&&resultado}
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
