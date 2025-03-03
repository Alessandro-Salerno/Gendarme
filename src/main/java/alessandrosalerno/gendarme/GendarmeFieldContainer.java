package alessandrosalerno.gendarme;

import alessandrosalerno.gendarme.exceptions.GendarmeException;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

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
    public GendarmeResponse invoke(UserType commandIssuer, Object... args) throws GendarmeException {
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
    public Parameter[] getParameters() {
        this.evaluateField();
        return this.fieldValue.getParameters();
    }

    public boolean contains(Class<?> type) {
        this.evaluateField();
        return type.isAssignableFrom(this.fieldValue.getClass());
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
