import React, { useRef, useEffect, useState } from "react";
import * as tf from "@tensorflow/tfjs";
import { fetchDiseases, usePredictionsAnimal, usePredictionsBreedCat, usePredictionsBreedDog, usePredictionsDogsAndCats } from "app/shared/util/usePredictions";

const CamaraUser = () => {
  const [modelo, setModelo] = useState(null);
  const videoRef = useRef(null);
  const canvasRef = useRef(null);
  const otroCanvasRef = useRef(null);
  const [result, setResult] = useState<any>({});
  const [facingMode, setFacingMode] = useState("user");
  const [stream, setStream] = useState(null);

  useEffect(() => {
    const cargarModeloInterno = async () => {
      console.log("Cargando modelo...");
      try {
        const modeloCargado = await tf.loadLayersModel("model.json");
        setModelo(modeloCargado);
        console.log("Modelo cargado");
      } catch (error) {
        console.error("Error al cargar el modelo:", error);
      }
    };

    cargarModeloInterno();

    return () => {};
  }, []);

  useEffect(() => {
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
      clearInterval(intervalId); // Limpiar el intervalo al desmontar el componente
    };
  }, [canvasRef, videoRef]);
  

  useEffect(() => {
    const predecir = async () => {
      if (modelo != null && canvasRef.current && otroCanvasRef.current) {
        resample_single(
          canvasRef.current,
          100,
          100,
          otroCanvasRef.current
        );

        const image = canvasRef.current.toDataURL('image/jpeg');
        const ctx2 = otroCanvasRef.current.getContext("2d");
        const imgData = ctx2.getImageData(0, 0, 100, 100);
        let arr = [];
        let arr100 = [];

        for (let p = 0; p < imgData.data.length; p += 4) {
          const rojo = imgData.data[p] / 255;
          const verde = imgData.data[p + 1] / 255;
          const azul = imgData.data[p + 2] / 255;

          const gris = (rojo + verde + azul) / 3;

          arr100.push([gris]);
          if (arr100.length === 100) {
            arr.push(arr100);
            arr100 = [];
          }
        }

        arr = [arr];

        const tensor = tf.tensor4d(arr);
        const resultado = modelo.predict(tensor).dataSync();
        const resultBDogAndCat = await usePredictionsDogsAndCats(image);
        console.log(resultBDogAndCat);

        let respuesta:any;
        if (resultado <= 0.5&&resultBDogAndCat&&resultBDogAndCat.includes("cat")) {
          const breedCat = await usePredictionsBreedCat(image);
          console.log(breedCat);
          

          respuesta = {
            "especie": 'Gato',
            "raza": breedCat
          };
        } else {
          const breedDog = await usePredictionsBreedDog(image);
          console.log(breedDog);
          
          respuesta = {
            "especie": 'Perro',
            "raza": breedDog
          };
        }
        setResult(respuesta);
      }

      setTimeout(predecir, 10000);
    };

    predecir();

    return () => {};
  }, [modelo]);

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
      })
      .catch((error) => {
        console.log("Oops, hubo un error", error);
      });
  };

  return (
    <div>
      <div className="b-example-divider"></div>
      <div className="container mt-5">
        <div className="row">
          <div className="col-12 col-md-4 offset-md-4 text-center">
            <video ref={videoRef} id="video" playsInline autoPlay style={{ width: "1px" }} />
            <button className="btn btn-primary mb-2" id="cambiar-camara" onClick={cambiarCamara}>Cambiar cámara</button>
            <canvas ref={canvasRef} id="canvas" width="400" height="400" style={{ maxWidth: "100%" }}></canvas>
            <canvas ref={otroCanvasRef} id="otrocanvas" width="150" height="150" style={{ display: "none" }}></canvas>
            <div id="resultado">
            {result&&result.especie}<br/>
            {result&&result.raza}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CamaraUser;
