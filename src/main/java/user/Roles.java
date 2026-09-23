/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package user;

/**
 *
 * @author eduardx_2
 */
public enum Roles {
    ADMIN(1,"ADMIN"),
    USER(2,"USER"),
    CLIENTE(3,"CLIENTE");
    
    private final int role;
    private final String value;

    Roles(int role, String value){
        this.role = role;
        this.value = value;
    }


    public int getRole() {
        return role;
    }

    public String getValue() {
        return value;
    }
    
    public static String verificationsUser(String user){
        for (Roles rol: Roles.values()){
            if(rol.value == null ? user == null : rol.value.equals(user)){
                return rol.value;
            }
        }
        return null;
    }
    
    public static Roles verification_asset(int co){
        for(Roles rol: Roles.values()){
            if(rol.role == co){
                return rol;
            }
        }
        throw new IllegalArgumentException("[-] USTED INGRESO UN ROL EQUIVOCADO");
    }
    
}
