package com.memorial.st.mst.utils;

import com.nimbusds.jose.jwk.RSAKey;

import javax.net.ssl.X509ExtendedKeyManager;
import java.net.Socket;
import java.security.KeyPair;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

public class MstKeyManager extends X509ExtendedKeyManager {
    private final KeyPair keyPair;
    private final X509Certificate[] certificateChain;

    public MstKeyManager(KeyPair keyPair, X509Certificate[] certificateChain) {
        this.keyPair = keyPair;
        this.certificateChain = certificateChain;
    }

    @Override
    public String[] getClientAliases(String keyType, Principal[] issuers) {
        return new String[] {"rsa"};
    }

    @Override
    public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
        return "rsa";
    }

    @Override
    public String[] getServerAliases(String keyType, Principal[] issuers) {
        return new String[] {"rsa"};
    }

    @Override
    public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
        return "rsa";
    }

    @Override
    public X509Certificate[] getCertificateChain(String alias) {
        // 실제 환경에서는 X509 인증서를 반환해야 합니다.
        return certificateChain;
    }

    @Override
    public PrivateKey getPrivateKey(String alias) {
        return keyPair.getPrivate();
    }

    public RSAKey getRsaKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

}
