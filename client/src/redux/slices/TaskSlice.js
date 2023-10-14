import { createSlice } from "@reduxjs/toolkit"
import { createTask, deleteTask, getProjectTasks, updateTask } from "../../services/TaskService"


const initialState = {
    taskList : [],
    isLoading: false,
    responseStatus: 0,
}

export const taskSlice = createSlice({
    name: 'task',
    initialState,
    reducers:{

    },
    extraReducers:{
        [createTask.pending] : (state) => {
            state.isLoading = true;
        },
        [createTask.fulfilled] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = payload.responseStatus;
        },
        [createTask.rejected] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = payload.responseStatus;
        },
        [getProjectTasks.pending] : (state) => {
            state.isLoading = true;
        },
        [getProjectTasks.fulfilled] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = payload.responseStatus;
            state.taskList = payload.data;
        },
        [getProjectTasks.rejected] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = payload.responseStatus;
        },
        [deleteTask.pending] : (state) => {
            state.isLoading = true;
        },
        [deleteTask.fulfilled] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = payload.responseStatus;
        },
        [deleteTask.rejected] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = payload.responseStatus;
        },
        [updateTask.pending] : (state) => {
            state.isLoading = true;
        },
        [updateTask.fulfilled]: (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = payload.responseStatus;
        },
        [updateTask.rejected] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = payload.responseStatus;
        }
    }
})

export const {} = taskSlice.actions;

export default taskSlice.reducer;