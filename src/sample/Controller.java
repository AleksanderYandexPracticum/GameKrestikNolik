package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class Controller {

    @FXML
    private Text pole_0x0;
    @FXML
    private Text pole_0x1;
    @FXML
    private Text pole_0x2;
    @FXML
    private Text pole_1x0;
    @FXML
    private Text pole_1x1;
    @FXML
    private Text pole_1x2;
    @FXML
    private Text pole_2x0;
    @FXML
    private Text pole_2x1;
    @FXML
    private Text pole_2x2;

    @FXML
    protected Text gameOver;

    private Text[][] arrayCell;

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void initialize() {
        arrayCell = new Text[][]{
                {pole_0x0, pole_1x0, pole_2x0},
                {pole_0x1, pole_1x1, pole_2x1},
                {pole_0x2, pole_1x2, pole_2x2}
        };
    }

    // Старт новой игры
    protected void startNewGameClickEsc() {
        Main.isGameStopped = false;
        Main.currentPlayer = "X";
        for (int x = 0; x < arrayCell.length; x++) {
            for (int y = 0; y < arrayCell[x].length; y++) {
                arrayCell[x][y].setFill(Paint.valueOf("white"));
                arrayCell[x][y].setText(" ");
            }
        }
    }

    // Сброс игры
    protected void breakGameAndRestartClickSpace() {
        Main.isGameStopped = false;
        Main.currentPlayer = "X";
        for (int x = 0; x < arrayCell.length; x++) {
            for (int y = 0; y < arrayCell[x].length; y++) {
                arrayCell[x][y].setFill(Paint.valueOf("white"));
                arrayCell[x][y].setText(" ");
            }
        }
    }


    @FXML
    protected void onClick(MouseEvent event) {
        if (Main.isGameStopped) {
            return;
        }
        if (((Text) event.getSource()).getText().equals(" ")) {
            setSignAndCheck(event);
            if(Main.isGameStopped){
                return;
            }

            // Ход компьютера!!!
            for (int i = 0; i < arrayCell.length; i++) {
                for (int j = 0; j < arrayCell[i].length; j++) {
                    if (arrayCell[i][j].getText().equals(" ")) {
                        computerTurn(event);
                        return;
                    }
                }
            }
        }
    }

    public void computerTurn(MouseEvent event) {
        // Ход в центр
        if (arrayCell[1][1].getText().equals(" ")) {
            setComp(arrayCell[1][1], event);
            return;
        }
        // Пробуем выиграть одним ходом
        for (int x = 0; x < arrayCell.length; x++) {
            for (int y = 0; y < arrayCell[x].length; y++) {
                if (checkFuture(x, y, "O")) {
                    setComp(arrayCell[x][y], event);
                    return;
                }
            }
        }
        // Мешаем противнику выиграть одним ходом
        for (int x = 0; x < arrayCell.length; x++) {
            for (int y = 0; y < arrayCell[x].length; y++) {
                if (checkFuture(x, y, "X")) {
                    setComp(arrayCell[x][y], event);
                    return;
                }
            }
        }

//         Ход в любую свободную клетку
        while (true) {
            int x = new Random().nextInt(3);
            int y = new Random().nextInt(3);

            if (arrayCell[x][y].getText().equals(" ")) {
                setComp(arrayCell[x][y], event);
                return;
            }
        }
    }

    // ход компьютера
    public void setComp(Text text, MouseEvent event) {
        text.setFill(Paint.valueOf("blue"));
        text.setText("O");
        Main.currentPlayer = "X";
        viewWinner(text);

    }

    // Проверка сможем ли выиграть в будующем или помешать выиграть
    public boolean checkFuture(int x, int y, String player) {
        if (!arrayCell[x][y].getText().equals(" ")) {
            return false;
        }
        arrayCell[x][y].setText(player);
        if (checkWin(player, x, y)) {
            arrayCell[x][y].setText(" ");
            return true;
        }

        arrayCell[x][y].setText(" ");
        return false;
    }


    // Ход и проверка
    public void setSignAndCheck(MouseEvent event) {
        if (Main.currentPlayer.equals("X")) {
            ((Text) event.getSource()).setFill(Paint.valueOf("red"));
            ((Text) event.getSource()).setText("X");
            Main.currentPlayer = "O";
        }
        viewWinner((Text) event.getSource());

    }

    // Проверка
    public void viewWinner(Text text) {
        int x = 0;
        int y = 0;

        EXIT:
        for (int i = 0; i < arrayCell.length; i++) {
            for (int j = 0; j < arrayCell[i].length; j++) {
                if (arrayCell[i][j].equals(text)) {
                    x = i;
                    y = j;
                    break EXIT;
                }
            }
        }
        if (checkWin("X", x, y)) {
            Main.isGameStopped = true;
            System.out.println("Выиграл первый игрок X");
            gameOver("Выиграл первый игрок X");
            return;
        }
        if (checkWin("O", x, y)) {
            Main.isGameStopped = true;
            System.out.println("Выиграл второй игрок O");
            gameOver("Выиграл второй игрок O");
            return;
        }
        if (!hasEmptyCell()) {
            System.out.println("Ничья!!!");
            gameOver("Ничья!!!");
            Main.isGameStopped = true;
            return;
        }
    }

    // Проверка на победителя
    public boolean checkWin(String player, int x, int y) {
        if (arrayCell[x][0].getText().equals(player) && arrayCell[x][1].getText().equals(player) && arrayCell[x][2].getText().equals(player)) {
            return true;
        }
        if (arrayCell[0][y].getText().equals(player) && arrayCell[1][y].getText().equals(player) && arrayCell[2][y].getText().equals(player)) {
            return true;
        }
        if (arrayCell[0][0].getText().equals(player) && arrayCell[1][1].getText().equals(player) && arrayCell[2][2].getText().equals(player)) {
            return true;
        }
        if (arrayCell[0][2].getText().equals(player) && arrayCell[1][1].getText().equals(player) && arrayCell[2][0].getText().equals(player)) {
            return true;
        }
        return false;
    }

    // Проверка наличия пустой ячейки
    public boolean hasEmptyCell() {
        for (int x = 0; x < arrayCell.length; x++) {
            for (int y = 0; y < arrayCell[x].length; y++) {
                if (arrayCell[x][y].getText().equals(" ")) {
                    return true;
                }
            }
        }
        return false;
    }

    private void gameOver(String str) {
        initialize();
        Stage stage = new Stage();
        BorderPane root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("gameOver.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root, 600, 300);
        stage.setTitle("Кто выиграл!!!!!!!!!!!");
//        gameOver.setFill(Paint.valueOf("green"));

        if (str.equals("Выиграл первый игрок X")) {
            scene.setFill(Paint.valueOf("green"));
            ((Text) root.getChildren().get(0)).setFill(Paint.valueOf("red"));
            ((Text) root.getChildren().get(0)).setText(str);
            ((Text) root.getChildren().get(0)).setLineSpacing(10);
        } else if (str.equals("Выиграл второй игрок O")) {
            scene.setFill(Paint.valueOf("grey"));
            ((Text) root.getChildren().get(0)).setFill(Paint.valueOf("blue"));
            ((Text) root.getChildren().get(0)).setText(str);
            ((Text) root.getChildren().get(0)).setLineSpacing(10);
        } else {
            ((Text) root.getChildren().get(0)).setFill(Paint.valueOf("magenta"));
            ((Text) root.getChildren().get(0)).setText(str);
            ((Text) root.getChildren().get(0)).setLineSpacing(10);
        }
        stage.setX(650);
        stage.setY(300);
        stage.setResizable(false);
        stage.setScene(scene);
        scene.getProperties();
        stage.show();
    }
}
