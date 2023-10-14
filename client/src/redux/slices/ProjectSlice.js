import { createSlice } from "@reduxjs/toolkit"
import { createProject, deleteProject, getAllProjects, updateProject } from "../../services/ProjectService"


const initialState = {
    projectList: [],
    isLoading: false,
    responseStatus: 0,
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
            state.responseStatus = payload.responseStatus;
            state.projectList = payload.data;
        },
        [getAllProjects.rejected] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = payload.responseStatus;
        },
        [createProject.pending] : (state) => {
            state.isLoading = true;
        },
        [createProject.fulfilled] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = payload.responseStatus;
        },
        [createProject.rejected] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = payload.responseStatus;
        },
        [deleteProject.pending] : (state) => {
            state.isLoading = true;
        },
        [deleteProject.fulfilled] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = payload.responseStatus;
        },
        [deleteProject.rejected] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = payload.responseStatus;
        },
        [updateProject.pending] : (state) => {
            state.isLoading = true;
        },
        [updateProject.fulfilled] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = payload.responseStatus;
        },
        [updateProject.rejected] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = payload.responseStatus;
        }
    }
})

export const {} = projectSlice.actions;

export default projectSlice.reducer;