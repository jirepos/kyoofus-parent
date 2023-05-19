package com.jirepo.core.codec;


/**
 * 16진수 변환을 제공한다. 
 */
public class HexConverter {
    /**
     * 바이트 배열을 16진수값으로 변환한다.
     *
     * @param hash hex값으로 변환할 byte 배열
     * @return hex로 변환된 문자열
     */
    public static String bytesToHex(byte[] hash) {

        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02X", b & 0xff));
        }
        return sb.toString();
    }// :

    /**
     * Hex 값으로 이루어진 문자열을 바이트 배열로 변환한다.
     *
     * @param s 바이트로 변환할 문자열
     * @return
     */
    public static byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }// :

    /**
     * 문자열을 Hex 문자열로 변환한다.
     *
     * @param s 변환할 문자열
     * @return Hex 값으로 변환된 문자열
     */
    public static String strToHex(String s) {
        return bytesToHex(s.getBytes());
    }

    /**
     * Hex 문자열을 원래의 문자열로 변환한다.
     *
     * @param s Hex 문자열
     * @return 변환된 문자열
     */
    public static String hexToString(String s) {
        return new String(hexToBytes(s));
    }
}
