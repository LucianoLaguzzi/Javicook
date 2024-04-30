package controller;

import dao.AbstractEntityDAO;
import dao.RecetaDAO;
import datamodel.GenericDataModel;
import model.Ingrediente;
import model.Receta;
import model.RecetaIngredienteCantidad;
import model.Usuario;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@ManagedBean
@ViewScoped
public class DetalleRecetaBacking extends AbstractBacking<Receta> {

    @EJB
    RecetaDAO recetaDAO;

    private Receta receta;

    private List<String> cantidadIngredientes = new ArrayList<>();

    public DetalleRecetaBacking() {
        receta = new Receta();
    }


    @PostConstruct
    @Override
    public void init() {
        newEntity();
        setDataModel(new GenericDataModel<>(getEntityDAO(), getEntity()));
        setInactivos(false);
        filtrarInactivos();

        // Obtener el ID de la receta de los parámetros de la solicitud
        FacesContext context = FacesContext.getCurrentInstance();
        String idRecetaString = context.getExternalContext().getRequestParameterMap().get("idReceta");


        if (idRecetaString != null) {
            long idReceta = Long.parseLong(idRecetaString);
            try {
                receta = recetaDAO.findById(Receta.class, idReceta);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String tituloAMayuscula(String titulo) {
        if (titulo == null || titulo.isEmpty()) {
            return titulo; // Devuelve null o cadena vacía si el título es nulo o vacío
        }
        // Convierte la primera letra del título en mayúscula y conserva el resto de la cadena
        return Character.toUpperCase(titulo.charAt(0)) + titulo.substring(1);
    }


    public List<String> obtenerCantidadesIngredientes(Long idReceta) {
        try {
            cantidadIngredientes = recetaDAO.findCantidadIngredientes(receta.getId());
            if (!cantidadIngredientes.isEmpty()){
            return cantidadIngredientes;
            }else{
                System.out.println("Error, no se obtuvieron Ingredientes y cantidades para la receta");
                List<String> listaVacia = new ArrayList<>();
                listaVacia.add("No hay");
                return listaVacia;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    public String formatIngredientes(String ingredientes) {
        String[] ingredientesArray = ingredientes.split("\\r\\n");
        StringBuilder builder = new StringBuilder();
        for (String ingrediente : ingredientesArray) {
            // Capitalizar la primera letra del ingrediente
            ingrediente = capitalizeFirstLetter(ingrediente);
            // Agregar el ingrediente al StringBuilder
            builder.append(ingrediente).append("<br>");
        }
        return builder.toString();
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }










    @Override
    public void newEntity() {
        setEntity(new Receta());
    }


    // Getter y Setter para receta
    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }

    public List<String> getCantidadIngredientes() {
        return cantidadIngredientes;
    }

    public void setCantidadIngredientes(List<String> cantidadIngredientes) {
        this.cantidadIngredientes = cantidadIngredientes;
    }
}


