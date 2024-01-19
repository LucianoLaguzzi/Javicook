package controller;

import dao.GenericDAO;
import dao.UsuarioDAO;
import datamodel.GenericDataModel;
import model.Usuario;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;


import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@ManagedBean(name="usuarioBacking")
@SessionScoped
public class UsuarioBacking  extends AbstractBacking<Usuario>{


    @EJB
    UsuarioDAO usuarioDAO;

    private GenericDataModel<Usuario> dataModel;

    private Usuario usuario;

    private List<String> erroresLogin = new ArrayList<>();

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

    }


    public String iniciarSesion() {
        erroresLogin.clear();
        FacesContext context = FacesContext.getCurrentInstance();
        Flash flash = context.getExternalContext().getFlash();

        if (usuario.getNombre() == null || usuario.getNombre().isEmpty() || usuario.getContraseña() == null || usuario.getContraseña().isEmpty()) {
            erroresLogin.add("Debe ingresar un usuario y una contraseña");
            FacesMessage errorMessage = new FacesMessage("Debe ingresar un usuario y una contraseña");
            errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(null, errorMessage);
            flash.put("erroresLogin", erroresLogin); // Almacenar mensajes en flash
            return "login.xhtml?faces-redirect=true";
        }

        Usuario usuarioEncontrado = usuarioDAO.findByNombreYContraseña(usuario.getNombre(), usuario.getContraseña());
        if (usuarioEncontrado != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", usuarioEncontrado);
            return "index.xhtml?faces-redirect=true";
        } else {
            erroresLogin.add("Nombre de usuario o contraseña incorrectos");
            FacesMessage errorMessage = new FacesMessage("Nombre de usuario o contraseña incorrectos");
            errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(null, errorMessage);
            flash.put("erroresLogin", erroresLogin); // Almacenar mensajes en flash
            return "login.xhtml?faces-redirect=true";
        }
    }


    public String redireccionarACrearUsuario() {
        return "crear_usuario.xhtml?faces-redirect=true";
    }

    public String redireccionarALogin() {
        return "login.xhtml?faces-redirect=true";
    }


    public String cerrarSesion() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        System.out.println("Cerrando sesion...");
        return "login.xhtml?faces-redirect=true";
    }



    public void agregarContacto() throws Exception {
        System.out.println("Agregando Contacto");
        usuarioDAO.create(usuario);
        setEntity(new Usuario()); // para limpiar el formulario
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
}