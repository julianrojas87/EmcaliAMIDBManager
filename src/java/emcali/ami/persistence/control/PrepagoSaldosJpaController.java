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
import emcali.ami.persistence.entity.PrepagoSaldos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class PrepagoSaldosJpaController implements Serializable {

    public PrepagoSaldosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PrepagoSaldos prepagoSaldos) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AmyMedidores fkAmyMedidores = prepagoSaldos.getFkAmyMedidores();
            if (fkAmyMedidores != null) {
                fkAmyMedidores = em.getReference(fkAmyMedidores.getClass(), fkAmyMedidores.getIdMedidores());
                prepagoSaldos.setFkAmyMedidores(fkAmyMedidores);
            }
            em.persist(prepagoSaldos);
            if (fkAmyMedidores != null) {
                fkAmyMedidores.getPrepagoSaldosCollection().add(prepagoSaldos);
                fkAmyMedidores = em.merge(fkAmyMedidores);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPrepagoSaldos(prepagoSaldos.getIdSaldos()) != null) {
                throw new PreexistingEntityException("PrepagoSaldos " + prepagoSaldos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PrepagoSaldos prepagoSaldos) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PrepagoSaldos persistentPrepagoSaldos = em.find(PrepagoSaldos.class, prepagoSaldos.getIdSaldos());
            AmyMedidores fkAmyMedidoresOld = persistentPrepagoSaldos.getFkAmyMedidores();
            AmyMedidores fkAmyMedidoresNew = prepagoSaldos.getFkAmyMedidores();
            if (fkAmyMedidoresNew != null) {
                fkAmyMedidoresNew = em.getReference(fkAmyMedidoresNew.getClass(), fkAmyMedidoresNew.getIdMedidores());
                prepagoSaldos.setFkAmyMedidores(fkAmyMedidoresNew);
            }
            prepagoSaldos = em.merge(prepagoSaldos);
            if (fkAmyMedidoresOld != null && !fkAmyMedidoresOld.equals(fkAmyMedidoresNew)) {
                fkAmyMedidoresOld.getPrepagoSaldosCollection().remove(prepagoSaldos);
                fkAmyMedidoresOld = em.merge(fkAmyMedidoresOld);
            }
            if (fkAmyMedidoresNew != null && !fkAmyMedidoresNew.equals(fkAmyMedidoresOld)) {
                fkAmyMedidoresNew.getPrepagoSaldosCollection().add(prepagoSaldos);
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
                Long id = prepagoSaldos.getIdSaldos();
                if (findPrepagoSaldos(id) == null) {
                    throw new NonexistentEntityException("The prepagoSaldos with id " + id + " no longer exists.");
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
            PrepagoSaldos prepagoSaldos;
            try {
                prepagoSaldos = em.getReference(PrepagoSaldos.class, id);
                prepagoSaldos.getIdSaldos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The prepagoSaldos with id " + id + " no longer exists.", enfe);
            }
            AmyMedidores fkAmyMedidores = prepagoSaldos.getFkAmyMedidores();
            if (fkAmyMedidores != null) {
                fkAmyMedidores.getPrepagoSaldosCollection().remove(prepagoSaldos);
                fkAmyMedidores = em.merge(fkAmyMedidores);
            }
            em.remove(prepagoSaldos);
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

    public List<PrepagoSaldos> findPrepagoSaldosEntities() {
        return findPrepagoSaldosEntities(true, -1, -1);
    }

    public List<PrepagoSaldos> findPrepagoSaldosEntities(int maxResults, int firstResult) {
        return findPrepagoSaldosEntities(false, maxResults, firstResult);
    }

    private List<PrepagoSaldos> findPrepagoSaldosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PrepagoSaldos.class));
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

    public PrepagoSaldos findPrepagoSaldos(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PrepagoSaldos.class, id);
        } finally {
            em.close();
        }
    }

    public int getPrepagoSaldosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PrepagoSaldos> rt = cq.from(PrepagoSaldos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
