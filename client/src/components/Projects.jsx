import React, { useEffect } from 'react'
import { Topbar } from './Topbar'
import { Box, Button, CircularProgress } from '@mui/material'
import { TableGrid } from './TableGrid';
import { useDispatch, useSelector } from 'react-redux';
import { createProject, deleteProject, getAllProjects } from '../services/ProjectService';
import FormatListBulletedIcon from '@mui/icons-material/FormatListBulleted';
import { Navigate, useNavigate } from 'react-router-dom';

export const Projects = () => {

    const loading = useSelector((state) => state.project.isLoading);
    const responseMessage = useSelector((state) => state.project.responseMessage);
    const responseStatus = useSelector((state) => state.project.responseStatus);
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const projectList = useSelector((state) => state.project.projectList);

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
        },
        {
            field: 'tasks',
            headerName: 'Tasks',
            minWidth: 60,
            flex: 0.20,
            renderCell: (params) => (
                <Button startIcon={<FormatListBulletedIcon/>} onClick={() => goTaskPage(params.row.id)}/>
            )
        }
    ]
    
    useEffect(() => {
        console.log("Is it called")
        getProjects();
    }, [])

    async function getProjects() {
        await dispatch(getAllProjects("test"));
    }

    async function saveProject(savedRow) {
        await dispatch(createProject(savedRow['projectName']));
    }

    async function removeProject(projectId){
        await dispatch(deleteProject(projectId));
    }

    const goTaskPage = (id) => {
        navigate(`/${id}`);
    }

    return (
        <Box sx={{ minHeight: '100%' }}>
            <Topbar
                title="Projects"
                isBackArrowRequested={false}
            />
            {
                (loading === true)
                    ? <CircularProgress />
                    : (loading === false && responseStatus === 200)
                        ? <TableGrid
                            rows={projectList}
                            columns={columns}
                            saveFunction={saveProject}
                            removeFunction={removeProject}
                        />
                        : null
            }
        </Box>
    )
}
