package com.haven.simplej.tcp;

import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.tcp.pool.TcpPoolObjectFactory;
import com.haven.simplej.text.StringUtil;
import com.haven.simplej.util.BytesUtil;
import com.vip.vjtools.vjkit.base.type.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Map;


/**
 * tcp 协议的客户端工具
 * @author: havenzhang
 * @date: 2018/9/30 14:46
 * @version 1.0
 */
@Slf4j
public class TcpClient {


	static {
		initPool();
	}

	// 创建对象池
	static GenericKeyedObjectPool<String, Socket> pool;

	/**
	 * 初始化对象池
	 * @return GenericKeyedObjectPool
	 */
	public static void initPool() {
		// 创建池对象工厂
		KeyedPooledObjectFactory<String, Socket> factory = new TcpPoolObjectFactory();

		GenericKeyedObjectPoolConfig poolConfig = new GenericKeyedObjectPoolConfig();
		// 最大空闲数
		poolConfig.setMaxIdlePerKey(5);
		// 最小空闲数, 池中只有一个空闲对象的时候，池会在创建一个对象，并借出一个对象，从而保证池中最小空闲数为1
		poolConfig.setMinIdlePerKey(1);
		// 最大池对象总数
		poolConfig.setMaxTotal(20);
		// 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
		poolConfig.setMinEvictableIdleTimeMillis(1800000);
		// 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
		poolConfig.setTimeBetweenEvictionRunsMillis(1800000 * 2L);
		// 在获取对象的时候检查有效性, 默认false
		poolConfig.setTestOnBorrow(true);
		// 在归还对象的时候检查有效性, 默认false
		poolConfig.setTestOnReturn(false);
		// 在空闲时检查有效性, 默认false
		poolConfig.setTestWhileIdle(false);
		// 最大等待时间， 默认的值为-1，表示无限等待。
		poolConfig.setMaxWaitMillis(5000);
		// 是否启用后进先出, 默认true
		poolConfig.setLifo(true);
		// 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
		poolConfig.setBlockWhenExhausted(true);
		// 每次逐出检查时 逐出的最大数目 默认3
		poolConfig.setNumTestsPerEvictionRun(3);

		// 创建对象池
		pool = new GenericKeyedObjectPool<String, Socket>(factory, poolConfig);

	}

	/**
	 * 文件传输
	 * @param fileBytes 文件流
	 * @return Pair<byte   [   ]   ,   byte   [   ]> 报文头和报文体
	 */
	public static Pair<byte[], byte[]> transfer(String host, int port, int timeout, Map<String, String> header,
			Map<String, String> params, byte[] fileBytes) {
		//从对象池中获取对象
		String key = host + ":" + port;
		Socket socket = null;
		byte[] headerBuf;
		byte[] bodyBuf;
		long start = System.currentTimeMillis();
		Pair<byte[], byte[]> response;
		try {
			byte[] headerBytes = StringUtil.convert2QueryString(header).getBytes(Charset.forName("UTF-8"));
			byte[] paramsBytes = StringUtil.convert2QueryString(params).getBytes(Charset.forName("UTF-8"));
			socket = pool.borrowObject(key);
			socket.setSoTimeout(timeout);
			OutputStream socketOut;
			InputStream is;
			socketOut = socket.getOutputStream();
			//头部长度
			int headerLen = headerBytes.length;
			log.debug("send header len:" + headerLen);
			//报文体长度
			int bodyLen = paramsBytes.length;
			log.debug("send body len:" + headerLen);

			//文件流长度
			int fileByteLen = 0;
			if (fileBytes != null) {
				fileByteLen = fileBytes.length;
			}
			socketOut.write(StringUtil.Int2ByteArray(headerLen)); // 报文头的长度size
			socketOut.write(StringUtil.Int2ByteArray(bodyLen)); // 报文体的长度size
			socketOut.write(StringUtil.Int2ByteArray(fileByteLen)); // 报文体的长度size
			socketOut.write(StringUtil.Int2ByteArray(4)); //序列化方式
			socketOut.write(headerBytes); // header
			socketOut.write(paramsBytes); // params
			socketOut.write(fileBytes); // fileBytes
			socketOut.flush();

			//响应
			is = socket.getInputStream();
			byte[] sizeBuf = new byte[4];
			int sizeLen = is.read(sizeBuf);
			if (sizeLen != 4) {
				throw new IOException("is.read() size error, sizeLen!=4");
			}
			//报文头长度
			headerLen = StringUtil.ByteArray2Int(sizeBuf);

			//报文体长度
			sizeBuf = new byte[4];
			is.read(sizeBuf);
			bodyLen = StringUtil.ByteArray2Int(sizeBuf);

			//序列化类型
			sizeBuf = new byte[4];
			is.read(sizeBuf);
			int respSerializeType = StringUtil.ByteArray2Int(sizeBuf);

			//读取头部
			headerBuf = new byte[headerLen];
			int byteRead = 0;
			while (byteRead < headerLen) {
				byteRead += is.read(headerBuf, byteRead, headerLen - byteRead);
			}
			if (byteRead != headerLen) {
				throw new IOException("is.read() content error,contentLen=" + byteRead + " not-equal size=" + headerLen);
			}
			//读取头部
			bodyBuf = new byte[bodyLen];
			byteRead = 0;
			while (byteRead < bodyLen) {
				byteRead += is.read(bodyBuf, byteRead, bodyLen - byteRead);
			}
			if (byteRead != bodyLen) {
				throw new IOException("is.read() content error,contentLen=" + byteRead + " not-equal size=" + headerLen);
			}
			if (4 != respSerializeType) {
				log.error("response msg:{}", new String(headerBuf));
				throw new IOException("resp serializeType error, serializeType:" + respSerializeType + "!=4");
			}
			response = new Pair<>(headerBuf, bodyBuf);
		} catch (Exception ex) {
			throw new UncheckedException(ex);
		} finally {
			if (socket != null) {
				pool.returnObject(key, socket);
			}
			log.debug("send tcp cost:{}", System.currentTimeMillis() - start);
		}
		return response;
	}


