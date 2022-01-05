package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;

public class OperationEQ extends OperationCompare{
    public OperationEQ() {
        super(ValuesBase.OEqual, "eq");
        }
    @Override
    public Operation clone() {
        return new OperationEQ();
        }
    @Override
    public boolean opCompare(int res) {
        return  res==0;
        }
}
