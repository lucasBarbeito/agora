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

class Navbar extends Component {
  constructor(props) {
    super(props);
  }

  isLoggedIn() {
    return !!this.context.token && this.context.userInfo;
  }

  goHome = () => {
    this.props.history.push(this.isLoggedIn() ? "/home" : "/");
  };

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
