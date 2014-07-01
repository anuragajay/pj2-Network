package cs61bafa;
import player.Player;
import player.Move;
import java.util.Random;
import java.util.Arrays;

import cs61bafa.list.*;

public class MachinePlayer extends Player {
private static int[][] TOP = {{1, 0}, {2, 0}, {3, 0}, {4, 0}, {5, 0}, {6, 0}};
private static int[][] BOTTOM = {{1, 7}, {2, 7}, {3, 7}, {4, 7}, {5, 7}, {6, 7}};
private static int[][] LEFT = {{0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}};
private static int[][] RIGHT= {{7, 1}, {7, 2}, {7, 3}, {7, 4}, {7, 5}, {7, 6}};
private static int[][] ALL_DIR = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};
private static int horizontalDirection = 0;
private static int verticalDirection = 1;
private static int DFDirection = 2;
private static int DBDirection = 3;
private static int NONE = 4;


  private final int DEPTH = 2;
  
    protected int color;
    protected int searchDepth;
    protected Board gameBoard;

  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).
  public MachinePlayer(int color) {
      this.color = color;
      this.searchDepth = DEPTH;
      gameBoard = new Board();
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).
  public MachinePlayer(int color, int searchDepth) {
      this.color = color;
      this.searchDepth = searchDepth;
      gameBoard = new Board();
  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove() {
	  try {
	  	Strategy strategy = new Strategy(this);
	  	Best best = strategy.chooseMoveMinMax(gameBoard, color, searchDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
	  	gameBoard.performMove(color, best.move);
	  	return best.move;
	  } catch (InvalidNodeException e) {
		return new Move();
	 }
  } 

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {
    if (gameBoard.isValidMove(m, opponentColor(color))) {
    	return gameBoard.performMove(opponentColor(color), m);
  	}
  	return false;
  }
   
  
  /**
  * If the Move m is legal, records the move as a move by "this" player
  * (updates the internal game board) and returns true.  If the move is
  * illegal, returns false without modifying the internal state of "this"
  * player.  This method is used to help set up "Network problems" for your
  * player to solve.
  */
  public boolean forceMove(Move m) {
    if (gameBoard.isValidMove(m, color)) {
    	return gameBoard.performMove(color, m);
  	}
  	return false;
  }
  
  /**
  * int[][] startingGoal(int x, int y) returns the goal at which the particular player starts playing from
  * @param x: x coordinate of the starting point
  * @param y: y coordinate of the starting point
  * @return a 2D integer array which is the goal at which the current player starts
  **/
  private int[][] startingGoal(int x, int y) {
		for (int i=0; i<LEFT.length; i++) 
			if (x == LEFT[i][0] && y == LEFT[i][1])
				return LEFT;

		for (int i=0; i<RIGHT.length; i++) 
			if (x == RIGHT[i][0] && y == RIGHT[i][1]) 
				return RIGHT;

		for (int i=0; i<TOP.length; i++) 
			if (x == TOP[i][0] && y == TOP[i][1]) 
				return TOP;

		for (int i=0; i<BOTTOM.length; i++) {
			if (x == BOTTOM[i][0] && y == BOTTOM[i][1]) {
				return BOTTOM;
      }
    }
		return null;
  }

  /** 
   * eqArrays() checks the equality of 2D arrays
   * function to return the opponent's color, given player's color
   * @param arr1 which is the first array
   * @param arr2, the second array
   * @return true if the two arrays contain the same items. Else it returns false
   */
  private boolean eqArrays(int[][] arr1, int[][] arr2) {
  	if (arr1.length == arr2.length) {
  		for(int i=0; i<arr1.length; i++) {
  			if (!Arrays.equals(arr1[i], arr2[i])) {
  				return false;
  			}
  		}
  		return true;
  	}
  	return false;
  }

  /**
  * reachedEnd(int[][] startGoal, int x, int y) returns (x,y) is in EndGoal if starting goal is the
  * given startGoal.
  * @param startGoal: 2D integer array which is the start goal
  * @param x: x coordinate of the starting point
  * @param y: y coordinate of the starting point
  * @return true if x,y) is in EndGoal if starting goal is the given startGoal. Else, it returns false.
  **/
  private boolean reachedEnd(int[][] startGoal, int x, int y) {
			int[][] endGoal;
			
			if (eqArrays(startGoal, LEFT)) {
				endGoal=RIGHT;
			} else if (eqArrays(startGoal, RIGHT)) {
				endGoal = LEFT;
			} else if (eqArrays(startGoal, TOP)) {
				endGoal = BOTTOM;
			} else {
				endGoal = TOP;
			}
			for (int i=0; i<endGoal.length; i++) 
					if (x == endGoal[i][0] && y == endGoal[i][1]) 
							return true;
					return false;
}

/**
* traverseSearch(DList prev, int x, int y) method
*  Sees the DList of valid co-ordinates, and searches for a particular x and y co-ordidate
*  in the DList.
*  @param prev gives the DList of existng co-ordinates.
*  @param x, y gives the co-ordinates that are being searched for.
*  @throws InvalidNodeException if any of the helper function throws InvalidNodeException 
*  @return true if (x,y) is in prev
*/
 private boolean traverseSearch(DList prev, int x, int y) throws InvalidNodeException
 {
			DListNode temp = (DListNode) prev.front();
				while (temp != null && temp.item() != null) 
						{
					if (x == ((int[]) temp.item())[0] && y == ((int[]) temp.item())[1]) 
								return true;
					temp = (DListNode) temp.next();
						}
			return false;
  }

  /**
   * Returns a list of all the connections from x, y 
   * @param int x - x coordinate on board
   * @param int y - y coordinate on board
   * @param Board board - the current gameboard
   * @return a list of all the connections from x, y
   */
 public DList hasConnection(int x, int y, Board board)
 {
		DList connList = new DList();
		for (int i=0; i<ALL_DIR.length; i++) 
			{
				int[] connection = hasConnectionhelper(x, y, ALL_DIR[i], board);
				if (connection != null) 
					connList.insertFront(connection);
			}
		return connList;
  }
  
   /**
   * Returns the correct pre-defined constant 
   * defining the condition satisfied by x, y
   * @param int x - x coordinate on board
   * @param int y - y coordinate on board
   * @param Board board - the current gameboard
   * @return a list of all the connections from x, y
   */
   private int[] hasConnectionhelper(int x, int y, int[] coods, Board board) 
  {
		int color = board.getElement(x, y);
		x += coods[0];
		y += coods[1];
		while (board.getElement(x, y) != color) 
			{	// Changing the last condition from board.getElement(x, y) != board.inBoard(x,y) to !board.inBoard(x,y)
				if (board.getElement(x, y) == opponentColor(color) || !board.inBoard(x,y)) 
						return null;
				x += coods[0];
				y += coods[1];
			}
		return new int[] {x, y};

  }

  /**
   * The evaluateBoard method
   * this method's main purpose is to tell the player what the best move 
   * to make is once search depth has been reached and minimax+alpha-beta has 
   * been applied, based on the score that it provides.
   * Conditions that this method checks:
   *  1)If opponent has a network, then score is automatically -100.
   *  2)Makes sure that you dont crowd around either of your goals, since
   *    the opponent chips cannot even be placed in those.
   *  3)Works on giving almost double the score based on the number of 
   *    connections that a particular chip could add.
   * Also uses a helper method that checks if the movement is in the right 
   *  direction.
   * @param board takes in the final board of the leaf to give score
   * @return a score for current state of Board which of type double
  */
  public double evaluateBoard(Board board) throws InvalidNodeException
  {
    double score = 0, playerConn = 0, opponentConn = 0, counter=0;
      //check1
      if(hasNetwork(board, opponentColor(color))) 
        return -100;
      
      if (board.numberOfChips()>2)
      {
      for(int i=0; i<Board.DIMENSION; i++) {
        for(int j=0; j<Board.DIMENSION; j++) {
          int currColor = board.getElement(i, j);
          //check2-->First end check
          if(currColor != Board.EMPTY) {
            int[] currPos = {i,j};
            if(board.numberOfChips()==20) {
              for (int[] element : LEFT) {
                if (board.getElement(element[0],element[1])==Board.EMPTY)
                      playerConn-=10;
                  }
              for (int[] element : RIGHT) {
                if (board.getElement(element[0],element[1])!=Board.EMPTY) 
                      counter++;
                  }
                if (counter>=2)
                  playerConn-=5;
                else if (counter<=1)
                  playerConn+=7;
              }

            //second end check 
            if (currPos[0]==0)
            {   for(int k = 0;k<8;k++)
                  if (board.getElement(0,k)!=Board.EMPTY)
                      playerConn-=2;
              }

            int connectionsNumber = connNumber(currPos,board);
            if(currColor == color) {
              if(towardsGoal(color, currPos)) 
                playerConn += connectionsNumber*2;
              else
                playerConn += connectionsNumber*1.5;
            }
            else if (currColor== opponentColor(color)) {
              if(towardsGoal(opponentColor(color), currPos)) 
                opponentConn += connectionsNumber*2;
              else 
                opponentConn += connectionsNumber*1.5;
            }      
          }
        }
      }
      score = playerConn - opponentConn;
      }
    return score;
  }

  /**
  * towardsGoal(int color, int[] coord) checks whether the coordinate coord is in goal of the
  * player with the given color.
  * @param color: color of the player
  * @param coord: array representing the coordinate
  * @return true if coordinate coord is in goal of the player with the given color. Else, it 
  * returns false.
  **/
  protected static boolean towardsGoal(int color, int[] coord) {
    if(color == Board.BLACK) {
      if(coord[1] == 0 || coord[1] == 7) {
        return true;
      }
    }
    if(color == Board.WHITE) {
      if(coord[0]== 0 || coord[0] == 7) {
        return true;
      }
    }
    return false;
  }

  /**
  * connNumber(int[] array, Board board) returns the number of connection from the chip whose 
  * coordinates are contained in the given array
  * @param array: 2D integer array which contains coordinate of the chip
  * @param board: Board object which shows the current state of the game
  * @return the length of number of connections
  **/
  private int connNumber(int[] array, Board board)
  	{
    return hasConnection(array[0], array[1], board).length();
  	}

  /**
   * hasNetwork() detects whether a valid network can be found.
   *  starts at a certain goal area, if there is a chip of the color
   *  in the goal, check if a network can be found from that point.
   *  If not, return false directly
   *  @param board, the current game board
   *  @param color, WHITE or BLACK
   *  @return true or false, ie whether a network is found or not
   */
  public boolean hasNetwork(Board board, int color) {
  	switch(color) {
  		case Board.WHITE:
  			for (int[] coods: LEFT) {
  				if ((board.getElement(coods[0], coods[1]) == color)&&(possiblePointNetwork(board,coods[0],coods[1],1,NONE, LEFT, new DList()))) {
  					return true;
  				}
  			}
  			return false;
  		case Board.BLACK:
  			for (int[] coods: TOP) {
  				if ((board.getElement(coods[0], coods[1]) == color)&&(possiblePointNetwork(board, coods[0],coods[1],1,NONE,TOP, new DList()))) {
  					return true;
  				}  				
  			}
  			return false;
  		 default:
  		 	return false;	
    }
  }

  /**
  * possiblePointNetwork method
  *
  * 1)Initially check if the the number of points in the connection is less than , equal to or greater 
  * than 6 and quit if lesser than 6.
  * 2)We search if the node we are looking for is in the previous list of moves
  * 3)We check that the direction keeps changing while evaluating networks so that our connection does not
  * go along a straight line.
  * 4)Then we check if we are finding the possible networks in the directions of our end goal to validate 
  * possible networks.
  * @param board evaluates the current given board at a gven point
  * @param x,y checks the network from a given set of o-ordinates
  * @param startGoal gives the entire range of starting goal co-ordinates
  * @param points gives the number of points to which a particular x,y co-ordinates connect to.
  * @param direction gives the number(i.e the direction) refer to top of machinePlayer for
  *  a better view.
  * @param prev gives the list of previous moves that have been made.
  *
  */
  private boolean possiblePointNetwork(Board board, int x, int y,int points, int direction, int[][] startGoal, DList previousConnections) {
		try{
    if (reachedEnd(startGoal,x,y)) {
			if (points >= 6) {
				return true;
      }
			else { 
				previousConnections.removeFront();
				return false;
			}
		} else {
			DListNode connList = (DListNode) hasConnection(x, y, board).front();
			while (connList != null && connList.item() != null) {
				int newX = ((int[]) connList.item())[0];
				int newY = ((int[]) connList.item())[1];
				if (!traverseSearch(previousConnections, newX, newY) && (direction != direction(x,y, newX, newY)) && !rightGoal(x,y, newX, newY)) 
        {
					previousConnections.insertFront(new int[] {x,y});
					if (possiblePointNetwork(board, newX, newY, points+1, direction(x,y, newX, newY), startGoal,previousConnections)) { 
						return true;
          }
				}
				connList = (DListNode) connList.next();
			}
		}
		previousConnections.removeFront();
		return false;
    } catch (InvalidNodeException e) {
      return false;
    }
  }

  /**
   * function to return the player's color
   * @return player color
   */    
  protected int color() {
  	return color;
  }

  /**
   * function to return the opponent's color
   * @return opposing color
   */
  protected int opponentColor() {
  	return opponentColor(color);
  }

  /**
   * function to return the opponent's color, given player's color
   * @param color which is the color of player
   * @return opponent color
   */
  protected static int opponentColor(int color) 
  {
	  if(color == Board.WHITE) 
		  return Board.BLACK;
	return Board.WHITE;
	  
  }

  /**
   * Returns the correct pre-defined constant defining
   * which direction the network points
   * defining the condition satisfied by x, y
   * @param x1, the first x coordinate
   * @param y1, the first y coordinate
   * @param x2, the second x coordinate
   * @param y2, the second y coordinate
   * @return the constant that is represented by either
   * HORIZONTAL, VERTICAL, DIAGONAL_BACK or DIAGONAL_FORWARD
   */
  private int direction(int x1, int y1, int x2, int y2) 
  {
		if (x1 == x2) 
			return verticalDirection;
		else if (y1 == y2) 
			return horizontalDirection;
		else 
			if ((y2-y1) / (x2-x1) < 0) 
				return DBDirection;
		else 
			return DFDirection;
  }

  /**
  * rightGoal(int x1, int y1, int x2, int y2) checks if both co-ordinates have the same starting goal and 
  * checks if not null and returns a boolean output value based on what it finds.
  * @param x1, the first x coordinate
  * @param y1, the first y coordinate
  * @param x2, the second x coordinate
  * @param y2, the second y coordinate
  * @return true if both co-ordinates have the same starting goal. Else, it returns false.
  **/
  private boolean rightGoal(int x1, int y1, int x2, int y2) {
			if (startingGoal(x1, y1) == startingGoal(x2, y2) && startingGoal(x1, y1) != null) 
				return true;
			return false;
  }
  
  }