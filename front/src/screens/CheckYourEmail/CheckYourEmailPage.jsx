import React, { Component } from "react";
import { Button, Grid } from "@material-ui/core";
import "./CheckYourEmailPage.css";

export default class CheckYourEmailPage extends Component {
    render() {
        return (
            <div className="Agorabackground">
                <Grid container id="check-your-email-main-grid">
                    <Grid item xs={6}>
                        <Grid container id="check-your-email-left-grid">
                            <Grid item xs={12}>
                                <h1 className="check-your-email-description-text">
                                    Por favor revise su Email para verificar la
                                    cuenta.
                                </h1>
                            </Grid>
                            <Grid item xs={12}>
                                <Grid
                                    container
                                    spacing={3}
                                    id="check-your-email-buttons-grid"
                                >
                                    <Grid item xs={3}>
                                        <Button
                                            id="check-your-email-button"
                                            variant="contained"
                                            color="primary"
                                            onClick={() =>
                                                this.props.history.push("/")
                                            }
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
                            className="check-your-email-agora-Logo"
                            alt="AgoraLogo"
                            src="agora-logo.png"
                        ></img>
                    </Grid>
                </Grid>
            </div>
        );
    }
}
