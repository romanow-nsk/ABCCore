package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;


public class OperationAdd extends OperationBinary{
    public OperationAdd() {
        super(ValuesBase.OAdd, "add");
        }
    @Override
    public double opDouble(double one, double two) {
        return one+two;
        }
    @Override
    public long opLong(long one, long two) {
        return one+two;
        }
    @Override
    public Operation clone() {
        return new OperationAdd();
        }
}
