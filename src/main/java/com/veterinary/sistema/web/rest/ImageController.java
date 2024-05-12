package com.veterinary.sistema.web.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private static final String UPLOAD_DIR = "C:\\Users\\USUARIO\\OneDrive - fempa.es\\Escritorio\\fotos";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a file");
        }

        try {
            // Crear el directorio de subida si no existe
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Guardar el archivo en la ubicación deseada
            String fileName = file.getOriginalFilename();
            String filePath = UPLOAD_DIR + File.separator + fileName; // Utiliza File.separator para la compatibilidad multiplataforma
            File dest = new File(filePath);
            file.transferTo(dest);

            // Devolver la URL del archivo guardado
            String fileUrl = "URL_de_tu_aplicacion" + "/api/images/" + fileName; // Reemplaza "URL_de_tu_aplicacion" con la URL real de tu aplicación
            return ResponseEntity.ok().body(fileUrl);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) {
        try {
            // Obtener el archivo del directorio de subida
            File file = new File(UPLOAD_DIR + "/" + fileName);
            byte[] imageBytes = org.apache.commons.io.FileUtils.readFileToByteArray(file);
            return ResponseEntity.ok().body(imageBytes);
        } catch (IOException e) {
            System.out.println("Error image: " +e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}

