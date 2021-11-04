package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptRunTimeException;
import romanow.abc.core.script.types.TypeDouble;
import romanow.abc.core.script.types.TypeFace;
import romanow.abc.core.script.types.TypeInt;

public class OperationAdd extends OperationBinary{
    public OperationAdd() {
        super(ValuesBase.OAdd, "add");
        }
    @Override
    public double opDouble(double one, double two) {
        return one+two;
        }
    @Override
    public long opLong(long one, long two) {
        return one+two;
        }
    @Override
    public Operation clone() {
        return new OperationAdd();
        }
}
