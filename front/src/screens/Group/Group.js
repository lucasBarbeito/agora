import React, { Component } from 'react';
import { withRouter } from "react-router-dom";
import { Button, Divider, Grid, Paper, Typography, IconButton, TextField, Container, Accordion } from "@material-ui/core";
import LinkIcon from '@material-ui/icons/Link';
import ArrowBackIosIcon from '@material-ui/icons/ArrowBackIos';
import EditIcon from '@material-ui/icons/Edit';
import './Group.css';
import Post from '../../Post';
import GroupMembersAccordion from '../../common/GroupMembersAccordion/GroupMembersAccordion.js'
import { UserContext } from "../../user-context";

class Group extends Component {

  getInvitationLink() {
    const groupId = this.props.match.params.id;
    navigator.clipboard.writeText(`ID de grupo: ${groupId}`);
  }
  componentDidMount() {
    this.addUserToGroupIfNotAMember()
  }

  async addUserToGroupIfNotAMember() {
    const groupId = this.props.match.params.id;
    try {
      const response = await fetch(`http://localhost:8080/studyGroup/${groupId}`, {
        headers: {
          'Content-type': 'application/json; charset=UTF-8',
          'Authorization': `Bearer ${this.context.token}`
        },
      })
      const res = await response.json()
      const userInGroup = res.userContacts.find(item => item.id === this.context.userInfo.id)

      if (!userInGroup) {
        await this.addUserToGroup(groupId);
      }

    } catch (e) {
      alert('Error, no es posible conectarse al back-end');
    }
  }

  async addUserToGroup(groupId) {
    try {
      const userId = this.context.userInfo.id;
      await fetch(`http://localhost:8080/studyGroup/${groupId}/${userId}`, {
        method: 'POST',
        headers: {
          'Content-type': 'application/json; charset=UTF-8',
          'Authorization': `Bearer ${this.context.token}`
        },
      })
    } catch (e) {
      alert('Error, no es posible conectarse al back-end');
    }
  }

  render() {
    const members = [
      {
        name: "Manuel Pedrozo",
        contacts: [
          { type: 'email', value: "manuelpedrozo@gmail.com" },
          { type: 'phone', value: "11123456" },
        ],
        isAdmin: true
      },
      {
        name: "Tomás Pérez",
        contacts: [
          { type: 'email', value: "t.perez@gmail.com" },
          { type: 'phone', value: "1112345678" },
          { type: 'twitter', value: "tomipedrozo" }
        ],
        isAdmin: false
      },
      {
        name: "Matías Boracchia",
        contacts: [
          { type: 'email', value: "matiboracchia@gmail.com" },
          { type: 'phone', value: "1112345678" },
          { type: 'instagram', value: "matiboracchia" },
        ],
        isAdmin: false
      },
      {
        name: "Franco Velárdez",
        contacts: [
          { type: 'email', value: "franco.velardeeeeeez@gmail.com" },
          { type: 'phone', value: "11123456" },
          { type: 'instagram', value: "fvelardez" },
          { type: 'twitter', value: "fvelardez" },
          { type: 'facebook', value: 'fvelardez' }
        ],
        isAdmin: false
      }
    ]
    return (
      <div className="main-div">
        <Container id="main-container" maxWidth="lg">
          <Grid container spacing={2}>
            <Grid container item xs={3}>
              <Grid container direction="column" spacing={2}>

                <Grid item>
                  <Button fullWidth id="back-button" variant="contained" color="primary"
                    onClick={() => this.props.history.goBack()}>
                    <ArrowBackIosIcon id="back-icon" />
                    VOLVER ATRÁS
                  </Button>
                </Grid>

                <Grid item>
                  <Paper>
                    <Grid container>
                      {this.props.admin && <Grid item id="edit-group-grid" xs={1}>
                        <IconButton onClick={() => {/*TODO*/
                        }}>
                          <EditIcon id="edit-icon" />
                        </IconButton>
                      </Grid>}

                      <Grid item xs={9} id="group-name-grid">
                        <Typography id="group-name" variant="h5">Nombre de grupo</Typography>
                        <Typography id="group-creation">Creado el 10/08/2021
                          por {'User name'}</Typography>
                      </Grid>
                    </Grid>
                    <Divider />
                    <Typography id="group-description">Esta es una descripcion</Typography>
                    <Grid container justifyContent="flex-end">
                      <Button id="abandon-group-button" onClick={() => {/*TODO*/
                      }}>{this.props.admin ? "ELIMINAR GRUPO" : "ABANDONAR GRUPO"}</Button>
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
                      onChange={(e) => {/*TODO*/
                      }}
                    />
                  </Grid>
                  <Grid item xs={5}>
                    <Button fullWidth id="new-announcement-button" variant="contained"
                      color="primary" onClick={() => {/*TODO*/
                      }}>AGREGAR ANUNCIO</Button>
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
                  <Button fullWidth id="invite-button" variant="contained" color="primary"
                    onClick={() => this.getInvitationLink()}>
                    <LinkIcon id="invite-icon" />
                    INVITAR AL GRUPO
                  </Button>
                </Grid>
                <Grid item>
                  <GroupMembersAccordion members={members} isAdmin={true} />
                </Grid>
              </Grid>
            </Grid>
          </Grid>
        </Container>
            </div>
        );
    }
}

Group.contextType = UserContext;

export default withRouter(Group);
