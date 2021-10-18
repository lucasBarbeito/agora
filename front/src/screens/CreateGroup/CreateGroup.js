import React, { Component } from "react";
import { Box, Button, Grid } from "@material-ui/core";
import "./CreateGroup.css";
import FormControl from "@material-ui/core/FormControl";
import InputLabel from "@material-ui/core/InputLabel";
import OutlinedInput from "@material-ui/core/OutlinedInput";
import Autocomplete from "@material-ui/lab/Autocomplete";
import TextField from "@material-ui/core/TextField";
import { AppContext } from "../../app-context";
import HomeStructure from "../../common/HomeStructure/HomeStructure.js";
import baseUrl from "../../baseUrl";
import { withRouter } from "react-router";
import PropTypes from "prop-types";

class CreateGroup extends Component {
  constructor(props) {
    super(props);
    this.state = {
      groupName: "",
      label: [],
      description: "",
      createdUnsuccessfully: false,
      errorMsg: "",
    };
  }

  static propTypes = {
    history: PropTypes.object.isRequired
  };

  render() {


    const createGroup = async () => {
      if (!this.state.groupName) {
        this.setState({
          createdUnsuccessfully: true,
          errorMsg: "Por favor ingrese un nombre de grupo",
        });
      } else if (this.state.label.length === 0) {
        this.setState({
          createdUnsuccessfully: true,
          errorMsg: "Por favor seleccione al menos una etiqueta",
        });
      } else if (!this.state.description) {
        this.setState({
          createdUnsuccessfully: true,
          errorMsg: "Por favor ingrese una descripción",
        });
      } else {
        this.setState({ createdUnsuccessfully: false });
        let date = new Date();
        const { token } = this.context;
        try {
          const response = await fetch(`${baseUrl}/studyGroup`, {
            method: "POST",
            body: JSON.stringify({
              creationDate: date.toISOString(), //"2011-12-19T15:28:46.493Z"
              creatorId: this.context.userInfo.id, //
              description: this.state.description,
              name: this.state.groupName,
              labels: this.state.label
            }),
            headers: {
              "Content-type": "application/json; charset=UTF-8",
              Authorization: `Bearer ${token}`,
            },
          });
          if (response.status === 409) {
            this.setState({
              errorMsg: "Grupo creado con nombre ya existente",
              createdUnsuccessfully: true,
            });
          } else if (response.status === 404) {
            this.setState({
              errorMsg: "Tu usuario no existe.",
              createdUnsuccessfully: true,
            });
          } else if (!response.ok) {
            this.setState({
              errorMsg: "Ha ocurrido un error.",
              createdUnsuccessfully: true,
            });
          } else {
            const res = await response.json();
            this.props.history.push({
              pathname: `/group/${res.id}`,
            });
          }
        } catch (e) {
          alert("Error, no es posible conectarse al back-end");
        }
      }
    };
    return (
      <HomeStructure>
        <Box className="creategroup-form-box" boxShadow={2}>
          <Grid
            container
            direction="column"
            id="creategroup-box-grid"
            spacing={1}
          >
            <Grid item>
              <h1 className="creategroup-title">Crear nuevo grupo de AGORA</h1>
            </Grid>
            <Grid item>
              <div className="creategroup-subtitle">
                Crear un nuevo grupo dentro de AGORA para poder aprender con
                nuevos amigos!
              </div>
            </Grid>
            <Grid item>
              <FormControl
                fullWidth
                variant="outlined"
                id="creategroup-form-control"
              >
                <InputLabel htmlFor="outlined-adornment-amount">
                  Nombre
                </InputLabel>
                <OutlinedInput
                  id="creategroup-outlined-adornment-amount"
                  value={this.state.groupName}
                  label="Nombre"
                  labelWidth={60}
                  onChange={(value) =>
                    this.setState({
                      groupName: value.target.value,
                    })
                  }
                />
              </FormControl>
            </Grid>
            <Grid item>
              <div className="creategroup-selection-form">
                <Autocomplete
                  multiple
                  id="creategroup-tags-outlined"
                  options={this.context.labels.map(index => index.name)}
                  filterSelectedOptions
                  fullWidth
                  onChange={(event, newValue) => {
                    this.setState({ label: this.context.labels.filter(item => newValue.includes(item.name))});
                  }}
                  renderInput={(params) => (
                    <TextField
                      {...params}
                      fullWidth
                      variant="outlined"
                      label="Etiquetas"
                    />
                  )}
                />
              </div>
            </Grid>
            <Grid item>
              <div className="creategroup-form-description">
                <TextField
                  id="creategroup-outlined-multiline-static"
                  label="Descripción"
                  variant="outlined"
                  multiline
                  fullWidth
                  rows={6}
                  onChange={(text) =>
                    this.setState({
                      description: text.target.value,
                    })
                  }
                />
              </div>
            </Grid>
            <Grid item>
              {this.state.createdUnsuccessfully ? (
                <Box id="creategroup-warning-box">
                  <div id="creategroup-warning-message">
                    {this.state.errorMsg}
                  </div>
                </Box>
              ) : <Box id={'creategroup-warning'} />}
            </Grid>
            <Grid item container direction="row" justifyContent="flex-end">
              <Grid item>
                <Button id="creategroup-button-color" onClick={createGroup}>
                  CREAR GRUPO
                </Button>
              </Grid>
            </Grid>
          </Grid>
        </Box>
      </HomeStructure>
    );
  }
}
CreateGroup.contextType = AppContext;

export default withRouter(CreateGroup);
