package board.jpa.querydsl.exception.errorcode;

public enum ReplyErrorMessage {
     
    DATA_NOT_FOUND("E001", "작성자, 제목, 내용은 필수 사항입니다."),
    BOARD_NUMBER_NOT_FOUND("E002", "해당하는 게시물의 번호가 없습니다. %s"),
    REPLY_NUMBER_NOT_FOUND("E003", "해당하는 댓글의 번호가 없습니다. %s"),
    MEMBER_EMAIL_NOT_FOUND("E004", "해당하는 이메일의 회원이 없습니다.");

    private final String code;
    private final String message;

    ReplyErrorMessage(String code, String message) {
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
