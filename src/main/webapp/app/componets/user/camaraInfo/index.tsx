import ErrorBoundaryRoutes from "app/shared/error/error-boundary-routes";
import React from "react";
import { Route } from 'react-router-dom';
import InfoCamara from "./InfoCamara";




const InfoCamRoutes = () =>{

    return(
        <ErrorBoundaryRoutes>
                 <Route index element={<InfoCamara />} />
        </ErrorBoundaryRoutes>
    );
}



export default InfoCamRoutes;

