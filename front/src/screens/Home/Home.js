import { Component } from 'react';
import { Button, Grid, TextField } from '@material-ui/core';
import GroupCard from '../../common/GroupCard/GroupCard';
import './Home.css';

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

class Home extends Component {

  render() {
    return (
      <div className="Home">
        <Grid container spacing={2}>
          <Grid item xs={3}>
            {/* Aquí va el tablero con "Todos los grupos", "Mis grupos", "Crear grupo", "Mi perfil" */}
          </Grid>
          <Grid item xs={9}>
            <Grid container spacing={3}>
              <Grid item xs={12}>
                <div className='search-content'>
                  <TextField
                    label='Buscar nombre de grupo'
                    variant="outlined"
                  />
                  <TextField 
                    variant="outlined"
                  />
                  <Button 
                    id='search-button'
                  >
                    Lupa
                  </Button>
                </div>
              </Grid>
              {
                
                groups.map((group, index) => (
                  <Grid item xs={12} sm={12} md={6} lg={4} xl={3} key={index}>
                    <GroupCard
                      name={group.groupTitle}
                      tags={group.groupTags}
                      description={group.groupDescription}
                      onJoinGroup={() => this.props.history.push(`/group/${group.groupId}`)}
                    />
                  </Grid>
                ))
              }
            </Grid>
          </Grid>
        </Grid>
      </div>
    )
  }
}

export default Home;