package romanow.abc.core.types;

import romanow.abc.core.UniException;
import romanow.abc.core.constants.ValuesBase;


public class TypeLong extends TypeFace {
    private long value;
    public TypeLong(boolean valid, long vv) {
        super(valid);
        value = vv;
        }
    public TypeLong(long word) {
        super(true);
        value = word;
        }
    public TypeLong(TypeLong two) {
        super(two); }
    @Override
    public int type() {
        return ValuesBase.DTLong; }
    @Override
    public String typeNameTitle() {
        return "длинное"; }
    @Override
    public Object cloneWrapper() {
        return new Long(value);
        }
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
            value = Long.parseLong(ss);
            setValid(true);
            } catch (Exception ee){
                setValid(false);
                throwFormat("Формат целого: "+ss);
                }
            }
    @Override
    public TypeFace clone() {
        return new TypeLong(isValid(),value);
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
        value =val;
        }
    @Override
    public String toString(){
        return super.toString()+" "+value;
    }
}