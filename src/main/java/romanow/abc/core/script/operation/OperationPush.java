package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptException;
import romanow.abc.core.script.types.TypeFace;
import romanow.abc.core.script.types.TypeVoid;

public class OperationPush extends Operation{
    private TypeFace data;
    public OperationPush(TypeFace data0) {
        super(ValuesBase.OPush, "push");
        data = data0;
        }
    @Override
    public void exec(OperationStack stack, CallContext context,boolean trace) throws ScriptException {
        if (trace) setTrace(toString());
        stack.push(data);
        }
    @Override
    public Operation clone() {
        return new OperationPush(data);
        }
    @Override
    public String toString(){
        return super.toString()+" "+data;
    }
}
