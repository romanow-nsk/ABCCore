package romanow.abc.dataserver;

import com.google.gson.Gson;
import romanow.abc.core.API.RestAPIBase;
import romanow.abc.core.DBRequest;
import romanow.abc.core.constants.ConstList;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.EntityList;
import romanow.abc.core.entity.baseentityes.JLong;
import romanow.abc.core.entity.baseentityes.JString;
import romanow.abc.core.entity.users.Person;
import romanow.abc.core.entity.users.User;
import romanow.abc.core.utils.City;
import romanow.abc.core.utils.Street;
import retrofit2.Response;

import java.util.ArrayList;

public class DBExample implements I_DBTarget<RestAPIBase> {
    private static void putId(Response<JLong> ans){
        if (!ans.isSuccessful())
            System.out.println(ans.message());
        else
            System.out.println("id="+ans.body());
    }
    static EntityList<City> cityList=new EntityList<>();
    static EntityList<Street> strList=new EntityList<>();
    static EntityList<User> ulist=new EntityList<>();
    static EntityList<Person> plist = new EntityList<>();
    static Gson gson = new Gson();


    @Override
    public void createAll(RestAPIBase service, String pass) {
        try {
            String token = service.debugToken(ValuesBase.DebugTokenPass).execute().body().getValue();
            Response<JString> res = service.clearDB(token,ValuesBase.DebugTokenPass).execute();
            System.out.println(res.body());
            System.out.println(service.getConstAll(token).execute().body());
            ArrayList<ConstList> constList = service.getConstByGroups(token).execute().body();
            System.out.println(constList);
            putId(service.addUser(token,new User(ValuesBase.UserGuestType,"Гость","","","Гость","112233","89335555555")).execute());
            putId(service.addUser(token,new User(ValuesBase.UserAdminType,"Админ","Павел","Ильич","tt2","1234","89136666666")).execute());
            ulist = service.getUserList(token,ValuesBase.GetAllModeActual,0).execute().body();
            System.out.println(ulist);
            //------------------------- Улицы и НП
            service.addEntity(token,new DBRequest(new City("Новосибирск"),gson),0).execute();
            service.addEntity(token,new DBRequest(new City("Бердск"),gson),0).execute();
            service.addEntity(token,new DBRequest(new City("Коченево",ValuesBase.CCountry),gson),0).execute();
            cityList.load(service.getEntityList(token,"City",ValuesBase.GetAllModeActual,0).execute().body());
            System.out.println(cityList);
            service.addEntity(token,new DBRequest(new Street("Немировича-Данченко",ValuesBase.SStreet,cityList.get(0).getOid()),gson),0).execute();
            service.addEntity(token,new DBRequest(new Street("Экваторная",ValuesBase.SStreet,cityList.get(0).getOid()),gson),0).execute();
            service.addEntity(token,new DBRequest(new Street("Ватутина",ValuesBase.SStreet,cityList.get(0).getOid()),gson),0).execute();
            service.addEntity(token,new DBRequest(new Street("Гоголя",ValuesBase.SStreet,cityList.get(0).getOid()),gson),0).execute();
            service.addEntity(token,new DBRequest(new Street("Фрунзе",ValuesBase.SStreet,cityList.get(0).getOid()),gson),0).execute();
            service.addEntity(token,new DBRequest(new Street("Геодезическая",ValuesBase.SStreet,cityList.get(0).getOid()),gson),0).execute();
            service.addEntity(token,new DBRequest(new Street("Карла Маркса",ValuesBase.SProspect,cityList.get(0).getOid()),gson),0).execute();
            service.addEntity(token,new DBRequest(new Street("Ольги Жилиной",ValuesBase.SStreet,cityList.get(0).getOid()),gson),0).execute();
            service.addEntity(token,new DBRequest(new Street("Крылова",ValuesBase.SStreet,cityList.get(0).getOid()),gson),0).execute();
            service.addEntity(token,new DBRequest(new Street("Ленина",ValuesBase.SStreet,cityList.get(0).getOid()),gson),0).execute();
            service.addEntity(token,new DBRequest(new Street("Ленина",ValuesBase.SStreet,cityList.get(1).getOid()),gson),0).execute();
            service.addEntity(token,new DBRequest(new Street("Ленина",ValuesBase.SStreet,cityList.get(2).getOid()),gson),0).execute();
            strList.load(service.getEntityList(token,"Street",ValuesBase.GetAllModeActual,1).execute().body());
            System.out.println(strList);
            City city = cityList.get(0);
            //--------------------- Контрагенты ------------------------------------------------------------------------
            Person person = new Person("Иванов","Иван","Иванович","директор","9139999999","111@ru.ru");
            service.addEntity(token,new DBRequest(person,gson),1).execute();
            person = new Person("Петров","Иван","Иванович","директор","9139999990","222@ru.ru");
            service.addEntity(token,new DBRequest(person,gson),1).execute();
            person = new Person("Зайкова","Мария","Петровна","глав.бух.","9139999997","333@ru.ru");
            service.addEntity(token,new DBRequest(person,gson),1).execute();
            person = new Person("Волкова","Мария","Петровна","глав.бух.","9139999994","444@ru.ru");
            service.addEntity(token,new DBRequest(person,gson),1).execute();
            person = new Person("Зуева","Мария","Петровна","инженер","9139999991","444@ru.ru");
            service.addEntity(token,new DBRequest(person,gson),1).execute();
            person = new Person("Минаева","Мария","Петровна","инженер","9139999992","444@ru.ru");
            service.addEntity(token,new DBRequest(person,gson),1).execute();
            plist.load(service.getEntityList(token,"Person",ValuesBase.GetAllModeActual,0).execute().body());
            //-----------------------------------------------------------------------------------
        } catch (Exception ex) {
            System.out.println(ex.toString()+"\n");
            }
        }
    }

