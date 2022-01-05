package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;

public class OperationLE extends OperationCompare{
    public OperationLE() {
        super(ValuesBase.OLE, "le");
        }
    public boolean opCompare(int res) {
        return  res<=0;
        }
    @Override
    public Operation clone() {
        return new OperationLE();
        }
}
