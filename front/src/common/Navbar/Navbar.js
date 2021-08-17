import { Component } from 'react';
import { AppBar, Chip, Container, Grid, IconButton, Toolbar, Typography } from '@material-ui/core';
import './Navbar.css';
import NotificationsIcon from '@material-ui/icons/Notifications';
import AccountCircleIcon from '@material-ui/icons/AccountCircle';

class Navbar extends Component {
  render() {
    return (
      <div>
        <AppBar style={{background: "#E6E2D5"}} className="appbar">
          <Toolbar>
            <Grid container direction="row" justify="space-between" alignItems="center">
              <Grid item>
                <img src={"/agora-logo.png"} alt="Logo" className="logo"/>
              </Grid>
              { 
                this.props.loggedIn && <Grid item>
                  <Grid container direction="row" alignItems="center">
                    <Grid item xs={2}>
                      <IconButton>
                        <NotificationsIcon/>
                      </IconButton>
                    </Grid>
                    <Grid item xs={10}>
                      <Container>
                      <Chip 
                        style={{background: "#E6E2D5"}}
                        avatar={<AccountCircleIcon/>} 
                        label={
                          <Typography style={{color: "#000000", fontWeight: 500}}>
                            { this.props.name }
                          </Typography>
                        }
                        clickable
                        // onClick={}
                      />
                      </Container>
                    </Grid>
                  </Grid>
                </Grid>
              }
            </Grid>
          </Toolbar>
        </AppBar>
        <Toolbar/>
      </div>
    )
  }
}

export default Navbar;