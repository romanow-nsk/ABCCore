package romanow.abc.core.ftp;

import romanow.abc.core.constants.ValuesBase;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.Socket;

public class ClientFileReader extends Thread{
    private Socket sk;
    private DataInputStream is;
    private DataOutputStream os;
    private String localDir;
    private String fname;
    private String ip;
    private int port;
    private AsyncTaskBack back;
    private void closeLoading(){
        try {
            os.close();
            is.close();
            sk.close();
            }catch (Exception ee){}
        }
    public ClientFileReader(String localDir0, String fname0, String ip0, int port0, AsyncTaskBack back0){
        port = port0;
        localDir = localDir0;
        fname = fname0;
        ip = ip0;
        back = back0;
        start();
        }
    public void run(){
         try {
             final ByteArrayOutputStream out = new ByteArrayOutputStream();
             Socket sk = new Socket(ip, port+1);
             DataOutputStream os = new DataOutputStream(sk.getOutputStream());
             DataInputStream is = new DataInputStream(sk.getInputStream());
             os.writeUTF(fname);
             int fileSize0 = is.readInt();
             if (fileSize0 <= 0) {
                 closeLoading();
                 back.runInGUI(new Runnable() {
                     @Override
                     public void run() {
                         back.onError("Ошибка загрузки - нет файла");
                     }
                 });
                 return;
             }
             int sz = ValuesBase.FileBufferSize;
             byte bb[] = new byte[sz];
             int fileSize = fileSize0;
             int oldProc = 0;
             while (fileSize != 0) {
                 int sz2 = fileSize > sz ? sz : fileSize;
                 if (is.available() < sz2) continue;
                 is.read(bb, 0, sz2);
                 out.write(bb, 0, (int) sz2);
                 fileSize -= sz2;
                 final int proc = (int) ((fileSize0 - fileSize) * 100 / fileSize0);
                 if (proc >= oldProc + 10) {
                     back.runInGUI(new Runnable() {
                         @Override
                         public void run() {
                             back.onMessage("Загружено "+proc + " %");
                         }
                     });
                     oldProc = proc;
                 }
             }
             out.flush();
             final FileOutputStream fs = new FileOutputStream(localDir + "/" + fname);
             fs.write(out.toByteArray());
             out.close();
             fs.close();
             closeLoading();
             back.runInGUI(new Runnable() {
                 @Override
                 public void run() {
                     back.onFinish(true);
                 }
             });
         } catch (final Exception ee){
             back.runInGUI(new Runnable() {
                 @Override
                 public void run() {
                     back.onError("Ошибка загрузки: "+ee.toString());
                 }
             });
         }
    }
}
