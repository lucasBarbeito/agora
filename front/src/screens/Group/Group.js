import React, {Component} from 'react';
import { Button, Divider, Grid, Paper, Typography } from "@material-ui/core";
import './Group.css';

class Group extends Component {

  constructor(props) {
    super(props);
    this.state = {
        name: '',
        description: '',
    }
  }

  render() {
    return (
      <div className='container'>
        <Paper id="group-info">
          <Typography id="group-name" variant="h5">{this.props.history.location.state.name}</Typography>
          <Typography id="group-creation">Creado el {this.props.history.location.state.creationDate} por {'User name'}</Typography>
          <Divider />
          <Typography id="group-description">{this.props.history.location.state.description}</Typography>
          <Grid container justifyContent="flex-end">
            <Button id="abandon-group-button">Abandonar grupo</Button>
          </Grid>
        </Paper>
      </div>
    );
  }

}

export default Group;
