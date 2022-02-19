package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptException;
import romanow.abc.core.types.TypeDouble;
import romanow.abc.core.types.TypeFace;
import romanow.abc.core.types.TypeLong;

public class OperationOrWord extends OperationBinary{
    public OperationOrWord() {
        super(ValuesBase.OOrW,"orWord");
    }
    @Override
    public TypeFace operation(TypeFace one, TypeFace two) throws ScriptException {
        if (one.getGroup()==ValuesBase.DTGInteger || two.getGroup()==ValuesBase.DTGInteger)
            return new TypeLong(one.toLong() | two.toLong()) ;
        throw new ScriptException(ValuesBase.SEBug,"Недопустимая операция "+name+" "+" для "+ValuesBase.DTGroupNames[one.getGroup()]);
        }

    @Override
    public Operation clone() {
        return new OperationOrWord();
    }}
