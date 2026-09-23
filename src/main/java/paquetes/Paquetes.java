/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paquetes;

import com.util.JpaUtil;
import jakarta.persistence.EntityManager;
import java.util.Scanner;
import user.Usuarios;
import paquetes.Codigo;

/**
 *
 * @author eduardx_2
 */
public class Paquetes { 
    
    private static Scanner scan_text = new Scanner(System.in);
    
    public static void registered_data_package(int id){
        String descripcion;
        float peso;
        float largo;
        float ancho;
        float alto;
        float valor;
        System.out.println("----------------Registro de Paquetes------------------");
        System.out.println("Descripción");
        descripcion = scan_text.next();
        System.out.println("Peso: ");
        peso = scan_text.nextFloat();
        System.out.println("Largo: ");
        largo = scan_text.nextFloat();
        System.out.println("Ancho: ");
        ancho = scan_text.nextFloat();
        System.out.println("Alto: ");
        alto = scan_text.nextFloat();
        System.out.println("Valor: ");
        valor = scan_text.nextFloat();
        PaquetesData paquetes = new PaquetesData(id,Codigo.generateRandomCode(),descripcion,largo,peso,ancho,alto,valor);
        userCreatePaquetes(paquetes);
        
    }
    
    
   
    
    public static void userCreatePaquetes(PaquetesData paquetes) {
        EntityManager manager = JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            manager.getTransaction().begin();
            manager.persist(paquetes);
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
