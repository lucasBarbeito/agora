import React, {Component} from 'react'
import './RegisterPage.css';
import {TextField} from '@material-ui/core'
import IconButton from '@material-ui/core/IconButton';
import OutlinedInput from '@material-ui/core/OutlinedInput';
import InputLabel from '@material-ui/core/InputLabel';
import InputAdornment from '@material-ui/core/InputAdornment';
import FormControl from '@material-ui/core/FormControl';
import Visibility from '@material-ui/icons/Visibility';
import VisibilityOff from '@material-ui/icons/VisibilityOff';
import baseUrl from '../../baseUrl'

export default class RegisterPage extends Component {

    constructor(props) {
        super(props)

        this.state = {
            nombre: "",
            apellido: "",
            email: "",
            confirmedEmail: "",
            password: "",
            confirmedPassword: "",
            errorMsg: "",
            validateName: null,
            validateLastName: null,
            validatePassword: null,
            validateEmail: null,
            samePassword: null,
            sameEmail: null,
            completeFields: null,
            showPassword: false,
            showErrorMsg: false,
        }
    }

    handleCreateAccountClick = () => {

        this.validatePassword();
        this.checkSamePassword();
        this.validateEmail();
        this.checkSameEmail();
        this.validateLastName();
        this.validateName();
        this.handleUncompleteFields();
    }


    stopError = () => {
        const {validateEmail, validateLastName, validatePassword, validateName, samePassword, sameEmail, completeFields} = this.state

        if (validateEmail && validateLastName && validatePassword && validateName && samePassword && sameEmail && completeFields) {
            this.setState({
                showErrorMsg: false
            }, () => this.createUser())
        }
    }

    createUser = async () => {

        const request_url = `${baseUrl}/user`

        try {
            const response = await fetch(request_url, {
                    method: 'POST',
                    body: JSON.stringify({
                        email: this.state.email,
                        name: this.state.nombre,
                        password: this.state.password,
                        surname: this.state.apellido
                    }),
                    headers: {
                        'Content-type': 'application/json; charset=UTF-8',
                    }
                }
            )
            if (response.status === 409) {
                this.setState({
                    showErrorMsg: true,
                    errorMsg: 'Correo electrónico ya existente.'
                });
            } else if (response.ok) {
                this.props.history.push("/check-email");
            } else {
                this.setState({
                    showErrorMsg: true,
                    errorMsg: 'Ha ocurrido un error. Por favor, intente nuevamente.'
                });
            }
        } catch {
            this.setState({showErrorMsg: true, errorMsg: 'Ha ocurrido un error. Por favor, intente nuevamente.'});
        }
    }


    handleUncompleteFields = (e) => {

        if (!this.state.nombre || !this.state.apellido || !this.state.email || !this.state.confirmedEmail || !this.state.password || !this.state.confirmedPassword) {
            this.setState({
                completeFields: false,
                showErrorMsg: true,
                errorMsg: "Todos los campos deben ser completados"
            });
        } else {
            this.setState({
                completeFields: true,
            })
        }
    }
    checkSamePassword = (e) => {
        if (this.state.password !== this.state.confirmedPassword || (this.state.password === "" || this.state.confirmedPassword === "")) {
            this.setState({
                samePassword: false,
                showErrorMsg: true,
                errorMsg: "Las contraseñas no coinciden"
            });
        } else {
            this.setState({
                samePassword: true,
            })
        }
    }

    checkSameEmail = (e) => {
        if (this.state.email !== this.state.confirmedEmail || (this.state.email === "" || this.state.confirmedEmail === "")) {
            this.setState({
                sameEmail: false,
                showErrorMsg: true,
                errorMsg: "Los Correos Electrónicos no coinciden"
            });
        } else {
            this.setState({
                sameEmail: true,
            })
        }
    }

    validateEmail = (e) => {
        if (!(/^[^\s@]+@[^\s@]+\.[^\s@]+$/).test(this.state.email)) {
            this.setState({
                validateEmail: false,
                showErrorMsg: true,
                errorMsg: "El correo electrónico no es valido"
            });
        } else {
            this.setState({
                validateEmail: true,
            })
        }
    }
    validatePassword = (e) => {
        const CapitalRegex = /[A-Z]/;
        const NumberRegex = /[0-9]/;
        if (this.state.password.length <= 8) {
            this.setState({
                validatePassword: false,
                showErrorMsg: true,
                errorMsg: "La contraseña debe tener más de 8 caracteres"
            });
        } else if (!CapitalRegex.test(this.state.password) || !NumberRegex.test(this.state.password)) {
            this.setState({
                validatePassword: false,
                showErrorMsg: true,
                errorMsg: "La contraseña debe tener al menos una mayúscula y un número"
            }, () => console.log("Mayusc"));
        } else {
            this.setState({
                validatePassword: true,
            }, () => this.stopError())
        }
    }

