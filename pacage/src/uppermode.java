/**
 * Created by zw on 17-7-25.
 */
public class uppermode {
    private int status;
    private int mod;
    private int tem;
    private int windMod;
    private int indoorTem;
    private String bdvice;
    public int getIndoorTem() {
        return indoorTem;
    }

    public void setIndoorTem(String indoorTem) {
        this.indoorTem =  Integer.parseInt(indoorTem,16);
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(String status) {
//        int tmpstatus = Integer.parseInt(status);
//        this.status = status;
        if (status.equals("01")){
            this.status = 10;
        }else {
            this.status=20;
        }
    }
    public int getMod() {
        return mod;
    }
    public void setMod(String mod) {
        if (mod.equals("01")){
            this.mod = 10;
        }else if (mod.equals("02"))
        {
            this.mod = 20;
        }else {
            this.mod = 30;
        }
    }
    public int getTem() {
        return tem;
    }
    public void setTem(String tem) {
        this.tem =  Integer.parseInt(tem,16);
    }
    public int getWindMod() {
        return windMod;
    }
    public void setWindMod(String windMod) {
     if (windMod.equals("00"))
     {
         this.windMod = 13;
     }else if (windMod.equals("01")){
         this.windMod = 12;
     }else if (windMod.equals("02")){
         this.windMod = 11;
     }else {
         this.windMod  = 10;
     }
//        this.windMod = windMod;
    }

    @Override
    public String toString() {
        return "uppermode{" +
                "status='" + status + '\'' +
                ", mod='" + mod + '\'' +
                ", tem='" + tem + '\'' +
                ", windMod='" + windMod + '\'' +
                ", indoorTem='" + indoorTem + '\'' +
                '}';
    }

    public static void main(String[] args) {
        uppermode pp = new uppermode();

    }
}
