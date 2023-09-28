import { configureStore } from "@reduxjs/toolkit";
import projectReducer from "../slices/ProjectSlice";


export const store = configureStore({
    reducer:{
        project : projectReducer
    },
})