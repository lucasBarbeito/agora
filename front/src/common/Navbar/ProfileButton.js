import React from "react";
import { Chip, Typography } from "@material-ui/core";
import Menu from "@material-ui/core/Menu";
import AccountCircleIcon from "@material-ui/icons/AccountCircle";
import MenuItem from "@material-ui/core/MenuItem";
import history from "../../history";

export const ProfileButton = ({ name, surname }) => {
  const [anchorEl, setAnchorEl] = React.useState(null);

  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleMyProfileClick = () => {
    setAnchorEl(null);
    history.push("/profile");
  };

  const handleLogOutClick = () => {
    setAnchorEl(null);
    alert("Cerrando sesión");
    //TODO
  };

  return (
    <div>
      <Chip
        id="chip"
        avatar={<AccountCircleIcon />}
        label={<Typography id="name">{name + " " + surname}</Typography>}
        clickable
        onClick={handleClick}
      />
      <Menu
        id="simple-menu"
        anchorEl={anchorEl}
        keepMounted
        open={Boolean(anchorEl)}
        onClose={handleClose}
      >
        <MenuItem onClick={handleMyProfileClick}>Mi perfil</MenuItem>
        <MenuItem onClick={handleLogOutClick}>Cerrar sesión</MenuItem>
      </Menu>
    </div>
  );
};
export default ProfileButton;
