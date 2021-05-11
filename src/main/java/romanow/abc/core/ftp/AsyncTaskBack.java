package romanow.abc.core.ftp;

public interface AsyncTaskBack {
    public void runInGUI(Runnable run);
    public void onError(String mes);
    public void onMessage(String mes);
    public void onFinish(boolean result);
}
