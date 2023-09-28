import { Box, Card, Typography } from "@mui/material"
import { Projects } from "../components/Projects"
import { Tasks } from "../components/Tasks"


export const Home = () => {
    const onNewClick = () => {
        console.log("New Button Clicked")
    }

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
            <Projects />
            {/* <Tasks/> */}
        </Card>
    )
}