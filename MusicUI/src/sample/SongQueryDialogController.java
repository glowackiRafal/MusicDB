package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sample.model.Datasource;
import sample.model.SongsQueryData;

public class SongQueryDialogController {
    @FXML
    private TableColumn artist;
    @FXML
    private TableColumn album;
    @FXML
    private TableView<SongsQueryData> songsTable = new TableView<>();

    public SongsQueryData getSelectedSong() {
        return songsTable.getSelectionModel().getSelectedItem();
    }

    public void listSongs(int artistId, int albumId) {
        if (albumId == -1) {
            artist.setVisible(false);
        }
        if (artistId == -1) {
            album.setVisible(false);
            artist.setVisible(false);
        }
        if (artistId == -1 && albumId == -1) {
            album.setVisible(true);
            artist.setVisible(true);
        }
        Task<ObservableList<SongsQueryData>> task = new Task<>() {
            @Override
            protected ObservableList<SongsQueryData> call() {
                return FXCollections.observableArrayList(Datasource.getInstance().songsQuery(artistId, albumId));
            }
        };
        songsTable.itemsProperty().bind(task.valueProperty());
        new Thread(task).start();
    }
}
