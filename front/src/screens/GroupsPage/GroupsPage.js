import { Component } from "react";
import { Grid, IconButton, TextField } from "@material-ui/core";
import Autocomplete from "@material-ui/lab/Autocomplete";
import SearchIcon from "@material-ui/icons/Search";
import "./GroupsPage.css";
import GroupCard from "../../common/GroupCard/GroupCard";
import HomeStructure from "../../common/HomeStructure/HomeStructure.js";
import { AppContext } from "../../app-context";
import baseUrl from "../../baseUrl";

class GroupsPage extends Component {
  constructor(props) {
    super(props);
    this.state = {
      groupName: "",
      tags: [],
      studyGroups: [],
    };
  }

  getGroups = async () => {
    const { token } = this.context;
    try {
      const response = await fetch(`${baseUrl}/studyGroup`, {
        method: "GET",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
          Authorization: `Bearer ${token}`,
        },
      });
      this.setState({
        studyGroups: await response.json(),
      });
    } catch (e) {
      alert("Error, no es posible conectarse al back-end");
    }
  };

  getMyGroups = async () => {
    const { token } = this.context;
    try {
      const response = await fetch(`${baseUrl}/studyGroup/me`, {
        method: "GET",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
          Authorization: `Bearer ${token}`,
        },
      });
      this.setState({
        studyGroups: await response.json(),
      });
    } catch (e) {
      alert("Error, no es posible conectarse al back-end");
    }
  };

  checkIfOnlyMyGroups() {
    if (this.props.onlyMyGroups) {
      this.getMyGroups();
    } else {
      this.getGroups();
    }
  }

  componentDidMount() {
    this.checkIfOnlyMyGroups();
  }

  componentDidUpdate(prevProps) {
    if (this.props.onlyMyGroups !== prevProps.onlyMyGroups) {
      this.checkIfOnlyMyGroups();
    }
  }

  async searchGroups() {
    const esc = encodeURIComponent;
    try {
      const response = await fetch(
        `${baseUrl}/studyGroup?text=${esc(this.state.groupName)}`,
        {
          headers: {
            "Content-type": "application/json; charset=UTF-8",
            Authorization: `Bearer ${this.context.token}`,
          },
        }
      );
      this.setState({
        studyGroups: await response.json(),
      });
    } catch (e) {
      alert("Error, no es posible conectarse al back-end");
    }
  }

  joinGroup(id) {
    this.props.history.push(`/group/${id}`);
  }

  render() {
    return (
      <HomeStructure history={this.props.history}>
        <Grid container spacing={3}>
          <Grid item xs={12}>
            <div className="search-content">
              <TextField
                id="group-name-field"
                label="Buscar nombre de grupo"
                variant="outlined"
                value={this.state.groupName}
                onChange={(value) =>
                  this.setState({
                    groupName: value.target.value,
                  })
                }
              />
              <div id="group-label-field">
                <Autocomplete
                  multiple
                  options={this.context.labels.map(index => index.name)}
                  filterSelectedOptions
                  onChange={(event, newValue) => {
                    this.setState({ tags: this.context.labels.filter(item => newValue.includes(item.name))});
                  }}
                  renderInput={(params) => (
                    <TextField
                      {...params}
                      variant="outlined"
                      label="Etiquetas"
                    />
                  )}
                />
              </div>
              <IconButton
                id="search-button"
                onClick={() => this.searchGroups()}
              >
                <SearchIcon />
              </IconButton>
            </div>
          </Grid>
          {this.state.studyGroups.map((group, index) => (
            <Grid
              item
              xs={12}
              sm={6}
              md={4}
              lg={4}
              key={index}
              container
              direction="column"
              alignItems="center"
            >
              <GroupCard
                name={group.name}
                labels={group.labels.map(index => index.name)}
                description={group.description}
                buttonAction={() => this.joinGroup(group.id)}
                buttonLabel={
                  group.currentUserIsMember ? "Ver grupo" : "Sumarme al grupo"
                }
              />
            </Grid>
          ))}
        </Grid>
      </HomeStructure>
    );
  }
}

GroupsPage.contextType = AppContext;

export default GroupsPage;
