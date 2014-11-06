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
import emcali.ami.persistence.entity.ConsumosConsumos;
import emcali.ami.persistence.entity.AmyConsumos;
import emcali.ami.persistence.entity.AmyTipolecturas;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class AmyTipolecturasJpaController implements Serializable {

    public AmyTipolecturasJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AmyTipolecturas amyTipolecturas) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (amyTipolecturas.getAmyLecturasCollection() == null) {
            amyTipolecturas.setAmyLecturasCollection(new ArrayList<AmyLecturas>());
        }
        if (amyTipolecturas.getConsumosConsumosCollection() == null) {
            amyTipolecturas.setConsumosConsumosCollection(new ArrayList<ConsumosConsumos>());
        }
        if (amyTipolecturas.getAmyConsumosCollection() == null) {
            amyTipolecturas.setAmyConsumosCollection(new ArrayList<AmyConsumos>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<AmyLecturas> attachedAmyLecturasCollection = new ArrayList<AmyLecturas>();
            for (AmyLecturas amyLecturasCollectionAmyLecturasToAttach : amyTipolecturas.getAmyLecturasCollection()) {
                amyLecturasCollectionAmyLecturasToAttach = em.getReference(amyLecturasCollectionAmyLecturasToAttach.getClass(), amyLecturasCollectionAmyLecturasToAttach.getIdLecturas());
                attachedAmyLecturasCollection.add(amyLecturasCollectionAmyLecturasToAttach);
            }
            amyTipolecturas.setAmyLecturasCollection(attachedAmyLecturasCollection);
            Collection<ConsumosConsumos> attachedConsumosConsumosCollection = new ArrayList<ConsumosConsumos>();
            for (ConsumosConsumos consumosConsumosCollectionConsumosConsumosToAttach : amyTipolecturas.getConsumosConsumosCollection()) {
                consumosConsumosCollectionConsumosConsumosToAttach = em.getReference(consumosConsumosCollectionConsumosConsumosToAttach.getClass(), consumosConsumosCollectionConsumosConsumosToAttach.getIdConsumo());
                attachedConsumosConsumosCollection.add(consumosConsumosCollectionConsumosConsumosToAttach);
            }
            amyTipolecturas.setConsumosConsumosCollection(attachedConsumosConsumosCollection);
            Collection<AmyConsumos> attachedAmyConsumosCollection = new ArrayList<AmyConsumos>();
            for (AmyConsumos amyConsumosCollectionAmyConsumosToAttach : amyTipolecturas.getAmyConsumosCollection()) {
                amyConsumosCollectionAmyConsumosToAttach = em.getReference(amyConsumosCollectionAmyConsumosToAttach.getClass(), amyConsumosCollectionAmyConsumosToAttach.getIdConsumo());
                attachedAmyConsumosCollection.add(amyConsumosCollectionAmyConsumosToAttach);
            }
            amyTipolecturas.setAmyConsumosCollection(attachedAmyConsumosCollection);
            em.persist(amyTipolecturas);
            for (AmyLecturas amyLecturasCollectionAmyLecturas : amyTipolecturas.getAmyLecturasCollection()) {
                AmyTipolecturas oldFkAmyTipolecturasOfAmyLecturasCollectionAmyLecturas = amyLecturasCollectionAmyLecturas.getFkAmyTipolecturas();
                amyLecturasCollectionAmyLecturas.setFkAmyTipolecturas(amyTipolecturas);
                amyLecturasCollectionAmyLecturas = em.merge(amyLecturasCollectionAmyLecturas);
                if (oldFkAmyTipolecturasOfAmyLecturasCollectionAmyLecturas != null) {
                    oldFkAmyTipolecturasOfAmyLecturasCollectionAmyLecturas.getAmyLecturasCollection().remove(amyLecturasCollectionAmyLecturas);
                    oldFkAmyTipolecturasOfAmyLecturasCollectionAmyLecturas = em.merge(oldFkAmyTipolecturasOfAmyLecturasCollectionAmyLecturas);
                }
            }
            for (ConsumosConsumos consumosConsumosCollectionConsumosConsumos : amyTipolecturas.getConsumosConsumosCollection()) {
                AmyTipolecturas oldFkAmyTipolecturasOfConsumosConsumosCollectionConsumosConsumos = consumosConsumosCollectionConsumosConsumos.getFkAmyTipolecturas();
                consumosConsumosCollectionConsumosConsumos.setFkAmyTipolecturas(amyTipolecturas);
                consumosConsumosCollectionConsumosConsumos = em.merge(consumosConsumosCollectionConsumosConsumos);
                if (oldFkAmyTipolecturasOfConsumosConsumosCollectionConsumosConsumos != null) {
                    oldFkAmyTipolecturasOfConsumosConsumosCollectionConsumosConsumos.getConsumosConsumosCollection().remove(consumosConsumosCollectionConsumosConsumos);
                    oldFkAmyTipolecturasOfConsumosConsumosCollectionConsumosConsumos = em.merge(oldFkAmyTipolecturasOfConsumosConsumosCollectionConsumosConsumos);
                }
            }
            for (AmyConsumos amyConsumosCollectionAmyConsumos : amyTipolecturas.getAmyConsumosCollection()) {
                AmyTipolecturas oldFkAmyTipolecturasOfAmyConsumosCollectionAmyConsumos = amyConsumosCollectionAmyConsumos.getFkAmyTipolecturas();
                amyConsumosCollectionAmyConsumos.setFkAmyTipolecturas(amyTipolecturas);
                amyConsumosCollectionAmyConsumos = em.merge(amyConsumosCollectionAmyConsumos);
                if (oldFkAmyTipolecturasOfAmyConsumosCollectionAmyConsumos != null) {
                    oldFkAmyTipolecturasOfAmyConsumosCollectionAmyConsumos.getAmyConsumosCollection().remove(amyConsumosCollectionAmyConsumos);
                    oldFkAmyTipolecturasOfAmyConsumosCollectionAmyConsumos = em.merge(oldFkAmyTipolecturasOfAmyConsumosCollectionAmyConsumos);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAmyTipolecturas(amyTipolecturas.getIdTipolectura()) != null) {
                throw new PreexistingEntityException("AmyTipolecturas " + amyTipolecturas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AmyTipolecturas amyTipolecturas) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AmyTipolecturas persistentAmyTipolecturas = em.find(AmyTipolecturas.class, amyTipolecturas.getIdTipolectura());
            Collection<AmyLecturas> amyLecturasCollectionOld = persistentAmyTipolecturas.getAmyLecturasCollection();
            Collection<AmyLecturas> amyLecturasCollectionNew = amyTipolecturas.getAmyLecturasCollection();
            Collection<ConsumosConsumos> consumosConsumosCollectionOld = persistentAmyTipolecturas.getConsumosConsumosCollection();
            Collection<ConsumosConsumos> consumosConsumosCollectionNew = amyTipolecturas.getConsumosConsumosCollection();
            Collection<AmyConsumos> amyConsumosCollectionOld = persistentAmyTipolecturas.getAmyConsumosCollection();
            Collection<AmyConsumos> amyConsumosCollectionNew = amyTipolecturas.getAmyConsumosCollection();
            List<String> illegalOrphanMessages = null;
            for (AmyLecturas amyLecturasCollectionOldAmyLecturas : amyLecturasCollectionOld) {
                if (!amyLecturasCollectionNew.contains(amyLecturasCollectionOldAmyLecturas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyLecturas " + amyLecturasCollectionOldAmyLecturas + " since its fkAmyTipolecturas field is not nullable.");
                }
            }
            for (ConsumosConsumos consumosConsumosCollectionOldConsumosConsumos : consumosConsumosCollectionOld) {
                if (!consumosConsumosCollectionNew.contains(consumosConsumosCollectionOldConsumosConsumos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ConsumosConsumos " + consumosConsumosCollectionOldConsumosConsumos + " since its fkAmyTipolecturas field is not nullable.");
                }
            }
            for (AmyConsumos amyConsumosCollectionOldAmyConsumos : amyConsumosCollectionOld) {
                if (!amyConsumosCollectionNew.contains(amyConsumosCollectionOldAmyConsumos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyConsumos " + amyConsumosCollectionOldAmyConsumos + " since its fkAmyTipolecturas field is not nullable.");
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
            amyTipolecturas.setAmyLecturasCollection(amyLecturasCollectionNew);
            Collection<ConsumosConsumos> attachedConsumosConsumosCollectionNew = new ArrayList<ConsumosConsumos>();
            for (ConsumosConsumos consumosConsumosCollectionNewConsumosConsumosToAttach : consumosConsumosCollectionNew) {
                consumosConsumosCollectionNewConsumosConsumosToAttach = em.getReference(consumosConsumosCollectionNewConsumosConsumosToAttach.getClass(), consumosConsumosCollectionNewConsumosConsumosToAttach.getIdConsumo());
                attachedConsumosConsumosCollectionNew.add(consumosConsumosCollectionNewConsumosConsumosToAttach);
            }
            consumosConsumosCollectionNew = attachedConsumosConsumosCollectionNew;
            amyTipolecturas.setConsumosConsumosCollection(consumosConsumosCollectionNew);
            Collection<AmyConsumos> attachedAmyConsumosCollectionNew = new ArrayList<AmyConsumos>();
            for (AmyConsumos amyConsumosCollectionNewAmyConsumosToAttach : amyConsumosCollectionNew) {
                amyConsumosCollectionNewAmyConsumosToAttach = em.getReference(amyConsumosCollectionNewAmyConsumosToAttach.getClass(), amyConsumosCollectionNewAmyConsumosToAttach.getIdConsumo());
                attachedAmyConsumosCollectionNew.add(amyConsumosCollectionNewAmyConsumosToAttach);
            }
            amyConsumosCollectionNew = attachedAmyConsumosCollectionNew;
            amyTipolecturas.setAmyConsumosCollection(amyConsumosCollectionNew);
            amyTipolecturas = em.merge(amyTipolecturas);
            for (AmyLecturas amyLecturasCollectionNewAmyLecturas : amyLecturasCollectionNew) {
                if (!amyLecturasCollectionOld.contains(amyLecturasCollectionNewAmyLecturas)) {
                    AmyTipolecturas oldFkAmyTipolecturasOfAmyLecturasCollectionNewAmyLecturas = amyLecturasCollectionNewAmyLecturas.getFkAmyTipolecturas();
                    amyLecturasCollectionNewAmyLecturas.setFkAmyTipolecturas(amyTipolecturas);
                    amyLecturasCollectionNewAmyLecturas = em.merge(amyLecturasCollectionNewAmyLecturas);
                    if (oldFkAmyTipolecturasOfAmyLecturasCollectionNewAmyLecturas != null && !oldFkAmyTipolecturasOfAmyLecturasCollectionNewAmyLecturas.equals(amyTipolecturas)) {
                        oldFkAmyTipolecturasOfAmyLecturasCollectionNewAmyLecturas.getAmyLecturasCollection().remove(amyLecturasCollectionNewAmyLecturas);
                        oldFkAmyTipolecturasOfAmyLecturasCollectionNewAmyLecturas = em.merge(oldFkAmyTipolecturasOfAmyLecturasCollectionNewAmyLecturas);
                    }
                }
            }
            for (ConsumosConsumos consumosConsumosCollectionNewConsumosConsumos : consumosConsumosCollectionNew) {
                if (!consumosConsumosCollectionOld.contains(consumosConsumosCollectionNewConsumosConsumos)) {
                    AmyTipolecturas oldFkAmyTipolecturasOfConsumosConsumosCollectionNewConsumosConsumos = consumosConsumosCollectionNewConsumosConsumos.getFkAmyTipolecturas();
                    consumosConsumosCollectionNewConsumosConsumos.setFkAmyTipolecturas(amyTipolecturas);
                    consumosConsumosCollectionNewConsumosConsumos = em.merge(consumosConsumosCollectionNewConsumosConsumos);
                    if (oldFkAmyTipolecturasOfConsumosConsumosCollectionNewConsumosConsumos != null && !oldFkAmyTipolecturasOfConsumosConsumosCollectionNewConsumosConsumos.equals(amyTipolecturas)) {
                        oldFkAmyTipolecturasOfConsumosConsumosCollectionNewConsumosConsumos.getConsumosConsumosCollection().remove(consumosConsumosCollectionNewConsumosConsumos);
                        oldFkAmyTipolecturasOfConsumosConsumosCollectionNewConsumosConsumos = em.merge(oldFkAmyTipolecturasOfConsumosConsumosCollectionNewConsumosConsumos);
                    }
                }
            }
            for (AmyConsumos amyConsumosCollectionNewAmyConsumos : amyConsumosCollectionNew) {
                if (!amyConsumosCollectionOld.contains(amyConsumosCollectionNewAmyConsumos)) {
                    AmyTipolecturas oldFkAmyTipolecturasOfAmyConsumosCollectionNewAmyConsumos = amyConsumosCollectionNewAmyConsumos.getFkAmyTipolecturas();
                    amyConsumosCollectionNewAmyConsumos.setFkAmyTipolecturas(amyTipolecturas);
                    amyConsumosCollectionNewAmyConsumos = em.merge(amyConsumosCollectionNewAmyConsumos);
                    if (oldFkAmyTipolecturasOfAmyConsumosCollectionNewAmyConsumos != null && !oldFkAmyTipolecturasOfAmyConsumosCollectionNewAmyConsumos.equals(amyTipolecturas)) {
                        oldFkAmyTipolecturasOfAmyConsumosCollectionNewAmyConsumos.getAmyConsumosCollection().remove(amyConsumosCollectionNewAmyConsumos);
                        oldFkAmyTipolecturasOfAmyConsumosCollectionNewAmyConsumos = em.merge(oldFkAmyTipolecturasOfAmyConsumosCollectionNewAmyConsumos);
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
                Long id = amyTipolecturas.getIdTipolectura();
                if (findAmyTipolecturas(id) == null) {
                    throw new NonexistentEntityException("The amyTipolecturas with id " + id + " no longer exists.");
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
            AmyTipolecturas amyTipolecturas;
            try {
                amyTipolecturas = em.getReference(AmyTipolecturas.class, id);
                amyTipolecturas.getIdTipolectura();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The amyTipolecturas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<AmyLecturas> amyLecturasCollectionOrphanCheck = amyTipolecturas.getAmyLecturasCollection();
            for (AmyLecturas amyLecturasCollectionOrphanCheckAmyLecturas : amyLecturasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyTipolecturas (" + amyTipolecturas + ") cannot be destroyed since the AmyLecturas " + amyLecturasCollectionOrphanCheckAmyLecturas + " in its amyLecturasCollection field has a non-nullable fkAmyTipolecturas field.");
            }
            Collection<ConsumosConsumos> consumosConsumosCollectionOrphanCheck = amyTipolecturas.getConsumosConsumosCollection();
            for (ConsumosConsumos consumosConsumosCollectionOrphanCheckConsumosConsumos : consumosConsumosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyTipolecturas (" + amyTipolecturas + ") cannot be destroyed since the ConsumosConsumos " + consumosConsumosCollectionOrphanCheckConsumosConsumos + " in its consumosConsumosCollection field has a non-nullable fkAmyTipolecturas field.");
            }
            Collection<AmyConsumos> amyConsumosCollectionOrphanCheck = amyTipolecturas.getAmyConsumosCollection();
            for (AmyConsumos amyConsumosCollectionOrphanCheckAmyConsumos : amyConsumosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyTipolecturas (" + amyTipolecturas + ") cannot be destroyed since the AmyConsumos " + amyConsumosCollectionOrphanCheckAmyConsumos + " in its amyConsumosCollection field has a non-nullable fkAmyTipolecturas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(amyTipolecturas);
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

    public List<AmyTipolecturas> findAmyTipolecturasEntities() {
        return findAmyTipolecturasEntities(true, -1, -1);
    }

    public List<AmyTipolecturas> findAmyTipolecturasEntities(int maxResults, int firstResult) {
        return findAmyTipolecturasEntities(false, maxResults, firstResult);
    }

    private List<AmyTipolecturas> findAmyTipolecturasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AmyTipolecturas.class));
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

    public AmyTipolecturas findAmyTipolecturas(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AmyTipolecturas.class, id);
        } finally {
            em.close();
        }
    }

    public int getAmyTipolecturasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AmyTipolecturas> rt = cq.from(AmyTipolecturas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
