package romanow.abc.core.entity.base;

import java.util.ArrayList;
import java.util.Comparator;

public class StringList extends ArrayList<String> {
    public void sort(){
        super.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
                }
            });
        }
}
