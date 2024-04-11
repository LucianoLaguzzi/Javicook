package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "ingrediente")
@NamedQueries({
        @NamedQuery(name = "Ingrediente.findAll", query = "SELECT i FROM Ingrediente i"),

})
@SequenceGenerator(name = "SEQ_ING", initialValue = 1, allocationSize = 1)
public class Ingrediente extends AbstractEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ING")

    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "CANTIDAD")
    private String cantidad;

    @ManyToMany
    @JoinTable(
            name = "receta_ingrediente",
            joinColumns = @JoinColumn(name = "id_ingrediente"),
            inverseJoinColumns = @JoinColumn(name = "id_receta")
    )
    private List<Receta> recetas;


    public Ingrediente() {

    }

    public Ingrediente(String nombre, Receta receta){
        this.nombre = nombre;
//        this.recetas.

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
    public String getCantidad() {
        return cantidad;
    }
    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
    public List<Receta> getRecetas() {
        return recetas;
    }
    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
    }

    @Override
    public String toString() {
        return "Ingrediente: " + getNombre() ;
    }
}
