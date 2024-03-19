package dao;

import model.Ingrediente;
import model.PersistentEntity;
import model.Usuario;

import javax.ejb.Stateless;

@Stateless
public class IngredienteDAO extends AbstractEntityDAO<Ingrediente> {
    @Override
    public PersistentEntity findById(Long id) throws Exception {
        return null;
    }
}
