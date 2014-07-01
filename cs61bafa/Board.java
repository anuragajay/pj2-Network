package cs61bafa;
import player.Player;
import player.Move;

import cs61bafa.list.*;


public class Board {

//----------------------Constants for representing gameBoard and chip-------------------    
    protected final static int DIMENSION = 8;
    protected final static int BLACK = 0;
    protected final static int WHITE = 1;
    protected final static int EMPTY = 2;
    protected final static int CORNERS = 3;

//----------------------Different types of Moves-----------------------------------

    protected final static int QUIT = 0;
    protected final static int ADD = 1;
    protected final static int STEP = 2;

//----------------------Private fields for internal use-----------------------------
    
    private int blackChips; // Keeps count of number of black chips on Board initiated to 0
    private int whiteChips; // Keeps count of number of black chips on Board initiated to 0
    private int[][] gameBoard = new int[DIMENSION][DIMENSION];  // A 2D Array representing and maintaining current state of Board

    /**
     * Board() constructs an empty game Board of Dimension 8 X 8. The four corners are initialized to CORNERS which means
     * that they won't be used during the game. The rest other squares on Board are initialized to EMPTY. As the game will
     * progress, they get changed to either BLACK or WHITE. The number of Black chips and White chips is initialialized to
     * 0. These numbers would increase upto 10 as the game progresses.
     * @return Board object
     **/

    public Board() {
    	for(int i=0; i<DIMENSION; i++) {
    		for(int j=0; j<DIMENSION; j++) {
    			gameBoard[i][j] = EMPTY;
    		}
    	}
      gameBoard[0][DIMENSION-1]=CORNERS;
      gameBoard[DIMENSION-1][0]=CORNERS;
      gameBoard[0][0]=CORNERS;
      gameBoard[DIMENSION-1][DIMENSION-1]=CORNERS;
      blackChips = 0;
      whiteChips = 0;

    }
    
  /**
    * inBoard(int x,int y) returns whether the position(x,y) lies inside the Board.
    * @param x: x coordinate of the position
    * @param y: y coordinate of the position
    * @return True if position(x,y) is inside the Board. Else, it returns False.
    **/
    protected boolean inBoard(int x,int y) {
      return ((x>=0)&&(x<DIMENSION)&&(y>=0)&&(y<DIMENSION));
    }

    /** 
    * stillUnoccupied(int x, int y) checks whether the position(x,y) is occupied
    * @param x: x coordinate of the position
    * @param y: y coordinate of the position
    * @return true if position(x,y) is unoccupied. Else, it returns false.
    **/     
    private boolean stillUnoccupied(int x,int y) {
      return (getElement(x,y)==EMPTY);
    }

    /**
     * numberOfWhiteChips() returns the number of total white chips
     * @return the total number of white chips
     **/    
    public int numberOfWhiteChips(){
      return whiteChips;
    }
    
    /**
     * numberOfBlackChips() returns the number of total black chips
     * @return the total number of black chips
     **/
    public int numberOfBlackChips(){
      return blackChips;
    }

    /**
     * numberOfChips() returns the number of total chips
     * @return the total number of chips
     **/
    public int numberOfChips() {
    	return blackChips + whiteChips;
    }

    /**
     * numberOfChips(int color) returns the number of chips for that particular color
     * @param color: the color of chip whose number we want
     * @return the number of chips for the color
     **/
    public int numberOfChips(int color) {
    	if(color == BLACK) {
    		return blackChips;
    	} else if(color == WHITE){
    		return whiteChips;
    	} else {
    		return -1;
    	}
    }

    /**
     * currentBoard() returns a 2D array of DIMENSION 8 X 8 which represents the current state of Board.
     * It can be helpful in testing
     * @return the gameBoard field of current Board object.
     **/
    protected int[][] currentBoard() {
	   return gameBoard;
    }

    /**
     * getElement(int x, int y) returns the color of chip at position(x,y). If position has no chip or
     * is one of corners of the Board or is outside the Board, we get EMPTY as result. 
     * @param x: x coordinate of position whose content we want
     * @param y: y coordinate of position whose content we want
     * @return the color of chip at position(x,y). If no chip present at position(x,y), EMPTY is returned
     * If (x,y) lies outside the Board, EMPTY is returned
     **/
    protected int getElement(int x, int y) {
    	if (inBoard(x,y)) {
        return gameBoard[x][y];
      } else {
        return EMPTY;
      }
    }

