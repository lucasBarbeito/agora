import React, { Component } from "react";
import {
  Button,
  CircularProgress,
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
import EditGroup from "../../common/EditGroup/EditGroup.jsx";
import GroupMembersAccordion from "../../common/GroupMembersAccordion/GroupMembersAccordion.js";
import { AppContext } from "../../app-context";
import { withRouter } from "react-router-dom";
import baseUrl from "../../baseUrl";
import GroupAnnouncement from "../../common/GroupAnnouncement/GroupAnnouncement.js";
import SimpleSnackbar from "../../common/SimpleSnackbar/SimpleSnackbar.js";
import InvitationForm from "../../common/InvitationForm/InvitationForm";

class Group extends Component {
  constructor(props) {
    super(props);
    this.state = {
      editGroupFormVisible: false,
      invitationGroupFormVisible: false,
      groupName: "",
      description: "",
      labels: [],
      confirmationDialogIsOpen: false,
      isFetching: true,
      creationDate: "",
      creatorName: "",
      requestLoading: false,
      openAbandonGroupSnack: false,
      openInvitationLinkSnack: false,
      newAnnouncementContent: "",
      creatingAnnouncement: false,
      openAnnouncementCreationErrorSnack: false,
      getAnnouncementErrorSnack: false,
      announcements: [],
      deletingAnnouncements: [],
      openAnnouncementDeletionErrorSnack: false,
    };
  }

  handleEditGroupClick = () => {
    this.setState((state) => ({
      editGroupFormVisible: !state.editGroupFormVisible,
    }));
  };

  handleInvitationClick = () => {
    this.setState((state) => ({
      invitationGroupFormVisible: !state.invitationGroupFormVisible,
    }));
  };

  handleOnChange = () => {
    this.setState({ isFetching: true });
    this.fetchGroupInformation();
  };

  componentDidMount() {
    this.fetchGroupInformation();
  }

  getAnnouncements = async (contacts) => {
    const groupId = this.props.match.params.id;

    try {
      const response = await fetch(`${baseUrl}/studyGroup/${groupId}/forum`, {
        headers: {
          "Content-type": "application/json; charset=UTF-8",
          Authorization: `Bearer ${this.context.token}`,
        },
      });

      if (!response.ok) {
        this.setState({ getAnnouncementErrorSnack: true });
      } else {
        const res = await response.json();

        const announcementFormatPromises = res.map(async (item) => {
          const userId = item.creatorId;
          let user = contacts.find((user) => user.id === userId);
          if (!user) {
            const response = await fetch(`${baseUrl}/user/${userId}`, {
              method: "GET",
              headers: {
                "Content-type": "application/json; charset=UTF-8",
                Authorization: `Bearer ${this.context.token}`,
              },
            });
            if (response.ok) {
              user = await response.json();
              user.name = user.name + " (ex-miembro)";
            } else {
              throw new Error("User not found");
            }
          }
          return {
            name: user.name,
            date: new Date(item.creationDateAndTime).toLocaleDateString(
              "es-AR"
            ),
            content: item.content,
            creatorId: item.creatorId,
            id: item.id,
          };
        });
        const announcementFormat = await Promise.all(
          announcementFormatPromises
        );
        this.setState({ announcements: announcementFormat });
      }
    } catch (e) {
      alert("Error, no es posible conectarse al back-end");
    }
  };

  deleteGroup = async () => {
    const groupId = this.props.match.params.id;
    this.setState({ requestLoading: true });
    try {
      const response = await fetch(`${baseUrl}/studyGroup/${groupId}`, {
        method: "DELETE",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
          Authorization: `Bearer ${this.context.token}`,
        },
      });
      this.setState({ requestLoading: false });
      if (response.ok) {
        this.props.history.push("/home");
      } else {
        this.setState({
          openAbandonGroupSnack: true,
          confirmationDialogIsOpen: false,
        });
      }
    } catch (e) {
      alert("Error, no es posible conectarse al back-end");
    }
  };

  abandonGroup = async () => {
    const groupId = this.props.match.params.id;
    this.setState({ requestLoading: true });
    try {
      const response = await fetch(`${baseUrl}/studyGroup/${groupId}/me`, {
        method: "DELETE",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
          Authorization: `Bearer ${this.context.token}`,
        },
      });

      if (response.ok) {
        this.setState({ requestLoading: false });
        this.props.history.push("/home");
      } else {
        this.setState({ requestLoading: false, openAbandonGroupSnack: true });
      }
    } catch (e) {
      alert("Error, no es posible conectarse al back-end");
    }
  };

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
        labels: res.labels,
        userContacts: res.userContacts,
        isFetching: false,
        creatorName: creator.name,
        creatorId: res.creatorId,
        isAdmin: res.creatorId === this.context.userInfo.id,
      });

      await this.getAnnouncements(res.userContacts);
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

  async addAnnouncement() {
    const groupId = this.props.match.params.id;
    const date = new Date();
    this.setState({ creatingAnnouncement: true });

    try {
      const response = await fetch(`${baseUrl}/studyGroup/${groupId}/forum`, {
        method: "POST",
        body: JSON.stringify({
          content: this.state.newAnnouncementContent,
          creationDateAndTime: date.toISOString(),
        }),
        headers: {
          "Content-type": "application/json; charset=UTF-8",
          Authorization: `Bearer ${this.context.token}`,
        },
      });

      this.setState({ creatingAnnouncement: false });

      if (!response.ok) {
        this.setState({ openAnnouncementCreationErrorSnack: true });
      } else {
        await this.getAnnouncements(this.state.userContacts);
      }
    } catch (e) {
      alert("Error, no es posible conectarse al back-end");
    }

    this.setState({ newAnnouncementContent: "" });
  }

  async deleteAnnouncement(id) {
    let updatedDeletingAnnouncements = this.state.deletingAnnouncements;
    updatedDeletingAnnouncements.push(id);
    this.setState({ deletingAnnouncements: updatedDeletingAnnouncements });

    const groupId = this.props.match.params.id;
    try {
      const response = await fetch(
        `${baseUrl}/studyGroup/${groupId}/forum/${id}`,
        {
          method: "DELETE",
          headers: {
            "Content-type": "application/json; charset=UTF-8",
            Authorization: `Bearer ${this.context.token}`,
          },
        }
      );

      updatedDeletingAnnouncements = this.state.deletingAnnouncements.filter(
        (an) => an.id === id
      );
      this.setState({ deletingAnnouncements: updatedDeletingAnnouncements });

      if (!response.ok) {
        this.setState({ openAnnouncementDeletionErrorSnack: true });
      } else {
        const updatedAnnouncements = this.state.announcements.filter(
          (an) => an.id !== id
        );
        this.setState({ announcements: updatedAnnouncements });
      }
    } catch (e) {
      alert("Error, no es posible conectarse al back-end");
    }
  }

  compareDates(date1, date2) {
    console.log(date1);

    const date1Components = date1.split("/");
    const date2Components = date2.split("/");

    const newDate1 = new Date(
      date1Components[2],
      date1Components[1] - 1,
      date1Components[0]
    );
    const newDate2 = new Date(
      date2Components[2],
      date2Components[1] - 1,
      date2Components[0]
    );

    return newDate2 - newDate1;
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
                            groupId={this.props.match.params.id}
                            token={this.context.token}
                            onClose={() => this.handleEditGroupClick()}
                            initialGroupName={this.state.groupName}
                            initialDescription={this.state.description}
                            tags={this.context.labels}
                            groupLabel={this.state.labels.map(
                              (index) => index.name
                            )}
                            onChange={() => this.handleOnChange()}
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
                    {this.state.labels.map((index) => (
                      <Typography id="group-description">
                        - {index.name}
                      </Typography>
                    ))}
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
                            disabled={this.state.requestLoading}
                          >
                            Cancelar
                          </Button>
                          <Button
                            onClick={
                              this.state.isAdmin
                                ? () => this.deleteGroup()
                                : () => this.abandonGroup()
                            }
                            disabled={this.state.requestLoading}
                            autoFocus
                          >
                            {this.state.requestLoading ? (
                              <CircularProgress size={20} />
                            ) : null}
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
                      onChange={(e) =>
                        this.setState({
                          newAnnouncementContent: e.target.value,
                        })
                      }
                      value={this.state.newAnnouncementContent}
                    />
                  </Grid>
                  <Grid
                    container
                    item
                    xs={5}
                    justifyContent="space-between"
                    alignItems="center"
                  >
                    {this.state.creatingAnnouncement && (
                      <Grid item xs={2}>
                        <CircularProgress size={30} />
                      </Grid>
                    )}
                    <Grid item xs={this.state.creatingAnnouncement ? 10 : 12}>
                      <Button
                        fullWidth
                        id="new-announcement-button"
                        variant="contained"
                        color="primary"
                        onClick={() => this.addAnnouncement()}
                        disabled={
                          this.state.creatingAnnouncement ||
                          this.state.newAnnouncementContent === ""
                        }
                      >
                        AGREGAR ANUNCIO
                      </Button>
                    </Grid>
                  </Grid>
                </Grid>
                <Container id="announcement">
                  {!this.state.isFetching &&
                    this.state.announcements
                      .sort((a1, a2) => this.compareDates(a1.date, a2.date))
                      .map((announcement, index) => (
                        <GroupAnnouncement
                          key={index}
                          canDelete={
                            this.state.isAdmin ||
                            this.context.userInfo.id === announcement.creatorId
                          }
                          handleDelete={() =>
                            this.deleteAnnouncement(announcement.id)
                          }
                          name={announcement.name}
                          date={announcement.date}
                          content={announcement.content}
                          isBeingRemoved={this.state.deletingAnnouncements.includes(
                            announcement.id
                          )}
                        />
                      ))}
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
                    onClick={() => this.handleInvitationClick()}
                  >
                    <LinkIcon id="invite-icon" />
                    INVITAR AL GRUPO
                  </Button>
                  <InvitationForm
                    onClose={() => this.handleInvitationClick()}
                    visible={this.state.invitationGroupFormVisible}
                    groupId={this.props.match.params.id}
                    token={this.context.token}
                    members={this.state.userContacts}
                  />
                  <SimpleSnackbar
                    open={this.state.openInvitationLinkSnack}
                    handleClose={() =>
                      this.setState({ openInvitationLinkSnack: false })
                    }
                    message="Link de invitación copiado al portapapeles"
                  />
                </Grid>
                <Grid item>
                  {!this.state.isFetching && (
                    <GroupMembersAccordion
                      memberContacts={this.state.userContacts}
                      creatorId={this.state.creatorId}
                      isAdmin={this.state.isAdmin}
                      history={this.props.history}
                    />
                  )}
                </Grid>
              </Grid>
            </Grid>
          </Grid>
          <SimpleSnackbar
            open={this.state.openAbandonGroupSnack}
            handleClose={() => this.setState({ openAbandonGroupSnack: false })}
            message={`Hubo un error al ${
              this.state.isAdmin ? "eliminar" : "abandonar"
            } el grupo`}
          />
          <SimpleSnackbar
            open={this.state.openAnnouncementCreationErrorSnack}
            handleClose={() =>
              this.setState({ openAnnouncementCreationErrorSnack: false })
            }
            message="Hubo un error al crear el anuncio"
          />
          <SimpleSnackbar
            open={this.state.getAnnouncementErrorSnack}
            handleClose={() =>
              this.setState({ getAnnouncementErrorSnack: false })
            }
            message="Hubo un error al pedir los anuncios del grupo"
          />
          <SimpleSnackbar
            open={this.state.openAnnouncementDeletionErrorSnack}
            handleClose={() =>
              this.setState({ openAnnouncementDeletionErrorSnack: false })
            }
            message="Hubo un error al eliminar el anuncio"
          />
        </Container>
      </div>
    );
  }
}

Group.contextType = AppContext;

export default withRouter(Group);
