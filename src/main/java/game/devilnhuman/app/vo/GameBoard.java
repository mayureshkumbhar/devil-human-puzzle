package game.devilnhuman.app.vo;

import game.devilnhuman.app.common.Direction;
import lombok.Data;

@Data
public class GameBoard {
	
	private int humansOnLeft;
	private int devilsOnLeft;
	private int humansOnRight;
	private int devilsOnRight;
	private Direction direction;
	
	public GameBoard(int count) {
		this.humansOnLeft = count;
		this.devilsOnLeft = count;
		this.humansOnRight = 0;
		this.devilsOnRight = 0;
		this.direction = Direction.LEFT;
	}
}
