import {
  Box,
  Button,
  FormControl,
  Grid,
  IconButton,
  InputAdornment,
  InputLabel,
  OutlinedInput,
  TextField,
  Select,
  MenuItem,
  Snackbar,
} from "@material-ui/core";
import { withRouter } from "react-router-dom";
import React, { useState } from "react";
import HomeStructure from "../../common/HomeStructure/HomeStructure";
import Visibility from "@material-ui/icons/Visibility";
import VisibilityOff from "@material-ui/icons/VisibilityOff";
import CancelIcon from "@material-ui/icons/Cancel";
import AddIcon from "@material-ui/icons/Add";
import "./EditProfilePage.css";
import ClearIcon from "@material-ui/icons/Clear";
import baseUrl from "../../baseUrl";

function EditProfilePage(props) {
  const [name, setName] = useState(props.context.userInfo.name);
  const [lastName, setLastName] = useState(props.context.userInfo.surname);
  const [email, setEmail] = useState(props.context.userInfo.email);

  const [contactLinks, setContactLinks] = useState(
    props.context.userInfo.contactLinks
  );
  const [password, setPassword] = useState("");
  const [confirmedPassword, setConfirmedPassword] = useState("");
  const [errorMsg, setErrorMsg] = useState("");
  const [contactErrorMsg, setContactErrorMsg] = useState("");
  const [showErrorMsg, setShowErrorMsg] = useState(false);
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [snackBarMsg, setSnackBarMsg] = useState("");
  const [showContactErrorMsg, setShowContactErrorMsg] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const handleClickShowPassword = (e) => {
    setShowPassword(!showPassword);
  };

  const handleConfirmEditClick = (e) => {
    stopError();
  };

  const handleUncompleteFields = (e) => {
    if (!name || !lastName) {
      setShowErrorMsg(true);
      setErrorMsg("Todos los campos deben ser completados");
      return false;
    } else {
      return true;
    }
  };

  const editUserRequest = async () => {
    try {
      const response = await fetch(`${baseUrl}/user/me`, {
        method: "PUT",
        body: JSON.stringify({
          name: name,
          password: password,
          surname: lastName,
        }),
        headers: {
          "Content-type": "application/json; charset=UTF-8",
          Authorization: `Bearer ${props.token}`,
        },
      });
      if (!response.ok) {
        setShowErrorMsg(true);
        setErrorMsg("Ha ocurrido un error. Por favor, intente nuevamente.");
      }
    } catch (e) {
      alert("Error, no es posible conectarse al back-end");
    }
  };

  const saveContactChanges = async () => {
    try {
      const response = await fetch(`${baseUrl}/user/me`, {
        method: "POST",
        body: JSON.stringify({
          contactLinks: contactLinks,
        }),
        headers: {
          "Content-type": "application/json; charset=UTF-8",
          Authorization: `Bearer ${props.context.token}`,
        },
      });

      if (!response.ok) {
        setShowContactErrorMsg(true);
        setContactErrorMsg(
          "Ha ocurrido un error. Por favor, intente nuevamente."
        );
      } else {
        setSnackBarMsg("¡Medios de contacto actualizados con éxito!");
        setOpenSnackbar(true);
      }
    } catch (e) {
      alert("Error, no es posible conectarse al back-end");
      console.log(e);
    }
  };

  const stopError = () => {
    if (
      handleUncompleteFields() &&
      validatePasswordFunction() &&
      validateNameFunction() &&
      validateLastNameFunction() &&
      checkSamePassword()
    ) {
      setShowErrorMsg(false);
      alert("Se hicieron cambios");
      editUserRequest();
    }
  };

  const checkSamePassword = (e) => {
    if (password !== confirmedPassword) {
      setShowErrorMsg(true);
      setErrorMsg("Las contraseñas no coinciden");
      return false;
    } else {
      return true;
    }
  };

  const addNewContact = () => {
    setContactLinks([...contactLinks, { id: "", linkType: "", link: "" }]);
  };

  const handleContactTypeChange = (index, value) => {
    const contact = contactLinks[index];
    const newContactLink = { ...contact, linkType: value };
    const newContactLinks = [...contactLinks];
    newContactLinks[index] = newContactLink;
    setContactLinks(newContactLinks);
  };

  const handleContactLinkChange = (index, value) => {
    const contact = contactLinks[index];
    const newContactLink = { ...contact, link: value };
    const newContactLinks = [...contactLinks];
    newContactLinks[index] = newContactLink;
    setContactLinks(newContactLinks);
  };

  const handleDeleteContact = (index) => {
    setContactLinks(contactLinks.filter((value, idx) => idx !== index));
  };

  const handleSaveMemberContacts = () => {
    let success = true;
    contactLinks.forEach(({ linkType, link }) => {
      if (!linkType || !link) success = false;
    });
    if (success) {
      setShowContactErrorMsg(false);
      saveContactChanges();
    } else {
      setShowContactErrorMsg(true);
      setContactErrorMsg("No es posible crear un link de contacto vacío.");
    }
  };

  const validatePasswordFunction = (e) => {
    const CapitalRegex = /[A-Z]/;
    const NonCapitalRegex = /[a-z]/;
    const NumberRegex = /[0-9]/;
    if (password.length === 0) return true;
    if (password.length < 8) {
      setShowErrorMsg(true);
      setErrorMsg("La contraseña debe tener 8 caracteres o más");
      return false;
    } else if (
      !CapitalRegex.test(password) ||
      !NonCapitalRegex.test(password) ||
      !NumberRegex.test(password)
    ) {
      setShowErrorMsg(true);
      setErrorMsg(
        "La contraseña debe tener al menos una mayúscula, una minúscula y un número"
      );
      return false;
    } else {
      return true;
    }
  };

  const validateNameFunction = (e) => {
    if (/[0-9]/.test(name) || name === "") {
      setShowErrorMsg(true);
      setErrorMsg(`El nombre no debe contener números`);
      return false;
    } else {
      return true;
    }
  };

  const validateLastNameFunction = (e) => {
    if (/[0-9]/.test(lastName) || lastName === "") {
      setShowErrorMsg(true);
      setErrorMsg(`El apellido no debe contener números`);
      return false;
    } else {
      return true;
    }
  };

  const logOut = async () => {
    try {
      const response = await fetch(`${baseUrl}/auth`, {
        method: "DELETE",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
          Authorization: `Bearer ${props.context.token}`,
        },
      });

      if (response.ok) {
        props.context.setToken(null, "/");
      }
    } catch (e) {
      alert("Error, no es posible conectarse al back-end");
    }
  };

  const deleteAccount = async () => {
    logOut();
    try {
      const response = await fetch(`${baseUrl}/user/me`, {
        method: "DELETE",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
          Authorization: `Bearer ${props.context.token}`,
        },
      });
    } catch (e) {
      alert("Error, no es posible conectarse al back-end");
    }
  };

  return (
    <HomeStructure>
      <Grid container>
        <Box className="edit-profile-form-box" boxShadow={2}>
          <Grid container xs={12} id="creategroup-box-grid" spacing={1}>
            <Grid item>
              <h1 className="creategroup-title">
                Actualizar mis datos de cuenta
              </h1>
            </Grid>
            <Grid item>
              <div className="creategroup-subtitle">
                Actualiza tus datos en caso de que no esten al día. Modifica tu
                nombre y contraseña!
              </div>
            </Grid>
            <Grid item xs={6}>
              <TextField
                id="edit-profile-NameForm"
                style={{ marginTop: 10 }}
                label="Nombre"
                value={name}
                variant="outlined"
                onChange={(e) => setName(e.target.value)}
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                id="edit-profile-LastNameForm"
                style={{ marginTop: 10 }}
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
                disabled
                style={{ width: "100%", marginTop: 10 }}
              />
            </Grid>
            <Grid item xs={6}>
              <FormControl variant="outlined">
                <InputLabel
                  style={{ marginTop: 10 }}
                  htmlFor="outlined-adornment-password"
                >
                  Contraseña
                </InputLabel>
                <OutlinedInput
                  id="outlined-adornment-password-1"
                  style={{ marginTop: 10 }}
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
                        {showPassword ? <Visibility /> : <VisibilityOff />}
                      </IconButton>
                    </InputAdornment>
                  }
                  labelWidth={80}
                />
              </FormControl>
            </Grid>
            <Grid item xs={6}>
              <FormControl variant="outlined">
                <InputLabel
                  style={{ marginTop: 10 }}
                  htmlFor="outlined-adornment-password"
                >
                  Confirmar Contraseña
                </InputLabel>
                <OutlinedInput
                  id="outlined-adornment-password-2"
                  style={{ marginTop: 10 }}
                  type={showPassword ? "text" : "password"}
                  value={confirmedPassword}
                  onChange={(e) => setConfirmedPassword(e.target.value)}
                  endAdornment={
                    <InputAdornment position="end">
                      <IconButton
                        iconSize={1}
                        aria-label="toggle password visibility"
                        onClick={() => handleClickShowPassword()}
                        edge="end"
                      >
                        {showPassword ? <Visibility /> : <VisibilityOff />}
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
            ) : (
              <div className="edit-profile-page-ghost-warning-box" />
            )}

            <div className="edit-profile-page-button-position">
              <button
                className="edit-profile-page-button"
                onClick={() => handleConfirmEditClick()}
              >
                Guardar Cambios
              </button>
            </div>
          </Grid>
        </Box>
        <Grid item xs={4}>
          <Button
            fullWidth
            id="userpage-back-button"
            variant="contained"
            color="primary"
            onClick={() => deleteAccount()}
          >
            <ClearIcon id="userpage-back-icon" />
            ELIMINAR CUENTA
          </Button>
        </Grid>
        <br />
        <br />
        <Box className="editprofile-members-contact-box" boxShadow={2}>
          <Grid container xs={12} id="creategroup-box-grid" spacing={1}>
            <Grid item>
              <h1 className="creategroup-title">Mis medios de Contacto</h1>
            </Grid>
            <Grid item>
              <div className="creategroup-subtitle">
                Incluye medios de contactos para que los miembros de los grupos
                puedan comunicarse!
              </div>
            </Grid>

            <Grid container item direction="column" spacing={2}>
              <Grid id="editprofile-email-contact" container item spacing={4}>
                <Grid item xs={4}>
                  <FormControl fullWidth variant="outlined">
                    <InputLabel id="member-contact-select-label-email">
                      Tipo
                    </InputLabel>
                    <Select
                      id="member-contact-select-email"
                      labelId="member-contact-select-label-email"
                      value={"EMAIL"}
                      label="Tipo"
                      disabled
                    >
                      <MenuItem value={"EMAIL"}>Email</MenuItem>
                    </Select>
                  </FormControl>
                </Grid>

                <Grid item xs={5}>
                  <TextField
                    id="edit-profile-NameForm-email"
                    label="Link"
                    value={props.context.userInfo.email}
                    variant="outlined"
                    disabled
                  />
                </Grid>
              </Grid>
              {contactLinks.map((contact, index) => (
                <Grid container item spacing={4}>
                  <Grid item xs={4}>
                    <FormControl fullWidth variant="outlined">
                      <InputLabel id={`member-contact-select-label${index}`}>
                        Tipo
                      </InputLabel>
                      <Select
                        id={`member-contact-select${index}`}
                        labelId={`member-contact-select-label${index}`}
                        value={contact.linkType}
                        label="Tipo"
                        onChange={(e) =>
                          handleContactTypeChange(index, e.target.value)
                        }
                      >
                        <MenuItem value={"EMAIL"}>Email</MenuItem>
                        <MenuItem value={"PHONE"}>Celular</MenuItem>
                        <MenuItem value={"INSTAGRAM"}>Instagram</MenuItem>
                        <MenuItem value={"FACEBOOK"}>Facebook</MenuItem>
                        <MenuItem value={"TWITTER"}>Twitter</MenuItem>
                      </Select>
                    </FormControl>
                  </Grid>

                  <Grid item xs={5}>
                    <TextField
                      id={`edit-profile-NameForm${index}`}
                      label="Link"
                      value={contact.link}
                      variant="outlined"
                      onChange={(e) =>
                        handleContactLinkChange(index, e.target.value)
                      }
                    />
                  </Grid>
                  <Grid item xs={3}>
                    <IconButton
                      id="delete-button"
                      onClick={() => handleDeleteContact(index)}
                    >
                      <CancelIcon id="delete-icon" />
                    </IconButton>
                  </Grid>
                </Grid>
              ))}
            </Grid>

            <Grid item container direction="column" alignContent="center">
              <Grid item>
                <IconButton
                  id="member-contact-add-button"
                  onClick={() => addNewContact()}
                >
                  <AddIcon id="delete-icon" />
                </IconButton>
              </Grid>
            </Grid>

            {showContactErrorMsg && (
              <div className="edit-profile-page-warning-box">
                <p className="edit-profile-page-warning-msg">
                  {contactErrorMsg}
                </p>
              </div>
            )}
            <Grid item container justifyContent="flex-end">
              <Grid item>
                <Button
                  id="editprofile-save-members-contact-button"
                  onClick={() => {
                    handleSaveMemberContacts();
                  }}
                >
                  Guardar Cambios
                </Button>
              </Grid>
            </Grid>
          </Grid>
        </Box>
      </Grid>
      <Snackbar
        open={openSnackbar}
        autoHideDuration={4000}
        onClose={() => setOpenSnackbar(false)}
        message={snackBarMsg}
      />
    </HomeStructure>
  );
}

export default withRouter(EditProfilePage);
