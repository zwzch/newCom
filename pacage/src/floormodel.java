import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw on 17-8-12.
 */
public class floormodel {
    private int floorid;
    private String ip;
    public  List orderList;
    public  List<Flag> flagList;
    public floormodel(int floorid, String ip) {
        this.floorid = floorid;
        this.ip = ip;
        this.orderList = new ArrayList();
        flagList = new ArrayList();
    }
    @Override
    public String toString() {
        return "floormodel{" +
                "floorid='" + floorid + '\'' +
                ", ip='" + ip + '\'' +
                ", orderList=" + orderList +
                '}';
    }

    public int getFloorid() {
        return floorid;
    }

    public void setFloorid(int floorid) {
        this.floorid = floorid;
    }

    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public boolean add(Flag info){
           String temp = Integer.toHexString(Integer.parseInt(info.getADN()));
        if (temp.length()<2)
            temp="0"+temp;
        this.orderList.add(temp);
        info.setADN(temp);
       return this.flagList.add(info);
    }

    public boolean delete(){
        return  true;
    }
}
