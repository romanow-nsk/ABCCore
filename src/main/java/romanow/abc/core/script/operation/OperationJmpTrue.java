package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptException;
import romanow.abc.core.types.TypeBoolean;
import romanow.abc.core.types.TypeFace;

public class OperationJmpTrue extends Operation{
    public final int offset;
    public OperationJmpTrue(int ff) {
        super(ValuesBase.OJmpFalse, "jfalse");
        offset=ff;
        }
    @Override
    public void exec(OperationStack stack, CallContext context,boolean trace) throws ScriptException {
        if (trace) setTrace(toString());
        TypeFace cond = stack.pop();
        if (cond.type()!=ValuesBase.DTBoolean)
            throwException(context,ValuesBase.SEIllegalDT, this.name + " " + cond.typeName());
        if (((TypeBoolean)cond).getValue()==true)
            context.jump(offset);
        }
    @Override
    public Operation clone() {
        return new OperationJmpTrue(offset);
        }
    @Override
    public String toString(){
        return super.toString()+" "+offset;
    }
}
