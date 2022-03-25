package romanow.abc.core.API;

import retrofit2.Call;
import retrofit2.Response;
import romanow.abc.core.UniException;
import romanow.abc.core.Utils;
import romanow.abc.core.constants.ValuesBase;

import java.io.IOException;

public abstract class APICallSynch<T> {
    public abstract Call<T> apiFun();
    public APICallSynch(){}
    public T call()throws UniException {
        String mes="";
        String mes1="";
        Response<T> res;
        long tt;
        try {
            tt = System.currentTimeMillis();
            res = apiFun().execute();
            } catch (Exception ex) {
                throw UniException.bug(ex);
                }
        if (!res.isSuccessful()){
            if (res.code()== ValuesBase.HTTPAuthorization){
                mes =  "Сеанс закрыт " + Utils.httpError(res);
                throw UniException.io(mes);
                }
            try {
                mes1 = "Ошибка " + res.message() + " (" + res.code() + ")";
                mes = mes1 + "\n"+res.errorBody().string();
                }
                catch (IOException ex){ mes += "\nОшибка: "+ex.toString(); }
            throw UniException.io(mes);
            }
        return res.body();
    }
}