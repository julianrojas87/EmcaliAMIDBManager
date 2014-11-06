/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emcali.ami.persistence.control;

import emcali.ami.persistence.control.exceptions.NonexistentEntityException;
import emcali.ami.persistence.control.exceptions.PreexistingEntityException;
import emcali.ami.persistence.control.exceptions.RollbackFailureException;
import emcali.ami.persistence.entity.AmyEventos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import emcali.ami.persistence.entity.AmyTipoEventos;
import emcali.ami.persistence.entity.AmyMedidores;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class AmyEventosJpaController implements Serializable {

    public AmyEventosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AmyEventos amyEventos) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AmyTipoEventos fkAmyTipoEventos = amyEventos.getFkAmyTipoEventos();
            if (fkAmyTipoEventos != null) {
                fkAmyTipoEventos = em.getReference(fkAmyTipoEventos.getClass(), fkAmyTipoEventos.getIdTipoEvento());
                amyEventos.setFkAmyTipoEventos(fkAmyTipoEventos);
            }
            AmyMedidores fkAmyMedidores = amyEventos.getFkAmyMedidores();
            if (fkAmyMedidores != null) {
                fkAmyMedidores = em.getReference(fkAmyMedidores.getClass(), fkAmyMedidores.getIdMedidores());
                amyEventos.setFkAmyMedidores(fkAmyMedidores);
            }
            em.persist(amyEventos);
            if (fkAmyTipoEventos != null) {
                fkAmyTipoEventos.getAmyEventosCollection().add(amyEventos);
                fkAmyTipoEventos = em.merge(fkAmyTipoEventos);
            }
            if (fkAmyMedidores != null) {
                fkAmyMedidores.getAmyEventosCollection().add(amyEventos);
                fkAmyMedidores = em.merge(fkAmyMedidores);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAmyEventos(amyEventos.getIdEvento()) != null) {
                throw new PreexistingEntityException("AmyEventos " + amyEventos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AmyEventos amyEventos) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AmyEventos persistentAmyEventos = em.find(AmyEventos.class, amyEventos.getIdEvento());
            AmyTipoEventos fkAmyTipoEventosOld = persistentAmyEventos.getFkAmyTipoEventos();
            AmyTipoEventos fkAmyTipoEventosNew = amyEventos.getFkAmyTipoEventos();
            AmyMedidores fkAmyMedidoresOld = persistentAmyEventos.getFkAmyMedidores();
            AmyMedidores fkAmyMedidoresNew = amyEventos.getFkAmyMedidores();
            if (fkAmyTipoEventosNew != null) {
                fkAmyTipoEventosNew = em.getReference(fkAmyTipoEventosNew.getClass(), fkAmyTipoEventosNew.getIdTipoEvento());
                amyEventos.setFkAmyTipoEventos(fkAmyTipoEventosNew);
            }
            if (fkAmyMedidoresNew != null) {
                fkAmyMedidoresNew = em.getReference(fkAmyMedidoresNew.getClass(), fkAmyMedidoresNew.getIdMedidores());
                amyEventos.setFkAmyMedidores(fkAmyMedidoresNew);
            }
            amyEventos = em.merge(amyEventos);
            if (fkAmyTipoEventosOld != null && !fkAmyTipoEventosOld.equals(fkAmyTipoEventosNew)) {
                fkAmyTipoEventosOld.getAmyEventosCollection().remove(amyEventos);
                fkAmyTipoEventosOld = em.merge(fkAmyTipoEventosOld);
            }
            if (fkAmyTipoEventosNew != null && !fkAmyTipoEventosNew.equals(fkAmyTipoEventosOld)) {
                fkAmyTipoEventosNew.getAmyEventosCollection().add(amyEventos);
                fkAmyTipoEventosNew = em.merge(fkAmyTipoEventosNew);
            }
            if (fkAmyMedidoresOld != null && !fkAmyMedidoresOld.equals(fkAmyMedidoresNew)) {
                fkAmyMedidoresOld.getAmyEventosCollection().remove(amyEventos);
                fkAmyMedidoresOld = em.merge(fkAmyMedidoresOld);
            }
            if (fkAmyMedidoresNew != null && !fkAmyMedidoresNew.equals(fkAmyMedidoresOld)) {
                fkAmyMedidoresNew.getAmyEventosCollection().add(amyEventos);
                fkAmyMedidoresNew = em.merge(fkAmyMedidoresNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = amyEventos.getIdEvento();
                if (findAmyEventos(id) == null) {
                    throw new NonexistentEntityException("The amyEventos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AmyEventos amyEventos;
            try {
                amyEventos = em.getReference(AmyEventos.class, id);
                amyEventos.getIdEvento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The amyEventos with id " + id + " no longer exists.", enfe);
            }
            AmyTipoEventos fkAmyTipoEventos = amyEventos.getFkAmyTipoEventos();
            if (fkAmyTipoEventos != null) {
                fkAmyTipoEventos.getAmyEventosCollection().remove(amyEventos);
                fkAmyTipoEventos = em.merge(fkAmyTipoEventos);
            }
            AmyMedidores fkAmyMedidores = amyEventos.getFkAmyMedidores();
            if (fkAmyMedidores != null) {
                fkAmyMedidores.getAmyEventosCollection().remove(amyEventos);
                fkAmyMedidores = em.merge(fkAmyMedidores);
            }
            em.remove(amyEventos);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AmyEventos> findAmyEventosEntities() {
        return findAmyEventosEntities(true, -1, -1);
    }

    public List<AmyEventos> findAmyEventosEntities(int maxResults, int firstResult) {
        return findAmyEventosEntities(false, maxResults, firstResult);
    }

    private List<AmyEventos> findAmyEventosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AmyEventos.class));
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

    public AmyEventos findAmyEventos(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AmyEventos.class, id);
        } finally {
            em.close();
        }
    }

    public int getAmyEventosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AmyEventos> rt = cq.from(AmyEventos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
