package romanow.abc.core.script.functions;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptException;
import romanow.abc.core.types.TypeDouble;
import romanow.abc.core.types.TypeFace;
import romanow.abc.core.types.TypeString;

public class FStdAlert extends FunctionCall {
    public FStdAlert() {
        super("alert", "стандартная:трассировка");
        }
    @Override
    public int getResultType() {
        return ValuesBase.DTVoid;
        }
    @Override
    public int[] getParamTypes() {
        return new int[]{ ValuesBase.DTString,ValuesBase.DTDouble };
        }
    @Override
    public void call(CallContext context) throws ScriptException{
        OperationStack stack = context.getStack();
        String title;
        double value;
        try {
            TypeFace par1 = stack.pop();
            TypeFace par0 = stack.pop();
            title = par0.getSymbolValue();
            value = par1.getRealValue();
            System.out.println("Скрипт: "+context.getScriptTime()+" "+title+"="+value+" время: "+context.getScriptTime()+" мс");
            } catch (Exception ee){
                throw new ScriptException(ValuesBase.SEBug,"Исключение: "+ee.toString());
                }
        context.trace("Трассировка: "+title+value);
        }
}
