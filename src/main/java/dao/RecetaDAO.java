package dao;

import model.PersistentEntity;
import model.Receta;
import model.Usuario;

import javax.ejb.Stateless;

@Stateless
public class RecetaDAO extends AbstractEntityDAO<Receta> {
    @Override
    public PersistentEntity findById(Long id) throws Exception {
        return null;
    }
}
