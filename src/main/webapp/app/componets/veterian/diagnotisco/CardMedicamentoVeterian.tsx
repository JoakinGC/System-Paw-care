import React from "react";
import { Card } from "react-bootstrap";

const CardMedicamentoVeterian = ({ nombre, descripcion }) => {
    return (
        <Card>
            <Card.Body>
                <Card.Title><strong>Nombre: </strong>{nombre || "Nombre de medicamento no disponible"}</Card.Title>
                <Card.Text><strong>Descripcion: </strong>{descripcion || "Descripción no disponible"}</Card.Text>
            </Card.Body>
        </Card>
    );
}


export default CardMedicamentoVeterian;