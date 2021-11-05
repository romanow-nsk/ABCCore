package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptRunTimeException;
import romanow.abc.core.script.types.TypeDouble;
import romanow.abc.core.script.types.TypeFace;
import romanow.abc.core.script.types.TypeInt;
import romanow.abc.core.script.types.TypeLong;

public abstract class OperationBinary extends Operation{
    public OperationBinary(int code, String name) {
        super(code,name);
        }
    public abstract double opDouble(double one, double two);
    public abstract long opLong(long one, long two);
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
            TypeDouble res = new TypeDouble(opDouble(vv1,vv2));
            if (trace)
                out+="->"+res;
            stack.push(res);
            return;
            }
        if (one.isIntType() && two.isIntType()) {
            long dd1 = one.toLong();
            long dd2 = two.toLong();
            TypeLong res2 = new TypeLong(opLong(dd1,dd2));
            if (trace)
                out+="->"+res2;
            stack.push(res2);
            return;
            }
        throwException(context,ValuesBase.SREIllegalOperation, this.name + " " + one.typeName() + " " + two.typeName());
        }
}
