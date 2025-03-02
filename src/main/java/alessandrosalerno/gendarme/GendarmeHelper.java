package alessandrosalerno.gendarme;

import alessandrosalerno.gendarme.annotations.GendarmeCommand;
import alessandrosalerno.gendarme.annotations.GendarmeDescription;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class GendarmeHelper {
    private final GendarmeCommandGroup<?> target;

    public GendarmeHelper(GendarmeCommandGroup<?> target) {
        this.target = target;
    }

    public GendarmeResponse getHelp() {
        GendarmeResponse res = new GendarmeResponse("", "Command", "Signature", "Description");

        for (String commandName : this.target.subcommands.keySet()) {
            List<? extends GendarmeInvokable<?>> commands = this.target.subcommands.get(commandName);

            for (GendarmeInvokable<?> command : commands) {
                res.addTableRow(commandName, this.generateSignature(command), command.getDescription());
            }
        }

        return res;
    }

    private String generateSignature(GendarmeInvokable<?> of) {
        if (of instanceof GendarmeFieldContainer<?> container
            && container.contains(GendarmeCommandGroup.class)) {
            return "String inner";
        }

        return this.generateMethodSignature(of.getParameters());
    }

    private String generateMethodSignature(Parameter[] params) {
        StringBuilder builder = new StringBuilder();

        if (null == params) {
            return "???";
        }

        for (int i = 1; i < params.length; i++) {
            String name = "arg" + i;
            if (params[i].isNamePresent()) {
                name = params[i].getName();
            }

            builder.append(params[i].getType().getSimpleName()).append(" ").append(name);

            if (i != params.length - 1) {
                builder.append(", ");
            }
        }

        return builder.toString();
    }
}
