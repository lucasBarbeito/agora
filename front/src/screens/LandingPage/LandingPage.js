import React, {Component, Text} from 'react';
import {Button, Container, Grid} from '@material-ui/core';
import './LandingPage.css'

class LandingPage extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className='background'>
                <Grid container id='main-grid'>
                    <Grid item xs={6}>
                        <Grid container id="left-grid">
                            <Grid item xs={12}>
                                <h1 className='welcomeText'>Bienvenidos a AGORA</h1>
                            </Grid>
                            <Grid item xs={12}>
                                <h1 className='descriptionText'>Unite a una comunidad de estudiantes.</h1>
                                <h1 className='descriptionText'>Compartí ideas y conocé nuevos amigos!</h1>
                            </Grid>
                            <Grid item xs={12}>
                                <Grid container spacing={3} id="buttons-grid">
                                    <Grid item xs={3}>
                                        <Button id="button" variant="contained" color="primary"
                                                onClick={() => this.props.onLoginClick()}>
                                            INICIAR SESIÓN
                                        </Button>
                                    </Grid>
                                    <Grid item xs={3}>
                                        <Button id="button" variant="contained" color="primary"
                                                onClick={() => this.props.onRegisterClick()}>
                                            REGISTRARME
                                        </Button>
                                    </Grid>
                                </Grid>
                            </Grid>
                         </Grid>
                     </Grid>
                    <Grid item xs={6}>
                        <img className="agoraLogo" src= 'agora-logo.png' >
                        </img>
                    </Grid>
                </Grid>
            </div>
        );
    }
}
export default LandingPage;