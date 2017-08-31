/**
 * Created by zw on 17-8-14.
 */
public  class Flag{
    private String ADN;
    private String bdvice;

    public Flag(String ADN, String bdvice) {
        this.ADN = ADN;
        this.bdvice = bdvice;
    }

    public String getADN() {
        return ADN;
    }

    public void setADN(String ADN) {
        this.ADN = ADN;
    }

    public String getBdvice() {
        return bdvice;
    }

    public void setBdvice(String bdvice) {
        this.bdvice = bdvice;
    }
}
