package romanow.abc.core.types;

import romanow.abc.core.UniException;
import romanow.abc.core.constants.ValuesBase;

public class TypeFloat extends TypeFace {
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
    public String typeNameTitle() {
        return "короткое вещ."; }
    @Override
    public int compare(TypeFace two) throws UniException {
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
        throwBug("Недопустимое сравнение: "+this.typeName()+"/"+two.typeName());
        return 0;
    }
    @Override
    public String format(String fmtString) throws UniException {
        try {
            return String.format(fmtString, value);
            } catch (Exception ee){
                throwFormat("Форматирование целого: "+fmtString);
                return "";
                }
        }
    @Override
    public void parse(String ss) throws UniException {
        try{
             value = Float.parseFloat(ss);
             setValid(true);
            } catch (Exception ee){
                setValid(false);
                throwBug("Ошибка конвертации из double "+ss);
                }
            }
    @Override
    public TypeFace clone() {
        return new TypeFloat(this);
        }

    @Override
    public Object cloneWrapper() {
        return new Float(value);
    }

    public double toDouble() throws UniException{
        return value;
        }
    public void fromDouble(double val) throws UniException{
        value = (float) val;
        setValid(true);
        }
    @Override
    public long toLong() throws UniException {
        return (long)value;
        }
    @Override
    public void fromLong(long val) throws UniException {
        value = val;
        }
    @Override
    public String toString(){
        return super.toString()+" "+value;
    }
}
