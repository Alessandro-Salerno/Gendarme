package alessandrosalerno.gendarme.exceptions;

public class GendarmePermissionDeniedException extends GendarmeException {
    public GendarmePermissionDeniedException(String commandName, String permission) {
        super("Command \"" + commandName + "\" requires permission \"" + permission + "\"");
    }
}
