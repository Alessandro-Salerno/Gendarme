package alessandrosalerno.gendarme;

public class GendarmeHelper {
    private final GendarmeInvokable<?> target;

    public GendarmeHelper(GendarmeInvokable<?> target) {
        this.target = target;
    }

    public GendarmeResponse getHelp() {
        GendarmeResponse res = new GendarmeResponse("Help menu");
        return res;

//        try {
//            for (Field f : this.target.getClass().getFields()) {
//                if (f.getType().isInstance(GendarmeInvokable.class)) {
//                    GendarmeInvokable<?> command = GendarmeInvokable.class.cast(f.get(this));
//                    Map<String, Object> row = new HashMap<>();
//                    row.put("name", f.getName());
//                    row.put("parameter types", this.joinTypes(command.getParameterTypes()))
//                }
//            }
//
//            for (Method m : this.getClass().getMethods()) {
//                if (m.isAnnotationPresent(GendarmeCommand.class)) {
//                    GendarmeMethodAdapter<UserType> container = new GendarmeMethodAdapter<>(m, this);
//                    container.setRequiredAuthentication(m.isAnnotationPresent(GendarmeRequireAuthentication.class));
//
//                    if (m.isAnnotationPresent(GendarmeRequirePermission.class)) {
//                        GendarmeRequirePermission perm = m.getAnnotation(GendarmeRequirePermission.class);
//                        container.setRequiredPermission(perm.value());
//                    }
//
//                    if (m.isAnnotationPresent(GendarmeDescription.class)) {
//                        GendarmeDescription desc = m.getAnnotation(GendarmeDescription.class);
//                        container.setDescription(desc.value());
//                    }
//
//                    this.addCommand(m.getName(), container);
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }
}
