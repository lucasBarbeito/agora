import React, { Component } from "react";
import {
  Button,
  Container,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Divider,
  Grid,
  IconButton,
  Paper,
  Typography,
  TextField,
} from "@material-ui/core";
import LinkIcon from "@material-ui/icons/Link";
import ArrowBackIosIcon from "@material-ui/icons/ArrowBackIos";
import EditIcon from "@material-ui/icons/Edit";
import "./Group.css";
import Post from "../../Post";
import EditGroup from "../../common/EditGroup/EditGroup.jsx";
import GroupMembersAccordion from "../../common/GroupMembersAccordion/GroupMembersAccordion.js";
import { UserContext } from "../../user-context";
import { withRouter } from "react-router-dom";
import baseUrl from "../../baseUrl";

class Group extends Component {
  constructor(props) {
    super(props);

    this.state = {
      editGroupFormVisible: false,
      groupName: "",
      description: "",
      confirmationDialogIsOpen: false,
      isFetching: true,
      groupName: "",
      creationDate: "",
      description: "",
      creatorName: "",
    };
  }

  handleEditGroupClick = () => {
    this.setState((state) => ({
      editGroupFormVisible: !state.editGroupFormVisible,
    }));
  };

  handleOnChange = (newGroupName, newDescription) => {
    if (newGroupName) {
      this.setState({
        groupName: newGroupName,
      });
    }
    if (newDescription) {
      this.setState({
        description: newDescription,
      });
    }
  };

  componentDidMount() {
    this.fetchGroupInformation();
  }

  deleteGroup = () => {
    this.props.history.push("/home");
  };

  abandonGroup = () => {
    this.props.history.push("/home");
  };

