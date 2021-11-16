import { Component } from "react";
import { Divider, Drawer, Grid, MenuList, Typography } from "@material-ui/core";
import "./NotificationDrawer.css";
import { AppContext } from "../../app-context";
import { withRouter } from "react-router";
import Notification from "../Notification/Notification";
import SimpleSnackbar from "../SimpleSnackbar/SimpleSnackbar";
import baseUrl from "../../baseUrl";

class NotificationDrawer extends Component {
  constructor(props) {
    super(props);
    this.state = {
      notifications: this.props.notifications,
      openReadNotificationErrorSnack: false,
    };
  }

  componentDidUpdate(prevProps) {
    if (prevProps.notifications !== this.props.notifications) {
      this.setState({
        notifications: this.props.notifications,
      });
    }
  }

  readNotification = async (id) => {
    const updatedNotifications = this.context.notifications;

    try {
      const response = await fetch(`${baseUrl}/notification/${id}/read`, {
        method: "POST",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
          Authorization: `Bearer ${this.context.token}`,
        },
      });

      if (!response.ok) {
        this.setState({ openReadNotificationErrorSnack: true });
      } else {
        const notificationIndex = updatedNotifications.findIndex(
          (n) => n.notificationId === id
        );
        updatedNotifications[notificationIndex].read = true;
        this.setState({ notifications: updatedNotifications });
      }
    } catch (e) {
      alert("Error, no es posible conectarse al back-end");
    }
  };

  handleNotificationClick = (id) => {
    this.readNotification(id);
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
          {this.state.notifications.map((notification, index) => (
            <Notification
              key={index}
              read={notification.read}
              id={notification.notificationId}
              userId={notification.notificationTypeId}
              groupId={notification.studyGroupId}
              type={notification.notificationType}
              handleNotificationClick={this.handleNotificationClick}
              readNotification={this.readNotification}
              close={this.props.closeDrawer}
            />
          ))}
        </MenuList>
        <SimpleSnackbar
          open={this.state.openReadNotificationErrorSnack}
          handleClose={() =>
            this.setState({ openReadNotificationErrorSnack: false })
          }
          message="Hubo un error al marcar la notificación como leída"
        />
      </Drawer>
    );
  }
}

NotificationDrawer.contextType = AppContext;

export default withRouter(NotificationDrawer);
