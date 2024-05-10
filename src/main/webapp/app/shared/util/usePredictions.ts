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


 


export async function TranslateString(mensaje: string): Promise<string | null> {
    const url = 'https://deep-translate1.p.rapidapi.com/language/translate/v2';
    const options = {
      method: 'POST',
      headers: {
        'content-type': 'application/json',
        'X-RapidAPI-Key': 'a5ddfbd33fmshd814e3004466489p199862jsn86413ef59e8d',
        'X-RapidAPI-Host': 'deep-translate1.p.rapidapi.com'
      },
      body: JSON.stringify({
        q: mensaje,
        source: 'en',
        target: 'es'
      })
    };
    
    try {
      const response = await fetch(url, options);
      const result = await response.text();
      console.log(result);
      return result;
    } catch (error) {
      console.error(error);
      return null;
    }
  }

export async function getDogBreedInfo(breedName) : Promise<any | null>{
  const url = `https://api.thedogapi.com/v1/breeds/search?q=${breedName}`;
  
  try {
      const response = await fetch(url);
      const data = await response.json();
      
      // Aquí puedes procesar los datos obtenidos, como imprimirlos en la consola
      console.log(data);
      return data[0]
  } catch (error) {
      console.error('Error al obtener información de la raza de perro:', error);
      return null;
  }
}


export async function getCatBreedInfo(breedName: string): Promise<any | null> {
  const url = `https://api.thecatapi.com/v1/breeds/search?q=${breedName}`;

  try {
      const response = await fetch(url);
      const data = await response.json();

      // Aquí puedes procesar los datos obtenidos, como imprimirlos en la consola
      console.log(data);
      return data[0];
  } catch (error) {
      console.error('Error al obtener información de la raza de gato:', error);
      return null;
  }
}

