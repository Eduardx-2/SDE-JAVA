/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package destinatarios;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

/**
 *
 * @author eduardx_2
 */
@Entity
@Table(name ="destinatarios",schema = "paquetes_system")
public class Destinatarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_destinatario;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "telefono")
    private int telefono;
    @Column(name = "correo")
    private String correo;
    @Column(name= "direccion")
    private String direccion;
    @Column(name = "referencias")
    private String referencias;
    @Column(name = "fecha_registro")
    private LocalDateTime fecha_registro;
    @Column(name = "id_envio", nullable = false, unique = true)
    private int id_envio;
    
    public Destinatarios(){
        
    }

    public Destinatarios(String nombre, int telefono, String correo, String direccion, String referencias, LocalDateTime fecha_registro, int id_envio) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.referencias = referencias;
        this.fecha_registro = fecha_registro;
        this.id_envio = id_envio;
    }

    public int getId_destinatario() {
        return id_destinatario;
    }
    
    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getReferencias() {
        return referencias;
    }

    public void setReferencias(String referencias) {
        this.referencias = referencias;
    }

    public LocalDateTime getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(LocalDateTime fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public int getId_envio() {
        return id_envio;
    }

    public void setId_envio(int id_envio) {
        this.id_envio = id_envio;
    }
    
    
    
    
    
   
}
