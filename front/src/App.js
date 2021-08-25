import {Box, Button, Container, Grid} from '@material-ui/core';
import './App.css';
import {Component} from 'react';
import Navbar from './common/Navbar/Navbar';
import ErrorNotFound from './Screens/Error/ErrorNotFound';

class App extends Component {
  render() {
    return (
      <>
        <Navbar name="User name" loggedIn={true}/>
        <Container style={{display: "flex", flexDirection: "column"}}>
        <Container style={{maxWidth: "600px"}}>
          <Grid container spacing={2}>
            <ErrorNotFound />
          </Grid>
        </Container>
      </Container>
      </>
    );
  }
}

export default App;
