import { Box, ThemeProvider, createTheme } from "@mui/material";
import { Home } from "./pages/Home";
import { Navigate, Route, Router, Routes } from "react-router-dom";

const darkTheme = createTheme({
  palette: {
    mode: 'dark'
  }
})


function App() {
  return (
    <ThemeProvider theme={darkTheme}>
      <Box sx={{
        display:'flex', 
        height: '100vh',
        backgroundColor: '#212121'
      }}>
        <Home />
      </Box>
    </ThemeProvider>


  );
}

export default App;
