package dao;

import model.PersistentEntity;
import model.Receta;
import model.RecetaIngredienteCantidad;
import model.Usuario;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class RecetaDAO extends AbstractEntityDAO<Receta> {

    // Método para encontrar un rango específico de recetas
    public List<Receta> findRange(int inicio, int cantidad) {
        TypedQuery<Receta> query = em.createQuery("SELECT r FROM Receta r ORDER BY r.id", Receta.class)
                .setFirstResult(inicio) // Establece el primer resultado del rango
                .setMaxResults(cantidad); // Establece la cantidad máxima de resultados a obtener
        return query.getResultList();
    }


    public List<String> findCantidadIngredientes(Long recetaId) {
        TypedQuery<String> query = em.createNamedQuery("RecetaIngredienteCantidad.findCantidadIngredientes", String.class);
        query.setParameter("recetaId", recetaId);
        return query.getResultList();
    }


    @Override
    public PersistentEntity findById(Long id) throws Exception {
        return null;
    }
}
