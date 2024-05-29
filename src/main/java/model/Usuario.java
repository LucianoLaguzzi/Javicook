package model;

import controller.UsuarioBacking;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "usuario")
@NamedQueries({
        @NamedQuery(name = "Usuario.findAllActivos", query = "SELECT u FROM Usuario u WHERE u.estado = 'A'"),
        @NamedQuery(name = "Usuario.findAllNamedBy", query = "SELECT u FROM Usuario u WHERE u.estado = 'A'"),
        @NamedQuery(name = "Usuario.findByNombreYContrasenia", query = "SELECT u FROM Usuario u WHERE u.nombre = :nombre AND u.contrasenia = :contrasenia"),
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

    @Column(name = "CONTRASENIA")
    private String contrasenia;


    public Usuario(){
    }

    public Usuario(String nombre, String contrasenia, String email) {
        this.nombre = nombre;
        this.contrasenia=contrasenia;
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

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    @Override
    public String toString() {
        return  getNombre();
    }



}
