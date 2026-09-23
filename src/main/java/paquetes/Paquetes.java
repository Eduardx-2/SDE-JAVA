/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paquetes;

import com.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
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
        descripcion = scan_text.nextLine();
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
    
    public static void eliminated_paquetes(){
        String code;
        System.out.println("---------- Eliminar Paquete ----------\n");
        System.out.print("Codigo de Paquete: ");
        code = scan_text.next();
        EntityManager manager = JpaUtil.getEntityManagerFactory().createEntityManager(); 
        try { 
            manager.getTransaction().begin(); 
            String jpql = "DELETE FROM PaquetesData u WHERE u.codigo = :codigo"; 
            int filas = manager.createQuery(jpql).setParameter("codigo", code) .executeUpdate(); 
            manager.getTransaction().commit(); 
            if (filas > 0) { 
                System.out.println("Paquete eliminado"); 
            } else { 
                System.out.println("No existe ningún paquete con ese CODIGO."); 
            } 
        } catch (Exception e) { 
            if (manager.getTransaction().isActive()) { 
                manager.getTransaction().rollback(); 
            } 
            e.printStackTrace(); } finally { manager.close(); 
        }
    }
    //función para consultar paquetes
    public static void consultarMispaquetes(int id){
        System.out.println("------------- PAQUETES -------------");
        List<PaquetesData> paquetes = sqlPaquetes(id);
        if (paquetes.isEmpty()) {
            System.out.println("No hay paquetes registrados.");
            return;
        }
        for (PaquetesData paq : paquetes) {
            System.out.println(paq.toString());
        }
    }
    
    // funcion que busca paquetes del usuarios por medio del codigo
    public static void buscarMispaquetes(){
        String codigo;
        System.out.println("------------Busqueda de Codigo por paquete-------------");
        System.out.print("Ingrese el codigo: ");
        codigo = scan_text.next();
        PaquetesData paq = sqlSearchPaquetes(codigo);
        System.out.println(paq.toString()); 
    }
    
    // se conecta a la base de datos y inserta el paquete a registrar
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
    
    //JPQL para buscar el paquete median el codigo
    private static PaquetesData sqlSearchPaquetes(String id){
        EntityManager manager = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<PaquetesData> userQuery = manager.createQuery(
                    "SELECT u FROM PaquetesData u WHERE u.codigo = :codigo",
                    PaquetesData.class
            );
            userQuery.setParameter("codigo", id); //ingresa el valor id recibido en el campo :codigo
            return userQuery.getResultStream().findFirst().orElse(null);
        }finally{
            manager.close();
        }
    }
    
    private static List<PaquetesData> sqlPaquetes(int id){
        EntityManager manager = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<PaquetesData> userQuery = manager.createQuery(
                    "SELECT u FROM PaquetesData u WHERE u.id_usuario = :id_usuario",
                    PaquetesData.class
            );
            userQuery.setParameter("id_usuario", id);
            return userQuery.getResultList();
        }finally{
            manager.close();
        }
    }
}
