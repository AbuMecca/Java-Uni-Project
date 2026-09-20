/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xo_game;


public abstract class Participant {
    private static int participantCount = 0;  

    protected final int id;                   
    protected final String gamerTag;          

    protected Participant(String gamerTag) {
        this.id = ++participantCount;         
        this.gamerTag = gamerTag;
    }

    public int getId() {
        return id;
    }

    public String getGamerTag() {
        return gamerTag;
    }
}
