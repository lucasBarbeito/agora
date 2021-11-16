import { Component } from "react";
import {
  CircularProgress,
  Grid,
  IconButton,
  MenuItem,
  Typography,
} from "@material-ui/core";
import "./Notification.css";
import { AppContext } from "../../app-context";
import { withRouter } from "react-router";
import AddCommentIcon from "@material-ui/icons/AddComment";
import AddIcon from "@material-ui/icons/Add";
import BookmarkIcon from "@material-ui/icons/Bookmark";
import DraftsIcon from "@material-ui/icons/Drafts";
import baseUrl from "../../baseUrl";

const notificationsIcons = {
  USER_INVITE_NOTIFICATION: <AddCommentIcon id="notification-icon" />,
  NEW_MEMBER_NOTIFICATION: <AddIcon id="notification-icon" />,
  NEW_POST_NOTIFICATION: <BookmarkIcon id="notification-icon" />,
};

class Notification extends Component {
  constructor(props) {
    super(props);
    this.state = {
      name: "",
      surname: "",
      group: "",
      fetchingData: false,
    };
  }

  componentDidMount() {
    if (this.props.type === "NEW_MEMBER_NOTIFICATION") {
      this.getUser();
    } else if (this.props.type === "USER_INVITE_NOTIFICATION") {
      this.setState({
        name: this.context.userInfo.name,
        surname: this.context.userInfo.surname,
      });
    }
    this.getGroup();
  }

  getUser = async () => {
    this.setState({ fetchingData: true });
    const response = await fetch(`${baseUrl}/user/${this.props.userId}`, {
      headers: {
        "Content-type": "application/json; charset=UTF-8",
        Authorization: `Bearer ${this.context.token}`,
      },
    });
    const res = await response.json();
    if (res.length !== 0) {
      this.setState({
        name: res.name,
        surname: res.surname,
      });
    }
  };

  getGroup = async () => {
    const response = await fetch(
      `${baseUrl}/studyGroup/${this.props.groupId}`,
      {
        method: "GET",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
          Authorization: `Bearer ${this.context.token}`,
        },
      }
    );
    const group = await response.json();

    this.setState({
      group: group.name,
    });
    this.setState({ fetchingData: false });
  };

  acceptInviteToGroup = (groupId) => {
    this.props.history.push(`/group/${groupId}`);
    this.props.closeDrawer();
  };

  message = {
    USER_INVITE_NOTIFICATION: ", has sido invitado al grupo ",
    NEW_MEMBER_NOTIFICATION: " se ha unido al grupo ",
    NEW_POST_NOTIFICATION: "Nuevo anuncio en el grupo ",
  };

  ifNotificationsHasNotBeenReaded(func) {
    const notificationHasNotBeenReaded = !this.props.read;
    if (notificationHasNotBeenReaded) {
      func(this.props.id);
    }
  }

  render() {
    return (
      <Grid
        id={this.props.read ? "notification-read" : "notification"}
        container
        direction="row"
        alignItems="center"
        justifyContent="space-between"
      >
        {this.state.fetchingData && (
          <Grid
            item
            xs={12}
            align="center"
            id="notification-progress-container"
          >
            <CircularProgress id="notification-progress" size={20} />
          </Grid>
        )}
        {!this.state.fetchingData && (
          <>
            <MenuItem
              id="notification-menu-item"
              onClick={() => {
                this.ifNotificationsHasNotBeenReaded(
                  this.props.handleNotificationClick
                );
                this.props.close();
                this.props.history.push(`/group/${this.props.groupId}`);
              }}
            >
              <Grid item xs={2}>
                {notificationsIcons[this.props.type]}
              </Grid>
              <Grid item xs={8} zeroMinWidth>
                <Typography id="notification-message">
                  {this.message[this.props.type] !== "NEW_POST_NOTIFICATION" &&
                    this.state.name + " " + this.state.surname}
                  {this.message[this.props.type]}
                  {this.state.group}
                </Typography>
              </Grid>
            </MenuItem>
            <Grid item xs={2}>
              <IconButton
                onClick={() =>
                  this.ifNotificationsHasNotBeenReaded(
                    this.props.readNotification
                  )
                }
              >
                <DraftsIcon id="notification-icon" />
              </IconButton>
            </Grid>
          </>
        )}
      </Grid>
    );
  }
}

Notification.contextType = AppContext;

export default withRouter(Notification);
