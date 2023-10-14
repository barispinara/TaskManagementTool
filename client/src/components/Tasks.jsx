
import React, { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { useParams } from 'react-router-dom';
import { renderTaskStatus } from './TaskStatusRenderer';
import { TaskStatus } from '../models/TaskStatus';
import { Box, CircularProgress } from '@mui/material';
import { Topbar } from './Topbar';
import { TableGrid } from './TableGrid';
import { createTask, deleteTask, getProjectTasks, updateTask } from '../services/TaskService';

export const Tasks = () => {

    const { projectId } = useParams();
    const loading = useSelector((state) => state.task.isLoading);
    const responseStatus = useSelector((state) => state.task.responseStatus);
    const dispatch = useDispatch();
    const taskList = useSelector((state) => state.task.taskList);

    const columns = [
        {
            field: 'id',
            headerName: 'ID',
            type: 'number',
            minWidth: 30,
            flex: 0.10,
        },
        {
            field: 'taskName',
            headerName: 'Task Name',
            minWidth: 90,
            flex: 0.20,
            editable: true,
        },
        {
            field: 'taskStatus',
            type: 'singleSelect',
            headerName: 'Status',
            renderCell: renderTaskStatus,
            minWidth: 50,
            valueOptions: [
                TaskStatus.TODO,
                TaskStatus.PROGRESS,
                TaskStatus.DONE
            ],
            flex: 0.20,
            editable: true,
        },
        {
            field: 'createdDate',
            headerName: 'Created Date',
            minWidth: 50,
            flex: 0.20,
        },
        {
            field: 'updatedDate',
            headerName: 'Updated Date',
            minWidth: 50,
            flex: 0.20,
        },

    ]

    useEffect(() => {
        getTasks()
    }, []);

    async function getTasks() {
        await dispatch(getProjectTasks(projectId));
    }

    async function saveTask(savedRow){
        const createTaskRequest = {
            taskName: savedRow.taskName,
            projectId: projectId,
        };
        await dispatch(createTask(createTaskRequest));
        getTasks();
    }

    async function removeTask(taskId){
        await dispatch(deleteTask(taskId));
        getTasks();
    }

    async function modifyTask(updatedRow){
        const updateTaskRequest = {
            id: updatedRow.id,
            taskName: updatedRow.taskName,
            taskStatus: updatedRow.taskStatus,
        };
        await dispatch(updateTask(updateTaskRequest));
        getTasks();
    }

    return (
        <Box sx={{ minHeight: '100%' }}>
            <Topbar
                title="Tasks"
                isBackArrowRequested={true}
            />
            {
                (loading === true)
                    ? <CircularProgress />
                    : (loading === false && responseStatus === 200)
                        ? <TableGrid
                            rows={taskList}
                            columns={columns}
                            saveFunction={saveTask}
                            removeFunction={removeTask}
                            updateFunction={modifyTask}
                        />
                        : null
            }
        </Box>
    )
}
