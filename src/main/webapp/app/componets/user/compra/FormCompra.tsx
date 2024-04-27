import React, { Dispatch, SetStateAction, useState } from 'react';
import { Button, Form, FormGroup, Label, Input } from 'reactstrap';
import { useDispatch } from 'react-redux';
import { createCompra } from 'app/entities/compra/compra.reducer';
import { createEntity } from 'app/entities/datelle-compra/datelle-compra.reducer';
import { AppDispatch, useAppSelector } from 'app/config/store';
import { ICompra } from 'app/shared/model/compra.model';
import dayjs from 'dayjs';
import { getAccount } from 'app/shared/reducers/authentication';
import { IUser } from 'app/shared/model/user.model';
import { IUsuario } from 'app/shared/model/usuario.model';
import { getEntity, getUsuario } from 'app/entities/usuario/usuario.reducer';
import { IDatelleCompra } from 'app/shared/model/datelle-compra.model';
import ProductDetails from './ProductDetails';
import "./productsDetails.css";

interface PropsProduco {
  productos:any;
  isForm:Dispatch<SetStateAction<boolean>>;
}


const FormCompra = ({productos,isForm}:PropsProduco) => {
  const dispatch = useDispatch<AppDispatch>();
  const [values, setValues] = useState({
    fechaCompra: '',
    total: '',
    usuario: '',
  });
  let totalProductos = 0;
  const productoList = useAppSelector(state => state.producto.entities);
  const productosSelecteds = productoList.filter((ele,index) =>{
    return Object.keys(productos).includes(String(ele.id));
  });

  const detallesCompra = productosSelecteds.map((productoSeleccionado) => {
    const productoEnLista = productoList.find((producto) => producto.id === productoSeleccionado.id);
    const cantidad = productos[productoSeleccionado.id];
    const totalProducto = cantidad * productoEnLista.nombre;

    totalProductos += totalProducto;

    const nuevoDetalleCompra: IDatelleCompra = {
      cantidad: cantidad,
      precioUnitario: productoEnLista.nombre,
      totalProducto: totalProducto,
      producto: productoList.find((e) => e.id === productoSeleccionado.id),
    };

    return nuevoDetalleCompra;
  });

  const nuevaCompra: ICompra = {
    fechaCompra: dayjs(),
    total: totalProductos,
  };
 
  const handleSubmit = async (event) => {
    event.preventDefault();
  
    const user = await dispatch(getAccount());
    const { id } = (user.payload as any).data;
    const usuario = await ((await dispatch(getEntity(id))).payload as any).data ;
  
    nuevaCompra['usuario'] = usuario;
  
    try {
      const compra = await dispatch(createCompra(nuevaCompra));
      console.log(compra);
  
      // Recién aquí, después de guardar la compra, podemos asignarla a los detalles de compra
      detallesCompra.map(async(ele)=>{
        ele['compra'] = (compra.payload as any).data;
        const detalleCompraPrueba = await dispatch(createEntity(ele));
      });
  
    } catch (error) {
      console.error('Error al crear la compra:', error);
    }
  };
  

  
    return (
      <Form onSubmit={handleSubmit} >
       {
        
        
        (detallesCompra && detallesCompra.length>0)?
        detallesCompra.map((e,i) =>{

          return (
            <ProductDetails
              name={e.producto.nombre}
              price={e.precioUnitario}
              quantity={e.cantidad}
              total={e.totalProducto}
              key={i}
              src={e.producto.ruta}
            />
          )
        })
        :
        "Parece que no selecciono nada ;("
        
       }

<div className="d-flex justify-content-center">
  <p className="margin-p">Total:</p> 
  <p className="margin-p">{totalProductos}</p> 
</div>



    
        <div className="d-flex justify-content-center mt-4"> {/* Clases de Bootstrap para centrar y añadir margen superior */}
          <Button type="submit" className="me-2">Compra</Button> {/* Clase me-2 para añadir margen a la derecha */}
          <Button onClick={() => isForm(false)}>Volver</Button>
        </div>
      </Form>
    );
};

export default FormCompra;
