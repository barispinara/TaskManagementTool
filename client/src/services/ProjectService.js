import { createAsyncThunk } from "@reduxjs/toolkit";
import { AxiosApi } from "./AxiosApi";

const serviceURL = "project"

export const createProject = createAsyncThunk(
    '/new',
    async(projectName, {rejectWithValue}) => {
        try{
            console.log("Service " , projectName)
            const payload = await AxiosApi.post(
                `${serviceURL}/new` , {
                    projectName
                }
            )
            return payload.data;
        } catch (error){
            return rejectWithValue(error.response.data)
        }
    }
)

export const getAllProjects = createAsyncThunk(
    '/',
    async(testValue, {rejectWithValue}) => {
        try{
            const payload = await AxiosApi.get(
                `${serviceURL}`
            )
            return payload.data;
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
            return payload.data;
        } catch(error){
            return rejectWithValue(error.response.data)
        }
    }
)

export const updateProject = createAsyncThunk(
    '/updateProject',
    async(updateRequest, {rejectWithValue}) => {
        try{
            const{
                id,
                projectName
            } = updateRequest;
            const payload = await AxiosApi.put(
                `${serviceURL}` , {
                    id,
                    projectName
                }
            )
            return payload.data;
        } catch(error){
            return rejectWithValue(error.response.data)
        }
    }
)