package romanow.abc.core.script.types;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptRunTimeException;

public class TypeVoid extends TypeFace{
    private long val;
    public TypeVoid(long word) {
        super(word); }
    public TypeVoid() {}
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
    public int compare(TypeFace two) throws ScriptRunTimeException {
        throw new ScriptRunTimeException(ValuesBase.SREIllegalOperation,"Недопустимое сравнение для void");
        }
    @Override
    public String format(String fmtString) throws ScriptRunTimeException {
        return "";
        }
    @Override
    public void parse(String value) throws ScriptRunTimeException {}
    @Override
    public TypeFace clone() {
        return new TypeVoid(val);
        }
}
