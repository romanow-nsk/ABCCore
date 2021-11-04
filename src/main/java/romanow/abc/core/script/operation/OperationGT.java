package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;

public class OperationGT extends OperationCompare{
    public OperationGT() {
        super(ValuesBase.OGT, "gt");
        }
    @Override
    public boolean opDouble(double one, double two) {
        return one>two;
        }
    @Override
    public boolean opLong(long one, long two) {
        return one>two;
        }
    @Override
    public Operation clone() {
        return new OperationGT();
        }
}
