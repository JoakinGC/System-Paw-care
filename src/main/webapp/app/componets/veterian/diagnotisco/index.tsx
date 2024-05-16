import Routes from "app/entities/routes";
import ErrorBoundaryRoutes from "app/shared/error/error-boundary-routes";
import React from "react";
import { Route } from "react-router";
import DiagnostList from "./DianostList";


const DiagnostRoutes = ()=>{
    return(
        <ErrorBoundaryRoutes>
            <Route index element={<DiagnostList/>}/>
        </ErrorBoundaryRoutes>
    )
}

export default DiagnostRoutes;