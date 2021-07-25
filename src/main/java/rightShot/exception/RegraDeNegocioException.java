package rightShot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RegraDeNegocioException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public RegraDeNegocioException() {
		super();
	}

	public RegraDeNegocioException(String exception) {
        super(exception);
    }
	
	public RegraDeNegocioException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public RegraDeNegocioException(Throwable cause) {
        super(cause);
    }
}
