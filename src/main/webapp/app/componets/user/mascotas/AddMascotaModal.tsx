import React, { useState } from 'react';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button, Form, FormGroup, Label, Input } from 'reactstrap';

const AddMascotaModal = ({ isOpen, toggle, onSave }) => {
  const [nCarnet, setNCarnet] = useState('');
  const [fechaNacimiento, setFechaNacimiento] = useState('');
  const [duenoId, setDuenoId] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    // Aquí podrías realizar validaciones de los campos
    // Luego llamarías a la función onSave con los datos de la nueva mascota
    onSave({ nCarnet, fechaNacimiento, duenoId });
    // Después de guardar, cierra el modal
    toggle();
  };

  return (
    <Modal isOpen={isOpen} toggle={toggle}>
      <ModalHeader toggle={toggle}>Agregar Mascota</ModalHeader>
      <ModalBody>
        <Form onSubmit={handleSubmit}>
          <FormGroup>
            <Label for="nCarnet">Número de Carnet</Label>
            <Input type="text" name="nCarnet" id="nCarnet" value={nCarnet} onChange={(e) => setNCarnet(e.target.value)} />
          </FormGroup>
          <FormGroup>
            <Label for="fechaNacimiento">Fecha de Nacimiento</Label>
            <Input type="date" name="fechaNacimiento" id="fechaNacimiento" value={fechaNacimiento} onChange={(e) => setFechaNacimiento(e.target.value)} />
          </FormGroup>
          <FormGroup>
            <Label for="duenoId">ID del Dueño</Label>
            <Input type="text" name="duenoId" id="duenoId" value={duenoId} onChange={(e) => setDuenoId(e.target.value)} />
          </FormGroup>
          <Button color="primary" type="submit">Guardar</Button>
          <Button color="secondary" onClick={toggle}>Cancelar</Button>
        </Form>
      </ModalBody>
    </Modal>
  );
};

export default AddMascotaModal;