  async getInvitationLink() {
    const groupId = this.props.match.params.id;
    const response = await fetch(
      `${baseUrl}/studyGroup/${groupId}/inviteLink`,
      {
        method: "GET",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
          Authorization: `Bearer ${this.context.token}`,
        },
      }
    );
    return await response.text();
  }

  async copyInvitationLinkToClipboard() {
    try {
      let res = await this.getInvitationLink();
      navigator.clipboard.writeText(res);
      alert("Link de invitación copiado al portapapeles");
    } catch (e) {
      alert("Error, no es posible conectarse al back-end");
    }
  }

  async fetchGroupInformation() {
    const groupId = this.props.match.params.id;
    try {
      let res = await this.getGroupData(groupId);

      const userInGroup = res.userContacts.find(
        (item) => item.id === this.context.userInfo.id
      );
      if (!userInGroup) {
        await this.addUserToGroup(groupId);
        res = await this.getGroupData(groupId);
      }

      const creator = await this.getUserData(res.creatorId);

      this.setState({
        groupName: res.name,
        creationDate: res.creationDate,
        description: res.description,
        userContacts: res.userContacts,
        isFetching: false,
        creatorName: creator.name,
        creatorId: res.creatorId,
        isAdmin: res.creatorId === this.context.userInfo.id,
      });
    } catch (e) {
      alert("Error, no es posible conectarse al back-end");
    }
  }

  async addUserToGroup(groupId) {
    try {
      const userId = this.context.userInfo.id;
      await fetch(`${baseUrl}/studyGroup/${groupId}/${userId}`, {
        method: "POST",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
          Authorization: `Bearer ${this.context.token}`,
        },
      });
    } catch (e) {
      alert("Error, no es posible conectarse al back-end");
    }
  }

  async getUserData(id) {
    const response = await fetch(`${baseUrl}/user/${id}`, {
      method: "GET",
      headers: {
        "Content-type": "application/json; charset=UTF-8",
        Authorization: `Bearer ${this.context.token}`,
      },
    });
    return await response.json();
  }

  async getGroupData(groupId) {
    const response = await fetch(`${baseUrl}/studyGroup/${groupId}`, {
      headers: {
        "Content-type": "application/json; charset=UTF-8",
        Authorization: `Bearer ${this.context.token}`,
      },
    });
    return response.json();
  }

  render() {
    return (
      <div className="main-div">
        <Container id="main-container" maxWidth="lg">
          <Grid container spacing={2}>
            <Grid container item xs={3}>
              <Grid container direction="column" spacing={2}>
                <Grid item>
                  <Button
                    fullWidth
                    id="back-button"
                    variant="contained"
                    color="primary"
                    onClick={() => this.props.history.goBack()}
                  >
                    <ArrowBackIosIcon id="back-icon" />
                    VOLVER ATRÁS
                  </Button>
                </Grid>

                <Grid item>
                  <Paper>
                    <Grid container>
                      {this.state.isAdmin && (
                        <Grid item id="edit-group-grid" xs={1}>
                          <IconButton
                            onClick={() => this.handleEditGroupClick()}
                          >
                            <EditIcon id="edit-icon" />
                          </IconButton>
                          <EditGroup
                            visible={this.state.editGroupFormVisible}
                            onClose={() => this.handleEditGroupClick()}
                            initialGroupName={this.state.groupName}
                            initialDescription={this.state.description}
                            onChange={(newGroupName, newDescription) =>
                              this.handleOnChange(newGroupName, newDescription)
                            }
                          />
                        </Grid>
                      )}

                      <Grid item xs={9} id="group-name-grid">
                        <Typography id="group-name" variant="h5">
                          {!this.state.isFetching && this.state.groupName}
                        </Typography>
                        <Typography id="group-creation">
                          Creado el{" "}
                          {!this.state.isFetching && this.state.creationDate}{" "}
                          por {!this.state.isFetching && this.state.creatorName}
                        </Typography>
                      </Grid>
                    </Grid>
                    <Divider />
                    <Typography id="group-description">
                      {!this.state.isFetching && this.state.description}
                    </Typography>
                    <Grid container justifyContent="flex-end">
                      <Button
                        id="action-group-button"
                        onClick={() =>
                          this.setState({
                            confirmationDialogIsOpen: true,
                          })
                        }
                      >
                        {`${
                          this.state.isAdmin ? "ELIMINAR" : "ABANDONAR"
                        } GRUPO`}
                      </Button>
                      <Dialog
                        open={this.state.confirmationDialogIsOpen}
                        onClose={() =>
                          this.setState({
                            confirmationDialogIsOpen: false,
                          })
                        }
                      >
                        <DialogTitle>
                          {`${
                            this.state.isAdmin ? "Eliminar" : "Abandonar"
                          } grupo?`}
                        </DialogTitle>
                        <DialogContent>
                          <DialogContentText>
                            {`Estas seguro de que deseas ${
                              this.state.isAdmin ? "eliminar" : "abandonar"
                            } este grupo?`}
                          </DialogContentText>
                        </DialogContent>
                        <DialogActions>
                          <Button
                            onClick={() =>
                              this.setState({
                                confirmationDialogIsOpen: false,
                              })
                            }
                          >
                            Cancelar
                          </Button>
                          <Button
                            onClick={
                              this.state.isAdmin
                                ? () => this.deleteGroup()
                                : () => this.abandonGroup()
                            }
                            autoFocus
                          >
                            {this.state.isAdmin ? "Eliminar" : "Abandonar"}
                          </Button>
                        </DialogActions>
                      </Dialog>
                    </Grid>
                  </Paper>
                </Grid>
              </Grid>
            </Grid>

            <Grid container item xs={6}>
              <Grid container direction="column">
                <Grid
                  item
                  container
                  direction="row"
                  id="inner-mid-container"
                  spacing={2}
                >
                  <Grid item xs={7}>
                    <TextField
                      id="new-announcement-textfield"
                      fullWidth
                      label="Nuevo anuncio"
                      variant="outlined"
                      onChange={(e) => {
                        /*TODO*/
                      }}
                    />
                  </Grid>
                  <Grid item xs={5}>
                    <Button
                      fullWidth
                      id="new-announcement-button"
                      variant="contained"
                      color="primary"
                      onClick={() => {
                        /*TODO*/
                      }}
                    >
                      AGREGAR ANUNCIO
                    </Button>
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
                  <Button
                    fullWidth
                    id="invite-button"
                    variant="contained"
                    color="primary"
                    onClick={() => this.copyInvitationLinkToClipboard()}
                  >
                    <LinkIcon id="invite-icon" />
                    INVITAR AL GRUPO
                  </Button>
                </Grid>
                <Grid item>
                  {!this.state.isFetching && (
                    <GroupMembersAccordion
                      memberContacts={this.state.userContacts}
                      creatorId={this.state.creatorId}
                      isAdmin={this.state.isAdmin}
                    />
                  )}
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
