package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptRunTimeException;
import romanow.abc.core.script.types.TypeDouble;
import romanow.abc.core.script.types.TypeFace;
import romanow.abc.core.script.types.TypeInt;

public class OperationAdd extends Operation{
    public OperationAdd() {
        super(ValuesBase.OAdd, "add");
        }
    @Override
    public void exec(OperationStack stack, CallContext context, boolean trace) throws ScriptRunTimeException {
        String out="";
        TypeFace one = stack.pop();
        TypeFace two = stack.pop();
        if (trace)
            out = toString()+" "+one+" "+two;
        if (one.isFloatType() || two.isFloatType()){
            double vv1 = one.toDouble();
            double vv2 = two.toDouble();
            TypeDouble res = new TypeDouble(vv1+vv2);
            if (trace)
                out+="->"+res;
            stack.push(res);
            return;
            }
        if (one.isIntType() && two.isIntType()) {
            long dd1 = one.toLong();
            long dd2 = two.toLong();
            TypeInt res2 = new TypeInt((int)(dd1+dd2));
            if (trace)
                out+="->"+res2;
            stack.push(res2);
            return;
            }
        throw new ScriptRunTimeException(ValuesBase.SREIllegalOperation, "Операция " + this.name + " " + one.typeName() + " " + two.typeName());
        }
    @Override
    public Operation clone() {
        return new OperationAdd();
        }
}
