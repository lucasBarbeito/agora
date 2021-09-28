import { Component } from "react";
import { Typography, MenuItem } from "@material-ui/core";
import "./MemberContact.css";
import LocationOnIcon from "@material-ui/icons/LocationOn";
import EmailIcon from "@material-ui/icons/Email";
import PhoneIcon from "@material-ui/icons/Phone";
import InstagramIcon from "@material-ui/icons/Instagram";
import FacebookIcon from "@material-ui/icons/Facebook";
import TwitterIcon from "@material-ui/icons/Twitter";

const contactIcons = {
  email: <EmailIcon />,
  phone: <PhoneIcon />,
  instagram: <InstagramIcon />,
  facebook: <FacebookIcon />,
  twitter: <TwitterIcon />,
};

const contactURLs = {
  email: "mailto:",
  phone: "tel:",
  instagram: "https://www.instagram.com/",
  facebook: "https://www.facebook.com/",
  twitter: "https://twitter.com/",
};

class MemberContact extends Component {
  constructor(props) {
    super(props);
  }

  handleClick = (type, value) => {
    window.open(contactURLs[type] + value, "_blank");
  };

  render() {
    return contactIcons.hasOwnProperty(this.props.type) ? (
      <MenuItem
        onClick={() => this.handleClick(this.props.type, this.props.value)}
      >
        <div className="link">
          <Typography id="icon">{contactIcons[this.props.type]}</Typography>
          <Typography id="value" noWrap>
            {this.props.value}
          </Typography>
        </div>
      </MenuItem>
    ) : (
      <MenuItem>
        <div className="link">
          <Typography id="icon">
            <LocationOnIcon />
          </Typography>
          <Typography id="value" noWrap>
            {this.props.value}
          </Typography>
        </div>
      </MenuItem>
    );
  }
}

export default MemberContact;
