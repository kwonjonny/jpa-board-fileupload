package board.jpa.querydsl.exception;

public class ReplyNumberNotFoundException extends RuntimeException {
    public ReplyNumberNotFoundException() {
        super();
    }

    public ReplyNumberNotFoundException(String message) {
        super(message);
    }
}
