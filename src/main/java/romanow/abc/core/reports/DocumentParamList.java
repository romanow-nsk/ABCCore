package romanow.abc.core.reports;

public class DocumentParamList {
    private boolean landscape=false;
    private boolean full=false;
    private int nLines=1;
    public boolean isLandscape() { return landscape; }
    public DocumentParamList setLandscape() {
        landscape = true;
        return this;
        }
    public boolean isFull() { return full; }
    public DocumentParamList setFull() {
        full = true;
        return this;
        }
    public int getnLines() {
        return nLines; }
    public DocumentParamList setnLines(int nLines) {
        this.nLines = nLines;
        return this; }
}
