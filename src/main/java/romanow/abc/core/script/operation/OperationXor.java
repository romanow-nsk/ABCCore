package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;

public class OperationXor extends OperationBoolean{
    public OperationXor() {
        super(ValuesBase.OOr, "xor");
        }
    @Override
    public boolean opBoolean(boolean one, boolean two) {
        return one ^ two;
        }
    @Override
    public Operation clone() {
        return new OperationXor();
        }
}
