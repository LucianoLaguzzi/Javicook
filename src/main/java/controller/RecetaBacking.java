package controller;

import dao.RecetaDAO;
import datamodel.GenericDataModel;
import model.Ingrediente;
import model.Receta;
import model.Usuario;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;


@ManagedBean(name="recetaBacking")
@SessionScoped
public class RecetaBacking  extends AbstractBacking<Receta>{

    @EJB
    RecetaDAO recetaDAO;

    private GenericDataModel<Receta> dataModel;

    private Receta receta;



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



//Logica para el backing de recetas:






    //Getters y Setters
    @Override
    public GenericDataModel<Receta> getDataModel() {
        return dataModel;
    }
    @Override
    public void setDataModel(GenericDataModel<Receta> dataModel) {
        this.dataModel = dataModel;
    }

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }


}
