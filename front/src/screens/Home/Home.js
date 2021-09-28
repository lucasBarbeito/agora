import { Component } from 'react';
import { Grid, IconButton, TextField } from '@material-ui/core';
import Autocomplete from '@material-ui/lab/Autocomplete';
import SearchIcon from '@material-ui/icons/Search';
import './Home.css';
import GroupCard from '../../common/GroupCard/GroupCard';
import HomeStructure from '../../common/HomeStructure/HomeStructure.js';
import baseUrl from '../../baseUrl'
import { UserContext } from '../../user-context';

const tags = ['Etiqueta 1', 'Etiqueta con mucho texto', 'Etiqueta 3'];

class Home extends Component {

  constructor(props) {
    super(props);
    this.state = {
      groupName: '',
      tags: [],
      studyGroups: []
    }
  }

  getGroups = async () => {
    const { token } = this.context;
    try {
      const response = await fetch(`${baseUrl}/studyGroup`, {
        method: 'GET',
        headers: {
          'Content-type': 'application/json; charset=UTF-8',
          'Authorization': `Bearer ${token}`
        },
      }
      );
      this.setState({
        studyGroups: await response.json(),
      });
    } catch (e) {
      alert('Error, no es posible conectarse al back-end');
    }
  }

  componentDidMount() {
    this.getGroups();
  }


  searchGroups() {
    console.log(`searching group named ${this.state.groupName} with labels: ${this.state.tags}`);
  }

  joinGroup(id) {
    this.props.history.push(`/group/${id}`)
  }

  render() {
    return (
      <HomeStructure history={this.props.history}>
        <Grid container spacing={3}>
          <Grid item xs={12}>
            <div className='search-content'>
              <TextField
                id='group-name-field'
                label='Buscar nombre de grupo'
                variant="outlined"
                value={this.state.groupName}
                onChange={(value) => this.setState({ groupName: value.target.value })}
              />
              <div id='group-label-field'>
                <Autocomplete
                  multiple
                  options={tags}
                  filterSelectedOptions
                  onChange={(event, newValue) => {
                    this.setState({ tags: newValue });
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
                id='search-button'
                onClick={() => this.searchGroups()}
              >
                <SearchIcon />
              </IconButton>
            </div>
          </Grid>
          {this.state.studyGroups.map((group, index) => (
            <Grid item xs={12} sm={6} md={4} lg={4} key={index} container direction="column"
              alignItems="center">
              <GroupCard
                name={group.name}
                //tags={group.groupTags}
                description={group.description}
                buttonAction={() => this.joinGroup(group.id)}
                buttonLabel={'Sumarme al grupo'}
              />
            </Grid>
          ))
          }
        </Grid>
      </HomeStructure>
    )
  }
}

Home.contextType = UserContext;

export default Home;
