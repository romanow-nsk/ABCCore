package romanow.abc.core.utils;

import romanow.abc.core.entity.base.StringList;

public class StringFIFO {
    private String fifo[];
    private int sz;
    private int cnt;
    private int idx;            // Индекс очередного свободного
    public StringFIFO(int sz0){
        sz=sz0;
        fifo = new String[sz];
        idx = cnt = 0;
        }
    public void add(String ss){
        fifo[idx++] = ss;
        if (idx==sz) idx=0;
        if (cnt!=sz) cnt++;
        }
    public StringList getStrings(int lnt){
        if (lnt>cnt)
            lnt=cnt;
        StringList out = new StringList();
        int i=idx-lnt;
        if (i<0) i+=sz;
        while(lnt--!=0){
            out.add(fifo[i++]);
            if (i==sz) i=0;
            }
        return out;
    }
}
