// import {Box, Button, Container, Grid} from '@material-ui/core';
import './App.css';
import LandingPage from './Screens/LandingPage/LandingPage';
import {Component} from 'react';
import {
  Router,
  Switch,
  Route,
} from "react-router-dom";
import history from './history';

import Navbar from './common/Navbar/Navbar';
import ErrorNotFound from './Screens/Error/ErrorNotFound';


class App extends Component {
  render() {
    return (
      <Router history={history}>
        <div>
        <Navbar name="User name" loggedIn={true}/>
        <Switch>
          <Route exact path="/register">
            {/*<TODO />*/}
          </Route>
          <Route exact path="/login">
            {/*<TODO />*/}
          </Route>
          <Route exact path="/">
            <LandingPage onRegisterClick={() => history.push('/register')} onLoginClick={() => history.push('/login')} />
          </Route>
          <Route>
            <ErrorNotFound history = {history}></ErrorNotFound>
          </Route>
        </Switch>
        </div>
      </Router>
    );
  }
}

export default App;
