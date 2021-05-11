package romanow.abc.core.entity.notifications;

import com.google.gson.Gson;
import romanow.abc.core.I_TextFile;
import romanow.abc.core.UniException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class NTList implements I_TextFile {
    private HashMap<Long,NTMessage> map = new HashMap<>();
    private ArrayList<NTMessage> data = new ArrayList<>();
    public ArrayList<NTMessage> list(){
        return data;
        }
    private void createHash(){
        map.clear();
        for(NTMessage mes : data)
            map.put(mes.getOid(),mes);
        }
    public void save(BufferedWriter out) throws UniException {
        try {
            Gson gson = new Gson();
            out.write(""+data.size());
            out.newLine();
            for(NTMessage mes : data){
                out.write(gson.toJson(mes));
                out.newLine();
                }
        } catch (Exception ee){ throw UniException.io(ee); }
    }
    public void load(BufferedReader out) throws UniException{
        try {
            data.clear();
            Gson gson = new Gson();
            int sz = Integer.parseInt(out.readLine());
            map.clear();
            while(sz--!=0){
                data.add((NTMessage) gson.fromJson(out.readLine(),NTMessage.class));
                }
            createHash();
            } catch (Exception ee){ throw UniException.io(ee); }
        }
    public boolean add(NTMessage mes){
        boolean changed = map.get(mes.getOid())==null;
        if (changed){
            data.add(mes);
            map.put(mes.getOid(),mes);
            }
        return changed;
        }
    public boolean update(NTMessage mes){
        NTMessage orig = map.get(mes.getOid());
        if (orig==null)
            return false;
        data.remove(orig);
        map.remove(mes.getOid());
        add(mes);
        return true;
        }
    public boolean remove(long oid){
        NTMessage orig = map.get(oid);
        if (orig==null)
            return false;
        data.remove(orig);
        map.remove(oid);
        return true;
        }
    public ArrayList<NTMessage> getListByQuery(int type, int state, long userId){
        ArrayList<NTMessage> out = new ArrayList<>();
        for(NTMessage mes : list()){
            boolean b1 =  type==0 || mes.getUserReceiverType()==type;
            boolean b2 =  state==0 || mes.getState()==state;
            boolean b3 = userId==0 || mes.getUser().getOid()==userId;
            if (b1 && b2 && b3)
                out.add(mes);
            }
        return out;
        }
    public NTMessage get(long id){
        return map.get(id);
        }
    public int getCountByQuery(int type, int state, long userId){
        int count=0;
        for(NTMessage mes : list()){
            boolean b1 =  type==0 || mes.getUserReceiverType()==type;
            boolean b2 =  state==0 || mes.getState()==state;
            boolean b3 = userId==0 || mes.getUser().getOid()==userId;
            if (b1 && b2 && b3)
                count++;
            }
        return count;
        }
    public ArrayList<NTMessage> getData() {
        return data;
        }
    public int size(){ return data.size(); }
}

