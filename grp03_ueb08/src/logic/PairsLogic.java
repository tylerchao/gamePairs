package logic;

import java.util.Random;

/**
 * Logic of the game "Pairs". In this game two player play against each other on
 * a 4x3 grid. The players alternatingly select two cards of which they would
 * like to see the symbols. If the symbols match, they found a pair. Aim of this
 * game is to find more pairs than the other player.
 *
 * @author cei ,chien-hsun, husnain
 */
public class PairsLogic {

     // TODO: done You are not allowed to define global Attributes.

    /**
     * Name of the players in an array. Length must be 2.
     */
    private String[] players;
    /**
     * Index of the player. Must be either 0 or 1. Always start with 0 (strictly
     * speaking initialization is not necessary, but we do it anyway).
     */
    private int idxCurrPlayer = 0;
    /**
     * Connection to the gui.
     */
    private GUIConnector gui;

    /**
     * Enum for the symbol of the cards. More symbols necessary than needed for
     * a standard-sized (4x3) game to make it more interesting.
     */
    public enum Symbol {
        BEE, HONEY, BEAR, PIG,
        CAKE, OWL, GHOST, BOOK, BAT,
        POO, DOG, UNICORN, BUG
    }


    /**
     * [row][col]
     * The 2-dimensional grid of cards.
     */
    private Symbol[][] cards;

    /**
     * Enum for information about who discovered (solved) a pair. Either it was
     * the first player, the second player or the pair has not been yet
     * discovered. The ordinal value of the respective symbol of a player
     * corresponds with the index of this player in the player array.
     */
    enum Solved {
        FST, SND, NOT
    }

    /**
     * To remember at which positions pairs have already been found by which
     * player. Important once all pairs have been found.
     */
    private Solved[][] cardsSolved;

    /**
     * The positions of the cards that have currently been revealed. This array
     * always has the length two! If the two contained arrays are both empty,
     * that means either the game has just started or a player has just found a
     * pair. If only there is only a position at the first index, this means the
     * player has only selected one card to be revealed yet and we need to wait
     * for him/her to select a second card. If there are two positions stored in
     * here, the previous player had selected two cards (not a pair).
     * for playerTurn
     * { {  } , {  } }
     */
    private int[][] posOpenCards = new int[2][0];

    /**
     * Constructor for a game of pairs. Initializes the field.
     *
     * @param p1     name of the first player
     * @param p2     name of the second player
     * @param width  width of the game field
     * @param height height of the game field
     * @param gui    connection to the gui
     */
    public PairsLogic(String p1, String p2, int width, int height,
                      GUIConnector gui) {
        if (width < 1 || height < 1 || width * height % 2 != 0)
            throw new AssertionError();

        this.players = new String[]{p1, p2};
        this.cardsSolved = new Solved[width][height];
        this.gui = gui;
        cards = new Symbol[width][height];
        gui.setCurrentPlayer(getNameCurrentPlayer());
        fillWithRandomCards(cards);

        //initialize cardsSolved array.
        for (int i = 0; i < width; i++) {       // row
            for (int j = 0; j < height; j++) {  // col
                cardsSolved[i][j] = Solved.NOT;
            }
        }
    }

    /**
     * package-private constructor for testing that gets passed an array with the player names,
     * an array of card symbols, an array with information which card has been already "solved" Symbol[][] cards,
     * Solved[][] cardsSolved and a connection to the gui (see below for more information regarding testing)
     */
    PairsLogic(String[] names, Symbol[][] symbols, Solved[][] cardSolved, GUIConnector gui) {
        this.players = names;
        this.cards = symbols;
        this.cardsSolved = cardSolved;
        this.gui = gui;
    }

    /**
     * fills the given array with random pairs of symbols at random position and then returns it
     * TODO:done  Duplicate symbols are not allowed on the field. Make sure every pair has a different symbol.
     *
     * @param cards input cards which are suppose to be filled in cards and returned
     * @return Symbol 2D Array of cards filled with randum cards
     */
    private Symbol[][] fillWithRandomCards(Symbol[][] cards) {
        Random generator = new Random();
        // we added
        while (!noPositionLeft(cards)) {
            int[] freePosition = getFreePos(cards);
            int Row = freePosition[0];
            int Col = freePosition[1];
            Symbol trackingValue= Symbol.values()[generator.nextInt(Symbol.values().length)];//bee
            for(int i= 0; i < cards.length; i++){
                for(int j=0; j< cards[i].length; j++){
                   if(cards[i][j]== trackingValue){
                       trackingValue = Symbol.values()[generator.nextInt(Symbol.values().length)];
                       i = 0;
                       j = 0;
                   }
                }
            }
            cards[Row][Col] = trackingValue;
            // check if there is some position left then call getfreePos method
            freePosition = getFreePos(cards);
            cards[freePosition[0]][freePosition[1]] = cards[Row][Col];
        }
        return cards;
    }



