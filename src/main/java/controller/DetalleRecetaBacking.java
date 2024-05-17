package controller;

import dao.ComentarioDAO;
import dao.RecetaDAO;
import datamodel.GenericDataModel;
import model.*;

import javax.ejb.EJB;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;



@ManagedBean(name="detalleRecetaBacking")
@SessionScoped
public class DetalleRecetaBacking extends AbstractBacking<Receta> {

    @EJB
    RecetaDAO recetaDAO;

    private Receta receta;


    private List<String> cantidadIngredientes = new ArrayList<>();

    @EJB
    private ComentarioDAO comentarioDAO;

    private String nuevoComentario;
    private List<Comentario> comentarios= new ArrayList<>();


    public DetalleRecetaBacking() {
        receta = new Receta();
    }

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

        nuevoComentario = null;
        cargarComentarios();

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
            // Separar el ingrediente y la cantidad por ":"
            String[] partes = ingrediente.split(":");
            if (partes.length == 2) { // Verificar si hay dos partes separadas por ":"
                String nombreIngrediente = capitalizeFirstLetter(partes[0].trim()); // Capitalizar y eliminar espacios en blanco
                String cantidad = partes[1].trim(); // Eliminar espacios en blanco
                // Verificar si hay un espacio después de ":"
                if (!cantidad.startsWith(" ")) {
                    // Agregar un espacio entre ":" y la cantidad
                    cantidad = " " + cantidad;
                }
                // Construir el ingrediente formateado y agregarlo al StringBuilder
                builder.append(nombreIngrediente).append(":").append(cantidad).append("<br>");
            } else {
                // Si no hay dos partes separadas por ":", agregar el ingrediente sin cambios
                builder.append(ingrediente).append("<br>");
            }
        }
        return builder.toString();
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }


    public String cerrarSesion() {
        System.out.println("Se entro al metodo cerrar sesion del backing DetalleReceta");
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        System.out.println("Cerrando sesion...");
        return "login.xhtml?faces-redirect=true";
    }



    public String valorarReceta(int valoracionSeleccionada) throws Exception {
        // Obtener el ID de la receta
        Long idReceta = receta.getId();
        Usuario usuarioActual = obtenerUsuarioActual();


        // Guardar la valoración del usuario en la base de datos
        guardarValoracionUsuario(usuarioActual, idReceta, valoracionSeleccionada);

        // Recalcular la valoración promedio de la receta
        recalcularValoracionPromedio(idReceta);

        // Redirigir a la misma página o a donde desees después de la valoración
        return "detalle_receta?faces-redirect=true&idReceta=" + idReceta;
    }


    private void guardarValoracionUsuario(Usuario usuario, Long idReceta, int valoracionSeleccionada) throws Exception {
        try {
            if (!recetaDAO.findUsuarioHaValoradoReceta(usuario, idReceta)) {
                // Crea una nueva instancia de ValoracionUsuario y asigna los valores
                ValoracionUsuario valoracionUsuario = new ValoracionUsuario(usuario, receta, valoracionSeleccionada);
                // Guarda la valoración del usuario en la base de datos
                recetaDAO.create(valoracionUsuario);
            }else{
                // Si el usuario ya ha valorado la receta, muestra un mensaje o realiza alguna acción adecuada
                System.out.println("El usuario ya ha valorado esta receta.");
            }

        }catch (Exception e){
            System.out.println(e);
        }

    }



    private void recalcularValoracionPromedio(Long idReceta) throws Exception {

        try {
            List<ValoracionUsuario> valoraciones = recetaDAO.findAllValoracionUsuario(idReceta);
            int sumaValoraciones = 0;
            for (ValoracionUsuario valoracion : valoraciones) {
                sumaValoraciones += valoracion.getValoracion();
            }
            int valoracionPromedio = valoraciones.isEmpty() ? 0 : sumaValoraciones / valoraciones.size();
            receta.setValoracion(valoracionPromedio);
            recetaDAO.update(receta);
        }catch (Exception e){
            System.out.println(e);
        }

    }





//    public List<Integer> getEstrellas() {
//        List<Integer> estrellas = new ArrayList<>();
//        int valoracion = receta.getValoracion(); // Obtener la valoración actual de la receta
//        for (int i = 1; i <= 5; i++) { // Loop para generar las estrellas
//            if (i <= valoracion) {
//                estrellas.add(i); // Agregar estrella llena si el índice es menor o igual a la valoración
//            } else {
//                estrellas.add(0); // Agregar estrella vacía si el índice es mayor que la valoración
//            }
//        }
//        return estrellas;
//    }



    public int getValoracionUsuario() {
        Usuario usuarioActual = obtenerUsuarioActual(); // Método para obtener el usuario actual
        if (usuarioActual != null) {
            try {
                ValoracionUsuario valoracionUsuario = recetaDAO.findValoracionUsuario(usuarioActual, receta.getId());
                if (valoracionUsuario != null) {
                    return valoracionUsuario.getValoracion();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0; // Si no se puede obtener la valoración del usuario, devolver 0 por defecto
    }

    public Usuario obtenerUsuarioActual() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (Usuario) session.getAttribute("usuario");
        } else {
            return null;
        }
    }




    private void cargarComentarios() {
        comentarios.clear();
        if (receta != null && receta.getId() != null) {
            comentarios.addAll(comentarioDAO.findByRecetaId(receta.getId())); // Agregar los nuevos comentarios
        }
    }

    public void agregarComentario() throws Exception {
        try {
            // Obtener el usuario actual y la receta actual
            Usuario usuarioActual = obtenerUsuarioActual();
            if (usuarioActual == null) {
                return;
            }
            if (nuevoComentario == null || nuevoComentario.equals("")) {
                return;
            }
            // Crear un nuevo comentario
            Comentario comentario = new Comentario(usuarioActual,receta,nuevoComentario);

            // Guardar el comentario en la base de datos
            comentarioDAO.create(comentario);

            // Recargar los comentarios después de agregar uno nuevo
            cargarComentarios();

            // Limpiar el campo de nuevo comentario
            nuevoComentario = null;


            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(externalContext.getRequestContextPath() + "/index.xhtml");

        }catch (Exception e){
            System.out.println(e);
        }

    }


    public String obtenerFechaComentario(Comentario comentario) {
        try {
            return comentarioDAO.findFechaByComentario(comentario);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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


    public String getNuevoComentario() {
        return nuevoComentario;
    }

    public void setNuevoComentario(String nuevoComentario) {
        this.nuevoComentario = nuevoComentario;
    }



    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }



}


