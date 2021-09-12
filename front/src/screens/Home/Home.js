import { Component } from 'react';
import { Container, Grid, IconButton, TextField } from '@material-ui/core';
import Autocomplete from '@material-ui/lab/Autocomplete';
import SearchIcon from '@material-ui/icons/Search';
import './Home.css';
import GroupCard from '../../common/GroupCard/GroupCard';
import HomeMenu from '../../common/HomeMenu/HomeMenu';

const groups = [
  {
    groupId: 1,
    groupTitle: 'Lorem Ipsum',
    groupTags: ['Etiqueta 1', 'Etiqueta 2'],
    groupDescription: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit',
  },
  {
    groupId: 2,
    groupTitle: 'Excepteur sint',
    groupTags: ['Etiqueta 1', 'Etiqueta 2', 'Etiqueta 3'],
    groupDescription: 'Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua',
  },
  {
    groupId: 3,
    groupTitle: 'Excepteur sint',
    groupTags: ['Etiqueta 1', 'Etiqueta 2', 'Etiqueta 3'],
    groupDescription: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. ',
  },
  {
    groupId: 4,
    groupTitle: 'Excepteur sint',
    groupTags: ['Etiqueta 1', 'Etiqueta 2', 'Etiqueta 3'],
    groupDescription: 'Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua',
  },
  {
    groupId: 5,
    groupTitle: 'Excepteur sint occaecat cupidatat non proident, sunt in culpa',
    groupTags: ['Etiqueta 1', 'Etiqueta 2', 'Etiqueta 3'],
    groupDescription: 'Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua',
  },
];

const tags = ['Etiqueta 1', 'Etiqueta con mucho texto', 'Etiqueta 3'];

class Home extends Component {

  constructor(props) {
    super(props);
    this.state = {
      groupName: '',
      tags: [],
    }
  }

  searchGroups() {
    console.log(`searching group named ${this.state.groupName} with labels: ${this.state.tags}`);
  }

  joinGroup(id) {
    this.props.history.push(`/group/${id}`)
  }

  render() {
    return (
      <Container id='Home'>
        <Grid container spacing={2}>
          <Grid item sm={12} md={3} container direction="column" alignItems="flex-end">
            <HomeMenu history={this.props.history}/>
          </Grid>
          <Grid item md={12} lg={9} id='groups-listing'>
            <Grid container spacing={3}>
              <Grid item xs={12}>
                <div className='search-content'>
                  <TextField
                    id='group-name-field'
                    label='Buscar nombre de grupo'
                    variant="outlined"
                    value={this.state.groupName}
                    onChange={(value) => this.setState({groupName: value.target.value})}
                  />
                  <div id='group-label-field'>
                    <Autocomplete
                      multiple
                      options={tags}
                      filterSelectedOptions
                      onChange={(event, newValue) => {
                        this.setState({tags: newValue});
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
                    <SearchIcon/>
                  </IconButton>
                </div>
              </Grid>
              {
                groups.map((group, index) => (
                  <Grid item xs={12} sm={6} md={4} lg={4} key={index} container direction="column" alignItems="center">
                    <GroupCard
                      name={group.groupTitle}
                      tags={group.groupTags}
                      description={group.groupDescription}
                      buttonAction={() => this.joinGroup(group.groupId)}
                      buttonLabel={'Sumarme al grupo'}
                    />
                  </Grid>
                ))
              }
            </Grid>
          </Grid>
        </Grid>
      </Container>
    )
  }
}

export default Home;