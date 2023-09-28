import { AppBar, Box, Button, Toolbar, Typography } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
import React from 'react';

export const Topbar = ({ title, onNewClick, onDeleteClick, isSelected}) => {
    return (
        <Box sx={{ flexGrow: 1 }}>
            <AppBar position="static">
                <Toolbar>
                    <Typography variant="subtitle1" component="div" sx={{ flexGrow: 1 }}>
                        {title}
                    </Typography>
                </Toolbar>
            </AppBar>
        </Box>
    )
}
