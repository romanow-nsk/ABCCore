package romanow.abc.core.types;

import lombok.Getter;
import lombok.Setter;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;

public abstract class TypeFace {
    @Getter @Setter private boolean boolValue=false;
    @Getter @Setter private long intValue=0;
    @Getter @Setter private double realValue=0;
    @Getter @Setter private String symbolValue="";
    @Getter @Setter private int type=ValuesBase.DTVoid;
    @Getter @Setter private String varName="...";
    public abstract int compare(TypeFace two) throws ScriptException;     // Сравнение
    public abstract String formatTo() throws ScriptException;                      // Форматированный ТД
    public abstract void  parse(String value) throws ScriptException;     // Парсинг из строки, !=null - сообщение об ошибке
    abstract public Object cloneWrapper() throws ScriptException;
    public String getTypeName(){
        return ValuesBase.DTTypes[type]; }
    public String toString(){
        return varName+"["+getTypeName()+"]:";
        }
    public TypeFace(TypeFace two){
        }
    public String valueToString(){
        if (isLogical())
            return ""+boolValue;
        if (isInteger())
            return ""+intValue;
        if (isReal())
            return ""+String.format("%8.3f",realValue);
        if (isSymbol())
            return symbolValue;
        return "???";
        }
    public TypeFace(){}
    public TypeFace cloneVar() throws ScriptException{
        TypeFace two = null;
        try {
            two = getClass().newInstance();
            } catch (Exception ee){
                throw new ScriptException(ValuesBase.SEBug,"Ошибка клонирования "+getTypeName());
                }
        two.boolValue = boolValue;
        two.intValue = intValue;
        two.realValue = realValue;
        two.symbolValue = symbolValue;
        two.type = type;
        return two;
        }
    public int getGroup(){
        return ValuesBase.DTGroup[type]; }
    public void setTypeByGroup(int group){
        type = ValuesBase.DTGTypes[group];
        }
    public boolean isNumeric(){
        return getGroup()==ValuesBase.DTGReal || getGroup()==ValuesBase.DTGInteger;}
    public boolean isInteger(){
        return getGroup()==ValuesBase.DTGInteger; }
    public boolean isSymbol(){
        return getGroup()==ValuesBase.DTGSymbol; }
    public boolean isReal(){
        return getGroup()==ValuesBase.DTGReal; }
    public boolean isLogical(){
        return getGroup()==ValuesBase.DTGLogical; }
    public boolean isEqualGroups(TypeFace two){
        return getGroup()==two.getGroup(); }
    public abstract double toDouble() throws ScriptException;
    public abstract void fromDouble(double val) throws ScriptException;
    public abstract long toLong() throws ScriptException;
    public abstract void fromLong(long val) throws ScriptException;
    public String getVarName() {
        return varName; }
    public void setVarName(String varName) {
        this.varName = varName; }
    public void throwFormat(String mes) throws ScriptException{
        throw new ScriptException(ValuesBase.SEIllegalFormat,mes);
        }
    public void throwBug(String mes) throws ScriptException{
        throw new ScriptException(ValuesBase.SEBug,mes);
        }
    //--------------- Изменение группы представления в новому типу ---------------------
    // public abstract void convertToGroup(boolean runTime,int group) throws ScriptException;
    public abstract void setValue(boolean runTime,TypeFace two) throws ScriptException;
    public int getCommonGroup(TypeFace two) throws ScriptException{
        int group1 = getGroup();
        int group2 = two.getGroup();
        if (group1==group2) return group1;
        if (group1==ValuesBase.DTGSymbol || group2==ValuesBase.DTGSymbol)
            return group1;            // Все приводится к строке
        if (group1==ValuesBase.DTGLogical || group2==ValuesBase.DTGLogical)
            throw new ScriptException(ValuesBase.SEIllegalTypeConvertion,"Недопустимое сочетание типов  "+getTypeName()+"<>"+two.getTypeName());
        if (group1==ValuesBase.DTGReal || group2==ValuesBase.DTGReal)
            return ValuesBase.DTGReal;
        return ValuesBase.DTGInteger;
        }
    public static int getCommonGroup(int group1, int group2) {
        if (group1==group2) return group1;
        if (group1==ValuesBase.DTGSymbol || group2==ValuesBase.DTGSymbol)
            return group1;            // Все приводится к строке
        if (group1==ValuesBase.DTGLogical || group2==ValuesBase.DTGLogical)
            return -1;
        if (group1==ValuesBase.DTGReal || group2==ValuesBase.DTGReal)
            return ValuesBase.DTGReal;
        return ValuesBase.DTGInteger;
    }
    //------------------- Проверка на разрешение присваивания к типу this=two
    public boolean isSetEnable(int group2){
        int group1 = getGroup();
        if (group1==ValuesBase.DTGUndefuned || group2==ValuesBase.DTGUndefuned)
            return false;
        if (group1==group2)
            return true;
        if (group1==ValuesBase.DTGSymbol)
            return true;
        if (group1==ValuesBase.DTGLogical)
            return false;
        if (group1==ValuesBase.DTGReal && (group2==ValuesBase.DTGLogical))
            return false;
        if (group1==ValuesBase.DTGInteger && (group2==ValuesBase.DTGLogical))
            return false;
        return true;
        }
    }
