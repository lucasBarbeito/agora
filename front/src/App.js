import {Box, Button, Container, Grid} from '@material-ui/core';
import './App.css';
import {Component} from 'react';
import Navbar from './common/Navbar/Navbar';

class App extends Component {
  render() {
    return (
      <>
        <Navbar name="User name" loggedIn={true}/>
        <Container style={{display: "flex", flexDirection: "column"}}>
          <h1>Agora</h1>
          <Container style={{maxWidth: "600px"}}>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <Box style={{display: "flex", flexDirection: "column"}}>
                  <Button style={{alignSelf: "center"}} variant="contained" color="primary" onClick={() => alert("Hello!")}>
                    Say hi
                  </Button>
                </Box>
              </Grid>
            </Grid>
          </Container>
        </Container>
      </>
    );
  }
}

export default App;
