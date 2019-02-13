package game.devilnhuman.app.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDetails {
	private String source;
	private String detailsMessage;
}
