package romanow.abc.core.export;

import org.apache.poi.ss.usermodel.Sheet;
import romanow.abc.core.UniException;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.mongo.I_MongoDB;
import romanow.abc.core.utils.FileNameExt;

import java.util.ArrayList;
import java.util.HashMap;

public interface I_Excel {
    public void exportData(ArrayList<Entity> data) throws UniException;
    public void exportHeader(Entity ent) throws UniException;
    public int sheetCount();
    public String importSheet(Entity proto, Sheet sh, I_MongoDB mongo, HashMap<String,Integer> colMap) throws UniException;
    public void save(FileNameExt fspec) throws UniException;
    public void save(String fullName) throws UniException;
    public String load(FileNameExt fspec, I_MongoDB mongo);
    public String load(String fullName, I_MongoDB mongo);
}
