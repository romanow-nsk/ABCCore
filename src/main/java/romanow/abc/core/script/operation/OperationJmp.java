package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptException;

public class OperationJmp extends Operation{
    public final int offset;
    public OperationJmp(int ff) {
        super(ValuesBase.OJmp, "jmp");
        offset=ff;
        }
    @Override
    public void exec(OperationStack stack, CallContext context,boolean trace) throws ScriptException {
        if (trace) setTrace(toString());
        context.jump(offset);
        }
    @Override
    public Operation clone() {
        return new OperationJmp(offset);
        }
    @Override
    public String toString(){
        return super.toString()+" "+offset;
    }
}
