package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptException;
import romanow.abc.core.script.types.TypeBoolean;
import romanow.abc.core.script.types.TypeFace;

public class OperationJmpFalse extends Operation{
    public final int offset;
    public OperationJmpFalse(int ff) {
        super(ValuesBase.OJmpFalse, "jfalse");
        offset=ff;
        }
    @Override
    public void exec(OperationStack stack, CallContext context,boolean trace) throws ScriptException {
        if (trace) setTrace(toString());
        TypeFace cond = stack.pop();
        if (cond.type()!=ValuesBase.DTBoolean)
            throwException(context,ValuesBase.SEIllegalDT, "Недопустимый ТД  " + this.name + " " + cond.typeName());
        if (((TypeBoolean)cond).getValue()==false)
            context.jump(offset);
        }
    @Override
    public Operation clone() {
        return new OperationJmpFalse(offset);
        }
    @Override
    public String toString(){
        return super.toString()+" "+offset;
    }
}
