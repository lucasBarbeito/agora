import React, { useState } from "react";
import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import DialogActions from "@material-ui/core/DialogActions";
import { TextField, CircularProgress, Box } from "@material-ui/core";
import { Autocomplete } from "@material-ui/lab";
import baseUrl from "../../baseUrl";

export default function CustomizedDialogs(props) {
  const [name, setName] = useState(props.initialGroupName);
  const [description, setDescription] = useState(props.initialDescription);
  const [waringMsg, setWarningMsg] = useState("");
  const [waitingResponse, setWaitingResponse] = useState(false);
  const [editUnsuccessfully, setEditUnsuccessfully] = useState(false);
  const [label, setLabel] = useState([]);

  const handleSaveChanges = () => {
    props.onChange(name, description);
    props.onClose();
  };

  const requestBackChanges = async () => {
    if (label.length > 0) {
      setWaitingResponse(true);
      setEditUnsuccessfully(false);
      setWarningMsg("");

      if (!name) {
        setEditUnsuccessfully(true);
        setWarningMsg("Por favor ingrese un nombre de grupo");
        setWaitingResponse(false);
      } else if (!description) {
        setEditUnsuccessfully(true);
        setWarningMsg("Por favor ingrese una descripción");
        setWaitingResponse(false);
      } else {
        try {
          const response = await fetch(
            `${baseUrl}/studyGroup/${props.groupId}`,
            {
              method: "PUT",
              body: JSON.stringify({
                description: description,
                name: name,
                labels: label,
              }),
              headers: {
                "Content-type": "application/json; charset=UTF-8",
                Authorization: `Bearer ${props.token}`,
              },
            }
          );
          if (response.ok) {
            setWaitingResponse(false);
            handleSaveChanges();
          } else if (response.status === 409) {
            setWarningMsg("Grupo creado con nombre ya existente");
            setWaitingResponse(false);
            setEditUnsuccessfully(true);
          }
        } catch (e) {
          alert("Error, no es posible conectarse al back-end");
        }
      }
    } else {
      alert("El grupo debe tener al menos una etiqueta");
    }
  };
  return (
    <div>
      <Dialog onClose={props.onClose} open={props.visible} fullWidth>
        <DialogTitle
          id="edit-group-customized-dialog-title"
          onClose={props.onClose}
        >
          <div id="edit-group-customized-dialog-title-paragraph">
            Editar Grupo{" "}
          </div>
        </DialogTitle>
        <DialogContent dividers scroll="paper">
          <TextField
            id="NameForm"
            label="Nombre"
            variant="outlined"
            defaultValue={name}
            onChange={(event) => {
              setName(event.target.value);
            }}
            fullWidth
            autoFocus
            margin="normal"
          />
          <Autocomplete
            multiple
            id="creategroup-tags-outlined"
            options={props.tags.map((index) => index.name)}
            filterSelectedOptions
            defaultValue={props.groupLabel}
            fullWidth
            autoFocus
            margin="normal"
            onChange={(event, newValue) => {
              setLabel(
                props.tags.filter((item) => newValue.includes(item.name))
              );
            }}
            renderInput={(params) => (
              <TextField
                {...params}
                fullWidth
                variant="outlined"
                label="Etiquetas"
                autoFocus
                margin="normal"
              />
            )}
          />
          <TextField
            id="creategroup-outlined-multiline-static"
            label="Descripción"
            variant="outlined"
            multiline
            fullWidth
            rows={6}
            autoFocus
            margin="normal"
            defaultValue={description}
            onChange={(text) => setDescription(text.target.value)}
          />
          <div style={{ display: "flex", justifyContent: "center" }}>
            {waitingResponse ? <CircularProgress size={20} /> : null}
          </div>
          {editUnsuccessfully ? (
            <Box id="editGroup-warning-box">
              <div id="editGroup-warning-message">{waringMsg}</div>
            </Box>
          ) : null}
        </DialogContent>
        <DialogActions>
          <Button
            id="edit-group-save-changes-button"
            autoFocus
            disabled={waitingResponse}
            onClick={() => props.onClose()}
          >
            Cancelar
          </Button>
          <Button
            id="edit-group-save-changes-button"
            autoFocus
            disabled={waitingResponse}
            onClick={() => requestBackChanges()}
          >
            Guardar Cambios
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