    /**
     * setElement(int x, int y, int color) sets the content at (x,y) to the color, taken as input. 
     * If color is BLACK/WHITE:
     *  (1) (x,y) is empty
     *       The content of (x,y) is set as BLACK/WHITE and number of black/white chips increments by 1
     *  (2) (x,y) is BLACK/WHITE
     *       Nothing happens
     *  (3) (x,y) is WHITE/BLACK
     *      WHITE/BLACK is replaced by BLACK/WHITE at (x,y). The number of white/black chips decrements
     *      by 1 and that of black/white increments by 1.      
     * If color is EMPTY and the earlier content of (x,y) is either black or white, 
     * the number of black or white chips is decreased by 1. If color is EMPTY and the earlier content of 
     * (x,y) is also EMPTY, nothing happens. If position (x,y) lies outside the board, nothing happens.
     * @param x: x coordinate of position
     * @param y: y coordinate of position
     * @param color: color to which the position(x,y) is being set
     **/
    protected void setElement(int x, int y, int color) {
    	int current = gameBoard[x][y];
    	if(color != EMPTY && current == EMPTY) {
    		if(color == BLACK) {
    			blackChips++;
    		} else if(color == WHITE) {
    			whiteChips++;
    		}
    	} else if(color == EMPTY && current != EMPTY) {
    		if(current == BLACK) {
    			blackChips--;
    		} else if(current == WHITE) {
    			whiteChips--;
    		}
    	}
      if (inBoard(x, y)) {
    	 gameBoard[x][y] = color;
      }
      else {
       return;   
      }
    }

     /**
     * copy() clones the current game board into a new object and returns the new Object
     * This method comes handy in Strategy class which could manipulate the copy of Board and predict the best move
     * which player can choose.
     * @return the new Board object whose content is similar to the current Board 
     **/
    protected Board copy() {
      Board clone = new Board();
      for(int i=0; i<DIMENSION; i++) {
        for(int j=0; j<DIMENSION; j++) {
          clone.gameBoard[i][j] = this.getElement(i, j);
        }
      }
       clone.whiteChips = whiteChips;
       clone.blackChips = blackChips;
       return clone;
    }

    /**
    * notInOpponentGoal(int x, int y, int color) returns whether the position(x,y) is in opponent
    * goal of the player with the given color
    * @param x: x coordinate of the position
    * @param y: y coordinate of the position
    * @param color: color of the player
    * @return true if (x,y) is not in opponent goal of player having the given color. Else, it returns
    * false
    **/
    private boolean notInOpponentGoal(int x, int y, int color) {
      if (color == WHITE) {
        return ((y != 0)&&(y != DIMENSION-1));
      }
      else if (color == BLACK) {
        return ((x != 0)&&(x != DIMENSION-1));
      } else {
        return true;
      }
    }

    /**
    * listOfValidMoves(int color) gives list of all valid moves the player having the 
    * given color makes.
    * @param color: color of the player whose list of valid moves we want to calculate
    * @return list of Move objects which player with given color can make
    **/
    protected Move[] listOfValidMoves(int color) throws InvalidNodeException {  
      Move[] lst;
      int itemNo = 0;
      SList tmp = new SList();

      // Generating a list of coordinates where chips can be added
      int[][] lstcoordinates = lstPlace(color);
      
      // Adding add moves
      for (int[] coordinate : lstcoordinates) {
        Move m = new Move(coordinate[0], coordinate[1]);
        if (isValidMove(m, color)) {
          tmp.insertBack(m); 
          itemNo++;
        }
      }

      // Adding step moves
      // The list of new position will remain same as in add moves 
      for (int i=0; i<DIMENSION; i++) {
        for (int j=0; j<DIMENSION; j++) {
          if (getElement(i, j) == color) {
            for (int[] coordinate : lstcoordinates) {
              Move m = new Move(coordinate[0], coordinate[1], i, j);
              if (isValidMove(m, color)) {
                
                tmp.insertBack(m); 
                itemNo++;
              }
            }
          }
        }
      }          
      lst = new Move[itemNo];
      SListNode tmp2 = (SListNode) tmp.front();
      for (int i=0; i<itemNo; i++) {
        lst[i] = (Move) tmp2.item();
        tmp2 = (SListNode) tmp2.next();
      } 
      return lst;
    }

    /**
    * lstPlace(int color) returns coordinates of places in form of 2D integer array where chip of given 
    * color can be placed.It is a helper function for list of Valid Moves.
    * @param color: color of chip wh list of places where it can be placed is returned by the function
    * @return a 2D array of coordinates where chip of given color can be placed.
    **/
    private int[][] lstPlace(int color) {
      // Just for counting for array initialization
      int count = 0;
      for (int i=0; i<DIMENSION; i++) {
        for (int j=0; j<DIMENSION; j++) {
          if (followRules(i, j, color)) {
            count++;
          }
        }
      }

      int[][] lst = new int[count][2];
      int itemNo = 0;
      for (int i=0; i<DIMENSION; i++) {
        for (int j=0; j<DIMENSION; j++) {
          if (followRules(i, j, color)) {
            
            lst[itemNo] = new int[] {i, j};
            itemNo++;
          }
        }
      }
      return lst;
    }

