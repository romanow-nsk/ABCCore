package romanow.abc.core.script.functions;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptException;
import romanow.abc.core.types.TypeDouble;
import romanow.abc.core.types.TypeFace;

public class FStdSin extends FunctionCall {
    public FStdSin() {
        super("sin", "стандартная:sin");
        }
    @Override
    public int getResultType() {
        return ValuesBase.DTDouble;
        }
    @Override
    public int[] getParamTypes() {
        return new int[]{ ValuesBase.DTDouble };
        }
    @Override
    public void call(CallContext context, Object env) throws ScriptException{
        OperationStack stack = context.getStack();
        TypeFace par0 = stack.pop();
        double dd = Math.sin(par0.toDouble());
        stack.push(new TypeDouble(dd));
        }
}
