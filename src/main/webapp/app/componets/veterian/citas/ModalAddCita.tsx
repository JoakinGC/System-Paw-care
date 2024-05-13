import React, { useState } from 'react';
import { Modal, Button } from 'react-bootstrap'; // Importa los componentes de Bootstrap necesarios

const ModalAddCita = ({ isOpen, toggle }) => {
  const handleClose = () => {
    toggle(); // Llama a la función toggle pasada como prop para cerrar el modal
  }

  return (
    <>
      <Modal show={isOpen} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Título del Modal</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          Contenido del modal aquí...
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Cerrar
          </Button>
          <Button variant="primary" onClick={handleClose}>
            Guardar Cambios
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}

export default ModalAddCita;
