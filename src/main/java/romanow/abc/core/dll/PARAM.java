package romanow.abc.core.dll;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)               // Типы аннотируемых элементов
@Retention(RetentionPolicy.RUNTIME)     // Время исполнения аннотации
public @interface PARAM {               // Синтаксис аннотации
    String name() default "...";       // Параметры аннотации
    String title() default "...";
}