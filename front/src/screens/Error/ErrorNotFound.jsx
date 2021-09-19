import React, {Component} from 'react';
import {Grid} from '@material-ui/core';
import './ErrorNotFound.css'
import GoBackButton from '../../Components/GoBackButton';







export default class ErrorNotFound extends Component {

    
   
    render() {
        return (
            <div className='Agorabackground'>
                <Grid container id='error-not-found-main-grid'>
                    <Grid item xs={6}>
                        <Grid container id="error-not-found-left-grid">
                            <Grid item xs={12}>
                                <h1 className='error-not-found-welcomeText'>
                                    <div>404</div>
                                    <div>NOT FOUND</div>
                                    </h1>
                            </Grid>
                            <Grid item xs={12}>
                                <h1 className='error-not-found-descriptionText'>Lo lamentamos, pero no encontramos la </h1>
                                <h1 className='error-not-found-descriptionText'>página que buscas.</h1>
                            </Grid>
                            <Grid item xs={12}>
                                <Grid container spacing={3} id="error-not-found-buttons-grid">
                                    <Grid item xs={3}>
                                        <GoBackButton history = {this.props.history}></GoBackButton>
                                    </Grid>
                                </Grid>
                            </Grid>
                         </Grid>
                     </Grid>
                    <Grid item xs={6}>
                        <img className="error-not-found-agoraLogo" alt ="AgoraLogo" src= '/agora-logo.png' >
                        </img>
                    </Grid>
                </Grid>
            </div>
        )
    }
}
