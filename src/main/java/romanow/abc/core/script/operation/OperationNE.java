package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;

public class OperationNE extends OperationCompare{
    public OperationNE() {
        super(ValuesBase.ONoEqual, "neq");
        }
    public boolean opCompare(int res) {
        return  res!=0;
        }
    @Override
    public Operation clone() {
        return new OperationNE();
        }
}
