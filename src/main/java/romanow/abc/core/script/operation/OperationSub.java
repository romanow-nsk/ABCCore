package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;
import romanow.abc.core.types.TypeFace;

public class OperationSub extends OperationBinary{
    public OperationSub() {
        super(ValuesBase.OSub, "sub");
        }
    @Override
    public void operation(TypeFace one, TypeFace two) throws ScriptException {
        switch (one.getGroup()){
            case ValuesBase.DTGInteger: one.setIntValue(one.getIntValue()-two.getIntValue());
                return;
            case ValuesBase.DTGReal: one.setRealValue(one.getRealValue()-two.getRealValue());
                return;
            }
        throw new ScriptException(ValuesBase.SEBug,"Недопустимая операция "+name+" "+" для "+ValuesBase.DTGroupNames[one.getGroup()]);
    }

    @Override
    public Operation clone() {
        return new OperationSub();
        }
}