    /**
    * isValidMove(Move m, int color) determines whether the Move m by the player having the given color is
    * valid.
    * @param m: Move object played by the player having the given color
    * @param color: color of the player playing the move
    * @return true if Move m by the player with given color is valid. Else, it returns false.
    **/
    protected boolean isValidMove(Move m, int color) {
        switch(m.moveKind) {
          case QUIT:
            return true;
          case ADD:
            if (numberOfChips(color) >= 10) {
              return false;
            }
            return followRules(m.x1, m.y1, color);
          case STEP:
            if (numberOfChips(color) < 10) {
              return false;
            }
            return followRules(m.x1, m.y1, color);    
        }

        return false;
    }
    
    /**
    * followRules(int x, int y, int color) checks whether the ADD/STEP move ending at (x,y) by the player
    * of given color follows all the rules of Network.
    * @param x: x coordinate of final position
    * @param y: y coordinate of final position
    * @param color: color of the player playing the move
    * @return true he ADD/STEP move ending at (x,y) by the player  of given color follows all the rules of Network
    * Else, it returns false.
    **/
    private boolean followRules(int x, int y, int color) {
      int count = 0;
      if ((notInOpponentGoal(x, y, color))&&(stillUnoccupied(x, y))) {
        Board tmp = copy();
        tmp.setElement(x, y, color);
        if (tmp.noCluster(color)) {
          return true;
        } else {
          return false;
        } 
      }
      return false;
    }

    /**
    * noCluster(int color) checks whether the Board contains cluster of chip of given color
    * @param color: color of chip
    * @return true if no cluster of chip of given player is present. Else, it returns false.
    **/
    private boolean noCluster(int color) {
      for (int i=0; i<DIMENSION; i++) {
        for (int j=0; j<DIMENSION; j++) {
          if((inBoard(i,j))&&(getElement(i,j) == color)) {
            int count = 0;
            for (int x = i-1; x<=i+1; x++) {
              for(int y = j-1; y<=j+1; y++) {
                if ((inBoard(x,y))&&(notInOpponentGoal(x,y,color))&&(getElement(x,y) == color)) {
                  count++;
                }
                if (count >= 3) {
                  return false;
                }            
              }
            }
          }
        }
      }
      return true;
    }


    /**
    * performMove(int color, Move m) performs the Move m by player of given color and returns true
    * if move was performed. Else it returns false.
    * @param color: color of the player performing the move
    * @param m: Move object being performed by the player
    * @return true if move m by the player of given color was successfully performed. Else, it returns false
    **/
    public boolean performMove(int color, Move m) {
      if (isValidMove(m, color)) {
        switch(m.moveKind) {
          case QUIT:
            return true;
          case ADD:
            setElement(m.x1, m.y1, color);
            return true;
          case STEP:
            setElement(m.x2, m.y2, EMPTY);
            setElement(m.x1, m.y1, color);
            return true;    
        }  
      } 
      return false;
    }

      /**
    * toString() returns the string representing the current state of the Board. It helps
    * in visualization of the Board. It can also be used to print Board during debugging.
    * @return string representing the current state of the Board.
    **/ 
    public String toString() {
      String string = new String();
      String horizontal = "--------------------------\n";
      for(int i=0; i<DIMENSION; i++) {
        String vertical = "| ";
        for(int j=0; j<DIMENSION; j++) {
          if(gameBoard[j][i] == BLACK) {
            vertical = vertical + " B ";
          } else if(gameBoard[j][i] == WHITE) {
            vertical = vertical + " W ";
          } else {
            vertical = vertical + " - ";
          }
        }
        vertical = vertical + "|\n";
        string = string + vertical;
      }
      string = horizontal + string + horizontal;
      return string;
    }
     
    /**
    * main(String[] arg) tests the methods of Board class
    * @param arg: Though present, arg doesn't need to be entered by the user
    **/ 
    // public static void main(String[] args) {
    //   MachinePlayer white = new MachinePlayer(Board.WHITE), black = new MachinePlayer(Board.BLACK);
    //   System.out.println(white.gameBoard);
    //   white.forceMove(new Move(3,4));
    //   System.out.println(white.gameBoard);
    //   white.forceMove(new Move(3,6));
    //   System.out.println(white.gameBoard);
    //   white.forceMove(new Move(3,7));
    //   System.out.println(white.gameBoard);
    //   white.forceMove(new Move(0,6));
    //   System.out.println(white.gameBoard);
    //   white.forceMove(new Move(4,4));
    //   System.out.println(white.gameBoard);
    //   white.forceMove(new Move(4,1));
    //   System.out.println(white.gameBoard);
    //   white.forceMove(new Move(7,2));
    //   white.forceMove(new Move(7,1));
    //   System.out.println(white.gameBoard);
    //   System.out.println(white.hasNetwork(white.gameBoard, Board.WHITE));
    // }

}
