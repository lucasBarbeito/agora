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
import CheckYourEmailPage from './screens/CheckYourEmail/CheckYourEmailPage';

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
            if(newToken !== null){
                localStorage.setItem('token', newToken);
                this.getUserInfo(newToken);
            }
        }      

        this.state = {
            token: null,
            userInfo: null,
            setToken: this.setToken
        }
    }

    async getUserInfo(token){
        try{
            const response = await fetch("http://localhost:8080/user/me", {
                method: 'GET',
                headers: {
                    'Content-type': 'application/json; charset=UTF-8',
                    'Authorization': `Bearer ${token}`
                },
            });
            if (response.ok) {
                const userInfo = await response.json()
                this.setState({userInfo});
                history.push('/home')
            }else{
                this.setToken(null);
                history.push('/')
            }
        }catch(e){
            this.setToken(null);
            history.push('/')
        }
        
    }

    componentDidMount(){
        this.setToken(localStorage.getItem('token'));
    }


    render() {
        return (
            <UserContext.Provider value={this.state}>
                <Router history={history}>
                    <div>
                        <Navbar history={history}/>
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
                            <Route exact path="/check-email">
                              <CheckYourEmailPage history={history} />
                            </Route>
                            <AuthRoute path="/home">
                                <Home history={history}/>
                            </AuthRoute>
                            <AuthRoute path='/create-group'>
                                <CreateGroup history={history}/>
                            </AuthRoute>
                            <AuthRoute path="/group/:id">
                                <Group history={history} admin={true}/>
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
