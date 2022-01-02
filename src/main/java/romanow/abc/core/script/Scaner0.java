package romanow.abc.core.script;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

//----------------------- Лексический анализатор ---------------------------------
public class Scaner0 {
    private ArrayList<String> lines = new ArrayList();
    private String str;
    private int idx=0;
    private int lineIdx=0;
    final private  int	TBL[][]= {            // Матрица переходов КА
            { -100, 0, 1, 2, 2, -11, -12, -16, 3, 4, -10, -13, -14, 5, -4, -15, 0, -19, -20, -21, 7},
            {-2,-2, 1, -2, 1, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, 6, -2, -2, -2, -2},
            {-1,-1, 2, 2, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {-5,-5,-5, -5, -5, -5, -5, -7, -5, -17, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5},
            {-6,-6,-6, -6, -6, -6, -6, -8, -6, -6, -6, -6, -6, -6, -6, -6, -6, -6, -6, -6, -6},
            {-9,-9,-9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -18, -9, -9, -9, -9, -9, -9, -9},
            {-2,-2, 6, -2, 6, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
            { 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8},
            {-22,-22, -22, -22, -22, -22, -22, -22, -22, -22, -22, -22, -22, -22, -22, -22, -22, -22, -22, -22, 7}
            };
    //          0 1 2 3 4 5 6 7 8 9 10  12  14  16  18  20  22
    int back[]={0,1,1,0,0,1,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1};
    final private HashMap<Character,Lexem> lexems=new HashMap<>();
    final private HashMap<String,Lexem> keywords=new HashMap<>();
    final private ArrayList<Lexem> finStates = new ArrayList<>();
    public Scaner0(){
        Lexem lexem;
        lexem = new Lexem('z',"illegal");
        finStates.add(lexem);       //0
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('a',"ident");
        finStates.add(lexem);       //1
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('c',"const");
        finStates.add(lexem);       //2
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('?',"comment");
        finStates.add(lexem);       //3
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('/',"/");
        finStates.add(lexem);       //4
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('<',"<");
        finStates.add(lexem);       //5
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('>',">");
        finStates.add(lexem);       //6
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('l',"<=");
        finStates.add(lexem);       //7
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('g',">=");
        finStates.add(lexem);       //8
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('*',"*");
        finStates.add(lexem);       //9
        lexems.put(lexem.type,lexem);
        lexem = new Lexem(';',";");
        finStates.add(lexem);       //10
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('(',"(");
        finStates.add(lexem);       //11
        lexems.put(lexem.type,lexem);
        lexem = new Lexem(')',")");
        finStates.add(lexem);       //12
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('+',"+");
        finStates.add(lexem);       //13
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('-',"-");
        finStates.add(lexem);       //14
        lexems.put(lexem.type,lexem);
        lexem = new  Lexem('#',"EOF");
        finStates.add(lexem);       //15
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('=',"=");
        finStates.add(lexem);       //16
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('n',"<>");
        finStates.add(lexem);       //17
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('p',"**");
        finStates.add(lexem);       //18
        lexems.put(lexem.type,lexem);
        lexem = new Lexem(',',",");
        finStates.add(lexem);       //19
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('{',"{");
        finStates.add(lexem);       //20
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('}',"}");
        finStates.add(lexem);       //21
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('s',"string");
        finStates.add(lexem);       //22
        lexems.put(lexem.type,lexem);
        //-------------------- Ключевые слова -----------------------------------
        lexem = new Lexem('U',"if");
        lexems.put(lexem.type,lexem);
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('E',"else");
        lexems.put(lexem.type,lexem);
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('W',"while");
        lexems.put(lexem.type,lexem);
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('&',"and");
        lexems.put(lexem.type,lexem);
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('|',"or");
        lexems.put(lexem.type,lexem);
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('!',"not");
        lexems.put(lexem.type,lexem);
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('I',"int");
        lexems.put(lexem.type,lexem);
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('C',"char");
        lexems.put(lexem.type,lexem);
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('S',"short");
        lexems.put(lexem.type,lexem);
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('L',"long");
        lexems.put(lexem.type,lexem);
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('V',"void");
        lexems.put(lexem.type,lexem);
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('R',"float");
        lexems.put(lexem.type,lexem);
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('D',"double");
        lexems.put(lexem.type,lexem);
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('B',"boolean");
        lexems.put(lexem.type,lexem);
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('T',"true");
        lexems.put(lexem.type,lexem);
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('F',"false");
        lexems.put(lexem.type,lexem);
        keywords.put(lexem.value,lexem);
        }
    boolean open(String nm) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(nm), "Windows-1251"));
            lines.clear();
            String ss;
            while ((ss=reader.readLine())!=null)
                lines.add(ss);
            lines.add("#");
            reader.close();
            idx=0;
            lineIdx=0;
            str = lines.get(lineIdx);
            }   catch (Exception ee){ return false;}
    return true;
    }

Lexem get(){
    String s="";
    int st = 0;
    char cc;
    while (st >= 0) {              // Пока не достигнуто конечное состояние
        if (st == 0) s="";         // Накопление лексемы с 0-го состояния
        if (idx>=str.length()){
            idx=0;
            lineIdx++;
            if (lineIdx>=lines.size())
                str = "#";
            else
                str = lines.get(lineIdx);
            }
        if (idx<str.length())
            cc = str.charAt(idx++);
        else
            cc=' ';
        s = s + cc;
        int cl = sclass(cc);
        int st1 = TBL[st][cl];
        st = st1;
        }
    st = -st;                       // Преобразовать номер конечного в индекс
    if (st==100) st=0;
    int k = s.length();
    int l = back[st];              // длина лексемы  и кол-во возвращаемых литер
    idx-=l;
    Lexem lexem= finStates.get(st);
    String vv = s.substring(0, k - l);
    Lexem two=new Lexem(lexem.type,vv);
    if (st == 1) {
        Lexem keyword = keywords.get(two.value);
        if (keyword!=null){
            two = keyword.clone();
            }
        }
    two.setSrc(lineIdx,idx-vv.length(),str);
    return two;
    }
int sclass(char c){
    switch (c)
        {
case '(': return 5;
case ')': return 6;
case '=': return 7;
case '<': return 8;
case '>': return 9;
case ';': return 10;
case '+': return 11;
case '-': return 12;
case '*': return 13;
case '/': return 14;
case '#': return 15;
case '.': return 16;
case ',': return 17;
case '{': return 18;
case '}': return 19;
case '\"': return 20;
default:
        if (c>='A' && c<='F') return 4;
        if (c>='a' && c<='f') return 4;
        if (c>='0' && c<='9') return 2;
        if (c>='A' && c<='Z') return 3;
        if (c>='a' && c<='z') return 3;
        if (c>='а' && c<='я') return 3;
        if (c>='А' && c<='Я') return 3;
        if (c==' ' || c=='\t')
            return 1;
        return 0;
        }
}
//-----------------------------------
    public static void main(String[] args) {
        Lexem B;
        Scaner0 lex = new Scaner0();
        boolean bb=lex.open("Input01.txt");
        if (bb){
            do {
                B = lex.get();
                System.out.println(B);
            } while (B.type != '#');
        }
    }
}
