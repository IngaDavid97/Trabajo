/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.exceptions.NonexistentEntityException;

/**
 *
 * @author pc
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona idpersona = usuario.getIdpersona();
            if (idpersona != null) {
                idpersona = em.getReference(idpersona.getClass(), idpersona.getIdPersona());
                usuario.setIdpersona(idpersona);
            }
            em.persist(usuario);
            if (idpersona != null) {
                idpersona.getUsuarioList().add(usuario);
                idpersona = em.merge(idpersona);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdusuario());
            Persona idpersonaOld = persistentUsuario.getIdpersona();
            Persona idpersonaNew = usuario.getIdpersona();
            if (idpersonaNew != null) {
                idpersonaNew = em.getReference(idpersonaNew.getClass(), idpersonaNew.getIdPersona());
                usuario.setIdpersona(idpersonaNew);
            }
            usuario = em.merge(usuario);
            if (idpersonaOld != null && !idpersonaOld.equals(idpersonaNew)) {
                idpersonaOld.getUsuarioList().remove(usuario);
                idpersonaOld = em.merge(idpersonaOld);
            }
            if (idpersonaNew != null && !idpersonaNew.equals(idpersonaOld)) {
                idpersonaNew.getUsuarioList().add(usuario);
                idpersonaNew = em.merge(idpersonaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getIdusuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdusuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            Persona idpersona = usuario.getIdpersona();
            if (idpersona != null) {
                idpersona.getUsuarioList().remove(usuario);
                idpersona = em.merge(idpersona);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Usuario buscarUsuario(String usuario, String clave){
        Usuario u=null;
        
        for(Usuario user: findUsuarioEntities()){
            if(user.getUsuario().equals(usuario) && user.getClave().equals(clave)){
                u=user;
            }
        }
        return u;
    }
    
     public List<Usuario> buscarUsuario(String usuario){
        System.out.println(usuario);
        EntityManager em= getEntityManager();
        try{
            TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findByUsuario", Usuario.class);
            query.setParameter("usuario", usuario);
            List<Usuario> list = query.getResultList();
            return list;
        }finally{
            em.close();
        }
    }
}
