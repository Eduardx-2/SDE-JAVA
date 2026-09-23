/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.systempaq;

import static com.systempaq.SistemaPaquetes.userCreateSession;
import com.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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
import user.Usuarios;
import user.Roles;
import paquetes.Paquetes;
/**
 *
 * @author eduardx_2
 */
public class Login {
    
    private static final Scanner using_input = new Scanner(System.in);
    
    
    private static void user_data() throws NoSuchAlgorithmException, InvalidKeySpecException{
        String nombre;
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
        nombre = using_input.nextLine();
        System.out.println("Apellido: ");
        apellido = using_input.nextLine();
        System.out.println("Correo: ");
        correo = using_input.next();
        if (!CheckOn.emailCheck(correo)){
            System.out.println("Correo Invalido.");
            return;
        }
        System.out.println("Telefono: ");
        telefono = using_input.next();
        char[] passwordUser = consol.readPassword("Contraseña: ");
        if (passwordUser.length < 8){
            System.out.println("Su contraseña es muy corta.");
            return;
        }
        System.out.println("Rol: ");
        rol = using_input.nextInt();
        String data_password = Login.cifradate_pkbd(passwordUser);
        Roles rolDb = Roles.verification_asset(rol); //toma un rol apartir de un numero, y asigna ese rol
        Usuarios userCreate = new Usuarios(nombre.toLowerCase(),apellido.toLowerCase(),correo,telefono,data_password,rolDb); //crea el objeto usuario
        userCreateSession(userCreate); //insertar los datos del usuario  
      
    }
    private static void menu_user_(int userId){
        int opciones;
        System.out.println("----------- Usuarios Menu-------------");
        System.out.print("1- Registrar Paquetes\n2 - Ver mis Paquetes\n3 - Buscar mi paquete\n\nSeleccióne una opción: ");
        opciones = using_input.nextInt();
        switch(opciones){
            case 1 -> Paquetes.registered_data_package(userId);
            case 2 -> Paquetes.consultarMispaquetes(userId);
            case 3 -> Paquetes.buscarMispaquetes();
            default -> System.out.println("Ingreso una opción no valida");
        }
    }
    
    private static void menu_admin(int userId){
        int opciones;
        System.out.println("----------- Administrador Menu-------------");
        System.out.print("1- Registrar Paquetes\n2 - Eliminar Paquetes\n3 - Buscar paquetes\n4 - Crear Usuarios\nSeleccióne una opción: ");
        opciones = using_input.nextInt();
        switch(opciones){
            case 1 -> Paquetes.registered_data_package(userId);
            case 2 -> Paquetes.eliminated_paquetes();
            case 3 -> Paquetes.buscarMispaquetes();
                
            case 4 -> {
                try {
                    user_data();
                }catch(NoSuchAlgorithmException | InvalidKeySpecException e){
                    e.printStackTrace();
                }
            }
            default -> System.out.println("Opción no válida.");
        }
    }
    
    public static void login_session_data() throws NoSuchAlgorithmException, InvalidKeySpecException{
        String user;
        Console consol = System.console();
        System.out.println("--------------- Login ------------------");
        System.out.print("Email: ");
        user = using_input.next();
        char[] passwordUser = consol.readPassword("Contraseña: ");
        Usuarios dat = bs_tablas_data_user(user);
        if (dat == null){
            System.out.println("No existe su correo.");
            return;
        }
        boolean checkpass = checkOnpass(passwordUser,dat.getPass());
        if (checkpass == true){
            if (Roles.verificationsUser(String.valueOf(dat.getRolUser())).equals("USER")){ //si es rol usuario despligue el menu usuario
                menu_user_(dat.getId_usuario());//ENVIA EL ID DEL USUARIO
            }else if(Roles.verificationsUser(String.valueOf(dat.getRolUser())).equals("ADMIN")){
                menu_admin(dat.getId_usuario());
            }
        }else{
            System.out.println("[-] Contraseña Invalida");
            return;
        }
        
        
    }
    
    private static Usuarios bs_tablas_data_user(String correo){
        EntityManager manager = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Usuarios> userQuery = manager.createQuery(
                    "SELECT u FROM Usuarios u WHERE u.correo = :correo",
                    Usuarios.class
            );
            userQuery.setParameter("correo", correo);
            return userQuery.getResultStream().findFirst().orElse(null);
        }finally{
            manager.close();
        }
    }
    
    //cifrar password
    public static String cifradate_pkbd(char[] pass) throws NoSuchAlgorithmException, InvalidKeySpecException{
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
    //validate pass
    private static boolean checkOnpass(char[] password, String hash) throws NoSuchAlgorithmException, InvalidKeySpecException{
        String[] datahash = hash.split(":"); //datahash divide en dos partes el string, obtiene salt y hash
        KeySpec spec = new PBEKeySpec(password,Base64.getDecoder().decode(datahash[0]),65536,128);
        SecretKeyFactory keyFactAlght = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hashV = keyFactAlght.generateSecret(spec).getEncoded();
        Arrays.fill(password, '\0');
        return java.security.MessageDigest.isEqual(Base64.getDecoder().decode(datahash[1]), hashV);
    }
    
    
}
