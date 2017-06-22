package test.anons.te;/**
 * Created by Алексей on 21.06.2017.
 */

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BX {

    A[] value();
}
