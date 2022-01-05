package romanow.abc.core.types;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;

public abstract class TypeFace {
    private boolean valid=true;
    private String varName="...";
    abstract public int type();                                           // Индекс ТД в ЯОП
    abstract public String typeNameTitle();                               // Имя ТД внешнее
    abstract public int compare(TypeFace two) throws ScriptException;        // Сравнение
    abstract String format(String fmtString) throws ScriptException;         // Форматированный ТД
    public abstract void  parse(String value) throws ScriptException;        // Парсинг из строки, !=null - сообщение об ошибке
    abstract public TypeFace clone();                                     // Клонирование
    abstract public Object cloneWrapper() throws ScriptException;
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
    public abstract double toDouble() throws ScriptException;
    public abstract void fromDouble(double val) throws ScriptException;
    public abstract long toLong() throws ScriptException;
    public abstract void fromLong(long val) throws ScriptException;
    public String getVarName() {
        return varName; }
    public void setVarName(String varName) {
        this.varName = varName; }
    public void setValue(boolean runTime, TypeFace two) throws ScriptException {
        if (type()==ValuesBase.DTString && two.type()==ValuesBase.DTString){
            if (runTime)
                ((TypeString)this).parse(two.toString());
            return;
            }
        if (type()==ValuesBase.DTBoolean){
            if (two.type()!=ValuesBase.DTBoolean)
                throw new ScriptException(ValuesBase.SEIllegalTypeConvertion,"Ошибка конвертации  "+typeName()+"->"+two.typeName());
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
            throw new ScriptException(ValuesBase.SEIllegalTypeConvertion,"Ошибка конвертации  "+typeName()+"->"+two.typeName());
       }
    public void throwFormat(String mes) throws ScriptException{
        throw new ScriptException(ValuesBase.SEIllegalFormat,mes);
        }
    public void throwBug(String mes) throws ScriptException{
        throw new ScriptException(ValuesBase.SEBug,mes);
        }
}
