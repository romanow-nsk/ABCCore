package romanow.abc.core.dll;

import romanow.abc.core.UniException;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.mongo.DAO;
import romanow.abc.core.script.ScriptException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class DLLFunction {
    public final String name;
    public final int resType;
    public final String title;
    public final Method method;
    public final ArrayList<DLLField> params;
    public DLLFunction(String name, int result, Method method1, ArrayList<DLLField> params, String title) {
        this.name = name;
        this.resType = result;
        this.params = params;
        this.title = title;
        method = method1;
        method.setAccessible(true);
        }
    public String toString(){
        String ss =  "Функция: "+name+" "+ ValuesBase.constMap().getGroupMapByValue("DAOType").get(resType).name()+" "+title+"\n";
        for(DLLField zz : params)
            ss+=zz.toString();
        return ss;
        }
    // pars[0] - окружение
    public Object invoke(String header, Object pars[]) throws ScriptException {
        if (params.size()+1!=pars.length)
            throw new ScriptException(ValuesBase.SEIllegalFormat,"Несовпадение количества параметров: "+ header+" "+(params.size()+1)+"/"+pars.length);
        for(int i=1;i<pars.length;i++){
            DLLField fld = params.get(i-1);
            String name1 = pars[i].getClass().getSimpleName();
            String name2 = null;
            name2 = ValuesBase.dataTypes().getByCode(fld.type).cloneWrapper().getClass().getSimpleName();
            if (!name1.equals(name2))
                throw new ScriptException(ValuesBase.SEIllegalFormat,"Несовпадение типов параметров "+header+"."+fld.name+"["+(i-1)+"]: "+name1+"/"+name2);
                }
        try {
            return method.invoke(null,pars);
            } catch (IllegalAccessException e) {
                throw new ScriptException(ValuesBase.SEBug,"Ошибка вызова функции "+header+": "+e.toString());
            } catch (InvocationTargetException e) {
                throw new ScriptException(ValuesBase.SEBug,"Ошибка вызова функции "+header+": "+e.toString());
                }
        }
    }
