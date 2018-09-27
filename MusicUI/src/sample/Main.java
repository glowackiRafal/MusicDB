package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import sample.model.Datasource;


public class Main extends Application {
    private boolean exceptionInInit = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        if (exceptionInInit) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("FATAL ERROR!");
            alert.setContentText("Couldn't connect to database!");
            alert.showAndWait();
            Platform.exit();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
            Parent root = loader.load();
            Controller controller = loader.getController();
            controller.listArtists();
            primaryStage.setTitle("MusicDB");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        }
    }

    @Override
    public void init() throws Exception {
        super.init();
        if (!Datasource.getInstance().open()) {
            exceptionInInit = true;
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Datasource.getInstance().close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
