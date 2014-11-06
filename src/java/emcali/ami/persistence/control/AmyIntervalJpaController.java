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
import java.util.Collection;
import emcali.ami.persistence.entity.AmyConsumos;
import emcali.ami.persistence.entity.AmyInterval;
import java.util.List;
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
        if (amyInterval.getAmyLecturasCollection() == null) {
            amyInterval.setAmyLecturasCollection(new ArrayList<AmyLecturas>());
        }
        if (amyInterval.getAmyConsumosCollection() == null) {
            amyInterval.setAmyConsumosCollection(new ArrayList<AmyConsumos>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<AmyLecturas> attachedAmyLecturasCollection = new ArrayList<AmyLecturas>();
            for (AmyLecturas amyLecturasCollectionAmyLecturasToAttach : amyInterval.getAmyLecturasCollection()) {
                amyLecturasCollectionAmyLecturasToAttach = em.getReference(amyLecturasCollectionAmyLecturasToAttach.getClass(), amyLecturasCollectionAmyLecturasToAttach.getIdLecturas());
                attachedAmyLecturasCollection.add(amyLecturasCollectionAmyLecturasToAttach);
            }
            amyInterval.setAmyLecturasCollection(attachedAmyLecturasCollection);
            Collection<AmyConsumos> attachedAmyConsumosCollection = new ArrayList<AmyConsumos>();
            for (AmyConsumos amyConsumosCollectionAmyConsumosToAttach : amyInterval.getAmyConsumosCollection()) {
                amyConsumosCollectionAmyConsumosToAttach = em.getReference(amyConsumosCollectionAmyConsumosToAttach.getClass(), amyConsumosCollectionAmyConsumosToAttach.getIdConsumo());
                attachedAmyConsumosCollection.add(amyConsumosCollectionAmyConsumosToAttach);
            }
            amyInterval.setAmyConsumosCollection(attachedAmyConsumosCollection);
            em.persist(amyInterval);
            for (AmyLecturas amyLecturasCollectionAmyLecturas : amyInterval.getAmyLecturasCollection()) {
                AmyInterval oldFkAmyIntervalOfAmyLecturasCollectionAmyLecturas = amyLecturasCollectionAmyLecturas.getFkAmyInterval();
                amyLecturasCollectionAmyLecturas.setFkAmyInterval(amyInterval);
                amyLecturasCollectionAmyLecturas = em.merge(amyLecturasCollectionAmyLecturas);
                if (oldFkAmyIntervalOfAmyLecturasCollectionAmyLecturas != null) {
                    oldFkAmyIntervalOfAmyLecturasCollectionAmyLecturas.getAmyLecturasCollection().remove(amyLecturasCollectionAmyLecturas);
                    oldFkAmyIntervalOfAmyLecturasCollectionAmyLecturas = em.merge(oldFkAmyIntervalOfAmyLecturasCollectionAmyLecturas);
                }
            }
            for (AmyConsumos amyConsumosCollectionAmyConsumos : amyInterval.getAmyConsumosCollection()) {
                AmyInterval oldFkAmyIntervalOfAmyConsumosCollectionAmyConsumos = amyConsumosCollectionAmyConsumos.getFkAmyInterval();
                amyConsumosCollectionAmyConsumos.setFkAmyInterval(amyInterval);
                amyConsumosCollectionAmyConsumos = em.merge(amyConsumosCollectionAmyConsumos);
                if (oldFkAmyIntervalOfAmyConsumosCollectionAmyConsumos != null) {
                    oldFkAmyIntervalOfAmyConsumosCollectionAmyConsumos.getAmyConsumosCollection().remove(amyConsumosCollectionAmyConsumos);
                    oldFkAmyIntervalOfAmyConsumosCollectionAmyConsumos = em.merge(oldFkAmyIntervalOfAmyConsumosCollectionAmyConsumos);
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
            Collection<AmyLecturas> amyLecturasCollectionOld = persistentAmyInterval.getAmyLecturasCollection();
            Collection<AmyLecturas> amyLecturasCollectionNew = amyInterval.getAmyLecturasCollection();
            Collection<AmyConsumos> amyConsumosCollectionOld = persistentAmyInterval.getAmyConsumosCollection();
            Collection<AmyConsumos> amyConsumosCollectionNew = amyInterval.getAmyConsumosCollection();
            List<String> illegalOrphanMessages = null;
            for (AmyLecturas amyLecturasCollectionOldAmyLecturas : amyLecturasCollectionOld) {
                if (!amyLecturasCollectionNew.contains(amyLecturasCollectionOldAmyLecturas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyLecturas " + amyLecturasCollectionOldAmyLecturas + " since its fkAmyInterval field is not nullable.");
                }
            }
            for (AmyConsumos amyConsumosCollectionOldAmyConsumos : amyConsumosCollectionOld) {
                if (!amyConsumosCollectionNew.contains(amyConsumosCollectionOldAmyConsumos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyConsumos " + amyConsumosCollectionOldAmyConsumos + " since its fkAmyInterval field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<AmyLecturas> attachedAmyLecturasCollectionNew = new ArrayList<AmyLecturas>();
            for (AmyLecturas amyLecturasCollectionNewAmyLecturasToAttach : amyLecturasCollectionNew) {
                amyLecturasCollectionNewAmyLecturasToAttach = em.getReference(amyLecturasCollectionNewAmyLecturasToAttach.getClass(), amyLecturasCollectionNewAmyLecturasToAttach.getIdLecturas());
                attachedAmyLecturasCollectionNew.add(amyLecturasCollectionNewAmyLecturasToAttach);
            }
            amyLecturasCollectionNew = attachedAmyLecturasCollectionNew;
            amyInterval.setAmyLecturasCollection(amyLecturasCollectionNew);
            Collection<AmyConsumos> attachedAmyConsumosCollectionNew = new ArrayList<AmyConsumos>();
            for (AmyConsumos amyConsumosCollectionNewAmyConsumosToAttach : amyConsumosCollectionNew) {
                amyConsumosCollectionNewAmyConsumosToAttach = em.getReference(amyConsumosCollectionNewAmyConsumosToAttach.getClass(), amyConsumosCollectionNewAmyConsumosToAttach.getIdConsumo());
                attachedAmyConsumosCollectionNew.add(amyConsumosCollectionNewAmyConsumosToAttach);
            }
            amyConsumosCollectionNew = attachedAmyConsumosCollectionNew;
            amyInterval.setAmyConsumosCollection(amyConsumosCollectionNew);
            amyInterval = em.merge(amyInterval);
            for (AmyLecturas amyLecturasCollectionNewAmyLecturas : amyLecturasCollectionNew) {
                if (!amyLecturasCollectionOld.contains(amyLecturasCollectionNewAmyLecturas)) {
                    AmyInterval oldFkAmyIntervalOfAmyLecturasCollectionNewAmyLecturas = amyLecturasCollectionNewAmyLecturas.getFkAmyInterval();
                    amyLecturasCollectionNewAmyLecturas.setFkAmyInterval(amyInterval);
                    amyLecturasCollectionNewAmyLecturas = em.merge(amyLecturasCollectionNewAmyLecturas);
                    if (oldFkAmyIntervalOfAmyLecturasCollectionNewAmyLecturas != null && !oldFkAmyIntervalOfAmyLecturasCollectionNewAmyLecturas.equals(amyInterval)) {
                        oldFkAmyIntervalOfAmyLecturasCollectionNewAmyLecturas.getAmyLecturasCollection().remove(amyLecturasCollectionNewAmyLecturas);
                        oldFkAmyIntervalOfAmyLecturasCollectionNewAmyLecturas = em.merge(oldFkAmyIntervalOfAmyLecturasCollectionNewAmyLecturas);
                    }
                }
            }
            for (AmyConsumos amyConsumosCollectionNewAmyConsumos : amyConsumosCollectionNew) {
                if (!amyConsumosCollectionOld.contains(amyConsumosCollectionNewAmyConsumos)) {
                    AmyInterval oldFkAmyIntervalOfAmyConsumosCollectionNewAmyConsumos = amyConsumosCollectionNewAmyConsumos.getFkAmyInterval();
                    amyConsumosCollectionNewAmyConsumos.setFkAmyInterval(amyInterval);
                    amyConsumosCollectionNewAmyConsumos = em.merge(amyConsumosCollectionNewAmyConsumos);
                    if (oldFkAmyIntervalOfAmyConsumosCollectionNewAmyConsumos != null && !oldFkAmyIntervalOfAmyConsumosCollectionNewAmyConsumos.equals(amyInterval)) {
                        oldFkAmyIntervalOfAmyConsumosCollectionNewAmyConsumos.getAmyConsumosCollection().remove(amyConsumosCollectionNewAmyConsumos);
                        oldFkAmyIntervalOfAmyConsumosCollectionNewAmyConsumos = em.merge(oldFkAmyIntervalOfAmyConsumosCollectionNewAmyConsumos);
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
            Collection<AmyLecturas> amyLecturasCollectionOrphanCheck = amyInterval.getAmyLecturasCollection();
            for (AmyLecturas amyLecturasCollectionOrphanCheckAmyLecturas : amyLecturasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyInterval (" + amyInterval + ") cannot be destroyed since the AmyLecturas " + amyLecturasCollectionOrphanCheckAmyLecturas + " in its amyLecturasCollection field has a non-nullable fkAmyInterval field.");
            }
            Collection<AmyConsumos> amyConsumosCollectionOrphanCheck = amyInterval.getAmyConsumosCollection();
            for (AmyConsumos amyConsumosCollectionOrphanCheckAmyConsumos : amyConsumosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyInterval (" + amyInterval + ") cannot be destroyed since the AmyConsumos " + amyConsumosCollectionOrphanCheckAmyConsumos + " in its amyConsumosCollection field has a non-nullable fkAmyInterval field.");
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
