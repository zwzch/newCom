import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import net.sf.json.JSONObject;


import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
/**
 * Created by zw on 17-7-17.
 */
public class ThreadManage {
    public static ArrayList<String> getRes=new ArrayList<>();
    private HashSet orderset = new HashSet();
    public ThreadManage(HashSet set) {
        this.orderset = set;
    }
    public ThreadManage() {
    }
    private static Properties prop = null;
    public static List<String> list;
    public void getSingleRes(String tempIp, int port) throws IOException {
        NClient.log.debug("单个连接请求的"+tempIp);
//        System.out.println(tempIp);

        for (Object ss:orderset) {
//            System.out.println(ss.toString());
            NClient.log.debug("连接发送的指令"+ss.toString());
            new Test().connect(tempIp,ss.toString());
        }
    }
    public int getAllres(int i) {
        int ipnum = Integer.parseInt(prop.getProperty("ipsize"));
        int busnum = Integer.parseInt(prop.getProperty("busnum"));
        int length = Integer.parseInt(prop.getProperty("length"));
        /*实际所用循环*/
        System.out.println(i);
        final int port=i;
        new getClient("127.0.0.1",port,list).start();
        return  0;
    }
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        ThreadManage tt = new ThreadManage();
                   tt.getAllres(33333);
    }
}

class getClient {
   private final String host;
   private final int port;
   private List list;

   public getClient(String host, int port, List list) {
       this.host = host;
       this.port = port;
       this.list = list;
   }
   public void start(){
       EventLoopGroup group = new NioEventLoopGroup();
       try {
           Bootstrap b = new Bootstrap();
           b.group(group)
                   .channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
               @Override
               protected void initChannel(SocketChannel socketChannel) throws Exception {
                   socketChannel.pipeline().addLast(new getAllHandler(list));
               }
           });
           System.out.println(host+port);
           ChannelFuture f = b.connect(host,port).sync();
           f.channel().closeFuture().sync();
       } catch (InterruptedException e) {
           e.printStackTrace();
       } finally {
           group.shutdownGracefully();
       }
   }


}

class getAllHandler extends SimpleChannelInboundHandler<ByteBuf>{
    private List  list;
    public getAllHandler(List ll ) {
        this.list=ll;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        for (Object order:list) {
            String temp = order.toString();
            ctx.writeAndFlush(Unpooled.copiedBuffer((temp),
                    CharsetUtil.UTF_8));
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        List tmpList = new ArrayList();
        for(int i=0;i<5;i++){
            tmpList.add(body.substring(i*14,(i+1)*14));
        }
        upperParse upperParse = new upperParse();
        uppermode uppermode=  upperParse.parseReg((ArrayList) tmpList);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid","uuid");
        jsonObject.put("buildmessage","buildmessage");
        jsonObject.put("cdate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        jsonObject.put("ctime",new SimpleDateFormat("HH:mm:ss").format(new Date()));
        jsonObject.put("status",uppermode.getStatus());
        jsonObject.put("borg","4dc*****");
        jsonObject.put("tem",uppermode.getIndoorTem());
        jsonObject.put("modset",uppermode.getMod());
        jsonObject.put("devicestatus",uppermode.getStatus());
        jsonObject.put("wind",uppermode.getWindMod());
        jsonObject.put("temset",uppermode.getTem());
        System.out.println(uppermode);
        System.out.println("Client received: " + body);
        System.out.println(jsonObject.toString());
        HttpClient httpClient = new HttpClient();
        super.read(ctx);
    }
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
    }
    public static void main(String[] args) {
        String body = "010300040001c5cb";
        String[] serl = new String[body.length()/2];
        for(int i=0;i<serl.length;i++){
            serl[i]=body.substring(i*2,(i+1)*2);
        }
        for (String str :serl) {
            System.out.println(str);
        }
    }
}