package alessandrosalerno.gendarme;

import alessandrosalerno.gendarme.exceptions.GendarmeException;

import java.lang.reflect.Parameter;

public interface GendarmeInvokable<UserType extends GendarmeUser> {
    GendarmeResponse invoke(UserType commandIssuer, Object... args) throws GendarmeException;
    boolean accepts(Object[] args);
    String getDescription();
    Parameter[] getParameters();
}