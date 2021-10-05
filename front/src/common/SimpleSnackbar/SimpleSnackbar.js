import React, { Component } from 'react';
import {
  IconButton,
  Snackbar,
  Typography,
} from '@material-ui/core';
import CloseIcon from '@material-ui/icons/Close';

class SimpleSnackbar extends Component {

  render() {
    return (
      <Snackbar
        anchorOrigin={{
          vertical: 'bottom',
          horizontal: 'left',
        }}
        open={this.props.open}
        autoHideDuration={5000}
        onClose={this.props.handleClose}
        message={<Typography variant="h5">{ this.props.message }</Typography>}
        action={[
          <IconButton
            color="inherit"
            onClick={this.props.handleClose}
          >
            <CloseIcon />
          </IconButton>,
        ]}
      />
    );
  }
}

export default SimpleSnackbar;