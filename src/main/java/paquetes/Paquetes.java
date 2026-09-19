/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paquetes;

import java.util.Scanner;

/**
 *
 * @author eduardx_2
 */
public class Paquetes { 
    
    private static Scanner scan_text = new Scanner(System.in);
    
    public static void registered_data_package(){
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
        
        
        
    }
    
}
