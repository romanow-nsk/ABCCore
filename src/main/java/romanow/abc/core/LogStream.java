package romanow.abc.core;

import romanow.abc.core.utils.OwnDateTime;
import romanow.abc.core.utils.StringFIFO;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class LogStream extends OutputStream {
    private StringBuffer str = new StringBuffer();
    private int bb=0;
    private boolean two=false;
    private boolean newString=true;
    private boolean utf8;
    private I_String back;
    private StringFIFO fifo=null;
    public LogStream(boolean utf80, StringFIFO fifo0, I_String back0){        // ВОЗВРАЩАЕТ БЕЗ КОНЦА СТРОКИ
        super();
        utf8 = utf80;
        back = back0;
        fifo = fifo0;
        }
    public void setStringFIFO(StringFIFO fifo0){
        fifo = fifo0;
        }
    private void procString(){
        String ss;
        int sz = str.length();
        char c1 = sz==0 ? 0 : str.charAt(sz-1);
        char c2 = sz<2 ? 0 : str.charAt(sz-2);
        boolean b1 = c1=='\r' || c1=='\n';
        boolean b2 = c2=='\r' || c2=='\n';
        if (b2)
            ss = str.substring(0,sz-2);
        else
        if (b1)
            ss= str.substring(0,sz-1);
        else
            ss=str.toString();
        ss = new OwnDateTime().timeFullToString()+" "+ss;
        if (back!=null)
            back.onEvent(ss);
        if (fifo!=null)
            fifo.add(str.toString());
        str = new StringBuffer();
        }
    @Override
    public void write(int b) throws IOException {
        String ss;
        // Перекодировка байтов W1251 обратно в Unicode
        //if (newString){
        //    newString=false;
        //    procString();
        //    }
        if (!utf8){
            if (b=='\n'){
                procString();
                //newString=true;
                }
            else{
                byte bb[]=new byte[1];
                bb[0]=(byte) b;
                ss = new String(bb,"Windows-1251");
                str.append(ss);
                }
            }
        else{
            if (two) {
                ss =  "" + (char) (((bb & 0x1F) << 6) | (b & 0x3F));
                str.append(ss);
                two = false;
                }
            else
            if ((b & 0x0E0) == 0x0C0) {
                bb = b;
                two = true;
                }
            else{
                if (b=='\n'){
                    //newString=true;
                    procString();
                    }
                else
                    str.append((char)b);
                }
            }
        }
    private static int cnt=0;
    public static void main(String ss[]) throws IOException {
        LogStream log = new LogStream(true, null,new I_String() {
            @Override
            public void onEvent(String ss) {
                cnt++;
            }
        });
        System.setOut(new PrintStream(log));
        System.setErr(new PrintStream(log));
        System.out.println("аааааааааааааааа");
        System.out.println("ббббббббббббб\nвввввввввввввв\nнннннннннннннн");
        System.out.println("ббббббббббббб\nвввввввввввввв\nнннннннннннннн");
        System.in.read();
    }
}
