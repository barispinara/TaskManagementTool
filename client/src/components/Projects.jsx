import React, { useEffect, useState } from 'react'
import { Topbar } from './Topbar'
import { Box, Button, CircularProgress } from '@mui/material'
import { TableGrid } from './TableGrid';
import { useDispatch, useSelector } from 'react-redux';
import { createProject, deleteProject, getAllProjects, updateProject } from '../services/ProjectService';
import FormatListBulletedIcon from '@mui/icons-material/FormatListBulleted';
import { Navigate, useNavigate } from 'react-router-dom';

export const Projects = () => {

    const loading = useSelector((state) => state.project.isLoading);
    const responseStatus = useSelector((state) => state.project.responseStatus);
    const responseProject = useSelector((state) => state.project.project);
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
        getProjects();
    }, [])

    async function getProjects() {
        await dispatch(getAllProjects());
    }

    async function saveProject(savedRow) {
        await dispatch(createProject(savedRow['projectName']));
        getProjects();
    }

    async function removeProject(projectId){
        await dispatch(deleteProject(projectId));
        getProjects();
    }

    async function modifyProject(updatedRow){
        const updateProjectRequest = {
            id: updatedRow.id,
            projectName: updatedRow.projectName,
        };
        await dispatch(updateProject(updateProjectRequest));
        getProjects();
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
                            updateFunction={modifyProject}
                        />
                        : null
            }
        </Box>
    )
}
