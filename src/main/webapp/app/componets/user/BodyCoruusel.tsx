import React from 'react';
import CardProduct from './CardProduct';
import './user.css';

const BodyCorousel = () => {
    return (
        <div className="card-container">
            <CardProduct
                cardText='Pepe'
                cardTitle='Pepona'
                onClick={() => console.log("prueba")}
            />
            <CardProduct
                cardText='Lorem ipsum dolor sit amet consectetur, adipisicing elit. Id veniam nesciunt dicta distinctio, molestiae, excepturi voluptatem a temporibus quia exercitationem saepe iure ducimus voluptatum corrupti perferendis. Perferendis vel vero quas?'
                cardTitle='Lore'
                onClick={() => console.log("prueba")}
            />
            <CardProduct
                cardText='Pepe'
                cardTitle='Pepona'
                onClick={() => console.log("prueba")}
            />
            <CardProduct
                cardText='Pepe'
                cardTitle='Pepona'
                onClick={() => console.log("prueba")}
            />
            <CardProduct
                cardText='Pepe'
                cardTitle='Pepona'
                onClick={() => console.log("prueba")}
            />
            <CardProduct
                cardText='Pepe'
                cardTitle='Pepona'
                onClick={() => console.log("prueba")}
            />
            <CardProduct
                cardText='Pepe'
                cardTitle='Pepona'
                onClick={() => console.log("prueba")}
            />
        </div>
    );
}

export default BodyCorousel;
