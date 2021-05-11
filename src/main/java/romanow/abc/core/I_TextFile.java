package romanow.abc.core;


import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Created by romanow on 14.02.2018.
 */
public interface I_TextFile {
    public void load(BufferedReader in) throws UniException;
    public void save(BufferedWriter out) throws UniException;
}
