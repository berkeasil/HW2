import java.util.*;

public class Tile {
    
    int value;

    /*
     * creates a tile using the given value. False jokers are not included in this game.
     */
    public Tile(int value) {
        this.value = value;
    }

    /*
     * DONE: Checks if the given tile t and this tile have the same value 
     * returns true if they are matching, false otherwise
     */
    public boolean matchingTiles(Tile t) {
        return this.getValue() == t.getValue();
    }

    /*
     * DONE: Compares the order of these two tiles
     * returns 1 if given tile has smaller in value
     * returns 0 if they have the same value
     * returns -1 if the given tile has higher value
     */
    public int compareTo(Tile t) {

       if(this.getValue() > t.getValue())
       {
            return 1;
       }
       else if (this.getValue() < t.getValue())
       {
            return -1;
       }
       else 
       {
        return 0;
       }
    }

    /*
     * DONE: Determines if this tile and given tile can form a chain together
     * checks the difference in values of the two tiles
     * returns true if the absoulute value of the difference is 1 (they can form a chain)
     * otherwise, returns false (they cannot form a chain)
     */
    public boolean canFormChainWith(Tile t) {
        return Math.abs(this.getValue() - t.getValue()) == 1;
    }

    public String toString() {
        return "" + value;
    }

    public int getValue() {
        return value;
    }

}