package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "receta")
@NamedQueries({
        @NamedQuery(name = "Receta.findAll", query = "SELECT r FROM Receta r"),

})
@SequenceGenerator(name = "SEQ_REC", initialValue = 1, allocationSize = 1)
public class Receta extends AbstractEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_REC")

    @Id
    @Column(name = "ID", nullable = false)
    private Long id;


    @Column(name = "TITULO")
    private String titulo;

    @Column(name = "PASOS")
    private String pasos;

    @Column(name = "IMAGEN")
    private String imagen;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA")
    private Date fecha;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    private Usuario usuario;

    @Column(name = "CATEGORIA")
    private String categoria;

    @ManyToMany(mappedBy = "recetas")
    private List<Ingrediente> ingredientes;

    @Column(name = "VALORACION")
    private int valoracion;

    @Column(name = "DIFUCULTAD")
    private String dificultad;

    @Column(name = "TIEMPO_PREPARACION")
    private String tiempo_preparacion;




//Getter y Setter
    @Override
    public Long getId() {
        return id;
    }
    @Override
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getPasos() {
        return pasos;
    }
    public void setPasos(String pasos) {
        this.pasos = pasos;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public String getTiempo_preparacion() {
        return tiempo_preparacion;
    }

    public void setTiempo_preparacion(String tiempo_preparacion) {
        this.tiempo_preparacion = tiempo_preparacion;
    }

    @Override
    public String toString() {
        return "Receta: " + getTitulo();
    }
}
