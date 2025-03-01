package alessandrosalerno.gendarme.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface GendarmeRequirePermission {
    String value();
}
