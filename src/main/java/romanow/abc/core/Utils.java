package romanow.abc.core;

import romanow.abc.core.constants.ValuesBase;
import retrofit2.Response;

import javax.swing.*;
import java.io.IOException;

public class Utils {
    public static boolean hasDigits(String ss){
        char cc[] = ss.toCharArray();
        for(char zz : cc)
            if (!(zz >= '0' && zz<='9'))
                return false;
         return true;
        }
    public static int nextMonth(int mnt){
        mnt++;
        if (mnt==13) mnt=1;
        return mnt;
        }
    public static int prevMonth(int mnt){
        mnt--;
        if (mnt==0) mnt=12;
        return mnt;
    }
    public static  int nextMonth(int mnt,int cnt){
        mnt+=cnt;
        if (mnt>12) mnt=mnt-12;
        return mnt;
    }
    public static String cutWithWords(String ss, int sz){
        if (ss.length()>sz)
            ss = ss.substring(0,sz);
        int idx = ss.lastIndexOf(" ");
        if (idx!=-1)
            ss = ss.substring(0,idx)+"...";
        return ss;
        }
     public static void toTextArea(String ss1, int sz, JTextArea mes){
        String ss = ss1;
        mes.setText("");
        while(true){
            if (ss.length()<sz){
                mes.append(ss);
                return;
                }
            String zz = ss.substring(0,sz);
            int idx = zz.lastIndexOf(" ");
            if (idx!=-1){
                mes.append(zz.substring(0,idx)+"\n");
                ss = ss.substring(idx+1);
                }
            else{
                mes.append(ss);
                return;
                }
            }
        }
    public static String generateToken(){
        return generateToken(ValuesBase.SessionTokenLength);
        }
    public static String generateToken(int sz){
        char cc[]=new char[sz];
        for(int i=0;i<cc.length;i++){
            int k=(int)(62*Math.random());
            if (k<26)
                cc[i]=(char)('A'+k);
            else
            if (k<52)
                cc[i]=(char)('a'+k-26);
            else
                cc[i]=(char)('0'+k-52);
        }
        return new String(cc);
        }
    public static String timeInMinToString(int time){
        if (time/(60*24)==0)
            return String.format("%2d:%2d",time/60,time%60);
        else
            return String.format("%d:%2d",time/60,time%60);
        }
    public static String timeInSecToString(int time){
        if (time/3600==0)
            return String.format("%2d:%2d",time/60,time%60);
        else
            return String.format("%2d:%2d:%2d",time/3600,(time/60)%60,time%60);
        }
    public static long getPID() {
        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        if (processName != null && processName.length() > 0) {
            try {
                return Long.parseLong(processName.split("@")[0]);
            }
            catch (Exception e) { return 0; }
        }
        return 0;
        }
    public static String nDigits(int vv, int ndig){
        String out = "";
        while(ndig--!=0){
            out = (char)(vv%10+'0')+out;
            vv/=10;
            }
        return out;
        }
    public static String httpError(Response res){
        String ss =   res.message()+" ("+res.code()+") ";
        try {
            ss+=res.errorBody().string();
            } catch (IOException e) {}
        return ss;
        }
    public static String createFatalMessage(Throwable ee) {
        return createFatalMessage(ee,ValuesBase.FatalExceptionStackSize);
        }
    public static String createFatalMessage(Throwable ee, int stackSize) {
        String ss = ee.toString() + "\n";
        StackTraceElement dd[] = ee.getStackTrace();
        for (int i = 0; i < dd.length && i < stackSize; i++) {
            ss += dd[i].getClassName() + "." + dd[i].getMethodName() + ":" + dd[i].getLineNumber() + "\n";
        }
        String out = "Программная ошибка:\n" + ss;
        return out;
        }
    public static void printFatalMessage(Throwable ee) {
        System.out.println(createFatalMessage(ee));
        }
    public static String when(String ss){
        if (ss.length()==0) return ss;
        int idx=ss.length();
        char cc = ss.charAt(idx-1);
        switch (cc){
            case 'а': return ss.substring(0,idx-1)+'у';
            case 'о': return ss;
            case 'й': return ss.substring(0,idx-1)+'я';
            case 'я': return ss.substring(0,idx-1)+'ю';
            default:  return ss+'a';
            }
        }
    public static String toUpperFirst(String ss){
        return ss.substring(0,1).toUpperCase()+ss.substring(1);
        }
    public final static String FirstToUpper(String ss){
        return ss.substring(0,1).toUpperCase()+ss.substring(1);
        }
    public static void main(String a[]){
        System.out.println(when("Иванов"));
        System.out.println(when("Зуева"));
        System.out.println(when("Евгений"));
        System.out.println(when("Петр"));
        System.out.println(when("Мария"));
        System.out.println(when("Семенович"));
    }
}
