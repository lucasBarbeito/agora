import { Component } from "react";
import { Divider, Drawer, Grid, MenuList, Typography } from "@material-ui/core";
import "./NotificationDrawer.css";
import { AppContext } from "../../app-context";
import { withRouter } from "react-router";
import Notification from "../Notification/Notification";

class NotificationDrawer extends Component {
  constructor(props) {
    super(props);
  }

  readNotification = (id) => {
    const updatedNotifications = this.context.notifications;
    const notificationIndex = updatedNotifications.findIndex(
      (n) => n.notificationTypeId === id
    );
    updatedNotifications[notificationIndex].read = true;
    this.setState({ notifications: updatedNotifications });
  };

  render() {
    return (
      <Drawer
        anchor={"right"}
        open={this.props.open}
        onClose={this.props.onClose}
      >
        <div className="top-frame" />
        <Grid id="drawer-header" container item direction="column">
          <Typography variant="h6">Mis Notificaciones</Typography>
          <Typography id="notification-name">{`${this.context.userInfo.name} ${this.context.userInfo.surname}`}</Typography>
        </Grid>
        <Divider />
        <MenuList>
          {this.context.notifications.map((notification, index) => {
            return (
              <Notification
                key={index}
                read={notification.read}
                id={notification.notificationTypeId}
                userId={notification.userRecipientId}
                groupId={notification.studyGroupId}
                type={notification.notificationType}
                readNotification={this.readNotification}
                history={this.props.history}
                closeDrawer={this.props.closeDrawer}
              />
            );
          })}
        </MenuList>
      </Drawer>
    );
  }
}

NotificationDrawer.contextType = AppContext;

export default withRouter(NotificationDrawer);
