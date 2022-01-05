package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;

public class OperationGT extends OperationCompare{
    public OperationGT() {
        super(ValuesBase.OGT, "gt");
        }
    public boolean opCompare(int res) {
        return  res>0;}
    @Override
    public Operation clone() {
        return new OperationGT();
        }
}
