package com.haven.simplej.http;

import com.google.common.collect.Lists;
import com.haven.simplej.exception.UncheckedException;
import com.vip.vjtools.vjkit.collection.MapUtil;
import lombok.Data;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author haven.zhang http 请求执行器
 */
@Data
public class HttpExecuter {

	private static final Logger log = LoggerFactory.getLogger(HttpExecuter.class);
	/**
	 *
	 */
	private static final String HTTPS = "https";
	/**
	 * 最大连接数
	 */
	private static final int DEFAULT_MAX_CONN = 3000;
	/**
	 * 最大每个路由的连接数
	 */
	private static final int DEFAULT_MAX_CONN_PER_ROUTE = 200;
	/**
	 * httpclient对象，长链接的时候使用
	 */
	private volatile CloseableHttpClient keepAliveHttpclient;


	/**
	 * httpclient对象 短连接使用
	 */
	private volatile CloseableHttpClient shortHttpclient;
	/**
	 * httpclient对象 短连接使用 https使用
	 */
	private volatile CloseableHttpClient shortHttpsclient;

	/**
	 * 外部传入的连接池
	 */
	private PoolingHttpClientConnectionManager poolManager;

	/**
	 * 默认的contentType
	 */
	private static final String DEFAULT_CONTENT_TYPE = "text/html";

	/**
	 * 是否重定向
	 */
	private boolean redirectsEnabled = false;

	private static SSLHandler sslContxt = new SSLHandler();
	private static SSLConnectionSocketFactory sslFactory;
	/**
	 * 从连接池获取连接的时候等待获取的时间 单位毫秒
	 */
	private static final int waitePoolTimeout = 5000;


	// 创建CookieStore实例
	private CookieStore cookieStore = new BasicCookieStore();
	/**
	 * http 全局代理
	 */
	private HttpHost proxy;
	/**
	 * 连接等待时间
	 */
	private int connectTimeout = 300;
	/**
	 * socket 读写时间
	 */
	private int socketTimeout = 850;
	/**
	 * 默认content type
	 */
	private String contentType = DEFAULT_CONTENT_TYPE;
	/**
	 * 默认编码
	 */
	private String encoding = "utf-8";
	/**
	 * 默认长链接
	 */
	private boolean keepAlive = true;

	/**
	 * 默认的连接管理器
	 */
	private static PoolingHttpClientConnectionManager defaultPoolManager = null;

