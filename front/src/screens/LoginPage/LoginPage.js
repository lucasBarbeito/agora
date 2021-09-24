import React, { Component } from "react";
import { Box, Button } from "@material-ui/core";
import "./LoginPage.css";
import TextField from "@material-ui/core/TextField";
import FormControl from "@material-ui/core/FormControl";
import InputLabel from "@material-ui/core/InputLabel";
import OutlinedInput from "@material-ui/core/OutlinedInput";
import InputAdornment from "@material-ui/core/InputAdornment";
import IconButton from "@material-ui/core/IconButton";
import { Visibility, VisibilityOff } from "@material-ui/icons";
import { UserContext } from "../../user-context";

class LoginPage extends Component {
    constructor(props) {
        super(props);
        this.state = {
            showPassword: false,
            password: "",
            email: "",
            unsuccessfulLogin: false,
            invalidInformation: false,
            errorMsg: "",
        };
    }

    handleLogin = async () => {
        this.setState({
            errorMsg: "",
            invalidInformation: false,
            unsuccessfulLogin: false,
        });
        const verificationEmail = this.validateEmail();
        if (verificationEmail) {
            try {
                const response = await fetch("http://localhost:8080/auth", {
                    method: "POST",
                    body: JSON.stringify({
                        email: this.state.email,
                        password: this.state.password,
                    }),
                    headers: {
                        "Content-type": "application/json;",
                    },
                });

                if (response.status === 401) {
                    this.setState({
                        errorMsg: "Email o contraseña incorrectos",
                        invalidInformation: true,
                    });
                } else if (response.status === 403) {
                    this.setState({
                        errorMsg: "Confirme su email para iniciar sesión",
                        invalidInformation: true,
                    });
                } else if (response.status === 404) {
                    this.setState({
                        errorMsg:
                            "No existe un usuario con la información ingresada",
                        invalidInformation: true,
                    });
                } else if (!response.ok) {
                    this.setState({
                        errorMsg: "Ha ocurrido un error",
                        invalidInformation: true,
                    });
                } else {
                    const res = await response.json();
                    const { setToken } = this.context;
                    setToken(res.token);
                    this.props.history.push("/home");
                }
            } catch (e) {
                alert("Error, no es posible conectarse al back-end");
            }
        }
    };

    validateEmail = () => {
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.state.email)) {
            this.setState({
                unsuccessfulLogin: true,
            });
            return false;
        } else {
            return true;
        }
    };

    render() {
        return (
            <div className="Agorabackground">
                <div>
                    <Box className="login-page-form-box" boxShadow={2}>
                        <div className="login-page-header">
                            <h2 className="login-page-title">Iniciar Sesión</h2>
                            <p className="login-page-subtitle">
                                <div>
                                    Ingresa los datos de tu cuenta. Si no tienes
                                    una, puedes
                                </div>
                                <div>
                                    crearla{" "}
                                    <span
                                        className="login-page-link"
                                        onClick={() =>
                                            this.props.history.push("/register")
                                        }
                                    >
                                        aquí!
                                    </span>
                                </div>
                            </p>
                        </div>
                        <div className="login-page-form-description">
                            <TextField
                                id="login-page-text-field"
                                label="Correo electrónico"
                                variant="outlined"
                                onChange={(e) =>
                                    this.setState({ email: e.target.value })
                                }
                                style={{ width: "96%" }}
                            />
                        </div>
                        <div className="login-page-form-description-pass">
                            <FormControl
                                style={{ width: "96%" }}
                                variant="outlined"
                                id="login-page-password-field"
                            >
                                <InputLabel htmlFor="outlined-adornment-password">
                                    Contraseña
                                </InputLabel>
                                <OutlinedInput
                                    id="outlined-adornment-password-1"
                                    type={
                                        this.state.showPassword
                                            ? "text"
                                            : "password"
                                    }
                                    value={this.state.password}
                                    onChange={(e) =>
                                        this.setState({
                                            password: e.target.value,
                                        })
                                    }
                                    endAdornment={
                                        <InputAdornment position="end">
                                            <IconButton
                                                aria-label="toggle password visibility"
                                                onClick={() =>
                                                    this.setState({
                                                        showPassword:
                                                            !this.state
                                                                .showPassword,
                                                    })
                                                }
                                                edge="end"
                                            >
                                                {this.state.showPassword ? (
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
                        </div>
                        {this.state.unsuccessfulLogin ? (
                            <Box id="login-page-warning-box">
                                <div id="login-page-warning-message">
                                    El correo electrónico no es válido
                                </div>
                            </Box>
                        ) : null}
                        {this.state.invalidInformation ? (
                            <Box id="login-page-warning-box">
                                <div id="login-page-warning-message">
                                    {this.state.errorMsg}
                                </div>
                            </Box>
                        ) : null}
                        <Button
                            id="login-page-create-button"
                            onClick={this.handleLogin}
                        >
                            Ingresar
                        </Button>
                    </Box>
                </div>
                <img
                    className="login-page-agoraLogo"
                    alt="Agora"
                    src="agora-logo.png"
                />
            </div>
        );
    }
}

LoginPage.contextType = UserContext;

export default LoginPage;
