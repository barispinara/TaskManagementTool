import { createSlice } from "@reduxjs/toolkit"
import { createProject, deleteProject, getAllProjects, updateProject } from "../../services/ProjectService"


const initialState = {
    projectList: [],
    isLoading: false,
    responseStatus: 0,
    responseMessage: ''
}

export const projectSlice = createSlice({
    name: 'project',
    initialState,
    reducers:{

    },
    extraReducers:{
        [getAllProjects.pending] : (state) => {
            state.isLoading = true;
        },
        [getAllProjects.fulfilled] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = 200;
            state.projectList = payload;
        },
        [getAllProjects.rejected] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = 400;
        },
        [createProject.pending] : (state) => {
            state.isLoading = true;
        },
        [createProject.fulfilled] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = 200;
            state.projectList = payload;
        },
        [createProject.rejected] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = 400;
        },
        [deleteProject.pending] : (state) => {
            state.isLoading = true;
        },
        [deleteProject.fulfilled] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = 200;
        },
        [deleteProject.rejected] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = 400;
        },
        [updateProject.pending] : (state) => {
            state.isLoading = true;
        },
        [updateProject.fulfilled] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = 200;
        },
        [updateProject.rejected] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = 400;
        }
    }
})

export const {} = projectSlice.actions;

export default projectSlice.reducer;