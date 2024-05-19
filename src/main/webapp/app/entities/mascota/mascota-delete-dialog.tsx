import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getMascota, deleteEntity } from './mascota.reducer';
import { getEntity } from '../dueno/dueno.reducer';

export const MascotaDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getMascota(id));
    setLoadModal(true);
  }, []);

  const mascotaEntity = useAppSelector(state => state.mascota.entity);
  const updateSuccess = useAppSelector(state => state.mascota.updateSuccess);

  const handleClose = () => {
    navigate('/mascota' + pageLocation.search);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = async() => {
    dispatch(deleteEntity(mascotaEntity.id));

    const getDueno = await((await dispatch(getEntity(mascotaEntity.dueno.id))).payload as any).data;
    console.log(mascotaEntity);
    
    
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="mascotaDeleteDialogHeading">
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody id="veterinarySystemApp.mascota.delete.question">
        <Translate contentKey="veterinarySystemApp.mascota.delete.question" interpolate={{ id: mascotaEntity.id }}>
          Are you sure you want to delete this Mascota?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button id="jhi-confirm-delete-mascota" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">Delete</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default MascotaDeleteDialog;
