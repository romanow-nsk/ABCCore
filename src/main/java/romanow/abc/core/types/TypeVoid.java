package romanow.abc.core.types;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;

public class TypeVoid extends TypeFace {
    public TypeVoid(){
        setType(ValuesBase.DTVoid);
        }
    @Override
    public double toDouble() throws ScriptException {
        throwBug("Недопустимое приведение void->double");
        return 0;
        }
    @Override
    public void fromDouble(double val) throws ScriptException {
        throwBug("Недопустимое приведение double->void");
        }
    @Override
    public long toLong() throws ScriptException {
        throwBug("Недопустимое приведение void->long");
        return 0;
        }
    @Override
    public void fromLong(long val) throws ScriptException {
        throwBug("Недопустимое приведение long->void");
        }
    @Override
    public void convertToGroup(boolean runTime, int group) throws ScriptException {
        throwBug("Недопустимое приведение void->"+ValuesBase.DTGroupNames[group]);
        }
    @Override
    public void setValue(boolean runTime, TypeFace two) throws ScriptException {
        throwBug("Недопустимое присваивание для void");
        }

    public int compare(TypeFace two) throws ScriptException {
        throwBug("Недопустимое сравнение для void");
        return 0;
        }
    @Override
    String format() throws ScriptException {
        return "";
        }
    @Override
    public void parse(String value) throws ScriptException {}
    @Override
    public Object cloneWrapper() throws ScriptException{
        throwBug("Нет wrapper для void");
        return null;
        }

}
