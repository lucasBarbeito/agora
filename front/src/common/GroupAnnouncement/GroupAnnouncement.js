import { Component } from "react";
import { 
  Card, 
  CardContent, 
  Container, 
  IconButton, 
  Typography 
} from "@material-ui/core";
import "./GroupAnnouncement.css";
import CancelIcon from '@material-ui/icons/Cancel';

class GroupAnnouncement extends Component {
  render() {
    return (
      <Card id="group-announcement">
        <CardContent>
          <Container id="announcement-container">
            <Container id="cancel-button-and-title">
              { 
                this.props.canDelete && 
                <IconButton id="delete-button" onClick={() => this.props.handleDelete(this.props.id)}>
                  <CancelIcon id="delete-icon"/>
                </IconButton>
              }
              <Typography id="announcement-name" variant="h5" component="h2">
                { this.props.name }
              </Typography>
            </Container>
            <Typography id="announcement-date" color="textSecondary" component="p">
              { this.props.date }
            </Typography>
          </Container>
          <Typography id="announcement-content" color="textSecondary" component="p">
            { this.props.content }
          </Typography>
        </CardContent>
    </Card>
    );
  }
}

export default GroupAnnouncement;
