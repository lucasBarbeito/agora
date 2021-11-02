import { Component } from "react";
import { Grid, IconButton, MenuItem, Typography } from "@material-ui/core";
import "./Notification.css";
import { AppContext } from "../../app-context";
import { withRouter } from "react-router";
import AddCommentIcon from "@material-ui/icons/AddComment";
import AddIcon from "@material-ui/icons/Add";
import BookmarkIcon from "@material-ui/icons/Bookmark";
import DraftsIcon from "@material-ui/icons/Drafts";
import baseUrl from "../../baseUrl";

const notificationsIcons = {
  groupInvite: <AddCommentIcon id="notification-icon" />,
  NEW_MEMBER_NOTIFICATION: <AddIcon id="notification-icon" />,
  newPost: <BookmarkIcon id="notification-icon" />,
};

class Notification extends Component {
  constructor(props) {
    super(props);
    this.state = {
      name: "",
      surname: "",
      group: "",
    };
  }

  componentDidMount() {
    this.getUser();
    this.getGroup();
  }

  getUser = async () => {
    const response = await fetch(`${baseUrl}/user`, {
      headers: {
        "Content-type": "application/json; charset=UTF-8",
        Authorization: `Bearer ${this.context.token}`,
      },
    });
    const res = await response.json();
    if (res.length !== 0) {
      this.setState({
        name: res.find(item => item.id === this.props.userId).name,
        surname: res.find(item => item.id === this.props.userId).surname
      });
    }
  };

  getGroup = async () => {

    const response = await fetch(`${baseUrl}/studyGroup/paged?page=0`, {
      method: "GET",
      headers: {
        "Content-type": "application/json; charset=UTF-8",
        Authorization: `Bearer ${this.context.token}`,
      },
    });
    const page1 = await response.json();

    const res = await fetch(`${baseUrl}/studyGroup/paged?page=1`, {
      method: "GET",
      headers: {
        "Content-type": "application/json; charset=UTF-8",
        Authorization: `Bearer ${this.context.token}`,
      },
    });
    const page2 = await res.json();

    this.setState({ group: page1.content.concat(page2.content).find(item => item.id === this.props.groupId).name });
  };

  message = {
    groupInvite: " ,has sido invitado al grupo ",
    NEW_MEMBER_NOTIFICATION: " se ha unido al grupo ",
    newPost: " ha enviado un nuevo anuncio al grupo ",
  };

  render() {
    return (
      <Grid
        id={this.props.read ? "notification-read" : "notification"}
        container
        direction="row"
        alignItems="center"
        justifyContent="space-between"
      >
        <MenuItem
          id="notification-menu-item"
          onClick={() => this.props.handleNotificationClick(this.props.id)}
        >
          <Grid item xs={2}>
            {notificationsIcons[this.props.type]}
          </Grid>
          <Grid item xs={8} zeroMinWidth>
            <Typography id="notification-message">
              {this.state.name + " " +this.state.surname}
              {this.message[this.props.type]}
              {this.state.group}
            </Typography>
          </Grid>
        </MenuItem>
        <Grid item xs={2}>
          <IconButton
            onClick={() => this.props.readNotification(this.props.id)}
          >
            <DraftsIcon id="notification-icon" />
          </IconButton>
        </Grid>
      </Grid>
    );
  }
}

Notification.contextType = AppContext;

export default withRouter(Notification);
