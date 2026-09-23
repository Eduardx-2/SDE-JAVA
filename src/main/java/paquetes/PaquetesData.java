/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paquetes;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
import jakarta.persistence.*;

/**
 *
 * @author eduardx_2
 */
@Entity
@Table(name = "paquetes", schema = "paquetes_system")
public class PaquetesData implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_paquete;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "largo")
    private float largo;
    @Column(name = "peso")
    private float peso;
    @Column(name = "ancho")
    private float ancho;
    @Column(name = "alto")
    private float alto;
    @Column(name = "valor")
    private float valor;
    @Column(name = "id_usuario")
    private int id_usuario;

    
    public PaquetesData(){
        
    }

    public PaquetesData(int usuario,String codigo, String descripcion, float largo, float peso, float ancho, float alto, float valor) {
        this.id_usuario = usuario;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.largo = largo;
        this.peso = peso;
        this.ancho = ancho;
        this.alto = alto;
        this.valor = valor;
    }

    public int getId_paquete() {
        return id_paquete;
    }

    public void setId_paquete(int id_paquete) {
        this.id_paquete = id_paquete;
    }
    
    

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
    
    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescrip() {
        return descripcion;
    }

    public void setDescrip(String descrip) {
        this.descripcion = descrip;
    }

    public float getLargo() {
        return largo;
    }

    public void setLargo(float largo) {
        this.largo = largo;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public float getAncho() {
        return ancho;
    }

    public void setAncho(float ancho) {
        this.ancho = ancho;
    }

    public float getAlto() {
        return alto;
    }

    public void setAlto(float alto) {
        this.alto = alto;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

   

    
    
    
    
}
