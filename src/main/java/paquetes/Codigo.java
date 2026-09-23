/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paquetes;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Random;

/**
 *
 * @author eduardx_2
 */
public class Codigo {
    private static final char[] BASE32_ALPHABET = "0123456789ABCDEFGHJKMNPQRSTVWXYZ".toCharArray();
    private static final int BASE32_RADIX = BASE32_ALPHABET.length;

    // Random criptográficamente seguro para evitar ataques de enumeración
    private static final SecureRandom RANDOM = new SecureRandom();
    
    public static String generateRandomCode(){
        String time = encodeBase32(Instant.now().toEpochMilli()); //Instant.now toma el tiempo actual
        return randomPrefix() + "-" + time + "-" + generateRandomString(4);
    }
    
    private static String randomPrefix(){
        String alphabet = "PKINBCVcvDxXfcV";
        StringBuilder sbi = new StringBuilder();
        Random rd = new Random();
        int ratio = 4;
        for (int i = 0; i < ratio; i++){
            int index = rd.nextInt(alphabet.length());
            char randStr = alphabet.charAt(index);
            sbi.append(randStr);
        }
        return sbi.toString();
    }
    
    private static String encodeBase32(long value){//value recibe el tiempo actual 
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.insert(0, BASE32_ALPHABET[(int) (value % BASE32_RADIX)]);
            value /= BASE32_RADIX;
        }
        return sb.toString();
    }
    
    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(BASE32_ALPHABET[RANDOM.nextInt(BASE32_RADIX)]);
        }
        return sb.toString();
    }
}