    /**
     * determines a random free position (represented through null) in the given array and returns it.
     * If there is no free position this method may run infinite.
     *
     * @param cards input cards array which are supposed to be checked and get free position in this array
     * @return 1D Array of free position having arr[0] should be "Row" arr[1] should be "Col"
     */
    private int[] getFreePos(Symbol[][] cards) {
        Random generator = new Random();
        // create array
        int[] freePosition = new int[2];
        // create a flag
        boolean foundFreePosition = false;
        // find randum position
        while (!foundFreePosition) {

            // we just need a location
            int row = generator.nextInt(cards.length);
            int col = generator.nextInt(cards[cards.length - 1].length);
            if (cards[row][col] == null) {
                // set the flag to stop the loop
                foundFreePosition = true;
                // store it to 1D Array (arr[0] = x-axis (row), arr[1], y-axis (col)) setting position
                freePosition[0] = row;
                freePosition[1] = col;
            }
        }
        return freePosition;
    }

    /**
     * returns true, if all pairs have been discovered
     *
     * @return true if all pairs are discovered,
     * false if all pairs are not discovered
     */
    protected boolean allSolved() {
        boolean allCardsSolved = true;
        for (int i = 0; i < cardsSolved.length && allCardsSolved; i++) {
            for (int j = 0; j < cardsSolved[i].length && allCardsSolved; j++) {
                if (cardsSolved[i][j] == Solved.NOT) {
                    // set the flag
                    allCardsSolved = false;
                }
            }
        }
        return allCardsSolved;
    }

    /**
     * returns the winner name, if there was a tie it returns null.
     * Use an assertion to ensure this method is only ever called once all cards has been solved
     *
     * @return winner name
     */
    protected String getWinnerName() {
        if (!allSolved()) {
            throw new AssertionError();
        }
        int firstPlayer = 0;
        int secondPlayer = 0;
        for (int i = 0; i < cardsSolved.length; i++) {
            for (int j = 0; j < cardsSolved[i].length; j++) {
                if (cardsSolved[i][j] == Solved.FST) {
                    firstPlayer++;
                } else if (cardsSolved[i][j] == Solved.SND) {
                    secondPlayer++;
                }
            }
        }

        if (firstPlayer > secondPlayer) {
            return players[0];
        } else if (secondPlayer > firstPlayer) {
            return players[1];
        } else {
            return null;
        }
    }

    /**
     * returns the name of the current player. Outside of PairsLogic it should only be used for testing.
     *
     * @return the name of the player
     */
    String getNameCurrentPlayer() {
        return players[idxCurrPlayer];
    }

    /**
     * only used for testing/debugging. Returns a String representation of the field.
     * Columns are separated by whitespace, rows by a line break.
     * For an example have a look at the given tests.
     * <p>
     * <p>testing
     * {{BEE, BAT, OWL},
     * {BEE, BAT, OWL},
     * {PIG, POO, BUG},
     * {PIG, POO, BUG},}
     *
     * <p> real output
     * "BEE   BEE   PIG   PIG   \n"
     * + "BAT   BAT   POO   POO   \n"
     * + "OWL   OWL   BUG   BUG   \n",
     *
     * @return output in the test case
     */
    String cardsToString() {
        String output = "";
        int col = cards[0].length;
        int row = cards.length;
        for (int j = 0; j < col; j++) {
            for (int i = 0; i < row; i++) {
                output = output + cards[i][j].name() + "   ";
                if (col == row && i == col - 1) {
                    output = output + "\n";
                } else if (col != row && i == col) {
                    output = output + "\n";
                }
            }
        }
        return output;
    }


