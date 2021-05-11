package romanow.abc.core.jdbc;

import java.sql.ResultSet;

public interface I_OnRecord {
    public void onRecord(ResultSet rs);
}
