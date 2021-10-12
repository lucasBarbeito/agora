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
import "./UserPage.css";
import EditGroup from "../../common/EditGroup/EditGroup.jsx";
import GroupMembersAccordion from "../../common/GroupMembersAccordion/GroupMembersAccordion.js";
import { AppContext } from "../../app-context";
import { withRouter } from "react-router-dom";
import baseUrl from "../../baseUrl";
import MemberContact from "../../common/MemberContact/MemberContact";

import GroupCard from "../../common/GroupCard/GroupCard";

const groups = [
  {
    id: 1,
    name: "Lorem Ipsum",
    labels: [
      { id: 0, name: "Etiqueta 1" },
      { id: 0, name: "Etiqueta 2" },
      { id: 0, name: "Etiqueta 3" },
    ],
    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
    currentUserIsMember: true,
  },
  {
    id: 2,
    name: "Excepteur sint",
    labels: [
      { id: 0, name: "Etiqueta 1" },
      { id: 0, name: "Etiqueta 2" },
      { id: 0, name: "Etiqueta 3" },
    ],
    description:
      "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
    currentUserIsMember: false,
  },
  {
    id: 3,
    name: "Excepteur sint",
    labels: [
      { id: 0, name: "Etiqueta 1" },
      { id: 0, name: "Etiqueta 2" },
      { id: 0, name: "Etiqueta 3" },
    ],
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. ",
    currentUserIsMember: false,
  },
  {
    id: 4,
    name: "Excepteur sint",
    labels: [
      { id: 0, name: "Etiqueta 1" },
      { id: 0, name: "Etiqueta 2" },
      { id: 0, name: "Etiqueta 3" },
    ],
    description:
      "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
    currentUserIsMember: true,
  },
  {
    id: 5,
    name: "Excepteur sint occaecat cupidatat non proident, sunt in culpa",
    labels: [
      { id: 0, name: "Etiqueta 1" },
      { id: 0, name: "Etiqueta 2" },
      { id: 0, name: "Etiqueta 3" },
    ],
    description:
      "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
    currentUserIsMember: true,
  },
];

class UserPage extends Component {
  constructor(props) {
    super(props);
  }

  joinGroup(id) {
    this.props.history.push(`/group/${id}`);
  }

  render() {
    return (
      <div className="userpage-main-div">
        <Container id="userpage-main-container" maxWidth="lg">
          <Grid container spacing={2}>
            <Grid container item xs={3}>
              <Grid container direction="column" spacing={2}>
                <Grid item>
                  <Button
                    fullWidth
                    id="userpage-back-button"
                    variant="contained"
                    color="primary"
                    onClick={() => this.props.history.goBack()}
                  >
                    <ArrowBackIosIcon id="userpage-back-icon" />
                    VOLVER ATRÁS
                  </Button>
                </Grid>
                <Grid item>
                  <Paper>
                    <Grid container>
                      <Grid item xs={9} id="userpage-user-name-grid">
                        <Typography id="userpage-user-name" variant="h5">
                          Franco Velardez
                        </Typography>
                        <Typography id="userpage-user-creation">
                          Se unió a la plataforma el {"10/9/2021"}
                        </Typography>
                      </Grid>
                    </Grid>
                    <Divider />
                    <Typography id="userpage-user-description">
                      Medios de contacto:
                    </Typography>
                    <Grid
                      container
                      direction="column"
                      id="userpage-user-contacts-grid"
                    >
                      <MemberContact
                        key={0}
                        type={"email"}
                        value={"franco.velardez@gmail.com"}
                      />
                      <MemberContact
                        key={1}
                        type={"phone"}
                        value={"11 3346 3332"}
                      />
                      <MemberContact
                        key={3}
                        type={"instagram"}
                        value={"@fvelardez"}
                      />
                    </Grid>
                    <br></br>
                  </Paper>
                </Grid>
              </Grid>
            </Grid>
            <Grid container item xs={9}>
              <Grid container direction="column">
                <Typography id="userpage-groups-text" variant="h6">
                  Grupos en los que {"Franco Velardez"} es miembro
                </Typography>
                <Grid container spacing={3}>
                  <Grid item xs={12} />
                  {groups.map((group, index) => (
                    <Grid
                      item
                      xs={12}
                      sm={6}
                      md={4}
                      lg={4}
                      key={index}
                      container
                      direction="column"
                      alignItems="center"
                    >
                      <GroupCard
                        name={group.name}
                        labels={group.labels.map((index) => index.name)}
                        description={group.description}
                        buttonAction={() => this.joinGroup(group.id)}
                        buttonLabel={
                          group.currentUserIsMember
                            ? "Ver grupo"
                            : "Sumarme al grupo"
                        }
                      />
                    </Grid>
                  ))}
                </Grid>
              </Grid>
            </Grid>
          </Grid>
        </Container>
      </div>
    );
  }
}

UserPage.contextType = AppContext;

export default withRouter(UserPage);
