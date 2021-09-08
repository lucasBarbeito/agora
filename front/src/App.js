import './App.css';
import React, {Component} from 'react';
import {
    Router,
    Switch,
    Route,
    Redirect,
} from "react-router-dom";
import history from './history';
import {UserContext} from './user-context'
import Navbar from './common/Navbar/Navbar';
import RegisterPage from './screens/Register/RegisterPage';
import LoginPage from "./screens/LoginPage/LoginPage";
import Group from './screens/Group/Group';
import ErrorNotFound from './screens/Error/ErrorNotFound';
import CreateGroup from './screens/CreateGroup/CreateGroup';
import EmailConfirmation from './screens/EmailConfirmation/EmailConfirmation';
import LandingPage from './screens/LandingPage/LandingPage';
import Home from './screens/Home/Home';

const AuthRoute = ({children, ...rest}) => (
    <UserContext.Consumer>
        {({token}) => <Route {...rest} render={(props) => (
            !!token
                ? <> {children} </>
                : <Redirect to={{pathname: '/login', state: {from: props.location}}}/>
        )}/>}
    </UserContext.Consumer>
)

class App extends Component {

    constructor(props) {
        super(props)

        this.setToken = (newToken) => {
            this.setState({token: newToken})
        }

        this.state = {
            token: null,
            setToken: this.setToken
        }
    }

    render() {
        return (
            <UserContext.Provider value={this.state}>
                <Router history={history}>
                    <div>
                        <Navbar name="User name" loggedIn={!!this.state.token}/>
                        <Switch>
                            <Route exact path="/">
                                <LandingPage onRegisterClick={() => history.push('/register')}
                                             onLoginClick={() => history.push('/login')}/>
                            </Route>
                            <Route path="/login">
                                <LoginPage history={history}/>
                            </Route>
                            <Route path="/register">
                                <RegisterPage history={history}/>
                            </Route>
                            <Route
                                path="/user/verify-user/:id"
                                render={(props) => <EmailConfirmation history={history} {...props}/>}
                            />
                            <AuthRoute path="/home">
                                <Home history={history}/>
                            </AuthRoute>
                            <AuthRoute path='/create-group'>
                                <CreateGroup history={history}/>
                            </AuthRoute>
                            <AuthRoute path="/group/:id">
                                <Group history={history}/>
                            </AuthRoute>
                            <Route>
                                <ErrorNotFound history={history}/>
                            </Route>
                        </Switch>
                    </div>
                </Router>
            </UserContext.Provider>
        );
    }
}

export default App;