    /**
     * Handles the turn of a player. If the cell chosen by the player is not
     * empty(" first or second "), nothing happens (the player can chose another cell).
     * If it was empty (" Not "), the symbol of the current player is placed and the update of the
     * gui is initiated. If it was the first card the player chose, not much
     * happens. If it was the second card of the player, his or her turn is
     * finished and its the turn of the next player. If a pair was found, it
     * should be checked if someone won the game. Contradictory to the common
     * rules for pairs, someone who found a pair will not get another try at
     * finding a pair right away.
     * <p>
     * <p>
     * Working Logic
     * // first time he will get 2 turns
     * // if the first card he choose is 'NOT' solved he will get another one turn
     * // he will choose second card
     * // if she choose a card which was already find out by {her or someone else} she still get second chance
     * // if the second card he choose was already find out then by (him or her, "the complete pair")
     * // he will get two chances to choose
     * // if we find out any of the pair we will keep it open
     * // if you choose a card whose pair is not yet find out you will get another chance
     *
     * @param pos coordinate in the field at which the player wants to place
     *            his/her symbol
     */
    public void playerTurn(int[] pos) {
        if (pos == null || pos.length != 2)
            throw new AssertionError();

        if (cardsSolved[pos[0]][pos[1]] == Solved.NOT) {
            // he found a pair or just start
            // simply start everything in the beginning of the game
            if (posOpenCards[0].length == 0 || (posOpenCards[0].length == 2 &&
                    !((posOpenCards[0][0] == pos[0] && posOpenCards[1][0] == pos[1])
                    || (posOpenCards[0][1] == pos[0] && posOpenCards[1][1] == pos[1])))) {
                // when every player make his/her first turn
                firstTurn(pos);
            }

            // Second turn of the player
            else if (posOpenCards[0].length == 1) {

                // player should not be able to press the button which was pressed on the first turn
                // if that's the button which was pressed in the first turn of the player then go inside this if coniditon
                if (!(posOpenCards[0][0] == pos[0] && posOpenCards[1][0] == pos[1])) {

                    // copying previous array values to new array
                    copyPrevArrayValues();
                    posOpenCards[0][1] = pos[0];
                    posOpenCards[1][1] = pos[1];
                    gui.showCard(pos, cards[pos[0]][pos[1]]);

                    //if the player find out the pair
                    if (cards[posOpenCards[0][0]][posOpenCards[1][0]] == cards[posOpenCards[0][1]][posOpenCards[1][1]]) {
                        pairFound();
                    }
                    // switch to other player in both conditions if pair is found or not
                    switchPlayerName();
                }

            }
        }

    }

    /**
     * additional method
     * copying previous array values
     * and copy them back to posOpenCards
     */
    private void copyPrevArrayValues(){
        int[][] PrevCardLocation = new int[2][1];
        PrevCardLocation[0][0] = posOpenCards[0][0];
        PrevCardLocation[1][0] = posOpenCards[1][0];
        posOpenCards = new int[2][2];
        posOpenCards[0][0] = PrevCardLocation[0][0];
        posOpenCards[1][0] = PrevCardLocation[1][0];
    }


    /**
     * additional method
     * the methods used to be called when we find out the pair
     * set posOpenCards to initial phase
     * call the allSolved method and set the winner if we find out the winner
     */
    private void pairFound() {
        // which player have find out the pair
        // set the pair inside cardsolved
        if (idxCurrPlayer == 0) {
            cardsSolved[posOpenCards[0][0]][posOpenCards[1][0]] = Solved.FST;
            cardsSolved[posOpenCards[0][1]][posOpenCards[1][1]] = Solved.FST;
        } else if (idxCurrPlayer == 1) {
            cardsSolved[posOpenCards[0][0]][posOpenCards[1][0]] = Solved.SND;
            cardsSolved[posOpenCards[0][1]][posOpenCards[1][1]] = Solved.SND;
        }
        // set the posOpenCards to posOpenCards = new int[2][0]; because we found the pair
        posOpenCards = new int[2][0];
        if (allSolved()) {
            gui.onGameEnd(getWinnerName());
        }
    }

    /**
     * additional method
     * Called when player choose first card
     * @param pos position of the first card
     */
    private void firstTurn(int[] pos) {
        hideEveyNotPairCard();
        // save the first position
        posOpenCards = new int[2][1];
        posOpenCards[0][0] = pos[0];
        posOpenCards[1][0] = pos[1];
        gui.showCard(pos, cards[pos[0]][pos[1]]);
    }

    /**
     * additional method
     * decide which the current player , and change after two times
     */
    private void switchPlayerName() {
        idxCurrPlayer = (idxCurrPlayer + 1) % players.length;
        gui.setCurrentPlayer(players[idxCurrPlayer]);
    }

    /**
     * additional method
     * hide evey card whose pairs is not being figured out yet by the players
     */
    private void hideEveyNotPairCard() {
        for (int i = 0; i < cardsSolved.length; i++) {
            for (int j = 0; j < cardsSolved[i].length; j++) {
                if (cardsSolved[i][j] == Solved.NOT) {
                    int[] position = new int[2];
                    position[0] = i;
                    position[1] = j;
                    gui.hideCard(position);
                }
            }
        }
    }

    /**
     * additional method
     * no position left
     *
     * @param cards input cards array which are supposed to be checked and get free position in this array
     * @return true, if there are no position left
     * false, if there are still position inside the cards array
     */
    private boolean noPositionLeft(Symbol[][] cards) {
        boolean noPosition = true;
        for (int i = 0; i < cards.length & noPosition; i++) {
            for (int j = 0; j < cards[i].length & noPosition; j++) {
                if (cards[i][j] == null) {
                    // set the flag
                    noPosition = false;
                }
            }
        }
        return noPosition;
    }
}