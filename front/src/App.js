import {Box, Button, Container, Grid} from '@material-ui/core';
import './App.css';
import LandingPage from './screens/LandingPage/LandingPage';
import {Component} from 'react';
import {
  Router,
  Switch,
  Route,
} from "react-router-dom";
import history from './history';

import Navbar from './common/Navbar/Navbar';

class App extends Component {
  render() {
    return (
      <Router history={history}>
        <div>
        <Navbar name="User name" loggedIn={true}/>
        <Switch>
          <Route path="/register">
            {/*<TODO />*/}
          </Route>
          <Route path="/login">
            {/*<TODO />*/}
          </Route>
          <Route path="/">
            <LandingPage onRegisterClick={() => history.push('/register')} onLoginClick={() => history.push('/login')} />
          </Route>
        </Switch>
        </div>
      </Router>
    );
  }
}

export default App;
