package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;

public class OperationOr extends OperationBoolean{
    public OperationOr() {
        super(ValuesBase.OOr, "or");
        }
    @Override
    public boolean opBoolean(boolean one, boolean two) {
        return one || two;
        }
    @Override
    public Operation clone() {
        return new OperationOr();
        }
}
