import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.apache.tomcat.util.buf.ByteChunk;


import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;

/**
 * Created by zw on 17-6-20.
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

public EchoClientHandler(HashSet set){
    this.orderset = set;
    }
  private HashSet orderset;
//    private Charset charset = Charset.forName("UTF-8");
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        for (Object order:orderset) {
            String temp = order.toString();
            System.out.println(YYY(XXX(temp)));
            ByteBuf resp1 = Unpooled.copiedBuffer(XXX(temp));
            ByteBufOutputStream outputStream = new ByteBufOutputStream(resp1);

            ctx.writeAndFlush(resp1);
        }
    }
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("read....");
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);

//        String body = new String(req, "UTF-8");
//        System.out.println("Client received: " + body);
        String body = YYY(req);
        System.out.println(body);
        returnParse parse = new returnParse();
        HashSet resset = new HashSet();
        resset.add(body);
        List list =  parse.parseReturn(resset);
//        for (Object ss:list) {
//            NClient.sc.write(charset.encode(ss.toString()));
//        }

        //3
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {                    //4
        cause.printStackTrace();
        ctx.close();
    }
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {

    }


    public static byte[] XXX(String hexString){
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

    public static String YYY(byte[] src){
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
