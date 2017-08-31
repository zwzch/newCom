import com.sun.org.apache.xpath.internal.SourceTree;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zw on 17-8-11.
 */
public class ThreadgetAll {
    /*
    * 获取传递轮巡类
    * */
    static {
        PropertyConfigurator.configure("/home/jinan/uppertest/log/log4j.properties");
    }
    public static Logger log = Logger.getLogger(ThreadgetAll.class.getName());
    public floormodel floormodel;
    public static String tempJSon;
    public static List tempOrderList;
    private static Properties prop = null;
    public static List<String> list;
    static {
        prop = new Properties();
        try {
            list = new ArrayList<>();
            list.add("0300020001");//读取是否开机
            list.add("0300030001");//读运行模式
            list.add("0300050001");//读风机模式
            list.add("0400000001");//读室内温度
            list.add("0300040001");//读设置温度
        } catch (Exception e) {
//            e.printStackTrace();
            NClient.log.debug(e);
            throw new RuntimeException(e);
        }
    }
    public ThreadgetAll(floormodel floormodel) {
        this.floormodel = floormodel;
    }
    public String getTempJSon(){
        String ip = floormodel.getIp();//获得ip
        for (Flag flag:floormodel.flagList) {//转码好的面板位置
            log.debug("当前轮巡的ip"+ip+"当前轮巡的adn"+flag.getADN()+"当前轮巡的bdevice"+flag.getBdvice());
//            System.out.println(flag.getADN());
//            System.out.println(flag.getBdvice());
            new getRes(ip,this.floormodel,flag.getADN(),flag.getBdvice()).run();
        }
        return null;
    }
    public static void main(String[] args) {
        System.out.println(UUID.randomUUID());
    }
}

class getRes implements Runnable{
    /*
    * 获取资源的Runnable
    * */
    public static List<String> list;
    private String pos;
    private String order;
    private String ip;
    private floormodel floormodel;
    private String bdevice;
    static {
        try {
            list = new ArrayList<>();
            list.add("0300020001");//读取是否开机
            list.add("0300030001");//读运行模式
            list.add("0300050001");//读风机模式
            list.add("0400000001");//读室内温度
            list.add("0300040001");//读设置温度
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public getRes(String ip, floormodel ff, String pos ,String bdevice) {//ip，floor模式，面板位置
        this.ip = ip;
        this.floormodel = ff;
        this.pos=pos;
        this.bdevice = bdevice;
    }
    @Override
    public void run() {
        try {
        List tmpList = new ArrayList();
        Socket socket = new Socket(ip,24798);//访问的端口
            checkOrder.log.debug("发出的ip和端口"+ip+":"+"24798");
            socket.setSoTimeout(3000);//收不到数据3秒后断开
            OutputStream os = null;
            InputStream is = null;
            os = socket.getOutputStream();
            is = socket.getInputStream();
            for (int i = 0; i< list.size();i++){
                byte[] sbuf2 = CRC_16.getSendBuf(pos +list.get(i));
                String sendorder = CRC16M.getBufHexStr(sbuf2);
                checkOrder.log.debug("sending......."+sendorder);
                os.write(StoHex(sendorder));
                os.flush();
                checkOrder.log.debug("成功刷写"+sendorder);
                byte[] bytes = new byte[8];
                is.read(bytes);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                System.out.println("====================");
                checkOrder.log.debug("成功接收");
                String order = HextoS(bytes);
                System.out.println("recive:"+order);
                tmpList.add(order);
            }
            is.close();
            os.close();
            socket.close();
            upperParse upperParse = new upperParse();
            uppermode uppermode=  upperParse.parseReg((ArrayList) tmpList);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("duid",UUID.randomUUID().toString());
            jsonObject.put("buildmessage","buildmessage");
            jsonObject.put("cdate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            jsonObject.put("ctime",new SimpleDateFormat("HH:mm:ss").format(new Date()));
            jsonObject.put("status",10);
            jsonObject.put("borg","4dc259ff-8750-4532-9240-de0e63f06776");
            jsonObject.put("bdevice",this.bdevice);
            jsonObject.put("tem",uppermode.getIndoorTem());
            jsonObject.put("modeset",uppermode.getMod());
            jsonObject.put("devicestatus",uppermode.getStatus());
            jsonObject.put("wind",uppermode.getWindMod());
            jsonObject.put("temset",uppermode.getTem());
            checkOrder.log.debug(uppermode);
            checkOrder.log.debug(jsonObject.toString());
            checkOrder.log.debug("当前ip"+ip);
            new HttpClient().connect("47.92.110.231",8888,jsonObject.toString());
        } catch (Exception e) {
//            e.printStackTrace();
            ThreadgetAll.log.debug("当前传感器无法获取数据",e);
            try {
                JSONObject ejsonObject = new JSONObject();
                ejsonObject.put("duid",UUID.randomUUID().toString());
                ejsonObject.put("buildmessage","buildmessage");
                ejsonObject.put("cdate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                ejsonObject.put("ctime",new SimpleDateFormat("HH:mm:ss").format(new Date()));
                ejsonObject.put("status",20);
                ejsonObject.put("borg","4dc259ff-8750-4532-9240-de0e63f06776");
                ejsonObject.put("bdevice",this.bdevice);
                ejsonObject.put("tem",0);
                ejsonObject.put("modeset",0);
                ejsonObject.put("devicestatus",0);
                ejsonObject.put("wind",0);
                ejsonObject.put("temset",0);
                System.out.println(ejsonObject.toString());
                NClient.log.info("发送"+ejsonObject);
                new HttpClient().connect("47.92.110.231",8888,ejsonObject.toString());
            } catch (Exception e1) {
//                e1.printStackTrace();
                NClient.log.debug("当前传感其无法发送数据",e1);
            }
        }

    }
    //转换为16进制的数组
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
    //16进制数组转换成字符串
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

