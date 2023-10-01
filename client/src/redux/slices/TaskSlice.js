import { createSlice } from "@reduxjs/toolkit"
import { createTask, deleteTask, getProjectTasks, updateTask } from "../../services/TaskService"


const initialState = {
    taskList : [],
    isLoading: false,
    responseStatus: 0,
    responseMessage: ''
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
            state.responseStatus = 200;
        },
        [createTask.rejected] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = 400;
        },
        [getProjectTasks.pending] : (state) => {
            state.isLoading = true;
        },
        [getProjectTasks.fulfilled] : (state, {payload}) => {
            state.isLoading = false;
            state.taskList = payload;
            state.responseStatus = 200;
        },
        [getProjectTasks.rejected] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = 400;
        },
        [deleteTask.pending] : (state, {payload}) => {
            state.isLoading = true;
        },
        [deleteTask.fulfilled] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = 200;
        },
        [deleteTask.rejected] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = 400;
        },
        [updateTask.pending] : (state) => {
            state.isLoading = true;
        },
        [updateTask.fulfilled]: (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = 200;
        },
        [updateTask.rejected] : (state, {payload}) => {
            state.isLoading = false;
            state.responseStatus = 400;
        }
    }
})

export const {} = taskSlice.actions;

export default taskSlice.reducer;