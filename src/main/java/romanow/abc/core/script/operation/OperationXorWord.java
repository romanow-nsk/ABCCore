package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptException;
import romanow.abc.core.types.TypeFace;
import romanow.abc.core.types.TypeLong;

public class OperationXorWord extends OperationBinary{
    public OperationXorWord() {
        super(ValuesBase.OXorW,"xorWord");
    }
    @Override
    public void operation(TypeFace one, TypeFace two) throws ScriptException {
        switch (one.getGroup()){
            case ValuesBase.DTGInteger: one.setIntValue(one.getIntValue() ^ two.getIntValue());
                return;
            }
        throw new ScriptException(ValuesBase.SEBug,"Недопустимая операция "+name+" "+" для "+ValuesBase.DTGroupNames[one.getGroup()]);
    }

    @Override
    public Operation clone() {
        return new OperationXorWord();
    }}
