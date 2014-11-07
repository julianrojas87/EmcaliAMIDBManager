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
import emcali.ami.persistence.entity.AmyTipolecturas;
import emcali.ami.persistence.entity.ConsumosConsumos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class ConsumosConsumosJpaController implements Serializable {

    public ConsumosConsumosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ConsumosConsumos consumosConsumos) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AmyTipolecturas fkAmyTipolecturas = consumosConsumos.getFkAmyTipolecturas();
            if (fkAmyTipolecturas != null) {
                fkAmyTipolecturas = em.getReference(fkAmyTipolecturas.getClass(), fkAmyTipolecturas.getIdTipolectura());
                consumosConsumos.setFkAmyTipolecturas(fkAmyTipolecturas);
            }
            em.persist(consumosConsumos);
            if (fkAmyTipolecturas != null) {
                fkAmyTipolecturas.getConsumosConsumosList().add(consumosConsumos);
                fkAmyTipolecturas = em.merge(fkAmyTipolecturas);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findConsumosConsumos(consumosConsumos.getIdConsumo()) != null) {
                throw new PreexistingEntityException("ConsumosConsumos " + consumosConsumos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ConsumosConsumos consumosConsumos) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ConsumosConsumos persistentConsumosConsumos = em.find(ConsumosConsumos.class, consumosConsumos.getIdConsumo());
            AmyTipolecturas fkAmyTipolecturasOld = persistentConsumosConsumos.getFkAmyTipolecturas();
            AmyTipolecturas fkAmyTipolecturasNew = consumosConsumos.getFkAmyTipolecturas();
            if (fkAmyTipolecturasNew != null) {
                fkAmyTipolecturasNew = em.getReference(fkAmyTipolecturasNew.getClass(), fkAmyTipolecturasNew.getIdTipolectura());
                consumosConsumos.setFkAmyTipolecturas(fkAmyTipolecturasNew);
            }
            consumosConsumos = em.merge(consumosConsumos);
            if (fkAmyTipolecturasOld != null && !fkAmyTipolecturasOld.equals(fkAmyTipolecturasNew)) {
                fkAmyTipolecturasOld.getConsumosConsumosList().remove(consumosConsumos);
                fkAmyTipolecturasOld = em.merge(fkAmyTipolecturasOld);
            }
            if (fkAmyTipolecturasNew != null && !fkAmyTipolecturasNew.equals(fkAmyTipolecturasOld)) {
                fkAmyTipolecturasNew.getConsumosConsumosList().add(consumosConsumos);
                fkAmyTipolecturasNew = em.merge(fkAmyTipolecturasNew);
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
                Long id = consumosConsumos.getIdConsumo();
                if (findConsumosConsumos(id) == null) {
                    throw new NonexistentEntityException("The consumosConsumos with id " + id + " no longer exists.");
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
            ConsumosConsumos consumosConsumos;
            try {
                consumosConsumos = em.getReference(ConsumosConsumos.class, id);
                consumosConsumos.getIdConsumo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The consumosConsumos with id " + id + " no longer exists.", enfe);
            }
            AmyTipolecturas fkAmyTipolecturas = consumosConsumos.getFkAmyTipolecturas();
            if (fkAmyTipolecturas != null) {
                fkAmyTipolecturas.getConsumosConsumosList().remove(consumosConsumos);
                fkAmyTipolecturas = em.merge(fkAmyTipolecturas);
            }
            em.remove(consumosConsumos);
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

    public List<ConsumosConsumos> findConsumosConsumosEntities() {
        return findConsumosConsumosEntities(true, -1, -1);
    }

    public List<ConsumosConsumos> findConsumosConsumosEntities(int maxResults, int firstResult) {
        return findConsumosConsumosEntities(false, maxResults, firstResult);
    }

    private List<ConsumosConsumos> findConsumosConsumosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ConsumosConsumos.class));
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

    public ConsumosConsumos findConsumosConsumos(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ConsumosConsumos.class, id);
        } finally {
            em.close();
        }
    }

    public int getConsumosConsumosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ConsumosConsumos> rt = cq.from(ConsumosConsumos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
