import React, { Dispatch, SetStateAction, useEffect, useState } from 'react';
import { Button, Form } from 'reactstrap';
import { PayPalButtons, PayPalScriptProvider } from '@paypal/react-paypal-js';
import { useDispatch } from 'react-redux';
import { createCompra } from 'app/entities/compra/compra.reducer';
import { createEntity } from 'app/entities/datelle-compra/datelle-compra.reducer';
import { AppDispatch, useAppSelector } from 'app/config/store';
import { ICompra } from 'app/shared/model/compra.model';
import { getEntities, getEntity, getUsuario } from 'app/entities/usuario/usuario.reducer';
import dayjs from 'dayjs';
import { getAccount } from 'app/shared/reducers/authentication';
import { IDatelleCompra } from 'app/shared/model/datelle-compra.model';
import ProductDetails from './ProductDetails';
import "./productsDetails.css";


interface PropsProduco {
  productos: any;
  isForm: Dispatch<SetStateAction<boolean>>;
}

const FormCompra = ({ productos, isForm }: PropsProduco) => {
  const dispatch = useDispatch<AppDispatch>();
  const [paypalOrderId, setPaypalOrderId] = useState(null);
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
  const handleApprove = async (data: any, actions: any, totalProductos: number) => {
    try {
      // Realizar la compra y guardar los detalles de compra
      const user = await dispatch(getAccount());
      const { id } = (user.payload as any).data;
      const allUsuarios = await dispatch(getEntities({}));
      const usuarioActual = (allUsuarios.payload as any).data.filter((e, i) => e.user.id == id);
  
      const nuevaCompra: ICompra = {
        fechaCompra: dayjs(),
        total: totalProductos,
        usuario: usuarioActual[0]
      };
  
      const compra = await dispatch(createCompra(nuevaCompra));
  
      const detallesCompra = productosSelecteds.map((productoSeleccionado) => {
        const productoEnLista = productoList.find((producto) => producto.id === productoSeleccionado.id);
        const cantidad = productos[productoSeleccionado.id];
        const totalProducto = cantidad * productoEnLista.nombre;
  
        const nuevoDetalleCompra: IDatelleCompra = {
          cantidad: cantidad,
          precioUnitario: productoEnLista.nombre,
          totalProducto: totalProducto,
          producto: productoList.find((e) => e.id === productoSeleccionado.id),
          compra: (compra.payload as any).data
        };
  
        return nuevoDetalleCompra;
      });
  
      detallesCompra.forEach(async (ele) => {
        await dispatch(createEntity(ele));
      });
  
      // Obtener el ID de la compra y establecerlo como paypalOrderId
      const orderId = (compra.payload as any).data.id;
      setPaypalOrderId(orderId);
  
      // Marcar el formulario como no visible
      isForm(false);
    } catch (error) {
      console.error('Error al capturar el pago:', error);
      // Manejo de errores
      throw error;
    }
  };
  
  
  const handleSubmit = async (event) => {
    event.preventDefault();
  
    const user = await dispatch(getAccount());
    const { id } = (user.payload as any).data;
    const allUsarios = await dispatch(getEntities({}))
    const usuarioActual = (allUsarios.payload as any).data.filter((e,i) => 
    e.user.id==id);
    
  
    nuevaCompra['usuario'] = usuarioActual[0];
  
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
    isForm(false);
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
          <PayPalScriptProvider options={{ clientId: 'ASGgebZ+cJw9pRVtxCGTGJFgp71FbyiY-WSSANZlkrvfmZHW6IP28wEmRknua-9hU_nvQlA84BGPTGg0D' }}>
      <PayPalButtons onApprove={(data, actions) => handleApprove(data, actions, totalProductos)} />
      </PayPalScriptProvider>
        </div>
      </Form>
    );
};
export default FormCompra;
