package game.devilnhuman.app.exception;

/**
 * Exception class to handle invalid move actions in application.
 *
 */

public class InvalidMoveException extends GameException {

	private static final long serialVersionUID = 2535874005692488954L;

	public InvalidMoveException(String errorMessage) {
		super(errorMessage);
	}
}
