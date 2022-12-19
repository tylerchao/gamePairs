package logic;

import logic.PairsLogic.Solved;
import static logic.PairsLogic.Solved.FST;
import static logic.PairsLogic.Solved.NOT;
import static logic.PairsLogic.Solved.SND;
import logic.PairsLogic.Symbol;
import static logic.PairsLogic.Symbol.BAT;
import static logic.PairsLogic.Symbol.BEE;
import static logic.PairsLogic.Symbol.PIG;
import static logic.PairsLogic.Symbol.POO;
import static logic.PairsLogic.Symbol.OWL;

import org.junit.Test;
import static org.junit.Assert.*;
import static logic.PairsLogic.Symbol.BUG;

/**
 * Test cases for the logic of the Game Pairs. While these test cases cover a
 * lot, they do not cover all possible scenarios. It is still possible for the
 * logic to malfunction, albeit in special cases. Thinking about own test cases
 * is recommended.
 *
 * @author cei,
 */
public class PubPairsLogicTest {

    /**
     * If this test fails, all other tests can be ignored - they might just fail
     * because they require one of the two tested methods.
     */
    @Test
    public void testFieldToStringAndTestConstructor4x3() {
        PairsLogic game = new PairsLogic(new String[]{"Marcus", "Cordula"},
                new Symbol[][]{
                        {BEE, BAT, OWL},
                        {BEE, BAT, OWL},
                        {PIG, POO, BUG},
                        {PIG, POO, BUG}
                },
                new Solved[][]{
                        {NOT, NOT, NOT},
                        {NOT, NOT, NOT},
                        {NOT, NOT, NOT},
                        {NOT, NOT, NOT}
                }, new FakeGUI());
        //expecting the additional whitespace and linebreak after each symbol
        //or line makes the implementation easier. As this method is just for
        //testing anyway, this is acceptable behaviour.
        assertEquals("BEE   BEE   PIG   PIG   \n"
                        + "BAT   BAT   POO   POO   \n"
                        + "OWL   OWL   BUG   BUG   \n",
                game.cardsToString());
    }

    /**
     * If this test fails, all other tests can be ignored - they might just fail
     * because they require one of the two tested methods.
     */
    @Test
    public void testFieldToStringAndTestConstructor2x2() {
        PairsLogic game = new PairsLogic(new String[]{"Marcus", "Cordula"},
                new Symbol[][]{
                        {BEE, BAT},
                        {BEE, BAT},},
                new Solved[][]{
                        {NOT, NOT},
                        {NOT, NOT},}, new FakeGUI());
        //expecting the additional whitespace and linebreak after each symbol
        //or line makes the implementation easier. As this method is just for
        //testing anyway, this is acceptable behaviour.
        assertEquals("BEE   BEE   \n"
                + "BAT   BAT   \n", game.cardsToString());
    }

    @Test
    public void testPlayerDoesNotChangeAfterFirstCard2x2() {
        PairsLogic game = new PairsLogic(new String[]{"Marcus", "Cordula"},
                new Symbol[][]{
                        {BEE, BAT},
                        {BEE, BAT},},
                new Solved[][]{
                        {NOT, NOT},
                        {NOT, NOT},}, new FakeGUI());
        String currPlayer = game.getNameCurrentPlayer();
        game.playerTurn(new int[]{0, 0});
        assertEquals(currPlayer, game.getNameCurrentPlayer());
    }

    @Test
    public void testPlayerChangesAfterSecondCard2x2() {
        PairsLogic game = new PairsLogic(new String[]{"Marcus", "Cordula"},
                new Symbol[][]{
                        {BEE, BAT},
                        {BEE, BAT},},
                new Solved[][]{
                        {NOT, NOT},
                        {NOT, NOT},}, new FakeGUI());
        String currPlayer = game.getNameCurrentPlayer();  // marcus
        game.playerTurn(new int[]{0, 0});  // marcus -1
        game.playerTurn(new int[]{1, 1});  // marcus-1
        assertNotEquals(currPlayer, game.getNameCurrentPlayer());
    }

