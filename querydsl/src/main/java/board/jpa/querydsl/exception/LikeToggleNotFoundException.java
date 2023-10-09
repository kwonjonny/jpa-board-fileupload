package board.jpa.querydsl.exception;

public class LikeToggleNotFoundException extends RuntimeException {
    public LikeToggleNotFoundException() {
        super();
    }

    public LikeToggleNotFoundException(String message) {
        super(message);
    }
}
