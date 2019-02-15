package game.devilnhuman.app.exception;

/**
 * Base Exception class to handle exceptions in application.
 *
 */

public class GameException extends RuntimeException {

	private static final long serialVersionUID = 9118167282278075805L;

	public GameException(String message) {
		super(message);
	}
}
