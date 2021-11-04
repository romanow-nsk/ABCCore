package romanow.abc.core.script.types;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptRunTimeException;

public class TypeDouble extends TypeFace{
    private double value;
    public TypeDouble(double val) {
        super(true);
        value = val;
        }
    public TypeDouble(TypeDouble two){
        super(two.isValid());
        value = two.value;
        }
    @Override
    public int type() {
        return ValuesBase.DTDouble; }
    @Override
    public String typeName() {
        return "double"; }
    @Override
    public String typeNameTitle() {
        return "длинное вещ."; }
    @Override
    public int compare(TypeFace two) throws ScriptRunTimeException {
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
        throw new ScriptRunTimeException(ValuesBase.SREIllegalCompare,"Недопустимое сравнение: "+this.typeName()+"/"+two.typeName());
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
        try{
             value = Double.parseDouble(ss);
             setValid(true);
            } catch (Exception ee){
                setValid(false);
                throw new ScriptRunTimeException(ValuesBase.SREFloatConvertation,"Ошибка конвертации из double "+ss);
                }
            }
    @Override
    public TypeFace clone() {
        return new TypeDouble(this);
        }
    public double toDouble() throws ScriptRunTimeException{
        return value;
        }
    public void fromDouble(double val) throws ScriptRunTimeException{
        value = val;
        setValid(true);
        }
    @Override
    public long toLong() throws ScriptRunTimeException {
        return (long)value;
        }
    @Override
    public void fromLong(long val) throws ScriptRunTimeException {
        value = val;
        }
    @Override
    public String toString(){
        return super.toString()+" "+value;
    }
}
