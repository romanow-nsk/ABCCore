package romanow.abc.core.basic;
import java.io.*;
import java.util.*;

public class Exec {
        int	IP;			// Счетчик команд
        double M[]=new double[1000];    // Память
        double ST[]=new double[10000];  // Стек операндов
        int sp=-1;                      // Указатель стека
        boolean suspend=false;
        void init(){ suspend=false; IP=0; sp=-1; }
        String system[]={	        // Система команд
        "CALL","LOADM","LOADC","SAVE","IN","OUT","OUTL","ADD","SUB","MUL","DIV",
        "POW","MIN","INT","AND","OR","NOT","LE","LT","GE","GT","EQ","NE","JMP","JNOT","PUT","ENDL"
        };
        String func[]={                  // Таблица функций
        "sin","cos","tan","exp","abs" };
        Vector PROG=new Vector();
        BufferedReader F;

        String myScanf(){
        String ss="";
        char vv;
        try{
            while (true){
            vv=(char)System.in.read();
            if (vv=='\n') break;
            ss=ss+vv;
            }
        } catch(IOException ee){ ss=""; }
        return ss;
        }

        String ftoa(double vv){
                if (vv==0) return "0";
                int i,n=(int)vv;
                String out="";
                for(i=0;n!=0;i++,n=n/10);
                int k=i;
                n=(int)vv;
                vv-=n;
                for(i--;i>=0;i--,n=n/10)
                    out=""+(char)(n%10+'0')+out;
                out=out+".";
                for (int j=0;j<6;j++) {
                        vv*=10; n=(int)vv; vv-=n;
                        out=out+(char)(n%10+'0');
                        }
                return out;
                }
        //----------------- Расшифровка команды --------------------------------
        Command getCommand(){
            String ss=(String)PROG.get(IP++);
            int k=0;
            while(k< ss.length() && ss.charAt(k)!=' ' && ss.charAt(k)!='\t') k++;
            String s1=ss.substring(0,k);
            String s2="-1";
            if (k!=ss.length()) s2=ss.substring(k+1);
            int code=-1;
            for (k=0;k<system.length;k++)
                if (system[k].compareTo(s1)==0) { code=k; break; }
            if (code!=0) return new Command(code,s2);
            else {
                 int kk=-1;
                 for (k=0;k<func.length;k++)
                    if (func[k].compareTo(s2)==0) { kk=k; break; }
                 return new Command(code,""+kk);
            }
        }
        //------------------ Выполнение одной команды -------------------------
        String oneStep(String in){
            int k;
            double vv;
            if (IP >= PROG.size()) { init(); return "\nЗавершение программы\n"; }
            Command cd=getCommand();
            switch(cd.op){
            //----------------------------------------------------------------
case 0:         switch((int)cd.addr){
        case 0: ST[sp]=Math.sin(ST[sp]); break;
        case 1: ST[sp]=Math.cos(ST[sp]); break;
        case 2: ST[sp]=Math.tan(ST[sp]); break;
        case 3: ST[sp]=Math.exp(ST[sp]); break;
        case 4: ST[sp]=Math.abs(ST[sp]); break;
                }
                break;
case 1:		ST[++sp]=M[(int)cd.addr]; break;
case 2:		ST[++sp]= cd.addr; break;
case 3:		M[(int)cd.addr]=ST[sp--]; break;
case 4:		if (!suspend) { suspend=true; IP--; return "Ввод:"; }
                else {suspend=false; M[(int)cd.addr] = cd.atof(in); return in+"\n"; }
case 5:		return ""+ST[sp--];
case  7:	ST[sp-1]=ST[sp-1]+ST[sp]; sp--; break;
case  8:	ST[sp-1]=ST[sp-1]-ST[sp]; sp--; break;
case  9:	ST[sp-1]=ST[sp-1]*ST[sp]; sp--; break;
case 10:	ST[sp-1]=ST[sp-1]/ST[sp]; sp--; break;
case 11:	ST[sp-1]=Math.pow(ST[sp-1],ST[sp]); sp--; break;
case 12:	if (ST[sp]<ST[sp-1]) ST[sp-1]=ST[sp]; sp--; break;
case 13:	ST[sp]=(int)ST[sp]; break;
case 14:	ST[sp-1]=((int)ST[sp]) & ((int)ST[sp-1]); sp--; break;
case 15:	ST[sp-1]=((int)ST[sp]) | ((int)ST[sp-1]); sp--; break;
case 16:	if (ST[sp]!=0) ST[sp]=0; else ST[sp]=1; break;
case 17:	if (ST[sp]<=0) ST[sp]=1; else ST[sp]=0; break;
case 18:	if (ST[sp]<0) ST[sp]=1; else ST[sp]=0; break;
case 19:	if (ST[sp]>=0) ST[sp]=1; else ST[sp]=0; break;
case 20:	if (ST[sp]>0) ST[sp]=1; else ST[sp]=0; break;
case 21:	if (ST[sp]==0) ST[sp]=1; else ST[sp]=0; break;
case 22:	if (ST[sp]!=0) ST[sp]=1; else ST[sp]=0; break;
case 23:	IP+=cd.addr; break;
case 24:	if (ST[sp--]==0)  IP+=cd.addr; break;
case 25:        return cd.val;
case 26:        return "\n";
default:        return "Недопустимый код команды\n";
                }
          return null;
        }
        //------------------ Загрузка и выполнение файла -----------------------
        boolean load(String nm){
                int i;
                try {
                    F = new BufferedReader(new InputStreamReader(new FileInputStream(nm), "Windows-1251"));
                    while(true){
                        String ss = F.readLine();
                        if (ss==null) break;
                        PROG.add(ss);
                    }
                    F.close();
                }   catch (Exception ee){ return false;}
                init();
                return true;
                }

    public Exec() {
    }

    public static void main(String[] args) {
        Exec exec = new Exec();
        exec.load("Input01_bin.txt");
    }
}
