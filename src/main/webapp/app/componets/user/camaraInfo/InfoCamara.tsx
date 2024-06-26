import React, { useState } from "react";
import { Modal, Button } from "react-bootstrap";
import { Link } from "react-router-dom"; // Importa el componente Link de react-router-dom
import "./infoCamara.css";


const InfoCamara = () => {
  const [showModal, setShowModal] = useState(false);
  const handleCloseModal = () => setShowModal(false);
  const handleShowModal = () => setShowModal(true);

  return (
    <div className="container-info-cam">
      <h2 className="title-info-cam">¿Por qué conocer a tu mejor amigo peludo?</h2>
      <p className="paragraph">Descubre un servicio gratuito que te permite explorar características de tu perro que quizás desconocías, como su edad en años humanos o las enfermedades a las que podría ser propenso. Además, obtén información sobre cuidados especiales y mucho más.</p>
      <h2 className="subtitle-info-cam">¿Por qué es importante conocer a tu fiel compañero?</h2>
      <p className="paragraph">Conocer a fondo a tu mascota te permite brindarle una vida plena y saludable. No hay nada comparable a comprender a tu amigo peludo, quien estará a tu lado en los momentos buenos y malos, haciéndote compañía y brindándote amor incondicional.</p>
      <p className="paragraph">¡Haz clic aquí para descubrir más sobre tu mejor amigo y fortalecer vuestro vínculo!</p>

      {/* Botón para abrir el modal */}
      <Button variant="primary" onClick={handleShowModal}>
        Descubrir más
      </Button>

      {/* Modal */}
      <Modal show={showModal} onHide={handleShowModal} centered>
        <Modal.Header closeButton>
          <Modal.Title>Seleccione la opción que quieras:</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="d-flex justify-content-around">
            <Link to={`/camaraInfo/camara`} className="btn btn-primary">Gato/Perro</Link>
            <Link to="/camaraInfo/camaraAnimal">
              <Button variant="warning" onClick={handleCloseModal}>
                Animal
              </Button>
            </Link>
          </div>
        </Modal.Body>
      </Modal>
    </div>
  );
};

export default InfoCamara;
