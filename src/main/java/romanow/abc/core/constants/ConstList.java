package romanow.abc.core.constants;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

public class ConstList extends ArrayList<ConstValue>{
    public final String group;
    public ConstList(String group) {
        this.group = group; }
    public ArrayList<ConstValue> getValuesList(String fgroup){
        ArrayList<ConstValue> out = new ArrayList<>();
        for(ConstValue cc : this)
            if (cc.groupName().equals(fgroup))
                out.add(cc);
        return out;
        }
}
