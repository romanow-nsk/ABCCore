package romanow.abc.core.types;

import romanow.abc.core.UniException;
import romanow.abc.core.constants.ValuesBase;

public abstract class TypeFace {
    private boolean valid=true;
    private String varName="...";
    abstract public int type();                                           // Индекс ТД в ЯОП
    abstract public String typeNameTitle();                               // Имя ТД внешнее
    abstract public int compare(TypeFace two) throws UniException;        // Сравнение
    abstract String format(String fmtString) throws UniException;         // Форматированный ТД
    public abstract void  parse(String value) throws UniException;        // Парсинг из строки, !=null - сообщение об ошибке
    abstract public TypeFace clone();                                     // Клонирование
    abstract public Object cloneWrapper() throws UniException;
    public String typeName() {
        return ValuesBase.DTypes[type()];
        }
    public TypeFace(TypeFace two){
        valid = two.valid;
        }
    public TypeFace(boolean valid0){
        valid = valid0;
        }
    public TypeFace() {
        valid = true;
        }
    public String toString(){
        return varName+":"+typeName()+"/"+typeNameTitle();
        }
    public boolean isIntType(){
        int type = type();
        return type== ValuesBase.DTInt || type==ValuesBase.DTLong || type==ValuesBase.DTShort;
        }
    public boolean isFloatType(){
        int type = type();
        return type== ValuesBase.DTFloat || type==ValuesBase.DTDouble;
        }
    public boolean isNumericType(){
        return isFloatType() || isIntType();
        }
    public boolean isValid() {
        return valid; }
    public void setValid(boolean valid) {
        this.valid = valid; }
    public abstract double toDouble() throws UniException;
    public abstract void fromDouble(double val) throws UniException;
    public abstract long toLong() throws UniException;
    public abstract void fromLong(long val) throws UniException;
    public String getVarName() {
        return varName; }
    public void setVarName(String varName) {
        this.varName = varName; }
    public void setValue(boolean runTime, TypeFace two) throws UniException {
        if (type()==ValuesBase.DTBoolean){
            if (two.type()!=ValuesBase.DTBoolean)
                throw new UniException(ValuesBase.SEIllegalTypeConvertion,"Ошибка конвертации  "+typeName()+"->"+two.typeName());
            if (runTime)
                ((TypeBoolean)this).fromLong(two.toLong());
            }
        else
        if (isNumericType() && two.isNumericType()){
            if (runTime){
                if (isFloatType())
                    fromDouble(two.toDouble());
                else
                    fromLong(two.toLong());
                }
            }
        else
            throw new UniException(ValuesBase.SEIllegalTypeConvertion,"Ошибка конвертации  "+typeName()+"->"+two.typeName());
       }
    public void throwFormat(String mes) throws UniException{
        throw UniException.userFormat(mes);
        }
    public void throwBug(String mes) throws UniException{
        throw UniException.bug(mes);
        }
}
