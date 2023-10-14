import { createAsyncThunk } from "@reduxjs/toolkit";
import { AxiosApi } from "./AxiosApi";

const serviceURL = "project"

export const createProject = createAsyncThunk(
    '/new',
    async(projectName, {rejectWithValue}) => {
        try{
            const payload = await AxiosApi.post(
                `${serviceURL}/new` , {
                    projectName
                }
            )
            const data = payload.data;
            const responseStatus = payload.status;
            return {data, responseStatus};
        } catch (error){
            return rejectWithValue(error.response.data)
        }
    }
)

export const getAllProjects = createAsyncThunk(
    '/',
    async(_, {rejectWithValue}) => {
        try{
            const payload = await AxiosApi.get(
                `${serviceURL}`
            )
            const data = payload.data;
            const responseStatus = payload.status;
            return {data, responseStatus};
        } catch(error){
            return rejectWithValue(error.response.data)
        }
    }
)

export const deleteProject = createAsyncThunk(
    '/deleteProject',
    async(projectId, {rejectWithValue}) => {
        try{
            const payload = await AxiosApi.delete(
                `${serviceURL}/${projectId}`
            )
            const data = payload.data;
            const responseStatus = payload.status;
            return {data, responseStatus};
        } catch(error){
            return rejectWithValue(error.response.data)
        }
    }
)

export const updateProject = createAsyncThunk(
    '/updateProject',
    async(updateProjectRequest, {rejectWithValue}) => {
        try{
            const{
                id,
                projectName
            } = updateProjectRequest;
            const payload = await AxiosApi.put(
                `${serviceURL}` , {
                    id,
                    projectName
                }
            )
            const data = payload.data;
            const responseStatus = payload.status;
            return {data, responseStatus};
        } catch(error){
            return rejectWithValue(error.response.data)
        }
    }
)