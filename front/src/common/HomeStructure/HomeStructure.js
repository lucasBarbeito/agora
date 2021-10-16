import { Component } from "react";
import { Container, Grid, IconButton, TextField } from "@material-ui/core";
import "./HomeStructure.css";
import HomeMenu from "../../common/HomeMenu/HomeMenu";
import { withRouter } from "react-router";
import PropTypes from "prop-types";

class HomeStructure extends Component {

  static propTypes = {
    history: PropTypes.object.isRequired
  };

  render() {
    return (
      <Container id="main-container">
        <Grid container spacing={2}>
          <Grid item xs={3} container direction="column" alignItems="flex-end">
            <HomeMenu />
          </Grid>
          <Grid item xs={9} id="child-grid">
            {this.props.children}
          </Grid>
        </Grid>
      </Container>
    );
  }
}

export default withRouter(HomeStructure);
