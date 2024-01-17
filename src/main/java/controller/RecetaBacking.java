package controller;

import datamodel.GenericDataModel;
import model.Receta;
import model.Usuario;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;

@ManagedBean(name="recetaBacking")
@SessionScoped
public class RecetaBacking  extends AbstractBacking<Receta>{


    private GenericDataModel<Receta> dataModel;

    private Receta receta;
    private Receta NuevaReceta;

    // Agrega la propiedad recetas y su correspondiente getter
    private List<Receta> recetas;

    public RecetaBacking() {
        receta = new Receta();
    }

    @Override
    public void init() {
        newEntity();
        setDataModel(new GenericDataModel<>(getEntityDAO(), getEntity()));
        setInactivos(false);
        filtrarInactivos();
    }

    @Override
    public void newEntity() {
        setEntity(new Receta());
    }


    //Getters y Setters


    public Receta getNuevaReceta() {
        return NuevaReceta;
    }

    public void setNuevaReceta(Receta nuevaReceta) {
        NuevaReceta = nuevaReceta;
    }

    public List<Receta> getRecetas() {
        return recetas;
    }
    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
    }
}