    @Test
    public void testAllSolved_NothingSolved() {
        PairsLogic game = new PairsLogic(new String[]{"Marcus", "Cordula"},
                new Symbol[][]{
                        {BEE, BAT, OWL},
                        {BEE, BAT, OWL},
                        {PIG, POO, BUG},
                        {PIG, POO, BUG}
                },
                new Solved[][]{
                        {NOT, NOT, NOT},
                        {NOT, NOT, NOT},
                        {NOT, NOT, NOT},
                        {NOT, NOT, NOT}
                }, new FakeGUI());
        assertFalse(game.allSolved());
    }

    @Test
    public void testAllSolved_SomethingsSolved() {
        PairsLogic game = new PairsLogic(new String[]{"Marcus", "Cordula"},
                new Symbol[][]{
                        {BEE, BAT, OWL},
                        {BEE, BAT, OWL},
                        {PIG, POO, BUG},
                        {PIG, POO, BUG}
                },
                new Solved[][]{
                        {FST, NOT, FST},
                        {FST, NOT, FST},
                        {NOT, SND, NOT},
                        {NOT, SND, NOT}
                }, new FakeGUI());
        assertFalse(game.allSolved());
    }

    @Test
    public void testAllSolved_Everything() {
        PairsLogic game = new PairsLogic(new String[]{"Marcus", "Cordula"},
                new Symbol[][]{
                        {BEE, BAT, OWL},
                        {BEE, BAT, OWL},
                        {PIG, POO, BUG},
                        {PIG, POO, BUG}
                },
                new Solved[][]{
                        {FST, FST, FST},
                        {FST, FST, FST},
                        {SND, SND, SND},
                        {SND, SND, SND}
                }, new FakeGUI());
        assertTrue(game.allSolved());
    }

    @Test
    public void testAllSolvedNoWinner() {
        PairsLogic game = new PairsLogic(new String[]{"Marcus", "Cordula"},
                new Symbol[][]{
                        {BEE, BAT, OWL},
                        {BEE, BAT, OWL},
                        {PIG, POO, BUG},
                        {PIG, POO, BUG}
                },
                new Solved[][]{
                        {FST, FST, FST},
                        {FST, FST, FST},
                        {SND, SND, SND},
                        {SND, SND, SND}
                }, new FakeGUI());
        assertTrue(game.allSolved());
        assertNull(game.getWinnerName());
    }

    @Test
    public void testAllSolvedAWinner() {
        PairsLogic game = new PairsLogic(new String[]{"Marcus", "Cordula"},
                new Symbol[][]{
                        {BEE, BAT, OWL},
                        {BEE, BAT, OWL},
                        {PIG, POO, BUG},
                        {PIG, POO, BUG}
                },
                new Solved[][]{
                        {SND, FST, FST},
                        {SND, FST, FST},
                        {SND, SND, SND},
                        {SND, SND, SND}
                }, new FakeGUI());
        assertTrue(game.allSolved());
        assertEquals("Cordula", game.getWinnerName());
    }


    @Test
    public void testCardAlreadySolvedPlayerCanChooseAgain() {
        PairsLogic game = new PairsLogic(new String[]{"Marcus", "Cordula"},
                new Symbol[][]{
                        {BEE, BAT, OWL},
                        {BEE, BAT, OWL},
                        {PIG, POO, BUG},
                        {PIG, POO, BUG}
                },
                new Solved[][]{
                        {FST, NOT, FST},
                        {FST, NOT, FST},
                        {NOT, SND, NOT},
                        {NOT, SND, NOT}
                }, new FakeGUI());

        String currPlayer = game.getNameCurrentPlayer();
        game.playerTurn(new int[]{0, 0});
        assertEquals(currPlayer, game.getNameCurrentPlayer());
        game.playerTurn(new int[]{1, 1});
        assertEquals(currPlayer, game.getNameCurrentPlayer());
        game.playerTurn(new int[]{2, 2});
        assertNotEquals(currPlayer, game.getNameCurrentPlayer());
    }





}
