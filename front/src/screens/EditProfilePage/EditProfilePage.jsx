import { Box, Button, FormControl, Grid, IconButton, InputAdornment, InputLabel, OutlinedInput, TextField } from '@material-ui/core';
import { withRouter } from "react-router-dom";
import React, { Component, useEffect, useState } from 'react'
import HomeStructure from '../../common/HomeStructure/HomeStructure'
import Visibility from '@material-ui/icons/Visibility';
import VisibilityOff from '@material-ui/icons/VisibilityOff';
import "./EditProfilePage.css";



function EditProfilePage(props) {

    const [name, setName] = useState(props.context.userInfo.name);
    const [lastName, setLastName] = useState(props.context.userInfo.surname);
    const [email, setEmail] = useState(props.context.userInfo.email);
    const [confirmedEmail, setConfirmedEmail] = useState(props.context.userInfo.email);
    const [password, setPassword] = useState("");
    const [confirmedPassword, setConfirmedPassword] = useState("");
    const [errorMsg, setErrorMsg] = useState("");
    const [showErrorMsg, setShowErrorMsg] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    
    

    const handleClickShowPassword = (e) => {
        setShowPassword(!showPassword);
    }

    const handleConfirmEditClick = (e) =>{
        stopError()
    }

    const handleUncompleteFields = (e) => {
        if (
          !name || !lastName || !email || !confirmedEmail ) {
            setShowErrorMsg(true);
            setErrorMsg("Todos los campos deben ser completados");
            return false
        } else {
            return true
        }
      }
    
    const stopError = () =>{
        if (
            handleUncompleteFields() &&
            checkSameEmail() &&
            validateEmailFunction() &&
            validatePasswordFunction() &&
            validateNameFunction() &&
            validateLastNameFunction() &&
            checkSamePassword()
          ){
              setShowErrorMsg(false)
              alert("Se hicieron cambios")
              console.log(name,lastName,email,password)
          }

    }
    
    const checkSamePassword = (e) => {
        if(password !== confirmedPassword){
            setShowErrorMsg(true);
            setErrorMsg("Las contraseñas no coinciden");
            return false

        }else{
            return true
        }
    }

    const checkSameEmail = (e) =>{
        if(email !== confirmedEmail || email === "" || confirmedEmail === "" ){
            setShowErrorMsg(true)
            setErrorMsg("Los Correos Electrónicos no coinciden")
            return false

        }else{
            return true
        }
    }

    const validateEmailFunction = (e) => {
        if(!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)){
            setShowErrorMsg(true)
            setErrorMsg("El correo electrónico no es valido")
            return false

        }else{
            return true
        }
    }

    const validatePasswordFunction = (e) =>{
        const CapitalRegex = /[A-Z]/;
        const NumberRegex = /[0-9]/;
        if(password.length === 0) return true
        if(password.length <= 8){
            setShowErrorMsg(true);
            setErrorMsg("La contraseña debe tener más de 8 caracteres")
            return false

        }else if (!CapitalRegex.test(password) || !NumberRegex.test(password)){
            setShowErrorMsg(true)
            setErrorMsg("La contraseña debe tener al menos una mayúscula y un número")
            return false

        }else{
            return true
        }
    }

    const validateNameFunction = (e) => {
        if(/[0-9]/.test(name) || name === ""){
            setShowErrorMsg(true)
            setErrorMsg(`El nombre no debe contener números`)
            return false

        }else{
            return true
        }
    }

    const validateLastNameFunction = (e) => {
        if(/[0-9]/.test(lastName) || lastName === ""){
            setShowErrorMsg(true)
            setErrorMsg(`El apellido no debe contener números`)
             return false

        }else{
            return true
        }
    }




    return (
        <HomeStructure>
            <Box className="creategroup-form-box" boxShadow={2}>
              <Grid
                    container
                    xs = {12}
                    id="creategroup-box-grid"
                    spacing={1}
              >
                <Grid item>
                    <h1 className="creategroup-title">Actualizar mis datos de cuenta</h1>
                </Grid>
                <Grid item>
                    <div className="creategroup-subtitle">
                        Actualiza tus datos en caso de que no esten al día. Modifica tu nombre, correo electrónico y contraseña!
                    </div>
                </Grid>
                <Grid item xs={6}>
                    <TextField
                        id="edit-profile-NameForm"
                        style={{"marginTop":10}}
                        label="Nombre"
                        value={name}
                        variant="outlined"
                        onChange={(e) => setName(e.target.value)}
                    />
                </Grid>
                <Grid item xs={6}>
                    <TextField
                        id="edit-profile-LastNameForm"
                        style={{"marginTop":10}}
                        label="Apellido"
                        value={lastName}
                        variant="outlined"
                        onChange={(e) => setLastName(e.target.value)}
                    />
                </Grid>
                <Grid item xs={12}>
                    <TextField
                        id="edit-profile-emailField"
                        label="Correo electrónico"
                        value={email}
                        variant="outlined"
                        onChange={(e) => setEmail(e.target.value)}
                        style={{ width: "100%", "marginTop":10 }}
                    />
                </Grid>
                <Grid item xs={12}>
                    <TextField
                        id="edit-profile-confirmedEmailField"
                        label="Confirmar correo electrónico"
                        value={confirmedEmail}
                        variant="outlined"
                        onChange={(e) => setConfirmedEmail(e.target.value)}
                        style={{ width: "100%", "marginTop":10 }}
                    />
                </Grid>
                <Grid item xs={6}>
                    <FormControl variant="outlined">
                        <InputLabel style={{"marginTop":10}} htmlFor="outlined-adornment-password">
                            Contraseña
                        </InputLabel>
                    <OutlinedInput
                        id="outlined-adornment-password-1"
                        style={{"marginTop":10}}
                        type={showPassword ? "text" : "password"}
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        endAdornment={
                            <InputAdornment position="end">
                                <IconButton
                                    aria-label="toggle password visibility"
                                    onClick={() => handleClickShowPassword()}
                                    edge="end"
                                >
                        {showPassword ? (
                          <Visibility />
                        ) : (
                          <VisibilityOff />
                        )}
                      </IconButton>
                    </InputAdornment>
                  }
                  labelWidth={80}
                 />
                </FormControl>
                </Grid>
                <Grid item xs={6}>
                <FormControl
                variant="outlined"
              >
                <InputLabel style={{"marginTop":10}} htmlFor="outlined-adornment-password">
                  Confirmar Contraseña
                </InputLabel>
                <OutlinedInput
                  id="outlined-adornment-password-2"
                  style={{"marginTop":10}}
                  type={showPassword ? "text" : "password"}
                  value={confirmedPassword}
                  onChange={(e) => setConfirmedPassword(e.target.value)}
                  endAdornment={
                    <InputAdornment position="end">
                      <IconButton
                        iconSize ={1}
                        aria-label="toggle password visibility"
                        onClick={() => handleClickShowPassword()}
                        edge="end"
                      >
                        {showPassword ? (
                          <Visibility />
                        ) : (
                          <VisibilityOff />
                        )}
                      </IconButton>
                    </InputAdornment>
                  }
                  labelWidth={160}
                />
              </FormControl>
                </Grid>
                    {showErrorMsg ? (
                            <div className="edit-profile-page-warning-box">
                                <p className="edit-profile-page-warning-msg">{errorMsg}</p>
                            </div>
                    ) : <div className="edit-profile-page-ghost-warning-box"/> }

                    <div className = "edit-profile-page-button-position">
                        <button
                            className="edit-profile-page-button"
                            onClick={() => handleConfirmEditClick()}>
                            Guardar Cambios
                        </button>
                    </div>
              </Grid>
            </Box>
        </HomeStructure>
    )
}

export default withRouter(EditProfilePage);
