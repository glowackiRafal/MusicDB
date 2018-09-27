package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import sample.model.*;

import java.io.IOException;
import java.util.Optional;


public class Controller {
    @FXML
    private TableView artistTable;
    @FXML
    private Button showAlbums;
    @FXML
    private Button updateArtist;
    @FXML
    private ProgressBar progressBar;

    @FXML
    public Dialog<ButtonType> dialogsHandler(FXMLLoader loader, String title, String fxmlFile, boolean twoButton) {
        Dialog<ButtonType> dialog = new Dialog<>();
        if (artistTable.getSelectionModel().getSelectedItem() != null) {
            if (artistTable.getSelectionModel().getSelectedItem().getClass().equals(Artist.class)) {
                Artist artist = (Artist) artistTable.getSelectionModel().getSelectedItem();
                dialog.setTitle(title + artist.getName());
            } else if (artistTable.getSelectionModel().getSelectedItem().getClass().equals(Album.class)) {
                Album album = (Album) artistTable.getSelectionModel().getSelectedItem();
                dialog.setTitle(title + album.getName());
            }
        } else {
            dialog.setTitle(title);
        }
        loader.getClass().getResource(fxmlFile);
        loader.setLocation(getClass().getResource(fxmlFile));
        if (dialogWindow(dialog, loader)) return null;
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        if (twoButton) {
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        }
        return dialog;
    }

    @FXML
    public void songQuery() {
        Dialog<ButtonType> dialog;
        FXMLLoader loader = new FXMLLoader();
//        ButtonType delete = new ButtonType("Delete");                                 TODO LATER
        if (artistTable.getSelectionModel().getSelectedItem() != null) {
            if (artistTable.getSelectionModel().getSelectedItem().getClass().equals(Artist.class)) {
                Artist artist = (Artist) artistTable.getSelectionModel().getSelectedItem();
                dialog = dialogsHandler(loader, "Song list for artist: ", "songQueryDialog.fxml", false);
//                dialog.getDialogPane().getButtonTypes().add(delete);
                SongQueryDialogController controller = loader.getController();
                controller.listSongs(artist.getId(), -1);
            } else {

                Album album = (Album) artistTable.getSelectionModel().getSelectedItem();
                dialog = dialogsHandler(loader, "Song list for album: ", "songQueryDialog.fxml", false);
//                dialog.getDialogPane().getButtonTypes().add(delete);
                SongQueryDialogController controller = loader.getController();
                controller.listSongs(-1, album.getId());
            }
        } else {
            dialog = dialogsHandler(loader, "Song list", "songQueryDialog.fxml", false);
//            dialog.getDialogPane().getButtonTypes().add(delete);
            SongQueryDialogController controller = loader.getController();
            controller.listSongs(-1, -1);
        }
        dialog.showAndWait();
    }

    @FXML
    public void addSongHandler() {
        FXMLLoader loader = new FXMLLoader();
        Dialog<ButtonType> dialog = dialogsHandler(loader, "Adding new song", "addSongDialog.fxml", true);
        AddSongController controller = loader.getController();
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call() {
                    try {
                        controller.addSong();
                        return true;
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setContentText("Operation failed.");
                        alert.showAndWait();
                        dialog.close();
                        return false;
                    }

                }
            };
            task.setOnSucceeded(e -> listArtists());
            new Thread(task).start();
        }
    }

//    @FXML
//    public void deleteHandler() {                                                         TODO LATER
//        SongQueryDialogController controller = new SongQueryDialogController();
//        SongsQueryData song = controller.getSelectedSong();
//        if (isSelected(song)) return;
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Deleting song: " + song);
//        alert.setContentText("To confirm operation press \"OK\".");
//        alert.showAndWait();
//    }

    @FXML
    public void updateButtonHandler() {
        Artist artist = (Artist) artistTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader();
        Dialog<ButtonType> dialog = dialogsHandler(loader, "Updating artist: ", "editDialog.fxml", true);
        EditDialogController controller = loader.getController();
        controller.fillNameField(artist.getName());
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call() {
                    return Datasource.getInstance().updateArtist(controller.editArtist(), artist.getId());

                }
            };
            task.setOnSucceeded(e -> listArtists());
            new Thread(task).start();
        }
    }

    @FXML
    public void albumsForArtist() {
        Artist artist = (Artist) artistTable.getSelectionModel().getSelectedItem();
        if (isSelected(artist)) return;
        showAlbums.setDisable(true);
        updateArtist.setDisable(true);
        Task<ObservableList<Album>> task = new Task<>() {
            @Override
            protected ObservableList<Album> call() {
                return FXCollections.observableArrayList(Datasource.getInstance().queryAlbumsForArtistById(artist.getId()));
            }
        };
        artistTable.itemsProperty().bind(task.valueProperty());
        new Thread(task).start();
    }


    @FXML
    public void listArtists() {
        Task<ObservableList<Artist>> task = new GetAllArtistsTask();
        showAlbums.setDisable(false);
        updateArtist.setDisable(false);
        artistTable.itemsProperty().bind(task.valueProperty());
        progressBar.progressProperty().bind(task.progressProperty());
        progressBar.setVisible(true);
        task.setOnSucceeded(e -> progressBar.setVisible(false));
        task.setOnFailed(e -> progressBar.setVisible(false));
        new Thread(task).start();
    }

    private boolean isSelected(Object obj) {
        if (obj == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Item Selected");
            alert.setHeaderText("Warning!");
            alert.setContentText("Please select the item you want to operate on.");
            alert.showAndWait();
            return true;
        }
        return false;
    }

    private boolean dialogWindow(Dialog<ButtonType> dialog, FXMLLoader fxmlLoader) {
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return true;
        }
        return false;
    }

}

class GetAllArtistsTask extends Task {
    @Override
    public ObservableList<Artist> call() {
        return FXCollections.observableArrayList(Datasource.getInstance().queryArtists(Datasource.ORDER_BY_ASC));
    }
}

