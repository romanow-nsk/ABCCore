package romanow.abc.core.dll;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)               // Типы аннотируемых элементов
@Retention(RetentionPolicy.RUNTIME)     // Время исполнения аннотации
public @interface METHOD {               // Синтаксис аннотации
    String title() default "...";
}