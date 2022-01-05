package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;
import romanow.abc.core.types.TypeFace;


public class OperationAdd extends OperationBinary{
    public OperationAdd() {
        super(ValuesBase.OAdd, "add");
        }
    @Override
    public Operation clone() {
        return new OperationAdd();
        }
    @Override
    public void operation(TypeFace one, TypeFace two) throws ScriptException {
        switch (one.getGroup()){
            case ValuesBase.DTGInteger: one.setIntValue(one.getIntValue()+two.getIntValue());
                return;
            case ValuesBase.DTGReal: one.setRealValue(one.getRealValue()+two.getRealValue());
                return;
            case ValuesBase.DTGSymbol: one.setSymbolValue(one.getSymbolValue()+two.getSymbolValue());
                return;
                }
            throw new ScriptException(ValuesBase.SEBug,"Недопустимая операция "+name+" "+" для "+ValuesBase.DTGroupNames[one.getGroup()]);
        }
}
