import React, { Component } from "react";
import { CircularProgress, Grid, Button } from "@material-ui/core";
import baseUrl from "../../baseUrl";
import "./EmailConfirmation.css";
import { withRouter } from "react-router";
import PropTypes from "prop-types";

 class EmailConfirmation extends Component {
  constructor(props) {
    super(props);
    this.state = {
      requestSuccess: false,
      message: "",
    };
  }

  static propTypes = {
    history: PropTypes.object.isRequired
  };

  makeRequest = async () => {
    try {
      const response = await fetch(`${baseUrl}/user/verify_user`, {
        method: "POST",
        body: JSON.stringify({
          userVerificationToken: this.props.match.params.id,
        }),
        headers: {
          "Content-type": "application/json; charset=UTF-8",
        },
      });
      if (response.ok) {
        this.setState({
          message: "Tu correo ha sido confirmado exitosamente",
          requestSuccess: true,
        });
      } else {
        console.log("respuesta " + response.status);
        this.setState({
          message: "Ha ocurrido un error!",
          requestSuccess: true,
        });
      }
    } catch (e) {
      alert("Error, no es posible conectarse al back-end");
    }
  };

  componentDidMount() {
    this.makeRequest();
  }

  render() {
    return (
      <div className="Agorabackground">
        {this.state.requestSuccess ? (
          <Grid container id="email-verification-main-grid">
            <Grid item xs={6}>
              <Grid container id="email-verification-left-grid">
                <Grid item xs={12}>
                  <h1 className="email-verification-description-text">
                    {this.state.message}
                  </h1>
                </Grid>
                <Grid item xs={12}>
                  <Grid
                    container
                    spacing={3}
                    id="email-verification-buttons-grid"
                  >
                    <Grid item xs={3}>
                      <Button
                        id="email-verification-button"
                        variant="contained"
                        color="primary"
                        onClick={() => this.props.history.push("/")}
                      >
                        {" "}
                        Volver al inicio
                      </Button>
                    </Grid>
                  </Grid>
                </Grid>
              </Grid>
            </Grid>
            <Grid item xs={6}>
              <img
                className="email-verification-agora-Logo"
                alt="AgoraLogo"
                src="/agora-logo.png"
              />
            </Grid>
          </Grid>
        ) : (
          <div id="email-verification-spinner-container">
            <CircularProgress id="email-verification-spinner" size={40} />
          </div>
        )}
      </div>
    );
  }
}
export default withRouter(EmailConfirmation);
