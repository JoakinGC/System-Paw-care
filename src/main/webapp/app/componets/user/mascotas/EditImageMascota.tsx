import axios from 'axios';
import React, { useState, useEffect } from 'react';
import { Modal, ModalHeader, ModalBody, Button } from 'reactstrap';

const EditImageMascota = ({ isOpen, toggle, imageUrl }) =>{
    const [capturedImage, setCapturedImage] = useState(null);

  const handleCameraCapture = async () => {
    try {
      const mediaStream = await navigator.mediaDevices.getUserMedia({ video: true });
      const mediaStreamTrack = mediaStream.getVideoTracks()[0];
      const imageCapture = new window.ImageCapture(mediaStreamTrack);
      const blob = await imageCapture.takePhoto();

      // Display the captured image
      setCapturedImage(URL.createObjectURL(blob));

      // Delete the existing image on the server and save the new one
      await deleteAndSaveImage(blob);
      window.location.reload();
    } catch (error) {
      console.error('Error capturing image:', error);
    }
  };

  const deleteAndSaveImage = async (newImageBlob) => {
    try {

      // Save the new image to the server
      const formData = new FormData();
      formData.append('file', newImageBlob,imageUrl);
      const response = await axios.post('http://localhost:9000/api/images/upload', formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
    });

      console.log('Image saved successfully.');
    } catch (error) {
      console.error('Error deleting or saving image:', error);
    }
  };

  return (
    <Modal isOpen={isOpen} toggle={toggle}>
      <ModalHeader toggle={toggle}>Capture Image</ModalHeader>
      <ModalBody>
        {capturedImage ? (
          <img src={capturedImage} alt="Captured" />
        ) : (
          <p>No image captured yet.</p>
        )}
        <Button color="primary" onClick={handleCameraCapture}>
          Capture Image
        </Button>
        <Button color="secondary" onClick={toggle}>
          Cancel
        </Button>
      </ModalBody>
    </Modal>
  );
};

export default EditImageMascota;