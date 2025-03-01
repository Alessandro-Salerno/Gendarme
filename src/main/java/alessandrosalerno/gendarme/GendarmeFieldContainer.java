package alessandrosalerno.gendarme;

import java.lang.reflect.Field;

public class GendarmeFieldContainer<UserType extends GendarmeUser> implements GendarmeInvokable<UserType> {
    private final Field field;
    private final Object parent;
    private GendarmeInvokable<UserType> fieldValue;

    public GendarmeFieldContainer(Field field, Object parent) {
        this.field = field;
        this.parent = parent;

        this.fieldValue = null;
    }

    @Override
    public GendarmeResponse invoke(UserType commandIssuer, Object... args) {
        this.evaluateField();
        return this.fieldValue.invoke(commandIssuer, args);
    }

    @Override
    public boolean accepts(Object[] args) {
        this.evaluateField();
        return this.fieldValue.accepts(args);
    }

    @Override
    public String getDescription() {
        this.evaluateField();;
        return this.fieldValue.getDescription();
    }

    @Override
    public Class<?>[] getParameterTypes() {
        this.evaluateField();
        return this.fieldValue.getParameterTypes();
    }

    private void evaluateField() {
        if (null == this.fieldValue) {
            try {
                this.fieldValue = GendarmeInvokable.class.cast(this.field.get(this.parent));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
