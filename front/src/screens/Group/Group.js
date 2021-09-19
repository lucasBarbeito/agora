import React, { Component } from 'react';
import { Button, Divider, Grid, Paper, Typography, IconButton, TextField, Container, Accordion } from "@material-ui/core";
import LinkIcon from '@material-ui/icons/Link';
import ArrowBackIosIcon from '@material-ui/icons/ArrowBackIos';
import EditIcon from '@material-ui/icons/Edit';
import './Group.css';
import Post from '../../Post';
import GroupMembersAccordion from '../../common/GroupMembersAccordion/GroupMembersAccordion.js'

class Group extends Component {

  render() {
    const members = [
      { name: "Manuel Pedrozo", email: "manuelpedrozo@gmail.com", phone: "11123456", instagram: "@manupedrozo" },
      { name: "Tomás Pérez", email: "t.perez@gmail.com", phone: "1112345678", instagram: "@tomipedrozo" },
      { name: "Matías Boracchia", email: "matiboracchia@gmail.com", phone: "1112345678", instagram: "@matiboracchia" },
      { name: "Franco Velárdez", email: "franco.velardez@gmail.com", phone: "11 3346 3332", instagram: "@fvelardez" }
    ]
    return (
      <div className="main-div">
        <Container id="main-container" maxWidth="lg">
          <Grid container spacing={2} >
            <Grid container item xs={3}>
              <Grid container direction="column" spacing={2}>

                <Grid item >
                  <Button fullWidth id="back-button" variant="contained" color="primary" onClick={() => this.props.history.goBack()}> <ArrowBackIosIcon id="back-icon"></ ArrowBackIosIcon>
                    VOLVER ATRÁS
                  </Button>
                </Grid>

                <Grid item >
                  <Paper >
                    <Grid container >
                      {this.props.admin && <Grid item id="edit-group-grid" xs={1}>
                        <IconButton onClick={() => {/*TODO*/ }}>
                          <EditIcon id="edit-icon" />
                        </IconButton>
                      </Grid>}

                      <Grid item xs={9} id="group-name-grid">
                        <Typography id="group-name" variant="h5">Nombre de grupo</Typography>
                        <Typography id="group-creation">Creado el 10/08/2021 por {'User name'}</Typography>
                      </Grid>
                    </Grid>
                    <Divider />
                    <Typography id="group-description">Esta es una descripcion</Typography>
                    <Grid container justifyContent="flex-end">
                      <Button id="abandon-group-button" onClick={() => {/*TODO*/ }} >{this.props.admin ? "ELIMINAR GRUPO" : "ABANDONAR GRUPO"}</Button>
                    </Grid>
                  </Paper>
                </Grid>

              </Grid>
            </Grid>

            <Grid container item xs={6}>
              <Grid container direction="column">
                <Grid item container direction="row" id="inner-mid-container" spacing={2}>
                  <Grid item xs={7}>
                    <TextField
                      id="new-announcement-textfield"
                      fullWidth
                      label="Nuevo anuncio"
                      variant="outlined"
                      onChange={(e) => {/*TODO*/ }}
                    />
                  </Grid>
                  <Grid item xs={5}>
                    <Button fullWidth id="new-announcement-button" variant="contained" color="primary" onClick={() => {/*TODO*/ }} >AGREGAR ANUNCIO</Button>
                  </Grid>

                </Grid>
                <Container>
                  <br></br>
                  <Post />
                  <br></br>
                  <Post />
                  <br></br>
                  <Post />
                  <br></br>
                  <Post />
                  <br></br>
                  <Post />
                  <br></br>
                  <Post />
                </Container>
              </Grid>
            </Grid>

            <Grid container item xs={3}>
              <Grid container item direction="column" spacing={2}>
                <Grid item>
                  <Button fullWidth id="invite-button" variant="contained" color="primary" onClick={() => {/*TODO*/ }}>
                    <LinkIcon id="invite-icon" />
                    INVITAR AL GRUPO
                  </Button>
                </Grid>
                <Grid item>
                  <GroupMembersAccordion data={members} admin={true} />
                </Grid>
              </Grid>
            </Grid>
          </Grid>
        </Container>
      </div>
    );
  }
}

export default Group;
