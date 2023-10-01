import { configureStore } from "@reduxjs/toolkit";
import projectReducer from "../slices/ProjectSlice";
import taskReducer from "../slices/TaskSlice";


export const store = configureStore({
    reducer:{
        project : projectReducer,
        task: taskReducer,
    },
})