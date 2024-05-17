package model;

import controller.UsuarioBacking;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "usuario")
@NamedQueries({
        @NamedQuery(name = "Usuario.findAllActivos", query = "SELECT u FROM Usuario u WHERE u.estado = 'A'"),
        @NamedQuery(name = "Usuario.findAllNamedBy", query = "SELECT u FROM Usuario u WHERE u.estado = 'A'"),
        @NamedQuery(name = "Usuario.findByNombreYContraseña", query = "SELECT u FROM Usuario u WHERE u.nombre = :nombre AND u.contraseña = :contraseña"),
        @NamedQuery(name = "Usuario.findByNombre", query = "SELECT u FROM Usuario u WHERE u.nombre = :nombre"),

})
@SequenceGenerator(name = "SEQ_USU", initialValue = 1, allocationSize = 1)
public class Usuario extends AbstractEntity implements Serializable {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USU")

    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "CONTRASEÑA")
    private String contraseña;


    public Usuario(){
    }

    public Usuario(String nombre, String contraseña, String email) {
        this.nombre = nombre;
        this.contraseña=contraseña;
        this.email=email;
    }


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    @Override
    public String toString() {
        return  "Contacto - Nombre: " + getNombre() + " - Email: " + getEmail();
    }



}
