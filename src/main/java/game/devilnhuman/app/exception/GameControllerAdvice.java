package game.devilnhuman.app.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GameControllerAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(GameControllerAdvice.class);
	
	@ExceptionHandler(InvalidMoveException.class)
	public ResponseEntity<ErrorDetails> handleInvalidMoveException(InvalidMoveException imex) {
		LOGGER.error("Invalid Move ocurred: {} {}" , imex.getMessage(), imex.getCause());
		ErrorDetails details = new ErrorDetails("Move", imex.getMessage());
		return new ResponseEntity<>(details,HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(ValidatationException.class)
	public ResponseEntity<ErrorDetails> handleValidatationException(ValidatationException vex) {
		LOGGER.error("Invalid Request recieved: {} {}" , vex.getMessage(), vex.getCause());
		ErrorDetails details = new ErrorDetails("Request", vex.getMessage());
		return new ResponseEntity<>(details,HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(GameException.class)
	public ResponseEntity<ErrorDetails> handleInvalidMoveException(GameException gex) {
		LOGGER.error("Error ocurred while processing request: {} {}" , gex.getMessage(), gex.getCause());
		ErrorDetails details = new ErrorDetails("Server", gex.getMessage());
		return new ResponseEntity<>(details,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> handleException(Exception ex) {
		LOGGER.error("Generic error ocurred: {} {}" , ex.getMessage(), ex.getCause());
		ErrorDetails details = new ErrorDetails("Input Data", ex.getMessage());
		return new ResponseEntity<>(details,HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
