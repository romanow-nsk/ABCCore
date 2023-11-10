package romanow.abc.core.script.functions;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptException;
import romanow.abc.core.types.TypeFace;

public class FStdAlertBool extends FunctionCall {
    public FStdAlertBool() {
        super("alert", "стандартная:трассировка");
        }
    @Override
    public int getResultType() {
        return ValuesBase.DTVoid;
        }
    @Override
    public int[] getParamTypes() {
        return new int[]{ ValuesBase.DTString,ValuesBase.DTBoolean };
        }
    @Override
    public void call(CallContext context, boolean trace) throws ScriptException{
        OperationStack stack = context.getStack();
        String title;
        String value;
        try {
            TypeFace par1 = stack.pop();
            TypeFace par0 = stack.pop();
            title = par0.getSymbolValue();
            value = par1.valueToString();
            } catch (Exception ee){
                throw new ScriptException(ValuesBase.SEBug,"Исключение: "+ee.toString());
                }
        context.trace("Трассировка: "+title+value);
        }
}
