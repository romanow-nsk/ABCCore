package romanow.abc.core.reports;

public interface TableCallBack {
    public void rowSelected(int row);
    public void colSelected(int col);
    public void cellSelected(int row, int col);
    public void onClose();
    }
