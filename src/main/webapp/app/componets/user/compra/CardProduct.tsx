import React from "react";
import './user.css';
import { Button } from "reactstrap";
import { Link } from "react-router-dom";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlus, faMinus } from '@fortawesome/free-solid-svg-icons';

interface PropsCardProduct {
    id:string;
    cardTitle: string;
    cardText: string;
    link:string;
    quantity: number; // Propiedad para la cantidad de productos seleccionados
    onQuantityChange: (quantity: number) => void; // Función para manejar el cambio de cantidad
};

const CardProduct = ({ id, cardTitle, cardText, link, quantity, onQuantityChange }: PropsCardProduct) => {
    const handleIncrease = () => {
        onQuantityChange(quantity + 1);
    };

    const handleDecrease = () => {
        onQuantityChange(Math.max(quantity - 1, 0));
    };

    return (
        <div className="card-wrapper">
            <div className='card'>
                <img src={link} className="card-img-top" alt="..." />
                <div className="card-body">
                    
                        <h5 className="card-text">{cardText}</h5>
                   
                    <div className="d-flex justify-content-between align-items-center">
                        <p className="card-title">{cardTitle}€</p>
                        <div className="d-flex align-items-center">
                            <Button color="primary" onClick={handleIncrease}>
                                <FontAwesomeIcon icon={faPlus} />
                            </Button>
                            <span className="mx-2">{quantity}</span>
                            <Button color="primary" onClick={handleDecrease}>
                                <FontAwesomeIcon icon={faMinus} />
                            </Button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}


export default CardProduct;
