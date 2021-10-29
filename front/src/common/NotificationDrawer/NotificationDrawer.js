import { Component } from "react";
import { Divider, Drawer, Grid, MenuList, Typography } from "@material-ui/core";
import "./NotificationDrawer.css";
import { AppContext } from "../../app-context";
import { withRouter } from "react-router";
import Notification from "../Notification/Notification";

const notifications = [
  {
    id: 1,
    type: "groupInvite",
    message: "Has sido invitado al grupo ${Nombre de grupo}",
    read: false,
  },
  {
    id: 2,
    type: "newMember",
    message:
      "${Nombre y apellido de usuario} se ha unido al grupo ${Nombre de grupo}",
    read: false,
  },
  {
    id: 3,
    type: "newPost",
    message:
      "${Nombre y apellido de usuario} ha enviado un nuevo anuncio al grupo ${Nombre de grupo}",
    read: true,
  },
];

class NotificationDrawer extends Component {
  constructor(props) {
    super(props);

    this.state = {
      notifications: notifications,
    };
  }

  readNotification = (id) => {
    const updatedNotifications = this.state.notifications;
    const notificationIndex = updatedNotifications.findIndex(
      (n) => n.id === id
    );
    updatedNotifications[notificationIndex].read = true;
    this.setState({ notifications: updatedNotifications });
  };

  handleNotificationClick = (id) => {
    this.readNotification(id);
    console.log(`Notification ${id} has been clicked`);
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
              id={notification.id}
              message={notification.message}
              type={notification.type}
              handleNotificationClick={this.handleNotificationClick}
              readNotification={this.readNotification}
            />
          ))}
        </MenuList>
      </Drawer>
    );
  }
}

NotificationDrawer.contextType = AppContext;

export default withRouter(NotificationDrawer);
