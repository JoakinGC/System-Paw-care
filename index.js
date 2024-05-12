import express from 'express';
import { promises as fsPromises, readFileSync } from 'fs';

const app = express();
const PORT = 8088; // Cambiamos el puerto a 8080
let solitud = 0;
let solitudAnimal = 0;
let solitudRazaPerro = 0;

async function queryDogsAndCat(filename) {
	const data = await readFileSync(filename);
	const response = await fetch(
		"https://api-inference.huggingface.co/models/omarques/autotrain-dogs-and-cats-1527055142",
		{
			headers: { Authorization: "Bearer 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" },
			method: "POST",
			body: data,
		}
	);
	const result = await response.json();
	return result;
}

async function queryAnymalT(filename) {
  const data = await readFileSync(filename);
  const response = await fetch(
    "https://api-inference.huggingface.co/models/victor/animals-classifier",
    {
      headers: { Authorization: "Bearer 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" },
      method: "POST",
      body: data,
    }
  );
  const result = await response.json();
  return result;
}

async function fetchResultWithRetry(filename, maxRetries = 3) {
  try {
    const response = await queryAnymalT(filename);
    console.log(JSON.stringify(response));

    if (response.hasOwnProperty("estimated_time") && response.estimated_time > 0) {
      await new Promise(resolve => setTimeout(resolve, response.estimated_time * 1000));
      return fetchResultWithRetry(filename, maxRetries - 1);
    } else {
      console.log("No hay tiempo estimado o ha terminado el proceso.");
      return response;
    }
  } catch (error) {
    console.error("Error al realizar la consulta:", error);
    return null;
  }
}


async function queryFetchBredDog(filename) {
	const data = await readFileSync(filename);
	const response = await fetch(
		"https://api-inference.huggingface.co/models/skyau/dog-breed-classifier-vit",
		{
			headers: { Authorization: "Bearer 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" },
			method: "POST",
			body: data,
		}
	);
	const result = await response.json();
	return result;
}


async function queryFetchBreedCat(filename) {
	const data = await readFileSync(filename);
	const response = await fetch(
		"https://api-inference.huggingface.co/models/dima806/67_cat_breeds_image_detection",
		{
			headers: { Authorization: "Bearer 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" },
			method: "POST",
			body: data,
		}
	);
	const result = await response.json();
	return result;
}

async function fetchResultWithRetryBreedCat(filename, maxRetries = 3) {
  try {
    const response = await queryFetchBreedCat(filename);
    console.log(JSON.stringify(response));

    if (response.hasOwnProperty("estimated_time") && response.estimated_time > 0) {
      await new Promise(resolve => setTimeout(resolve, response.estimated_time * 1000));
      return queryFetchBreedCat(filename, maxRetries - 1);
    } else {
      console.log("No hay tiempo estimado o ha terminado el proceso.");
      return response;
    }
  } catch (error) {
    console.error("Error al realizar la consulta:", error);
    return null;
  }
}


//API express

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Configuración de CORS
app.use((req, res, next) => {
  res.setHeader('Access-Control-Allow-Origin', 'http://localhost:9000');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');
  res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type');
  res.setHeader('Access-Control-Allow-Credentials', true);
  next();
});

app.post('/checkDogsAndCats', async (req, res) => {
  try {
    // Guardar la imagen localmente
    const { image } = req.body;
    const fileName = `dogsAndCats/${solitud}uploaded_image.jpg`;
    const base64Data = image.replace(/^data:image\/jpeg;base64,/, '');
    await fsPromises.writeFile(fileName, base64Data, 'base64');
    solitud++;
    const result =  await queryDogsAndCat(fileName)
    console.log('Imagen guardada exitosamente'); 
    res.status(200).json({ message: 'Imagen guardada exitosamente' ,result});
  } catch (error) {
    console.error('Error al guardar la imagen:', error);
    res.status(500).send('Error interno del servidor');
  }
});

app.post('/checkAnimal', async (req, res) => {
  try {
    // Guardar la imagen localmente
    const { image } = req.body;
    const fileName = `animals/${solitudAnimal}uploaded_image.jpg`;
    const base64Data = image.replace(/^data:image\/jpeg;base64,/, '');
    await fsPromises.writeFile(fileName, base64Data, 'base64');
    solitudAnimal++;
    const result =  await fetchResultWithRetry(fileName)
    console.log('Imagen guardada exitosamente'); 
    res.status(200).json({ message: 'Imagen guardada exitosamente' ,result});
  } catch (error) {
    console.error('Error al guardar la imagen:', error);
    res.status(500).send('Error interno del servidor');
  }
});


