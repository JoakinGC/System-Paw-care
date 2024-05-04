
# Paw Care Chat UI

Basado en chat Huggiface, implementa el llamado de API que tiene Huggiface (https://huggingface.co/docs/api-inference/index) y la busqueda local Google.
Chat estilo chat GPT pensado para aquellos que prefieren un asistente virtual capas de comunicarse y saber informacion del usuario que se logeo en Paw Care  como sus mascotas y proximas citas ademas de permitir la busqueda de hoteles y cuidadores compententes por medio de Scrapping ya que las busquedas por defecto no son tan eficientes en los resultados.

Combinado con la generación de reconicimiento de Mascotas permitira generar citas y fichas si se da el consentimiento del mismo


## Tech Stack

**Client:** Svelte Kit, TypeScript, TailwindCSS

**Server:** Node, Express

**DataBase:** MySQL, MongoDB


## API Reference


| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `api_key` | `string` | **Required**. https://huggingface.co/docs/api-inference/index|

#### Consultas Modelo

```http
  GET /models/<MODEL_ID> 
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `model`      | `string` | **Required**. MODEL_ID |




## Documentation

[Documentation](https://linktodocumentation)