	public static byte[] send(String host, int port, int timeout, String msg, int serializeType) {

		return send(host, port, timeout, msg.getBytes(), serializeType);
	}

	/**
	 * 发送 tcp 请求
	 * @param host 服务ip
	 * @param port 服务端口
	 * @param timeout 读超时时间
	 * @param bytes 消息
	 * @param serializeType  报文序列化类型，1-probstuff 2-json，3-relay,4-不进行编解码，
	 * @throws Exception
	 */
	public static byte[] send(String host, int port, int timeout, byte[] bytes, int serializeType) {
		//从对象池中获取对象
		String key = host + ":" + port;
		Socket socket = null;
		byte[] contentBuf;
		long start = System.currentTimeMillis();
		try {
			socket = pool.borrowObject(key);
			socket.setSoTimeout(timeout);
			OutputStream socketOut;
			InputStream is;
			socketOut = socket.getOutputStream();
			int len = bytes.length;
			log.debug("send msg len:" + len);

			socketOut.write(Byte.valueOf("1")); // size
			socketOut.write(BytesUtil.int2Bytes(len)); // size
			socketOut.write(BytesUtil.int2Bytes(serializeType)); //序列化方式
			socketOut.write(bytes); // content
			socketOut.flush();

			is = socket.getInputStream();
			byte[] msgFlag = new byte[1];
			is.read(msgFlag);

			byte[] sizeBuf = new byte[4];
			is.read(sizeBuf);
			int size = BytesUtil.bytes2Int(sizeBuf);

			byte[] serialBuf = new byte[4];
			is.read(serialBuf);
			int respSerializeType = BytesUtil.bytes2Int(serialBuf);

			contentBuf = new byte[size];
			int byteRead = 0;
			while (byteRead < size) {
				byteRead += is.read(contentBuf, byteRead, size - byteRead);
			}
			if (byteRead != size) {
				throw new IOException("is.read() content error,contentLen=" + byteRead + " not-equal size=" + size);
			}
			if (serializeType != respSerializeType) {
				log.error("response msg:{}", new String(contentBuf));
				throw new IOException("resp serializeType error, serializeType:" + respSerializeType + "!=" + serializeType);
			}
		} catch (Exception ex) {
			throw new UncheckedException(ex);
		} finally {
			if (socket != null) {
				pool.returnObject(key, socket);
			}
			log.debug("send tcp cost:{}", System.currentTimeMillis() - start);
		}
		return contentBuf;
	}
}
