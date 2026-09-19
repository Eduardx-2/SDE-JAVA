/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.systempaq;

import java.io.Console;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import com.util.JpaUtil;
import jakarta.persistence.EntityManager;
import user.Roles;
import user.Usuarios;

/**
 *
 * @author eduardx_2
 */
public class SistemaPaquetes {

    private static Scanner input = new Scanner(System.in);
    
    public static void main(String[] args) {
        try {
            smokeEntityManager();
            user_data();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException d) {
            System.out.println(d.getMessage());
        } finally {
            JpaUtil.close();
        }
    }

    private static void smokeEntityManager() {
        EntityManager entityManager = JpaUtil.createEntityManager();
        try {
            if (!entityManager.isOpen()) {
                throw new IllegalStateException("EntityManager no está abierto");
            }
            System.out.println("EntityManager conectado (unidad " + JpaUtil.PERSISTENCE_UNIT + ")");
        } finally {
            entityManager.close();
        }
    }
    
    private static void user_data() throws NoSuchAlgorithmException, InvalidKeySpecException{
        String nombre = "";
        String apellido;
        String correo;
        String telefono;
        int rol;
        Console consol = System.console();
        if (consol == null){
            System.out.println("Fallo: Sin consola disponible");
            return;
        }
        System.out.println("---------------------- Registro de usuarios ----------------------\n");
        System.out.println("Nombre: ");
        nombre = input.next();
        System.out.println("Apellido: ");
        apellido = input.next();
        System.out.println("Correo: ");
        correo = input.next();
        System.out.println("Telefono: ");
        telefono = input.next();
        char[] passwordUser = consol.readPassword("Contraseña: ");
        System.out.println("Rol: ");
        rol = input.nextInt();
        String data_password = cifradate_pkbd(passwordUser);
        Roles rolDb = Roles.verification_asset(rol); //toma un rol apartir de un numero, y asigna ese rol
        Usuarios userCreate = new Usuarios(nombre,apellido,correo,telefono,data_password,rolDb); //crea el objeto usuario
        userCreateSession(userCreate); //insertar los datos del usuario
// boolean bol = checkOnpass(passwordUser,data_password); //verifica la contraseña
       
        
        
    }
    
    
    public static void userCreateSession(Usuarios user){
        EntityManager manager = (EntityManager) JpaUtil.getEntityManagerFactory();
        try{
            manager.getTransaction().begin();
            manager.persist(user);
            manager.getTransaction().commit();
            System.out.println("Usuario Registrado");
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            manager.close();
        }
    }
    
    private static String cifradate_pkbd(char[] pass) throws NoSuchAlgorithmException, InvalidKeySpecException{
        //uso de PBKDF2, es ampliamente utilizado,Aplica una función pseudoaleatoria, como HMAC
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];//el valor de la salt se puede modificar,16,32,64, no cambia ni genera más seguridad
        random.nextBytes(salt);
        KeySpec dataKey = new PBEKeySpec(pass,salt,65536,128);//contraseña,salt,iteraciones,longitud
        SecretKeyFactory key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hashing = key.generateSecret(dataKey).getEncoded();
        Arrays.fill(pass, '\0');
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hashing);
    }
    
    private static boolean checkOnpass(char[] password, String hash) throws NoSuchAlgorithmException, InvalidKeySpecException{
        String[] datahash = hash.split(":"); //datahash divide en dos partes el string, obtiene salt y hash
        KeySpec spec = new PBEKeySpec(password,Base64.getDecoder().decode(datahash[0]),65536,128);
        SecretKeyFactory keyFactAlght = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hashV = keyFactAlght.generateSecret(spec).getEncoded();
        Arrays.fill(password, '\0');
        return java.security.MessageDigest.isEqual(Base64.getDecoder().decode(datahash[1]), hashV);
    }
}
