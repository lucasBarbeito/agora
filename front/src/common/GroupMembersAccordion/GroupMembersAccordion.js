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
          this.props.memberContacts.map((member, index) => (
             <Accordion id="members-accordion" key={index} >
              <AccordionSummary
                expandIcon={<ExpandMoreIcon/>}
                aria-controls="panel1a-content"
                id="panel1a-header"
              >
                <Typography id="member-name-typography">{ member.name }</Typography>
                { this.props.creatorId == member.id && <Typography id="admin-typography"> admin </Typography> }
              </AccordionSummary>
              <AccordionDetails id="accordion-details">
                <Box>
                  <MenuList>
                    {
                      Object.keys(member).slice(2).map((contactType, index) => (
                        <MemberContact 
                          key={index} 
                          type={contactType}
                          value={member[contactType]}  
                        />
                      ))
                    }
                  </MenuList>
                  {
                    !(this.props.creatorId == member.id) && 
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