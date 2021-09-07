import React, {Component} from 'react';
import {Box, Button} from "@material-ui/core";
import './LoginPage.css'
import TextField from "@material-ui/core/TextField";
import FormControl from "@material-ui/core/FormControl";
import InputLabel from "@material-ui/core/InputLabel";
import OutlinedInput from "@material-ui/core/OutlinedInput";
import InputAdornment from "@material-ui/core/InputAdornment";
import IconButton from "@material-ui/core/IconButton";
import {Visibility, VisibilityOff} from "@material-ui/icons";

class LoginPage extends Component {

    constructor(props) {
        super(props);
        this.state = {
            showPassword: false,
            password: '',
            email: '',
            unsuccessfulLogin: false,
            validateEmail: null,
            validatePassword: null
        }
    }

    handleLogin = () => {
        this.validateEmail();
    }

    validateEmail = () => {
        if (!(/^[^\s@]+@[^\s@]+\.[^\s@]+$/).test(this.state.email)) {
            this.setState({
                validateEmail: false,
                unsuccessfulLogin: true
            });
        } else {
            this.setState({
                validateEmail: true,
            }, () => this.props.history.push("/createGroup"))
            alert('entro')
        }
    }

    render() {

        return (
            <div className='container'>
                <div>
                    <Box className='form-box' boxShadow={2}>
                        <h6 className='title'>Iniciar Sesión</h6>
                        <div className='subtitle'>Ingresa los datos de tu cuenta. Si no tienes una, puedes
                            crearla {' '}
                            <span className="link" onClick={() => this.props.history.push("/register")}>aquí!</span>
                        </div>
                        <div className="form-description">
                            <TextField
                                id='text-field'
                                label="Correo electrónico"
                                variant="outlined"
                                onChange={(e) => this.setState({email: e.target.value})}
                            />
                        </div>
                        <div className="form-description">
                            <FormControl variant="outlined" id='password-field'>
                                <InputLabel htmlFor="outlined-adornment-password">Contraseña</InputLabel>
                                <OutlinedInput
                                    id="outlined-adornment-password-1"
                                    type={this.state.showPassword ? 'text' : 'password'}
                                    value={this.state.password}
                                    onChange={(e) => this.setState({password: e.target.value})}
                                    endAdornment={
                                        <InputAdornment position="end">
                                            <IconButton
                                                aria-label="toggle password visibility"
                                                onClick={() => this.setState({showPassword: !this.state.showPassword})}
                                                edge="end"
                                            >
                                                {this.state.showPassword ? <Visibility/> : <VisibilityOff/>}
                                            </IconButton>
                                        </InputAdornment>
                                    }
                                    labelWidth={80}
                                />
                            </FormControl>
                        </div>
                        {this.state.unsuccessfulLogin ?
                            <Box id='warning-box'>
                                <div id='warning-message'>El correo electrónico no es válido</div>
                            </Box>
                            : null}
                        <Button id="create-button" onClick={this.handleLogin}>Ingresar</Button>
                    </Box>
                </div>
                <img src="agora-logo.png" className="agoraLogo" alt='Agora'/>
            </div>
        );
    }
}

export default LoginPage;
