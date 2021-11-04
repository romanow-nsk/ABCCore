package romanow.abc.core.script.types;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptRunTimeException;

public class TypeBoolean extends TypeFace{
    private boolean value;
    public TypeBoolean(boolean valid, boolean vv) {
        super(valid);
        value = vv;
        }
    public TypeBoolean(boolean word) {
        super(true);
        value = word;
        }
    public TypeBoolean(TypeBoolean two) {
        super(two);
        value = two.value;
        }
    @Override
    public int type() {
        return ValuesBase.DTBoolean; }
    @Override
    public String typeName() {
        return "boolean"; }
    @Override
    public String typeNameTitle() {
        return "логическое"; }
    @Override
    public int compare(TypeFace two) throws ScriptRunTimeException {
        if (two.type()!=ValuesBase.DTBoolean)
            throw new ScriptRunTimeException(ValuesBase.SREIllegalCompare,"Недопустимое сравнение: "+this.typeName()+"/"+two.typeName());
        long vv = toLong() - two.toLong();
        return vv==0 ? 0 :(vv<0 ? -1 : 1);
        }
    @Override
    public String format(String fmtString) throws ScriptRunTimeException {
        try {
            return String.format(fmtString, value);
            } catch (Exception ee){
                throw new ScriptRunTimeException(ValuesBase.SREIntOutFormat,ValuesBase.SEModeWarning,"Форматирование целого: "+fmtString);
                }
        }
    @Override
    public void parse(String ss) throws ScriptRunTimeException {
        try {
            value = Boolean.parseBoolean(ss);
            setValid(true);
            } catch (Exception ee){
                setValid(false);
                throw new ScriptRunTimeException(ValuesBase.SREIntFormat,"Формат целого: "+ss);
                }
            }
    @Override
    public TypeFace clone() {
        return new TypeBoolean(isValid(),value);
        }

    @Override
    public double toDouble() throws ScriptRunTimeException {
        return value ? 1: 0;
        }
    @Override
    public void fromDouble(double val) throws ScriptRunTimeException {
        value = val!=0;
        }
    @Override
    public long toLong() throws ScriptRunTimeException {
        return value ? 1: 0;
        }
    @Override
    public void fromLong(long val) throws ScriptRunTimeException {
        value = val!=0;
        }
    @Override
    public String toString(){
        return super.toString()+" "+value;
    }
}
