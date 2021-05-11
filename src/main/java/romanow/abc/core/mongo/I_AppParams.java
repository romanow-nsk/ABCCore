package romanow.abc.core.mongo;

import romanow.abc.core.entity.users.User;

public interface I_AppParams {
    public String mongoDBName();
    public String mongoDBUser();
    public String mongoDBPassword();
    public String ServerName();
    public String apkName();
    public User superUser();
}
