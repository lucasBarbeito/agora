import React, { Component } from 'react'
import {CircularProgress, Grid,Button} from '@material-ui/core';
import "./EmailConfirmation.css";


export default class EmailConfirmation extends Component {

  constructor(props) {
    super(props);
    this.state = {
      requestSuccess: false,
    }
  }

  makeRequest = async (id) => {
    const response = await fetch('http://localhost:8080/user/verify_user', {
                        method: 'POST',
                        body: JSON.stringify({
                          userVerificationToken: this.props.match.params.id,
                        }),
                        headers: {
                          'Content-type': 'application/json; charset=UTF-8',
                        },
                    });
  }

  componentDidMount() {
    try {
      this.makeRequest();
      this.setState({requestSuccess: true});
      setTimeout(() => { 
        this.props.history.push('/');
      }, 2500);
    } catch {
      this.props.history.push('/');
    }
  }
    
  render() {
    return (
      <div className="Agorabackground">
          { this.state.requestSuccess ?
            <Grid container id='email-verification-main-grid'>
            <Grid item xs={6}>
                <Grid container id="email-verification-left-grid">
                    <Grid item xs={12}>
                        <h1 className='email-verification-description-text'>Tu correo ha sido confirmado exitosamente</h1>
                    </Grid>
                    <Grid item xs={12}>
                        <Grid container spacing={3} id="email-verification-buttons-grid">
                            <Grid item xs={3}>
                                <Button id="email-verification-button" variant="contained" color="primary" onClick = {() => this.props.history.push("/")}> Volver al inicio</Button>
                            </Grid>
                        </Grid>
                    </Grid>
                 </Grid>
             </Grid>
            <Grid item xs={6}>
                <img className="email-verification-agora-Logo" alt ="AgoraLogo" src='/agora-logo.png' />
            </Grid>
           </Grid>
            :
            <div id="email-verification-spinner-container">
              <CircularProgress id="email-verification-spinner" size={40} />
            </div>
          }
      </div>
    )
  }
} 



