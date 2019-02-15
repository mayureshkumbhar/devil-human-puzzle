package game.devilnhuman.app.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This class represents error response to be send as a part of http response
 * when exception occurs.
 *
 */

@Data
@AllArgsConstructor
public class ErrorDetails {
	private String source;
	private String detailsMessage;
}
