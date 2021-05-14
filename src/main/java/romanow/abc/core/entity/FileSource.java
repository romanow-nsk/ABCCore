package romanow.abc.core.entity;

import org.apache.poi.ss.usermodel.Row;

public class FileSource {
    public final String module;
    public final String title;
    public final String comment;
    public final String fileName;
    public final long fileSize;
    public FileSource(String module0, String title, String comment, String fileName, long size) {
        module = module0;
        this.title = title;
        this.comment = comment;
        this.fileName = fileName;
        fileSize = size;
        }
}
