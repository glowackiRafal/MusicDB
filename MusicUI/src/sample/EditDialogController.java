package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EditDialogController {

    @FXML
    private TextField nameField;

    public String editArtist() {
        String newName = nameField.getText().trim();
        return newName;
    }

    public void fillNameField(String name) {
        nameField.setText(name);
    }
}
