package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;

public class OperationMul extends OperationBinary{
    public OperationMul() {
        super(ValuesBase.OMul, "mul");
        }
    @Override
    public double opDouble(double one, double two) {
        return one*two;
        }
    @Override
    public long opLong(long one, long two) {
        return one*two;
        }
    @Override
    public Operation clone() {
        return new OperationMul();
        }
}
