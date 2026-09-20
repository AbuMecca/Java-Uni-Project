/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xo_game;

/**
 * Score:
 * Represents one round’s result: player1 vs. player2 and who won.
 */
public class Score {
    private final String player1, player2, winner;

    public Score(String player1, String player2, String winner) {
        this.player1 = player1;
        this.player2 = player2;
        this.winner  = winner;
    }

    public String getPlayer1()  { return player1; }
    public String getPlayer2()  { return player2; }
    public String getWinner()   { return winner; }
}
