package romanow.abc.core.types;

import romanow.abc.core.UniException;
import romanow.abc.core.constants.ValuesBase;

public class TypeString extends TypeFace {
    private String value;
    public TypeString(String val) {
        super(true);
        value = val;
        }
    public TypeString(TypeString two){
        super(two.isValid());
        value = two.value;
        }
    @Override
    public int type() {
        return ValuesBase.DTString; }
    @Override
    public String typeNameTitle() {
        return "короткое вещ."; }
    @Override
    public int compare(TypeFace two) throws UniException {
        if (two.type()==ValuesBase.DTString)
            return value.compareTo(((TypeString)two).value);
        throwBug("Недопустимое сравнение: "+this.typeName()+"/"+two.typeName());
        return 0;
        }
    @Override
    public String format(String fmtString) throws UniException {
        return value;
        }
    @Override
    public void parse(String ss) throws UniException {
        value = ss;
        }
    @Override
    public TypeFace clone() {
        return new TypeString(this);
        }
    public double toDouble() throws UniException{
        try {
            return Double.parseDouble(value);
            } catch (Exception ee){
                throwFormat("Недопустимое приведение к double "+value);
                return 0;
                }
        }
    public void fromDouble(double val) throws UniException{
        value = ""+val;
        setValid(true);
        }
    @Override
    public long toLong() throws UniException {
        try {
            return Long.parseLong(value);
        } catch (Exception ee){
            throwFormat("Недопустимое приведение к long "+value);
            return 0;
            }
        }
    @Override
    public void fromLong(long val) throws UniException {
        value = ""+val;
        }
    @Override
    public Object cloneWrapper() {
        return new String(value);
        }

    @Override
    public String toString(){
        return super.toString()+" "+value;
    }
}
