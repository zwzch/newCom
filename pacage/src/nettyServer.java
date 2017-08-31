import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * Created by zw on 17-7-17.
 */
public class nettyServer {
    private final  int port;
    public nettyServer(int port) {
        this.port = port;
    }
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Please input port");
        Scanner sc  = new Scanner(System.in);
        new nettyServer(sc.nextInt()).start();
    }
    private void start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(port)).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new EchoServerHandler());
                }
            });
            ChannelFuture f = b.bind(port).sync();
            System.out.println(nettyServer.class.getName() + " started and listen on " + f.channel().localAddress());
//           绑定的服务器;sync 等待服务器关闭
            f.channel().closeFuture().sync();
//           关闭 channel和块，直到它被关闭
        }finally {
            group.shutdownGracefully().sync();
//           关机的 EventLoopGroup，释放所有资源。
        }
    }
}
