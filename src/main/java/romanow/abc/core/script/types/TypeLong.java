package romanow.abc.core.script.types;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptRunTimeException;

public class TypeLong extends TypeFace{
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
    public String typeName() {
        return "long"; }
    @Override
    public String typeNameTitle() {
        return "длинное"; }
    @Override
    public int compare(TypeFace two) throws ScriptRunTimeException {
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
        try {
            value = Long.parseLong(ss);
            setValid(true);
            } catch (Exception ee){
                setValid(false);
                throw new ScriptRunTimeException(ValuesBase.SREIntFormat,"Формат целого: "+ss);
                }
            }
    @Override
    public TypeFace clone() {
        return new TypeLong(isValid(),value);
        }

    @Override
    public double toDouble() throws ScriptRunTimeException {
        return value;
        }
    @Override
    public void fromDouble(double val) throws ScriptRunTimeException {
        value = (int)val;
        }
    @Override
    public long toLong() throws ScriptRunTimeException {
        return value;
        }
    @Override
    public void fromLong(long val) throws ScriptRunTimeException {
        value =val;
        }
    @Override
    public String toString(){
        return super.toString()+" "+value;
    }
}
