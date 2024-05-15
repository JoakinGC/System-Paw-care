import { IDatelleCompra } from "app/shared/model/datelle-compra.model";
import React from "react";
import { Card, Button } from 'react-bootstrap';

const CardCompraDetils = ({ detalleCompra }:{detalleCompra:IDatelleCompra}) => {
    const { cantidad, precioUnitario, producto, totalProducto } = detalleCompra;

    return (
        <Card>
            <Card.Body style={{ textAlign: 'center' }}>
                <Card.Title>{producto.nombre}</Card.Title>
                <Card.Text>
                    Cantidad: {cantidad} <br />
                    Precio Unitario: {precioUnitario} <br />
                    Total Producto: {totalProducto} <br />
                    Producto: {producto.nombre} <br />
                    Descripción: {producto.descripcion} <br />
                </Card.Text>
                <Button variant="primary">Ver Detalles</Button>
            </Card.Body>
        </Card>
    );
}

export default CardCompraDetils;
