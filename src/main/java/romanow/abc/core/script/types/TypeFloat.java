package romanow.abc.core.script.types;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;

public class TypeFloat extends TypeFace{
    private float value;
    public TypeFloat(float val) {
        super(true);
        value = val;
        }
    public TypeFloat(TypeFloat two){
        super(two.isValid());
        value = two.value;
        }
    @Override
    public int type() {
        return ValuesBase.DTFloat; }
    @Override
    public String typeName() {
        return "float"; }
    @Override
    public String typeNameTitle() {
        return "короткое вещ."; }
    @Override
    public int compare(TypeFace two) throws ScriptException {
        if (two.isIntType()){
            double vv =  value-two.toDouble();
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
        try{
             value = Float.parseFloat(ss);
             setValid(true);
            } catch (Exception ee){
                setValid(false);
                throw new ScriptException(ValuesBase.SEFloatConvertation,"Ошибка конвертации из double "+ss);
                }
            }
    @Override
    public TypeFace clone() {
        return new TypeFloat(this);
        }
    public double toDouble() throws ScriptException{
        return value;
        }
    public void fromDouble(double val) throws ScriptException{
        value = (float) val;
        setValid(true);
        }
    @Override
    public long toLong() throws ScriptException {
        return (long)value;
        }
    @Override
    public void fromLong(long val) throws ScriptException {
        value = val;
        }
    @Override
    public String toString(){
        return super.toString()+" "+value;
    }
}
