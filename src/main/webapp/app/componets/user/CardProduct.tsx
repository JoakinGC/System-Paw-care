import React from "react";
import './user.css';
import { Button } from "reactstrap";


interface PropsCardProduct {
    cardTitle: string;
    cardText: string;
    onClick: () => void;
    isSelected: boolean; // Nueva prop para indicar si el producto está seleccionado
    link:string;
};

const CardProduct = ({ cardTitle, cardText, onClick, isSelected,link}: PropsCardProduct) => {
    // Clase condicional para aplicar estilos si el producto está seleccionado
    const cardClassName = isSelected ? "card card-selected" : "card";

    return (
        <div className="card-wrapper">
            <div className={cardClassName} onClick={() => onClick()}>
                <img src={link} className="card-img-top" alt="..." />
                <div className="card-body">
                    <h5 className="card-text">{cardText}</h5>
                    <p className="card-title">{cardTitle}€</p>
                    
                </div>
            </div>
          
        </div>
    );
}

export default CardProduct;
