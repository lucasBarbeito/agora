import React from "react";
import { Chip, Typography } from "@material-ui/core";
import Menu from "@material-ui/core/Menu";
import AccountCircleIcon from "@material-ui/icons/AccountCircle";
import MenuItem from "@material-ui/core/MenuItem";
import baseUrl from "../../baseUrl";
import { withRouter } from "react-router";
import PropTypes from "prop-types";



export const ProfileButton = (props) => {
  const [anchorEl, setAnchorEl] = React.useState(null);

  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const propTypes = {
    history: PropTypes.object.isRequired
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleMyProfileClick = () => {
    setAnchorEl(null);
    props.history.push("/profile");
  };

  const handleLogOutClick = async () => {
    setAnchorEl(null);

    try{
      const response = await fetch(`${baseUrl}/auth`, {
        method: "DELETE",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
          Authorization: `Bearer ${props.token}`,
        },
      })
      if(response.ok){
        props.setToken(null,"/")
      }else{ 
        props.handleLoginError(true)
      }
    }catch (e){
      alert("Error, no es posible conectarse al back-end");
    }
  };

  return (
    <div>
      <Chip
        id="chip"
        avatar={<AccountCircleIcon />}
        label={<Typography id="name">{props.name + " " + props.surname}</Typography>}
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
export default withRouter(ProfileButton);
