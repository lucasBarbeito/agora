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
import { AppContext } from "../../app-context";
import ProfileButton from "./ProfileButton";
import SimpleSnackbar from "../SimpleSnackbar/SimpleSnackbar";
import { withRouter } from "react-router";
import PropTypes from "prop-types";


class Navbar extends Component {
  constructor(props) {
    super(props);
    
    this.state ={
      snackBarVisible: false,
    }
  }
  static propTypes = {
    history: PropTypes.object.isRequired
  };

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

Navbar.contextType = AppContext;

export default withRouter(Navbar);
