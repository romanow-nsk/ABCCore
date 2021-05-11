package romanow.abc.core.entity.notifications;

import com.google.gson.Gson;
import romanow.abc.core.I_TextFile;
import romanow.abc.core.UniException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;

public class NTGroupList extends ArrayList<NTGroupMessage> implements I_TextFile {
    public void save(BufferedWriter out) throws UniException {
        try {
            Gson gson = new Gson();
            out.write(""+size());
            out.newLine();
            for(NTGroupMessage mes : this){
                out.write(gson.toJson(mes));
                out.newLine();
                }
            } catch (Exception ee){ throw UniException.io(ee); }
        }
    public void load(BufferedReader out) throws UniException{
        try {
            clear();
            Gson gson = new Gson();
            int sz = Integer.parseInt(out.readLine());
            while(sz--!=0){
                add((NTGroupMessage) gson.fromJson(out.readLine(),NTGroupMessage.class));
                }
            } catch (Exception ee){ throw UniException.io(ee); }
        }
    public void add(NTMessage mes, boolean withText){
        for(NTGroupMessage group : this){
            if (group.addToOld(mes,withText))
                return;
            }
        NTGroupMessage xx = new NTGroupMessage();
        xx.add(mes);
        add(xx);
        }
    public NTGroupList(NTList list, boolean withText){
        clear();
        for(NTMessage mes : list.getData())
            add(mes,withText);
        }
}
