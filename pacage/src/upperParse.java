

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by zw on 17-7-25.
 */
public class upperParse {
    private HashMap<String,String> orderMap =null;
    private HashSet<String> orderSet = null;
    private static  HashMap snap = new HashMap();
    static {
        snap.put(0,"setStatus");
        snap.put(1,"setMod");
        snap.put(2,"setWindMod");
        snap.put(3,"setIndoorTem");
        snap.put(4,"setTem");
    }
    public upperParse() {
        orderSet = new HashSet<>();
        orderMap = new HashMap<>();
    }
    public int running_status_get(configModel configModel){
        if(configModel.isGetSetStatus()){
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(configModel.getPort())
                .append("0300020001");
            String crc = CRC16M.getBufHexStr(CRC_16.getSendBuf(stringBuilder.toString()));
//            stringBuilder.append(crc);
//                .append("CRC");
//            System.out.println(stringBuilder.toString());
            orderSet.add(crc);
        }
//        StringBuilder stringBuilder = new StringBuilder();
//        String temp=null;
//        if(id_address<10)
//            temp="0"+id_address;
//        stringBuilder.append(temp)
//                .append(" 03 00 02 00 01 ")
//                .append("CRC");
//        System.out.println(stringBuilder.toString());

        return 0;
    }
    public int running_status_set( configModel configModel){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(configModel.getPort())
                .append("06000200");
        if (!configModel.getSetStatus().equals("ff"))
        {
            stringBuilder.append(configModel.getSetStatus());
            System.out.println(stringBuilder.toString());
            String crc = CRC16M.getBufHexStr(CRC_16.getSendBuf(stringBuilder.toString()));
//            stringBuilder.append(crc);
//         System.out.println(stringBuilder.toString());
            orderSet.add(crc);
        }
        return 0;
    }
    public int running_mode_get(configModel configModel){
        if(configModel.isGetMod()){
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(configModel.getPort())
                    .append("0300030001");
            String crc = CRC16M.getBufHexStr(CRC_16.getSendBuf(stringBuilder.toString()));

//            System.out.println(stringBuilder.toString());
            orderSet.add(crc);
        }
//        StringBuilder stringBuilder = new StringBuilder();
//        String temp=null;
//        if(id_address<10)
//            temp="0"+id_address;
//        stringBuilder.append(temp)
//                .append(" 03 00 02 00 01 ")
//                .append("CRC");
//        System.out.println(stringBuilder.toString());
        return 0;
    }
    public int running_mode_set(configModel configModel  ){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(configModel.getPort())
                .append("06000300");
        if (!configModel.getSetMod().equals("ff"))
        {
            stringBuilder.append(configModel.getSetStatus());
            String crc = CRC16M.getBufHexStr(CRC_16.getSendBuf(stringBuilder.toString()));

//            System.out.println(stringBuilder.toString());
            orderSet.add(crc);
        }
        return 0;
    }
    public int ventilator_mode_get(configModel configModel){
        if(configModel.isGetWind()){
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(configModel.getPort())
                    .append("0300050001");
            String crc = CRC16M.getBufHexStr(CRC_16.getSendBuf(stringBuilder.toString()));

//            System.out.println(stringBuilder.toString());
            orderSet.add(crc);
        }
        return 0;
    }
    public int ventilator_mode_set( configModel configModel ){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(configModel.getPort())
                .append("06000500");
        if (!configModel.getSetWind().equals("ff"))
        {
            stringBuilder.append(configModel.getSetStatus());
            String crc = CRC16M.getBufHexStr(CRC_16.getSendBuf(stringBuilder.toString()));

//            System.out.println(stringBuilder.toString());
            orderSet.add(crc);
        }
        return 0;
    }
    public float temp_indoor ( configModel configModel){
        if(configModel.isReadIndoorTem()){
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(configModel.getPort())
                    .append("0400000001");
            String crc = CRC16M.getBufHexStr(CRC_16.getSendBuf(stringBuilder.toString()));

//            System.out.println(stringBuilder.toString());
            orderSet.add(crc);
        }
        return 0;
    }
    public float temp_get (configModel configModel){
        if(configModel.isGetTem()){
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(configModel.getPort())
                    .append("0300040001");
            String crc = CRC16M.getBufHexStr(CRC_16.getSendBuf(stringBuilder.toString()));
            orderSet.add(crc);
        }
        return 0;
    }
    public int temp_set (configModel configModel)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(configModel.getPort())
                .append("060002");
        if (!configModel.getSetTem().equals("ff"))
        {
//            stringBuilder.append(" "+configModel.getSetStatus()).append(" CRC");
            char[] tempChar = Integer.toHexString(Integer.parseInt(configModel.getSetTem())).toCharArray();
            String[] temp ={"0","0","0","0"};
            int ptr=0;
            for (int i=(4-tempChar.length);i<=tempChar.length;i++){
                temp[i]= String.valueOf(tempChar[ptr]);
                ptr++;
            }
//            System.out.println(configModel.getSetTem());
            stringBuilder.append(temp[0]+temp[1]).append(temp[2]+temp[3]);
            String crc = CRC16M.getBufHexStr(CRC_16.getSendBuf(stringBuilder.toString()));
            orderSet.add(crc);
        }
        return 0;
    }
    public HashSet parseAll(configModel configModel){
        this.running_status_get(configModel);
        this.running_status_set(configModel);
        this.running_mode_get(configModel);
        this.running_mode_set(configModel);
        this.temp_get(configModel);
        this.temp_set(configModel);
        this.ventilator_mode_get(configModel);
        this.temp_indoor(configModel);
        this.ventilator_mode_set(configModel);
        return orderSet;
    }
    public uppermode parseReg(ArrayList list) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        String tempMethod = null;
        Class clazz = uppermode.class;
        Object obj = clazz.newInstance();
            for (int i=0;i<5;i++){
                String temSpl = (String) list.get(i);
                tempMethod = (String) snap.get(i);
                String[] serl = new String[temSpl.length()/2];
                for(int j=0;j<serl.length;j++){
                    //System.out.println(body.substring(i*14,(i+1)*14));
                    serl[j]=temSpl.substring(j*2,(j+1)*2);
                }
                Method method =clazz.getMethod(tempMethod,String.class);
//                System.out.println(temSpl[3]+temSpl[4]);
               if(i>2) {
                   method.invoke(obj, serl[3]+serl[4]);
               }else {
                   method.invoke(obj,serl[4]);
               }
               }
        System.out.println(obj);

//        Class clazz = configModel.class;
//        Object obj = clazz.newInstance();
//        for (String key:orderMap.keySet()) {
////            System.out.println(shotMap.get(key));
//            String translate=shotMap.get(key);
////            System.out.println(translate);
//            Method method = clazz.getMethod(translate,String.class);
//            method.invoke(obj,orderMap.get(key));
//        }
         return (uppermode) obj;
    }
    public static void main(String[] args) {
        // upperParse upperParse = new upperParse();
//        upperParse.running_status_get();
//       char[] tempChar = Integer.toHexString(350).toCharArray();
//        String[] temp ={"0","0","0","0"};
//        int ptr=0;
//        for (int i=(4-tempChar.length);i<=tempChar.length;i++){
//            temp[i]= String.valueOf(tempChar[ptr]);
//            ptr++;
//        }
//        for (String ss:temp) {
//            System.out.println(ss);
//        }
//
//      System.out.println(Integer.parseInt("011D",16));
        String crc = CRC16M.getBufHexStr(CRC_16.getSendBuf("110300020001"));
        System.out.println(crc);
    }
}
