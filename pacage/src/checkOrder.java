import com.sun.org.apache.xpath.internal.SourceTree;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw on 17-8-11.
 */
public class checkOrder {
    static {
        PropertyConfigurator.configure("/home/jinan/uppertest/log/log4j.properties");
    }
    public static Logger log = Logger.getLogger(checkOrder.class.getName());
    /*
    * 检查order
    * */
    public static List FloorList = new ArrayList();//静态FloorLIst
    private floormodel floormodel;
    private  String order ;
    private List flagList;
    public checkOrder(String order) {
        this.order = order;
    }
    public boolean check()  {
        String check=null;
        try{
         check= order.substring(0,3);
        System.out.println(check);
        }catch (Exception e){
            log.error("指令分解失败 传入的指令是"+order,e);
            NClient.clientThread.key.interestOps(SelectionKey.OP_READ);
//            e.printStackTrace();
//            System.out.println("指令分解失败");
        }

       if(!check.equals("")&&check!=null) {
           /*
           * 检查ADN
           * */
         if (check.equals("ADN")) {
//           System.out.println("ADN注入=====>");
             log.debug("ADN注入=====>");
             String[] testOrders  =null;
             try {
                 testOrders = order.split("#");//使用#号分割
             }catch (Exception e){
                 NClient.log.debug("指令解析失败+传入的参数是"+order,e);
             }
             String ip = "100.1."+testOrders[1]+"."+testOrders[2];
//           System.out.println("当前绑定ip："+ip);
             NClient.log.debug("当前绑定ip："+ip);
             String orignOrder = testOrders[3];
//           System.out.println(orignOrder);
//             NClient.log.debug(orignOrder);
                String[] tuids = orignOrder.split(";");//使用分号分割
                flagList = new ArrayList();
                floormodel = new floormodel(Integer.parseInt(testOrders[1]),ip);
                for (String ss:tuids) {
                    String[] flags=ss.split(":");//使用：分割
//                    System.out.println(ss);
                    floormodel.add(new Flag(flags[0],flags[1]));
                }
//             System.out.println(floormodel);
             this.FloorList.add(floormodel);
           }
           /*
           * 检查是否结束
           * */
           else if(check.equals("END")){
//               ThreadPoolManager tm  = new ThreadPoolManager();
//             System.out.println("开始初始化=========>"+check);
             log.debug("开始初始化=========>"+check);
             new Thread(new Runnable() {
                 @Override
                 public void run() {
                     while (true){
                         try {
                             for (Object ff:FloorList) {
                                ThreadgetAll tt = new ThreadgetAll((floormodel) ff);
                                tt.getTempJSon();
                             }
                             Thread.sleep(1000*60*3);
                         } catch (InterruptedException e) {
//                             e.printStackTrace();
                                log.error("异常中断"+e);
                         }
                     }
                 }
             }).start();

           }
           /*
           * 检查HEARTBEAT
           * */
           else if (check.equals("HEA")){
//               System.out.println("PONG");
           }
           /*
           * 检查TCP的指令
           * */
           else {
               NClient.log.debug("开始controlParse");
               new controlParse().Parse(order);
           }
       }
        return  true;
    }
}
