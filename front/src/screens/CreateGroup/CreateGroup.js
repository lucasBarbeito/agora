import React, {Component} from 'react';
import {Box, Button} from "@material-ui/core";
import './CreateGroup.css';
import FormControl from "@material-ui/core/FormControl";
import InputLabel from "@material-ui/core/InputLabel";
import OutlinedInput from "@material-ui/core/OutlinedInput";
import Autocomplete from '@material-ui/lab/Autocomplete';
import TextField from "@material-ui/core/TextField";
import HomeMenu from '../../common/HomeMenu/HomeMenu';
import {UserContext} from "../../user-context";

class CreateGroup extends Component {

    constructor(props) {
        super(props);
        this.state = {
            groupName: '',
            label: [],
            description: '',
            createdUnsuccessfully: false,
            errorMsg: ''
        }
    }

    render() {

        const labels = ['Etiqueta1', 'Etiqueta2', 'Etiqueta3']

        const createGroup = async () => {

            if (!this.state.groupName) {
                this.setState({
                    createdUnsuccessfully: true,
                    errorMsg: 'Por favor ingrese un nombre de grupo'
                })
            } else if (this.state.label.length === 0) {
                this.setState({
                    createdUnsuccessfully: true,
                    errorMsg: 'Por favor seleccione al menos una etiqueta'
                })
            } else if (!this.state.description) {
                this.setState({
                    createdUnsuccessfully: true,
                    errorMsg: 'Por favor ingrese una descripción'
                })
            } else {
                this.setState({createdUnsuccessfully: false})
                let date = new Date();
                const {token} = this.context;
                try {
                    const response = await fetch('http://localhost:8080/studyGroup', {
                        method: 'POST',
                        body: JSON.stringify({
                            creationDate: date.toISOString(), //"2011-12-19T15:28:46.493Z"
                            creatorId: 0, //
                            description: this.state.description,
                            name: this.state.groupName,
                            //labels
                        }),
                        headers: {
                            'Content-type': 'application/json; charset=UTF-8',
                            'Authorization': `Bearer ${token}`
                        },
                    });
                    if (response.status === 409) {
                        this.setState({
                            errorMsg: 'Grupo creado con nombre ya existente',
                            createdUnsuccessfully: true
                        })
                    } else if (!response.ok && response.status !== 404) {
                        throw new Error('Server Error');
                    } else {
                        this.props.history.push({
                            pathname: `/group/${0}`,
                            state: {
                                name: this.state.groupName,
                                description: this.state.description,
                                creationDate: ('0' + date.getDate()).slice(-2) + '/' +
                                    ('0' + (date.getMonth() + 1)).slice(-2) + '/' +
                                    date.getFullYear(),
                            }
                        })
                    }
                } catch (e) {
                    alert('Error, no es posible conectarse al back-end');
                }
            }
        }
        return (
            <div id='container'>
                <Box m={10} display="flex" flexDirection="row" p={1}>
                    <HomeMenu history={this.props.history} />
                    <Box className='form-box' boxShadow={2}>
                        <h6 className='title'>
                            Crear nuevo grupo de AGORA
                        </h6>
                        <div className='subtitle'>
                            Crear un nuevo grupo dentro de AGORA para poder aprender con nuevos amigos!
                        </div>
                        <FormControl fullWidth variant="outlined" id='form-control'>
                            <InputLabel htmlFor="outlined-adornment-amount">Nombre</InputLabel>
                            <OutlinedInput
                                id="outlined-adornment-amount"
                                value={this.state.groupName}
                                label='Nombre'
                                labelWidth={60}
                                onChange={(value) => this.setState({groupName: value.target.value})}
                            />
                        </FormControl>
                        <div className='selection-form'>
                            <Autocomplete
                                multiple
                                id="tags-outlined"
                                options={labels}
                                filterSelectedOptions
                                onChange={(event, newValue) => {
                                    this.setState({label: newValue});
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
                        <div className='form-description'>
                            <TextField
                                id="outlined-multiline-static"
                                label="Descripción"
                                variant="outlined"
                                multiline
                                rows={6}
                                onChange={(text) => this.setState({description: text.target.value})}
                            />
                        </div>
                        {this.state.createdUnsuccessfully ?
                            <Box id='warning-box'>
                                <div id='warning-message'>{this.state.errorMsg}</div>
                            </Box>
                            : null}
                        <Button id='button-color' onClick={createGroup}>CREAR GRUPO</Button>
                    </Box>
                </Box>
            </div>
        );
    }
}
CreateGroup.contextType = UserContext;

export default CreateGroup;
