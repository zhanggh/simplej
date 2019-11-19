package com.haven.simplej.cert;

import com.haven.simplej.security.CertificateUtil;
import com.haven.simplej.security.KeyUtil;
import com.haven.simplej.security.SignUtil;
import com.haven.simplej.security.enums.KeyPairAlgorithms;
import com.haven.simplej.security.enums.SignAlgorithm;
import com.vip.vjtools.vjkit.time.DateUtil;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * @author: havenzhang
 * @date: 2019/5/15 16:39
 * @version 1.0
 */
public class CertificateTest {

	@Test
	public void createCert() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		KeyPair keyPair = KeyUtil.generateKeyPair(KeyPairAlgorithms.RSA, 2048);
		X500NameBuilder issuerBuilder = new X500NameBuilder();
		issuerBuilder.addRDN(BCStyle.C, "CN");
		issuerBuilder.addRDN(BCStyle.O, "tencent");
		issuerBuilder.addRDN(BCStyle.ST, "guangdong");
		issuerBuilder.addRDN(BCStyle.L, "shenzhen");

		X500NameBuilder subject = new X500NameBuilder();
		subject.addRDN(BCStyle.C, "CN");
		subject.addRDN(BCStyle.O, "tencent");
		subject.addRDN(BCStyle.OU, "tencent");
		subject.addRDN(BCStyle.ST, "guangdong");
		subject.addRDN(BCStyle.L, "shenzhen");
		//		issuerBuilder.
		Date notBefore = new Date();
		Date notAfter = DateUtil.setYears(new Date(), 2030);
		X509Certificate certificate = CertificateUtil.createV3Certificate(keyPair.getPublic(), keyPair, issuerBuilder, subject, SignAlgorithm.SHA1withRSA, notBefore, notAfter);
		FileUtils.writeByteArrayToFile(new File("tenpay.cer"), certificate.getEncoded());
		X509Certificate[] cers = new X509Certificate[]{certificate};
		CertificateUtil.generatePfx2("tencent", keyPair.getPrivate(), "1qa2ws@upi", cers, "tencent.pfx");


		String orgstr = "test";
		X509Certificate cert = CertificateUtil.loadCertificate("tenpay.cer");
		PrivateKey privateKey = CertificateUtil.getPrivateKeyFormPfx("tencent.pfx", "1qa2ws@upi");
		byte[] signMsg = SignUtil.sign(privateKey, orgstr.getBytes("utf-8"), SignAlgorithm.SHA1withRSA);

		boolean verify = SignUtil.verify(cert.getPublicKey(), signMsg, orgstr.getBytes("utf-8"), SignAlgorithm.SHA1withRSA);

		System.out.println("verify:" + verify);

		System.out.println("----------finish----------");
	}
}
