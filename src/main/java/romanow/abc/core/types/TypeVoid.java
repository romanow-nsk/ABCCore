package romanow.abc.core.types;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;

public class TypeVoid extends TypeFace {
    public TypeVoid() {}
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
    public int type() {
        return ValuesBase.DTVoid; }
    @Override
    public String typeName() {
        return "void"; }
    @Override
    public String typeNameTitle() {
        return "пустой"; }
    @Override
    public int compare(TypeFace two) throws ScriptException {
        throwBug("Недопустимое сравнение для void");
        return 0;
        }
    @Override
    public String format(String fmtString) throws ScriptException {
        return "";
        }
    @Override
    public void parse(String value) throws ScriptException {}
    @Override
    public TypeFace clone() {
        return new TypeVoid();
        }
    @Override
    public Object cloneWrapper() throws ScriptException{
        throwBug("Нет wrapper для void");
        return null;
        }

}
