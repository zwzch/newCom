import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

/**
 * Created by zw on 17-6-20.
 */
public class EchoServerHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("11111111");
        ByteBuf in = (ByteBuf) msg;
//        System.out.println("Server recived:"+in.toString(CharsetUtil.UTF_8));
        byte [] req = new byte[in.readableBytes()];
        System.out.println(YYY(req));
        ctx.write(in);
//        将所接收的消息返回给发送者。注意，这还没有冲刷数据
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("").addListener(ChannelFutureListener.CLOSE);

//        冲刷所有待审消息到远程节点。关闭通道后，操作完成
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
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
//    这种使用 ChannelHandler 的方式体现了关注点分离的设计原则，
//        并简化业务逻辑的迭代开发的要求。处理程序很简单;
//        它的每一个方法可以覆盖到“hook（钩子）”在活动周期适当的点。
//        很显然，我们覆盖 channelRead因为我们需要处理所有接收到的数据。