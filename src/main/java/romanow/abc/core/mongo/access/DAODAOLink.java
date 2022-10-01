package romanow.abc.core.mongo.access;

import org.apache.poi.ss.usermodel.Row;
import org.bson.Document;
import romanow.abc.core.UniException;
import romanow.abc.core.entity.EntityField;
import romanow.abc.core.export.ExCellCounter;
import romanow.abc.core.jdbc.SQLField;
import romanow.abc.core.jdbc.SQLFieldList;
import romanow.abc.core.mongo.DAO;
import romanow.abc.core.mongo.I_MongoDB;
import romanow.abc.core.mongo.RequestStatistic;

import java.util.ArrayList;
import java.util.HashMap;

class DAODAOLink implements I_DAOAccess {
        //-----------------------------------------------------------------------------------------------------------------
        @Override
        public void getField(EntityField ff, DAO dao) throws Exception{
            DAO dd = (DAO)ff.field.get(dao);
            ff.value = dd.toStringValue();
            }
    @Override
    public void getDBValue(EntityField ff, DAO dao, String prefix, Document out, int level, I_MongoDB mongo, HashMap<String, String> path, RequestStatistic statistic) throws UniException {
        try {
            DAO dd = (DAO) ff.field.get(dao);
            String pref = dao.getFieldPrefix(ff);
            if (pref != null)
                dd.getData(pref + "_", out, 0, null, statistic);
            else
                dao.error(prefix,ff);
            } catch (Exception ee) {
                //--------- Ссылку обнулять не надо
                //try {
                //    ff.field.set(dao, null);
                //    } catch (IllegalAccessException e) {}
                dao.error(prefix,ff);
                }
            }

    @Override
    public void updateField(EntityField ff, DAO dao, String value) throws Exception {
        DAO dd = (DAO)ff.field.get(dao);
        dd.parseValue(value);
        }

    @Override
    public void getXMLValues(EntityField ff, DAO dao, Row row, int cnt,HashMap<String,Integer> colMap) throws Exception {
        DAO dd = (DAO)ff.field.get(dao);
        String pref = dao.getFieldPrefix(ff);
        if (pref!=null)
            dd.getData(row,pref+"_",colMap);
        else
            dao.noField(3,ff);
        }

    @Override
    public void putXMLValues(EntityField ff, DAO dao, Row row, ExCellCounter cnt) throws Exception {
        DAO dd = (DAO)ff.field.get(dao);
        String pref = dao.getFieldPrefix(ff);
        if (pref!=null)
            dd.putData(row,cnt);
        else
            dao.noField(4,ff);
        }

    @Override
    public void loadDBValues(EntityField ff, DAO dao, DAO src, int level, I_MongoDB mongo) throws Exception {
        DAO proto = (DAO)ff.field.get(src);
        DAO clone = null;
        if (proto!=null){
            clone = (DAO)proto.getClass().newInstance();
            clone.loadDBValues(proto,0,null);
            }
        ff.field.set(dao,clone);                   // Ссылку копировать - КЛОН
        }

    @Override
    public void copyDBValues(EntityField ff, DAO dao, DAO src) throws Exception {
        DAO proto = (DAO)ff.field.get(dao);
        DAO clone = null;
        if (proto!=null){
            clone = (DAO)proto.getClass().newInstance();
            clone.copyDBValues(proto);
            }
        ff.field.set(dao,clone);
        }

    @Override
    public void putFieldValue(DAO dao, String prefix, Document out, int level, I_MongoDB mongo, EntityField ff) throws Exception {
        DAO dd = (DAO)ff.field.get(dao);
        String pref = dao.getFieldPrefix(ff);
        if (pref!=null)
            dd.putData(pref+"_",out,0,null);
        else
            dao.noField(2,ff);
        }
}
