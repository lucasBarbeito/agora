import React, {Component} from 'react';
import { Button, Divider, Grid, Paper, Typography } from "@material-ui/core";
import './Group.css';

class Group extends Component {

  constructor(props) {
    super(props);
    this.state = {
      name: props.history.location.state ? props.history.location.state.name : props.name,
      creationDate: props.history.location.state ? props.history.location.state.creationDate : props.creationDate,
      description: props.history.location.state ? props.history.location.state.description : props.description,
    }
  }

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
