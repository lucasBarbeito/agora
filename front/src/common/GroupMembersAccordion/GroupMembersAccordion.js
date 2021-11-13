import React, { Component } from "react";
import {
  Box,
  Button,
  MenuList,
  Typography,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  MenuItem,
} from "@material-ui/core";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import MemberContact from "..//MemberContact/MemberContact";
import "./GroupMembersAccordion.css";
import EmailIcon from "@material-ui/icons/Email";

class GroupMembersAccordion extends Component {
  constructor(props) {
    super(props);
  }

  handleClick = (value) => {
    window.open("mailto:" + value, "_blank");
  };

  render() {
    return (
      <>
        {" "}
        {this.props.memberContacts.map((member, index) => (
          <Accordion id="members-accordion" key={index}>
            <AccordionSummary
              expandIcon={<ExpandMoreIcon />}
              aria-controls="panel1a-content"
              id="panel1a-header"
            >
              <Typography id="member-name-typography">
                {member.name} {member.surname}
              </Typography>
              {this.props.creatorId == member.id && (
                <Typography id="admin-typography"> admin </Typography>
              )}
            </AccordionSummary>
            <AccordionDetails id="accordion-details">
              <Box>
                <MenuList>
                  <MenuItem onClick={() => this.handleClick(member.email)}>
                    <Typography id="icon">
                      <EmailIcon />
                    </Typography>
                    <Typography id="value" noWrap>
                      {member.email}
                    </Typography>
                  </MenuItem>
                  {member.contactLinks.map((link, index) => (
                    <MemberContact
                      key={index}
                      type={link.linkType.toLowerCase()}
                      value={link.link}
                    />
                  ))}
                </MenuList>
                <Button
                  id="visit-member-profile-button"
                  onClick={() => {
                    this.props.history.push(`/user/${member.id}`);
                  }}
                >
                  Ver perfil de usuario
                </Button>
                {this.props.isAdmin && !(this.props.creatorId == member.id) && (
                  <Box id="kick-member-button-container">
                    <Button
                      id="kick-member-button"
                      onClick={() => this.props.kickMember(member.id)}
                    >
                      Echar del grupo
                    </Button>
                  </Box>
                )}
              </Box>
            </AccordionDetails>
          </Accordion>
        ))}{" "}
      </>
    );
  }
}

export default GroupMembersAccordion;
