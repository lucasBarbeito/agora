import React, { Component } from "react";
import {
  Button,
  CircularProgress,
  Container,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Divider,
  Grid,
  IconButton,
  Paper,
  Typography,
  TextField,
} from "@material-ui/core";

import "./User.css";
import baseUrl from "../../baseUrl";
import { UserContext } from "../../user-context";
import { withRouter } from "react-router-dom";

class User extends Component {
  render() {
    return <></>;
  }
}

User.contextType = UserContext;

export default withRouter(User);
