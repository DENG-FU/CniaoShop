package cniao5shop.com.cniao5.cniaoshop.utils;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by DENGFU on 2016/7/22.
 */
public class DESUtil {

    public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";

    /**
     * DES algorithm encryption
     * @param key   private key ,the length should not be less than 8
     * @param data  need to be encryption string
     * @return
     */
    public static String encode(String key,String data){
        if (data == null)
            return null;
        try{
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //the key length need to longer than 8 byte
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
            AlgorithmParameterSpec parameterSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE,secretKey,parameterSpec);
            byte[] bytes = cipher.doFinal(data.getBytes());
            return byte2String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     *
     * @param bytes need to be converted byte sequence
     * @return
     */
    private static String byte2String(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String stemp;
        for (int n = 0; bytes != null && n<bytes.length;n++){
            stemp = Integer.toHexString(bytes[n] & 0XFF);
            if (stemp.length() == 1)
                sb.append('0');
            sb.append(stemp);
        }
        return sb.toString().toUpperCase(Locale.CHINA);
    }
    /**
     *
     * @param b need to be convert byte sequence
     * @return
     */
    private static byte[] byte2hex(byte[] b){
        if ((b.length%2) != 0)
            throw new IllegalArgumentException();
        byte[] b2 = new byte[b.length/2];
        for (int n = 0;n<b.length;n+=2){
            String item = new String(b,n,2);
            b2[n/2] = (byte)Integer.parseInt(item,16);
        }
        return b2;
    }
}
