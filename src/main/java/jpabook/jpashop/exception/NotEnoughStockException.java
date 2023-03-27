package jpabook.jpashop.exception;

public class NotEnoughStockException extends RuntimeException {
    //오버라이딩하는 이유: 메시지를 넘겨줄 때 예외가 발생한 근원적인 예외를 쭉 넣어서 표시할 수 있기 때문
    public NotEnoughStockException() {
        super();
    }

    public NotEnoughStockException(String message) {
        super(message);
    }

    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }

    protected NotEnoughStockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
