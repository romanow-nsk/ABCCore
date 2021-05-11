package romanow.abc.core;

import romanow.abc.core.entity.Entity;

public interface I_Condition<T extends Entity> {
    public boolean test(T vv);
}
