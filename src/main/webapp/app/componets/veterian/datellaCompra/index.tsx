import Routes from "app/entities/routes";
import ErrorBoundaryRoutes from "app/shared/error/error-boundary-routes";
import React from "react";
import { Route } from "react-router";
import DetalleCompraVeterianAndEstilis from "./DatelleCompra";

const RouterDatelleCompraVeterian = ()=>{
    return(
        <ErrorBoundaryRoutes>
            <Route index element={<DetalleCompraVeterianAndEstilis/>}/>
        </ErrorBoundaryRoutes>
    )
}

export default RouterDatelleCompraVeterian;