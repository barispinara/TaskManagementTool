import axios from 'axios'


export const AxiosApi = axios.create({
    baseURL: process.env.REACT_APP_SERVER_URL || 'http://localhost:8080/',
    headers: {
        'Content-Type': 'application/json',
    }
});