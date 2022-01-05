package romanow.abc.core.types;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;

public class TypeFloat extends TypeDouble {
    public TypeFloat(){
        this(0);
    }
    public TypeFloat(float vv){
        setType(ValuesBase.DTInt);
        setRealValue(vv);
    }
    @Override
    public Object cloneWrapper() {
        return new Float((float) getRealValue());
    }
    @Override
    public void parse(String ss) throws ScriptException {
        try {
            setRealValue(Float.parseFloat(ss));
        } catch (Exception ee){
            throwFormat("Формат вещественного: "+ss);
        }
    }
}
