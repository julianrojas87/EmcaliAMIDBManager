/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emcali.ami.persistence.control;

import emcali.ami.persistence.control.exceptions.NonexistentEntityException;
import emcali.ami.persistence.control.exceptions.PreexistingEntityException;
import emcali.ami.persistence.control.exceptions.RollbackFailureException;
import emcali.ami.persistence.entity.AmyTipoConsumo;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class AmyTipoConsumoJpaController implements Serializable {

    public AmyTipoConsumoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AmyTipoConsumo amyTipoConsumo) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(amyTipoConsumo);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAmyTipoConsumo(amyTipoConsumo.getIdTipoConsumo()) != null) {
                throw new PreexistingEntityException("AmyTipoConsumo " + amyTipoConsumo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AmyTipoConsumo amyTipoConsumo) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            amyTipoConsumo = em.merge(amyTipoConsumo);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = amyTipoConsumo.getIdTipoConsumo();
                if (findAmyTipoConsumo(id) == null) {
                    throw new NonexistentEntityException("The amyTipoConsumo with id " + id + " no longer exists.");
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
            AmyTipoConsumo amyTipoConsumo;
            try {
                amyTipoConsumo = em.getReference(AmyTipoConsumo.class, id);
                amyTipoConsumo.getIdTipoConsumo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The amyTipoConsumo with id " + id + " no longer exists.", enfe);
            }
            em.remove(amyTipoConsumo);
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

    public List<AmyTipoConsumo> findAmyTipoConsumoEntities() {
        return findAmyTipoConsumoEntities(true, -1, -1);
    }

    public List<AmyTipoConsumo> findAmyTipoConsumoEntities(int maxResults, int firstResult) {
        return findAmyTipoConsumoEntities(false, maxResults, firstResult);
    }

    private List<AmyTipoConsumo> findAmyTipoConsumoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AmyTipoConsumo.class));
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

    public AmyTipoConsumo findAmyTipoConsumo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AmyTipoConsumo.class, id);
        } finally {
            em.close();
        }
    }

    public int getAmyTipoConsumoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AmyTipoConsumo> rt = cq.from(AmyTipoConsumo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
