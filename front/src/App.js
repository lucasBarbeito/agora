// import {Box, Button, Container, Grid} from '@material-ui/core';
import './App.css';
import {Component} from 'react';
import Navbar from './common/Navbar/Navbar';
import ErrorNotFound from './Screens/Error/ErrorNotFound';
import history from './history';


class App extends Component {
  render() {
    return (
      <>
        <Navbar name="User name" loggedIn={true}/>
        <ErrorNotFound history={history}></ErrorNotFound>
      </>
    );
  }
}

export default App;
