import React, { Component } from 'react'
import {CircularProgress, Grid, Typography} from '@material-ui/core';
import Navbar from '../../common/Navbar/Navbar';
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
      <div className="EmailConfiguration">
        <Navbar />
          { this.state.requestSuccess ?
            <div className="content">
              <Grid 
                id="grid"
                container 
                spacing={0}
                direction="row"
                alignItems="center"
                justifyContent="center"
              >
                <Grid id="grid-item" item xs={6}>
                  <Typography id="message" variant="h2"> Tu correo ha sido confirmado exitosamente!</Typography>
                </Grid> 
                <Grid id="grid-item" item xs={6}>
                  <img src = "/agora-logo.png" alt = "AGORA" className = "agora-logo"></img>
                </Grid>
              </Grid> 
            </div>
            :
            <div id="spinner-container">
              <CircularProgress id="spinner" size={40} />
            </div>
          }
      </div>
    )
  }
} 



