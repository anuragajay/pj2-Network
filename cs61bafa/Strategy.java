package cs61bafa;
import player.Player;
import player.Move;
import java.lang.Math.*;
import cs61bafa.list.*;

public class Strategy {
    
    public final static int BLACK = Board.BLACK;
    public final static int WHITE = Board.WHITE;
    public final static int WHITEWIN = Integer.MAX_VALUE;
    public final static int BLACKWIN = Integer.MIN_VALUE;    
    private MachinePlayer keyplayer;

    /** 
    * Strategy(MachinePlayer player) is the constructor for strategy
    * @param player: the MachinePlayer which uses the strategy
    * @return a newly constructed
    **/
    public Strategy(MachinePlayer player) {
    	keyplayer = player;
    }

    /** 
    * switchPlayer() takes in color of player and returns the color of player's opponent
    * @param player: color of player given as input
    * @return the color of player's opponent
    **/
	private static int switchPlayer (int player) {
    	return Math.abs(1 - player);
    }

	/** 
	* chooseMoveMinMax(Board board, int player, int searchDepth, int alpha, int beta) : Overloaded minmax algorithm 
	* with alpha-beta pruning makes use of Game-Search trees to find the best move player can select
	* @param board: board on which game is being played
	* @param player: color of the player performing the current move
	* @param searchDepth: the maximum search depth till which the method seraches the game tree
	* @param alpha: best already explored option along path to root for maximizer
	* @param beta: best already explored option along path to root for minimizer
	* @throws InvalidNodeException whenever InvalidNodeException is thrown by the helper functions
	* @return the best move the player performing the move can choose
	**/
	public Best chooseMoveMinMax(Board board, int player, int searchDepth, int alpha, int beta) throws InvalidNodeException {
		int score;
		Best myBest = new Best();
		Move[] lst = board.listOfValidMoves(player);
		Move tmpMove = new Move();
		if (lst.length > 0) {
			myBest.move = lst[0];
			tmpMove = lst[0];
		}
		
		Best reply;

		if (keyplayer.hasNetwork(board,BLACK)) {
			if (keyplayer.color() == WHITE)
				return new Best(tmpMove, BLACKWIN);
			return new Best(tmpMove, WHITEWIN);
		}
		if (keyplayer.hasNetwork(board,WHITE)) {
			if (keyplayer.color() == WHITE)
				return new Best(tmpMove, WHITEWIN);
			return new Best(tmpMove, BLACKWIN);
		}
		if (searchDepth == 0) {
			score = (int) keyplayer.evaluateBoard(board);
			return new Best(new Move(), score);
		}

		if (player == keyplayer.color()) {
			myBest.score = alpha;
		} else {
			myBest.score = beta;
		}

		for (Move m : lst) {
			Board tmp = board.copy();
			tmp.performMove(player, m);	
			reply = chooseMoveMinMax(tmp, switchPlayer(player), searchDepth-1, alpha, beta);		

			if ((player == keyplayer.color()) && (reply.score > myBest.score)) {
				myBest.move = m;
				myBest.score = reply.score;
				alpha = reply.score;
		
			} else if ((player == keyplayer.opponentColor()) && (reply.score < myBest.score)) {
				myBest.move = m;
				myBest.score = reply.score;
				beta = reply.score;
			}
		
			if (alpha >= beta) {
				return myBest; 
				}
			}
		return myBest;

	}

}