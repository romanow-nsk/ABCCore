package romanow.abc.core.ftp;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class HTTPConnection {
    private Proxy httpProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("217.71.138.1", 8080));
    public byte []recieveBin(String url,String ip, int port, boolean withProxy) throws IOException {
        StringBuffer ss=new StringBuffer();
        URL url2 = new URL(url);
        HttpURLConnection connection;
        if (withProxy)
            connection= (HttpURLConnection) url2.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port)));
        else
            connection= (HttpURLConnection) url2.openConnection();
        connection.setReadTimeout(10000);
        connection.getResponseCode();
        InputStream stream = connection.getErrorStream();
        if (stream == null) {
            stream = connection.getInputStream();
            }
        //int n=connection.getContentLength();
        //byte out[]=new byte[n];
        //stream.read(out);
        //stream.close();
        ByteArrayOutputStream xx = new ByteArrayOutputStream();
        int vv=0;
        while ((vv=stream.read())!=-1){
            xx.write(vv);
            }
        return xx.toByteArray();
    }

    public StringBuffer recieve(String url,String ip, int port, boolean withProxy) throws IOException{
        StringBuffer ss=new StringBuffer();
        URL url2 = new URL(url);
        HttpURLConnection connection;
        if (withProxy)
            connection= (HttpURLConnection) url2.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port)));
        else
            connection= (HttpURLConnection) url2.openConnection();
        connection.setReadTimeout(10000);
        connection.getResponseCode();
        InputStream stream = connection.getErrorStream();
        if (stream == null) {
            stream = connection.getInputStream();
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line=null;
        while((line=in.readLine())!=null){
            ss.append(line+"\n");
            }
        in.close();
        return ss;
        }
}

