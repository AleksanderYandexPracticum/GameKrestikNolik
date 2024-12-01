package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Main extends Application {

    protected static String currentPlayer = "X";
    protected static boolean isGameStopped = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

        primaryStage.setResizable(false);
        primaryStage.setTitle("ИГРА КРЕСТИКИ-НОЛИКИ");
        primaryStage.setScene(new Scene(root, 400, 530));
        primaryStage.show();

        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (isGameStopped && event.getCode() == KeyCode.ESCAPE) {
                controller.startNewGameClickEsc();
                System.out.println("ESC");
                return;
            }
            if (!isGameStopped && event.getCode() == KeyCode.SPACE) {
                controller.breakGameAndRestartClickSpace();
                System.out.println("space");
                return;
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
