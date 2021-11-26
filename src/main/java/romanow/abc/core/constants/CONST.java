package romanow.abc.core.constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target(ElementType.FIELD)               // Типы аннотируемых элементов
@Retention(RetentionPolicy.RUNTIME)     // Время исполнения аннотации
public @interface CONST {               // Синтаксис аннотации
    String group() default "...";       // Параметры аннотации
    String title() default "...";
    String className() default "";      // Имя класса
}