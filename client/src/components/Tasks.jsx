import React, { useEffect, useState } from 'react'
import { Topbar } from './Topbar'
import { Box, Button, Divider, IconButton, List, ListItem, ListItemButton, ListItemText } from '@mui/material'
import DeleteIcon from '@mui/icons-material/Delete';
import { DataGrid, GridActionsCellItem, GridRowEditStartReasons, GridRowModes, GridToolbarContainer } from '@mui/x-data-grid';
import AddIcon from '@mui/icons-material/Add';
import SaveIcon from '@mui/icons-material/Save';
import CancelIcon from '@mui/icons-material/Close';
import EditIcon from '@mui/icons-material/Edit';
import ViewListIcon from '@mui/icons-material/ViewList';
import { renderTaskStatus } from './TaskStatusRenderer';


const TaskStatus = {
    TODO: 'TODO',
    PROGRESS: 'PROGRESS',
    DONE: 'DONE'
}

const tmpRows = [
    {id: 1, taskName: 'test', taskStatus:TaskStatus.TODO,createdDate: '2020-1-1', updatedDate: '2020-1-1'},
    {id: 2, taskName: 'test2', taskStatus:TaskStatus.PROGRESS,createdDate: '2020-1-1', updatedDate: '2020-1-1'},
    {id: 3, taskName: 'test3', taskStatus:TaskStatus.PROGRESS,createdDate: '2020-1-1', updatedDate: '2020-1-1'},
    {id: 4, taskName: 'test4', taskStatus:TaskStatus.DONE,createdDate: '2020-1-1', updatedDate: '2020-1-1'},
    {id: 5, taskName: 'test5', taskStatus:TaskStatus.DONE,createdDate: '2020-1-1', updatedDate: '2020-1-1'},
    {id: 6, taskName: 'test6', taskStatus:TaskStatus.DONE,createdDate: '2020-1-1', updatedDate: '2020-1-1'},
    {id: 7, taskName: 'test7', taskStatus:TaskStatus.DONE,createdDate: '2020-1-1', updatedDate: '2020-1-1'}
]

export const Tasks = () => {

    const handleSaveClick = (id) => () => {
        setRowModesModel({ ...rowModesModel, [id]: { mode: GridRowModes.View } });
    };

    const handleDeleteClick = (id) => () => {
        setRows(rows.filter((row) => row.id !== id));
    };

    const handleCancelClick = (id) => () => {
        setRowModesModel({
            ...rowModesModel,
            [id]: { mode: GridRowModes.View, ignoreModifications: true },
        });

        const editedRow = rows.find((row) => row.id === id);
        if (editedRow.isNew) {
            setRows(rows.filter((row) => row.id !== id));
        }
    };

    const processRowUpdate = (newRow) => {
        const updatedRow = { ...newRow, isNew: false };
        setRows(rows.map((row) => (row.id === newRow.id ? updatedRow : row)));
        return updatedRow;
    };

    const handleRowModesModelChange = (newRowModesModel) => {
        setRowModesModel(newRowModesModel);
    };
    
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
        {
            field: 'actions',
            type: 'actions',
            headerName: 'Actions',
            width: 80,
            flex: 0.30,
            cellClassName: 'actions',
            getActions: ({ id }) => {
                const isInEditMode = rowModesModel[id]?.mode === GridRowModes.Edit;

                if (isInEditMode) {
                    return [
                        <GridActionsCellItem
                            icon={<SaveIcon />}
                            label="Save"
                            sx={{
                                color: 'primary.main',
                            }}
                            onClick={handleSaveClick(id)}
                            title="Save changes"
                        />,
                        <GridActionsCellItem
                            icon={<CancelIcon />}
                            label="Cancel"
                            className="textPrimary"
                            onClick={handleCancelClick(id)}
                            color="inherit"
                            title="Cancel changes"
                        />,
                    ];
                }
                return [
                    <GridActionsCellItem
                        icon={<EditIcon />}
                        label="Edit"
                        className="textPrimary"
                        onClick={handleEditClick(id)}
                        color="inherit"
                        title="Edit project"
                    />,
                    <GridActionsCellItem
                        icon={<DeleteIcon />}
                        label="Delete"
                        onClick={handleDeleteClick(id)}
                        color="inherit"
                        title="Delete project"
                    />
                ];
            }
        }
    ]

    const [rowModesModel, setRowModesModel] = useState({});
    const [rows, setRows] = useState(tmpRows);

    const handleRowEditStop = (params, event) => {
        if (params.reason === GridRowEditStartReasons.rowFocusOut) {
            event.defaultMuiPrevented = true;
        }
    }

    const handleEditClick = (id) => () => {
        setRowModesModel({ ...rowModesModel, [id]: { mode: GridRowModes.Edit } });
    };

    function Toolbar(props) {
        const { setRows, setRowModesModel } = props;

        const handleAddClick = () => {
            const id = 8
            setRows((oldRows) => [...oldRows,  {id: 8, taskName: 'test8', taskStatus:TaskStatus.DONE,createdDate: '2020-1-1', updatedDate: '2020-1-1'}]);
            setRowModesModel((oldModel) => ({
                ...oldModel,
                [id]: { mode: GridRowModes.Edit, fieldToFocus: 'taskName' },
            }));
        };

        return (
            <GridToolbarContainer>
                <Button color='primary' startIcon={<AddIcon />} onClick={handleAddClick}>
                    Add Task
                </Button>
            </GridToolbarContainer>
        )

    }


    return (
        <Box>
            <Topbar
                title="Tasks"
            />
            <DataGrid
                rows={rows}
                columns={columns}
                initialState={{
                    pagination: {
                        paginationModel: { page: 0, pageSize: 5 },
                    }
                }}
                pageSizeOptions={[5, 10]}
                editMode='row'
                rowModesModel={rowModesModel}
                onRowModesModelChange={handleRowModesModelChange}
                onRowEditStop={handleRowEditStop}
                processRowUpdate={processRowUpdate}
                slots={{
                    toolbar: Toolbar,
                }}
                slotProps={{
                    toolbar: { setRows, setRowModesModel }
                }}
            />
        </Box>
    )
}
