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
        const modelo = await tf.loadLayersModel('../../../model.json');
        console.log("Modelo cargado exitosamente");
        return modelo;
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
      const imgData = ctx2.getImageData(0, 0, 100, 100);

      // Hacer la predicción
      var arr = [];
      var arr100 = [];

      for (var p = 0; p < imgData.data.length; p += 4) {
        var rojo = imgData.data[p] / 255;
        var verde = imgData.data[p + 1] / 255;
        var azul = imgData.data[p + 2] / 255;

        var gris = (rojo + verde + azul) / 3;

        arr100.push([gris]);
        if (arr100.length === 100) {
          arr.push(arr100);
          arr100 = [];
        }
      }

      arr = [arr];

      var tensor = window.tf.tensor4d(arr);
      var resultadoPrediccion = modelo.predict(tensor).dataSync();

      var respuesta;
      if (resultadoPrediccion <= 0.5) {
        respuesta = "Gato";
      } else {
        respuesta = "Perro";
      }
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
