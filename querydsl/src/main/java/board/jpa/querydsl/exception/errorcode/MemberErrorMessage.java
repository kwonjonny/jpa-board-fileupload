package board.jpa.querydsl.exception.errorcode;

public enum MemberErrorMessage {

    DATA_NOT_FOUND("E001", "이메일, 회원 이름, 회원 전화번호, 회원 패스워드는 필수 사항입니다."),
    DUPLICATE_EMAIL("E002", "이미 회원가입된 이메일입니다."),
    MEMBER_NOT_FOUND("E003", "해당하는 이메일의 회원이 없습니다. %s");

    private final String code;
    private final String message;

    MemberErrorMessage(String code, String message) {
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
