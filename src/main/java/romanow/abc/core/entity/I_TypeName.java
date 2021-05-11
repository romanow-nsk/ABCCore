package romanow.abc.core.entity;

import romanow.abc.core.UniException;

public interface I_TypeName {
    public String typeName();
    public int typeId();
    public Class typeClass() throws UniException;
}
