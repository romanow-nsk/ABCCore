package romanow.abc.core.basic;
import java.io.*;

//----------------------- Лексический анализатор ---------------------------------
public class Lex {
    InputStreamReader F;
    int	TBL[][]={	        // Матрица переходов КА
                {  0,  1,  2,  2,-11,-12,-16,  3,  4,-10,-13,-14,  5, -4,-15,  0,-19,-20,-21, 7},
                { -2,  1, -2,  1, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,  6, -2, -2, -2,-2},
                { -1,  2,  2,  2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1},
                { -5, -5, -5, -5, -5, -5, -7, -5,-17, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5,-5},
                { -6, -6, -6, -6, -6, -6, -8, -6, -6, -6, -6, -6, -6, -6, -6, -6, -6, -6, -6,-6},
                { -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,-18, -9, -9, -9, -9, -9, -9,-9},
                { -2,  6, -2,  6, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,-2},
                {  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7, 8},
                {-22,-22,-22,-22,-22,-22,-22,-22,-22,-22,-22,-22,-22,-22,-22,-22,-22,-22,-22, 7}
    };
    String lexems[]={		// Массив лексем и замещающих их символов
                    "a_ident",
                    "c_const",
                    "?_comment",
                    "/_/",
                    "<_<",
                    ">_>",
                    "l_<=",
                    "g_>=",
                    "*_*",
                    ";_;",
                    "(_(",
                    ")_)",
                    "+_+",
                    "-_-",
                    "#_end_of_file",
                    "=_=",
                    "n_<>",
                    "p_**",
                    ",_,",
                    "{_{",
                    "}_}",
                    "s_string"
    };
    int back[]=		        // Кол-во возвращаемых литер для конечных состояний
        {1,1,0,0,1,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1};
    String saved="";		// Возвращенные символы
    String keywords[]={         // Массив ключевых слов
                      "i_if",
                      "e_else",
                      "l_while",
                      "r_read",
                      "w_write",
                      "&_and",
                      "|_or",
                      "!_not",
                      "b_endl"
    };
    Lex(){}
    boolean open(String nm) {
        try {
            F = new InputStreamReader(new FileInputStream(nm), "Windows-1251");
            saved="";
        }   catch (Exception ee){ return false;}
    return true;
    }
    void close(){
        try { F.close(); }   catch (IOException ee){}
    }

Lexem get(){
    String s="";
    int ST = 0;
    char cc;
    while (ST >= 0) {              // Пока не достигнуто конечное состояние
        if (ST == 0) s="";         // Накопление лексемы с 0-го состояния
        if (saved!=null && saved.length() != 0) { // Строка возвращенных литер не пуста
            cc = saved.charAt(0);
            saved = saved.substring(1);
        } else {
            try {
            cc = (char)F.read();
            } catch (IOException ee) { cc = '#'; }
        }
        s = s + cc;
        int CL = sclass(cc);
        int ST1 = TBL[ST][CL];
        ST = ST1;
    }
    ST = -ST - 1;                  // Преобразовать номер конечного в индекс
    int k = s.length();
    int l = back[ST];              // длина лексемы  и кол-во возвращаемых литер
    saved = s.substring(k - l);
    Lexem L=new Lexem(lexems[ST].charAt(0),s.substring(0, k - l));
    if (ST == 0) {
        for (k = keywords.length-1; k >= 0; k--)
            if (L.val.compareTo(keywords[k].substring(2))==0) {
                L.type = keywords[k].charAt(0);
                break;
            }
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
        Lex lex = new Lex();
        boolean bb=lex.open("Input01.txt");
        if (bb){
            do {
                B = lex.get();
                System.out.println(B.type+" "+B.val);
            } while (B.type != '#');
        }
    }
}
