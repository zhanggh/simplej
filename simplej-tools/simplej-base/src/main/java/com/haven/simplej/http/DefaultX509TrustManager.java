package com.haven.simplej.http;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author: havenzhang
 * @date: 2019/11/11 21:08
 * @version 1.0
 */
public class DefaultX509TrustManager  implements javax.net.ssl.TrustManager,
		javax.net.ssl.X509TrustManager{
	@Override
	public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

	}

	@Override
	public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}
}
