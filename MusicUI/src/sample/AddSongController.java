package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sample.model.Album;
import sample.model.Datasource;


public class AddSongController {
    @FXML
    private TextField titleField;
    @FXML
    private TextField trackField;
    @FXML
    private TextField albumField;
    @FXML
    private TextField artistField;

    public void addSong() {
        String title = titleField.getText().trim();
        int track = Integer.parseInt(trackField.getText().trim());
        String album = albumField.getText().trim();
        String artist = artistField.getText().trim();
        Datasource.getInstance().insertSong(title,track,album,artist);
    }


}
