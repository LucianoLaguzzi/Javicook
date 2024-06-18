package controller;

import dao.UsuarioDAO;
import datamodel.GenericDataModel;
import model.Receta;
import model.Usuario;
import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@ManagedBean(name="usuarioBacking")
@SessionScoped
public class UsuarioBacking  extends AbstractBacking<Usuario>{

    @EJB
    UsuarioDAO usuarioDAO;

    private GenericDataModel<Usuario> dataModel;
    private Usuario usuario;
    private List<String> erroresLogin = new ArrayList<>();
    private List<String> erroresRegistro = new ArrayList<>();
    private List<String> exitoRegistro = new ArrayList<>();

    private List<Receta> recetasPorUsuario;

    private Part nuevaImagen;

    private String uniqueImageParam;

    public UsuarioBacking() {
        usuario = new Usuario();
    }
    @PostConstruct
    @Override
    public void init() {
        newEntity();
        setDataModel(new GenericDataModel<>(getEntityDAO(), getEntity()));
        setInactivos(false);
        filtrarInactivos();
        erroresLogin = new ArrayList<>();
        obtenerRecetasDeUsuario();
        generateUniqueImageParam();
    }



    public String iniciarSesion() {

        LocalDateTime fechaActual = LocalDateTime.now();
        DateTimeFormatter formatoArgentino = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", new Locale("es", "AR"));
        String fechaYHora = fechaActual.format(formatoArgentino);
        System.out.println(fechaYHora);

        erroresLogin.clear();
        FacesContext context = FacesContext.getCurrentInstance();
        Flash flash = context.getExternalContext().getFlash();

        if (usuario.getNombre() == null || usuario.getNombre().isEmpty() || usuario.getContrasenia() == null || usuario.getContrasenia().isEmpty()) {
            erroresLogin.add("Debe ingresar un usuario y una contraseña");
            FacesMessage errorMessage = new FacesMessage("Debe ingresar un usuario y una contraseña");
            errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(null, errorMessage);
            flash.put("erroresLogin", erroresLogin); // Almacenar mensajes en flash
            usuario.setNombre("");
            usuario.setContrasenia("");

            return "login.xhtml?faces-redirect=true";
        }

        Usuario usuarioEncontrado = usuarioDAO.findByNombreYContrasenia(usuario.getNombre(), usuario.getContrasenia());
        if (usuarioEncontrado != null) {
            context.getExternalContext().getSessionMap().put("usuario", usuarioEncontrado);
            return "index.xhtml?faces-redirect=true";
        } else {
            erroresLogin.add("Nombre de usuario o contraseña incorrectos");
            FacesMessage errorMessage = new FacesMessage("Nombre de usuario o contraseña incorrectos");
            errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(null, errorMessage);
            flash.put("erroresLogin", erroresLogin); // Almacenar mensajes en flash
            usuario.setNombre("");
            usuario.setContrasenia("");
            return "login.xhtml?faces-redirect=true";
        }
    }

    public String redireccionarACrearUsuario() {
        return "crear_usuario.xhtml?faces-redirect=true";
    }

    public String redireccionarALogin() {
        return "login.xhtml?faces-redirect=true";
    }

    public String redireccionarAlIndex() {
        return "index.xhtml?faces-redirect=true";
    }