	public HttpExecuter() {
		super();

	}

	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
		if (poolManager != null) {
			SocketConfig socketConfig = SocketConfig.custom().setSoKeepAlive(keepAlive).build();
			poolManager.setDefaultSocketConfig(socketConfig);
		}
	}

	public void setPoolManager(PoolingHttpClientConnectionManager poolManager) {
		if (poolManager == null) {
			this.poolManager = initDefaulPoolManager();
		} else {
			this.poolManager = poolManager;
		}
	}

	/**
	 * 初始化连接池
	 */
	public PoolingHttpClientConnectionManager initDefaulPoolManager() {
		if (defaultPoolManager == null) {
			synchronized (HttpExecuter.class) {
				if (defaultPoolManager == null) {
					try {
						Registry<ConnectionSocketFactory> socketFactoryRegistry =
								RegistryBuilder.<ConnectionSocketFactory>create().register("http",
										PlainConnectionSocketFactory.getSocketFactory()).register("https", getSSLSF()).build();
						defaultPoolManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
						defaultPoolManager.setMaxTotal(DEFAULT_MAX_CONN);
						defaultPoolManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONN_PER_ROUTE);
						SocketConfig socketConfig = SocketConfig.custom().setSoKeepAlive(true).build();
						defaultPoolManager.setDefaultSocketConfig(socketConfig);
						// Validate connections after 20 ms of inactivity
						defaultPoolManager.setValidateAfterInactivity(20000);
					} catch (Exception e) {
						log.error("initConnectionManager error", e);
					}
				}
			}
		}
		return defaultPoolManager;
	}

	private static class SSLHandler implements X509TrustManager {
		private SSLHandler() {
		}

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
			System.out.println("checkClientTrusted");
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
			System.out.println("checkServerTrusted");
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}
	}

	private static HostnameVerifier getVerifier() {
		return new NoopHostnameVerifier();
	}

	private static synchronized SSLConnectionSocketFactory getSSLSF() {
		if (sslFactory != null) {
			return sslFactory;
		}
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, new TrustManager[]{sslContxt}, new SecureRandom());
			sslFactory = new SSLConnectionSocketFactory(sc, getVerifier());
		} catch (Exception e) {
			throw new UncheckedException(e);
		}
		return sslFactory;
	}


	/**
	 * 增加请求cookie
	 * @param cookie
	 */
	public void addCookie(BasicClientCookie cookie) {
		if (cookie != null) {
			cookieStore.addCookie(cookie);
		}
	}

	/**
	 * 设置proxy配置
	 * @param proxy proxy
	 */
	public void setHttpProxy(HttpHost proxy) {
		this.proxy = proxy;
	}


	/**
	 * 发送http get请求
	 * @author haven.zhang
	 * @param url url
	 * @param queryString 查询参数，如：name=234234&age=12
	 * @return byte[]
	 */
	public byte[] get(String url, String queryString) {

		return get(url, queryString, null);
	}

	/**
	 * 发送http get请求
	 * @author haven.zhang
	 * @param url url
	 * @param queryString 查询参数，如：name=234234&age=12
	 * @return byte[]
	 */
	public byte[] get(String url, String queryString, Map<String, String> headers) {

		byte[] content = new byte[0];
		CloseableHttpResponse response = null;
		try {
			response = get2(url, queryString, headers);
			content = EntityUtils.toByteArray(response.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != response) {
					EntityUtils.consume(response.getEntity());
					response.close();
				}
			} catch (Exception e1) {
				log.error("http request error:", e1);
			}
		}
		return content;
	}

	/**
	 * 执行http请求
	 */
	private CloseableHttpResponse execute(String url, HttpRequestBase httpRequestBase) throws Exception {
		CloseableHttpResponse response;
		CloseableHttpClient httpclient = getHttpClient(url);
		response = httpclient.execute(httpRequestBase);
		int statusCode = response.getStatusLine().getStatusCode();
		if (HttpStatus.SC_MOVED_TEMPORARILY == statusCode) {
			log.info("请求服务处理完成，响应码：302 忽略");
		} else {
			log.info("请求服务处理完成，响应码：{}", statusCode);
		}
		return response;
	}

	/**
	 * 发送http get请求
	 * @author haven.zhang
	 * @param url url
	 * @param queryString 查询参数，如：name=234234&age=12
	 * @return CloseableHttpResponse
	 */
	public CloseableHttpResponse get2(String url, String queryString, Map<String, String> headers) {
		ContentType contentType = ContentType.create(this.contentType, encoding);
		HttpEntity httpEntity = new StringEntity(queryString, contentType);
		HttpRequestBase httpGet = null;

		log.info("getTotalStats|getMax:{},getAvailable:{}", poolManager.getTotalStats().getMax(),
				poolManager.getTotalStats().getAvailable());
		try {
			url += "?" + EntityUtils.toString(httpEntity);
			httpGet = new HttpGet(url);
			httpGet.setConfig(getReqConf(connectTimeout, socketTimeout));
			if (MapUtil.isNotEmpty(headers)) {
				headers.forEach(httpGet::addHeader);
			}
			return execute(url, httpGet);
		} catch (Exception e) {
			log.error("http request error:", e);
			throw new RuntimeException("http request failed", e);
		} finally {
			try {
				closeHttpConnection(httpEntity, httpGet, null);
			} catch (Exception e1) {
				log.error("http request error:", e1);
			}
		}

	}

	/**
	 * @param url 目标url
	 * @param params 请求参数
	 * @return byte[]
	 */
	public byte[] postForm(String url, Map<String, String> params) {

		return postForm(url, params, null);
	}

	/**
	 * @param url 目标url
	 * @param params 请求参数
	 * @return byte[]
	 */
	public byte[] postForm(String url, Map<String, String> params, Map<String, String> header) {

		CloseableHttpResponse response = null;
		try {
			byte[] content;
			response = postForm2(url, params, header);
			content = EntityUtils.toByteArray(response.getEntity());
			return content;
		} catch (Exception e) {
			log.error("http request error:", e);
			throw new RuntimeException("http request failed", e);
		} finally {
			try {
				if (null != response) {
					EntityUtils.consume(response.getEntity());
					response.close();
				}
			} catch (IOException e) {
				log.error("close response error", e);
			}
		}

	}

	/**
	 * @param url 目标url
	 * @param params 请求参数
	 * @return byte[]
	 */
	public CloseableHttpResponse postForm2(String url, Map<String, String> params, Map<String, String> header) {

		List<NameValuePair> parameters = Lists.newArrayList();
		Iterator<Entry<String, String>> iter = params.entrySet().iterator();
		BasicNameValuePair pair;
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
			parameters.add(pair);

		}
		HttpEntity httpEntity = null;
		HttpPost httpPost = null;
		try {
			httpPost = new HttpPost(url);
			httpEntity = new UrlEncodedFormEntity(parameters, encoding);
			httpPost.setConfig(getReqConf(connectTimeout, socketTimeout));
			httpPost.setEntity(httpEntity);
			if (MapUtil.isNotEmpty(header)) {
				header.forEach(httpPost::addHeader);
			}
			return execute(url, httpPost);
		} catch (Exception e) {
			log.error("http request error:", e);
			throw new RuntimeException("http request failed", e);
		} finally {
			try {
				closeHttpConnection(httpEntity, httpPost, null);
			} catch (Exception e1) {
				log.error("http request error:", e1);
			}
		}

	}


	/**
	 * @param url 目标url
	 * @param parameters 请求参数
	 * @return 字节数字
	 */
	public byte[] postForm(String url, List<NameValuePair> parameters, Map<String, String> header) {

		CloseableHttpResponse response = null;
		try {
			response = postForm2(url, parameters, header);
			byte[] content;
			content = EntityUtils.toByteArray(response.getEntity());
			return content;
		} catch (Exception e) {
			log.error("http request error:", e);
			throw new RuntimeException("http request failed", e);
		} finally {
			try {
				if (null != response) {
					EntityUtils.consume(response.getEntity());
					response.close();
				}
			} catch (IOException e) {
				log.error("close response error", e);
			}
		}

	}


	/**
	 * @param url 目标url
	 * @param parameters 请求参数
	 * @return 字节数字
	 */
	public CloseableHttpResponse postForm2(String url, List<NameValuePair> parameters, Map<String, String> header) {

		HttpEntity httpEntity = null;
		HttpPost httpPost = null;
		try {
			httpPost = new HttpPost(url);
			httpEntity = new UrlEncodedFormEntity(parameters, encoding);
			httpPost.setConfig(getReqConf(connectTimeout, socketTimeout));
			httpPost.setEntity(httpEntity);
			if (MapUtil.isNotEmpty(header)) {
				header.forEach(httpPost::addHeader);
			}
			return execute(url, httpPost);
		} catch (Exception e) {
			log.error("http request error:", e);
			throw new RuntimeException("http request failed", e);
		} finally {
			try {
				closeHttpConnection(httpEntity, httpPost, null);
			} catch (Exception e1) {
				log.error("http request error:", e1);
			}
		}

	}
	/**
	 * @param url 目标url
	 * @param message 请求参数
	 * @return
	 */
	public byte[] post(String url, String message){

		return post(url,message,null);
	}



	/**
	 * @param url 目标url
	 * @param message 请求参数
	 * @return
	 */
	public byte[] post(String url, String message, Map<String, String> headers) {
		try {
			return post(url, message.getBytes("UTF-8"), headers);
		} catch (UnsupportedEncodingException e) {
			throw new UncheckedException(e);
		}

	}

	/**
	 * @param url 目标url
	 * @param bytes 请求报文
	 * @return
	 */
	public byte[] post(String url, byte[] bytes, Map<String, String> headers) {

		CloseableHttpResponse response = null;
		try {
			response = post2(url, bytes, headers);
			byte[] content;
			content = EntityUtils.toByteArray(response.getEntity());
			return content;
		} catch (Exception e) {
			log.error("http request error:", e);
			throw new RuntimeException("http request failed", e);
		} finally {
			try {
				if (null != response) {
					EntityUtils.consume(response.getEntity());
					response.close();

				}
			} catch (IOException e) {
				log.error("close response error", e);
			}

		}

	}

	/**
	 * @param url 目标url
	 * @param bytes 请求报文
	 * @param headers
	 * @return
	 */
	public CloseableHttpResponse post2(String url, byte[] bytes, Map<String, String> headers) {
		HttpEntity httpEntity = null;
		HttpPost httpPost = null;
		try {
			ContentType contentType = ContentType.create(this.contentType, encoding);
			httpEntity = new ByteArrayEntity(bytes, contentType);
			httpPost = new HttpPost(url);
			if (MapUtil.isNotEmpty(headers)) {
				headers.forEach(httpPost::addHeader);
			}
			httpPost.setConfig(getReqConf(connectTimeout, socketTimeout));
			httpPost.setEntity(httpEntity);
			return execute(url, httpPost);
		} catch (Exception e) {
			log.error("http request error:", e);
			throw new RuntimeException("http request failed", e);
		} finally {
			try {
				closeHttpConnection(httpEntity, httpPost, null);
			} catch (Exception e1) {
				log.error("http request error:", e1);
			}
		}

	}

	protected void closeHttpConnection(HttpEntity httpEntity, HttpRequestBase httpRequestBase,
			CloseableHttpResponse response) {
		// 关闭连接,释放资源
		try {
			if (null != httpEntity) {
				httpEntity.getContent().close();
			}
			if (null != response) {
				EntityUtils.consume(response.getEntity());
				response.close();

			}
		} catch (IOException e) {
			log.error("close connection error", e);
		}
	}

	private RequestConfig getReqConf() {
		// http 请求配置
		RequestConfig requestConfig;
		if (this.proxy == null) {
			requestConfig =
					RequestConfig.custom().setConnectionRequestTimeout(waitePoolTimeout).setExpectContinueEnabled(false).setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).setRedirectsEnabled(redirectsEnabled).build();
		} else {
			requestConfig =
					RequestConfig.custom().setProxy(this.proxy).setConnectionRequestTimeout(waitePoolTimeout).setExpectContinueEnabled(false).setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).setRedirectsEnabled(redirectsEnabled).build();
		}
		return requestConfig;
	}

	private RequestConfig getReqConf(int connectTimeout, int socketTimeout) {
		// http 请求配置
		RequestConfig requestConfig;
		if (this.proxy == null) {
			requestConfig =
					RequestConfig.custom().setConnectionRequestTimeout(waitePoolTimeout).setExpectContinueEnabled(false).setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).setRedirectsEnabled(redirectsEnabled).build();
		} else {
			requestConfig =
					RequestConfig.custom().setProxy(this.proxy).setConnectionRequestTimeout(waitePoolTimeout).setExpectContinueEnabled(false).setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).setRedirectsEnabled(redirectsEnabled).build();
		}
		return requestConfig;

	}

	/**
	 * 支持http/https、长链接/短连接
	 * @param url url
	 * @return CloseableHttpClient
	 * @throws Exception Exception
	 */
	private CloseableHttpClient getHttpClient(String url) throws Exception {
		boolean https = url.startsWith(HTTPS);
		if (keepAlive) {// 如果是长链接，则取长连接的client
			if (keepAliveHttpclient == null) {
				synchronized (HttpExecuter.class) {
					if (keepAliveHttpclient == null) {
						keepAliveHttpclient = createHttpClient(url);
					}
				}
			}
			return keepAliveHttpclient;

		} else {// 如果是短链接，则取短连接的client
			if (https) {// 如果使用https
				if (shortHttpsclient == null) {
					synchronized (HttpExecuter.class) {
						if (shortHttpsclient == null) {
							shortHttpsclient = createHttpsClient(url);
						}
					}
				}
				return shortHttpsclient;
			} else {
				if (shortHttpclient == null) {
					synchronized (HttpExecuter.class) {
						if (shortHttpclient == null) {
							shortHttpclient = createHttpClient(url);
						}
					}
				}
				return shortHttpclient;
			}
		}
	}

	/**
	 * 创建httpclient对象 用于http
	 * @param url url
	 * @return CloseableHttpClient
	 * @throws Exception
	 */
	private CloseableHttpClient createHttpClient(String url) {
		log.info("create http client，url:{}", url);

		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setDefaultCookieStore(cookieStore).setRetryHandler(new DefaultHttpRequestRetryHandler(0, false)).setDefaultRequestConfig(getReqConf());
		if (keepAlive && poolManager != null) {
			log.debug("create keep alive connection");
			builder.setConnectionManager(poolManager);
		}

		return builder.build();
	}

	/**
	 * 创建httpclient对象 用于https
	 * @param url url
	 * @return CloseableHttpClient
	 * @throws Exception Exception
	 */
	private CloseableHttpClient createHttpsClient(String url) throws Exception {
		log.info("create http client，url:{}", url);

		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setSSLHostnameVerifier(getVerifier()).setSSLSocketFactory(sslFactory).setDefaultCookieStore(cookieStore).setRetryHandler(new DefaultHttpRequestRetryHandler(0, false)).setDefaultRequestConfig(getReqConf());

		if (keepAlive && poolManager != null) {
			log.debug("create keep alive connection");
			builder.setConnectionManager(poolManager);
		}
		return builder.build();
	}

	public static HttpBuilder create() {
		return new HttpBuilder();
	}

}
