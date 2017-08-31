//
//
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.net.InetSocketAddress;
//import java.nio.ByteBuffer;
//import java.nio.channels.SelectionKey;
//import java.nio.channels.Selector;
//import java.nio.channels.SocketChannel;
//import java.nio.charset.Charset;
//import java.util.Scanner;
//
//public class NClient {
//	// 定义检测SocketChannel的Selector对象
//	private Selector selector = null;
//	static final int PORT = 30000;
//	// 定义处理编码和解码的字符集
////	private Charset charset = Charset.forName("UTF-8");
//	// 客户端SocketChannel
//	public static SocketChannel sc = null;
//	public void init() throws IOException {
//		selector = Selector.open();
//		InetSocketAddress isa = new InetSocketAddress("127.0.0.1", PORT);
//		// 调用open静态方法创建连接到指定主机的SocketChannel    172.20.10.3
//		sc = SocketChannel.open(isa);
//
//		// 设置该sc以非阻塞方式工作
//		sc.configureBlocking(false);
//		// 将SocketChannel对象注册到指定Selector
//		sc.register(selector, SelectionKey.OP_READ);
//		// 启动读取服务器端数据的线程
//		new ClientThread().start();
//		// 创建键盘输入流
//		Scanner scan = new Scanner(System.in);
////		while (scan.hasNextLine()) {
//			// 读取键盘输入
//			String line = scan.nextLine();
//			// 将键盘输入的内容输出到SocketChannel中
//			sc.write(ByteBuffer.wrap(XXX(line)));
////			sc.shutdownOutput();
//
////		}
////		sc.write(charset.encode("caolinainai"+"\n"));
//	}
//	// 定义读取服务器数据的线程
//	private class ClientThread extends Thread {
//		public void run() {
//			try {
//				while (selector.select() > 0) {
//					// 遍历每个有可用IO操作Channel对应的SelectionKey
//					for (SelectionKey sk : selector.selectedKeys()) {
//						// 删除正在处理的SelectionKey
//						selector.selectedKeys().remove(sk);
//						// 如果该SelectionKey对应的Channel中有可读的数据
//						if (sk.isReadable()) {
//							// 使用NIO读取Channel中的数据
//							SocketChannel sc = (SocketChannel) sk.channel();
//							ByteBuffer buff = ByteBuffer.allocate(1024);
//							String content = "$$$$";
//							while (sc.read(buff) > 0) {
//								content="";
//								sc.read(buff);
//								buff.flip();
////								content += charset.decode(buff);
//							}
//							if(content.equals("$$$$")){
//								sk.cancel();
//							}
//							else {
//								System.out.println();
//								System.out.println(buff);
//								Thread.sleep(100);
//								sc.register(selector,SelectionKey.OP_READ);
//							}
//					}}
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	public static byte[] XXX(String hexString){
//		if (hexString == null || hexString.equals("")) {
//			return null;
//		}
//		hexString = hexString.toUpperCase();
//		int length = hexString.length() / 2;
//		char[] hexChars = hexString.toCharArray();
//		byte[] d = new byte[length];
//		for (int i = 0; i < length; i++) {
//			int pos = i * 2;
//			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
//		}
//		return d;
//	}
//
//
//	private static byte charToByte(char c) {
//		return (byte) "0123456789ABCDEF".indexOf(c);
//	}
//
//	public static String YYY(byte[] src){
//		StringBuilder stringBuilder = new StringBuilder("");
//		if (src == null || src.length <= 0) {
//			return null;
//		}
//		for (int i = 0; i < src.length; i++) {
//			int v = src[i] & 0xFF;
//			String hv = Integer.toHexString(v);
//			if (hv.length() < 2) {
//				stringBuilder.append(0);
//			}
//			stringBuilder.append(hv);
//		}
//		return stringBuilder.toString();
//	}
//
//	public static void main(String[] args) throws IOException {
//		new NClient().init();
//	}
//}
////##01=11#101=ff#02=ff#102=ff#03=ff#103=ff#04=ff#104=ff#05=10000100100103#06=ff