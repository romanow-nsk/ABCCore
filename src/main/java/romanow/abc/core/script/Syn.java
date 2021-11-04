package romanow.abc.core.script;
import java.io.*;
import java.util.*;

public class Syn extends Scaner {
    public Syn() {
    }
    /*---------------------------------------- Грамматика
    Z::= S#
    O::= a=E; | ra; | wM; | i(L)OX |  l(L)O | {S} | ;
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
    Vector var=new Vector();	// Таблица переменных - индекс=адрес
    int IP;			// Порядковый номер команды (для генерации меток)
    String story,err;           // Текст ошибок
    int nerror;                 // Кол-во ошибок
    BufferedWriter out;
    Lexem LX;			// Текущая лексема
    int size(String ss){        // Длина текущего блока - количество строк кода
        int i,n=ss.length(),ns=0;
        for (i=0;i<n;i++) if (ss.charAt(i)=='\n') ns++;
        return ns;
    }
    int getVar(String ss){
        int i,n=var.size();
        for (i=0;i<n;i++)
            if (ss.compareTo((String)var.get(i))==0) return i;
        var.add(ss);
        return n;
    }
    void sget(){
        LX=super.get();
        err+=LX.value;
        }
    void close() {
        }
    boolean open(String nm) {
        IP=0;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nm+"_bin.txt"), "windows-1251"));
            }   catch (Exception ee){ return false;}
            return super.open(nm+".txt");
       }
    void error(int code, String str){
        try {
            err+=LX.value+" [ Ошибка: "+str+" ]\n";
            story+=err;
            err="";
            nerror++;
            sget();
        }   catch (Exception ee){}
    }
//-------------------------------------------------------------
// Z::= S#
    String Z(){
        err="";story="";
        nerror=0;
        String own;
        own=S();
        if (LX.type!='#') error(1,"Не найден конец текста");
        return own; }
//-------------------------------------------------------------
//S::=  O | S;O
    String  S(){
        String own="";
        while(LX.type!='#') own+=O();
        return own; }
//-------------------------------------------------------------
//  O::= a=E; | ra; | wE; | i(L)OX |  l(L)O | {S} | ;
//  X::= пусто | eO
    String  O(){
        int k,lv,m1,m2,i1,i2,i3;
        String own="",own1,own2,own3,op,lop;
        err="";
        switch (LX.type){
    case 'a':
            k=getVar(LX.value);
            sget();
            if (LX.type!='=') error(4,"Пропущен =");
            else sget();
            own=E();
            if (LX.type!=';') error(10,"Пропущен ;");
            sget();
            own+="SAVE "+k+"\n";
            break;
    case 'r': sget();
            if (LX.type!='a') error(4,"Пропущего имя");
            k=getVar(LX.value);
            sget();
            if (LX.type!=';') error(10,"Пропущен ;");
            sget();
            own="IN "+k+"\n";
            break;
    case 'w': sget();
            own="";
            while (true){
                if (LX.type == 'b') { sget(); own+="ENDL\n"; }
                else
                if (LX.type == 's') {
                    own+="PUT " + LX.value.substring(1,LX.value.length()-1) + "\n";
                    sget();
                } else own+=E() + "OUT\n";
                if (LX.type==';') { sget(); break; }
                if (LX.type==',') { sget(); continue; }
                error(5,"Ошибка списка параметров");
                break;
            }
            break;
    case ';': sget();
            break;
    case '{': sget();
            while(LX.type!='}') own+=O();
            sget();
            break;
    case 'l': sget();
            if (LX.type!='(') error(5,"Пропущен (");
            sget();
            own1=L();
            i1=size(own1);
            if (LX.type!=')') error(6,"Пропущен )");
            sget();
            own2=O();
            i2=size(own2);
            own=own1+"JNOT "+(i2+1)+"\n"+own2+"JMP "+(-i1-i2-2)+"\n";
            break;
    case 'i': sget();
            if (LX.type!='(') error(5,"Пропущен (");
            sget();
            own1=L();
            i1=size(own1);
            if (LX.type!=')') error(6,"Пропущен )");
            sget();
            own2=O();
            i2=size(own2);
            if (LX.type!='e') {
                own=own1+"JNOT "+i2+"\n"+own2;
                }
            else{
                sget();
                own3=O();
                i3=size(own3);
                own=own1+"JNOT "+(i2 + 1)+"\n"+own2+"JMP "+i3+"\n"+own3;
                }
           break;
    default:error(2,"Недопустимый оператор");
            break;
            }
    return own; }
//-------------------------------------------------------------
//L::= A | L or A
    String  L(){
        String own,own1;
        own=A();
        while(LX.type=='|') {
            sget();
            own1=A();
            own+=own1+"OR\n";
            }
        return own; }
//-------------------------------------------------------------
//A::= B | A & B
    String  A(){
        String own,own1;
        own=B();
        while(LX.type=='&') {
            sget();
            own1=B();
            own+=own1+"AND\n";
            }
        return own; }
//-------------------------------------------------------------
//B::= C | !C
    String  B(){
        int cnt=0;
        String own;
        while(LX.type=='!') { sget();cnt++; }
        own=C();
        if (cnt==0) return own;
        return own+"NOT\n"; }
//-------------------------------------------------------------
//C::= (L) | E<E | EnE | E=E | E>E | EgE | ElE
     String  C(){
        String own,own1,op;
        if (LX.type=='('){
            sget();
            own=L();
            if (LX.type!=')') error(6,"Пропущен )");
            sget();
            }
        else{
            own=E();
            switch(LX.type){
        case '<': op="LT"; break;			// Команда проверки условия
        case '=': op="EQ"; break;
        case 'n': op="NE"; break;
        case '>': op="GT"; break;
        case 'g': op="GE"; break;
        case 'l': op="LE"; break;
        default:  error(9,"Неправильное условие"); return own;
        }
        sget();
        own1=E();
        own+=own1+"SUB\n"+op+"\n";
        }
     return own; }
//-------------------------------------------------------------
//E::= T | E+T | E-T
    String  E(){
        String own,own1,op;
        own=T();
        while(LX.type=='+' || LX.type=='-') {
            if (LX.type=='+') op="ADD"; else op="SUB";
            sget();
            own1=T();
            own=own+own1+op+"\n";
            }
        return own;
        }
//-------------------------------------------------------------
//T::= G | T*G | T/G
    String  T(){
        int lv2;
        String own,own1,op;
        own=G();
        while(LX.type=='*' || LX.type=='/') {
            if (LX.type=='*') op="MUL"; else op="DIV";
            sget();
            own1=G();
            own+=own1+op+"\n";
        }
    return own; }
//-------------------------------------------------------------
// G::= F | F ** F
    String  G(){
        int lv2;
        String own,own1,op;
        own=F();
        if(LX.type=='p') {
            sget();
            own1=F();
            own+=own1+"POW\n";
        }
        return own;
    }

//---------------------------------------------------------------------------
//  F::= DaY | Dc | D(E)
//  D::= пусто | -        - унарный минус
//  Y::= пусто | (E)      - вызов функции с одним аргументом
    String  F(){
        String own="",own1,name;
        int k;
        boolean minus=false;
        if (LX.type=='-'){ minus=true; sget(); }
        switch (LX.type){
    case 'a':  name=LX.value;
               sget();
               if (LX.type=='('){
                   sget();
                   own=E();
                   if (LX.type!=')') error(6,"Пропущен )");
                   sget();
                   own+="CALL "+name+"\n";
                   }
               else{
                   k = getVar(name); // Добавление к таблице переменных - индекс в ней
                   own = "LOADM " + k + "\n";
                   }
               break;
    case 'c':  own="LOADC "+LX.value+"\n";
               sget();
               break;
    case '(':  sget();
               own=E();
               if (LX.type!=')') error(6,"Пропущен )");
               sget();
               break;
    default: error(10,"Недопустимый терм"); get(); break;
             }
    if (minus) own="LOADC 0\n"+own+"SUB\n";
    return own;
    }
//--------------------------------------------------------------------
    void compile(String nm) {
        String ss;
        if (open(nm)){
            try {
                sget();
                ss=Z();
                char c[]=ss.toCharArray();
                int kk=c.length;
                for (int i=0;i<kk;i++)
                    if (c[i]=='\n') out.newLine();
                    else out.write(c[i]);
                close();
            } catch(IOException ee){}
        }
        }
//-----------------------------------
public static void main(String[] args) {
    Syn SS=new Syn();
    SS.compile("Input01");
    Exec exec = new Exec();
    exec.load("Input01_bin.txt");
   }
}
