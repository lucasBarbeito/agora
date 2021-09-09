import {Component} from 'react';
import {AppBar, Chip, Container, Grid, IconButton, Toolbar, Typography} from '@material-ui/core';
import './Navbar.css';
import NotificationsIcon from '@material-ui/icons/Notifications';
import AccountCircleIcon from '@material-ui/icons/AccountCircle';

class Navbar extends Component {
    constructor(props) {
        super(props);
    }

    goHome = () => {
        this.props.history.push(this.props.loggedIn ? '/home' : '/')
    }

    render() {

        return (
            <div>
                <AppBar id="appbar">
                    <Toolbar>
                        <Grid container direction="row" justifyContent="space-between" alignItems="center">
                            <Grid item>
                                <img src={"/agora-logo.png"} alt="Logo" className="logo"
                                     onClick={this.goHome} />
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
                                                    id="chip"
                                                    avatar={<AccountCircleIcon/>}
                                                    label={
                                                        <Typography id="name">
                                                            {this.props.name}
                                                        </Typography>
                                                    }
                                                    clickable
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
