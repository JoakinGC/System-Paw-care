import React, { useState } from 'react';

const ImageClassifier = () => {
  const [predictions, setPredictions] = useState([]);
  const [loading, setLoading] = useState(false);

  const classifyImage = async (file) => {
    setLoading(true);

    const formData = new FormData();
    formData.append('file', file);
    console.log(formData);
    
    try {
      const response = await fetch(
        "https://api-inference.huggingface.co/models/google/vit-base-patch16-224",
        {
          method: "POST",
          headers: {
            Authorization: "Bearer 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"
          },
          body: formData
        }
      );

      if (!response.ok) {
        throw new Error('Failed to classify image');
      }

      const result = await response.json();
      setPredictions(result);
    } catch (error) {
      console.error('Error classifying image:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleFileChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      classifyImage(file);
    }
  };

  return (
    <div>
      <input type="file" accept="image/*" onChange={handleFileChange} />
      {loading && <p>Loading...</p>}
      <ul>
        {predictions.map((prediction, index) => (
          <li key={index}>
            {prediction.label}: {prediction.score}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ImageClassifier;
