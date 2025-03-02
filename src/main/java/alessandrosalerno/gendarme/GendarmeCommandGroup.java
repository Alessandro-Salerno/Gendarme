package alessandrosalerno.gendarme;

import alessandrosalerno.gendarme.annotations.GendarmeCommand;
import alessandrosalerno.gendarme.annotations.GendarmeDescription;
import alessandrosalerno.gendarme.annotations.GendarmeRequireAuthentication;
import alessandrosalerno.gendarme.annotations.GendarmeRequirePermission;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class GendarmeCommandGroup<UserType extends GendarmeUser> implements GendarmeInvokable<UserType> {
    private final Map<String, List<GendarmeInvokable<UserType>>> subcommands;
    private String description;
    private final GendarmeHelper helper;

    public GendarmeCommandGroup(String description) {
        this.subcommands = new HashMap<>();
        this.description = description;
        this.helper = new GendarmeHelper(this);

        try {
            for (Field f : this.getClass().getFields()) {
                if (GendarmeInvokable.class.isAssignableFrom(f.getType())) {
                    this.addCommand(f.getName(), new GendarmeFieldContainer<>(f, this));
                }
            }

            for (Method m : this.getClass().getMethods()) {
                if (m.isAnnotationPresent(GendarmeCommand.class)) {
                    GendarmeMethodContainer<UserType> container = new GendarmeMethodContainer<>(m, this);
                    container.setRequiredAuthentication(m.isAnnotationPresent(GendarmeRequireAuthentication.class));
                    
                    if (m.isAnnotationPresent(GendarmeRequirePermission.class)) {
                        GendarmeRequirePermission perm = m.getAnnotation(GendarmeRequirePermission.class);
                        container.setRequiredPermission(perm.value());
                    }

                    if (m.isAnnotationPresent(GendarmeDescription.class)) {
                        GendarmeDescription desc = m.getAnnotation(GendarmeDescription.class);
                        container.setDescription(desc.value());
                    }

                    this.addCommand(m.getName(), container);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public GendarmeCommandGroup() {
        this("");
        this.description = this.getClass().getAnnotation(GendarmeDescription.class).value();
    }

    public GendarmeCommandGroup<UserType> addCommand(String name, GendarmeInvokable<UserType> command) {
        if (this.subcommands.containsKey(name)) {
            this.subcommands.get(name).add(command);
            return this;
        }

        this.subcommands.put(name, new ArrayList<>());
        this.subcommands.get(name).add(command);
        return this;
    }

    @Override
    public GendarmeResponse invoke(UserType commandIssuer, Object... args) {
        if (0 == args.length) {
            return this.baseCommand(commandIssuer);
        }

        if (!this.subcommands.containsKey(args[0])) {
            return this.helper.getHelp();
        }

        List<GendarmeInvokable<UserType>> candidates = this.subcommands.get(args[0]);
        Object[] subargs = this.subArguments(args);

        for (GendarmeInvokable<UserType> subcommand : candidates) {
            if (subcommand.accepts(subargs)) {
                return subcommand.invoke(commandIssuer, subargs);
            }
        }

        return this.helper.getHelp();
    }

    private Object[] subArguments(Object[] args) {
        if (0 == args.length) {
            return new Object[0];
        }

        if (1 == args.length) {
            return new Object[0];
        }

        return Arrays.copyOfRange(args, 1, args.length);
    }

    @Override
    public boolean accepts(Object[] args) {
        return true;
    }

    public GendarmeResponse baseCommand(UserType commandIssuer) {
        return null;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public Parameter[] getParameters() {
        return null;
    }
}
