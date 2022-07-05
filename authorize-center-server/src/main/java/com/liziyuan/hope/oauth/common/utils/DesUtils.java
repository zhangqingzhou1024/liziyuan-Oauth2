package com.liziyuan.hope.oauth.common.utils;


import javax.crypto.Cipher;
import java.security.Key;

/**
 * @author zqz
 * @date 2022-07-06 1:02
 */
public class DesUtils {
    // 字符串默认键值
    private static String strDefaultKey = "asdasdasdasdzcxczxczx";

    //加密工具
    private Cipher encryptCipher = null;

    // 解密工具
    private Cipher decryptCipher = null;

    /**
     * 默认构造方法，使用默认密钥
     */
    public DesUtils() throws Exception {
        this(strDefaultKey);
    }

    /**
     * 指定密钥构造方法
     * @param strKey 指定的密钥
     * @throws Exception
     */

    public DesUtils(String strKey) throws Exception {

        // Security.addProvider(new com.sun.crypto.provider.SunJCE());
        Key key = getKey(strKey.getBytes());
        encryptCipher = Cipher.getInstance("DES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        decryptCipher = Cipher.getInstance("DES");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }


    /**
     *
     * 加密字节数组
     * @param arrB 需加密的字节数组
     * @return 加密后的字节数组
     */
    public byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.doFinal(arrB);
    }

    /**
     * 加密字符串
     * @param strIn 需加密的字符串
     * @return 加密后的字符串
     */
    public String encrypt(String strIn) throws Exception {
        return EnCodeUtils.byteArr2HexStr(encrypt(strIn.getBytes()));
    }

    /**
     * 解密字节数组
     * @param arrB 需解密的字节数组
     * @return 解密后的字节数组
     */
    public byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }

    /**
     * 解密字符串
     * @param strIn 需解密的字符串
     * @return 解密后的字符串
     */
    public String decrypt(String strIn) throws Exception {
        return new String(decrypt(EnCodeUtils.hexStr2ByteArr(strIn)));
    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     * @param arrBTmp 构成该字符串的字节数组
     * @return 生成的密钥
     */
    private Key getKey(byte[] arrBTmp) throws Exception {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];
        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        // 生成密钥
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
        return key;
    }

    public static void main(String[] args) {
        try {
            String msg1 = "hello Cipher11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";

            DesUtils des1 = new DesUtils();// 使用默认密钥

            System.out.println("加密前的字符：" + msg1);

            System.out.println("加密后的字符：" + des1.encrypt(msg1));

            System.out.println("解密后的字符：" + des1.decrypt(des1.encrypt(msg1)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
