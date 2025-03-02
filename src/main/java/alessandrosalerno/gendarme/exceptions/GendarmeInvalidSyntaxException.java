package alessandrosalerno.gendarme.exceptions;

public class GendarmeInvalidSyntaxException extends GendarmeException {
    public GendarmeInvalidSyntaxException(String error, int index) {
        super(error + " at char " + index);
    }
}