    public String cerrarSesion() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        System.out.println("Cerrando sesion...");
        return "login.xhtml?faces-redirect=true";
    }


    public String registrarUsuario() throws Exception {
        erroresRegistro.clear();
        exitoRegistro.clear();

        FacesContext context = FacesContext.getCurrentInstance();
        Flash flash = context.getExternalContext().getFlash();

        if (usuario.getNombre() == null || usuario.getNombre().isEmpty() || usuario.getContrasenia() == null || usuario.getContrasenia().isEmpty()) {
            erroresRegistro.add("Usuario y contraseña requeridos para registrarse");
            System.out.println("No se ingreso usuario o contraseña!");

            flash.put("erroresRegistro", erroresRegistro); // Almacenar mensajes en flash
            usuario.setNombre("");
            usuario.setContrasenia("");
            usuario.setEmail("");
            return "crear_usuario.xhtml?faces-redirect=true";
        }

        try {
//Voy a la base de datos para ver si el usuario a crear ya existe
            Usuario verificarUsuario = usuarioDAO.findByNombre(usuario.getNombre());

//Si existe:
            if (verificarUsuario != null) {
                erroresRegistro.clear();
                System.out.println("El usuario a crear ya existe");
                erroresRegistro.add("El usuario ingresado ya existe");
                flash.put("erroresRegistro", erroresRegistro); // Almacenar mensajes en flash
                usuario.setNombre("");
                usuario.setContrasenia("");
                usuario.setEmail("");
                return "crear_usuario.xhtml?faces-redirect=true";
            }
//Si no existe, lo crea:
            exitoRegistro.add("Usuario registrado con éxito");
            System.out.println("Usuario creado!");

            Usuario usuarioCreado = new Usuario(usuario.getNombre(),usuario.getContrasenia(),usuario.getEmail(),"img/default-image.jpg");
            usuarioDAO.create(usuarioCreado);
            setEntity(new Usuario());

            flash.put("exitoRegistro", exitoRegistro);
            usuario.setNombre("");
            usuario.setContrasenia("");
            usuario.setEmail("");
            return "crear_usuario.xhtml?faces-redirect=true";


        }catch (Exception e){
            erroresRegistro.clear();
            System.out.println("Error al registrar usuario");
            erroresRegistro.add("Se produjo un error al crear el usuario");
            flash.put("erroresRegistro", erroresRegistro); // Almacenar mensajes en flash
        }

        return "crear_usuario.xhtml?faces-redirect=true";
    }



    public String irAPerfil() {
        cargarUsuarioDeSesion();
        obtenerRecetasDeUsuario(); // Asegúrate de que las recetas se cargan
        return "perfil?faces-redirect=true";
    }


    public void cargarUsuarioDeSesion() {
        FacesContext context = FacesContext.getCurrentInstance();
        usuario = (Usuario) context.getExternalContext().getSessionMap().get("usuario");
        if (usuario == null) {
            redirigirALogin();
        } else {
            if (usuario.getEmail() == null || usuario.getId() == null) {
                usuario = usuarioDAO.findByIdWithRecetasFavoritas(usuario.getId());
                context.getExternalContext().getSessionMap().put("usuario", usuario);
            }
        }
    }

    public void redirigirALogin() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void obtenerRecetasDeUsuario() {
        if (usuario != null) {
            recetasPorUsuario = usuarioDAO.findRecetasPorUsuario(usuario.getId());
        } else {
            recetasPorUsuario = new ArrayList<>();
        }
    }


    public String verDetallesReceta(int idReceta) {
        // Lógica para cargar los detalles de la receta con el ID proporcionado
        return "detalle_receta?faces-redirect=true&idReceta=" + idReceta;
    }



    public String cambiarImagen() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

        try {
            Part filePart = request.getPart("file");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = usuario.getNombre().toLowerCase().replaceAll("\\s+$", "").replaceAll("\\s+", "-") + "_profile.jpg";

                // Ruta 1: JaviCook\src\main\webapp\img
                String relativeWebPath = "/img";
                ServletContext servletContext = (ServletContext) externalContext.getContext();
                String absoluteDiskPath = servletContext.getRealPath(relativeWebPath);
                String filePath = Paths.get(absoluteDiskPath, fileName).toString();

                // Ruta 2: JaviCook\target\JaviCook-1.0-SNAPSHOT\img
                String workingDirectory = System.getProperty("user.dir");
                String fotosPath = workingDirectory + "/src/main/webapp/img";
                String targetPath = "\\wildfly-25.0.1.Final\\bin";
                String pathFinal = fotosPath.replace(targetPath, "") + "/" + fileName;

                // Leer el archivo en un byte array
                byte[] fileContent;
                try (InputStream input = filePart.getInputStream();
                     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = input.read(buffer)) > 0) {
                        byteArrayOutputStream.write(buffer, 0, length);
                    }
                    fileContent = byteArrayOutputStream.toByteArray();
                }

                // Guardar la imagen en ambas rutas
                try (OutputStream output1 = Files.newOutputStream(Paths.get(filePath))) {
                    output1.write(fileContent);
                }

                try (OutputStream output2 = Files.newOutputStream(Paths.get(pathFinal))) {
                    output2.write(fileContent);
                }

                // Actualizar la referencia de la imagen en la base de datos
                try {
                    usuario.setImagenPerfil(relativeWebPath + "/" + fileName);
                    usuarioDAO.update(usuario);
                    System.out.println("Imagen de perfil actualizada correctamente");

                    // Generar un nuevo parámetro único
                    generateUniqueImageParam();

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error al actualizar la imagen en la base de datos");

                }
            } else {
                System.out.println("Debe seleccionar una imagen para subir");

            }
        } catch (IOException | ServletException e) {
            e.printStackTrace();
            System.out.println("Error al procesar la imagen: " + e.getMessage());

        }

        return null;
    }




    public void toggleRecetaFavorita(Receta receta) throws Exception {
        if (usuario != null) {
            cargarUsuarioDeSesion();
            usuario = usuarioDAO.findByIdWithRecetasFavoritas(usuario.getId());

            if (usuario.getRecetasFavoritas() == null) {
                usuario.setRecetasFavoritas(new ArrayList<>());
            }

            if (receta != null) {
                if (usuario.getRecetasFavoritas().contains(receta)) {
                    usuario.getRecetasFavoritas().remove(receta);
                } else {
                    usuario.getRecetasFavoritas().add(receta);
                }
                usuarioDAO.update(usuario);
            }
        }
    }


    public boolean esRecetaFavorita(Receta receta) {
        cargarUsuarioDeSesion();
        usuario = usuarioDAO.findByIdWithRecetasFavoritas(usuario.getId());
        if (usuario != null && usuario.getRecetasFavoritas() != null) {
            return usuario.getRecetasFavoritas().contains(receta);
        }
        return false;
    }


    public String getUniqueImageParam() {
        return uniqueImageParam;
    }

    public void generateUniqueImageParam() {
        this.uniqueImageParam = UUID.randomUUID().toString();
    }



    @Override
    public void newEntity() {
        setEntity(new Usuario());
    }


    @Override
    public GenericDataModel<Usuario> getDataModel() {
        return dataModel;
    }

    @Override
    public void setDataModel(GenericDataModel<Usuario> dataModel) {
        this.dataModel = dataModel;
    }

    public Usuario getUsuario() {
        if (usuario == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            usuario = (Usuario) context.getExternalContext().getSessionMap().get("usuario");
            if (usuario == null) {
                redirigirALogin();
            }
        }
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    public List<String> getErroresLogin() {
        return erroresLogin;
    }

    public void setErroresLogin(List<String> erroresLogin) {
        this.erroresLogin = erroresLogin;
    }

    public List<String> getErroresRegistro() {
        return erroresRegistro;
    }

    public void setErroresRegistro(List<String> erroresRegistro) {
        this.erroresRegistro = erroresRegistro;
    }

    public List<String> getExitoRegistro() {
        return exitoRegistro;
    }

    public void setExitoRegistro(List<String> exitoRegistro) {
        this.exitoRegistro = exitoRegistro;
    }


    public List<Receta> getRecetasPorUsuario() {
        return recetasPorUsuario;
    }

    public void setRecetasPorUsuario(List<Receta> recetasPorUsuario) {
        this.recetasPorUsuario = recetasPorUsuario;
    }
}