package romanow.abc.core.script;
import romanow.abc.core.constants.ConstValue;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.operation.*;
import romanow.abc.core.script.types.TypeDouble;
import romanow.abc.core.script.types.TypeFace;
import romanow.abc.core.script.types.TypeInt;
import romanow.abc.core.script.types.TypeLong;
import static romanow.abc.core.constants.ValuesBase.*;

import java.io.*;
import java.util.*;

public class Syntax{
    private Scaner lex;
    private ArrayList<CompileError> errorList = new ArrayList<>();
    private VariableList variables = new VariableList();
    private HashMap<Integer, ConstValue> errorsMap;
    public Syntax(Scaner lex0) {
        lex = lex0;
        errorsMap = ValuesBase.constMap.getGroupMapByValue("SCError");
        }
    /*---------------------------------------- Грамматика
    Z::= S#
    O::= a=E; | i(L)OX |  l(L)O | {S} | V P
    P::= aGX
    X::= ; | ,P
    G:: пусто | =c
    M::= M,J
    J::= s | E | b
    X::= пусто | eO
    S::= пусто | OS
    L::= A | L | A
    A::= B | A & B
    B::= C | !C
    C::= (L) | E<E | E=E | E>E | EgE | ElE
    E::= T | E+T | E-T
    T::= F | T*F | T/F
    F::= DaY | Dc | D(E)
    D::= пусто | -        - унарный минус
    Y::= пусто | (E)      - вызов функции с одним аргументом

   Семантика стековой машины машины, операции
   LOADM a		- загрузка переменной в стек (номер)
   LOADC c		- загрузка константы в стек (значение)
   SAVE  a		- выталкивание из стека в переменную
   ADD			- операции - оба операнда в стеке - результат там же
   SUB,MUL,DIV,POW
    */
    // Семантика языка
    int IP;			// Порядковый номер команды (для генерации меток)
    Lexem LX;			// Текущая лексема
    int size(String ss){        // Длина текущего блока - количество строк кода
        int i,n=ss.length(),ns=0;
        for (i=0;i<n;i++) if (ss.charAt(i)=='\n') ns++;
        return ns;
        }
    void sget(){
        LX=lex.get();
        }
    void error(int code){
        error(code,"");
        }
    void error(int code, String str){
        ConstValue cc = errorsMap.get(code);
        if (cc==null){
            if (code!=SCENoCode)
                error(SCENoCode);
            errorList.add(new CompileError(code, SEModeInfo,LX,str));
            }
        else {
            errorList.add(new CompileError(code, SEModeInfo, LX, cc.title()+": "+str));
            }
        sget();
        }
//-------------------------------------------------------------
// Z::= S#
    FunctionCode Z(){
        FunctionCode own;
        own=S();
        if (LX.type!='#') error(SCENoEOF);
        return own; }
//-------------------------------------------------------------
//S::=  O | S;O
    private FunctionCode  S(){
        FunctionCode own=new FunctionCode();
        while(LX.type!='#') own.add(O());
        return own; }

//-------------------------------------------------------------
//  O::= a=E; | ra; | wE; | i(L)OX |  l(L)O | {S} | ;
//  X::= пусто | eO
    private FunctionCode createVarList(TypeFace proto){
        FunctionCode own = new FunctionCode();
        sget();
        while (true){
            if (LX.type!='a'){
                error(SCENoVarName,LX.value);
                return own;
                }
            TypeFace ff = proto.clone();
            if (variables.get(LX.value)!=null){
                error(SCEVarMultiply,LX.value);
                return own;
                }
            variables.add(LX.value,ff);
            sget();
            if (LX.type==';')
                return own;
            if (LX.type=='='){
                sget();
                own.add(E());
                }
            if (LX.type==','){
                sget();
                }
            else
            if (LX.type==';'){
                return own;
                }
            else{
                error(SCEVarListFormat,LX.value);
                }
            }
        }
    private FunctionCode  O(){
        int k,lv,m1,m2,i1,i2,i3;
        FunctionCode own = new FunctionCode();
        FunctionCode own1,own2,own3;
        switch (LX.type){
    case 'I':
            own = createVarList(new TypeInt(0));
            break;
    case 'D':
            own = createVarList(new TypeDouble(0));
            break;
    case 'a':
            TypeFace var = variables.get(LX.value);
            if (var==null){
                error(SCEVarNotDef,LX.value);
                return own;
                }
            sget();
            if (LX.type!='=') error(SCELexemLost,"=");
            else sget();
            own=E();
            if (LX.type!=';') error(SCELexemLost,";");
            sget();
            // TODO ---------------------------- SET
            //own.+="SAVE "+k+"\n";
            break;
    case ';': sget();
            break;
    case '{': sget();
            while(LX.type!='}') own.add(O());
            sget();
            break;
    case 'l': sget();
            if (LX.type!='(') error(SCELexemLost,"(");
            sget();
            own1=L();
            i1=own1.size();
            if (LX.type!=')') error(SCELexemLost,")");
            sget();
            own2=O();
            i2=own2.size();
            own.add(own1).addOne(new OperationJmpFalse(i2+1)).add(own2).addOne(new OperationJmp(-i1-i2-2));
            break;
    case 'w': sget();
            if (LX.type!='(') error(SCELexemLost,"(");
            sget();
            own1=L();
            i1=own1.size();
            if (LX.type!=')') error(SCELexemLost,")");
            sget();
            own2=O();
            i2=own2.size();
            own.add(own1).addOne(new OperationJmpFalse(i2 + 1)).add(own2).addOne(new OperationJmp(-(i2+i1+1)));
            break;
    case 'i': sget();
            if (LX.type!='(') error(SCELexemLost,"(");
            sget();
            own1=L();
            i1=own1.size();
            if (LX.type!=')') error(SCELexemLost,")");
            sget();
            own2=O();
            i2=own2.size();
            if (LX.type!='e') {
                own.add(own1).addOne(new OperationJmpFalse(i2)).add(own2);
                }
            else{
                sget();
                own3=O();
                i3=own3.size();
                own.add(own1).addOne(new OperationJmpFalse(i2 + 1)).add(own2).addOne(new OperationJmp(i3)).add(own3);
                }
           break;
    default:error(SCEIllegalOperator,LX.value);
            break;
            }
    return own; }
//-------------------------------------------------------------
//L::= A | L or A
    private FunctionCode  L(){
        FunctionCode own,own1;
        own=A();
        while(LX.type=='|') {
            sget();
            own1=A();
            own.add(own1).addOne(new OperationOr());
            }
        return own; }
//-------------------------------------------------------------
//A::= B | A & B
    private FunctionCode  A(){
    FunctionCode own,own1;
        own=B();
        while(LX.type=='&') {
            sget();
            own1=B();
            own.add(own1).addOne(new OperationAnd());
            }
        return own; }
//-------------------------------------------------------------
//B::= C | !C
    private FunctionCode B(){
        int cnt=0;
        FunctionCode own;
        while(LX.type=='!') { sget();cnt++; }
        own=C();
        if (cnt==0) return own;
        return own.addOne(new OperationNot());
        }
//-------------------------------------------------------------
//C::= (L) | E<E | EnE | E=E | E>E | EgE | ElE
     private FunctionCode  C(){
        FunctionCode own,own1,op;
        if (LX.type=='('){
            sget();
            own=L();
            if (LX.type!=')') error(SCELexemLost,")");
            sget();
            }
        else{
            own=E();
            Operation operation;
            switch(LX.type){
        case '<': operation = new OperationLT(); break;			// Команда проверки условия
        case '=': operation = new OperationEQ(); break;
        case 'n': operation = new OperationNE(); break;
        case '>': operation = new OperationGT(); break;
        case 'g': operation = new OperationGE(); break;
        case 'l': operation = new OperationLE(); break;
        default:  error(SCEIllegalCondition,LX.value);
                    return own;
                }
        sget();
        own1=E();
        own.add(own1).addOne(operation);
        }
     return own; }
//-------------------------------------------------------------
//E::= T | E+T | E-T
    private FunctionCode  E(){
        FunctionCode own,own1,op;
        own=T();
        while(LX.type=='+' || LX.type=='-') {
            sget();
            own1=T();
            own.add(own1).addOne(LX.type=='+' ? new OperationAdd() : new OperationSub());
            }
        return own;
        }
//-------------------------------------------------------------
//T::= G | T*G | T/G
    private FunctionCode T(){
        int lv2;
        FunctionCode own,own1,op;
        own=G();
        while(LX.type=='*' || LX.type=='/') {
            sget();
            own1=G();
            own.add(own1).addOne(LX.type=='*' ? new OperationMul() : new OperationDiv());
            }
    return own; }
//-------------------------------------------------------------
// G::= F | F ** F
    private FunctionCode  G(){
        int lv2;
        FunctionCode own,own1,op;
        own=F();
        if(LX.type=='p') {
            sget();
            own1=F();
            own.add(own1).addOne(new OperationPow());
        }
        return own;
    }

//---------------------------------------------------------------------------
//  F::= DaY | Dc | D(E)
//  D::= пусто | -        - унарный минус
//  Y::= пусто | (E)      - вызов функции с одним аргументом
    private FunctionCode  F(){
        FunctionCode own=new FunctionCode();
        String name;
        int k;
        boolean minus=false;
        if (LX.type=='-'){ minus=true; sget(); }
        switch (LX.type){
    case 'a':  name=LX.value;
               sget();
               if (LX.type=='('){
                   sget();
                   own=E();
                   if (LX.type!=')') error(SCELexemLost,")");
                   sget();
                   //-----------------TODO CALL ----------------------------------
                   //own+="CALL "+name+"\n";
                   }
               else{
                   TypeFace var = variables.get(name);
                   if (var==null)
                        error(SCEVarNotDef,name);
                   else
                        own.addOne(new OperationPush(var.clone()));
                   }
               break;
    case 'c':  if (LX.value.indexOf(".")==-1){
                   TypeFace vv = LX.value.indexOf(".")==-1 ? new TypeLong(0) : new TypeDouble(0);
               try {
                   vv.parse(LX.value);
                   own.addOne(new OperationPush(vv));
                   } catch (ScriptRunTimeException ee){
                       error(SCEConstFormat,LX.value);
                        }
                    }
               sget();
               break;
    case '(':  sget();
               own=E();
               if (LX.type!=')') error(SCELexemLost,")");
               sget();
               break;
    default: error(SCEIllegalSyntax,LX.value); lex.get(); break;
             }
    if (minus) {
        FunctionCode xx = new FunctionCode();
        xx.addOne(new OperationPush(new TypeInt(0))).add(own).addOne(new OperationSub());
        }
    return own;
    }
//--------------------------------------------------------------------
    FunctionCode compile() {
       sget();
       return Z();
       }
//-----------------------------------
public static void main(String[] args) throws ScriptException {
    ValuesBase.init();
    Scaner lex = new Scaner();
    boolean bb=lex.open("Input01.txt");
    Syntax SS=new Syntax(lex);
    FunctionCode ff = SS.compile();
    System.out.print(ff);
    System.out.println("errors: "+SS.errorList.size());
    for(CompileError error : SS.errorList)
        System.out.println(error);
    System.out.println(SS.variables);
    if (SS.errorList.size()==0){
        CallContext context = new CallContext(ff,SS.variables);
        context.call(true);
        }
   }
}
