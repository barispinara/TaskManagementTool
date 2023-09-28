import { Box, ThemeProvider, createTheme } from "@mui/material";
import { Home } from "./pages/Home";

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
