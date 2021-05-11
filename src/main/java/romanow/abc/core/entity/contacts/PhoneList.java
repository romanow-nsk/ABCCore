package romanow.abc.core.entity.contacts;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class PhoneList extends Contact {
    private transient boolean ready=false;
    private transient ArrayList<Phone> list=new ArrayList<>();
    public PhoneList(){}
    private boolean innerParse() {
        return innerParse(value);
        }
    private boolean innerParse(String zz){
        if (ready)
            return true;
        if (zz==null) zz="";          // При десериализации отсуствующих полей
        zz = zz.trim();
        list.clear();
        StringTokenizer tt= new StringTokenizer(zz,",");
        String ss=null;
        while(tt.hasMoreElements()){
            ss=tt.nextToken();
            Phone xx = new Phone(ss);
            if (!xx.valid())
                return false;
            list.add(xx);
            }
        value = zz;
        ready = true;
        return true;
        }
    public boolean parse(String ss){
        ready = false;
        return innerParse(ss);
        }
    private void test(){
        if (ready)
            return;
        innerParse();
        }
    public void refresh(){
        ready=false;
        innerParse(value);
        }
    private void refreshBack(){
        value="";
        if (list.size()==0) {
            return;
            }
        for(int i=0;i<list.size();i++){
            if (i!=0) value+=",";
            value+=list.get(i).value;
            }
        }
    public boolean remove(Phone pp){
        test();
        for(int i=0;i<list.size();i++)
            if (list.get(i).value.equals(pp.value)){
                list.remove(i);
                refreshBack();
                return true;
            }
        return false;
        }
    public void clear(){
        value="";
        list.clear();
        }
    public ArrayList<Phone> getList(){
        innerParse();
        return list;
        }
    public boolean addPhone(Phone phone){
        if (!phone.valid())
            return false;
        test();
        for (Phone pp : list){
            if (pp.value.equals(phone.value))
                return false;
            }
        if (value.length()!=0)
            value+=",";
        value += phone.getValue();
        list.add(phone);
        return true;
        }
    public static void main(String ss[]){
        PhoneList pp = new PhoneList();
        pp.addPhone(new Phone("9139449081"));
        pp.addPhone(new Phone("+79131111111"));
        pp.addPhone(new Phone("89132222222"));
        System.out.println(pp+" "+pp.getList().size());
        pp.addPhone(new Phone("89132222222"));
        System.out.println(pp+" "+pp.getList().size());
        pp.refresh();
        System.out.println(pp+" "+pp.getList().size());
        }
}