    validateName = (e) => {
        if ((/[0-9]/.test(this.state.nombre) || this.state.nombre === "")) {
            this.setState({
                validateName: false,
                showErrorMsg: true,
                errorMsg: `El nombre no debe contener números`
            });
        } else {
            this.setState({
                validateName: true,
            })
        }
    }

    validateLastName = (e) => {
        if ((/[0-9]/.test(this.state.apellido)) || this.state.apellido === "") {
            this.setState({
                validateLastName: false,
                showErrorMsg: true,
                errorMsg: `El apellido no debe contener números`
            });
        } else {
            this.setState({
                validateLastName: true,
            })
        }
    }


    handleClickShowPassword = (e) => {
        this.setState({
            showPassword: !this.state.showPassword
        })

    }


    render() {
        return (
            <div className="Agorabackground">
        
            <div className = "Register-Container">
              <div className = "Header">
                  <h2 className = "Title">Crear mi cuenta de AGORA</h2>
                  <p className = "Description">
                      <div>Ingresa los datos para crear una cuenta nueva. Si ya tienes una,</div>
                      <div>puedes ingresar a AGORA {' '}
                      <span className = "AgoraLogIn" onClick={() => this.props.history.push("/login")}>aquí!</span>
                      </div>
                  </p>
               </div>
               <div className = "Form">
                   <div className =  "Form-First-Row" >
                       <TextField 
                        id="NameForm" 
                        label="Nombre" 
                        variant="outlined"
                        onChange={(e) => this.setState({nombre: e.target.value})}
                        style = {{width: "46%" }} 
                        />
                       <TextField
                        id="LastNameForm" 
                        label="Apellido" 
                        variant="outlined"
                        onChange={(e) => this.setState({apellido: e.target.value})}
                        style = {{width: "46.8%", left: "11px" }} 
                         />
                   </div>
                   <div className = "Email-Row">
                       <TextField
                        id = "emailField"
                        label = "Correo electrónico"
                        variant = "outlined"
                        onChange={(e) => this.setState({email: e.target.value})}
                        style = {{width: "95%" }} 
                        />
                        <TextField
                        id = "confirmedEmailField"
                        label = "Confirmar correo electrónico"
                        variant = "outlined"
                        onChange={(e) => this.setState({confirmedEmail: e.target.value})}
                        style = {{width: "95%", top: '21px' }} 
                        />
                   </div>
                   <div className =  "Password-Row">
                       <FormControl   variant="outlined"  style = {{width: "46%" }}  >
                           <InputLabel htmlFor="outlined-adornment-password">Contraseña</InputLabel>
                           <OutlinedInput
                            id="outlined-adornment-password-1"
                            type={this.state.showPassword ? 'text' : 'password'}
                           value= {this.state.password}
                           onChange={(e) => this.setState({password: e.target.value})}
                           endAdornment={
                               <InputAdornment position="end">
                                   <IconButton
                                     aria-label="toggle password visibility"
                                     onClick={() => this.handleClickShowPassword()}
                                     // onMouseDown={() => this.handleMouseDownPassword()}
                                     edge="end"
                                       >
                                      {this.state.showPassword ? <Visibility /> : <VisibilityOff />}
                                   </IconButton>
                                </InputAdornment>
                               }
                               labelWidth={80}
                               />
                       </FormControl>
   
                       <FormControl   variant="outlined" style = {{width: "46.8%", left: "11px" }} >
                           <InputLabel htmlFor="outlined-adornment-password">Confirmar Contraseña</InputLabel>
                           <OutlinedInput
                            id="outlined-adornment-password-2"
                            type={this.state.showPassword ? 'text' : 'password'}
                            value= {this.state.confirmedPassword}
                            onChange={(e) => this.setState({confirmedPassword: e.target.value})}
                            endAdornment={
                               <InputAdornment position="end">
                                   <IconButton
                                     aria-label="toggle password visibility"
                                     onClick={() => this.handleClickShowPassword()}
                                     edge="end"
                                       >
                                      {this.state.showPassword ? <Visibility /> : <VisibilityOff />}
                                   </IconButton>
                                </InputAdornment>
                               }
                               labelWidth={160}
                           />
                       </FormControl>
                    </div>
                    { this.state.showErrorMsg ? 
                        <div className = "Errormsg">
                        <p className = "ErrorMsg__MSG">{this.state.errorMsg}</p> 
                       </div> : null 
                    }
                   
                    <button className = "CreateAccountButton" onClick = {() => this.handleCreateAccountClick()}>Crear mi Cuenta</button>
               </div> 
            </div>
            <img src = "/agora-logo.png" alt = "AGORA" className = "AgoraLogo"></img>
           </div>
        )
    }
}



