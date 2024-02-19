import java.util.*;

public class SimplifiedOkeyGame {

    Player[] players;
    Tile[] tiles;
    int tileCount;

    Tile lastDiscardedTile;

    int currentPlayerIndex = 0;

    int indexOfLastTile = 0; // counts index of tiles starting from 0

    public SimplifiedOkeyGame() {
        players = new Player[4];
    }

    public void createTiles() {
        tiles = new Tile[104];
        int currentTile = 0;

        // four copies of each value, no jokers
        for (int i = 1; i <= 26; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[currentTile++] = new Tile(i);
            }
        }

        tileCount = 104;
    }

    /*
     * TODO: distributes the starting tiles to the players
     * player at index 0 gets 15 tiles and starts first
     * other players get 14 tiles, this method assumes the tiles are already shuffled
     */
    public void distributeTilesToPlayers() {
        //DONE
        for (int i = 0; i < players.length; i++) {
            for (int j = 0; j < 14; j++) {
                players[i].addTile(tiles[indexOfLastTile]);
                indexOfLastTile++;
            }
        }
        players[0].addTile(tiles[indexOfLastTile]);
        indexOfLastTile++;
    }

    /*
     * TODO: get the last discarded tile for the current player
     * (this simulates picking up the tile discarded by the previous player)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getLastDiscardedTile() {
        //DONE
        players[currentPlayerIndex].addTile(lastDiscardedTile);
        return lastDiscardedTile.toString();
    }

    /*
     * TODO: get the top tile from tiles array for the current player
     * that tile is no longer in the tiles array (this simulates picking up the top tile)
     * and it will be given to the current player
     * returns the toString method of the tile so that we can print what we picked
     */
    public String getTopTile() {
        //DONE
        players[currentPlayerIndex].addTile(tiles[indexOfLastTile]);
        indexOfLastTile++;
        return tiles[indexOfLastTile - 1].toString();
    }

    /*
     * TODO: should randomly shuffle the tiles array before game starts
     */
    //DONE, turns tiles array into an ArrayList of tiles, uses Collections class to shuffle and reassigns shuffled values to the tiles array
    public void shuffleTiles() {
        ArrayList<Tile> tilesArrayList = new ArrayList<Tile>();

        for (int i = 0; i < tiles.length; i++) {
            tilesArrayList.add(tiles[i]);
        }

        Collections.shuffle(tilesArrayList);

        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = tilesArrayList.get(i);
        }
    }

    /*
     * TODO: check if game still continues, should return true if current player
     * finished the game. use checkWinning method of the player class to determine
     */

     //DONE, checks if the current player wins or if there are still enough tiles to play with
    public boolean didGameFinish() {
        boolean isFinished = false;

        if (players[currentPlayerIndex].checkWinning() || !hasMoreTileInStack()) {
            isFinished = true;
        }

        return isFinished;
    }

    /* TODO: finds the player who has the highest number for the longest chain
     * if multiple players have the same length may return multiple players
     */
    
    //Additional Method: finds the longest chain of the given tile list with length 15
     public int longestChainLength(Tile tiles[])
    {
        int counter = 1;
        int longestChain = 1;
        //ğ changed 14 to 13 to escape null pointer
        for(int i = 0; i<13; i++)
        {
            if(tiles[i].getValue() + 1 == tiles[i+1].getValue())
            {
                counter++;
            }
            else if(tiles[i].getValue() != tiles[i+1].getValue())
            {
                longestChain = Math.max(longestChain,counter);
                counter = 1;
            }
        }

        return longestChain;
    }
    //Done: Length of the longest chain among the longest chains of players is found, 
    //then ones that have the longest are added to the winners list
     public Player[] getPlayerWithHighestLongestChain() {
        Player winners[];
        ArrayList<Integer> bests = new ArrayList<Integer>();
        int longestChain=0;
        
        for(int i = 0; i<4; i++)
        {
            longestChain = Math.max(longestChain, longestChainLength(players[i].getTiles()));
        }


        for(int i = 0; i<4; i++)
        {
            if(longestChainLength(players[i].getTiles()) == longestChain)
            {
                bests.add(i);
            }
        }

        winners = new Player[bests.size()];
        for(int i = 0;i<winners.length;i++)
        {
            winners[i] = players[bests.get(i)];
        }

        return winners;
    }
    
    /*
     * checks if there are more tiles on the stack to continue the game
     */
    //ü starts counting from 0 to 103 instead of reverse
    public boolean hasMoreTileInStack() {
        return indexOfLastTile != 103;
    }

    /*
     * TODO: pick a tile for the current computer player using one of the following:
     * - picking from the tiles array using getTopTile()
     * - picking from the lastDiscardedTile using getLastDiscardedTile()
     * you should check if getting the discarded tile is useful for the computer
     * by checking if it increases the longest chain length, if not get the top tile
     */

     /*
      * Done: Picking tile for the computer
      */
    public void pickTileForComputer() {

        //adding the discardedTile to players tile array in ascended order

        Tile copy[] = new Tile[15];
        Player currentPlayer = players[getCurrentPlayerIndex()];
        boolean check = true;
        //changed 15 to 14 to escape null pointer
        for(int i = 0; i<14; i++)
        {
            if(check && lastDiscardedTile.getValue()<currentPlayer.getTiles()[i].getValue())
            {
                copy[i] =  lastDiscardedTile;
                check = false;
            }
            else if(check)
            {
                copy[i] = currentPlayer.getTiles()[i];
            }
            else
            {
                copy[i] = currentPlayer.getTiles()[i-1];
            }
        }
        if(check)
        {
            copy[14] = lastDiscardedTile;
        }

        //now checks whether length of the longest chain increased or not
        if(longestChainLength(copy)>longestChainLength(currentPlayer.getTiles()))
        {
            getLastDiscardedTile();
        }
        else
        {
            getTopTile();
        }
    }

    /*
     * TODO: Current computer player will discard the least useful tile.
     * you may choose based on how useful each tile is
     */

     /*
      * Done: Implementation is not optimal but efficient. 
      * It looks at the first and last chain of tiles. 
      * If the first chain is shorter, first element is deleted. 
      * Otherwise, last element is deleted.
      */
    public void discardTileForComputer() {

        for(int i = 0; i<14; i++)
        {
            if(players[getCurrentPlayerIndex()].getTiles()[i+1].getValue()== players[getCurrentPlayerIndex()].getTiles()[i].getValue())
            {
                discardTile(i);
                return;
            }
        }
        
        int leftCounter = 1;
        int rightCounter = 1;

        for(int i = 0; i<14; i++)
        {
            if(players[getCurrentPlayerIndex()].getTiles()[i+1].getValue() == players[getCurrentPlayerIndex()].getTiles()[i].getValue()+1)
            {
                leftCounter ++;
            }
            else
            {
                break;
            }

        }
        for(int i = 13; i>=0; i--)
        {
            if(players[getCurrentPlayerIndex()].getTiles()[i+1].getValue() == players[getCurrentPlayerIndex()].getTiles()[i].getValue()+1)
            {
                rightCounter ++;
            }
            else
            {
                break;
            }
        }

        if(leftCounter<rightCounter)
        {
            discardTile(0);
        }
        else
        {
            discardTile(14);
        }
    }

    /*
     * TODO: discards the current player's tile at given index
     * this should set lastDiscardedTile variable and remove that tile from
     * that player's tiles
     */

     /*
      * Done: Discarded tile is deleted 
      * by shifting all elements to left by 1 
      * and making the last element null
      */
    public void discardTile(int tileIndex) {
        Player currentPlayer = players[getCurrentPlayerIndex()];
        lastDiscardedTile = currentPlayer.getTiles()[tileIndex];
        for(int i = tileIndex; i<14; i++)
        {
            currentPlayer.getTiles()[i] = currentPlayer.getTiles()[i+1];
        }

        players[getCurrentPlayerIndex()].getTiles()[14] = null;
        currentPlayer.decreaseNumberOfTiles();
    }

    public void displayDiscardInformation() {
        if(lastDiscardedTile != null) {
            System.out.println("Last Discarded: " + lastDiscardedTile.toString());
        }
    }

    public void displayCurrentPlayersTiles() {
        players[currentPlayerIndex].displayTiles();
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

      public String getCurrentPlayerName() {
        return players[currentPlayerIndex].getName();
    }

    public void passTurnToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
    }

    public void setPlayerName(int index, String name) {
        if(index >= 0 && index <= 3) {
            players[index] = new Player(name);
        }
    }

}
