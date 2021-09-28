import React, { useState } from "react";
import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import DialogActions from "@material-ui/core/DialogActions";
import { Slide, TextField } from "@material-ui/core";
import { Autocomplete } from "@material-ui/lab";

export default function CustomizedDialogs(props) {
  const [name, setName] = useState(props.initialGroupName);
  const [description, setDescription] = useState(props.initialDescription);
  const [label, setLabel] = useState([]);
  const labels = ["Etiqueta1", "Etiqueta2", "Etiqueta3"];

  const handleSaveChanges = () => {
    props.onChange(name, description);
    props.onClose();
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
            options={labels}
            filterSelectedOptions
            fullWidth
            autoFocus
            margin="normal"
            onChange={(newValue) => {
              setLabel({ newValue });
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
        </DialogContent>
        <DialogActions>
          <Button
            id="edit-group-save-changes-button"
            autoFocus
            onClick={() => props.onClose()}
          >
            Cancelar
          </Button>
          <Button
            id="edit-group-save-changes-button"
            autoFocus
            onClick={() => handleSaveChanges()}
          >
            Guardar Cambios
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
