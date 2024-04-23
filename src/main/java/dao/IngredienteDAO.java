package dao;

import model.Ingrediente;
import model.PersistentEntity;
import model.Usuario;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class IngredienteDAO extends AbstractEntityDAO<Ingrediente> {



    public Ingrediente findByNombre(String nombre) {
        TypedQuery<Ingrediente> query = em.createNamedQuery("Ingrediente.findByNombre", Ingrediente.class);
        query.setParameter("nombre", nombre);
        List<Ingrediente> ingredientes = query.getResultList();
        return ingredientes.isEmpty() ? null : ingredientes.get(0);
    }





    @Override
    public PersistentEntity findById(Long id) throws Exception {
        return null;
    }
}
