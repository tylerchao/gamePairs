package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import logic.GUIConnector;
import logic.PairsLogic;
import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * control gui of the game and all action perform by players
 * TODO: done It should be possible to start a new game at any time. This overrides the current game.
 * TODO: done After starting the game, clicking the field causes NullPointerException. A clickable button should never cause
 *       an Exception. Either disable buttons, that shouldn't be clicked at this time or start a game directly after
 *       starting the program with default names.
 * TODO: done Use default names in the Textflieds for the new game.
 * TODO: done Line the Caption auf the textflieds up.
 *
 * @author chien-hsun, husnain
 */
public class FXMLDocumentController implements Initializable {
    /**
     * Arranging the buttons
     */
    @FXML
    private Button btn00;
    @FXML
    private Button btn01;
    @FXML
    private Button btn02;
    @FXML
    private Button btn10;
    @FXML
    private Button btn11;
    @FXML
    private Button btn12;
    @FXML
    private Button btn20;
    @FXML
    private Button btn21;
    @FXML
    private Button btn22;
    @FXML
    private Button btn30;
    @FXML
    private Button btn31;
    @FXML
    private Button btn32;

    @FXML
    private Label lblWinner;
    @FXML
    private Label lbNameCurrentPlayer;

    @FXML
    private Button newGame;
    @FXML
    private Button startGame;
    @FXML
    private TextField firstPlayerName;
    @FXML
    private TextField secondPlayerName;
    @FXML
    private Label lbTextCurrentPlayer;


    // buttons array
    @FXML
    private Button[][] buttons;
    @FXML
    private GridPane grdPnGame;
    private PairsLogic game;
    @FXML
    private VBox vBoxLeft;

    /**
     * initializing the game field
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        grdPnGame.setPadding(Insets.EMPTY);
        vBoxLeft.setVisible(false);
        lblWinner.setVisible(false);
        startGame.setVisible(false);
        grdPnGame.setDisable(true);

        buttons = new Button[3][4];
        // treat interface as an array in natural way

        buttons[0][0] = btn00;
        buttons[0][1] = btn10;
        buttons[0][2] = btn20;
        buttons[0][3] = btn30;

        buttons[1][0] = btn01;
        buttons[1][1] = btn11;
        buttons[1][2] = btn21;
        buttons[1][3] = btn31;

        buttons[2][0] = btn02;
        buttons[2][1] = btn12;
        buttons[2][2] = btn22;
        buttons[2][3] = btn32;

        //first indx is col, second is Row   connecting program and interface
        for(int i=0;i<buttons.length;i++){
            for(int j=0;j<buttons[i].length;j++){
                GridPane.setRowIndex(buttons[i][j], i);
                GridPane.setColumnIndex(buttons[i][j], j);
            }
        }

    }

    /**
     * This method will be called when any of the card button in the field is pressed
     * @param actionEvent
     */
    @FXML
    private void handleCard(ActionEvent actionEvent) {
        int[] position= new int[2];

        Integer x = GridPane.getRowIndex(((Button) actionEvent.getSource()));
        int row = x.intValue();
        position[0] =row;

        Integer y = GridPane.getColumnIndex(((Button) actionEvent.getSource()));
        int col = y.intValue();
        position[1] = col;

        game.playerTurn( position);
    }

    /**
     * This method will be called when the new game button in the field is pressed
     * @param actionEvent
     */
    @FXML
    private void newGame(ActionEvent actionEvent) {
        vBoxLeft.setVisible(true);
        startGame.setVisible(true);
        newGame.setDisable(true);
        lbTextCurrentPlayer.setDisable(true);
        this.grdPnGame.setDisable(true);
        lblWinner.setVisible(false);
    }

    /**
     * This method will be called when the start game button in the field is pressed
     * @param actionEvent
     */
    @FXML
    private void startingTheGame(ActionEvent actionEvent) {
        vBoxLeft.setVisible(false);
        startGame.setVisible(false);
        newGame.setDisable(false);
        lbTextCurrentPlayer.setDisable(false);
        this.grdPnGame.setDisable(false);

        JavaFXGUI gui = new JavaFXGUI(buttons, lbNameCurrentPlayer, lblWinner, newGame);
        //TODO: done Don't use magic numbers. Get the dimensions from the gridpane.
            game = new PairsLogic(firstPlayerName.getText(), secondPlayerName.getText(), grdPnGame.getRowCount(), grdPnGame.getColumnCount(), gui);
        // hide all the cards
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                int[] positionOfButtonsHide = new int[2];
                positionOfButtonsHide[0] = i;
                positionOfButtonsHide[1] = j;
                gui.hideCard(positionOfButtonsHide);
            }
        }

    }
}
