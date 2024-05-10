import ErrorBoundaryRoutes from "app/shared/error/error-boundary-routes";
import React from "react";
import { Route } from 'react-router-dom';
import CitasCalendario from "./CitasCalendario";




const RoutesCalendarCita = () =>{

    return(
        <ErrorBoundaryRoutes>
                 <Route index element={<CitasCalendario />} />
        </ErrorBoundaryRoutes>
    );
}



export default RoutesCalendarCita

