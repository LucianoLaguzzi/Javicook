package controller;

import dao.IngredienteDAO;
import dao.RecetaDAO;
import datamodel.GenericDataModel;
import model.Ingrediente;
import model.Receta;
import model.Usuario;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@ManagedBean(name="recetaBacking")
@SessionScoped
public class RecetaBacking  extends AbstractBacking<Receta>{

    @EJB
    RecetaDAO recetaDAO;

    @EJB
    IngredienteDAO ingredienteDAO;
    private GenericDataModel<Receta> dataModel;
    private Receta receta;

    private List<Receta> listaRecetas;

    private boolean hayRecetas;

    private String pathFinal = "";


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

    }







//Logica para guardar las recetas
public void registrarReceta() throws Exception {
    try {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        HttpSession session = request.getSession(false);

        // Obtener el usuario actualmente autenticado desde la sesión
        Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuario");
        if (usuarioAutenticado != null) {

            // Obtener los pasos
            String[] pasosArray = request.getParameterValues("pasos");
            List<String> pasos = (pasosArray != null) ? Arrays.asList(pasosArray) : new ArrayList<>();

            // Procesar la imagen
            Part filePart = request.getPart("file"); // Obtener la parte del archivo
            // Verificar si se está enviando un archivo
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = receta.getTitulo(); // Establecer el nombre del archivo (suponiendo que las imágenes son JPG)
                fileName = fileName.toLowerCase().replaceAll("\\s+$", ""); // Convertir a minúsculas y eliminar espacios al final
                fileName = fileName.replaceAll("\\s+", "-"); // Reemplazar espacios por guiones
                fileName += "_img.jpg";
                System.out.println(fileName);

                // Obtener la ruta (VER COMO OBTENER LA RUTA / me agrega /target/snapshot)
                String workingDirectory = System.getProperty("user.dir");
                String fotosPath = workingDirectory + "/src/main/webapp/img/fotos";
                // Reemplazar la parte no deseada de la ruta
                String targetPath = "\\wildfly-25.0.1.Final\\bin";
                pathFinal = fotosPath.replace(targetPath, "");
                System.out.println("Ruta final: " + pathFinal);
                pathFinal = pathFinal + "/" + fileName;
                // Guardar el archivo en el servidor
                try (InputStream input = filePart.getInputStream();
                     OutputStream output = Files.newOutputStream(Paths.get(pathFinal))) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = input.read(buffer)) > 0) {
                        output.write(buffer, 0, length);
                    }
                }
            }else{
                System.out.println("No se obtuvo ninguna imagen");
            }

            //Fecha y hora
            LocalDateTime fechaActual = LocalDateTime.now();
            DateTimeFormatter formatoArgentino = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", new Locale("es", "AR"));
            String fechaYHora = fechaActual.format(formatoArgentino);
            System.out.println(fechaYHora);

            //Categoria
            String categoria = receta.getCategoria();
            //Dificultad
            String dificultad = receta.getDificultad();










            //Ingredientes
            String ingredientesString = request.getParameter("ingredientes");
            String[] nombresIngredientes = (ingredientesString != null) ? ingredientesString.split(",") : new String[0];

            // Crear una lista de objetos de tipo Ingrediente
            List<Ingrediente> ingredientes = new ArrayList<>();
            for (String nombre : nombresIngredientes) {
                Ingrediente nuevoIngrediente = new Ingrediente(nombre,receta);
                ingredienteDAO.create(nuevoIngrediente);
                ingredientes.add(nuevoIngrediente);
            }

            // Ahora tienes una lista de objetos Ingrediente que puedes usar en tu lógica de negocio
            System.out.println("Lista de ingredientes recibidos: " + ingredientes);






            if ((receta.getTitulo() != null) || (!receta.getTitulo().equals(""))) {
                    Receta nuevaReceta = new Receta(receta.getTitulo(), usuarioAutenticado, pasos, pathFinal, fechaYHora,categoria,dificultad,ingredientes);
                    recetaDAO.create(nuevaReceta);
                    receta = new Receta();

                    RecetaBacking recetaBacking = context.getApplication().evaluateExpressionGet(context, "#{recetaBacking}", RecetaBacking.class);
                    recetaBacking.actualizarListaRecetas();
                }

        }else{
            System.out.println("No hay usuario activo");
        }

    }catch (Exception e){
        System.out.println(e);
    }
    finally {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect(externalContext.getRequestContextPath() + "/index.xhtml");
    }
}

    public void actualizarListaRecetas() {
        obtenerRecetas();
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
