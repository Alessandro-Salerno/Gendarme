package alessandrosalerno.gendarme;

public class GendarmeCommandRoot<UserType extends GendarmeUser> extends GendarmeCommandGroup<UserType> {
    public GendarmeCommandRoot(String description) {
        super(description);
    }
}
