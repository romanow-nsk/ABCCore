package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;

public class OperationSub extends OperationBinary{
    public OperationSub() {
        super(ValuesBase.OSub, "sub");
        }
    @Override
    public double opDouble(double one, double two) {
        return one-two;
        }
    @Override
    public long opLong(long one, long two) {
        return one-two;
        }
    @Override
    public Operation clone() {
        return new OperationSub();
        }
}
