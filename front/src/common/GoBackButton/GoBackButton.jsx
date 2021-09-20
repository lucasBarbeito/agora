import React from 'react';
import {Button} from '@material-ui/core';


export default function GoBackButton(props) {

    return (
        <Button id="button" variant="contained" color="primary" onClick={() => props.history.goBack()}>
            VOLVER ATRÁS 
        </Button>
    )
}



