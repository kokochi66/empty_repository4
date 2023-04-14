package com.memorial.st.mst.utils;

import javax.net.ssl.X509ExtendedKeyManager;
import java.net.Socket;
import java.security.KeyPair;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyManager extends X509ExtendedKeyManager {
    private final KeyPair keyPair;

    public SimpleKeyManager(KeyPair keyPair) {
        this.keyPair = keyPair;
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
        return null;
    }

    @Override
    public PrivateKey getPrivateKey(String alias) {
        return keyPair.getPrivate();
    }

    @Override
    public PublicKey getPublicKey(String alias) {
        return keyPair.getPublic();
    }
}
