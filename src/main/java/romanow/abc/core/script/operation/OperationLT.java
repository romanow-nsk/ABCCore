package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;

public class OperationLT extends OperationCompare{
    public OperationLT() {
        super(ValuesBase.OLT, "lt");
        }
    public boolean opCompare(int res) {
        return  res<0;
        }
    @Override
    public Operation clone() {
        return new OperationLT();
        }
}
