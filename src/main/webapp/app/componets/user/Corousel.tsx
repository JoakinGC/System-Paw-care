import React from 'react';
import CardProduct from './CardProduct';
import './user.css';
import BodyCorousel from './BodyCoruusel';

const Corusel = () =>{
    return (
        <div style={{display:'flex',flexDirection:'column'}}>
            <BodyCorousel/>
        </div>
    );
}

export default Corusel;