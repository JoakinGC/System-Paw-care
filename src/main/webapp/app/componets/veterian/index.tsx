import ErrorBoundaryRoutes from "app/shared/error/error-boundary-routes";
import React from "react";
import { Route } from 'react-router-dom';
import VeterianMain from "./VeterianMain";

const VeterianRoutes = () =>{

    return(
        <ErrorBoundaryRoutes>
                 <Route index element={<VeterianMain />} />
        </ErrorBoundaryRoutes>
    );
}



export default VeterianRoutes;

