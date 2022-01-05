package romanow.abc.core.script;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Scaner {
    public static char getSymbol(char c){
        switch (c){
            case '(': return c;
            case ')': return c;
            case '=': return c;
            case '<': return c;
            case '>': return c;
            case ';': return c;
            case '+': return c;
            case '-': return c;
            case '*': return c;
            case '/': return c;
            case '#': return c;
            case '.': return c;
            case ',': return c;
            case '{': return c;
            case '}': return c;
            case '\"': return c;
            case '^': return c;
            case '!': return c;
            case '&': return c;
            case '|': return c;
            default:
                if (c>='A' && c<='F') return 'F';
                if (c>='a' && c<='f') return 'F';
                if (c>='0' && c<='9') return '0';
                if (c>='A' && c<='Z') return 'A';
                if (c>='a' && c<='z') return 'A';
                if (c>='а' && c<='я') return 'A';
                if (c>='А' && c<='Я') return 'A';
                if (c==' ' || c=='\t')
                    return ' ';
                return 'z';
            }
        }
    //-----------------------------------------  Состояния -------------------------------
    public final static LexState state0 = new LexState() {
        @Override
        public LexState onEvent(char sym) {
            switch (sym){
                case '#': return new Lexem('#',"EOF");
                case 'z': return new Lexem('z',"Illegal");
                case ' ': return state0;
                case '0': return state1;
                case 'F': return state2;
                case 'A': return state2;
                case '<': return state3;
                case '>': return state4;
                case '*': return state5;
                case '\"': return state7;
                }
            return new Lexem(sym,""+sym);
            }
        };
    public final static LexState state1 = new LexState() {
        @Override
        public LexState onEvent(char sym) {
            switch (sym){
                case '0': return state1;
                case 'F': return state1;
                case '.': return state6;
                }
            return new Lexem('c',1,"const");
            }
        };
    public final static LexState state6 = new LexState() {
        @Override
        public LexState onEvent(char sym) {
            switch (sym){
                case '0': return state6;
                case 'F': return state6;
                }
            return new Lexem('c',1,"const");
            }
        };
    public final static LexState state2 = new LexState() {
        @Override
        public LexState onEvent(char sym) {
            switch (sym){
                case '0': return state2;
                case 'F': return state2;
                case 'A': return state2;
                }
            return new Lexem('a',1,"ident");
            }
        };
    public final static LexState state3 = new LexState() {
        @Override
        public LexState onEvent(char sym) {
            switch (sym){
                case '=': return new Lexem('l',"<=");
                case '>': return new Lexem('n',"<>");
                }
            return new Lexem('<',1,"<");
           }
        };
    public final static LexState state4 = new LexState() {
        @Override
        public LexState onEvent(char sym) {
            switch (sym){
                case '=': return new Lexem('g',">=");
                }
            return new Lexem('>',1,">");
            }
        };
    public final static LexState state5 = new LexState() {
        @Override
        public LexState onEvent(char sym) {
            switch (sym){
                case '*': return new Lexem('p',"**");
                }
            return new Lexem('*',1,"*");
            }
        };
    public final static LexState state7 = new LexState() {
        @Override
        public LexState onEvent(char sym) {
            switch (sym){
                case '\"': return state8;
                }
            return state7;
            }
        };
    public final static LexState state8 = new LexState() {
        @Override
        public LexState onEvent(char sym) {
            switch (sym){
                case '\"': return state7;
                }
            return new Lexem('s',1,"string");
            }
        };
    //------------------------------------------------------------------------------------
    final private HashMap<String, Lexem> keywords=new HashMap<>();
    private ArrayList<String> lines = new ArrayList();
    private String str;
    private int idx=0;
    private int lineIdx=0;
    public Scaner() {
        Lexem lexem = new Lexem('U',"if");
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('E',"else");
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('W',"while");
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('a',"and");
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('o',"or");
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('x',"xor");
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('t',"not");
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('I',"int");
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('C',"string");
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('S',"short");
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('L',"long");
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('V',"void");
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('R',"float");
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('D',"double");
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('B',"boolean");
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('T',"true");
        keywords.put(lexem.value,lexem);
        lexem = new Lexem('F',"false");
        keywords.put(lexem.value,lexem);
        }
    //-------------------------------------------------------------------------------------
    public void open(ArrayList<String> ss){
        lines = ss;
        idx=0;
        lineIdx=0;
        str = lines.get(lineIdx);
        }
    public boolean open(String nm) {
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
    public Lexem get(){
        String s="";
        LexState state = state0;
        Lexem lexem = null;
        char cc;
        while (lexem==null) {              // Пока не достигнуто конечное состояние
            if (state == state0) s = "";         // Накопление лексемы с 0-го состояния
            if (idx >= str.length()) {
                idx = 0;
                lineIdx++;
                if (lineIdx >= lines.size())
                    str = "#";
                else
                    str = lines.get(lineIdx);
            }
            if (idx < str.length())
                cc = str.charAt(idx++);
            else
                cc = ' ';
            s = s + cc;
            LexState res = state.onEvent(getSymbol(cc));
            if (res instanceof Lexem){
                lexem = (Lexem) res;
                }
            else
                state = res;
            }
        //if (st==100) st=0;
        int l = lexem.backSymCount;              // длина лексемы  и кол-во возвращаемых литер
        idx-=l;
        String vv = s.substring(0, s.length() - l);
        lexem.value = vv;
        Lexem keyword = keywords.get(lexem.value);
        if (keyword!=null){
            lexem = keyword.clone();
            }
        lexem.setSrc(lineIdx,idx-vv.length(),str);
        return lexem;
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
