package game.devilnhuman.app.vo;

import game.devilnhuman.app.common.Direction;
import game.devilnhuman.app.common.Status;
import lombok.Data;

/**
 * Value object to represent game board.
 *
 */
@Data
public class GameBoard {

	private int humansOnLeft;
	private int devilsOnLeft;
	private int humansOnRight;
	private int devilsOnRight;
	private Direction direction;
	private Status staus;

	public GameBoard() {
		this.humansOnLeft = 3;
		this.devilsOnLeft = 3;
		this.humansOnRight = 0;
		this.devilsOnRight = 0;
		this.direction = Direction.LEFT;
		this.staus = Status.NEW;
	}

	public boolean validate() {
		if (3 < (this.humansOnLeft + this.humansOnRight) || 3 < (this.devilsOnLeft + this.devilsOnRight))
			return false;
		return true;
	}
}
