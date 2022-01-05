package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;

public class OperationGE extends OperationCompare{
    public OperationGE() {
        super(ValuesBase.OGE, "ge");
        }
    public boolean opCompare(int res) {
        return  res>=0;
        }
    @Override
    public Operation clone() {
        return new OperationGE();
        }
}
