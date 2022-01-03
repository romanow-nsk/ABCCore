package romanow.abc.core.types;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;


public class TypeBoolean extends TypeFace {
    private boolean value;
    public boolean getValue() {
        return value; }
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
    public String typeNameTitle() {
        return "логическое"; }
    @Override
    public int compare(TypeFace two) throws ScriptException {
        if (two.type()!=ValuesBase.DTBoolean)
            throwBug("Недопустимое сравнение: "+this.typeName()+"/"+two.typeName());
        long vv = toLong() - two.toLong();
        return vv==0 ? 0 :(vv<0 ? -1 : 1);
        }
    @Override
    public String format(String fmtString) throws ScriptException {
        try {
            return String.format(fmtString, value);
            } catch (Exception ee){
                throwFormat("Форматирование целого: "+fmtString);
                return "";
                }
        }
    @Override
    public void parse(String ss) throws ScriptException {
        try {
            value = Boolean.parseBoolean(ss);
            setValid(true);
            } catch (Exception ee){
                setValid(false);
                throwFormat("Формат целого: "+ss);
                }
            }
    @Override
    public TypeFace clone() {
        return new TypeBoolean(isValid(),value);
        }

    @Override
    public Object cloneWrapper() throws ScriptException {
        return new Boolean(value);
    }

    @Override
    public double toDouble() throws ScriptException {
        return value ? 1: 0;
        }
    @Override
    public void fromDouble(double val) throws ScriptException {
        value = val!=0;
        }
    @Override
    public long toLong() throws ScriptException {
        return value ? 1: 0;
        }
    @Override
    public void fromLong(long val) throws ScriptException {
        value = val!=0;
        }
    @Override
    public String toString(){
        return super.toString()+" "+value;
    }
}
