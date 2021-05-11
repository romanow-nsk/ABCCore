package romanow.abc.core.jdbc;

import romanow.abc.core.mongo.I_MongoDB;
import romanow.abc.core.mongo.MongoDB;
import romanow.abc.core.mongo.MongoDB36;

import java.util.ArrayList;

public class JDBCFactory {
    private I_MongoDB[] list = {
        new MongoDB(),new MongoDB36(), new SQLDBDriver(new MySQLJDBC()),
            new SQLDBDriver(new SQLiteJDBC()), new SQLDBDriver(new MariaDBJDBC())
        };
    public JDBCFactory(){}
    public ArrayList<String> getNameList(){
        ArrayList<String> out = new ArrayList<>();
        for(I_MongoDB bb : list)
            out.add(bb.getDriverName());
        return out;
        }
    public I_MongoDB getDriverByName(String name){
        for(I_MongoDB bb : list)
            if (bb.getDriverName().equals(name))
                return bb;
        return null;
        }
    public I_MongoDB getDriverByIndex(int idx){
        return list[idx];
    }
}