app.post('/checkBreedDog', async (req, res) => {
  try {
    // Guardar la imagen localmente
    const { image } = req.body;
    const fileName = `breedDogs/${solitudRazaPerro}uploaded_image.jpg`;
    const base64Data = image.replace(/^data:image\/jpeg;base64,/, '');
    await fsPromises.writeFile(fileName, base64Data, 'base64');
    solitudRazaPerro++;
    const result =  await queryFetchBredDog(fileName)
    console.log('Imagen guardada exitosamente'); 
    res.status(200).json({ message: 'Imagen guardada exitosamente' ,result});
  } catch (error) {
    console.error('Error al guardar la imagen:', error);
    res.status(500).send('Error interno del servidor');
  }
});

app.post('/checkBreedCat', async (req, res) => {
  try {
    // Guardar la imagen localmente
    const { image } = req.body;
    const fileName = `breedCats/${solitudRazaPerro}uploaded_image.jpg`;
    const base64Data = image.replace(/^data:image\/jpeg;base64,/, '');
    await fsPromises.writeFile(fileName, base64Data, 'base64');
    solitudRazaPerro++;
    const result =  await fetchResultWithRetryBreedCat(fileName)
    console.log('Imagen guardada exitosamente'); 
    res.status(200).json({ message: 'Imagen guardada exitosamente' ,result});
  } catch (error) {
    console.error('Error al guardar la imagen:', error);
    res.status(500).send('Error interno del servidor');
  }
});


app.listen(PORT, () => {
  console.log(`Servidor Express escuchando en el puerto ${PORT}`);
});


//Limpiar Campetas

async function eliminarImagenesGuardadasAnimals() {
  try {
    // Obtener la lista de archivos en la carpeta dogsAndCats
    const files = await fsPromises.readdir('./animals');

    // Iterar sobre cada archivo y eliminarlo
    for (const file of files) {
      await fsPromises.unlink(`./dogsAndCats/${file}`);
      console.log(`Imagen ${file} eliminada con éxito`);
    }

    console.log('Imágenes guardadas eliminadas con éxito');
  } catch (error) {
    console.error('Error al eliminar las imágenes guardadas:', error);
  }
}


async function eliminarImagenesGuardadasDogsAndCats() {
  try {
    // Obtener la lista de archivos en la carpeta dogsAndCats
    const files = await fsPromises.readdir('./dogsAndCats');

    // Iterar sobre cada archivo y eliminarlo
    for (const file of files) {
      await fsPromises.unlink(`./dogsAndCats/${file}`);
      console.log(`Imagen ${file} eliminada con éxito`);
    }

    console.log('Imágenes guardadas eliminadas con éxito');
  } catch (error) {
    console.error('Error al eliminar las imágenes guardadas:', error);
  }
}

async function eliminarImagenesGuardadasCatsBreed() {
  try {
    // Obtener la lista de archivos en la carpeta dogsAndCats
    const files = await fsPromises.readdir('./breedCats');

    // Iterar sobre cada archivo y eliminarlo
    for (const file of files) {
      await fsPromises.unlink(`./breedCats/${file}`);
      console.log(`Imagen ${file} eliminada con éxito`);
    }

    console.log('Imágenes guardadas eliminadas con éxito');
  } catch (error) {
    console.error('Error al eliminar las imágenes guardadas:', error);
  }
}
async function eliminarImagenesGuardadasDogsBreed() {
  try {
    // Obtener la lista de archivos en la carpeta dogsAndCats
    const files = await fsPromises.readdir('./breedDogs');

    // Iterar sobre cada archivo y eliminarlo
    for (const file of files) {
      await fsPromises.unlink(`./breedDogs/${file}`);
      console.log(`Imagen ${file} eliminada con éxito`);
    }

    console.log('Imágenes guardadas eliminadas con éxito');
  } catch (error) {
    console.error('Error al eliminar las imágenes guardadas:', error);
  }
}

function AllClean(){
  eliminarImagenesGuardadasAnimals();
  eliminarImagenesGuardadasDogsAndCats();
  eliminarImagenesGuardadasCatsBreed();
  eliminarImagenesGuardadasDogsBreed();
}


setInterval(AllClean, 120000);