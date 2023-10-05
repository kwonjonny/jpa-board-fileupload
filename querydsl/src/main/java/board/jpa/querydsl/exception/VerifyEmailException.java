package board.jpa.querydsl.exception;

public class VerifyEmailException extends RuntimeException {
    public VerifyEmailException() {
        super();
    }
    
    public VerifyEmailException(String message) {
        super(message);
    }
}
