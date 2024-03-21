package controller;

import dao.RecetaDAO;
import datamodel.GenericDataModel;
import model.Receta;
import javax.annotation.PostConstruct;
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

    private List<Receta> listaRecetas;

    private boolean hayRecetas;


    public RecetaBacking() {
        receta = new Receta();
    }
    @PostConstruct
    @Override
    public void init() {
        newEntity();
        setDataModel(new GenericDataModel<>(getEntityDAO(), getEntity()));
        setInactivos(false);
        filtrarInactivos();
        obtenerRecetas();
    }

    @Override
    public void newEntity() {
        setEntity(new Receta());
    }



//Logica para el backing de recetas:

    public void obtenerRecetas() {
        try {
            listaRecetas = recetaDAO.findAll(Receta.class);
            if (!listaRecetas.isEmpty()) {
                System.out.println("Hay recetas disponibles");
                hayRecetas= true;
            } else {
                System.out.println("No hay recetas para mostrar");
                hayRecetas= false;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        hayRecetas= false;
    }









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

    public List<Receta> getListaRecetas() {
        return listaRecetas;
    }

    public void setListaRecetas(List<Receta> listaRecetas) {
        this.listaRecetas = listaRecetas;
    }

    public boolean isHayRecetas() {
        return hayRecetas;
    }

    public void setHayRecetas(boolean hayRecetas) {
        this.hayRecetas = hayRecetas;
    }
}
