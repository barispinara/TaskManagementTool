import { Box, Card, Typography } from "@mui/material"
import { Projects } from "../components/Projects"
import { BrowserRouter, Navigate, Route, Router, Routes } from "react-router-dom"
import { Tasks } from "../components/Tasks"


export const Home = () => {
    return (
        <Card
            sx={{
                width: '75%',
                height: '75%',
                margin: 'auto',
                border: '1px solid #e0e0e0',
                borderRadius: '5px',
                boxShadow: '0px 2px 4px rgba(0,0,0,0.1)'
            }}
        >
            <Box
                sx={{
                    width: "100%",
                    textAlign: "center",
                    borderBottom: '1px solid #e0e0e0',
                }}

            >
                <Typography variant="h6" sx={{ margin: 1 }}>
                    Task Management Tool
                </Typography>
            </Box>
            <BrowserRouter>
                <Routes>
                    <Route exact path="/" element={<Projects/>}/>
                    <Route path="*" element={<Navigate to="/" replace/>}/>
                    <Route path="/:projectId" element={<Tasks/>}/>
                </Routes>
            </BrowserRouter>
        </Card>
    )
}