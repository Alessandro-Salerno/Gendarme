package alessandrosalerno.gendarme;

import java.lang.reflect.Parameter;

public interface GendarmeInvokable<UserType extends GendarmeUser> {
    GendarmeResponse invoke(UserType commandIssuer, Object... args);
    boolean accepts(Object[] args);
    String getDescription();
    Parameter[] getParameters();
}