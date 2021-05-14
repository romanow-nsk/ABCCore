package romanow.abc.core.constants;

import romanow.abc.core.UniException;
import romanow.abc.core.entity.base.WorkSettingsBase;
import romanow.abc.core.entity.users.User;

public interface I_Environment {
    public User superUser();
    public Class applicationClass(int classType) throws UniException;
    public Object applicationObject(int classType) throws UniException;
    public String applicationName(int nameNype);
    public String applicationClassName(int classType);
    public int releaseNumber();
    public WorkSettingsBase currentWorkSettings();
    }
