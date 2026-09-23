/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package envios;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author eduardx_2
 */
@Entity
@Table(name = "envios", schema = "paquetes_system")
public class EnviosDb implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_envio;
    @Column(name = "codigo_seguimiento")
    private String codigo_seguimiento;
    @Column(name = "id_paquete")
    private int id_paquete;
    @Column(name = "id_usuario")
    private int id_usuario;
    @Column(name = "destinatario")
    private String destinatario;
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "estado")
    private String estado;
    @Column(name = "fecha_envio")
    private String fecha_envio;
    @Column(name = "fecha_entrega")
    private String fecha_entrega;

    public EnviosDb(){
        
    }
    public EnviosDb(String codigo_seguimiento, int id_paquete, int id_usuario, String destinatario, String direccion, String estado, String fecha_envio, String fecha_entrega) {
        this.codigo_seguimiento = codigo_seguimiento;
        this.id_paquete = id_paquete;
        this.id_usuario = id_usuario;
        this.destinatario = destinatario;
        this.direccion = direccion;
        this.estado = estado;
        this.fecha_envio = fecha_envio;
        this.fecha_entrega = fecha_entrega;
    }

    
    public int getId_envio() {
        return id_envio;
    }

    public void setId_envio(int id_envio) {
        this.id_envio = id_envio;
    }

    public String getCodigo_seguimiento() {
        return codigo_seguimiento;
    }

    public void setCodigo_seguimiento(String codigo_seguimiento) {
        this.codigo_seguimiento = codigo_seguimiento;
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

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha_envio() {
        return fecha_envio;
    }

    public void setFecha_envio(String fecha_envio) {
        this.fecha_envio = fecha_envio;
    }

    public String getFecha_entrega() {
        return fecha_entrega;
    }

    public void setFecha_entrega(String fecha_entrega) {
        this.fecha_entrega = fecha_entrega;
    }
    
    
    
}
