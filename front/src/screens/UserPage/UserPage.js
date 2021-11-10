import React, { Component } from "react";
import {
  Button,
  CircularProgress,
  Container,
  Divider,
  Grid,
  Paper,
  Typography,
} from "@material-ui/core";
import ArrowBackIosIcon from "@material-ui/icons/ArrowBackIos";
import "./UserPage.css";
import { AppContext } from "../../app-context";
import { withRouter } from "react-router-dom";
import baseUrl from "../../baseUrl";
import MemberContact from "../../common/MemberContact/MemberContact";
import GroupCard from "../../common/GroupCard/GroupCard";

class UserPage extends Component {
  constructor(props) {
    super(props);
    this.state = {
      fetchingUserData: true,
      userData: [],
    };
  }

  componentDidMount() {
    this.getUser();
  }

  joinGroup(id) {
    this.props.history.push(`/group/${id}`);
  }

  getUser = async () => {
    this.setState({ fetchingUserData: true });

    const { token } = this.context;
    const userId = this.props.match.params.id;

    try {
      const response = await fetch(`${baseUrl}/user/${userId}`, {
        method: "GET",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.ok) {
        const res = await response.json();
        this.setState({ userData: res });
      } else if (response.status === 404) {
        this.props.history.push(`/404`);
      }

      this.setState({ fetchingUserData: false });
    } catch (e) {
      alert("Error, no es posible conectarse al back-end");
    }
  };

  render() {
    return (
      <div className="userpage-main-div">
        <Container id="userpage-main-container" maxWidth="lg">
          {this.state.fetchingUserData && (
            <Grid item container direction="column" alignItems="center">
              <CircularProgress />
            </Grid>
          )}
          {!this.state.fetchingUserData && (
            <Grid container spacing={2}>
              <Grid container item xs={3}>
                <Grid container direction="column" spacing={2}>
                  <Grid item>
                    <Button
                      fullWidth
                      id="user-back-button"
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
                            {`${this.state.userData.name} ${this.state.userData.surname}`}
                          </Typography>
                          <Typography id="userpage-user-creation" />
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
                          type={"email"}
                          value={this.state.userData.email}
                        />
                      </Grid>
                      <br />
                    </Paper>
                  </Grid>
                </Grid>
              </Grid>
              <Grid container item xs={9}>
                <Grid container direction="column">
                  <Typography id="userpage-groups-text" variant="h6">
                    Grupos en los que{" "}
                    {`${this.state.userData.name} ${this.state.userData.surname}`}{" "}
                    es miembro
                  </Typography>
                  <Grid container spacing={3}>
                    <Grid item xs={12} />
                    {this.state.userData.userGroups.map((group, index) => (
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
          )}
        </Container>
      </div>
    );
  }
}

UserPage.contextType = AppContext;

export default withRouter(UserPage);
