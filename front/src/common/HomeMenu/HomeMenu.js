import { Component } from "react";
import { Box, MenuItem, MenuList } from "@material-ui/core";
import "./HomeMenu.css";
import { withRouter } from "react-router";
import PropTypes from "prop-types";

class HomeMenu extends Component {

  static propTypes = {
    history: PropTypes.object.isRequired
  };

  render() {
    const currentPath = this.props.history.location.pathname;

    const goTo = (path) => {
      if (currentPath !== path) {
        this.props.history.push(path);
      }
    };

    return (
      <Box className="menu-box">
        <MenuList>
          <MenuItem
            id={currentPath == "/home" ? "selected-menu-text" : "menu-text"}
            selected={currentPath == "/home"}
            onClick={() => goTo("/home")}
          >
            Todos los grupos
          </MenuItem>

          <MenuItem
            id={
              currentPath == "/my-groups" ? "selected-menu-text" : "menu-text"
            }
            selected={currentPath == "/my-groups"}
            onClick={() => goTo("/my-groups")}
          >
            Mis grupos
          </MenuItem>

          <MenuItem
            id={
              currentPath == "/create-group"
                ? "selected-menu-text"
                : "menu-text"
            }
            selected={currentPath == "/create-group"}
            onClick={() => goTo("/create-group")}
          >
            Crear grupo
          </MenuItem>

          <MenuItem
            id={currentPath == "/profile" ? "selected-menu-text" : "menu-text"}
            selected={currentPath == "/profile"}
            onClick={() => goTo("/profile")}
          >
            Mi perfil
          </MenuItem>
        </MenuList>
      </Box>
    );
  }
}

export default withRouter(HomeMenu);
