import React, { Component } from 'react'
import './ErrorNotFound.css';


export default class ErrorNotFound extends Component {
    constructor(props) {
        super(props)
    
        this.state = {
             
        }
    }
    
    render() {
        return (
            <div >
                <div className ="Container">
                    <div className = "Error404">
                        <p className = "Error404_MSG_1"> 404 </p>
                        <p className = "Error404_MSG_2"> NOT FOUND</p>
                    </div>
                    <div className= "ErrorDescription">
                        <p>Lo lamentamos, pero no encontramos la página que buscas.</p>
                    </div>
                    <div>
                        <button className = "goBackButton">VOLVER ATRÁS</button>
                    </div>
                </div>
            </div>
        )
    }
}
