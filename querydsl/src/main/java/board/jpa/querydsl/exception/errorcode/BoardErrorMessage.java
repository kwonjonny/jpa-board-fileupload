package board.jpa.querydsl.exception.errorcode;

public enum BoardErrorMessage {
    
    DATA_NOT_FOUND("E001", "작성자, 제목, 내용은 필수 사항입니다."),
    BOARD_NUMBER_NOT_FOUND("E002", "해당하는 게시물의 번호가 없습니다. %");

    private final String code;
    private final String message;

    BoardErrorMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getFormattedMessage(Object... args) {
        return String.format(message, args);
    }
}
