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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


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

    public UsuarioBacking() {
        usuario = new Usuario();
    }
    @PostConstruct
    @Override
    public void init() {
        newEntity();
//      setEntityDAO(usuarioDAO);
        setDataModel(new GenericDataModel<>(getEntityDAO(), getEntity()));
        setInactivos(false);
        filtrarInactivos();
        erroresLogin = new ArrayList<>();
        obtenerRecetasDeUsuario();

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
//            FacesMessage errorMessage = new FacesMessage("Usuario y contraseña requeridos");
//            errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
//            context.addMessage(null, errorMessage);
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
//                FacesMessage message = new FacesMessage("Usuario ya existente");
//                message.setSeverity(FacesMessage.SEVERITY_ERROR);
//                context.addMessage(null, message);
                flash.put("erroresRegistro", erroresRegistro); // Almacenar mensajes en flash
                usuario.setNombre("");
                usuario.setContrasenia("");
                usuario.setEmail("");
                return "crear_usuario.xhtml?faces-redirect=true";
            }
//Si no existe, lo crea:
        exitoRegistro.add("Usuario registrado con éxito");
        System.out.println("Usuario creado!");
//        FacesMessage message = new FacesMessage("Usuario creado con éxito");
//        message.setSeverity(FacesMessage.SEVERITY_INFO);
//        context.addMessage(null, message);

        Usuario usuarioCreado = new Usuario(usuario.getNombre(),usuario.getContrasenia(),usuario.getEmail());
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
//            FacesMessage message = new FacesMessage("Error al crear el usuario");
//            message.setSeverity(FacesMessage.SEVERITY_ERROR);
//            context.addMessage(null, message);

            flash.put("erroresRegistro", erroresRegistro); // Almacenar mensajes en flash
        }

        return "crear_usuario.xhtml?faces-redirect=true";

    }



    public String irAPerfil() {
        cargarUsuarioDeSesion();
        return "perfil?faces-redirect=true";
    }



    public void cargarUsuarioDeSesion() {
        FacesContext context = FacesContext.getCurrentInstance();
        usuario = (Usuario) context.getExternalContext().getSessionMap().get("usuario");
        if (usuario == null) {
            redirigirALogin();
        } else {
            if (usuario.getEmail() == null || usuario.getId() == null) {
                usuario = usuarioDAO.findByNombre(usuario.getNombre());
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
        FacesContext context = FacesContext.getCurrentInstance();
        Usuario usuarioAutenticado = usuarioDAO.findByNombreYContrasenia(usuario.getNombre(), usuario.getContrasenia());
        if (usuarioAutenticado != null) {
            context.getExternalContext().getSessionMap().put("usuario", usuarioAutenticado);
            recetasPorUsuario = usuarioDAO.findRecetasPorUsuario(usuarioAutenticado.getId());
        }else{
            recetasPorUsuario = new ArrayList<>();
        }

    }


    public String verDetallesReceta(int idReceta) {
        // Lógica para cargar los detalles de la receta con el ID proporcionado
        return "detalle_receta?faces-redirect=true&idReceta=" + idReceta;
    }


    @Override
    public void newEntity() {
        setEntity(new Usuario());
    }


//    public GenericDAO<Usuario> getUsuarioDAO() {
//        return usuarioDAO;
//    }
//
//    public void setUsuarioDAO(GenericDAO<Usuario> usuarioDAO) {
//        this.usuarioDAO = usuarioDAO;
//    }

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
            cargarUsuarioDeSesion();
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
