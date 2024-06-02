import React, { useRef, useEffect, useState } from "react";
import * as tf from "@tensorflow/tfjs";
import { usePredictionsAnimal } from "app/shared/util/usePredictions";
import { getEntities as getEnfermedads } from '../../../entities/enfermedad/enfermedad.reducer';
import { useAppDispatch, useAppSelector } from "app/config/store";
import { getEntities } from "app/entities/raza/raza.reducer";

const CamaraAnimal = () => {
  const dispatch = useAppDispatch();
  const [modelo, setModelo] = useState(null);
  const videoRef = useRef(null);
  const canvasRef = useRef(null);
  const otroCanvasRef = useRef(null);
  const [result, setResult] = useState<any>(null);
  const [facingMode, setFacingMode] = useState("user");
  const [stream, setStream] = useState(null);
  const enfermedades = useAppSelector(state => state.enfermedad.entities);
  const razas = useAppSelector(state => state.raza.entities);

  useEffect(() => {
    const cargarModeloInterno = async () => {
      try {
        const modeloCargado = await tf.loadLayersModel("model.json");
        setModelo(modeloCargado);
      } catch (error) {
        console.error("Error al cargar el modelo:", error);
      }
    };

    cargarModeloInterno();
  }, []);

  useEffect(() => {
    dispatch(getEnfermedads({ page: 0, size: 999, sort: `id,asc` }));
    dispatch(getEntities({ page: 0, size: 999, sort: `id,asc` }));
    const mostrarCamara = async () => {
      try {
        const newStream = await navigator.mediaDevices.getUserMedia({
          audio: false,
          video: { width: 400, height: 400 },
        });
        setStream(newStream);
        videoRef.current.srcObject = newStream;
      } catch (error) {
        console.error("No se pudo utilizar la cámara:", error);
      }
    };

    mostrarCamara();

    return () => {
      if (stream) {
        stream.getTracks().forEach((track) => track.stop());
      }
    };
  }, []);

  useEffect(() => {
    if (!canvasRef.current || !videoRef.current) return;

    const procesarCamara = () => {
      const ctx = canvasRef.current.getContext("2d");
      ctx.drawImage(
        videoRef.current,
        0,
        0,
        canvasRef.current.width,
        canvasRef.current.height
      );
    };

    const intervalId = setInterval(procesarCamara, 20);

    return () => {
      clearInterval(intervalId);
    };
  }, [canvasRef, videoRef]);

  const predecir = async () => {
    if (modelo != null && canvasRef.current && otroCanvasRef.current) {
      resample_single(
        canvasRef.current,
        100,
        100,
        otroCanvasRef.current
      );

      const image = canvasRef.current.toDataURL('image/jpeg');
      const resultBAnimal:any = await usePredictionsAnimal(image);
      setResult(resultBAnimal)
    }
  };

  const handlePredictButtonClick = () => {
    predecir();
  };

  const resample_single = (canvas, width, height, resizeCanvas) => {
    const widthSource = canvas.width;
    const heightSource = canvas.height;
    width = Math.round(width);
    height = Math.round(height);

    const ratioW = widthSource / width;
    const ratioH = heightSource / height;
    const ratioWHalf = Math.ceil(ratioW / 2);
    const ratioHHalf = Math.ceil(ratioH / 2);
    const ctx = canvas.getContext("2d");
    const ctx2 = resizeCanvas.getContext("2d");
    const img = ctx.getImageData(0, 0, widthSource, heightSource);
    const img2 = ctx2.createImageData(width, height);
    const data = img.data;
    const data2 = img2.data;

    for (let j = 0; j < height; j++) {
      for (let i = 0; i < width; i++) {
        const x2 = (i + j * width) * 4;
        let weight = 0;
        let weights = 0;
        let weightsAlpha = 0;
        let gxR = 0;
        let gxG = 0;
        let gxB = 0;
        let gxA = 0;
        const centerY = (j + 0.5) * ratioH;
        const yyStart = Math.floor(j * ratioH);
        const yyStop = Math.ceil((j + 1) * ratioH);

        for (let yy = yyStart; yy < yyStop; yy++) {
          const dy = Math.abs(centerY - (yy + 0.5)) / ratioHHalf;
          const centerX = (i + 0.5) * ratioW;
          const w0 = dy * dy;

          const xxStart = Math.floor(i * ratioW);
          const xxStop = Math.ceil((i + 1) * ratioW);

          for (let xx = xxStart; xx < xxStop; xx++) {
            const dx = Math.abs(centerX - (xx + 0.5)) / ratioWHalf;
            const w = Math.sqrt(w0 + dx * dx);

            if (w >= 1) {
              continue;
            }

            weight = 2 * w * w * w - 3 * w * w + 1;
            const posX = 4 * (xx + yy * widthSource);
            gxA += weight * data[posX + 3];
            weightsAlpha += weight;

            if (data[posX + 3] < 255) {
              weight = weight * data[posX + 3] / 250;
            }

            gxR += weight * data[posX];
            gxG += weight * data[posX + 1];
            gxB += weight * data[posX + 2];
            weights += weight;
          }
        }

        data2[x2] = gxR / weights;
        data2[x2 + 1] = gxG / weights;
        data2[x2 + 2] = gxB / weights;
        data2[x2 + 3] = gxA / weightsAlpha;
      }
    }

    ctx2.putImageData(img2, 0, 0);
  };

  const cambiarCamara = () => {
    if (videoRef.current.srcObject) {
      videoRef.current.srcObject.getTracks().forEach((track) => {
        track.stop();
      });
    }

    navigator.mediaDevices
      .getUserMedia({
        audio: false,
        video: { facingMode: facingMode === "user" ? "environment" : "user", width: 400, height: 400 },
      })
      .then((newStream) => {
        setStream(newStream);
        videoRef.current.srcObject = newStream;
        setFacingMode(facingMode === "user" ? "environment" : "user");
      })
      .catch((error) => {
        console.error("No se pudo utilizar la cámara:", error);
      });
  };

  return (
    <div className="container">
      <h1 className="text-center">Predicción de Raza Animal</h1>
      <div className="video-container text-center">
        <video ref={videoRef} autoPlay playsInline muted width="400" height="400"></video>
        <button className="btn btn-secondary" onClick={cambiarCamara}>Cambiar Cámara</button>
      </div>
      <div className="canvas-container text-center">
        <canvas ref={canvasRef} width="400" height="400" style={{ display: 'none' }}></canvas>
        <canvas ref={otroCanvasRef} width="100" height="100" style={{ display: 'none' }}></canvas>
      </div>
      <div className="text-center">
        <button className="btn btn-primary" onClick={handlePredictButtonClick}>Predecir</button>
      </div>
      {result && (
        <div className="result-container text-center">
          <h2>Resultado: {result}</h2>
        </div>
      )}
    </div>
  );
};

export default CamaraAnimal;
