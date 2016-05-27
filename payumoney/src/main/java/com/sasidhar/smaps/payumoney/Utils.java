package com.sasidhar.smaps.payumoney;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SASi on 26-May-16.
 */

public class Utils {

    public static String generateHash(HashMap<String, String> params, String salt) {
        String hashSequence = generateHashSequence(params, salt);
        return hashCal(hashSequence);
    }

    private static String hashCal(String hashSequence) {
        byte[] hashseq = hashSequence.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }
        return hexString.toString();
    }

    private static synchronized String generateHashSequence(HashMap<String, String> params,
                                                            String salt) {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%s|", params.get("key")));
        sb.append(String.format("%s|", params.get("txnid")));
        sb.append(String.format("%s|", params.get("amount")));
        sb.append(String.format("%s|", params.get("productinfo")));
        sb.append(String.format("%s|", params.get("firstname")));
        sb.append(String.format("%s|", params.get("email")));
        sb.append(String.format("%s|", params.get("udf1") == null ? "" : params.get("udf1")));
        sb.append(String.format("%s|", params.get("udf2") == null ? "" : params.get("udf2")));
        sb.append(String.format("%s|", params.get("udf3") == null ? "" : params.get("udf3")));
        sb.append(String.format("%s|", params.get("udf4") == null ? "" : params.get("udf4")));
        sb.append(String.format("%s||||||", params.get("udf5") == null ? "" : params.get("udf5")));
        sb.append(String.format("%s", salt));

        return sb.toString();
    }

    public synchronized static String urlEncodeUTF8(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?, ?> entry : params.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    urlEncodeUTF8(entry.getKey().toString()),
                    urlEncodeUTF8(entry.getValue().toString())
            ));
        }
        return sb.toString();
    }

    private synchronized static String urlEncodeUTF8(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
