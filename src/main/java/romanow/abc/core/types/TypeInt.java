package romanow.abc.core.types;

import romanow.abc.core.UniException;
import romanow.abc.core.constants.ValuesBase;

public class TypeInt extends TypeFace {
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
    public String typeNameTitle() {
        return "целое"; }
    @Override
    public int compare(TypeFace two) throws UniException {
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
    public Object cloneWrapper() {
        return new Integer(value);
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
        try {
            value = Integer.parseInt(ss);
            setValid(true);
            } catch (Exception ee){
                setValid(false);
                throwFormat("Формат целого: "+ss);
                }
            }
    @Override
    public TypeFace clone() {
        return new TypeInt(isValid(),value);
        }

    @Override
    public double toDouble() throws UniException {
        return value;
        }
    @Override
    public void fromDouble(double val) throws UniException {
        value = (int)val;
        }
    @Override
    public long toLong() throws UniException {
        return value;
        }
    @Override
    public void fromLong(long val) throws UniException {
        value =(int)val;
        }
    @Override
    public String toString(){
        return super.toString()+" "+value;
    }
}
