import ErrorBoundaryRoutes from "app/shared/error/error-boundary-routes";
import React from "react";
import { Route } from 'react-router-dom';
import CitasCalendario from "./CitasCalendario";
import AddCita from "./AddCita";



const RoutesCalendarCita = () =>{

    return(
        <ErrorBoundaryRoutes>
                 <Route index element={<CitasCalendario />} />
                 <Route path="newCita" element={<AddCita />} />
        </ErrorBoundaryRoutes>
    );
}



export default RoutesCalendarCita

