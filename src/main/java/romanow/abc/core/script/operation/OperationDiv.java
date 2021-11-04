package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;

public class OperationDiv extends OperationBinary{
    public OperationDiv() {
        super(ValuesBase.ODiv, "div");
        }
    @Override
    public double opDouble(double one, double two) {
        return one/two;
        }
    @Override
    public long opLong(long one, long two) {
        return one/two;
        }
    @Override
    public Operation clone() {
        return new OperationDiv();
        }
}
