package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptRunTimeException;
import romanow.abc.core.script.types.TypeBoolean;
import romanow.abc.core.script.types.TypeDouble;
import romanow.abc.core.script.types.TypeFace;
import romanow.abc.core.script.types.TypeLong;

public abstract class OperationCompare extends Operation{
    public OperationCompare(int code, String name) {
        super(code,name);
        }
    public abstract boolean opDouble(double one, double two);
    public abstract boolean opLong(long one, long two);
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
            TypeBoolean res = new TypeBoolean(opDouble(vv1,vv2));
            if (trace)
                out+="->"+res;
            stack.push(res);
            return;
            }
        if (one.isIntType() && two.isIntType()) {
            long dd1 = one.toLong();
            long dd2 = two.toLong();
            TypeBoolean res2 = new TypeBoolean(opLong(dd1,dd2));
            if (trace)
                out+="->"+res2;
            stack.push(res2);
            return;
            }
        throwException(context,ValuesBase.SREIllegalOperation, this.name + " " + one.typeName() + " " + two.typeName());
        }
}
