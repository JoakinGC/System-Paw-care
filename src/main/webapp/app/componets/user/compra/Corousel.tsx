import React, { useState } from 'react';
import CardProduct from './CardProduct';
import './user.css';
import BodyCorousel from './BodyCoruusel';
import FormCompra from './FormCompra';


function checkNonZeroValues(obj) {
    for (let key in obj) {
        if (obj.hasOwnProperty(key) && obj[key] !== 0) {
            return true; // Si encuentra un valor distinto de cero, devuelve true
        }
    }
    return false; // Si todos los valores son cero, devuelve false
}

const Corusel = () =>{
    const [form,setForm] = useState<boolean>(false)
    const [productosSelected,setProductosSelect]= useState<{}>({})

    

    console.log(checkNonZeroValues(productosSelected));
    console.log(productosSelected);
    
    return (
        <div style={{display:'flex',flexDirection:'column'}}>
            {(form&&checkNonZeroValues(productosSelected)) ? (<FormCompra
                productos={productosSelected}
                isForm={setForm}
            />)
            :
            
            (<BodyCorousel 
                isForm={setForm}
                setProductos={setProductosSelect}
            />)
        }

        </div>
    );
}

export default Corusel;