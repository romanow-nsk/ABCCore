package romanow.abc.core.script;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

//----------------------- Лексический анализатор ---------------------------------
public class Scaner {
    private StringBuffer is=new StringBuffer();
    private char str[];
    private int idx=0;
    final private  int	TBL[][]= {            // Матрица переходов КА
            {0, 1, 2, 2, -11, -12, -16, 3, 4, -10, -13, -14, 5, -4, -15, 0, -19, -20, -21, 7},
            {-2, 1, -2, 1, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, 6, -2, -2, -2, -2},
            {-1, 2, 2, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {-5, -5, -5, -5, -5, -5, -7, -5, -17, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5},
            {-6, -6, -6, -6, -6, -6, -8, -6, -6, -6, -6, -6, -6, -6, -6, -6, -6, -6, -6, -6},
            {-9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -18, -9, -9, -9, -9, -9, -9, -9},
            {-2, 6, -2, 6, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
            {7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8},
            {-22, -22, -22, -22, -22, -22, -22, -22, -22, -22, -22, -22, -22, -22, -22, -22, -22, -22, -22, 7}
            };
    final private HashMap<Character,Lexem> lexems=new HashMap<>();
    final private HashMap<String,Lexem> keywords=new HashMap<>();
    final private ArrayList<Lexem> finStates = new ArrayList<>();
    public Scaner(){
        Lexem lexem;
        lexem = new Lexem('a',"ident");
        finStates.add(lexem);
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('c',"const");
        finStates.add(lexem);
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('?',"comment");
        finStates.add(lexem);
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('/',"/");
        finStates.add(lexem);
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('<',"<");
        finStates.add(lexem);
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('>',">");
        finStates.add(lexem);
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('l',"<=");
        finStates.add(lexem);
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('g',">=");
        finStates.add(lexem);
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('*',"*");
        finStates.add(lexem);
        lexems.put(lexem.type,lexem);
        lexem = new Lexem(';',";");
        finStates.add(lexem);
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('(',"(");
        finStates.add(lexem);
        lexems.put(lexem.type,lexem);
        lexem = new Lexem(')',")");
        finStates.add(lexem);
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('+',"+");
        finStates.add(lexem);
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('-',"-");
        finStates.add(lexem);
        lexems.put(lexem.type,lexem);
        lexem = new  Lexem('#',"EOF");
        finStates.add(lexem);
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('=',"=");
        finStates.add(lexem);
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('n',"<>");
        finStates.add(lexem);
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('p',"**");
        finStates.add(lexem);
        lexems.put(lexem.type,lexem);
        lexem = new Lexem(',',",");
        finStates.add(lexem);
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('{',"{");
        finStates.add(lexem);
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('}',"}");
        finStates.add(lexem);
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('s',"string");
        finStates.add(lexem);
        lexems.put(lexem.type,lexem);
        lexem = new Lexem('U',"if");
        lexems.put(lexem.type,lexem);
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('E',"else");
        lexems.put(lexem.type,lexem);
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('w',"while");
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
    int back[]=		        // Кол-во возвращаемых литер для конечных состояний
        {1,1,0,0,1,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1};
    boolean open(String nm) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(nm), "Windows-1251"));
            is = new StringBuffer();
            String ss;
            while ((ss=reader.readLine())!=null)
                is.append(ss+" ");
            reader.close();
            str = is.toString().toCharArray();
            idx=0;
            }   catch (Exception ee){ return false;}
    return true;
    }

Lexem get(){
    String s="";
    int ST = 0;
    char cc;
    while (ST >= 0) {              // Пока не достигнуто конечное состояние
        if (ST == 0) s="";         // Накопление лексемы с 0-го состояния
        cc = str[idx++];
        s = s + cc;
        int CL = sclass(cc);
        int ST1 = TBL[ST][CL];
        ST = ST1;
        }
    ST = -ST - 1;                  // Преобразовать номер конечного в индекс
    int k = s.length();
    int l = back[ST];              // длина лексемы  и кол-во возвращаемых литер
    idx-=l;
    Lexem lexem= finStates.get(ST);
    Lexem L=new Lexem(lexem.type,s.substring(0, k - l));
    if (ST == 0) {
        Lexem keyword = keywords.get(L.value);
        if (keyword!=null)
            return keyword;
            }
    return L;
    }
int sclass(char c){
    switch (c)
        {
case '(': return 4;
case ')': return 5;
case '=': return 6;
case '<': return 7;
case '>': return 8;
case ';': return 9;
case '+': return 10;
case '-': return 11;
case '*': return 12;
case '/': return 13;
case '#': return 14;
case '.': return 15;
case ',': return 16;
case '{': return 17;
case '}': return 18;
case '\"': return 19;
default:
        if (c>='A' && c<='F') return 3;
        if (c>='a' && c<='f') return 3;
        if (c>='0' && c<='9') return 1;
        if (c>='A' && c<='Z') return 2;
        if (c>='a' && c<='z') return 2;
        if (c>='а' && c<='я') return 2;
        if (c>='А' && c<='Я') return 2;
        return 0;
        }
}
//-----------------------------------
    public static void main(String[] args) {
        Lexem B;
        Scaner lex = new Scaner();
        boolean bb=lex.open("Input01.txt");
        if (bb){
            do {
                B = lex.get();
                System.out.println(B.type+" "+B.value);
            } while (B.type != '#');
        }
    }
}
