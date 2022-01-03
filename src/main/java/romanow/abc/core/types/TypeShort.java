package romanow.abc.core.types;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;


public class TypeShort extends TypeFace {
    private short value;
    public TypeShort(boolean valid, short vv) {
        super(valid);
        value = vv;
        }
    public TypeShort(short word) {
        super(true);
        value = word;
        }
    public TypeShort(TypeShort two) {
        super(two); }
    @Override
    public int type() {
        return ValuesBase.DTShort; }
    @Override
    public String typeNameTitle() {
        return "короткое"; }
    @Override
    public int compare(TypeFace two) throws ScriptException {
        if (two.isIntType()){
            long vv =  value-two.toLong();
            if (vv==0) return 0;
            return vv <0 ? -1 : 1;
            }
        if (two.isFloatType()){
            double vv =  value-two.toDouble();
            if (vv==0) return 0;
            return vv <0 ? -1 : 1;
            }
        throwBug("Недопустимое сравнение: "+this.typeName()+"/"+two.typeName());
        return 0;
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
    public Object cloneWrapper() {
        return new Short(value);
        }
    @Override
    public void parse(String ss) throws ScriptException {
        try {
            value = Short.parseShort(ss);
            setValid(true);
            } catch (Exception ee){
                setValid(false);
                throwFormat("Формат целого: "+ss);
                }
            }
    @Override
    public TypeFace clone() {
        return new TypeShort(isValid(),value);
        }

    @Override
    public double toDouble() throws ScriptException {
        return value;
        }
    @Override
    public void fromDouble(double val) throws ScriptException {
        value = (short) val;
        }
    @Override
    public long toLong() throws ScriptException {
        return value;
        }
    @Override
    public void fromLong(long val) throws ScriptException {
        value =(short) val;
        }
    @Override
    public String toString(){
        return super.toString()+" "+value;
    }
}
