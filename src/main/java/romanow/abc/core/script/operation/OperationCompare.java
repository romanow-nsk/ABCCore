package romanow.abc.core.script.operation;

import romanow.abc.core.UniException;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptException;
import romanow.abc.core.types.TypeBoolean;
import romanow.abc.core.types.TypeFace;

public abstract class OperationCompare extends Operation{
    public OperationCompare(int code, String name) {
        super(code,name);
        }
    public abstract boolean opDouble(double one, double two);
    public abstract boolean opLong(long one, long two);
    @Override
    public void exec(OperationStack stack, CallContext context, boolean trace) throws ScriptException {
        String out="";
        TypeFace two = stack.pop();
        TypeFace one = stack.pop();
        if (trace)
            out = toString()+" "+one+" "+two;
        try{
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
            }catch (ScriptException ee){
                throwException(context,ValuesBase.SEIllegalTypeConvertion, this.name + " " + one.typeName() + " " + two.typeName());
                }

        throwException(context,ValuesBase.SEIllegalOperation, this.name + " " + one.typeName() + " " + two.typeName());
        }
}
