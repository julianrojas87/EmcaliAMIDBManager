/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emcali.ami.persistence.control;

import emcali.ami.persistence.control.exceptions.NonexistentEntityException;
import emcali.ami.persistence.control.exceptions.PreexistingEntityException;
import emcali.ami.persistence.control.exceptions.RollbackFailureException;
import emcali.ami.persistence.entity.AtrFncsActvMedidores;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import emcali.ami.persistence.entity.AtributosFuncionMedidores;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class AtrFncsActvMedidoresJpaController implements Serializable {

    public AtrFncsActvMedidoresJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AtrFncsActvMedidores atrFncsActvMedidores) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AtributosFuncionMedidores fkAtrFncMedidores = atrFncsActvMedidores.getFkAtrFncMedidores();
            if (fkAtrFncMedidores != null) {
                fkAtrFncMedidores = em.getReference(fkAtrFncMedidores.getClass(), fkAtrFncMedidores.getIdFuncionMedidores());
                atrFncsActvMedidores.setFkAtrFncMedidores(fkAtrFncMedidores);
            }
            em.persist(atrFncsActvMedidores);
            if (fkAtrFncMedidores != null) {
                fkAtrFncMedidores.getAtrFncsActvMedidoresCollection().add(atrFncsActvMedidores);
                fkAtrFncMedidores = em.merge(fkAtrFncMedidores);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAtrFncsActvMedidores(atrFncsActvMedidores.getIdFuncionActivaMedidor()) != null) {
                throw new PreexistingEntityException("AtrFncsActvMedidores " + atrFncsActvMedidores + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AtrFncsActvMedidores atrFncsActvMedidores) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AtrFncsActvMedidores persistentAtrFncsActvMedidores = em.find(AtrFncsActvMedidores.class, atrFncsActvMedidores.getIdFuncionActivaMedidor());
            AtributosFuncionMedidores fkAtrFncMedidoresOld = persistentAtrFncsActvMedidores.getFkAtrFncMedidores();
            AtributosFuncionMedidores fkAtrFncMedidoresNew = atrFncsActvMedidores.getFkAtrFncMedidores();
            if (fkAtrFncMedidoresNew != null) {
                fkAtrFncMedidoresNew = em.getReference(fkAtrFncMedidoresNew.getClass(), fkAtrFncMedidoresNew.getIdFuncionMedidores());
                atrFncsActvMedidores.setFkAtrFncMedidores(fkAtrFncMedidoresNew);
            }
            atrFncsActvMedidores = em.merge(atrFncsActvMedidores);
            if (fkAtrFncMedidoresOld != null && !fkAtrFncMedidoresOld.equals(fkAtrFncMedidoresNew)) {
                fkAtrFncMedidoresOld.getAtrFncsActvMedidoresCollection().remove(atrFncsActvMedidores);
                fkAtrFncMedidoresOld = em.merge(fkAtrFncMedidoresOld);
            }
            if (fkAtrFncMedidoresNew != null && !fkAtrFncMedidoresNew.equals(fkAtrFncMedidoresOld)) {
                fkAtrFncMedidoresNew.getAtrFncsActvMedidoresCollection().add(atrFncsActvMedidores);
                fkAtrFncMedidoresNew = em.merge(fkAtrFncMedidoresNew);
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
                Long id = atrFncsActvMedidores.getIdFuncionActivaMedidor();
                if (findAtrFncsActvMedidores(id) == null) {
                    throw new NonexistentEntityException("The atrFncsActvMedidores with id " + id + " no longer exists.");
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
            AtrFncsActvMedidores atrFncsActvMedidores;
            try {
                atrFncsActvMedidores = em.getReference(AtrFncsActvMedidores.class, id);
                atrFncsActvMedidores.getIdFuncionActivaMedidor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The atrFncsActvMedidores with id " + id + " no longer exists.", enfe);
            }
            AtributosFuncionMedidores fkAtrFncMedidores = atrFncsActvMedidores.getFkAtrFncMedidores();
            if (fkAtrFncMedidores != null) {
                fkAtrFncMedidores.getAtrFncsActvMedidoresCollection().remove(atrFncsActvMedidores);
                fkAtrFncMedidores = em.merge(fkAtrFncMedidores);
            }
            em.remove(atrFncsActvMedidores);
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

    public List<AtrFncsActvMedidores> findAtrFncsActvMedidoresEntities() {
        return findAtrFncsActvMedidoresEntities(true, -1, -1);
    }

    public List<AtrFncsActvMedidores> findAtrFncsActvMedidoresEntities(int maxResults, int firstResult) {
        return findAtrFncsActvMedidoresEntities(false, maxResults, firstResult);
    }

    private List<AtrFncsActvMedidores> findAtrFncsActvMedidoresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AtrFncsActvMedidores.class));
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

    public AtrFncsActvMedidores findAtrFncsActvMedidores(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AtrFncsActvMedidores.class, id);
        } finally {
            em.close();
        }
    }

    public int getAtrFncsActvMedidoresCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AtrFncsActvMedidores> rt = cq.from(AtrFncsActvMedidores.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
