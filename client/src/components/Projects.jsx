import React, { useEffect } from 'react'
import { Topbar } from './Topbar'
import { Box, CircularProgress } from '@mui/material'
import { TableGrid } from './TableGrid';
import { useDispatch, useSelector } from 'react-redux';
import { getAllProjects } from '../services/ProjectService';

export const Projects = () => {
    
    const loading = useSelector((state) => state.project.isLoading);
    const responseMessage = useSelector((state) => state.project.responseMessage);
    const responseStatus = useSelector((state) => state.project.responseStatus);
    const dispatch = useDispatch();
    const projectList = useSelector((state) => state.project.projectList);

    useEffect(() => {
        getProjects();
    },[])

    async function getProjects() {
        await dispatch(getAllProjects("test"));
    }
    
    const columns = [
        {
            field: 'id',
            headerName: 'ID',
            type: 'number',
            minWidth: 30,
            flex: 0.10,
        },
        {
            field: 'projectName',
            headerName: 'Project Name',
            minWidth: 80,
            flex: 0.20,
            editable: true,
        },
        {
            field: 'totalTasks',
            headerName: 'Total Tasks',
            minWidth: 60,
            flex: 0.20
        },
        {
            field: 'completedTasks',
            headerName: 'Completed Tasks',
            minWidth: 60,
            flex: 0.20
        },
        {
            field: 'createdDate',
            headerName: 'Created Date',
            minWidth: 60,
            flex: 0.20
        }
    ]

    return (
        <Box sx={{minHeight: '100%'}}>
            <Topbar
                title="Projects"
                isSelected={false}
            />
            {
                (loading === true) 
                ? <CircularProgress/>
                : (loading === false && responseStatus === 200)
                ? <TableGrid rows={projectList} columns={columns}/>
                : null
            }
        </Box>
    )
}
