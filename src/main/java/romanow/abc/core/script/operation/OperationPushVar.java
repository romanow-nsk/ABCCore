package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptException;
import romanow.abc.core.types.TypeFace;

public class OperationPushVar extends Operation{
    private String varName;
    public OperationPushVar(String varName0) {
        super(ValuesBase.OPushVar, "pushVar");
        varName = varName0;
        }
    @Override
    public void exec(OperationStack stack, CallContext context,boolean trace) throws ScriptException {
        TypeFace var = context.getVariables().get(varName);
        if (var==null)
            throwException(context,ValuesBase.SEIllegalVariable,varName);
        stack.push(var.cloneVar());
        }
    @Override
    public Operation clone() {
        return new OperationPushVar(varName);
        }
    @Override
    public String toString(){
        return super.toString()+" "+varName;
    }
}
