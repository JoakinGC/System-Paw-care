export async function usePredictionsDogsAndCats(image:any) :Promise<string | null>{
  try {
    const response = await fetch('http://localhost:8088/checkDogsAndCats', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ image }),
    });

    if (response.ok) {
      const data = await response.json();
      console.log('Respuesta del servidor:', data);
      
      if (data.message.includes("is loading")) {
        console.log("El modelo está cargando, espera un momento...");
        return null; 
      }

      if (data.hasOwnProperty("error")) {
        console.error("Error en la respuesta del servidor:", data.error);
        return null; 
      }

      const maxScoreObj = data.result.reduce((prev: any, current: any) => (prev.score > current.score) ? prev : current);

      const label = maxScoreObj.label;

      return label;
    } else {
      console.error('Error al enviar la imagen al servidor:', response.statusText);
      return null;
    }
  } catch (error) {
    console.error('Error de red:', error);
    return null;
  }
}


export async function usePredictionsAnimal(image:any) :Promise<string | null>{
    try {
      const response = await fetch('http://localhost:8088/checkAnimal', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ image }),
      });
  
      if (response.ok) {
        const data = await response.json();
        console.log('Respuesta del servidor:', data);
        // Encuentra el objeto con el puntaje más alto
      const maxScoreObj = data.result.reduce((prev: any, current: any) => (prev.score > current.score) ? prev : current);

      // Guarda el label del objeto con el puntaje más alto
      const label = maxScoreObj.label;

      // Devuelve el label al cliente
      return label;
      } else {
        console.error('Error al enviar la imagen al servidor:', response.statusText);
        return null;
      }
    } catch (error) {
      console.error('Error de red:', error);
      return null;
    }
  }

  export async function usePredictionsBreedDog(image:any) :Promise<string | null>{
    try {
      const response = await fetch('http://localhost:8088/checkBreedDog', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ image }),
      });
  
      if (response.ok) {
        const data = await response.json();
        console.log('Respuesta del servidor:', data);
        // Encuentra el objeto con el puntaje más alto
      const maxScoreObj = data.result.reduce((prev: any, current: any) => (prev.score > current.score) ? prev : current);

      // Guarda el label del objeto con el puntaje más alto
      const label = maxScoreObj.label;

      // Devuelve el label al cliente
      return label;
      } else {
        console.error('Error al enviar la imagen al servidor:', response.statusText);
        return null;
      }
    } catch (error) {
      console.error('Error de red:', error);
      return null;
    }
  }


  export async function usePredictionsBreedCat(image:any) :Promise<string | null>{
    try {
      const response = await fetch('http://localhost:8088/checkBreedCat', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ image }),
      });
  
      if (response.ok) {
        const data = await response.json();
        console.log('Respuesta del servidor:', data);
        // Encuentra el objeto con el puntaje más alto
      const maxScoreObj = data.result.reduce((prev: any, current: any) => (prev.score > current.score) ? prev : current);

      // Guarda el label del objeto con el puntaje más alto
      const label = maxScoreObj.label;

      // Devuelve el label al cliente
      return label;
      } else {
        console.error('Error al enviar la imagen al servidor:', response.statusText);
        return null;
      }
    } catch (error) {
      console.error('Error de red:', error);
      return null;
    }
  }

  export async function fetchDiseases(){
    const headers = new Headers({
      "Content-Type": "application/json",
      "x-api-key": "live_2CmCYbBbHxoKPFHN2tN89sxh5btpZOXtqE8jV68gNiAvG1nSp8TLIparkF2gypTx"
    });
    
    var requestOptions = {
      method: 'GET',
      headers: headers,
      redirect: 'follow'
    };
    
    fetch("https://api.thedogapi.com/v1/images/search?size=med&mime_types=jpg&format=json&has_breeds=true&order=RANDOM&page=0&limit=1", requestOptions)
      .then(response => response.text())
      .then(result => console.log(result))
      .catch(error => console.log('error', error));        
  }