package romanow.abc.core.mongo.access;

import org.apache.poi.ss.usermodel.Row;
import org.bson.Document;
import romanow.abc.core.UniException;
import romanow.abc.core.entity.*;
import romanow.abc.core.export.ExCellCounter;
import romanow.abc.core.mongo.*;

import java.util.HashMap;

class DAOEntityRefList implements I_DAOAccess {
        //-----------------------------------------------------------------------------------------------------------------
        @Override
        public void getField(EntityField ff, DAO dao){
            }
    @Override
    public void getDBValue(EntityField ff, DAO dao, String prefix, Document out, int level, I_MongoDB mongo, HashMap<String, String> path, RequestStatistic statistic) throws UniException {
        try {
            EntityRefList list2 = (EntityRefList) ff.field.get(dao);
            Class cc = list2.getTypeT();
            if (cc == null)
                return;
            String cname = cc.getSimpleName();
            boolean bb = level != 0 && cc != null && !(path != null && path.get(cname) == null);
            if (!bb)
                return;      // Имя поля = EntityLink совпадает с именем класса, на который ссылается
            Entity par1 = (Entity) cc.newInstance();
            long par2 = ((Entity) dao).getOid();
            I_DBQuery query = new DBQueryList().add("valid", true).add(dao.getClass().getSimpleName(), par2);
            EntityList res = mongo.getAllByQuery(par1, query, level - 1);
            list2.set(res);
            } catch (Exception ee) {
                throw UniException.bug(ee);
                }
        }
    @Override
    public void updateField(EntityField ff, DAO dao, String value) throws Exception {
        throw UniException.user("Обновление поля "+ff.name+" не поддерживается");
        }
    @Override
    public void getXMLValues(EntityField ff, DAO dao, Row row, int cnt, HashMap<String, Integer> colMap) throws Exception {
        row.getCell(cnt).getNumericCellValue();
        }

    @Override
    public void putXMLValues(EntityField ff, DAO dao, Row row, ExCellCounter cnt) throws Exception {
        row.createCell(cnt.getIdx()).setCellValue(0);
        }

    @Override
    public void loadDBValues(EntityField ff, DAO dao, DAO src, int level, I_MongoDB mongo) throws Exception {
        //TODO - клонировать из кэша....
        }

    @Override
    public void copyDBValues(EntityField ff, DAO dao, DAO src) throws Exception {
        //TODO - копировать без ссылок
        }

    @Override
    public void putFieldValue(DAO dao, String prefix, Document out, int level, I_MongoDB mongo, EntityField ff) throws Exception {
        }

    @Override
    public String createKotlinFieldDefine(EntityField ff) {
        //------------- Замена на прямой ArrayList ------------------------------------------------
        return ff.name+":ArrayList<"+ff.genericName+"> = ArrayList<"+ff.genericName+">()";
        //return ff.name+":EntityRefList<"+ff.genericName+"> = EntityRefList<"+ff.genericName+">()";
        }

}
