package romanow.abc.core.utils;

import romanow.abc.core.entity.base.StringList;

import java.util.ArrayList;

public class StringFIFO {
    private ArrayList<String> fifo = new ArrayList<>();
    private int sz;
    private long seqNum=0;
    public StringFIFO(int sz0){
        sz=sz0;
        seqNum = 1;
        }
    public void add(String ss){
        seqNum++;
        if(fifo.size()==sz)
            fifo.remove(0);
        fifo.add(ss);
        }
    public StringList getStrings(int lnt){
        StringList out = new StringList();
        if (lnt > sz)
            lnt = sz;
        for(int i=sz-1; lnt!=0; i--, lnt--)
            out.add(fifo.get(i));
        return out;
        }
    public Pair<Long,StringList> getStringsWithLastNum(long lastNum){
        StringList out = new StringList();
        if (fifo.size()==0)
            return new Pair<Long,StringList>(0L,out);
        long idx2 = seqNum - lastNum;
        if (idx2 >= fifo.size())
            idx2 = fifo.size();
        for(int i=fifo.size()-(int) idx2;i<fifo.size();i++)
               out.add(fifo.get(i));
        return new Pair<Long,StringList>(seqNum,out);
        }
    public static void main(String dd[]){
        StringFIFO fifo1 = new StringFIFO(50);
        for(int i=0;i<30;i++)
            fifo1.add(""+i);
        Pair<Long,StringList> vv = fifo1.getStringsWithLastNum(0);
        for(String ss : vv.o2)
            System.out.print(ss+" ");
        System.out.println("");
        for(int i=30;i<60;i++)
            fifo1.add(""+i);
        vv = fifo1.getStringsWithLastNum(vv.o1.longValue());
        for(String ss : vv.o2)
            System.out.print(ss+" ");
        System.out.println("");
        vv = fifo1.getStringsWithLastNum(vv.o1.longValue());
        for(String ss : vv.o2)
            System.out.print(ss+" ");
        System.out.println("");
        for(int i=60;i<90;i++)
            fifo1.add(""+i);
        vv = fifo1.getStringsWithLastNum(vv.o1.longValue());
        for(String ss : vv.o2)
            System.out.print(ss+" ");
        System.out.println("");
        vv = fifo1.getStringsWithLastNum(vv.o1.longValue());
        for(String ss : vv.o2)
            System.out.print(ss+" ");
        System.out.println("");
        }
}
