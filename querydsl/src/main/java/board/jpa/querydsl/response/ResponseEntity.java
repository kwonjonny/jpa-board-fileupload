package board.jpa.querydsl.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResponseEntity<T> {

    private int statusCode;
    private String message;
    private T data;

    public ResponseEntity(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseEntity<T> success(T data) {
        return new ResponseEntity<>(200, "Success", data);
    }

    public static <T> ResponseEntity<T> error(int statusCode, String message) {
        return new ResponseEntity<>(statusCode, message, null);
    }
}
