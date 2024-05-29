package controller;

import dao.IngredienteDAO;
import dao.RecetaDAO;
import datamodel.GenericDataModel;
import model.Ingrediente;
import model.Receta;
import model.Usuario;

import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
import java.util.stream.Collectors;


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

    private List<String> ingredientesCantidades = new ArrayList<>();

    private int cantidadRecetasCargadas = 0; // Inicializa la cantidad de recetas cargadas
    private int cantidadRecetasPorLote = 7; // Establece la cantidad de recetas por lote
    private String filtroIngredientes;


    // Nueva lista para almacenar el top 3 de recetas
    private List<Receta> top3Recetas = new ArrayList<>();




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
        cantidadRecetasCargadas = 0;
        obtenerRecetas();
        cargarTop3Recetas();

    }

    @Override
    public void newEntity() {
        setEntity(new Receta());
    }



//Logica para el backing de recetas:

    public void obtenerRecetas() {
        try {
            if (filtroIngredientes != null && !filtroIngredientes.trim().isEmpty()) {
                listaRecetas = recetaDAO.buscarPorIngredientes(filtroIngredientes);
            } else {
                listaRecetas = recetaDAO.findRange(cantidadRecetasCargadas, cantidadRecetasPorLote);
            }

            if (!listaRecetas.isEmpty()) {
                hayRecetas = true;
            } else {
                System.out.println("No hay recetas para mostrar");
                hayRecetas = false;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    // Método para cargar más recetas cuando se presiona el botón "Ver +"
    public void cargarMasRecetas() {
        cantidadRecetasPorLote += cantidadRecetasPorLote; // Incrementa la cantidad de recetas cargadas
        obtenerRecetas(); // Vuelve a obtener las recetas para cargar el siguiente lote
    }


    // Nuevo método para cargar el top 3 de recetas
    public void cargarTop3Recetas() {
        List<Receta> valoradas = recetaDAO.findTop3Recetas().stream()
                .filter(receta -> receta.getValoracion() > 0)
                .sorted((r1, r2) -> Integer.compare(r2.getValoracion(), r1.getValoracion()))
                .collect(Collectors.toList());

        top3Recetas.clear();
        top3Recetas.addAll(valoradas);
        completarConRecetasVacias(top3Recetas);
    }

    private void completarConRecetasVacias(List<Receta> recetas) {
        while (recetas.size() < 3) {
            recetas.add(null); // Usa null como marcador de posición para recetas vacías
        }
    }


    public void buscarPorIngredientes() {
        if (filtroIngredientes == null || filtroIngredientes.trim().isEmpty()) {
            filtroIngredientes = null;
            cantidadRecetasCargadas = 0;
            obtenerRecetas();
        } else {
            listaRecetas = recetaDAO.buscarPorIngredientes(filtroIngredientes);
        }
        hayRecetas = !listaRecetas.isEmpty();

        // Redirecciona para evitar reenviar formulario
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        try {
            externalContext.redirect(externalContext.getRequestContextPath() + "/index.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
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



            //Cantidades e ingredientes
            String[] cantidadesArray = request.getParameterValues("cantidadIngrediente");
            List<String> cantidades = (cantidadesArray != null) ? Arrays.asList(cantidadesArray) : new ArrayList<>();


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

            //Tiempo de preparacion
            String tiempo_preparacion = receta.getTiempo_preparacion();


            //Ingredientes
            String ingredientesString = request.getParameter("ingredientes");
            String[] nombresIngredientes = (ingredientesString != null) ? ingredientesString.toLowerCase().split(",") : new String[0];

            if ((receta.getTitulo() != null) || (!receta.getTitulo().equals(""))) {
                    Receta nuevaReceta = new Receta(receta.getTitulo(),cantidades, usuarioAutenticado, pasos, pathFinal, fechaYHora,categoria,dificultad,tiempo_preparacion);

                recetaDAO.create(nuevaReceta);

                System.out.println("Receta creada con exito!");

                // Crear una lista de objetos de tipo Ingrediente
                List<Ingrediente> ingredientes = new ArrayList<>();
                for (String nombre : nombresIngredientes) {
                    Ingrediente ingredienteExistente = ingredienteDAO.findByNombre(nombre);
                    // Si el ingrediente existe, añadirlo a la lista de ingredientes de la receta
                    // Si no existe, crear uno nuevo y añadirlo a la lista de ingredientes de la receta
                    if (ingredienteExistente != null) {
                        ingredientes.add(ingredienteExistente);
                    } else {
                        Ingrediente nuevoIngrediente = new Ingrediente(nombre);
                        ingredienteDAO.create(nuevoIngrediente);
                        ingredientes.add(nuevoIngrediente);
                    }
                }
                // Ahora tienes una lista de objetos Ingrediente que puedes usar en tu lógica de negocio
                System.out.println("Lista de ingredientes recibidos: " + ingredientes);
                // Asignar los ingredientes a la receta
                nuevaReceta.setIngredientes(ingredientes);
                // Actualizar la receta con la lista de ingredientes
                recetaDAO.update(nuevaReceta);

                receta = new Receta();

                RecetaBacking recetaBacking = context.getApplication().evaluateExpressionGet(context, "#{recetaBacking}", RecetaBacking.class);
                recetaBacking.actualizarListaRecetas();
                }


        }else{
            System.out.println("No hay usuario activo");
        }

    }catch (Exception e){
        e.printStackTrace();
        System.out.println("Error" + e.getMessage());
    }
    finally {
        actualizarListaRecetas();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect(externalContext.getRequestContextPath() + "/index.xhtml");
    }
}

    public void actualizarListaRecetas() {
        obtenerRecetas();
    }



    public String verDetallesReceta(int idReceta) {
        // Lógica para cargar los detalles de la receta con el ID proporcionado
        return "detalle_receta?faces-redirect=true&idReceta=" + idReceta;
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

    public List<String> getIngredientesCantidades() {
        return ingredientesCantidades;
    }

    public void setIngredientesCantidades(List<String> ingredientesCantidades) {
        this.ingredientesCantidades = ingredientesCantidades;
    }
    public String getFiltroIngredientes() {
        return filtroIngredientes;
    }

    public void setFiltroIngredientes(String filtroIngredientes) {
        this.filtroIngredientes = filtroIngredientes;
    }

    public List<Receta> getTop3Recetas() {
        return top3Recetas;
    }

    public void setTop3Recetas(List<Receta> top3Recetas) {
        this.top3Recetas = top3Recetas;
    }


}
