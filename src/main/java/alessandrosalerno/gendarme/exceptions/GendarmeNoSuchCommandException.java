package alessandrosalerno.gendarme.exceptions;

public class GendarmeNoSuchCommandException extends GendarmeException {
    public GendarmeNoSuchCommandException(String commandName) {
        super("No such command: " + commandName);
    }
}
