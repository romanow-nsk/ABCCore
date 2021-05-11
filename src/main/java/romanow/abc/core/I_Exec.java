package romanow.abc.core;

import romanow.abc.core.entity.Entity;

public interface I_Exec<T extends Entity> {
    public void exec(T vv);
}
