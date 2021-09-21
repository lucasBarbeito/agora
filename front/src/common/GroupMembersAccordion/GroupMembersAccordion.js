import React, {Component} from 'react';
import { 
  Box,
  Button,
  MenuList, 
  Typography, 
  Accordion, 
  AccordionSummary, 
  AccordionDetails 
} from "@material-ui/core";
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import MemberContact from '..//MemberContact/MemberContact';
import './GroupMembersAccordion.css';

class GroupMembersAccordion extends Component {

    constructor(props) {
      super(props);
    }

    kickMember = () => {
      
    }
  
    render() {
      return(
        <> {
          this.props.members.map((member, index) => (
            <Accordion id="members-accordion" key={index} >
              <AccordionSummary
                expandIcon={<ExpandMoreIcon/>}
                aria-controls="panel1a-content"
                id="panel1a-header"
              >
                <Typography id="member-name-typography">{ member.name }</Typography>
                { member.isAdmin && <Typography id="admin-typography"> admin </Typography> }
              </AccordionSummary>
              <AccordionDetails id="accordion-details">
                <Box>
                  <MenuList>
                    {
                      member.contacts.map((contact, index) => (
                        <MemberContact 
                          key={index} 
                          type={contact.type}
                          value={contact.value}  
                        />
                      ))
                    }
                  </MenuList>
                  {
                    this.props.isAdmin && 
                    <Box id="kick-member-button-container">
                      <Button 
                        id="kick-member-button"
                        onClick={() => this.kickMember()}
                      >
                        Echar del grupo
                      </Button>
                    </Box>
                  }
                </Box>
              </AccordionDetails>
            </Accordion>))
        } </>);
    }
  
  }
  
  export default GroupMembersAccordion;