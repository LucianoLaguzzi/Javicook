package controller;

import dao.ComentarioDAO;
import dao.RecetaDAO;
import dao.UsuarioDAO;
import datamodel.GenericDataModel;
import model.*;

import javax.ejb.EJB;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;


@ManagedBean(name="detalleRecetaBacking")
@SessionScoped
public class DetalleRecetaBacking extends AbstractBacking<Receta> {

    @EJB
    RecetaDAO recetaDAO;

    @EJB
    UsuarioDAO usuarioDAO;

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

//            Aca podria ir el envio de mail
            enviarCorreoConfirmacion(usuarioActual.getEmail(), comentario);

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



    public boolean esPropietarioReceta() {
        Usuario usuarioAutenticado = obtenerUsuarioActual();
        return usuarioAutenticado != null && receta.getUsuario().getId() == usuarioAutenticado.getId();
    }


    public String eliminarReceta(Long idReceta) {
        try {
            Usuario usuarioAutenticado = obtenerUsuarioActual();
            if (usuarioAutenticado == null) {
                System.out.println("Usuario no autenticado.");
                return null;
            }

            System.out.println("Usuario autenticado: " + usuarioAutenticado.getId());
            // Eliminar la receta de la base de datos
            eliminarImagenReceta(receta);
            // Eliminar los comentarios asociados a la receta
            comentarioDAO.eliminarComentariosDeReceta(idReceta);
//            Eliminar antes de la lista de favoritos
            eliminarFavoritoEnReceta(usuarioAutenticado,receta);

//            Eliminar valoracion en valoracion_usuario
            eliminarValoracionDelUsuario(usuarioAutenticado,receta);

            recetaDAO.eliminarReceta(idReceta);
            System.out.println("Receta eliminada: " + idReceta);
            return "index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void eliminarImagenReceta(Receta receta) {
        if (receta.getImagen() != null && !receta.getImagen().isEmpty()) {
            try {
                // Obtener el nombre del archivo de la ruta de la imagen en la receta
                String imageName = receta.getImagen().substring(receta.getImagen().lastIndexOf('/') + 1);

                // Obtener la ruta absoluta de la imagen en la carpeta del proyecto
                String relativeWebPath = "/img/fotos";
                FacesContext context = FacesContext.getCurrentInstance();
                ExternalContext externalContext = context.getExternalContext();
                ServletContext servletContext = (ServletContext) externalContext.getContext();
                String absoluteDiskPath = servletContext.getRealPath(relativeWebPath);

                // Construir la ruta completa para eliminar la imagen
                String filePath = Paths.get(absoluteDiskPath, imageName).toString();
                File file = new File(filePath);
                if (file.delete()) {
                    System.out.println("El archivo se eliminó correctamente.");
                } else {
                    System.out.println("No se pudo eliminar el archivo.");
                }
                // Obtener la ruta absoluta de la imagen en el sistema de archivos del proyecto
                String workingDirectory = System.getProperty("user.dir");
                String fotosPath = workingDirectory + "/src/main/webapp/img/fotos";
                String targetPath = "\\wildfly-25.0.1.Final\\bin";
                String pathFinalSnapshot = fotosPath.replace(targetPath, "") + "/" + imageName;
                File file2 = new File(pathFinalSnapshot);
                if (file2.delete()) {
                    System.out.println("El archivo se eliminó correctamente.");
                } else {
                    System.out.println("No se pudo eliminar el archivo.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    public void eliminarFavoritoEnReceta(Usuario usuarioAutenticado, Receta receta) throws Exception {
        usuarioAutenticado = usuarioDAO.findByIdWithRecetasFavoritas(usuarioAutenticado.getId());
        if (usuarioAutenticado == null) {
            System.out.println("Usuario no autenticado.");
        }else {
            usuarioAutenticado.getRecetasFavoritas().remove(receta);
            usuarioDAO.update(usuarioAutenticado);
            System.out.println("La receta" + receta + " fue removida de la lista de favoritos" );
        }
    }


    public void eliminarValoracionDelUsuario (Usuario usuarioAutenticado, Receta receta) throws Exception {
        usuarioAutenticado = usuarioDAO.findByIdWithRecetasFavoritas(usuarioAutenticado.getId());
        if (usuarioAutenticado == null) {
            System.out.println("Usuario no autenticado.");
        }else {
            recetaDAO.eliminarValoracionDeUsuario(receta.getId());
        }

    }



    // Método para enviar correo de confirmación
    private void enviarCorreoConfirmacion(String emailDestinatario, Comentario comentario) {
        // Configuración de JavaMail para enviar correo electrónico
        String host = "smtp.gmail.com";
        String username = "javicook.app@gmail.com"; // Tu dirección de Gmail
        String password = "tdhqvpfqpzqhrbys"; // Contraseña de aplicación generada
        int port = 587; // Puerto SMTP para Gmail

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");

        // Habilitar TLS
        props.put("mail.smtp.starttls.enable", "true");

        // Crear la sesión de JavaMail
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Preparar el mensaje de correo
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username)); // Dirección del remitente (tu dirección de Gmail)
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestinatario)); // Destinatario del correo
            message.setSubject("Confirmación de Comentario");
            message.setText("Hola,\n\nTu comentario ha sido registrado correctamente en nuestra aplicación.");

            // Enviar el correo
            Transport.send(message);

            System.out.println("Correo de confirmación enviado a " + emailDestinatario);

        } catch (SendFailedException e) {
            System.out.println("Error: Dirección de correo no válida o no puede recibir correos: " + emailDestinatario);
            // Aquí puedes manejar el error, por ejemplo, notificando al usuario
        } catch (MessagingException e) {
            System.out.println("Error al enviar correo de confirmación: " + e.getMessage());
        }
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


