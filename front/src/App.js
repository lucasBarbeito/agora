import './App.css';
import {Component} from 'react';
import LandingPage from './screens/LandingPage/LandingPage';
import RegisterPage from './screen/Register/RegisterPage';
import Group from './screen/Group/Group';
import CreateGroup from './screen/CreateGroup/CreateGroup';
import Navbar from './common/Navbar/Navbar';
import ErrorNotFound from './screens/Error/ErrorNotFound';
import history from './history';

import {
  Router,
  Switch,
  Route,
} from "react-router-dom";

class App extends Component {
  render() {
    return (
      <Router history={history}>
        <div>
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
