package romanow.abc.core.ftp;

import romanow.abc.core.constants.ValuesBase;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientFileWriter extends Thread{
    private Socket sk;
    private DataOutputStream os;
    private OutputStream oss;
    private FileInputStream fs;
    private volatile long fileSize;
    private AsyncTaskBack back;
    private String fname;
    private String dstName;
    private String ip;
    private int port;
    private String pass;
    private void closeLoading(){
        try {
            os.close();
            sk.close();
            fs.close();
            }catch (Exception ee){}
        }
    public void onError(String ss){
        back.runInGUI(new Runnable() {
            @Override
            public void run() {
                back.onError("Ошибка выгрузки: "+ss);
                }
            });
        }
    public ClientFileWriter(String fname0, String dstName0, String ip0, int port0, String pass0, AsyncTaskBack back0){
        back = back0;
        fname = fname0;
        dstName = dstName0;
        ip = ip0;
        port = port0;
        pass = pass0;
        setName("ClientFileWriter");
        start();
        }
    public void run(){
         try {
             File ff = new File(fname);
             if (!ff.exists()) {
                 back.onError("Файл не открыт: " + fname);
                 return;
                 }
             fileSize = ff.length();
             sk = new Socket(ip, port+1);
             oss = sk.getOutputStream();
             os = new DataOutputStream(oss);
             fs = new FileInputStream(ff);
             os.writeUTF(pass);
             os.writeUTF(dstName);
             os.writeLong(fileSize);
             os.flush();
             int sz = ValuesBase.FileBufferSize;
             byte bb[] = new byte[(int)fileSize];
             fs.read(bb);
             os.write(bb);
             /*
             long fileSize0 = fileSize;
             byte bb[] = new byte[sz];
             int oldProc = 0;
             while (fileSize != 0) {
                 int sz2 = sz;
                 if (fileSize < sz){
                     byte cc[] = new byte[(int)fileSize];
                     fs.read(cc);
                     oss.write(cc);
                     fileSize=0;
                     }
                 else{
                     fs.read(bb);
                     oss.write(bb);
                     fileSize -= sz;
                    }
                 final int proc = (int) ((fileSize0 - fileSize) * 100 / fileSize0);
                 if (proc >= oldProc + 10) {
                     back.runInGUI(new Runnable() {
                         @Override
                         public void run() {
                             back.onMessage("Выгружено "+proc + " %");
                         }
                     });
                     oldProc = proc;
                 }
             }
             */
             os.flush();
             sk.getInputStream().read();            // Один байт обратно
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
                     back.onError("Ошибка выгрузки: "+ee.toString());
                 }
             });
         }
    }
}
