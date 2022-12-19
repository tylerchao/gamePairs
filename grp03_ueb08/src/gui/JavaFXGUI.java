package gui;

import javafx.scene.control.Label;
import logic.GUIConnector;
import logic.PairsLogic.Symbol;
import javafx.scene.control.Button;

import javax.swing.*;

/**
 * This class is responsible for changing the gui when the logic deems it
 * necessary. Created by the gui and then passed as a parameter into the logic.
 * <br>
 * Addtional private or protected methods may be added to this.
 *
 * @author cei,chien-hsun, husnain
 */
public class JavaFXGUI implements GUIConnector {

    /**
     * The buttons of the game field stored in an array (position in the array =
     * position on the surface).
     */
    private Button[][] btnsField;


    private Label lblCurrentPlayer;
    private Label lblWinner;
    private Button newGame;

    /**
     * The constructor. Gets passed all components of the gui that may change
     * due to actions in the logic.
     *
     * @param btns             the buttons of the game field (can change their text to the
     *                         symbols of the players)
     *                         <br>
     *
     * @param lblCurrentPlayer
     * @param lblWinner
     */
    public JavaFXGUI(Button[][] btns, Label lblCurrentPlayer, Label lblWinner, Button newGame) {
        this.btnsField = btns;
        this.lblCurrentPlayer = lblCurrentPlayer;
        this.lblWinner = lblWinner;
        this.newGame = newGame;
    }


    /**
     * BEE, HONEY, BEAR, PIG,
     * CAKE, OWL, GHOST, BOOK, BAT,
     * POO, DOG, UNICORN, BUG;
     *
     * @param coord  coordinate
     * @param symbol the symbol which should be displayed at the given
     */
    @Override
    public void showCard(int[] coord, Symbol symbol) {
        if (symbol.toString() == "BEE") {
            btnsField[coord[0]][coord[1]].setText("\uD83D\uDC1D");
        } else if (symbol.toString() == "HONEY") {
            btnsField[coord[0]][coord[1]].setText("\uD83C\uDF6F");
        } else if (symbol.toString() == "BEAR") {
            btnsField[coord[0]][coord[1]].setText("\uD83D\uDC3B");
        } else if (symbol.toString() == "PIG") {
            btnsField[coord[0]][coord[1]].setText("\uD83D\uDC16");
        } else if (symbol.toString() == "CAKE") {
            btnsField[coord[0]][coord[1]].setText("\uD83C\uDF82");
        } else if (symbol.toString() == "OWL") {
            btnsField[coord[0]][coord[1]].setText("\uD83E\uDD89");
        } else if (symbol.toString() == "GHOST") {
            btnsField[coord[0]][coord[1]].setText("\uD83D\uDC7B");
        } else if (symbol.toString() == "BOOK") {
            btnsField[coord[0]][coord[1]].setText("\uD83D\uDCD6");
        } else if (symbol.toString() == "BAT") {
            btnsField[coord[0]][coord[1]].setText(" \uD83E\uDD87");
        } else if (symbol.toString() == "POO") {
            btnsField[coord[0]][coord[1]].setText("\uD83D\uDCA9");
        } else if (symbol.toString() == "DOG") {
            btnsField[coord[0]][coord[1]].setText("\uD83D\uDC15");
        } else if (symbol.toString() == "UNICORN") {
            btnsField[coord[0]][coord[1]].setText("\uD83E\uDD84");
        } else if (symbol.toString() == "BUG") {
            btnsField[coord[0]][coord[1]].setText("\uD83D\uDC1B");
        }
    }

    @Override
    public void hideCard(int[] pos) {
        btnsField[pos[0]][pos[1]].setText("");

    }

    @Override
    public void setCurrentPlayer(String name) {
        lblCurrentPlayer.setText(name);
    }


    @Override
    public void onGameEnd(String winnerName) {
        if (winnerName != null) {
            lblWinner.setVisible(true);
            lblWinner.setText("   Winner   \n" + "    is \n" + winnerName);
        } else {
            lblWinner.setVisible(true);
            lblWinner.setText("no one");
        }
        newGame.setDisable(false);
    }
}
