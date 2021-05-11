package romanow.abc.core.reports;

public class TableCol {
    public final static int AlignLeft=0;
    public final static int AlignCenter=1;
    public final static int AlignRight=2;
    String name="";
    int size;
    int hexBackColor=0;
    int align=AlignLeft;
    int linkIndex=0;                // Индекс сноски
    public TableCol(String name, int size) {
        this.name = name;
        this.size = size;
        }
    public TableCol setAlign(int align0){
        align = align0;
        return this;
        }
    public TableCol setLinkIndex(int index0){
        linkIndex = index0;
        return this;
    }
    public TableCol(String name, int size, int backColor) {
        this.name = name;
        this.size = size;
        hexBackColor = backColor;
    }
    public String getName() {
        return name;
        }
    public int getSize() {
        return size;
        }
}
