import ErrorBoundaryRoutes from "app/shared/error/error-boundary-routes";
import React from "react";
import { Route } from 'react-router-dom';
import InfoCamara from "./InfoCamara";
import CamaraUso from "./Camara";




const InfoCamRoutes = () =>{

    return(
        <ErrorBoundaryRoutes>
                 <Route index element={<InfoCamara />} />
                 <Route path="camara" element={<CamaraUso />} />
        </ErrorBoundaryRoutes>
    );
}



export default InfoCamRoutes;

