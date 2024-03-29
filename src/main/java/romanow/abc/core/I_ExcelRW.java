package romanow.abc.core;

import romanow.abc.core.export.ExCellCounter;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.HashMap;

public interface I_ExcelRW {
    public void putData(Row row, ExCellCounter cnt) throws UniException;
    public void getData(Row row, String prefix, HashMap<String,Integer> colMap) throws UniException;
    public void putHeader(String prefix, ArrayList<String> list) throws UniException;
}
