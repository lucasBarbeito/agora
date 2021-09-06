// import {Box, Button, Container, Grid} from '@material-ui/core';
i// import {Box, Button, Container, Grid} from '@material-ui/core';
import './App.css';
import {Component} from 'react';
import Navbar from './common/Navbar/Navbar';
import LandingPage from './screens/LandingPage/LandingPage';
import RegisterPage from './screens/Register/RegisterPage';
import Group from './screens/Group/Group';
import CreateGroup from './screens/CreateGroup/CreateGroup';
import ErrorNotFound from './screens/Error/ErrorNotFound';

import {Component} from 'react';
import {
    Router,
    Switch,
    Route,
} from "react-router-dom";
import EmailConfirmation from './screens/EmailConfirmation/EmailConfirmation';
import history from './history';

import Navbar from './common/Navbar/Navbar';
import ErrorNotFound from './screens/Error/ErrorNotFound';

class App extends Component {
  render() {
    return (
      <Router history={history}>
        <div>
        <Navbar name="User name" loggedIn={false} />
            <Navbar name="User name" loggedIn={false} />
            <Switch>
                <Route path="/register">
                    <RegisterPage history={history}/>
                </Route>
                <Route path="/create-group">
                    <CreateGroup history={history}/>
                </Route>
                <Route path="/group/:id">
                    <Group history={history}/>
                </Route>
                <Route
                    path="/user/verify-user/:id"
                    render={(props) => <EmailConfirmation history={history} {...props}/>}
                />
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
