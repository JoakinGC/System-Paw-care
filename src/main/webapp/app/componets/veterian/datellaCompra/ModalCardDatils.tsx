import { useAppDispatch, useAppSelector } from "app/config/store";
import { getEntity } from "app/entities/producto/producto.reducer";
import { IDatelleCompra } from "app/shared/model/datelle-compra.model";
import { IProducto } from "app/shared/model/producto.model";
import React, { useEffect, useState } from "react";
import CardCompraDetils from "./CardCompraDatils";


const ModalCardDetils = (
    {idCompra,datalleCompraList}:
    {idCompra:number|undefined,
        datalleCompraList:IDatelleCompra[]|null|undefined
    }
) =>{
    const dispatch = useAppDispatch();
    const [filtrados,setFiltrados] = useState<IDatelleCompra[]>([]);
    useEffect(() => {
        if(datalleCompraList && datalleCompraList.length > 0) {
            const fetchData = async () => {
                const filtradasComprasConId = datalleCompraList.filter((c: IDatelleCompra) => {
                    return c.compra && c.compra.id === idCompra;
                });
    
                const filtrados = await Promise.all(filtradasComprasConId.map(async (c: IDatelleCompra) => {
                    if(c.producto && c.producto.id){
                        const producto = ((await dispatch(getEntity(c.producto.id))).payload as any).data as IProducto;
                        return {...c, producto};
                    }
                    return c;
                }));
    
                console.log(filtrados);
                setFiltrados(filtrados);
            };
    
            fetchData();
        }
    }, []);
    

    return(<div className="card-detalle-compra">
        {(filtrados&&filtrados.length>0) ? 
        filtrados.map(c => (<CardCompraDetils
            detalleCompra={c}
        />))  
    :null}
    </div>)
}

export default ModalCardDetils;