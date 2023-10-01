import React, { useEffect, useState } from 'react'
import {
    DataGrid,
    GridActionsCellItem,
    GridRowEditStopReasons,
    GridRowModes,
    GridToolbarContainer
} from '@mui/x-data-grid';
import AddIcon from '@mui/icons-material/Add';
import SaveIcon from '@mui/icons-material/Save';
import CancelIcon from '@mui/icons-material/Close';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { Button } from '@mui/material';

export const TableGrid = (props) => {
    const [rowModesModel, setRowModesModel] = useState({});
    const [rows, setRows] = useState(props.rows);
    const genericColumns = props.columns;
    const {saveFunction, removeFunction} = props;

    const staticColumns = [
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

    function EditToolbar(props){
        const {setRows, setRowModesModel} = props;

        const handleClick = () => {
            const id = -1;
            setRows((oldRows) => [...oldRows, {id:id, isNew:true}]);
            setRowModesModel((oldModel) => ({
                ...oldModel,
                [id]: {mode: GridRowModes.Edit, fieldToFocus: genericColumns[1]['field']},
            }))
        }

        return(
            <GridToolbarContainer>
                <Button color="primary" startIcon={<AddIcon/>} onClick={handleClick}>
                    Add
                </Button>
            </GridToolbarContainer>
        )
    }

    const handleSaveClick = (id) => () => {
        setRowModesModel({
            ...rowModesModel,
            [id]: { mode: GridRowModes.View }
        });
    }

    const handleDeleteClick = (id) => () => {
        setRows(rows.filter((row) => row.id !== id));
    }

    const handleCancelClick = (id) => () => {
        setRowModesModel({
            ...rowModesModel,
            [id]: { mode: GridRowModes.View, ignoreModifications: true },
        })

        const editedRow = rows.find((row) => row.id === id);
        if (editedRow.isNew) {
            setRows(rows.filter((row) => row.id !== id));
        }
    }

    const processRowUpdate = (newRow) => {
        if(newRow.hasOwnProperty('isNew') && newRow['isNew'] == true){
            saveFunction(newRow)
        }
        const updatedRow = { ...newRow, isNew: false, id: newRow.id };
        setRows(rows.map((row) => (row.id === newRow.id ? updatedRow : row)));
        return updatedRow;
    }

    const handleRowModesModelChange = (newRowModesModel) => {
        setRowModesModel(newRowModesModel);
    }

    const handleRowEditStop = (params, event) => {
        if (params.reason === GridRowEditStopReasons.rowFocusOut) {
            event.defaultMuiPrevented = true;
        }
    }

    const handleEditClick = (id) => () => {
        setRowModesModel({ ...rowModesModel, [id]: { mode: GridRowModes.Edit } });
    }

    return (
        <DataGrid
            rows={rows}
            columns={[...genericColumns, ...staticColumns]}
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
            sx={{
                boxShadow: 1,
                border: 1,
                borderColor: 'white',
                height: '100%'
            }}
            slots={{
                toolbar: EditToolbar,
            }}
            slotProps={{
                toolbar: {setRows, setRowModesModel},
            }}

        />
    )
}
