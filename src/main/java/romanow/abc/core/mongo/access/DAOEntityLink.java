package romanow.abc.core.mongo.access;

import org.apache.poi.ss.usermodel.Row;
import org.bson.Document;
import romanow.abc.core.UniException;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityField;
import romanow.abc.core.entity.EntityLink;
import romanow.abc.core.export.ExCellCounter;
import romanow.abc.core.mongo.DAO;
import romanow.abc.core.mongo.I_MongoDB;
import romanow.abc.core.mongo.RequestStatistic;

import java.util.HashMap;

class DAOEntityLink implements I_DAOAccess {
        //-----------------------------------------------------------------------------------------------------------------
        @Override
        public void getField(EntityField ff, DAO dao) throws Exception{
            ff.value = "" + ((EntityLink)ff.field.get(dao)).getOid();
            }
    @Override
    public void getDBValue(EntityField ff, DAO dao, String prefix, Document out, int level, I_MongoDB mongo, HashMap<String, String> path, RequestStatistic statistic) throws UniException {
        EntityLink<Entity> link  = new EntityLink<>();
        Entity two = null;
        boolean bb;
        try {
            link = (EntityLink<Entity>) ff.field.get(dao);
            if (out.get(prefix + ff.name)==null){
                dao.error(prefix,ff);
                return;
                }
            link.setOid(((Long) out.get(prefix + ff.name)).longValue());
            Class cc = link.getTypeT();
            if (cc==null)
                return;
            String cname = cc.getSimpleName();
            bb = level != 0 && link.getOid() != 0 && !(path != null && path.get(cname) == null);
            if (!bb)
                return;
            two = (Entity) link.getTypeT().newInstance();
            if (!mongo.getById(two, link.getOid(), level - 1)) {
                long oid = link.getOid();
                link.setOid(0);
                link.setRef(null);
                throw UniException.user("Не найден: oid="+out.get("oid").toString()+" "+dao.getClass().getSimpleName()+"."+ff.name+"("+cname+") id="+ oid);
                }
            else{
                link.setRef(two);
                }
           } catch (Exception ee) {
                //try {
                link.setOid(0);
               //ff.field.setLong(dao, 0L);
               //     } catch (IllegalAccessException e) {}
                throw UniException.bug(ee);
                }
            }

    @Override
    public void updateField(EntityField ff, DAO dao, String value) throws Exception {
        EntityLink link = (EntityLink)ff.field.get(dao);
        link.setOid(Long.parseLong(value));
    }

    @Override
    public void getXMLValues(EntityField ff, DAO dao, Row row, int cnt, HashMap<String, Integer> colMap) throws Exception {
        EntityLink link = (EntityLink)ff.field.get(dao);
        link.setOid(((long)row.getCell(cnt).getNumericCellValue()));
        }

    @Override
    public void putXMLValues(EntityField ff, DAO dao, Row row, ExCellCounter cnt) throws Exception {
        EntityLink link = (EntityLink)ff.field.get(dao);
        row.createCell(cnt.getIdx()).setCellValue(link.getOid());
        }

    @Override
    public void loadDBValues(EntityField ff, DAO dao, DAO src, int level, I_MongoDB mongo) throws Exception {
        EntityLink link = (EntityLink)ff.field.get(dao);
        EntityLink link2 = (EntityLink)ff.field.get(src);
        link.setOid(link2.getOid());
        if (level!=0 && link.getTypeT()!=null && link.getOid()!=0){
            Entity two = (Entity)link.getTypeT().newInstance();
            if (!mongo.getById(two,link.getOid(),level-1,null)) {
                System.out.println("Не найден " + link.getTypeT().getSimpleName() + " id=" + link.getOid());
                link.setOid(0);
                link.setRef(null);
                }
            else
                link.setRef(two);
            }
        }

    @Override
    public void copyDBValues(EntityField ff, DAO dao, DAO src) throws Exception {
        EntityLink link = (EntityLink)ff.field.get(dao);
        EntityLink link2 = (EntityLink)ff.field.get(src);
        try {
            link.setOid(link2.getOid());
            link.setRef(null);
            } catch (Exception ee){
                dao.error("",ff);
                }
        }

    @Override
    public void putFieldValue(DAO dao, String prefix, Document out, int level, I_MongoDB mongo, EntityField ff) throws Exception {
        EntityLink link = (EntityLink)ff.field.get(dao);
        long oid = link.getOid();
        if (level!=0 && link.getRef()!=null){
            switch(link.getOperation()){
                case ValuesBase.OperationAdd:
                    oid = mongo.add(link.getRef(),level-1);
                    link.setOid(oid);
                    break;
                case ValuesBase.OperationUpdate:
                    mongo.update(link.getRef(),level-1);
                    break;
                }
            }
        out.put(prefix+ff.name,link.getOid());
        }

        @Override
    public String createKotlinFieldDefine(EntityField ff) {
        return ff.name+":EntityLink<"+ff.genericName+"> = EntityLink<"+ff.genericName+">()";
        }
}
