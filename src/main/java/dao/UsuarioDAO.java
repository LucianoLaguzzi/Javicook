package dao;

import model.PersistentEntity;
import model.Receta;
import model.Usuario;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class UsuarioDAO extends AbstractEntityDAO<Usuario> {

//    public UsuarioDAO() {
//        super(Usuario.class);
//    }

    public Usuario findByNombreYContrasenia(String nombre, String contrasenia) {
        TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findByNombreYContrasenia", Usuario.class);
        query.setParameter("nombre", nombre);
        query.setParameter("contrasenia", contrasenia);
        List<Usuario> usuarios = query.getResultList();
        return usuarios.isEmpty() ? null : usuarios.get(0);
    }
    public Usuario findByNombre(String nombre) {
        TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findByNombre", Usuario.class);
        query.setParameter("nombre", nombre);
        List<Usuario> usuarios = query.getResultList();
        return usuarios.isEmpty() ? null : usuarios.get(0);
    }


    public List<Receta> findRecetasPorUsuario(Long usuarioId) {
        TypedQuery<Receta> query = em.createQuery(
                "SELECT r FROM Receta r WHERE r.usuario.id = :usuarioId", Receta.class);
        query.setParameter("usuarioId", usuarioId);
        return query.getResultList();
    }



    public Usuario findByIdWithRecetasFavoritas(Long id) {
        return em.createNamedQuery("Usuario.findByIdWithRecetasFavoritas", Usuario.class)
                .setParameter("id", id)
                .getSingleResult();
    }



    public Usuario findByTokenRecuperacion(String token) {
        TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.tokenRecuperacion = :token", Usuario.class);
        query.setParameter("token", token);
        List<Usuario> resultados = query.getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }



    @Override
    public PersistentEntity findById(Long id) throws Exception {
        return null;
    }
}