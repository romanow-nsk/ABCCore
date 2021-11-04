package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;

public class OperationAnd extends OperationBoolean{
    public OperationAnd() {
        super(ValuesBase.OAnd, "and");
        }
    @Override
    public boolean opBoolean(boolean one, boolean two) {
        return one && two;
        }
    @Override
    public Operation clone() {
        return new OperationAnd();
        }
}
