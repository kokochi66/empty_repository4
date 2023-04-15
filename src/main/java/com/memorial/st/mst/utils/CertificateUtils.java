package com.memorial.st.mst.utils;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class CertificateUtils {

    public static X509Certificate generateSelfSignedCertificate(KeyPair keyPair) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        X500Name issuer = new X500Name("CN=localhost");
        BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());
        Date notBefore = Date.from(Instant.now());
        Date notAfter = Date.from(Instant.now().plus(Duration.ofDays(365)));
        X500Name subject = new X500Name("CN=localhost");

        X509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(
                issuer, serialNumber, notBefore, notAfter, subject, keyPair.getPublic());

        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withRSA")
                .build(keyPair.getPrivate());

        return new JcaX509CertificateConverter()
                .getCertificate(certificateBuilder.build(contentSigner));
    }
}
