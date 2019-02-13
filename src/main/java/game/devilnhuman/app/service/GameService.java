package game.devilnhuman.app.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import game.devilnhuman.app.common.Direction;
import game.devilnhuman.app.exception.InvalidMoveException;
import game.devilnhuman.app.exception.ValidatationException;
import game.devilnhuman.app.vo.GameBoard;

@Service
public class GameService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);
	
	public GameBoard createGame() {
		GameBoard gameBoard = new GameBoard(3);
		LOGGER.debug("Response: \n[{}]", gameBoard);
		return gameBoard;
	}
	
	
	
	public GameBoard move(int humans, int devils, Direction direction, GameBoard gameBoard) {
		LOGGER.debug("Request: \n[{}]\nhumans[{}]\ndevils[{}]\ndirection[{}]", new Object[]{gameBoard,humans,devils,direction});
		if(validateMove(humans, devils, direction, gameBoard)){
			gameBoard.setHumansOnLeft(gameBoard.getHumansOnLeft() - humans);
			gameBoard.setDevilsOnLeft(gameBoard.getDevilsOnLeft() - devils);
			gameBoard.setHumansOnRight(gameBoard.getHumansOnRight() + humans);
			gameBoard.setDevilsOnRight(gameBoard.getDevilsOnRight() + devils);
			gameBoard.setDirection(direction);
		}
		LOGGER.debug("Response: \n[{}]", gameBoard);
		return gameBoard;
	}

	private boolean validateMove(int humans, int devils, Direction direction, GameBoard gameBoard) {
		
		if(null == gameBoard)
			throw new ValidatationException("Current GameBoard should exists");
		
		if(humans > gameBoard.getHumansOnLeft() || devils > gameBoard.getDevilsOnLeft())
			throw new ValidatationException("Invalid Human/Devils count for boating");
		
		if(direction.equals(gameBoard.getDirection()))
			throw new InvalidMoveException("Incorrect Boat direction");
		
		if(2 < humans || 2 < devils || 2 < ( humans + devils))
			throw new InvalidMoveException("Only 2 member allowed on boat");
		
		if( Direction.RIGHT.equals(direction) && 
				(humans + gameBoard.getHumansOnRight()) < (devils + gameBoard.getDevilsOnRight()))
			throw new InvalidMoveException("Devils will eat human on right side");
		
		if( Direction.LEFT.equals(direction) && 
				(humans + gameBoard.getHumansOnLeft()) < (devils + gameBoard.getDevilsOnLeft()))
			throw new InvalidMoveException("Devils will eat human on left side");
		
		return true;
	}
}
