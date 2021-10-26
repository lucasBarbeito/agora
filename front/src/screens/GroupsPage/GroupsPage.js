import { Component } from "react";
import {
  Box,
  CircularProgress,
  Grid,
  IconButton,
  TextField,
} from "@material-ui/core";
import Autocomplete from "@material-ui/lab/Autocomplete";
import SearchIcon from "@material-ui/icons/Search";
import "./GroupsPage.css";
import GroupCard from "../../common/GroupCard/GroupCard";
import HomeStructure from "../../common/HomeStructure/HomeStructure.js";
import { AppContext } from "../../app-context";
import baseUrl from "../../baseUrl";
import Pagination from "@material-ui/lab/Pagination";
import { withRouter } from "react-router";
import PropTypes from "prop-types";
import SimpleSnackbar from "../../common/SimpleSnackbar/SimpleSnackbar";

class GroupsPage extends Component {
  constructor(props) {
    super(props);
    this.state = {
      groupName: "",
      tags: [],
      studyGroups: [],
      currentPage: 1,
      totalPages: 0,
      loadingStudyGroups: true,
      openGetGroupsErrorSnack: false,
    };
  }

  static propTypes = {
    history: PropTypes.object.isRequired,
  };

  componentDidMount() {
    this.searchGroups(this.props.onlyMyGroups);
  }

  componentDidUpdate(prevProps) {
    if (this.props.onlyMyGroups !== prevProps.onlyMyGroups) {
      this.setState(
        {
          groupName: "",
          tags: [],
          studyGroups: [],
          currentPage: 1,
          totalPages: 0,
          loadingStudyGroups: true,
          openGetGroupsErrorSnack: false,
        },
        () => this.searchGroups(this.props.onlyMyGroups)
      );
    }
  }

  async searchGroups(onlyMyGroups) {
    this.setState({ loadingStudyGroups: true });
    const esc = encodeURIComponent;

    let text = `?page=${this.state.currentPage - 1}`;
    if (this.state.groupName.trim() !== "") {
      text += "&text=" + esc(this.state.groupName.trim());
    }
    if (this.state.tags.length > 0) {
      text += "&label=" + this.state.tags.map((item) => item.id);
    }

    try {
      let input;
      if (onlyMyGroups) {
        input = baseUrl + "/studyGroup/me/paged" + text;
      } else {
        input = baseUrl + "/studyGroup/paged" + text;
      }

      const response = await fetch(input, {
        method: "GET",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
          Authorization: `Bearer ${this.context.token}`,
        },
      });

      if (!response.ok) {
        this.setState({ openGetGroupsErrorSnack: true });
      } else {
        const res = await response.json();
        this.setState({
          studyGroups: res.content,
          totalPages: res.totalPages,
        });
      }

      this.setState({ loadingStudyGroups: false });
    } catch (e) {
      alert("Error, no es posible conectarse al back-end");
    }
  }

  joinGroup(id) {
    this.props.history.push(`/group/${id}`);
  }

  handlePageChange = (_, page) => {
    this.setState({ currentPage: page }, () =>
      this.searchGroups(this.props.onlyMyGroups)
    );
  };

  render() {
    return (
      <HomeStructure>
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
                  options={this.context.labels.map((index) => index.name)}
                  filterSelectedOptions
                  onChange={(event, newValue) => {
                    this.setState({
                      tags: this.context.labels.filter((item) =>
                        newValue.includes(item.name)
                      ),
                    });
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
                onClick={() => {
                  this.setState({ currentPage: 1 }, () =>
                    this.searchGroups(this.props.onlyMyGroups)
                  );
                }}
              >
                <SearchIcon />
              </IconButton>
            </div>
          </Grid>
          {this.state.loadingStudyGroups && (
            <Grid item container direction="column" alignItems="center">
              <CircularProgress />
            </Grid>
          )}
          {!this.state.loadingStudyGroups &&
            this.state.studyGroups.map((group, index) => (
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
                  labels={group.labels.map((index) => index.name)}
                  description={group.description}
                  buttonAction={() => this.joinGroup(group.id)}
                  buttonLabel={
                    group.currentUserIsMember ? "Ver grupo" : "Sumarme al grupo"
                  }
                />
              </Grid>
            ))}
        </Grid>
        <Box
          display="flex"
          height={80}
          alignItems="center"
          justifyContent="center"
        >
          {!this.state.loadingStudyGroups && (
            <Pagination
              id="group-page-pagination"
              count={this.state.totalPages}
              page={this.state.currentPage}
              variant="outlined"
              onChange={this.handlePageChange}
            />
          )}
        </Box>
        <SimpleSnackbar
          open={this.state.openGetGroupsErrorSnack}
          handleClose={() => this.setState({ openGetGroupsErrorSnack: false })}
          message="Hubo un error al pedir los grupos"
        />
      </HomeStructure>
    );
  }
}

GroupsPage.contextType = AppContext;

export default withRouter(GroupsPage);
