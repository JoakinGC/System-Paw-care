import axios from "axios";
import React,{useState} from "react";


declare global {
    interface Window {
      ImageCapture: any; 
    }
  }

const ImageUploadForm = () => {
    const [selectedFile, setSelectedFile] = useState(null);
    const [capturedImage, setCapturedImage] = useState(null);
  
    const handleFileChange = (event) => {
      setSelectedFile(event.target.files[0]);
    };
  
    const displayCapturedImage = (blob) => {
      setCapturedImage(URL.createObjectURL(blob));
    };
    
    const handleCameraCapture = async () => {
      try {
        const mediaStream = await navigator.mediaDevices.getUserMedia({ video: true });
        console.log(mediaStream);
        
        const mediaStreamTrack = mediaStream.getVideoTracks()[0];
        console.log(mediaStreamTrack);
        const imageCapture = new window.ImageCapture(mediaStreamTrack); 
        console.log(imageCapture);
        
  
        const blob = await imageCapture.takePhoto();
        console.log(blob);
        
        setSelectedFile(blob);
        displayCapturedImage(blob);
      } catch (error) {
        console.error('Error al capturar la imagen:', error);
      }
    };
  
    const handleSubmit = async (event) => {
      event.preventDefault();
  
      if (!selectedFile) {
        alert('Por favor selecciona un archivo.');
        return;
      }
  
      const formData = new FormData();
          formData.append('file', selectedFile, 'captured_image.png');
  
      try {
        const response = await axios.post('http://localhost:9000/api/images/upload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        });
        console.log('Respuesta del servidor:', response.data);
        alert('Imagen subida con éxito.');
      } catch (error) {
        console.error('Error al subir la imagen:', error);
        alert('Error al subir la imagen. Por favor intenta de nuevo.');
      }
    };
  
    return (
      <form onSubmit={handleSubmit}>
        <div>
          <input type="file" accept="image/*" onChange={handleFileChange} />
        </div>
        <div>
  
          <button type="button" onClick={handleCameraCapture}>Tomar Foto</button>
        </div>
        <div>
          <button type="submit">Subir Imagen</button>
        </div>
        {capturedImage && <img src={capturedImage} alt="Captured" />}
      </form>
    );
  };


  export default ImageUploadForm;