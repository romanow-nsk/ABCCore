package romanow.abc.core.mongo.access;

import org.apache.poi.ss.usermodel.Row;
import org.bson.Document;
import romanow.abc.core.UniException;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityField;
import romanow.abc.core.entity.EntityLink;
import romanow.abc.core.entity.EntityLinkList;
import romanow.abc.core.export.ExCellCounter;
import romanow.abc.core.mongo.DAO;
import romanow.abc.core.mongo.I_MongoDB;
import romanow.abc.core.mongo.RequestStatistic;

import java.util.HashMap;

class DAOEntityLinkList implements I_DAOAccess {
        //-----------------------------------------------------------------------------------------------------------------
        @Override
        public void getField(EntityField ff, DAO dao)  throws Exception{
            EntityLinkList list = (EntityLinkList)ff.field.get(dao);
            ff.value =list.getIdListString();
            }
    @Override
    public void getDBValue(EntityField ff, DAO dao, String prefix, Document out, int level, I_MongoDB mongo, HashMap<String, String> path, RequestStatistic statistic) throws UniException {
        EntityLinkList list=null;
        try {
            list = (EntityLinkList) ff.field.get(dao);
            String mm = (String) out.get(prefix + ff.name);
            if (mm==null){
                dao.error(prefix,ff);
                return;
                }
            list.parseIdList(mm);
            } catch (Exception ee) {
                dao.error(prefix, ff);
                return;
                }
        Class cc = list.getTypeT();
        if (cc == null)
            return;
        if (level==0)
            return;
        String cname = cc.getSimpleName();
        boolean bb = level != 0 && cc != null && !(path != null && path.get(cname) == null);
        if (!bb)
            return;
        for (int ii = 0; ii < list.size(); ii++) {
            EntityLink link2 = (EntityLink) list.get(ii);
            if (link2.getOid() == 0)
                continue;
            Entity two = null;
            try {
                two = (Entity) list.getTypeT().newInstance();
                } catch (Exception ee){
                    throw UniException.bug(ee);
                    }
            if (!mongo.getById(two, link2.getOid(), level - 1, ValuesBase.DeleteMode, path, statistic)) {
                System.out.println("Не найден " + list.getTypeT().getSimpleName() + " id=" + link2.getOid());
                link2.setOid(0);
                link2.setRef(null);
                }
            else{
                link2.setRef(two);
                }
            }
        }

    @Override
    public void updateField(EntityField ff, DAO dao, String value) throws Exception {
        EntityLinkList list = (EntityLinkList)ff.field.get(dao);
        list.parseIdList(value);
        }

    @Override
    public void getXMLValues(EntityField ff, DAO dao, Row row, int cnt, HashMap<String, Integer> colMap) throws Exception {
        EntityLinkList list = (EntityLinkList)ff.field.get(dao);
        list.parseIdList(row.getCell(cnt).getStringCellValue());
        }

    @Override
    public void putXMLValues(EntityField ff, DAO dao, Row row, ExCellCounter cnt) throws Exception {
        EntityLinkList list = (EntityLinkList)ff.field.get(dao);
        row.createCell(cnt.getIdx()).setCellValue(list.getIdListString());
        }

    @Override
    public void loadDBValues(EntityField ff, DAO dao, DAO src, int level, I_MongoDB mongo) throws Exception {
        EntityLinkList list = (EntityLinkList)ff.field.get(dao);
        EntityLinkList list2 = (EntityLinkList)ff.field.get(src);
        try {
            for(int ii=0;ii<list2.size();ii++){
                list.add(((EntityLink)list2.get(ii)).getOid());   // КОПИИИ !!!!!!!!!!!!!!!!!
                }
            } catch (Exception ee){
                dao.error("",ff);
                list = new EntityLinkList();
                }
        if (level!=0 && list.getTypeT()!=null){
            for(int ii=0;ii<list.size();ii++){
                EntityLink link3 = (EntityLink) list.get(ii);
                if (link3.getOid()==0)
                    continue;
                Entity two = (Entity)list.getTypeT().newInstance();
                if (!mongo.getById(two,link3.getOid(),level-1,null)) {
                    System.out.println("Не найден " + list.getTypeT().getSimpleName() + " id=" + link3.getOid());
                    link3.setOid(0);
                    link3.setRef(null);
                    }
                else
                    link3.setRef(two);
                }
            }
        }

    @Override
    public void copyDBValues(EntityField ff, DAO dao, DAO src) throws Exception {
        EntityLinkList list = (EntityLinkList)ff.field.get(dao);
        EntityLinkList list2 = (EntityLinkList)ff.field.get(src);
        try {
            for(int ii=0;ii<list2.size();ii++){
                list.add(((EntityLink)list2.get(ii)).getOid());
                }
            } catch (Exception ee){
                dao.error("",ff);
                }
        }

    @Override
    public void putFieldValue(DAO dao, String prefix, Document out, int level, I_MongoDB mongo, EntityField ff) throws Exception {
        EntityLinkList list = (EntityLinkList)ff.field.get(dao);
        if (level!=0){
            for(int ii=0;ii<list.size();ii++){
                EntityLink link2 = (EntityLink) list.get(ii);
                if (link2.getRef()==null)
                    continue;
                switch(link2.getOperation()){
                    case ValuesBase.OperationAdd:
                        long oid2 = mongo.add(link2.getRef(),level-1);
                        link2.setOid(oid2);
                        break;
                    case ValuesBase.OperationUpdate:
                        mongo.update(link2.getRef(),level-1);
                        break;
                    }
                }
            }
        out.put(prefix+ff.name,list.getIdListString());
        }

    @Override
    public String createKotlinFieldDefine(EntityField ff) {
        //------------- Замена на прямой ArrayList ------------------------------------------------
        return ff.name+":ArrayList<EntityLink<"+ff.genericName+">> = ArrayList<EntityLink<"+ff.genericName+">>()";
        //return ff.name+":EntityLinkList<"+ff.genericName+"> = EntityLinkList<"+ff.genericName+">()";
    }

}
