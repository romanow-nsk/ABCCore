package romanow.abc.core;

import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityLink;

public interface I_ExecLink<T extends EntityLink<? extends Entity>>  {
    public void exec(T vv);
}
