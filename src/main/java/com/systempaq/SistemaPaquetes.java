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
import com.systempaq.CheckOn;
import com.systempaq.Login;
/**
 *
 * @author eduardx_2
 */
public class SistemaPaquetes {

    private static Scanner input = new Scanner(System.in);
    
    public static void main(String[] args) {
        try {
            smokeEntityManager();
            Login.login_session_data();
            //user_data();
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
    
    // registrar usuarios
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
        if (!CheckOn.emailCheck(correo)){
            System.out.println("Correo Invalido.");
            return;
        }
        System.out.println("Telefono: ");
        telefono = input.next();
        char[] passwordUser = consol.readPassword("Contraseña: ");
        if (passwordUser.length < 8){
            System.out.println("Su contraseña es muy corta.");
            return;
        }
        System.out.println("Rol: ");
        rol = input.nextInt();
        String data_password = Login.cifradate_pkbd(passwordUser);
        Roles rolDb = Roles.verification_asset(rol); //toma un rol apartir de un numero, y asigna ese rol
        Usuarios userCreate = new Usuarios(nombre.toLowerCase(),apellido.toLowerCase(),correo,telefono,data_password,rolDb); //crea el objeto usuario
        userCreateSession(userCreate); //insertar los datos del usuario


       
        
        
    }
    
    //inserta los usuarios
    public static void userCreateSession(Usuarios user) {
        EntityManager manager = JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            manager.getTransaction().begin();
            manager.persist(user);
            manager.getTransaction().commit();

            System.out.println("Usuario Registrado");

        } catch (Exception e) {
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }
            e.printStackTrace();

        } finally {
            manager.close();
        }
    }
    
    
    
}
