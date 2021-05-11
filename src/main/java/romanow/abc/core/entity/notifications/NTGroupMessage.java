package romanow.abc.core.entity.notifications;

import com.google.gson.Gson;
import romanow.abc.core.I_SelectObject;
import romanow.abc.core.I_TextFile;
import romanow.abc.core.UniException;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.users.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;

public class NTGroupMessage extends ArrayList<NTMessage> implements I_TextFile {
    public NTGroupMessage(){}
    public void forEach(I_SelectObject fun){
        for(NTMessage mes : this)
            fun.onSelect(mes);
        }
    public void save(BufferedWriter out) throws UniException {
        try {
            Gson gson = new Gson();
            out.write(""+size());
            out.newLine();
            for(NTMessage mes : this){
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
                add((NTMessage) gson.fromJson(out.readLine(),NTMessage.class));
                }
            } catch (Exception ee){ throw UniException.io(ee); }
        }
    public boolean addToOld(NTMessage mes, boolean withText){
        for(NTMessage mm : this){
            if (mm.getType()!=mes.getType())
                continue;
            if (mm.getExecuteMode()!=mes.getExecuteMode())
                continue;
            if (mm.getUserSenderType()!=mes.getUserSenderType())
                continue;
            if (withText && !mm.getHeader().equals(mes.getHeader()))
                continue;
            if (withText && !mm.getMessage().equals(mes.getMessage()))
                continue;
            if (mm.getType()== ValuesBase.NTUserAction && mm.getParam()!=mes.getParam())      // Разные действия
                continue;
            add(mes);
            return true;
            }
        return false;
        }
    public int getType(){ return get(0).getType(); }
    public User getUser(){ return get(0).getUser().getRef(); }
    public int getExecuteMode(){ return get(0).getExecuteMode(); }

}
