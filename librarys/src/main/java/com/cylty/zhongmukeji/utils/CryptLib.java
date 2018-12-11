package com.cylty.zhongmukeji.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class CryptLib {

    /**
     * Encryption mode enumeration
     */
    private enum EncryptMode {
        ENCRYPT, DECRYPT;
    }

    // cipher to be used for encryption and decryption
    Cipher cipher;

    // encryption key and initialization vector
    byte[] key, iv;

    public CryptLib() throws NoSuchAlgorithmException, NoSuchPaddingException {
        // initialize the cipher with transformation AES/CBC/PKCS5Padding
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        key = new byte[32]; // 256 bit key space
        iv = new byte[16]; // 128 bit IV
    }

    /**
     * @param inputText     Text to be encrypted or decrypted
     * @param encryptionKey Encryption key to used for encryption / decryption
     * @param mode          specify the mode encryption / decryption
     * @param initVector    Initialization vector
     * @return encrypted or decrypted string based on the mode
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    private String encryptDecrypt(String inputText, String encryptionKey, EncryptMode mode, String initVector)
            throws UnsupportedEncodingException, InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        String _out = "";// output string
        int len = encryptionKey.getBytes("UTF-8").length; // length of the key
        if (encryptionKey.getBytes("UTF-8").length > key.length)
            len = key.length;

        int ivlen = initVector.getBytes("UTF-8").length;

        if (initVector.getBytes("UTF-8").length > iv.length)
            ivlen = iv.length;

        System.arraycopy(encryptionKey.getBytes("UTF-8"), 0, key, 0, len);
        System.arraycopy(initVector.getBytes("UTF-8"), 0, iv, 0, ivlen);

        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        if (mode.equals(EncryptMode.ENCRYPT)) {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] results = cipher.doFinal(inputText.getBytes("UTF-8"));
            _out = Base64.encodeToString(results, Base64.DEFAULT);
        }

        // decryption
        if (mode.equals(EncryptMode.DECRYPT)) {
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decodedValue = Base64.decode(inputText.getBytes(), Base64.DEFAULT);
            byte[] decryptedVal = cipher.doFinal(decodedValue);
            _out = new String(decryptedVal);
        }
        return _out;
    }

    /***
     * This function encrypts the plain text to cipher text using the key
     * provided. You'll have to use the same key for decryption
     *
     * @param _plainText Plain text to be encrypted
     * @param _key       Encryption Key. You'll have to use the same key for decryption
     * @param _iv        initialization Vector
     * @return returns encrypted (cipher) text
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */

    public String encrypt(String _plainText, String _key, String _iv)
            throws InvalidKeyException, UnsupportedEncodingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        return encryptDecrypt(_plainText, _key, EncryptMode.ENCRYPT, _iv);
    }

    /***
     * This funtion decrypts the encrypted text to plain text using the key
     * provided. You'll have to use the same key which you used during
     * encryprtion
     *
     * @param _encryptedText Encrypted/Cipher text to be decrypted
     * @param _key           Encryption key which you used during encryption
     * @param _iv            initialization Vector
     * @return encrypted value
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public String decrypt(String _encryptedText, String _key, String _iv)
            throws InvalidKeyException, UnsupportedEncodingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        return encryptDecrypt(_encryptedText, _key, EncryptMode.DECRYPT, _iv);
    }
}
