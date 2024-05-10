import React, { useState, useEffect } from 'react';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap'; // O cualquier otro componente de modal que estés utilizando
import { Translate } from 'react-jhipster'; // Si estás utilizando esta librería para la traducción
import AddCita from './AddCita';



const AddCitaModal = ({ isOpen, toggle,selectedDate}) => {
  return (
    <Modal isOpen={isOpen} toggle={toggle}>
      <ModalHeader toggle={toggle}>
        <Translate contentKey="veterinarySystemApp.cita.home.createOrEditLabel">Create or edit a Cita</Translate>
      </ModalHeader>
      <ModalBody>
      <AddCita toggleModal={toggle} selectedDate={selectedDate} />
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={toggle}>
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default AddCitaModal;
