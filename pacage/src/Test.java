import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.SelectionKey;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 * Created by zw on 17-8-9.
 */
public class Test {
    private Charset charset = Charset.forName("UTF-8");
    public String connect(String ip,String order) {
        Socket socket = null;
        OutputStream os = null;
        InputStream is = null;
        String backorder = null;
        try {
            NClient.log.debug("xxxxxxxxxxxxxxxxxxxxx");
//            ip, 24798
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, 24798),500);
            socket.setSoTimeout(1000);
            System.out.println("sending.......");
            NClient.log.debug("单个指令sending......."+order);
            os = socket.getOutputStream();
//            System.out.println(order);
            os.write(StoHex(order));
            os.flush();
            NClient.log.debug("单个指令flush");
            is = socket.getInputStream();
            byte[] bytes = new byte[8];
            int m;
            is.read(bytes);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//        System.out.println("this is ..." + order);
//            System.out.println("recive:" + YYY(bytes));
            NClient.log.debug("单个指令recive:" + HextoS(bytes));
            backorder = HextoS(bytes);
            returnParse parse = new returnParse();
            HashSet resset = new HashSet();
            resset.add(backorder);
            List list =  parse.parseReturn(resset);
            for (Object ss:list) {
                NClient.sc.write(charset.encode("MESSAGE/"+controlParse.UUID+"#"+ss.toString()));
                NClient.log.debug(charset.encode("单个指令返回值MESSAGE/"+controlParse.UUID+"#"+ss.toString()));
            }
        } catch (Exception e) {
            NClient.log.debug("单个连接请求错误");
            NClient.clientThread.key.interestOps(SelectionKey.OP_READ);
            try {
                NClient.log.debug("MESSAGE/"+controlParse.UUID+"#"+"FFFFFFF");
                NClient.sc.write(charset.encode("MESSAGE/"+controlParse.UUID+"#"+"FFFFFFF"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }finally {
            try {
                is.close();
                os.close();
                socket.close();
            }catch (Exception e){
                NClient.log.debug("关闭失败");
            }
        }
//        for (int i = 0; i<sets.length; i++){
//         Socket socket = new Socket("100.1.1.1",24798);

        return backorder;
    }
    public static void main(String[] args) throws IOException {
        System.out.println("+++++++++++");
        Scanner sc = new Scanner(System.in);
        System.out.println("input ip");
        String ip = sc.next();
        System.out.println(ip);
        HashSet set = new HashSet();
        set.add("030300020001259F");
        HashSet returnSet = new HashSet();
        for (Object obj:set){
            Test tt = new Test();
           returnSet.add( tt.connect(ip,obj.toString()));
        }
    }
    public static byte[] StoHex(String hexString){
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }


    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String HextoS(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    }
//##01=ff#101=01#02=ff#102=ff#03=ff#103=ff#04=ff#104=ff#05=100001001001001#06=ff