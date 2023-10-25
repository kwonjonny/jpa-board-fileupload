package board.jpa.querydsl.exception.errorcode;

public enum LikeErroreMessage {
    DATA_NOT_FOUND("E001", "이메일, 게시물 번호는 필수 사항입니다.");

    private final String code;
    private final String message;

    LikeErroreMessage(String code, String message) {
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
