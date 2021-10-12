import "./App.css";
import React, { Component } from "react";
import { Router, Switch, Route, Redirect } from "react-router-dom";
import history from "./history";
import { AppContext } from "./app-context";
import Navbar from "./common/Navbar/Navbar";
import RegisterPage from "./screens/Register/RegisterPage";
import LoginPage from "./screens/LoginPage/LoginPage";
import Group from "./screens/Group/Group";
import ErrorNotFound from "./screens/Error/ErrorNotFound";
import CreateGroup from "./screens/CreateGroup/CreateGroup";
import EmailConfirmation from "./screens/EmailConfirmation/EmailConfirmation";
import LandingPage from "./screens/LandingPage/LandingPage";
import GroupsPage from "./screens/GroupsPage/GroupsPage";
import CheckYourEmailPage from "./screens/CheckYourEmail/CheckYourEmailPage";
import baseUrl from "./baseUrl.js";
import { CircularProgress } from "@material-ui/core";

const AuthRoute = ({ children, ...rest }) => (
  <AppContext.Consumer>
    {({ token }) => (
      <Route
        {...rest}
        render={(props) =>
          !!token ? (
            <> {children} </>
          ) : (
            <Redirect
              to={{
                pathname: "/login",
                state: { from: props.location },
              }}
            />
          )
        }
      />
    )}
  </AppContext.Consumer>
);

class App extends Component {
  constructor(props) {
    super(props);

    this.setToken = (newToken, redirectTo) => {
      this.setState({ token: newToken, logingIn: false });
      if (newToken !== null) {
        localStorage.setItem("token", newToken);
        this.getUserInfo(newToken, redirectTo);
        this.getLabels(newToken)
      } else {
        localStorage.removeItem("token");
        history.push("/")
      }
    };

    this.state = {
      token: null,
      userInfo: null,
      logingIn: true,
      labels: [],
      setToken: this.setToken,
    };
  }

  async getUserInfo(token, redirectTo) {
    try {
      const response = await fetch(`${baseUrl}/user/me`, {
        method: "GET",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.ok) {
        const userInfo = await response.json();
        this.setState({ userInfo });
        history.push(redirectTo || "/home");
      } else {
        this.setToken(null);
        history.push("/login");
      }
    } catch (e) {
      console.log(e);
      this.setToken(null);
      history.push("/login");
    }
  }

  async getLabels(newToken) {
    try {
      const response = await fetch(`${baseUrl}/studyGroup/label`, {
        method: "GET",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
          Authorization: `Bearer ${newToken}`,
         }
      })
      this.setState({labels: await response.json()})
      } catch (e) {
      alert("Error, no es posible conectarse al back-end");
    }
  }

  componentDidMount() {
    console.log(history.location.pathname);
    this.setToken(localStorage.getItem("token"), history.location.pathname);
  }

  render() {
    return (
      <>
        {" "}
        {this.state.logingIn ? (
          <CircularProgress />
        ) : (
          <AppContext.Provider value={this.state}>
            <Router history={history}>
              <div>
                <Navbar history={history}  />
                <Switch>
                  <Route exact path="/">
                    <LandingPage
                      onRegisterClick={() => history.push("/register")}
                      onLoginClick={() => history.push("/login")}
                    />
                  </Route>
                  <Route path="/login">
                    <LoginPage history={history} />
                  </Route>
                  <Route path="/register">
                    <RegisterPage history={history} />
                  </Route>
                  <Route
                    path="/user/verify-user/:id"
                    render={(props) => (
                      <EmailConfirmation history={history} {...props} />
                    )}
                  />
                  <Route exact path="/check-email">
                    <CheckYourEmailPage history={history} />
                  </Route>
                  <AuthRoute path="/home">
                    <GroupsPage history={history} onlyMyGroups={false} />
                  </AuthRoute>
                  <AuthRoute path="/my-groups">
                    <GroupsPage history={history} onlyMyGroups={true} />
                  </AuthRoute>
                  <AuthRoute path="/create-group">
                    <CreateGroup history={history} />
                  </AuthRoute>
                  <AuthRoute path="/group/:id">
                    <Group />
                  </AuthRoute>
                  <Route>
                    <ErrorNotFound history={history} />
                  </Route>
                </Switch>
              </div>
            </Router>
          </AppContext.Provider>
        )}
      </>
    );
  }
}

export default App;
