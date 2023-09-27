package board.jpa.querydsl.exception;

public class BoardNumberNotFoundException extends RuntimeException {
    public BoardNumberNotFoundException() {
        super();
    }

    public BoardNumberNotFoundException(String message) {
        super(message);
    }
}
