package romanow.abc.core.script;
import lombok.Getter;
import romanow.abc.core.UniException;
import romanow.abc.core.constants.ConstValue;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.functions.FunctionCall;
import romanow.abc.core.script.operation.*;
import romanow.abc.core.types.*;

import static romanow.abc.core.constants.ValuesBase.*;

import java.util.*;

public class Syntax{
    @Getter private Scaner lex;
    @Getter private ArrayList<CompileError> errorList = new ArrayList<>();
    @Getter private VariableList variables = new VariableList();
    @Getter private HashMap<Integer, ConstValue> errorsMap;
    @Getter private HashMap<Integer, ConstValue> typesMap;
    @Getter private HashMap<String, FunctionCall> functionMap = new HashMap<>();
    @Getter private TypeFactory typeFaces = new TypeFactory();
    @Getter private FunctionCode codeBase;
    public Syntax(Scaner lex0) {
        lex = lex0;
        errorsMap = ValuesBase.constMap.getGroupMapByValue("SError");
        typesMap = ValuesBase.constMap.getGroupMapByValue("DType");
        createFunctionMap();
        }
    public void createFunctionMap(){
        createFunctionMap(constMap.getGroupList("ScriptFun"));
        }
    public void createFunctionMap(ArrayList<ConstValue> list){
        functionMap.clear();
        for(ConstValue cc : list) {
            String fClassName = "romanow.abc.core.script.functions." + cc.className();
            try {
                Class cls = Class.forName(fClassName);
                Object oo = cls.newInstance();
                if (!(oo instanceof FunctionCall)){
                    error(SECreateFunctionBug,"Класс функции "+ cc.title()+" не поддерживает интерфейс FunctionCall");
                    }
                FunctionCall fun =(FunctionCall) oo;
                functionMap.put(cc.title(),fun);
                }  catch (Exception ee){
                    error(SECreateFunctionBug,"Ошибка класса функции "+ cc.title()+" "+ee.toString());
                    }
                }
            }
    /*---------------------------------------- Грамматика
    Z::= S#
    O::= a(Y); | a=E; | i(L)OX |  l(L)O | {S} | V P | ;
    P::= aGR
    R::= ; | ,P
    G:: пусто | =c
    M::= M,J
    J::= s | E | b
    X::= пусто | eO
    S::= пусто | OS
    L::= A | L or A | L xor A
    A::= B | A and B
    B::= C | not B
    C::= (L) | E<E | E=E | E>E | EgE | ElE
    E::= T | E+T | E-T
    T::= U | T*U | T/U
    U:: N | N**N
    N:: K , N|K, N^K
    K:: I | K&I
    I:: F | !I
    F::= DaY | Dc | D(E)
    D::= пусто | -        - унарный минус
    Y::= пусто | (H)      - вызов функции
    H::= пусто | EW
    W::= пусто | ,EW
    */
    // Семантика языка
    int IP;			            // Порядковый номер команды (для генерации меток)
    Lexem LX;			        // Текущая лексема
    void sget(){
        LX=lex.get();
        }
    void error(UniException ex){
        error(SEDataType,ex.getMessage());
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
    public FunctionCode  S(){
        FunctionCode own=new FunctionCode();
        while(LX.type!='#') own.add(O());
        return own; }

//------------ Определение переменных -----------------------------
//  P::= aGR
//  R::= ; | ,P
    public FunctionCode createVarList(TypeFace proto){
        FunctionCode own = new FunctionCode();
        own.setResultType(proto.getType());
        sget();
        while (true){
            if (LX.type!='a'){
                error(SENoVarName,LX.value);
                return own;
                }
            TypeFace ff=null;
            try {
                ff = proto.cloneVar();
                } catch (ScriptException ee){
                    error(SEBug,LX.value+" "+ee.toString());
                    return own;
                    }
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
                if (!ff.isSetEnable(own.getResultGroup())){
                    error(SEIllegalTypeConvertion,"Недопустимое присваивание "+ff.getTypeName()+"="+ own.getResultGroupName());
                    return own;
                    }
                own.addOne(new OperationSave(ff.getVarName()));
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
//----------------- Оператор/определение -----------------------------------
//  O::= a(Y); | a=E; | i(L)OX |  l(L)O | {S} | V P | ;
//  X::= пусто | eO
//  H::= пусто | EW
//  W::= пусто | ,EW
    public FunctionCode  O(){
        int k,lv,m1,m2,i1,i2,i3;
        FunctionCode own = new FunctionCode();
        FunctionCode own1,own2,own3;
        switch (LX.type){
    case 'C':
            own = createVarList(new TypeString(""));
            break;
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
            Lexem name= LX;
            sget();
            if (LX.type=='='){          // Присваивание
                sget();
                TypeFace var = variables.get(name.value);
                own = L();
                if (LX.type!=';') error(SELexemLost,";");
                sget();
                if (var==null){
                    error(SEVarNotDef,name.value);
                    }
                else{
                    if (!var.isSetEnable(own.getResultGroup())){
                        error(SEIllegalTypeConvertion,"Недопустимое присваивание "+var.getTypeName()+"="+ own.getResultGroupName());
                        return own;
                        }
                    own.addOne(new OperationSave(var.getVarName()));
                    }
                return own;
                }
            else
            if (LX.type=='('){
                sget();
                FunctionCode code = procFunctionCall(name.value);
                own.add(code);
                own.setResultType(code.getResultType());
                }
            else{
                error(SEIllegalSymbol,LX.value);
                sget();
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
public FunctionCode procFunctionCall(String funName){
    boolean bad=false;
    FunctionCode out = new FunctionCode();
    ArrayList<FunctionCode> paramsCode = new ArrayList<>();
    if (LX.type==')')
        sget();
    else{
        while (true) {
            FunctionCode own = L();
            paramsCode.add(own);
            if (LX.type == ')') {
                sget();
                break;
                }
            else
            if (LX.type==',')
               sget();
            else{
                error(SEIllegalSymbol,LX.value);
                error(SEIllegalFunSyntax,LX.value);
                bad = true;
                break;
                }
            }
        }
    //----------- Вызов функции = часть выражения
    //if (LX.type!=';'){
    //    error(SEIllegalSymbol,LX.value);
    //    error(SEIllegalFunSyntax,LX.value);
    //    }
    if (bad)
        return out;
    FunctionCall fun = functionMap.get(funName);
    if (fun==null){
        error(SEFunNotDefined,LX.value);
        return out;
        }
    int types[] = fun.getParamTypes();
    for(int i=0; i<paramsCode.size();i++){
        FunctionCode ff = paramsCode.get(i);
        int resGroup = DTGroup[types[i]];
        if (!ff.isSetEnableByType(types[i])){
            error(SEIllegalTypeConvertion,"Функция "+ funName+": несовпадение типов для "+i+" параметра "+ff.getResultType()+"-"+typesMap.get(types[i]));
            }
        if (resGroup!=ff.getResultGroup()){
            ff.addOne(new OperationConvertType(resGroup));
            ff.setResultTypeByGroup(resGroup);
            }
        out.add(ff);
        }
    out.add(new OperationCall(funName));
    out.setResultType(fun.getResultType());         // Тип результата
    return out;
    }
//---------------------------------------------------------------
    public boolean testExprGroup(FunctionCode own,int type){
        int tt = own.getResultGroup();
        if (tt!=type){
            error(SEIllegalExprDT,own.getResultType()+ValuesBase.DTGroupNames[type] +" "+LX.value);
            return false;
            }
        return true;
        }
//-------------------------------------------------------------
//L::= A | L or A | L xor A
    public FunctionCode  L(){
        FunctionCode own,own1;
        own=A();
        while(LX.type=='|' || LX.type=='o') {
            boolean or = LX.type=='|';
            sget();
            own1=A();
            if (!testExprGroup(own1,DTGLogical)){
                return own;
                }
            own.add(own1).addOne(or ? new OperationOr() : new OperationXor());
            }
        return own; }
//-------------------------------------------------------------
//A::= B | A and B
    public FunctionCode  A(){
    FunctionCode own,own1;
        own=B();
        while(LX.type=='a') {
            sget();
            own1=B();
            if (!testExprGroup(own1,DTGLogical)){
                return own;
                }
            own.add(own1).addOne(new OperationAnd());
            }
        return own; }
//-------------------------------------------------------------
//B::= C | not B
    public FunctionCode B(){
        int cnt=0;
        FunctionCode own;
        while(LX.type=='t') { sget();cnt++; }
        own=C();
        if (cnt!=0){
            if (!testExprGroup(own,DTGLogical)){
                return own;
                }
            }
        if (cnt%2==0) return own;
        return own.addOne(new OperationNot());
        }
    //-------------------------------------------------------------------------------------------------
    public void setResultTypeForBinary(FunctionCode own,FunctionCode own1){
        int commonGroup = TypeFace.getCommonGroup(own.getResultGroup(),own1.getResultGroup());
        if (commonGroup==-1) {
            error(ValuesBase.SEIllegalTypeConvertion, "Недопустимое сочетание типов  " + own.getResultType() + "<>" + own1.getResultType());
            }
        //-------------- Команды приведения не нужны, только следить........
        own.add(own1);
        own.setResultTypeByGroup(commonGroup);
        }
//-------------------------------------------------------------
// N:: K , N|K, N^K
    public FunctionCode  N(){
        FunctionCode own,own1;
        own=K();
        while(LX.type=='|' || LX.type=='^') {
            boolean or = LX.type=='|';
            sget();
            own1=K();
            if (!testExprGroup(own,DTGInteger) || !testExprGroup(own1,DTGInteger)){
                return own;
                }
            setResultTypeForBinary(own,own1);
            own.addOne(or ? new OperationOrWord() : new OperationXorWord());
            }
        return own; }
//-------------------------------------------------------------
// K:: I | K&I
    public FunctionCode  K(){
        FunctionCode own,own1;
        own=I();
        while(LX.type=='&') {
            sget();
            own1=I();
            if (!testExprGroup(own,DTGInteger) || !testExprGroup(own1,DTGInteger)){
                return own;
                }
            setResultTypeForBinary(own,own1);
            own.addOne(new OperationAndWord());
            }
        return own;
        }
//-------------------------------------------------------------
//I::= F | !I
    public FunctionCode I(){
        int cnt=0;
        FunctionCode own;
        while(LX.type=='!') { sget();cnt++; }
        own=F();
        if (cnt!=0){
            if (!testExprGroup(own,DTGInteger)){
                return own;
                }
            }
        if (cnt%2==0) return own;
        return own.addOne(new OperationNotWord());
        }
//-------------------------------------------------------------
// C::= (L) | E<E | EnE | E=E | E>E | EgE | ElE
     public FunctionCode  C(){
        FunctionCode own,own1,op;
        if (LX.type=='('){
            sget();
            own=L();
            if (LX.type!=')') error(SELexemLost,")");
            sget();
            //-------------------- TODO - грамматика для boolean и numeric ------------------
            //if (own.getResultType()!= DTBoolean){
            //    error(SEIllegalExprDT,""+typesMap.get(own.getResultType()));
            //    }
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
    public FunctionCode  E(){
        FunctionCode own,own1,op;
        own=T();
        while(LX.type=='+' || LX.type=='-') {
            char cc = LX.type;
            sget();
            own1=T();
            setResultTypeForBinary(own,own1);
            //if (!own.isResultNumetic()){
            //    error(SEIllegalExprDT,""+typesMap.get(own.getResultType()));
            //    }
            //if (!convertResultTypes(own,own1))
            //    return own;
            own.addOne(cc=='+' ? new OperationAdd() : new OperationSub());
            }
        return own;
        }
//------------------------------------------------------------
    public boolean convertResultTypes(FunctionCode one, FunctionCode two){
        return convertResultTypes(true,one,two);
        }
    public boolean convertResultTypes(boolean onlyNumeric, FunctionCode one, FunctionCode two){
        try {
            int resType = FunctionCode.convertResultTypes(one.getResultType(),two.getResultType(),onlyNumeric);
            one.setResultType(resType);
            } catch (ScriptException ee){
                error(ee.code, ee.getMessage());
                return false;
                }
        return true;
        }
//-------------------------------------------------------------
//T::= U | T*U | T/U
    public FunctionCode T(){
        int lv2;
        FunctionCode own,own1,op;
        own=U();
        while(LX.type=='*' || LX.type=='/') {
            char cc = LX.type;
            sget();
            own1=U();
            setResultTypeForBinary(own,own1);
            //if (!convertResultTypes(own,own1))
            //    return own;
            own.addOne(cc=='*' ? new OperationMul() : new OperationDiv());
            }
        return own;
        }
//-------------------------------------------------------------
// U::= N | N ** N
    public FunctionCode  U(){
        int lv2;
        FunctionCode own,own1,op;
        own=N();
        if(LX.type=='p') {
            sget();
            own1=N();
            setResultTypeForBinary(own,own1);
            //if (!convertResultTypes(own,own1))
            //    return own;
            own.addOne(new OperationPow());
            }
        return own;
    }

//---------------------------------------------------------------------------
//  F::= DaY | Dc | D(E)
//  D::= пусто | -        - унарный минус
//  Y::= пусто | (E)      - вызов функции с одним аргументом
    public FunctionCode  F(){
        FunctionCode own=new FunctionCode();
        String name;
        int k;
        boolean minus=false;
        if (LX.type=='-'){
            minus=true; sget(); }
        switch (LX.type){
    case 's':   own.addOne(new OperationPush(new TypeString(LX.value.substring(1,LX.value.length()-1))));
                own.setResultType(DTString);
                sget();
                return own;
    case 'a':   name=LX.value;
                sget();
                if (LX.type=='('){
                    sget();
                    FunctionCode code = procFunctionCall(name);
                    own.add(code);
                    own.setResultType(code.getResultType());
                    }
                else{
                    TypeFace var = variables.get(name);
                    if (var==null)
                        error(SEVarNotDef,name);
                    else{
                        own.addOne(new OperationPush(var));
                        own.setResultType(var.getType());
                        }
                    }
                break;
    case 'c':       TypeFace vv = LX.value.indexOf(".")==-1 ? new TypeLong(0) : new TypeDouble(0);
                    try {
                        vv.parse(LX.value);
                        own.addOne(new OperationPush(vv));
                        own.setResultType(vv.getType());
                        }
                    catch (ScriptException ee){ error(ee); }
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
        FunctionCode own1 = new FunctionCode();
        own1.addOne(new OperationPush(new TypeInt(0)));
        own1.setResultType(DTInt);
        setResultTypeForBinary(own1,own);
        own1.addOne(new OperationSub());
        own = own1;
        }
    return own;
    }
    //--------------------------------------------------------------------
    public FunctionCode compile() {
       sget();
       codeBase =  Z();
       return codeBase;
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
        CallContext context = new CallContext(SS,null);
        try {
            context.call(false);
            } catch (ScriptException ee){
                System.out.println(ee.toString());
                }
        System.out.println(context.getTraceList());
        }
   }
}
