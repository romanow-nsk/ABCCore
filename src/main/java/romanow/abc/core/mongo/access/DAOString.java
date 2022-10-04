package romanow.abc.core.mongo.access;

import org.apache.poi.ss.usermodel.Row;
import org.bson.Document;
import romanow.abc.core.UniException;
import romanow.abc.core.entity.EntityField;
import romanow.abc.core.export.ExCellCounter;
import romanow.abc.core.mongo.DAO;
import romanow.abc.core.mongo.I_MongoDB;
import romanow.abc.core.mongo.RequestStatistic;

import java.util.HashMap;

class DAOString implements I_DAOAccess {
        //-----------------------------------------------------------------------------------------------------------------
        @Override
        public void getField(EntityField ff, DAO dao)  throws Exception{
            ff.value = "" + ff.field.get(dao);
            }
    @Override
    public void getDBValue(EntityField ff, DAO dao, String prefix, Document out, int level, I_MongoDB mongo, HashMap<String, String> path, RequestStatistic statistic) throws UniException {
        try {
            ff.field.set(dao, (String) out.get(prefix + ff.name));
            } catch (Exception ee) {
                try {
                    ff.field.set(dao, "");
                    } catch (IllegalAccessException e) {}
                dao.error(prefix,ff);
                }
            }

    @Override
    public void updateField(EntityField ff, DAO dao, String value) throws Exception {
        ff.field.set(dao, value);
        }

    @Override
    public void getXMLValues(EntityField ff, DAO dao, Row row, int cnt, HashMap<String, Integer> colMap) throws Exception {
        ff.field.set(dao, row.getCell(cnt).getStringCellValue());
        }

    @Override
    public void putXMLValues(EntityField ff, DAO dao, Row row, ExCellCounter cnt) throws Exception {
        row.createCell(cnt.getIdx()).setCellValue((String) ff.field.get(dao));
    }

    @Override
    public void loadDBValues(EntityField ff, DAO dao, DAO src, int level, I_MongoDB mongo) throws Exception {
        try {
            ff.field.set(dao, (String)ff.field.get(src));
            } catch (Throwable ee){
                dao.error("",ff);
                }
        }

    @Override
    public void copyDBValues(EntityField ff, DAO dao, DAO src) throws Exception {
        try {
            ff.field.set(dao, (String)ff.field.get(src));
            } catch (Throwable ee){
                dao.error("",ff);
                }
        }

    @Override
    public void putFieldValue(DAO dao, String prefix, Document out, int level, I_MongoDB mongo, EntityField ff) throws Exception {
        out.put(prefix+ff.name,ff.field.get(dao));
        }

    @Override
    public String createKotlinFieldDefine(EntityField ff) {
        return ff.name+":String=\"\"";
    }
}
