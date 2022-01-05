package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptException;
import romanow.abc.core.types.TypeFace;
import romanow.abc.core.types.TypeLong;

public class OperationNotWord extends Operation{
    public OperationNotWord() {
        super(ValuesBase.ONotW,"notWord");
        }
    @Override
    public void exec(OperationStack stack, CallContext context, boolean trace) throws ScriptException {
        String out="";
        TypeFace one = stack.pop();
        if (trace)
            out = toString()+" "+one;
        if (one.getGroup()==ValuesBase.DTGInteger){
            long vv1 = one.getIntValue();
            TypeLong res = new TypeLong(~vv1);
            if (trace)
                out+="->"+res;
            stack.push(res);
            return;
            }
        throwException(context,ValuesBase.SEIllegalOperation, this.name + " " + one.getTypeName());
        }

    @Override
    public Operation clone() {
        return new OperationNotWord();
        }
}
