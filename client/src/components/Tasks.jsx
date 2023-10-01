
import React, { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { useParams } from 'react-router-dom';
import { renderTaskStatus } from './TaskStatusRenderer';
import { TaskStatus } from '../models/TaskStatus';
import { Box, CircularProgress } from '@mui/material';
import { Topbar } from './Topbar';
import { TableGrid } from './TableGrid';
import { getProjectTasks } from '../services/TaskService';

export const Tasks = () => {

    const { projectId } = useParams();
    const loading = useSelector((state) => state.task.isLoading);
    const responseMessage = useSelector((state) => state.task.responseMessage);
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
            minWidth: 120,
            flex: 0.30,
            editable: true,
        },
        {
            field: 'taskStatus',
            type: 'singleSelect',
            headerName: 'Status',
            renderCell: renderTaskStatus,
            minWidth: 60,
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
            minWidth: 60,
            flex: 0.20,
        },
        {
            field: 'updatedDate',
            headerName: 'Updated Date',
            minWidth: 60,
            flex: 0.20,
        },

    ]

    useEffect(() => {
        getTasks(projectId)
        console.log(taskList)
    }, []);

    async function getTasks(id) {
        await dispatch(getProjectTasks(id));
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
                        />
                        : null
            }
        </Box>
    )
}
