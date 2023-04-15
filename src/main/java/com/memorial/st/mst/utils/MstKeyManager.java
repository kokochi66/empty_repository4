package com.memorial.st.mst.utils;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

import javax.net.ssl.X509ExtendedKeyManager;
import java.net.Socket;
import java.security.KeyPair;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

public class MstKeyManager extends X509ExtendedKeyManager {
    private final KeyPair keyPair;

    public MstKeyManager(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    @Override
    public String[] getClientAliases(String keyType, Principal[] issuers) {
        return new String[0];
    }

    @Override
    public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
        return null;
    }

    @Override
    public String[] getServerAliases(String keyType, Principal[] issuers) {
        return new String[0];
    }

    @Override
    public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
        return null;
    }

    @Override
    public X509Certificate[] getCertificateChain(String alias) {
        return new X509Certificate[0];
    }

    @Override
    public PrivateKey getPrivateKey(String alias) {
        return keyPair.getPrivate();
    }

    public RSAPublicKey getPublicKey() {
        PublicKey publicKey = keyPair.getPublic();
        if (publicKey instanceof RSAPublicKey) {
            return (RSAPublicKey) publicKey;
        }
        return null;
    }

    public JWKSet getJWKSet() {
        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
        return new JWKSet(rsaKey);
    }

}
