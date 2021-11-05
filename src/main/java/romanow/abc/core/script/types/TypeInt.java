package romanow.abc.core.script.types;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;

public class TypeInt extends TypeFace{
    private int value;
    public TypeInt(boolean valid, int vv) {
        super(valid);
        value = vv;
        }
    public TypeInt(int word) {
        super(true);
        value = word;
        }
    public TypeInt(TypeInt two) {
        super(two); }
    @Override
    public int type() {
        return ValuesBase.DTInt; }
    @Override
    public String typeName() {
        return "int"; }
    @Override
    public String typeNameTitle() {
        return "целое"; }
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
        throw new ScriptException(ValuesBase.SEIllegalCompare,"Недопустимое сравнение: "+this.typeName()+"/"+two.typeName());
        }
    @Override
    public String format(String fmtString) throws ScriptException {
        try {
            return String.format(fmtString, value);
            } catch (Exception ee){
                throw new ScriptException(ValuesBase.SEIntOutFormat,ValuesBase.SEModeWarning,"Форматирование целого: "+fmtString);
                }
        }
    @Override
    public void parse(String ss) throws ScriptException {
        try {
            value = Integer.parseInt(ss);
            setValid(true);
            } catch (Exception ee){
                setValid(false);
                throw new ScriptException(ValuesBase.SEIntFormat,"Формат целого: "+ss);
                }
            }
    @Override
    public TypeFace clone() {
        return new TypeInt(isValid(),value);
        }

    @Override
    public double toDouble() throws ScriptException {
        return value;
        }
    @Override
    public void fromDouble(double val) throws ScriptException {
        value = (int)val;
        }
    @Override
    public long toLong() throws ScriptException {
        return value;
        }
    @Override
    public void fromLong(long val) throws ScriptException {
        value =(int)val;
        }
    @Override
    public String toString(){
        return super.toString()+" "+value;
    }
}
