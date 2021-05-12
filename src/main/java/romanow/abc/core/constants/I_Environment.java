package romanow.abc.core.constants;

import romanow.abc.core.entity.base.WorkSettingsBase;
import romanow.abc.core.entity.users.User;

public interface I_Environment {
    public String subjectAreaName();
    public String mongoDBName();
    public String mongoDBUser();
    public String mongoDBPassword();
    public String apkName();
    public String serverName();
    public User superUser();
    public Class valuesClass();
    public int releaseNumber();
    public WorkSettingsBase currentWorkSettings();
    public String modulePackage();
    }
