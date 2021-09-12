import React, {Component} from 'react';
import { Button, Divider, Grid, Paper, Typography } from "@material-ui/core";
import './Group.css';

class Group extends Component {

  render() {
    return (
      <div className='container'>
        <Paper id="group-info">
          <Typography id="group-name" variant="h5">{"Nombre del grupo"}</Typography>
          <Typography id="group-creation">Creado el {"10/08/2021"} por {'User name'}</Typography>
          <Divider />
          <Typography id="group-description">{"Descripción del grupo"}</Typography>
          <Grid container justifyContent="flex-end">
            <Button id="abandon-group-button">Abandonar grupo</Button>
          </Grid>
        </Paper>
      </div>
    );
  }

}

export default Group;
