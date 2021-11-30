package Encryption;


import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.HKDFBytesGenerator;
import org.bouncycastle.crypto.params.HKDFParameters;

public class KDF {

    public KDF(){

    }
    public  byte[] hkdfExpand(byte[] ikm, byte[] salt, int length) {

        HKDFBytesGenerator hkdf = new HKDFBytesGenerator(new SHA256Digest());
        hkdf.init(new HKDFParameters(ikm, salt,null));

        byte[] okm = new byte[length];
        hkdf.generateBytes(okm, 0, length);

        return okm;
    }


}
