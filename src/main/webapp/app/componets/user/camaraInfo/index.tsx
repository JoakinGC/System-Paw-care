import ErrorBoundaryRoutes from "app/shared/error/error-boundary-routes";
import React from "react";
import { Route } from 'react-router-dom';
import InfoCamara from "./InfoCamara";
import CamaraUser from "./Camara";
import CamaraAnimal from "./CamaraAnimal";




const InfoCamRoutes = () =>{

    return(
        <ErrorBoundaryRoutes>
                 <Route index element={<InfoCamara />} />
                 <Route path="camara" element={<CamaraUser />} />
                 <Route path="camaraAnimal" element={<CamaraAnimal />} />
        </ErrorBoundaryRoutes>
    );
}



export default InfoCamRoutes;

