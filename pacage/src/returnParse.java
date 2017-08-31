import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by zw on 17-7-28.
 */
public class returnParse {
    private List returnlist;
    public List parseReturn(HashSet set){
        returnlist = new ArrayList();
        for (Object obj:set){
            String temp = obj.toString();
String check = temp.substring(2,4);

//
            if (obj==null) {
                returnlist.add("FFFFFF#01");
            }else if(check.equals("83")) {
                returnlist.add("FFFFFF#02");
            }else if(check.equals("86")){
                returnlist.add("FFFFFF#03");
            }else if(check.equals("06")){
                returnlist.add("RRRRRR#WW");
            }else {
                returnlist.add("RRRRRR#"+temp.substring(6,10));
            }
        }
        return returnlist;
    }
    public List parseHttp(HashSet set){
        List tempList = new ArrayList();
        for (Object obj:set){
            String temp = obj.toString();

        }
        return tempList;
    }
    public static void main(String[] args) {
//        HashSet orderset = new HashSet();
//        orderset.add("01 06 00 05 00 00 99 CB");
//        orderset.add("0i 86 0x XX XX");
//        returnParse parse = new returnParse();
//        System.out.println(parse.parseReturn(orderset));
    String temp = "0i03020001xxxx";

        System.out.println(temp.substring(6,10));
    }
}
