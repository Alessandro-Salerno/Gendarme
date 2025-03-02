package alessandrosalerno.gendarme.exceptions;

public class GendarmeAccessRestrictedException extends GendarmeException {
    public GendarmeAccessRestrictedException(String commandName) {
        super("Command \"" + commandName + "\" requires user authentication");
    }
}
