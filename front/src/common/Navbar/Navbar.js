import { Component } from "react";
import {
  AppBar,
  Container,
  Grid,
  IconButton,
  Toolbar,
} from "@material-ui/core";
import "./Navbar.css";
import NotificationsIcon from "@material-ui/icons/Notifications";
import { UserContext } from "../../user-context";
import ProfileButton from "./ProfileButton";
import history from "../../history";
import SimpleSnackbar from "../SimpleSnackbar/SimpleSnackbar";

class Navbar extends Component {
  constructor(props) {
    super(props);
    
    this.state ={
      snackBarVisible: false,
    }
  }

  isLoggedIn() {
    return !!this.context.token && this.context.userInfo;
  }

  goHome = () => {
    this.props.history.push(this.isLoggedIn() ? "/home" : "/");
  };

  handleLoginError = (newVisibility) => {
    this.setState({
      snackBarVisible: newVisibility
    })
  }

  render() {
    return (
      <div>
        <AppBar id="appbar">
          <Toolbar>
            <Grid
              container
              direction="row"
              justifyContent="space-between"
              alignItems="center"
            >
              <Grid item>
                <img
                  src={"/agora-logo.png"}
                  alt="Logo"
                  className="logo"
                  onClick={this.goHome}
                />
              </Grid>
              <SimpleSnackbar 
                open={this.state.snackBarVisible}
                handleClose={() => this.setState({snackBarVisible: false})}
                message="Error al cerrar sesión" 
                />
              {this.isLoggedIn() && (
                <Grid item>
                  <Grid container direction="row" alignItems="center">
                    <Grid item xs={2}>
                      <IconButton>
                        <NotificationsIcon />
                      </IconButton>
                    </Grid>
                    <Grid item xs={10}>
                      <Container>
                        <ProfileButton
                          name={this.context.userInfo.name}
                          surname={this.context.userInfo.surname}
                          token = {this.context.token}
                          setToken = {this.context.setToken}
                          handleLoginError = {(newVisibility) => this.handleLoginError(newVisibility)}
                          history = {history}
                        />
                      </Container>
                    </Grid>
                  </Grid>
                </Grid>
              )}
            </Grid>
          </Toolbar>
        </AppBar>
        <Toolbar />
      </div>
    );
  }
}

Navbar.contextType = UserContext;

export default Navbar;
