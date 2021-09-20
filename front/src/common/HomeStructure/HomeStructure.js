import { Component } from 'react';
import { Container, Grid, IconButton, TextField } from '@material-ui/core';
import './HomeStructure.css';
import HomeMenu from '../../common/HomeMenu/HomeMenu';

class HomeStructure extends Component {
    
    render() {
        return (
            <Container id='main-container'>
                <Grid container spacing={2}>
                    <Grid item xs={3}  container direction="column" alignItems="flex-end">
                        <HomeMenu history={this.props.history}/>
                    </Grid>
                    <Grid item xs={9} id='child-grid'>
                        {this.props.children}
                    </Grid>
                </Grid>
            </Container>
        )}
}

export default HomeStructure;