import { createAsyncThunk } from "@reduxjs/toolkit"
import { AxiosApi } from "./AxiosApi";


const serviceURL = "task"

export const createTask = createAsyncThunk(
    '/new',
    async(createTaskRequest, {rejectWithValue}) => {
        try{
            const{
                projectId,
                taskName
            } = createTaskRequest;
            const payload = await AxiosApi.post(
                `${serviceURL}` , {
                    projectId,
                    taskName
                }
            )
            return payload.data;
        } catch(error){
            return rejectWithValue(error.response.data)
        }
    }
)

export const getProjectTasks = createAsyncThunk(
    '/all/projectId',
    async(projectId, {rejectWithValue}) => {
        try{
            const payload = await AxiosApi.get(
                `${serviceURL}/all/${projectId}`
            )
            return payload.data;
        } catch(error){
            return rejectWithValue(error.response.data);
        }
    }
)

export const deleteTask = createAsyncThunk(
    '/delete/taskId',
    async(taskId, {rejectWithValue}) => {
        try{
            const payload = await AxiosApi.delete(
                `${serviceURL}/${taskId}`
            )
            return payload.data;
        } catch(error){
            return rejectWithValue(error.response.data);
        }
    }
)

export const updateTask = createAsyncThunk(
    '/update/taskId',
    async(updateTaskRequest, {rejectWithValue}) => {
        try{
            const{
                id,
                taskName,
                taskStatus
            } = updateTaskRequest
            const payload = await AxiosApi.put(
                `{$serviceURL}` , {
                    id,
                    taskName,
                    taskStatus
                }
            )
            return payload.data;
        } catch(error){
            return rejectWithValue(error.response.data);
        }
    }
)