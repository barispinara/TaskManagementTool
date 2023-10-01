import React from 'react';
import { Chip } from "@mui/material";
import InfoIcon from '@mui/icons-material/Info';
import AutorenewIcon from '@mui/icons-material/Autorenew';
import DoneIcon from '@mui/icons-material/Done';
import styled from "@emotion/styled";



const StyledChip = styled(Chip)(() => ({
    justifyContent: 'left',
    '& .icon': {
        color: 'inherit',
    },
    '&.TODO': {
        color: '#2196f3',
        border: '1px solid'
    },
    '&.PROGRESS': {
        color: '#ff9800',
        border: '1px solid'
    },
    '&.DONE': {
        color: 'green',
        border: '1px solid'
    }
}));


const Status = React.memo((props) => {
    const {taskStatus} = props;

    let icon = null;
    if (taskStatus === 'TODO'){
        icon = <InfoIcon className="icon"/>;
    }
    else if (taskStatus === 'PROGRESS'){
        icon = <AutorenewIcon className="icon"/>;
    }
    else if (taskStatus === 'DONE'){
        icon = <DoneIcon className="icon"/>
    }

    return(
        <StyledChip className={taskStatus} icon={icon} size="small" label={taskStatus} variant="outlined"/>
    )
})

export function renderTaskStatus(params){
    if (params.value == null){
        return '';
    }

    return <Status taskStatus={params.value}/>
}