package game.devilnhuman.app.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import game.devilnhuman.app.common.Direction;
import game.devilnhuman.app.common.Status;
import game.devilnhuman.app.controller.GameController;
import game.devilnhuman.app.service.GameService;
import game.devilnhuman.app.vo.GameBoard;

/**
 * Class contains JUnit test cases for {@code GameController}
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = GameController.class)
public class GameControllerTest {

	
	private static final String EXPR_HUMAN_ON_LEFT = "$.humansOnLeft";
	private static final String EXPR_HUMAN_ON_RIGHT = "$.humansOnRight";
	private static final String EXPR_DEVILS_ON_LEFT = "$.devilsOnLeft";
	private static final String EXPR_DEVILS_ON_RIGHT = "$.devilsOnRight";
	private static final String EXPR_DIRECTION = "$.direction";
	private static final String EXPR_STATUS = "$.staus";
	private static final String ROUTE_NEW_GAME = "/game/new";
	private static final String ROUTE_MOVE= "/game/move";

	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Autowired
	private MockMvc mvc;

	@SpyBean
	private GameService service;

	@Test
	public void testNewGame() throws Exception {
		mvc.perform(get(ROUTE_NEW_GAME).contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath(EXPR_HUMAN_ON_LEFT, is(3)))
		.andExpect(jsonPath(EXPR_DEVILS_ON_LEFT, is(3)))
		.andExpect(jsonPath(EXPR_HUMAN_ON_RIGHT, is(0)))
		.andExpect(jsonPath(EXPR_DEVILS_ON_RIGHT, is(0)))
		.andExpect(jsonPath(EXPR_DIRECTION, is(Direction.LEFT.toString())))
		.andExpect(jsonPath(EXPR_STATUS, is(Status.NEW.toString())));
	}

	@Test
	public void testMove() throws Exception {
		mvc.perform(post(ROUTE_MOVE).param("humans", "2").param("devils", "0").param("direction", "RIGHT")
				.content(asJsonString(validGameBoard(Direction.LEFT))).contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath(EXPR_HUMAN_ON_LEFT, is(0)))
				.andExpect(jsonPath(EXPR_DEVILS_ON_LEFT, is(2)))
				.andExpect(jsonPath(EXPR_HUMAN_ON_RIGHT, is(3)))
				.andExpect(jsonPath(EXPR_DEVILS_ON_RIGHT, is(1)))
				.andExpect(jsonPath(EXPR_DIRECTION, is(Direction.RIGHT.toString())))
				.andExpect(jsonPath(EXPR_STATUS, is(Status.INPROGRESS.toString())));
	}
	
	@Test
	public void testMoveGameComplete() throws Exception {
		mvc.perform(post(ROUTE_MOVE).param("humans", "0").param("devils", "2").param("direction", "RIGHT")
				.content(asJsonString(validGameBoardGameComplete(Direction.LEFT))).contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath(EXPR_HUMAN_ON_LEFT, is(0)))
				.andExpect(jsonPath(EXPR_DEVILS_ON_LEFT, is(0)))
				.andExpect(jsonPath(EXPR_HUMAN_ON_RIGHT, is(3)))
				.andExpect(jsonPath(EXPR_DEVILS_ON_RIGHT, is(3)))
				.andExpect(jsonPath(EXPR_DIRECTION, is(Direction.RIGHT.toString())))
				.andExpect(jsonPath(EXPR_STATUS, is(Status.COMPLETED.toString())));
	}
	
	
	@Test
	public void testMoveEmptyBody() throws Exception {
		mvc.perform(post(ROUTE_MOVE).param("humans", "2").param("devils", "0").param("direction", "RIGHT")
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isInternalServerError()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.source", is("Input Data")))
				.andExpect(jsonPath("$.detailsMessage", containsString("Required request body is missing")));
	}
	
	
	@Test
	public void testMoveInvalidCurrentBoard() throws Exception {
		mvc.perform(post(ROUTE_MOVE).param("humans", "2").param("devils", "0").param("direction", "RIGHT")
				.content(asJsonString(invalidGameBoard())).contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.source", is("Request")))
				.andExpect(jsonPath("$.detailsMessage", is("Invalid current game status")));
	}
	
	@Test
	public void testMoveInvalidBoatDirection() throws Exception {
		mvc.perform(post(ROUTE_MOVE).param("humans", "2").param("devils", "0").param("direction", "LEFT")
				.content(asJsonString(validGameBoard(Direction.LEFT))).contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isConflict()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.source", is("Move")))
				.andExpect(jsonPath("$.detailsMessage", is("Incorrect Boat direction")));
	}
	
	@Test
	public void testMoveInvalidHumanCountsBoat() throws Exception {
		mvc.perform(post(ROUTE_MOVE).param("humans", "3").param("devils", "2").param("direction", "RIGHT")
				.content(asJsonString(validGameBoard(Direction.LEFT))).contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.source", is("Request")))
				.andExpect(jsonPath("$.detailsMessage", is("Invalid Human/Devils count for boating")));
	}
	
	@Test
	public void testMoveInvalidDevilCountsBoat() throws Exception {
		mvc.perform(post(ROUTE_MOVE).param("humans", "2").param("devils", "4").param("direction", "RIGHT")
				.content(asJsonString(validGameBoard(Direction.LEFT))).contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.source", is("Request")))
				.andExpect(jsonPath("$.detailsMessage", is("Invalid Human/Devils count for boating")));
	}
	
	@Test
	public void testMoveBoatHasMoreThan2() throws Exception {
		mvc.perform(post(ROUTE_MOVE).param("humans", "2").param("devils", "2").param("direction", "RIGHT")
				.content(asJsonString(validGameBoard(Direction.LEFT))).contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isConflict()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.source", is("Move")))
				.andExpect(jsonPath("$.detailsMessage", is("Only 2 member allowed on boat")));
	}
	
	@Test
	public void testMoveMoreDevilsOnRight() throws Exception {
		mvc.perform(post(ROUTE_MOVE).param("humans", "0").param("devils", "2").param("direction", "RIGHT")
				.content(asJsonString(validGameBoard(Direction.LEFT))).contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isConflict()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.source", is("Move")))
				.andExpect(jsonPath("$.detailsMessage", is("Devils will eat human on right side")));
	}
	
	@Test
	public void testMoveMoreDevilsOnLeft() throws Exception {
		mvc.perform(post(ROUTE_MOVE).param("humans", "0").param("devils", "2").param("direction", "LEFT")
				.content(asJsonString(validGameBoard(Direction.RIGHT))).contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isConflict()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.source", is("Move")))
				.andExpect(jsonPath("$.detailsMessage", is("Devils will eat human on left side")));
	}
	
	private GameBoard validGameBoard(Direction direction) {
		GameBoard board = new GameBoard();
		board.setHumansOnLeft(2);
		board.setDevilsOnLeft(2);
		board.setHumansOnRight(1);
		board.setDevilsOnRight(1);
		board.setDirection(direction);
		board.setStaus(Status.INPROGRESS);
		return board;
	}

	private GameBoard validGameBoardGameComplete(Direction direction) {
		GameBoard board = new GameBoard();
		board.setHumansOnLeft(0);
		board.setDevilsOnLeft(2);
		board.setHumansOnRight(3);
		board.setDevilsOnRight(1);
		board.setDirection(direction);
		board.setStaus(Status.INPROGRESS);
		return board;
	}
	
	private GameBoard invalidGameBoard() {
		GameBoard board = new GameBoard();
		board.setHumansOnLeft(2);
		board.setDevilsOnLeft(2);
		board.setHumansOnRight(2);
		board.setDevilsOnRight(2);
		board.setDirection(Direction.LEFT);
		board.setStaus(Status.INPROGRESS);
		return board;
	}
	
	private String asJsonString(GameBoard board) throws JsonProcessingException {
		return MAPPER.writeValueAsString(board);
	}
}
