import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by zw on 17-7-23.
 */
public class controlParse {
    /*
    * 匹配控制命令
    * */
    public static String UUID = null;
    private   static HashMap<String,String> shotMap= null;
    private String tempIp;
    private int port;
    static {
        //映射的方法
        shotMap = new HashMap<>();
        shotMap.put("101","setSetStatus");
        shotMap.put("102","setSetMod");
        shotMap.put("103","setSetTem");
        shotMap.put("104","setSetWind");
        shotMap.put("05","setLocation");
        shotMap.put("01","setGetsetStatus");
        shotMap.put("02","setGetMod");
        shotMap.put("03","setGetTem");
        shotMap.put("04","setGetWind");
        shotMap.put("06","setisReadIndoorTem");
    }
    HashMap <String,String> orderMap;
    public controlParse() {
        orderMap = new HashMap<>();
    }
    public void Parse(String originOrder)  {
        String temp = originOrder.substring(2);
        String[] order = temp.split("#");
        configModel model = new configModel();
        for (String ss:order) {
            String []setorder = ss.split("=");
            if (setorder[0].equals("uuid"))
            {
                controlParse.UUID=setorder[1];//第一个参数是uuid 用来做管道的标识
            }else {
            orderMap.put(setorder[0],setorder[1]);}
        }
        Class clazz = configModel.class;
        Object obj = null;
        try {
            obj = clazz.newInstance();
        } catch (InstantiationException e) {
//            e.printStackTrace();
            NClient.log.debug("传入参数"+originOrder,e);
        } catch (IllegalAccessException e) {
//            e.printStackTrace();
            NClient.log.debug("传入参数"+originOrder,e);
        }
        /*
        * 反射映射
        * */
        for (String key:orderMap.keySet()) {
            String translate=shotMap.get(key);
            Method method = null;
            try {
                method = clazz.getMethod(translate,String.class);
            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
                NClient.log.debug("反射错误",e);
            }
            try {
                method.invoke(obj,orderMap.get(key));
            } catch (IllegalAccessException e) {
                NClient.log.debug("反射错误",e);

            } catch (InvocationTargetException e) {
                NClient.log.debug("反射错误",e);
            }
        }
        //反射为configmodel赋值
//        NClient.log.debug(obj);
        NClient.log.debug("序列化完成的obj"+obj.toString());
        upperParse upperParse = new upperParse();
        //序列化成指令存入set
        HashSet set =  upperParse.parseAll((configModel) obj);
        //调用线程管理方法
        ThreadManage manage = new ThreadManage(set);
        Parselocation(orderMap.get("05"));
        try {
            manage.getSingleRes(tempIp,port);
        } catch (IOException e) {
//            e.printStackTrace();
            NClient.log.debug("传入参数"+tempIp+port,e);
        }
        //调用获取单个的资源
//        for (Object ss:set) {
//            System.out.println(ss);
//        }
    }
    public void Parselocation(String location)
    {
        //        分析location的位置
        StringBuilder builder = new StringBuilder();
        builder.append(Integer.parseInt(location.substring(0,3))).append(".")
                .append(Integer.parseInt(location.substring(3,6))).append(".")
                .append(Integer.parseInt(location.substring(6,9))).append(".")
                .append(Integer.parseInt(location.substring(10,12)));

        this.tempIp=builder.toString();
        int intt = Integer.parseInt(location.substring(12,15));
        String temp =  Integer.toHexString(intt);
        if (temp.length()<2)
            temp="0"+temp;
//        System.out.println(temp);
        NClient.log.debug(temp);
    }
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
       String s = "11";

       String temp =  Integer.toHexString(Integer.parseInt(s));
        if (temp.length()<2)
            temp="0"+temp;
        System.out.println(temp);
    }
}
