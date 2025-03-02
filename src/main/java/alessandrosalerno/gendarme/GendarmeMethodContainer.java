package alessandrosalerno.gendarme;

import alessandrosalerno.gendarme.exceptions.GendarmeAccessRestrictedException;
import alessandrosalerno.gendarme.exceptions.GendarmePermissionDeniedException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class GendarmeMethodContainer<UserType extends GendarmeUser> implements GendarmeInvokable<UserType> {
    private final Method method;
    private final Object parent;
    private final Class<?>[] parameterTypes;
    private boolean requiresAuthentication;
    private String requiresPermission;
    private String description;

    public GendarmeMethodContainer(Method method, Object parent) {
        this.method = method;
        this.parent = parent;
        this.parameterTypes = method.getParameterTypes();
        this.requiresAuthentication = false;
        this.requiresPermission = null;
        this.description = "Gendarme command";
    }

    public boolean isAuthenticationRequired() {
        return this.requiresAuthentication;
    }

    public void setRequiredAuthentication(boolean requiresAuthentication) {
        this.requiresAuthentication = requiresAuthentication;
    }

    public String getRequiredPermission() {
        return this.requiresPermission;
    }

    public void setRequiredPermission(String requiresPermission) {
        this.requiresPermission = requiresPermission;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public GendarmeResponse invoke(UserType commandIssuer, Object... args) {
        if (this.requiresAuthentication && !commandIssuer.isAuthenticated()) {
            throw new GendarmeAccessRestrictedException();
        }

        if (null != this.requiresPermission && !commandIssuer.hasPermission(this.requiresPermission)) {
            throw new GendarmePermissionDeniedException();
        }

        try {
            if (0 == args.length) {
                return (GendarmeResponse) this.method.invoke(this.parent, commandIssuer);
            }

            Object[] calledArgs = new Object[args.length + 1];
            calledArgs[0] = commandIssuer;
            System.arraycopy(args, 0, calledArgs, 1, args.length);

            return (GendarmeResponse) this.method.invoke(this.parent, (Object[]) calledArgs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean accepts(Object[] args) {
        if (args.length != this.parameterTypes.length - 1) {
            return false;
        }

        for (int i = 0; i < args.length; i++) {
            if (!this.parameterTypes[i + 1].isInstance(args[i])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public Parameter[] getParameters() {
        return this.method.getParameters();
    }
}
