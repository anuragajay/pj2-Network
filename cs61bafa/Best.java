/*Best.java*/
package cs61bafa;
import player.Player;
import player.Move;

/** 
* represents Best move of a player and its score in Strategy
**/

public class Best {

//-------------------------Protected fields for use in Best and Strategy class--------------------
	protected Move move; // Best move by the player
	protected int score; // Score of the best move above

//------------------------------------------------------------------------------------------------

	/**
	* Best(Move move, int score) creates a Best object with given move and score
	* @param move: Move object which is best move of the player
	* @param score : score of the Best move
	* @return Best object
	**/
	protected Best(Move move, int score) {
		this.move = move;
		this.score = score;
	}

	/**
	* Best(Move move, int score) creates a Best object with QUIT move and 0 score
	* @return Best object
	**/
	protected Best() {
		this.move = new Move();
		this.score = 0;   
	}
}