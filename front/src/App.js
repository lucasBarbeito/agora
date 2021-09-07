import {Box, Button, Container, Grid} from '@material-ui/core';
import './App.css';
import LandingPage from './screens/LandingPage/LandingPage';
import React, {Component} from 'react';
import {
  Router,
  Switch,
  Route,
  Redirect,
} from "react-router-dom";
import history from './history';
import Navbar from './common/Navbar/Navbar';
import RegisterPage from './screens/Register/RegisterPage';
import {UserContext} from './user-context'
import LoginPage from "./screens/LoginPage/LoginPage";
import Group from './screens/Group/Group';
import CreateGroup from './screens/CreateGroup/CreateGroup';
import ErrorNotFound from './screens/Error/ErrorNotFound';
import EmailConfirmation from './screens/EmailConfirmation/EmailConfirmation';
import Home from './screens/Home/Home';

const loggedIn = false

const AuthRoute = ({ component: Component, ...rest }) => (
    <Route {...rest} render={(props) => (
        loggedIn === true
            ? <Component {...props} />
            : <Redirect to={{pathname: '/login', state: {from: props.location}}} />
    )} />
)


class App extends Component {

  constructor(props){
    super(props)

    this.setToken = (newToken) => {this.setState({token:newToken})}

    this.state={
      token: null,
      setToken: this.setToken
    }
  }

  render() {
    return (
        <UserContext.Provider value={this.state}>
          <Router history={history}>
            <div>
              <Navbar name="User name" loggedIn={loggedIn}/>
              <Switch>
                <Route path="/home">
                  <Home history={history}/>
                </Route>
                <AuthRoute path="/create-group" component={<CreateGroup history={history}/>} />
                <Route path="/register">
                  <RegisterPage history={history} />
                </Route>
                <Route path="/login">
                  <LoginPage history={history} />
                </Route>
                <Route
                    path="/user/verify-user/:id"
                    render={(props) => <EmailConfirmation history={history} {...props}/>}
                />
                <Route path="/group/:id">
                  <Group history={history}/>
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
        </UserContext.Provider>
    );
  }
}

export default App;
