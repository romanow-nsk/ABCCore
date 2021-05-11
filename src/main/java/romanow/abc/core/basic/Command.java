package romanow.abc.core.basic;


public class Command {
    int op;
    double addr;
    String val;
    double atof(String in){
        double v=0,d=0.1;
        boolean minus=false;
        int i,k,l=in.length();
        for(i=0;i<l && in.charAt(i)!='.';i++){
                char cc=in.charAt(i);
                if (cc=='-') {minus=true; continue; }
                k=cc-'0';
                v=v*10+k;
                }
        if (i!=l) {
            for (i++; i < l; i++) {
                char cc = in.charAt(i);
                k = cc - '0';
                v += d * k;
                d = d / 10;
            }}
        if (minus) v=-v;
        return v;
        }
    public Command(int op0,String v0) { op=op0; val=v0; addr=atof(val); }
}
