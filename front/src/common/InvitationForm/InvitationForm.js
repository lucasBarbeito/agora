import {
  Accordion,
  AccordionActions,
  AccordionDetails,
  AccordionSummary,
  Button,
  Checkbox,
  Dialog,
  DialogContent,
  DialogContentText,
  DialogTitle,
  FormControlLabel,
  IconButton,
  MenuList,
  Snackbar,
  TextField,
  Typography,
} from "@material-ui/core";
import React, { useState } from "react";
import SearchIcon from "@material-ui/icons/Search";
import LinkIcon from "@material-ui/icons/Link";
import baseUrl from "../../baseUrl";
import CloseIcon from "@material-ui/icons/Close";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import MemberContact from "../MemberContact/MemberContact";
import "./InvitationForm.css";

export default function InvitationForm(props) {
  const [searchMsg, setSearchMsg] = useState("");
  const [openCopyLinkSnack, setOpenCopyLinkSnack] = useState(false);
  const [searchError, setSearchError] = useState(false);
  const [search, setSearch] = useState(true);
  const [users, setUsers] = useState([]);
  const [selectedMembers, setSelectedMembers] = useState([]);

  async function getInvitationLink() {
    const response = await fetch(
      `${baseUrl}/studyGroup/${props.groupId}/inviteLink`,
      {
        method: "GET",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
          Authorization: `Bearer ${props.token}`,
        },
      }
    );
    return await response.text();
  }

  async function copyInvitationLinkToClipboard() {
    try {
      let res = await getInvitationLink();
      navigator.clipboard.writeText(res);
      setOpenCopyLinkSnack(true);
    } catch (e) {
      alert("Error, no es posible conectarse al back-end");
    }
  }

  const searchUser = async () => {
    const esc = encodeURIComponent;
    try {
      if (searchMsg !== "") {
        setSearch(false);

        const response = await fetch(`${baseUrl}/user?name=${esc(searchMsg)}`, {
          headers: {
            "Content-type": "application/json; charset=UTF-8",
            Authorization: `Bearer ${props.token}`,
          },
        });

        const res = await response.json();
        const array = res.filter(
          (item) => !props.members.map((member) => member.id).includes(item.id)
        );

        setUsers(array);
        if (array.length === 0) setSearchError(true);
        else setSearchError(false);
      } else {
        setSearchError(true);
        setSearch(false);
        setUsers([]);
      }
    } catch (e) {
      alert("Error, no es posible conectarse al back-end");
    }
  };

  const handleNewMemberCheckBoxChanged = (checked, member) => {
    if (checked) {
      setSelectedMembers([...selectedMembers, member]);
    } else {
      setSelectedMembers(
        selectedMembers.filter((value, idx) => value !== member)
      );
    }
  };

  const sendInvitations = async () => {
    selectedMembers.forEach(async (member, index) => {
      try {
        const response = await fetch(
          `${baseUrl}/studyGroup/${props.groupId}/invite/${member.id}`,
          {
            method: "POST",
            headers: {
              "Content-type": "application/json; charset=UTF-8",
              Authorization: `Bearer ${props.token}`,
            },
          }
        );
      } catch (e) {
        alert("Error conectando con el back-end");
        console.log(e);
      }
    });

    props.onClose();
  };

  return (
    <div>
      <Dialog
        open={props.visible}
        onClose={props.onClose}
        fullWidth
        id="dialog"
      >
        <DialogTitle
          id="edit-group-customized-dialog-title"
          onClose={props.onClose}
        >
          <div id="edit-group-customized-dialog-title-paragraph">
            Invitar usuarios al grupo
          </div>
          <DialogContentText>
            <div id="invitation-description-message">
              Busca a usuarios registrados en la plataforma para invitar al
              grupo para que puedan formar parte de la discusión.
            </div>
          </DialogContentText>
        </DialogTitle>
        <div className="search-user">
          <TextField
            fullWidth
            label="Buscar usuarios"
            variant="outlined"
            onChange={(text) => setSearchMsg(text.target.value)}
          />
          <IconButton id="invitation-search-button" onClick={searchUser}>
            <SearchIcon />
          </IconButton>
        </div>
        {searchError ? (
          <div id="invitation-no-result-message">
            No hay resultados para su búsqueda. Si el usuario que está buscando
            no existe, pruebe compartiendo el link de invitación de grupo.
          </div>
        ) : null}

        {search ? (
          <div id="invitation-no-result-message">
            Ingrese el nombre de usuario que quiere agregar al grupo. Si no,
            pruebe compartiendo el link de invitación de grupo.
          </div>
        ) : null}
        {users.length !== 0 ? (
          <div className="invitation-accordion">
            {" "}
            {users.map((member, index) => (
              <Accordion id="selection-member-accordion" key={index}>
                <AccordionSummary
                  expandIcon={<ExpandMoreIcon />}
                  aria-controls="panel1a-content"
                  id="panel1a-header"
                >
                  <FormControlLabel
                    aria-label="Acknowledge"
                    onClick={(event) => event.stopPropagation()}
                    onFocus={(event) => event.stopPropagation()}
                    control={
                      <Checkbox
                        color="default"
                        onChange={(e) => {
                          handleNewMemberCheckBoxChanged(
                            e.target.checked,
                            member
                          );
                        }}
                      />
                    }
                    label={
                      <Typography id="user-name-typography">
                        {member.name} {member.surname}
                      </Typography>
                    }
                  />
                </AccordionSummary>
                <AccordionDetails id="accordion-detail">
                  <MenuList>
                    {member.contactLinks.map((link, index) => (
                      <MemberContact
                        key={index}
                        type={link.linkType.toLowerCase()}
                        value={link.link}
                      />
                    ))}
                  </MenuList>
                </AccordionDetails>
                <AccordionActions id="user-invitation-button">
                  <Button id="submit-invitation">Ver perfil de usuario</Button>
                </AccordionActions>
              </Accordion>
            ))}{" "}
          </div>
        ) : null}

        {searchError || search ? (
          <div className="invite-link-button">
            <Button
              fullWidth
              id="copy-link-button"
              variant="contained"
              color="primary"
              onClick={copyInvitationLinkToClipboard}
            >
              <LinkIcon id="invite-icon" />
              Copiar link de invitacion
            </Button>
          </div>
        ) : null}

        <Snackbar
          open={openCopyLinkSnack}
          autoHideDuration={5000}
          onClose={() => setOpenCopyLinkSnack(false)}
          message={<Typography>Link copiado exitosamente!</Typography>}
          action={[
            <IconButton
              color="inherit"
              onClick={() => setOpenCopyLinkSnack(false)}
            >
              <CloseIcon />
            </IconButton>,
          ]}
        />
        <div className="dialog-submit-button">
          <Button id="cancel-invitation" onClick={() => props.onClose()}>
            Cancelar
          </Button>
          <Button
            id="submit-invitation"
            disabled={selectedMembers.length === 0}
            onClick={sendInvitations}
          >
            Enviar invitaciones
          </Button>
        </div>
      </Dialog>
    </div>
  );
}
