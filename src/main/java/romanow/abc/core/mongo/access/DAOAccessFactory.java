package romanow.abc.core.mongo.access;

import lombok.Getter;
import romanow.abc.core.ErrorList;
import romanow.abc.core.constants.ConstList;
import romanow.abc.core.constants.ConstMap;
import romanow.abc.core.constants.ConstValue;
import romanow.abc.core.constants.ValuesBase;

import java.util.HashMap;

public class DAOAccessFactory {
    @Getter private HashMap<Integer,I_DAOAccess> classMap  = new HashMap();
    public  DAOAccessFactory(){}
    public void init(){
        ConstList list = ValuesBase.constMap().getValuesList("DAOType");
        for(ConstValue constValue : list){
            String className = ValuesBase.DAOAccessPrefix+constValue.title();
            try {
                classMap.put(constValue.value(), (I_DAOAccess)Class.forName(className).newInstance());
                } catch (Exception ee){
                    ValuesBase.errorList().addError("Отсутствует класс доступа DAO для "+constValue.className());
            }
        }
    }
}