/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emcali.ami.persistence.control;

import emcali.ami.persistence.control.exceptions.IllegalOrphanException;
import emcali.ami.persistence.control.exceptions.NonexistentEntityException;
import emcali.ami.persistence.control.exceptions.PreexistingEntityException;
import emcali.ami.persistence.control.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import emcali.ami.persistence.entity.AmyLecturas;
import java.util.ArrayList;
import java.util.List;
import emcali.ami.persistence.entity.AmyConsumos;
import emcali.ami.persistence.entity.AmyInterval;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class AmyIntervalJpaController implements Serializable {

    public AmyIntervalJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AmyInterval amyInterval) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (amyInterval.getAmyLecturasList() == null) {
            amyInterval.setAmyLecturasList(new ArrayList<AmyLecturas>());
        }
        if (amyInterval.getAmyConsumosList() == null) {
            amyInterval.setAmyConsumosList(new ArrayList<AmyConsumos>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<AmyLecturas> attachedAmyLecturasList = new ArrayList<AmyLecturas>();
            for (AmyLecturas amyLecturasListAmyLecturasToAttach : amyInterval.getAmyLecturasList()) {
                amyLecturasListAmyLecturasToAttach = em.getReference(amyLecturasListAmyLecturasToAttach.getClass(), amyLecturasListAmyLecturasToAttach.getIdLecturas());
                attachedAmyLecturasList.add(amyLecturasListAmyLecturasToAttach);
            }
            amyInterval.setAmyLecturasList(attachedAmyLecturasList);
            List<AmyConsumos> attachedAmyConsumosList = new ArrayList<AmyConsumos>();
            for (AmyConsumos amyConsumosListAmyConsumosToAttach : amyInterval.getAmyConsumosList()) {
                amyConsumosListAmyConsumosToAttach = em.getReference(amyConsumosListAmyConsumosToAttach.getClass(), amyConsumosListAmyConsumosToAttach.getIdConsumo());
                attachedAmyConsumosList.add(amyConsumosListAmyConsumosToAttach);
            }
            amyInterval.setAmyConsumosList(attachedAmyConsumosList);
            em.persist(amyInterval);
            for (AmyLecturas amyLecturasListAmyLecturas : amyInterval.getAmyLecturasList()) {
                AmyInterval oldFkAmyIntervalOfAmyLecturasListAmyLecturas = amyLecturasListAmyLecturas.getFkAmyInterval();
                amyLecturasListAmyLecturas.setFkAmyInterval(amyInterval);
                amyLecturasListAmyLecturas = em.merge(amyLecturasListAmyLecturas);
                if (oldFkAmyIntervalOfAmyLecturasListAmyLecturas != null) {
                    oldFkAmyIntervalOfAmyLecturasListAmyLecturas.getAmyLecturasList().remove(amyLecturasListAmyLecturas);
                    oldFkAmyIntervalOfAmyLecturasListAmyLecturas = em.merge(oldFkAmyIntervalOfAmyLecturasListAmyLecturas);
                }
            }
            for (AmyConsumos amyConsumosListAmyConsumos : amyInterval.getAmyConsumosList()) {
                AmyInterval oldFkAmyIntervalOfAmyConsumosListAmyConsumos = amyConsumosListAmyConsumos.getFkAmyInterval();
                amyConsumosListAmyConsumos.setFkAmyInterval(amyInterval);
                amyConsumosListAmyConsumos = em.merge(amyConsumosListAmyConsumos);
                if (oldFkAmyIntervalOfAmyConsumosListAmyConsumos != null) {
                    oldFkAmyIntervalOfAmyConsumosListAmyConsumos.getAmyConsumosList().remove(amyConsumosListAmyConsumos);
                    oldFkAmyIntervalOfAmyConsumosListAmyConsumos = em.merge(oldFkAmyIntervalOfAmyConsumosListAmyConsumos);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAmyInterval(amyInterval.getIdTiempo()) != null) {
                throw new PreexistingEntityException("AmyInterval " + amyInterval + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AmyInterval amyInterval) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AmyInterval persistentAmyInterval = em.find(AmyInterval.class, amyInterval.getIdTiempo());
            List<AmyLecturas> amyLecturasListOld = persistentAmyInterval.getAmyLecturasList();
            List<AmyLecturas> amyLecturasListNew = amyInterval.getAmyLecturasList();
            List<AmyConsumos> amyConsumosListOld = persistentAmyInterval.getAmyConsumosList();
            List<AmyConsumos> amyConsumosListNew = amyInterval.getAmyConsumosList();
            List<String> illegalOrphanMessages = null;
            for (AmyLecturas amyLecturasListOldAmyLecturas : amyLecturasListOld) {
                if (!amyLecturasListNew.contains(amyLecturasListOldAmyLecturas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyLecturas " + amyLecturasListOldAmyLecturas + " since its fkAmyInterval field is not nullable.");
                }
            }
            for (AmyConsumos amyConsumosListOldAmyConsumos : amyConsumosListOld) {
                if (!amyConsumosListNew.contains(amyConsumosListOldAmyConsumos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyConsumos " + amyConsumosListOldAmyConsumos + " since its fkAmyInterval field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<AmyLecturas> attachedAmyLecturasListNew = new ArrayList<AmyLecturas>();
            for (AmyLecturas amyLecturasListNewAmyLecturasToAttach : amyLecturasListNew) {
                amyLecturasListNewAmyLecturasToAttach = em.getReference(amyLecturasListNewAmyLecturasToAttach.getClass(), amyLecturasListNewAmyLecturasToAttach.getIdLecturas());
                attachedAmyLecturasListNew.add(amyLecturasListNewAmyLecturasToAttach);
            }
            amyLecturasListNew = attachedAmyLecturasListNew;
            amyInterval.setAmyLecturasList(amyLecturasListNew);
            List<AmyConsumos> attachedAmyConsumosListNew = new ArrayList<AmyConsumos>();
            for (AmyConsumos amyConsumosListNewAmyConsumosToAttach : amyConsumosListNew) {
                amyConsumosListNewAmyConsumosToAttach = em.getReference(amyConsumosListNewAmyConsumosToAttach.getClass(), amyConsumosListNewAmyConsumosToAttach.getIdConsumo());
                attachedAmyConsumosListNew.add(amyConsumosListNewAmyConsumosToAttach);
            }
            amyConsumosListNew = attachedAmyConsumosListNew;
            amyInterval.setAmyConsumosList(amyConsumosListNew);
            amyInterval = em.merge(amyInterval);
            for (AmyLecturas amyLecturasListNewAmyLecturas : amyLecturasListNew) {
                if (!amyLecturasListOld.contains(amyLecturasListNewAmyLecturas)) {
                    AmyInterval oldFkAmyIntervalOfAmyLecturasListNewAmyLecturas = amyLecturasListNewAmyLecturas.getFkAmyInterval();
                    amyLecturasListNewAmyLecturas.setFkAmyInterval(amyInterval);
                    amyLecturasListNewAmyLecturas = em.merge(amyLecturasListNewAmyLecturas);
                    if (oldFkAmyIntervalOfAmyLecturasListNewAmyLecturas != null && !oldFkAmyIntervalOfAmyLecturasListNewAmyLecturas.equals(amyInterval)) {
                        oldFkAmyIntervalOfAmyLecturasListNewAmyLecturas.getAmyLecturasList().remove(amyLecturasListNewAmyLecturas);
                        oldFkAmyIntervalOfAmyLecturasListNewAmyLecturas = em.merge(oldFkAmyIntervalOfAmyLecturasListNewAmyLecturas);
                    }
                }
            }
            for (AmyConsumos amyConsumosListNewAmyConsumos : amyConsumosListNew) {
                if (!amyConsumosListOld.contains(amyConsumosListNewAmyConsumos)) {
                    AmyInterval oldFkAmyIntervalOfAmyConsumosListNewAmyConsumos = amyConsumosListNewAmyConsumos.getFkAmyInterval();
                    amyConsumosListNewAmyConsumos.setFkAmyInterval(amyInterval);
                    amyConsumosListNewAmyConsumos = em.merge(amyConsumosListNewAmyConsumos);
                    if (oldFkAmyIntervalOfAmyConsumosListNewAmyConsumos != null && !oldFkAmyIntervalOfAmyConsumosListNewAmyConsumos.equals(amyInterval)) {
                        oldFkAmyIntervalOfAmyConsumosListNewAmyConsumos.getAmyConsumosList().remove(amyConsumosListNewAmyConsumos);
                        oldFkAmyIntervalOfAmyConsumosListNewAmyConsumos = em.merge(oldFkAmyIntervalOfAmyConsumosListNewAmyConsumos);
                    }
                }
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
                Long id = amyInterval.getIdTiempo();
                if (findAmyInterval(id) == null) {
                    throw new NonexistentEntityException("The amyInterval with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AmyInterval amyInterval;
            try {
                amyInterval = em.getReference(AmyInterval.class, id);
                amyInterval.getIdTiempo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The amyInterval with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<AmyLecturas> amyLecturasListOrphanCheck = amyInterval.getAmyLecturasList();
            for (AmyLecturas amyLecturasListOrphanCheckAmyLecturas : amyLecturasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyInterval (" + amyInterval + ") cannot be destroyed since the AmyLecturas " + amyLecturasListOrphanCheckAmyLecturas + " in its amyLecturasList field has a non-nullable fkAmyInterval field.");
            }
            List<AmyConsumos> amyConsumosListOrphanCheck = amyInterval.getAmyConsumosList();
            for (AmyConsumos amyConsumosListOrphanCheckAmyConsumos : amyConsumosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyInterval (" + amyInterval + ") cannot be destroyed since the AmyConsumos " + amyConsumosListOrphanCheckAmyConsumos + " in its amyConsumosList field has a non-nullable fkAmyInterval field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(amyInterval);
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

    public List<AmyInterval> findAmyIntervalEntities() {
        return findAmyIntervalEntities(true, -1, -1);
    }

    public List<AmyInterval> findAmyIntervalEntities(int maxResults, int firstResult) {
        return findAmyIntervalEntities(false, maxResults, firstResult);
    }

    private List<AmyInterval> findAmyIntervalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AmyInterval.class));
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

    public AmyInterval findAmyInterval(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AmyInterval.class, id);
        } finally {
            em.close();
        }
    }

    public int getAmyIntervalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AmyInterval> rt = cq.from(AmyInterval.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
