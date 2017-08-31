import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.HashSet;
import java.util.Scanner;

public class EchoClient {
    private final String host;
    private final int port;
    private String msg;
    private HashSet orderset;
    public EchoClient(String host,int port,HashSet set) {
        this.host = host;
        this.port = port;
        this.orderset=set;
    }
    public void start(){
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new EchoClientHandler(orderset));
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

    public static void main(String[] args) throws Exception {
        System.out.println("+++++++++++");
        Scanner sc = new Scanner(System.in);
        System.out.println("input ip");
        String ip = sc.next();
        System.out.println(ip);
        System.out.println("input port");
        final int port = sc.nextInt();
        HashSet set = new HashSet();
        set.add("030300020001259F");
        new EchoClient(ip, port,set).start();
    }
}