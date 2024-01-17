package dao;

import model.PersistentEntity;
import model.Usuario;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class UsuarioDAO extends AbstractEntityDAO<Usuario> {

//    public UsuarioDAO() {
//        super(Usuario.class);
//    }

    public Usuario findByNombreYContraseña(String nombre, String contraseña) {
        TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findByNombreYContraseña", Usuario.class);
        query.setParameter("nombre", nombre);
        query.setParameter("contraseña", contraseña);
        List<Usuario> usuarios = query.getResultList();
        return usuarios.isEmpty() ? null : usuarios.get(0);
    }

    @Override
    public PersistentEntity findById(Long id) throws Exception {
        return null;
    }
}
