package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;

public class OperationPow extends OperationBinary{
    public OperationPow() {
        super(ValuesBase.OPow, "pow");
        }
    @Override
    public double opDouble(double one, double two) {
        return Math.pow(one,two);
        }
    @Override
    public long opLong(long one, long two) {
        return (long)Math.pow(one,two);
        }
    @Override
    public Operation clone() {
        return new OperationPow();
        }
}
