package alessandrosalerno.gendarme;

public interface GendarmeInvokable<UserType extends GendarmeUser> {
    GendarmeResponse invoke(UserType commandIssuer, Object... args);
    boolean accepts(Object[] args);
    String getDescription();
    Class<?>[] getParameterTypes();
}
