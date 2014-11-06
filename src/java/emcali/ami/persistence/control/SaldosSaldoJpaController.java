/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emcali.ami.persistence.control;

import emcali.ami.persistence.control.exceptions.NonexistentEntityException;
import emcali.ami.persistence.control.exceptions.PreexistingEntityException;
import emcali.ami.persistence.control.exceptions.RollbackFailureException;
import emcali.ami.persistence.entity.SaldosSaldo;
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
public class SaldosSaldoJpaController implements Serializable {

    public SaldosSaldoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SaldosSaldo saldosSaldo) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(saldosSaldo);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findSaldosSaldo(saldosSaldo.getIdSaldo()) != null) {
                throw new PreexistingEntityException("SaldosSaldo " + saldosSaldo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SaldosSaldo saldosSaldo) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            saldosSaldo = em.merge(saldosSaldo);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = saldosSaldo.getIdSaldo();
                if (findSaldosSaldo(id) == null) {
                    throw new NonexistentEntityException("The saldosSaldo with id " + id + " no longer exists.");
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
            SaldosSaldo saldosSaldo;
            try {
                saldosSaldo = em.getReference(SaldosSaldo.class, id);
                saldosSaldo.getIdSaldo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The saldosSaldo with id " + id + " no longer exists.", enfe);
            }
            em.remove(saldosSaldo);
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

    public List<SaldosSaldo> findSaldosSaldoEntities() {
        return findSaldosSaldoEntities(true, -1, -1);
    }

    public List<SaldosSaldo> findSaldosSaldoEntities(int maxResults, int firstResult) {
        return findSaldosSaldoEntities(false, maxResults, firstResult);
    }

    private List<SaldosSaldo> findSaldosSaldoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SaldosSaldo.class));
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

    public SaldosSaldo findSaldosSaldo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SaldosSaldo.class, id);
        } finally {
            em.close();
        }
    }

    public int getSaldosSaldoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SaldosSaldo> rt = cq.from(SaldosSaldo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
