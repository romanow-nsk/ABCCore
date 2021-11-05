package romanow.abc.core.script;
import romanow.abc.core.constants.ConstValue;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.operation.*;
import romanow.abc.core.script.types.*;

import static romanow.abc.core.constants.ValuesBase.*;

import java.util.*;

public class Syntax{
    private Scaner lex;
    private ArrayList<CompileError> errorList = new ArrayList<>();
    private VariableList variables = new VariableList();
    private HashMap<Integer, ConstValue> errorsMap;
    private HashMap<Integer, ConstValue> typesMap;
    private TypeFactory typeFaces = new TypeFactory();
    public Syntax(Scaner lex0) {
        lex = lex0;
        errorsMap = ValuesBase.constMap.getGroupMapByValue("SError");
        typesMap = ValuesBase.constMap.getGroupMapByValue("DType");
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
    */
    // Семантика языка
    int IP;			            // Порядковый номер команды (для генерации меток)
    Lexem LX;			        // Текущая лексема
    void sget(){
        LX=lex.get();
        }
    void error(ScriptException ex){
        error(ex.code,ex.getMessage());
        }
    void error(int code){
        error(code,"");
        }
    void error(int code, String str){
        ConstValue cc = errorsMap.get(code);
        if (cc==null){
            if (code!=SENoCode)
                error(SENoCode);
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
        if (LX.type!='#') error(SENoEOF);
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
        own.setResultType(proto.type());
        sget();
        while (true){
            if (LX.type!='a'){
                error(SENoVarName,LX.value);
                return own;
                }
            TypeFace ff = proto.clone();
            if (variables.get(LX.value)!=null){
                error(SEVarMultiply,LX.value);
                return own;
                }
            variables.add(LX.value,ff);
            sget();
            if (LX.type==';')
                return own;
            if (LX.type=='='){
                sget();
                FunctionCode own1 = L();
                own.add(own1);
                try {
                    ff.setValue(false,typeFaces.getByCode(own.getResultType()));
                    own.addOne(new OperationSave(ff.getVarName()));
                    } catch (ScriptException e) {
                    error(e);
                    }
                }
            if (LX.type==','){
                sget();
                }
            else
            if (LX.type==';'){
                return own;
                }
            else{
                error(SEVarListFormat,LX.value);
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
    case 'S':
            own = createVarList(new TypeShort((short) 0));
            break;
    case 'L':
            own = createVarList(new TypeLong(0));
            break;
    case 'D':
            own = createVarList(new TypeDouble(0));
            break;
    case 'R':
            own = createVarList(new TypeFloat(0));
            break;
    case 'B':
            own = createVarList(new TypeBoolean(false));
            break;
    case 'V':
            own = createVarList(new TypeVoid());
            break;
    case 'a':
            TypeFace var = variables.get(LX.value);
            if (var==null){
                error(SEVarNotDef,LX.value);
                return own;
                }
            sget();
            if (LX.type!='=') error(SELexemLost,"=");
            else sget();
            own=L();
            if (LX.type!=';') error(SELexemLost,";");
            sget();
            try {
                var.setValue(false,typeFaces.getByCode(own.getResultType()));
                own.addOne(new OperationSave(var.getVarName()));
                } catch (ScriptException e) {
                    error(e);
                    }
            break;
    case ';': sget();
            break;
    case '{': sget();
            while(LX.type!='}') own.add(O());
            sget();
            break;
    case 'W': sget();
            if (LX.type!='(') error(SELexemLost,"(");
            sget();
            own1=L();
            i1=own1.size();
            if (LX.type!=')') error(SELexemLost,")");
            sget();
            own2=O();
            i2=own2.size();
            own.add(own1).addOne(new OperationJmpFalse(i2 + 1)).add(own2).addOne(new OperationJmp(-(i2+i1+2)));
            break;
    case 'U': sget();
            if (LX.type!='(') error(SELexemLost,"(");
            sget();
            own1=L();
            i1=own1.size();
            if (LX.type!=')') error(SELexemLost,")");
            sget();
            own2=O();
            i2=own2.size();
            if (LX.type!='E') {
                own.add(own1).addOne(new OperationJmpFalse(i2)).add(own2);
                }
            else{
                sget();
                own3=O();
                i3=own3.size();
                own.add(own1).addOne(new OperationJmpFalse(i2 + 1)).add(own2).addOne(new OperationJmp(i3)).add(own3);
                }
           break;
    default:error(SEIllegalOperator,LX.value);
            break;
            }
    return own; }
//---------------------------------------------------------------
    private boolean testExprType(FunctionCode own,int type){
        int tt = own.getResultType();
        if (tt!=type){
            String tname = typesMap.get(tt).name();
            error(SEIllegalExprDT,own.getResultType()+(tname==null ? "???" : tname) +" "+LX.value);
            return false;
            }
        return true;
        }
//-------------------------------------------------------------
//L::= A | L or A
    private FunctionCode  L(){
        FunctionCode own,own1;
        own=A();
        while(LX.type=='|') {
            sget();
            own1=A();
            if (!testExprType(own1,DTBoolean)){
                return own;
                }
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
            if (!testExprType(own1,DTBoolean)){
                return own;
                }
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
        if (cnt!=0){
            if (!testExprType(own,DTBoolean)){
                return own;
                }
            }
        if (cnt%2==0) return own;
        return own.addOne(new OperationNot());
        }
//-------------------------------------------------------------
//C::= (L) | E<E | EnE | E=E | E>E | EgE | ElE
     private FunctionCode  C(){
        FunctionCode own,own1,op;
        if (LX.type=='('){
            sget();
            own=L();
            if (LX.type!=')') error(SELexemLost,")");
            sget();
            if (own.getResultType()!= DTBoolean){
                error(SEIllegalExprDT,""+typesMap.get(own.getResultType()));
                }
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
        default:    return own;
                }
        sget();
        own1=E();
        if (!own.isResultNumetic() || !own1.isResultNumetic()){
            error(SEIllegalCondition,LX.value);
            return own;
            }
        own.add(own1).addOne(operation);
        own.setResultType(DTBoolean);
        }
     return own; }
//-------------------------------------------------------------
//E::= T | E+T | E-T
    private FunctionCode  E(){
        FunctionCode own,own1,op;
        own=T();
        while(LX.type=='+' || LX.type=='-') {
            char cc = LX.type;
            sget();
            own1=T();
            if (!own.isResultNumetic()){
                error(SEIllegalExprDT,""+typesMap.get(own.getResultType()));
                }
            if (!convertResultTypes(own,own1))
                return own;
            own.add(own1).addOne(cc=='+' ? new OperationAdd() : new OperationSub());
            }
        return own;
        }
//------------------------------------------------------------
    private boolean convertResultTypes(FunctionCode one, FunctionCode two){
        return convertResultTypes(true,one,two);
        }
    private boolean convertResultTypes(boolean onlyNumeric, FunctionCode one, FunctionCode two){
        try {
            one.convertResultTypes(two,onlyNumeric);
            } catch (ScriptException ee){
                error(ee.code, ee.getMessage());
                return false;
                }
        return true;
        }
//-------------------------------------------------------------
//T::= G | T*G | T/G
    private FunctionCode T(){
        int lv2;
        FunctionCode own,own1,op;
        own=G();
        while(LX.type=='*' || LX.type=='/') {
            char cc = LX.type;
            sget();
            own1=G();
            if (!convertResultTypes(own,own1))
                return own;
            own.add(own1).addOne(cc=='*' ? new OperationMul() : new OperationDiv());
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
            if (!convertResultTypes(own,own1))
                return own;
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
    case 'a':   name=LX.value;
                sget();
                if (LX.type=='('){
                    sget();
                    own=E();
                    if (LX.type!=')') error(SELexemLost,")");
                    sget();
                    //-----------------TODO CALL ----------------------------------
                    //own+="CALL "+name+"\n";
                    }
                else{
                    TypeFace var = variables.get(name);
                    if (var==null)
                        error(SEVarNotDef,name);
                    else{
                        own.addOne(new OperationPush(var));
                        own.setResultType(var.type());
                        }
                    }
                break;
    case 'c':       TypeFace vv = LX.value.indexOf(".")==-1 ? new TypeLong(0) : new TypeDouble(0);
                    try {
                        vv.parse(LX.value);
                        own.addOne(new OperationPush(vv));
                        own.setResultType(vv.type());
                        } catch (ScriptException ee){
                            error(SEConstFormat,LX.value);
                            }
                sget();
                break;
    case 'F':   own.addOne(new OperationPush(new TypeBoolean(false)));
                own.setResultType(DTBoolean);
                sget();
                break;
    case 'T':   own.addOne(new OperationPush(new TypeBoolean(true)));
                own.setResultType(DTBoolean);
                sget();
                break;
    case '(':  sget();
               own=E();
               if (LX.type!=')') error(SELexemLost,")");
               sget();
               break;
    default: error(SEIllegalSyntax,LX.value); lex.get(); break;
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
