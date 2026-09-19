/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package user;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import java.io.Serializable;
/**
 *
 * @author eduardx_2
 */
@Entity
@Table(name = "usuarios", schema = "paquetes_system")
public class Usuarios implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_usuario;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "apellido")
    private String apellido;
    @Column(name="correo")
    private String correo;
    @Column(name="telefono")
    private String telefono;
    @Column(name="password")
    private String pass;
    
    @Enumerated(EnumType.STRING)
    private Roles rolUser;
    
    protected Usuarios(){
        
    }

    public Usuarios(String nombre, String apellido, String correo, String telefono, String pass, Roles rolUser) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.pass = pass;
        this.rolUser = rolUser;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
    

        
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Roles getRolUser() {
        return rolUser;
    }

    public void setRolUser(Roles rolUser) {
        this.rolUser = rolUser;
    }

   
    
    
}
