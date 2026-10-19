/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package destinatarios;

import com.util.JpaUtil;
import jakarta.persistence.EntityManager;
import java.util.Scanner;
import destinatarios.Destinatarios;
import java.time.LocalDateTime;

/**
 *
 * @author eduardx_2
 */
public class SysDestinatarios {
    
    private static Scanner scan = new Scanner(System.in); 
    
    public static void data_input_destinarios(int id){
        String nombre;
        int telefono;
        String correo;
        String direccion;
        String referencias;
        System.out.println("--------- REGISTRO DESTINATARIO----------");
        System.out.print("Nombre: ");
        nombre = scan.nextLine();
        System.out.print("Telefono: ");
        telefono = scan.nextInt();
        System.out.print("Correo: ");
        correo =  scan.nextLine();
        System.out.print("dirección: ");
        direccion = scan.nextLine();
        referencias = scan.nextLine();
        Destinatarios destin = new Destinatarios(nombre,telefono,correo,direccion,referencias,LocalDateTime.MIN,id);
       
    }
    
    private static void registered_destinario(Destinatarios destinatarios){
        EntityManager manager = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            manager.getTransaction().begin();
            manager.persist(destinatarios);
            manager.getTransaction().commit();

            System.out.println("Paquete Registrado");

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
