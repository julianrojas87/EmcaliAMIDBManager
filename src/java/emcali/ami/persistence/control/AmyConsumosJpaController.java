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
import emcali.ami.persistence.entity.AmyMedidores;
import emcali.ami.persistence.entity.AmyInterval;
import emcali.ami.persistence.entity.AmyCanal;
import emcali.ami.persistence.entity.AmyConsumos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class AmyConsumosJpaController implements Serializable {

    public AmyConsumosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AmyConsumos amyConsumos) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AmyTipolecturas fkAmyTipolecturas = amyConsumos.getFkAmyTipolecturas();
            if (fkAmyTipolecturas != null) {
                fkAmyTipolecturas = em.getReference(fkAmyTipolecturas.getClass(), fkAmyTipolecturas.getIdTipolectura());
                amyConsumos.setFkAmyTipolecturas(fkAmyTipolecturas);
            }
            AmyMedidores fkAmyMedidores = amyConsumos.getFkAmyMedidores();
            if (fkAmyMedidores != null) {
                fkAmyMedidores = em.getReference(fkAmyMedidores.getClass(), fkAmyMedidores.getIdMedidores());
                amyConsumos.setFkAmyMedidores(fkAmyMedidores);
            }
            AmyInterval fkAmyInterval = amyConsumos.getFkAmyInterval();
            if (fkAmyInterval != null) {
                fkAmyInterval = em.getReference(fkAmyInterval.getClass(), fkAmyInterval.getIdTiempo());
                amyConsumos.setFkAmyInterval(fkAmyInterval);
            }
            AmyCanal fkAmyCanal = amyConsumos.getFkAmyCanal();
            if (fkAmyCanal != null) {
                fkAmyCanal = em.getReference(fkAmyCanal.getClass(), fkAmyCanal.getIdCanal());
                amyConsumos.setFkAmyCanal(fkAmyCanal);
            }
            em.persist(amyConsumos);
            if (fkAmyTipolecturas != null) {
                fkAmyTipolecturas.getAmyConsumosList().add(amyConsumos);
                fkAmyTipolecturas = em.merge(fkAmyTipolecturas);
            }
            if (fkAmyMedidores != null) {
                fkAmyMedidores.getAmyConsumosList().add(amyConsumos);
                fkAmyMedidores = em.merge(fkAmyMedidores);
            }
            if (fkAmyInterval != null) {
                fkAmyInterval.getAmyConsumosList().add(amyConsumos);
                fkAmyInterval = em.merge(fkAmyInterval);
            }
            if (fkAmyCanal != null) {
                fkAmyCanal.getAmyConsumosList().add(amyConsumos);
                fkAmyCanal = em.merge(fkAmyCanal);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAmyConsumos(amyConsumos.getIdConsumo()) != null) {
                throw new PreexistingEntityException("AmyConsumos " + amyConsumos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AmyConsumos amyConsumos) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AmyConsumos persistentAmyConsumos = em.find(AmyConsumos.class, amyConsumos.getIdConsumo());
            AmyTipolecturas fkAmyTipolecturasOld = persistentAmyConsumos.getFkAmyTipolecturas();
            AmyTipolecturas fkAmyTipolecturasNew = amyConsumos.getFkAmyTipolecturas();
            AmyMedidores fkAmyMedidoresOld = persistentAmyConsumos.getFkAmyMedidores();
            AmyMedidores fkAmyMedidoresNew = amyConsumos.getFkAmyMedidores();
            AmyInterval fkAmyIntervalOld = persistentAmyConsumos.getFkAmyInterval();
            AmyInterval fkAmyIntervalNew = amyConsumos.getFkAmyInterval();
            AmyCanal fkAmyCanalOld = persistentAmyConsumos.getFkAmyCanal();
            AmyCanal fkAmyCanalNew = amyConsumos.getFkAmyCanal();
            if (fkAmyTipolecturasNew != null) {
                fkAmyTipolecturasNew = em.getReference(fkAmyTipolecturasNew.getClass(), fkAmyTipolecturasNew.getIdTipolectura());
                amyConsumos.setFkAmyTipolecturas(fkAmyTipolecturasNew);
            }
            if (fkAmyMedidoresNew != null) {
                fkAmyMedidoresNew = em.getReference(fkAmyMedidoresNew.getClass(), fkAmyMedidoresNew.getIdMedidores());
                amyConsumos.setFkAmyMedidores(fkAmyMedidoresNew);
            }
            if (fkAmyIntervalNew != null) {
                fkAmyIntervalNew = em.getReference(fkAmyIntervalNew.getClass(), fkAmyIntervalNew.getIdTiempo());
                amyConsumos.setFkAmyInterval(fkAmyIntervalNew);
            }
            if (fkAmyCanalNew != null) {
                fkAmyCanalNew = em.getReference(fkAmyCanalNew.getClass(), fkAmyCanalNew.getIdCanal());
                amyConsumos.setFkAmyCanal(fkAmyCanalNew);
            }
            amyConsumos = em.merge(amyConsumos);
            if (fkAmyTipolecturasOld != null && !fkAmyTipolecturasOld.equals(fkAmyTipolecturasNew)) {
                fkAmyTipolecturasOld.getAmyConsumosList().remove(amyConsumos);
                fkAmyTipolecturasOld = em.merge(fkAmyTipolecturasOld);
            }
            if (fkAmyTipolecturasNew != null && !fkAmyTipolecturasNew.equals(fkAmyTipolecturasOld)) {
                fkAmyTipolecturasNew.getAmyConsumosList().add(amyConsumos);
                fkAmyTipolecturasNew = em.merge(fkAmyTipolecturasNew);
            }
            if (fkAmyMedidoresOld != null && !fkAmyMedidoresOld.equals(fkAmyMedidoresNew)) {
                fkAmyMedidoresOld.getAmyConsumosList().remove(amyConsumos);
                fkAmyMedidoresOld = em.merge(fkAmyMedidoresOld);
            }
            if (fkAmyMedidoresNew != null && !fkAmyMedidoresNew.equals(fkAmyMedidoresOld)) {
                fkAmyMedidoresNew.getAmyConsumosList().add(amyConsumos);
                fkAmyMedidoresNew = em.merge(fkAmyMedidoresNew);
            }
            if (fkAmyIntervalOld != null && !fkAmyIntervalOld.equals(fkAmyIntervalNew)) {
                fkAmyIntervalOld.getAmyConsumosList().remove(amyConsumos);
                fkAmyIntervalOld = em.merge(fkAmyIntervalOld);
            }
            if (fkAmyIntervalNew != null && !fkAmyIntervalNew.equals(fkAmyIntervalOld)) {
                fkAmyIntervalNew.getAmyConsumosList().add(amyConsumos);
                fkAmyIntervalNew = em.merge(fkAmyIntervalNew);
            }
            if (fkAmyCanalOld != null && !fkAmyCanalOld.equals(fkAmyCanalNew)) {
                fkAmyCanalOld.getAmyConsumosList().remove(amyConsumos);
                fkAmyCanalOld = em.merge(fkAmyCanalOld);
            }
            if (fkAmyCanalNew != null && !fkAmyCanalNew.equals(fkAmyCanalOld)) {
                fkAmyCanalNew.getAmyConsumosList().add(amyConsumos);
                fkAmyCanalNew = em.merge(fkAmyCanalNew);
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
                Long id = amyConsumos.getIdConsumo();
                if (findAmyConsumos(id) == null) {
                    throw new NonexistentEntityException("The amyConsumos with id " + id + " no longer exists.");
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
            AmyConsumos amyConsumos;
            try {
                amyConsumos = em.getReference(AmyConsumos.class, id);
                amyConsumos.getIdConsumo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The amyConsumos with id " + id + " no longer exists.", enfe);
            }
            AmyTipolecturas fkAmyTipolecturas = amyConsumos.getFkAmyTipolecturas();
            if (fkAmyTipolecturas != null) {
                fkAmyTipolecturas.getAmyConsumosList().remove(amyConsumos);
                fkAmyTipolecturas = em.merge(fkAmyTipolecturas);
            }
            AmyMedidores fkAmyMedidores = amyConsumos.getFkAmyMedidores();
            if (fkAmyMedidores != null) {
                fkAmyMedidores.getAmyConsumosList().remove(amyConsumos);
                fkAmyMedidores = em.merge(fkAmyMedidores);
            }
            AmyInterval fkAmyInterval = amyConsumos.getFkAmyInterval();
            if (fkAmyInterval != null) {
                fkAmyInterval.getAmyConsumosList().remove(amyConsumos);
                fkAmyInterval = em.merge(fkAmyInterval);
            }
            AmyCanal fkAmyCanal = amyConsumos.getFkAmyCanal();
            if (fkAmyCanal != null) {
                fkAmyCanal.getAmyConsumosList().remove(amyConsumos);
                fkAmyCanal = em.merge(fkAmyCanal);
            }
            em.remove(amyConsumos);
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

    public List<AmyConsumos> findAmyConsumosEntities() {
        return findAmyConsumosEntities(true, -1, -1);
    }

    public List<AmyConsumos> findAmyConsumosEntities(int maxResults, int firstResult) {
        return findAmyConsumosEntities(false, maxResults, firstResult);
    }

    private List<AmyConsumos> findAmyConsumosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AmyConsumos.class));
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

    public AmyConsumos findAmyConsumos(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AmyConsumos.class, id);
        } finally {
            em.close();
        }
    }

    public int getAmyConsumosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AmyConsumos> rt = cq.from(AmyConsumos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
