import React from "react";
import { Button } from "@material-ui/core";
import { withRouter } from "react-router";
import PropTypes from "prop-types";

 function GoBackButton(props) {

  const propTypes = {
    history: PropTypes.object.isRequired
  };
  
  return (
    <Button
      id="button"
      variant="contained"
      color="primary"
      onClick={() => props.history.goBack()}
    >
      VOLVER ATRÁS
    </Button>
  );
}
export default withRouter(GoBackButton);
