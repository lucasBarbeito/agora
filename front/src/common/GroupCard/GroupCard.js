import { Component } from "react";
import {
  Button,
  Card,
  CardActions,
  CardContent,
  Grid,
  Typography,
} from "@material-ui/core";
import "./GroupCard.css";

class GroupCard extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <Card id="GroupCard">
        <CardContent id="content">
          <Typography id="name" noWrap>
            {this.props.name}
          </Typography>
          <Typography id='tags' noWrap>{this.props.labels.join(', ')}</Typography>
          <Typography id="description">{this.props.description}</Typography>
        </CardContent>
        <CardActions>
          <Grid container justifyContent="flex-end">
            <Button
              id="join-group-button"
              onClick={() => this.props.buttonAction()}
            >
              {this.props.buttonLabel}
            </Button>
          </Grid>
        </CardActions>
      </Card>
    );
  }
}

export default GroupCard;
