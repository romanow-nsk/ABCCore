package romanow.abc.core.script.functions;

import lombok.Getter;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.ScriptException;

//------------------- Класс функции и ее вызова
public abstract class FunctionCall {
    @Getter private String name;
    @Getter private String comment;
    public FunctionCall(String name0, String comment0){
        name = name0;
        comment = comment0;
        }
    public abstract int getResultType();                                    // Тип результа
    public abstract int []getParamTypes();                                          // Типы параметров
    public abstract void call(CallContext context) throws ScriptException;  // Параметры и результат в стеке
}
