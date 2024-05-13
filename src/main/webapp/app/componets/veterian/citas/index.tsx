import ErrorBoundaryRoutes from "app/shared/error/error-boundary-routes";
import React from "react";
import { Route } from 'react-router-dom';
import VeterianMain from "./VeterianMain";
import FormVeterian from "./FormVeterian";

const VeterianRoutes = () =>{

    return(
        <ErrorBoundaryRoutes>
                 <Route index element={<VeterianMain />} />
                 <Route path="new" element={<FormVeterian />} />
        </ErrorBoundaryRoutes>
    );
}



export default VeterianRoutes;


