package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;

public class OperationLE extends OperationCompare{
    public OperationLE() {
        super(ValuesBase.OLE, "le");
        }
    @Override
    public boolean opDouble(double one, double two) {
        return one<=two;
        }
    @Override
    public boolean opLong(long one, long two) {
        return one<=two;
        }
    @Override
    public Operation clone() {
        return new OperationLE();
        }
}
