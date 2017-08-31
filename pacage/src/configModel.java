/**
 * Created by zw on 17-7-25.
 */
public class configModel {
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }



    public boolean isGetSetStatus() {
        return getSetStatus;
    }
    public void setGetsetStatus(String opinion){
        //System.out.println(opinion);
        if (opinion.trim().equals("11")) {
//            System.out.println(true);
            this.getSetStatus = true;
        }else {
            this.getSetStatus=false;
        }
    }
    public void setisReadIndoorTem(String opinion){
        //System.out.println(opinion);
        if (opinion.trim().equals("11")) {
//            System.out.println(true);
            this.isReadIndoorTem = true;
        }else {
            this.isReadIndoorTem = false;
        }
    }
    public boolean getReadIndoorTem(){
        return this.isReadIndoorTem;
    }
    public boolean isGetMod() {
        return getMod;
    }
    public void setGetMod(String opinion){
        if (opinion.equals("11")) {
            this.getMod = true;
        }else {
            this.getMod=false;
        }
    }
    public boolean isGetTem() {
        return getTem;
    }
    public void setGetTem(String opinion){
        if (opinion.equals("11")) {
            this.getTem = true;
        }else {
            this.getTem=false;
        }
    }

    public boolean isGetWind() {
        return getWind;
    }
    public void setGetWind(String opinion){
        if (opinion.equals("11")) {
            this.getWind = true;
        }else {
            this.getWind=false;
        }
    }
    private String setStatus;
    private boolean getSetStatus;
    private String setMod;
    private boolean getMod;
    private String setTem;
    private boolean getTem;
    private String setWind;
    private boolean getWind;
    private String location;
    private boolean isReadIndoorTem;



    public void setReadIndoorTem(String opinion) {
        if (opinion.equals("11")) {
            this.getWind = true;
        }else {
            this.getWind=false;
        }
    }

    private  String ip;
    private String port;
    public String getSetStatus() {
        return setStatus;
    }
    public void setSetStatus(String setStatus) {
        this.setStatus = setStatus;
    }
    public String getSetMod() {
        return setMod;
    }
    public void setSetMod(String setMod) {
        this.setMod = setMod;
    }
    public String getSetTem() {
        return setTem;
    }
    public void setSetTem(String setTem) {
        this.setTem = setTem;
    }
    public String getSetWind() {
        return setWind;
    }
    public void setGetSetStatus(boolean getSetStatus) {
        this.getSetStatus = getSetStatus;
    }
    public void setGetMod(boolean getMod) {
        this.getMod = getMod;
    }

    public boolean isReadIndoorTem() {
        return isReadIndoorTem;
    }

    public void setGetWind(boolean getWind) {
        this.getWind = getWind;
    }
    public void setSetWind(String setWind) {
        this.setWind = setWind;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location)
    {
        this.location = location;
        StringBuilder builder = new StringBuilder();
        builder.append(Integer.parseInt(location.substring(0,3))).append(".")
                .append(Integer.parseInt(location.substring(3,6))).append(".")
                .append(Integer.parseInt(location.substring(6,9))).append(".")
                .append(Integer.parseInt(location.substring(10,12)));
//        System.out.println(builder.toString());
        this.ip=builder.toString();
//        System.out.println(Integer.parseInt(location.substring(12,15)));
//        this.port=Integer.parseInt(location.substring(12,15));
        int intt = Integer.parseInt(location.substring(12,15));
        String temp =  Integer.toHexString(intt);
        if (temp.length()<2)
            temp="0"+temp;
        System.out.println(temp);
        this.port=temp;
//        System.out.println(Integer.parseInt(location.substring(0,3)));
//        System.out.println(Integer.parseInt(location.substring(3,6)));
//        System.out.println(Integer.parseInt(location.substring(6,9)));
//        System.out.println(Integer.parseInt(location.substring(9,12)));
    }

    @Override
    public String toString() {
        return "configModel{" +
                "setStatus='" + setStatus + '\'' +
                ", getSetStatus=" + getSetStatus +
                ", setMod='" + setMod + '\'' +
                ", getMod=" + getMod +
                ", setTem='" + setTem + '\'' +
                ", getTem=" + getTem +
                ", setWind='" + setWind + '\'' +
                ", getWind=" + getWind +
                ", location='" + location + '\'' +
                ", isReadIndoorTem=" + isReadIndoorTem +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }

    public static void main(String[] args) {
        String num = "192168001001011";
        configModel config = new configModel();
        config.setLocation(num);

    }
}
