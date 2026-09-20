/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xo_game;

import javafx.scene.image.Image;

public class Player extends Participant {
    private final char symbol;        // unused except for logic
    private final Image symbolImage;  // user-chosen image

    public Player(String gamerTag, char symbol, Image img) {
        super(gamerTag);
        this.symbol = symbol;
        this.symbolImage = img;
    }

    public char getSymbol() { return symbol; }
    public Image getSymbolImage() { return symbolImage; }

    @Override
    public String toString() {
        return "Player " + id + ": " + gamerTag + " (" + symbol + ")";
    }
}

