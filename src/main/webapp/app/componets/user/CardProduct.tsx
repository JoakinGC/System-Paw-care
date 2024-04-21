import React from "react";
import './user.css';


interface PropsCardProduct {
    cardTitle:string;
    cardText:string;
    onClick:() => void;
};

const CardProduct = ({cardTitle,cardText,onClick}:PropsCardProduct) =>{
    return(
        <div className="card-wrapper" >
            <div className="card" onClick={() => onClick()}>
                <img src="#" className="card-img-top" alt="..." />
                <div className="card-body">
                    <h5 className="card-title">{cardTitle}</h5>
                    <p className="card-text">{cardText}</p>
                </div>
            </div>
        </div>
    );
}

export default CardProduct;