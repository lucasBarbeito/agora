import React, {Component} from 'react';
import { Button, Divider, Grid, Paper, Typography, IconButton, TextField, Container, Accordion, AccordionSummary, AccordionDetails  } from "@material-ui/core";
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import EmailIcon from '@material-ui/icons/Email';
import PhoneIcon from '@material-ui/icons/Phone';
import InstagramIcon from '@material-ui/icons/Instagram';
import './GroupMembersAccordion.css';

class GroupMembersAccordion extends Component {

    constructor(props) {
      super(props);
    }
  
    render() {
        return (<>{this.props.data.map((info, index) => (
                        <Accordion id="members-accordion" key={index} >
                        <AccordionSummary
                          expandIcon={<ExpandMoreIcon />}
                          aria-controls="panel1a-content"
                          id="panel1a-header"
                        >
                        <Typography id="member-name-typography">{info.name}</Typography>
                        {index===0&&<Typography id="admin-typography">admin</Typography>}
                        </AccordionSummary>
                        <AccordionDetails>
                            <Grid container >
                                <Grid item container xs={12}>
                                    <Grid item xs={1}>
                                    <EmailIcon id="contact-icon"/>
                                    </Grid>
                                    <Grid item xs={10}>
                                    <Typography id="member-contact-info">{info.email}</Typography>
                                    </Grid>
                                </Grid>
                                
                                <Grid item container xs={12}>
                                    <Grid item xs={1}>
                                        <PhoneIcon id="contact-icon"/>
                                    </Grid>
                                    <Grid item xs={10}>
                                     <Typography id="member-contact-info">{info.phone}</Typography>
                                    </Grid>
                                </Grid>
                                <Grid item container xs={12}>
                                    <Grid item xs={1}>
                                        <InstagramIcon id="contact-icon"/>
                                    </Grid>
                                    <Grid item xs={10}>
                                     <Typography id="member-contact-info">{info.instagram}</Typography>
                                    </Grid>
                                </Grid>
                                <Grid item container xs={12} justifyContent="flex-end">
                                 {this.props.admin&&index!==0&&<Button id="remove-groupmember-button" onClick={() => {/*TODO*/}} >ECHAR DEL GRUPO</Button>}
                                </Grid>
                         </Grid>
                        </AccordionDetails>
                      </Accordion>))
    }</>);
    }
  
  }
  
  export default GroupMembersAccordion;