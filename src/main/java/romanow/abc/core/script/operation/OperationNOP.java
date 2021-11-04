package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptRunTimeException;

public class OperationNOP extends Operation{
    public OperationNOP() {
        super(ValuesBase.ONOP, "nop");
        }
    @Override
    public void exec(OperationStack stack, CallContext context) throws ScriptRunTimeException {
        }
    @Override
    public Operation clone() {
        return new OperationNOP();
        }
}
