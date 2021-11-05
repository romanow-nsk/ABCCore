package romanow.abc.core.script.types;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;

public class TypeVoid extends TypeFace{
    public TypeVoid() {}
    @Override
    public double toDouble() throws ScriptException {
        throw new ScriptException(ValuesBase.SEIllegalOperation,"Недопустимое приведение void->double");
        }
    @Override
    public void fromDouble(double val) throws ScriptException {
        throw new ScriptException(ValuesBase.SEIllegalOperation,"Недопустимое приведение double->void");
        }
    @Override
    public long toLong() throws ScriptException {
        throw new ScriptException(ValuesBase.SEIllegalOperation,"Недопустимое приведение void->long");
        }
    @Override
    public void fromLong(long val) throws ScriptException {
        throw new ScriptException(ValuesBase.SEIllegalOperation,"Недопустимое приведение long->void");
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
        throw new ScriptException(ValuesBase.SEIllegalOperation,"Недопустимое сравнение для void");
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
}
