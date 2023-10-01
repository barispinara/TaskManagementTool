import { AppBar, Box, Button, Toolbar, Typography } from '@mui/material';
import KeyboardReturnIcon from '@mui/icons-material/KeyboardReturn';
import React from 'react';
import { useNavigate } from 'react-router-dom';

export const Topbar = ({ title, isBackArrowRequested }) => {

    const navigate = useNavigate();

    const goBack = () => {
        navigate("/");
    }

    return (
        <Box sx={{ flexGrow: 1 }}>
            <AppBar position="static">
                <Toolbar>
                    {(isBackArrowRequested === true)
                        ? <Button color="primary" startIcon={<KeyboardReturnIcon />} onClick={goBack} />
                        : null
                    }
                    <Typography variant="subtitle1" component="div" sx={{ flexGrow: 1 }}>
                        {title}
                    </Typography>
                </Toolbar>
            </AppBar>
        </Box>
    )
}
