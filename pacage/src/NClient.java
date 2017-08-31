import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
public class NClient {
	/*
	* selector客户端
	* */
	static {
		PropertyConfigurator.configure("/home/jinan/uppertest/log/log4j.properties");
	}
	public static Logger log = Logger.getLogger(NClient.class.getName());
	// 定义检测SocketChannel的Selector对象
	private Selector selector = null;
	static final int PORT = 9999;  //9999
	// 定义处理编码和解码的字符集
	public static Charset charset = Charset.forName("UTF-8");
	// 客户端SocketChannel
	public static SocketChannel sc = null;
	public static ClientThread clientThread;
	public void init() throws IOException {
		selector = Selector.open();
		InetSocketAddress isa = new InetSocketAddress("47.92.110.231",9999);
		// 调用open静态方法创建连接到指定主机的SocketChannel    172.20.10.3  47.92.110.231
		while (sc==null){
		try {
			sc = SocketChannel.open(isa);
		}
		catch (ConnectException e){
//			System.out.println("连接请求失败,3秒后重连...");
			log.error("连接请求失败,3秒后重连..."+"连接ip：47.92.110.231，连接端口："+PORT,e);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				log.error(e1.getMessage()+"连接ip：172.20.10.8，连接端口："+PORT);
			}
		}}
		sc.setOption(StandardSocketOptions.SO_RCVBUF,1024);
		sc.setOption(StandardSocketOptions.TCP_NODELAY,true);
		// 设置该sc以非阻塞方式工作
		sc.configureBlocking(false);
		// 将SocketChannel对象注册到指定Selector
		sc.register(selector, SelectionKey.OP_READ);
		// 启动读取服务器端数据的线程
		clientThread = new ClientThread(selector);
		clientThread.start();
		log.debug("clientThread开始对读队列进行监听");
			// 读取键盘输入
		String line = "LOGIN/AAAA";
			// 将键盘输入的内容输出到SocketChannel中
		sc.write(charset.encode(line));
		log.debug("向客户端发送信息："+"LOGIN/AAAA");
		//定期发送心跳包
		log.debug("开始发送心跳包...");
		new Thread(new HeartBeat(sc,selector)).start();

	}
	// 定义读取服务器数据的线程

	public static void main(String[] args) throws IOException {
		new NClient().init();
	}
}
class HeartBeat implements Runnable{
	//发送心跳包的线程
	private SocketChannel sc = null;
	private Selector selector;
	private Charset charset = Charset.forName("UTF-8");
	public HeartBeat(SocketChannel sc,Selector selector) {
		this.sc = sc;
		this.selector =selector;
	}
	@Override
	public void run() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
            public void run() {
               NClient.log.debug("-------设定要指定任务--------");
				try {
					NClient.log.debug("发送心跳...");
					sc.write(charset.encode("HEARTBEAT/HEA"));
					NClient.log.debug("发送数据成功");
				} catch (IOException e) {
//					e.printStac##01=ff#101=ff#02=ff#102=ff#03=ff#103=ff#04=ff#104=ff#05=192168001001011#06=ffTrace();
//					System.out.println("心跳发送失败！");
					NClient.log.warn("心跳发送失败");
					try {
						reCon();
					} catch (InterruptedException e1) {
						NClient.log.debug("重连失败",e);
// 						e1.printStackTrace();
					}
					sc = NClient.sc;
				}
			}
        }, 1000,1000*60);
	}
	public void reCon() throws InterruptedException {
		int Flag=0;
		try {
			InetSocketAddress isa = new InetSocketAddress("47.92.110.231", 9999);
			NClient.log.debug("重连ip47.92.110.231,重连端口9999");
			SocketChannel newNc = null;
			while (newNc==null) {
				try {
					Flag++;
					if (Flag==100)
					{
						NClient.log.error("无法重连服务器");
						break;}
					selector = Selector.open();
					newNc = SocketChannel.open(isa);
				}catch (ConnectException e){
//					System.out.println("连接请求失败，三秒后重连...");
					NClient.log.debug("连接服务端失败，10秒后重连...");
					Thread.sleep(10000);
				}
			}
			if(newNc!=null){
			NClient.sc=newNc;
			NClient.sc.configureBlocking(false);
			// 将SocketChannel对象注册到指定Selector
			NClient.sc.register(selector, SelectionKey.OP_READ);
			String line = "LOGIN/AAAA";
			// 将键盘输入的内容输出到SocketChannel中
			NClient.log.debug("重连发送LOGIN/AAAA");
			NClient.sc.write(charset.encode(line));
			new ClientThread(selector).start();}
		} catch (IOException e) {
//			e.printStackTrace();
			NClient.log.debug("重连发送失败",e);
		}
	}
}
class ClientThread extends Thread {
	public Selector selector;
	public String upperStr = "";
	public boolean Flag = false;
	public ClientThread(Selector ss){
		this.selector=ss;
	}
	public ArrayList orderList = new ArrayList();
	public static SelectionKey key = null;
	public void run() {
		try {
			while (selector.select() > 0) {
				// 遍历每个有可用IO操作Channel对应的SelectionKey
				for (SelectionKey sk : selector.selectedKeys()) {
					key = sk;
					// 删除正在处理的SelectionKey
					selector.selectedKeys().remove(sk);
					// 如果该SelectionKey对应的Channel中有可读的数据
					if (sk.isReadable()) {
						// 使用NIO读取Channel中的数据
						SocketChannel sc = (SocketChannel) sk.channel();
						ByteBuffer buff = ByteBuffer.allocate(1024);
						String content = "";
						int a=0;
						while ((a=sc.read(buff)) > 0) {
//							NClient.log.debug("读取数据成功");
							sc.read(buff);
							buff.flip();
							content += NClient.charset.decode(buff);
						}
						String judgement = content.substring(0, 3);
						if (!judgement.equals("HEA") && Flag == false) {
//							System.out.println(content);
//								log.debug("读取到信息"+content);
							String[] temp = content.split("&");
							content = upperStr + temp[0];
//                            System.out.println(content);
//
//							System.out.println(content);
                         orderList.add(content);
							if (temp.length != 1) {
								upperStr = temp[temp.length-1];
								if(temp.length>2){
									for(int i=1;i<=temp.length-2;i++){
										orderList.add(temp[i]);
//										System.out.println(content);
									}
								}
							}
							if(orderList.size()==11){
								Flag=true;
								System.out.println(upperStr);
								try{
								judgement=upperStr.substring(0,3);}catch (Exception e){
									NClient.log.debug("数据无粘包");
								}
								NClient.log.debug("读取adn完毕,judgement:"+judgement);
							}
						}
						if(Flag==true&&content!=null&&!content.equals("")){

							String normalJudge = content.substring(0, 2);
							if (normalJudge.equals("##")) {
								NClient.log.debug("开始解析正常指令" + content);
								new checkOrder(content).check();
							}
							if (judgement.equals("END")) {
								NClient.log.debug("进行序列化");
								for (Object obj : orderList) {
//									System.out.println(obj.toString());
									NClient.log.debug(obj.toString());
									new checkOrder(obj.toString()).check();
								}
								new checkOrder("END").check();
							}
							NClient.log.debug(content);
//						System.out.println(content);
							// 打印输出读取的内容
						}
//							new  controlParse().ParseorderList(content);
//							log.info("向checkOrder类的check方法传递参数"+content+"执行check方法");
//							System.out.println(content);
						NClient.log.debug("注册SelectionKey.OP_READ事件为下一次读取作准备");
//						if(content!=null&&!content.equals("")){
//						new checkOrder(content).check();}
						sk.interestOps(SelectionKey.OP_READ);
						//反序列化传入的信息
						// 为下一次读取作准备
//						NClient.log.debug("注册SelectionKey.OP_READ事件为下一次读取作准备");
					}
				}
			}
		} catch (IOException e){
			NClient.log.debug(e);
			NClient.log.debug("注册SelectionKey.OP_READ事件为下一次读取作准备");
			key.interestOps(SelectionKey.OP_READ);
		}
	}
}
//##uuid=xxxx#01=ff#101=01#02=ff#102=ff#03=ff#103=ff#04=ff#104=ff#05=192168001001011#06=ff