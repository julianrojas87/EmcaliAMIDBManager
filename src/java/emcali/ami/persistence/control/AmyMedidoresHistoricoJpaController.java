/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emcali.ami.persistence.control;

import emcali.ami.persistence.control.exceptions.NonexistentEntityException;
import emcali.ami.persistence.control.exceptions.PreexistingEntityException;
import emcali.ami.persistence.control.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import emcali.ami.persistence.entity.AmyMedidores;
import emcali.ami.persistence.entity.AmyMedidoresHistorico;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class AmyMedidoresHistoricoJpaController implements Serializable {

    public AmyMedidoresHistoricoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AmyMedidoresHistorico amyMedidoresHistorico) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AmyMedidores fkAmyMedidores = amyMedidoresHistorico.getFkAmyMedidores();
            if (fkAmyMedidores != null) {
                fkAmyMedidores = em.getReference(fkAmyMedidores.getClass(), fkAmyMedidores.getIdMedidores());
                amyMedidoresHistorico.setFkAmyMedidores(fkAmyMedidores);
            }
            em.persist(amyMedidoresHistorico);
            if (fkAmyMedidores != null) {
                fkAmyMedidores.getAmyMedidoresHistoricoList().add(amyMedidoresHistorico);
                fkAmyMedidores = em.merge(fkAmyMedidores);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAmyMedidoresHistorico(amyMedidoresHistorico.getIdMedidoresHistorico()) != null) {
                throw new PreexistingEntityException("AmyMedidoresHistorico " + amyMedidoresHistorico + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AmyMedidoresHistorico amyMedidoresHistorico) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AmyMedidoresHistorico persistentAmyMedidoresHistorico = em.find(AmyMedidoresHistorico.class, amyMedidoresHistorico.getIdMedidoresHistorico());
            AmyMedidores fkAmyMedidoresOld = persistentAmyMedidoresHistorico.getFkAmyMedidores();
            AmyMedidores fkAmyMedidoresNew = amyMedidoresHistorico.getFkAmyMedidores();
            if (fkAmyMedidoresNew != null) {
                fkAmyMedidoresNew = em.getReference(fkAmyMedidoresNew.getClass(), fkAmyMedidoresNew.getIdMedidores());
                amyMedidoresHistorico.setFkAmyMedidores(fkAmyMedidoresNew);
            }
            amyMedidoresHistorico = em.merge(amyMedidoresHistorico);
            if (fkAmyMedidoresOld != null && !fkAmyMedidoresOld.equals(fkAmyMedidoresNew)) {
                fkAmyMedidoresOld.getAmyMedidoresHistoricoList().remove(amyMedidoresHistorico);
                fkAmyMedidoresOld = em.merge(fkAmyMedidoresOld);
            }
            if (fkAmyMedidoresNew != null && !fkAmyMedidoresNew.equals(fkAmyMedidoresOld)) {
                fkAmyMedidoresNew.getAmyMedidoresHistoricoList().add(amyMedidoresHistorico);
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
                Long id = amyMedidoresHistorico.getIdMedidoresHistorico();
                if (findAmyMedidoresHistorico(id) == null) {
                    throw new NonexistentEntityException("The amyMedidoresHistorico with id " + id + " no longer exists.");
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
            AmyMedidoresHistorico amyMedidoresHistorico;
            try {
                amyMedidoresHistorico = em.getReference(AmyMedidoresHistorico.class, id);
                amyMedidoresHistorico.getIdMedidoresHistorico();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The amyMedidoresHistorico with id " + id + " no longer exists.", enfe);
            }
            AmyMedidores fkAmyMedidores = amyMedidoresHistorico.getFkAmyMedidores();
            if (fkAmyMedidores != null) {
                fkAmyMedidores.getAmyMedidoresHistoricoList().remove(amyMedidoresHistorico);
                fkAmyMedidores = em.merge(fkAmyMedidores);
            }
            em.remove(amyMedidoresHistorico);
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

    public List<AmyMedidoresHistorico> findAmyMedidoresHistoricoEntities() {
        return findAmyMedidoresHistoricoEntities(true, -1, -1);
    }

    public List<AmyMedidoresHistorico> findAmyMedidoresHistoricoEntities(int maxResults, int firstResult) {
        return findAmyMedidoresHistoricoEntities(false, maxResults, firstResult);
    }

    private List<AmyMedidoresHistorico> findAmyMedidoresHistoricoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AmyMedidoresHistorico.class));
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

    public AmyMedidoresHistorico findAmyMedidoresHistorico(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AmyMedidoresHistorico.class, id);
        } finally {
            em.close();
        }
    }

    public int getAmyMedidoresHistoricoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AmyMedidoresHistorico> rt = cq.from(AmyMedidoresHistorico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
