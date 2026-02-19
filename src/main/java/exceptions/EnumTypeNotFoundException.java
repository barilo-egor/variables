package exceptions;

public class EnumTypeNotFoundException extends BaseException {
    public EnumTypeNotFoundException() {
    }

    public EnumTypeNotFoundException(String message) {
        super(message);
    }
}