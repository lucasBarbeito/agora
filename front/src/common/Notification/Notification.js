import { Component } from "react";
import { Grid, IconButton, MenuItem, Typography } from "@material-ui/core";
import "./Notification.css";
import { AppContext } from "../../app-context";
import { withRouter } from "react-router";
import AddCommentIcon from "@material-ui/icons/AddComment";
import AddIcon from "@material-ui/icons/Add";
import BookmarkIcon from "@material-ui/icons/Bookmark";
import DraftsIcon from "@material-ui/icons/Drafts";

const notificationsIcons = {
  groupInvite: <AddCommentIcon id="notification-icon" />,
  newMember: <AddIcon id="notification-icon" />,
  newPost: <BookmarkIcon id="notification-icon" />,
};

class Notification extends Component {
  constructor(props) {
    super(props);
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
        <MenuItem
          id="notification-menu-item"
          onClick={() => this.props.handleNotificationClick(this.props.id)}
        >
          <Grid item xs={2}>
            {notificationsIcons[this.props.type]}
          </Grid>
          <Grid item xs={8} zeroMinWidth>
            <Typography id="notification-message">
              {this.props.message}
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
