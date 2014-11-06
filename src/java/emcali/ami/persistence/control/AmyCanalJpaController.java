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
import emcali.ami.persistence.entity.AmyCanal;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import emcali.ami.persistence.entity.AmyLecturas;
import java.util.ArrayList;
import java.util.Collection;
import emcali.ami.persistence.entity.AmyConsumos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class AmyCanalJpaController implements Serializable {

    public AmyCanalJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AmyCanal amyCanal) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (amyCanal.getAmyLecturasCollection() == null) {
            amyCanal.setAmyLecturasCollection(new ArrayList<AmyLecturas>());
        }
        if (amyCanal.getAmyConsumosCollection() == null) {
            amyCanal.setAmyConsumosCollection(new ArrayList<AmyConsumos>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<AmyLecturas> attachedAmyLecturasCollection = new ArrayList<AmyLecturas>();
            for (AmyLecturas amyLecturasCollectionAmyLecturasToAttach : amyCanal.getAmyLecturasCollection()) {
                amyLecturasCollectionAmyLecturasToAttach = em.getReference(amyLecturasCollectionAmyLecturasToAttach.getClass(), amyLecturasCollectionAmyLecturasToAttach.getIdLecturas());
                attachedAmyLecturasCollection.add(amyLecturasCollectionAmyLecturasToAttach);
            }
            amyCanal.setAmyLecturasCollection(attachedAmyLecturasCollection);
            Collection<AmyConsumos> attachedAmyConsumosCollection = new ArrayList<AmyConsumos>();
            for (AmyConsumos amyConsumosCollectionAmyConsumosToAttach : amyCanal.getAmyConsumosCollection()) {
                amyConsumosCollectionAmyConsumosToAttach = em.getReference(amyConsumosCollectionAmyConsumosToAttach.getClass(), amyConsumosCollectionAmyConsumosToAttach.getIdConsumo());
                attachedAmyConsumosCollection.add(amyConsumosCollectionAmyConsumosToAttach);
            }
            amyCanal.setAmyConsumosCollection(attachedAmyConsumosCollection);
            em.persist(amyCanal);
            for (AmyLecturas amyLecturasCollectionAmyLecturas : amyCanal.getAmyLecturasCollection()) {
                AmyCanal oldFkAmyCanalOfAmyLecturasCollectionAmyLecturas = amyLecturasCollectionAmyLecturas.getFkAmyCanal();
                amyLecturasCollectionAmyLecturas.setFkAmyCanal(amyCanal);
                amyLecturasCollectionAmyLecturas = em.merge(amyLecturasCollectionAmyLecturas);
                if (oldFkAmyCanalOfAmyLecturasCollectionAmyLecturas != null) {
                    oldFkAmyCanalOfAmyLecturasCollectionAmyLecturas.getAmyLecturasCollection().remove(amyLecturasCollectionAmyLecturas);
                    oldFkAmyCanalOfAmyLecturasCollectionAmyLecturas = em.merge(oldFkAmyCanalOfAmyLecturasCollectionAmyLecturas);
                }
            }
            for (AmyConsumos amyConsumosCollectionAmyConsumos : amyCanal.getAmyConsumosCollection()) {
                AmyCanal oldFkAmyCanalOfAmyConsumosCollectionAmyConsumos = amyConsumosCollectionAmyConsumos.getFkAmyCanal();
                amyConsumosCollectionAmyConsumos.setFkAmyCanal(amyCanal);
                amyConsumosCollectionAmyConsumos = em.merge(amyConsumosCollectionAmyConsumos);
                if (oldFkAmyCanalOfAmyConsumosCollectionAmyConsumos != null) {
                    oldFkAmyCanalOfAmyConsumosCollectionAmyConsumos.getAmyConsumosCollection().remove(amyConsumosCollectionAmyConsumos);
                    oldFkAmyCanalOfAmyConsumosCollectionAmyConsumos = em.merge(oldFkAmyCanalOfAmyConsumosCollectionAmyConsumos);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAmyCanal(amyCanal.getIdCanal()) != null) {
                throw new PreexistingEntityException("AmyCanal " + amyCanal + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AmyCanal amyCanal) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AmyCanal persistentAmyCanal = em.find(AmyCanal.class, amyCanal.getIdCanal());
            Collection<AmyLecturas> amyLecturasCollectionOld = persistentAmyCanal.getAmyLecturasCollection();
            Collection<AmyLecturas> amyLecturasCollectionNew = amyCanal.getAmyLecturasCollection();
            Collection<AmyConsumos> amyConsumosCollectionOld = persistentAmyCanal.getAmyConsumosCollection();
            Collection<AmyConsumos> amyConsumosCollectionNew = amyCanal.getAmyConsumosCollection();
            List<String> illegalOrphanMessages = null;
            for (AmyLecturas amyLecturasCollectionOldAmyLecturas : amyLecturasCollectionOld) {
                if (!amyLecturasCollectionNew.contains(amyLecturasCollectionOldAmyLecturas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyLecturas " + amyLecturasCollectionOldAmyLecturas + " since its fkAmyCanal field is not nullable.");
                }
            }
            for (AmyConsumos amyConsumosCollectionOldAmyConsumos : amyConsumosCollectionOld) {
                if (!amyConsumosCollectionNew.contains(amyConsumosCollectionOldAmyConsumos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyConsumos " + amyConsumosCollectionOldAmyConsumos + " since its fkAmyCanal field is not nullable.");
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
            amyCanal.setAmyLecturasCollection(amyLecturasCollectionNew);
            Collection<AmyConsumos> attachedAmyConsumosCollectionNew = new ArrayList<AmyConsumos>();
            for (AmyConsumos amyConsumosCollectionNewAmyConsumosToAttach : amyConsumosCollectionNew) {
                amyConsumosCollectionNewAmyConsumosToAttach = em.getReference(amyConsumosCollectionNewAmyConsumosToAttach.getClass(), amyConsumosCollectionNewAmyConsumosToAttach.getIdConsumo());
                attachedAmyConsumosCollectionNew.add(amyConsumosCollectionNewAmyConsumosToAttach);
            }
            amyConsumosCollectionNew = attachedAmyConsumosCollectionNew;
            amyCanal.setAmyConsumosCollection(amyConsumosCollectionNew);
            amyCanal = em.merge(amyCanal);
            for (AmyLecturas amyLecturasCollectionNewAmyLecturas : amyLecturasCollectionNew) {
                if (!amyLecturasCollectionOld.contains(amyLecturasCollectionNewAmyLecturas)) {
                    AmyCanal oldFkAmyCanalOfAmyLecturasCollectionNewAmyLecturas = amyLecturasCollectionNewAmyLecturas.getFkAmyCanal();
                    amyLecturasCollectionNewAmyLecturas.setFkAmyCanal(amyCanal);
                    amyLecturasCollectionNewAmyLecturas = em.merge(amyLecturasCollectionNewAmyLecturas);
                    if (oldFkAmyCanalOfAmyLecturasCollectionNewAmyLecturas != null && !oldFkAmyCanalOfAmyLecturasCollectionNewAmyLecturas.equals(amyCanal)) {
                        oldFkAmyCanalOfAmyLecturasCollectionNewAmyLecturas.getAmyLecturasCollection().remove(amyLecturasCollectionNewAmyLecturas);
                        oldFkAmyCanalOfAmyLecturasCollectionNewAmyLecturas = em.merge(oldFkAmyCanalOfAmyLecturasCollectionNewAmyLecturas);
                    }
                }
            }
            for (AmyConsumos amyConsumosCollectionNewAmyConsumos : amyConsumosCollectionNew) {
                if (!amyConsumosCollectionOld.contains(amyConsumosCollectionNewAmyConsumos)) {
                    AmyCanal oldFkAmyCanalOfAmyConsumosCollectionNewAmyConsumos = amyConsumosCollectionNewAmyConsumos.getFkAmyCanal();
                    amyConsumosCollectionNewAmyConsumos.setFkAmyCanal(amyCanal);
                    amyConsumosCollectionNewAmyConsumos = em.merge(amyConsumosCollectionNewAmyConsumos);
                    if (oldFkAmyCanalOfAmyConsumosCollectionNewAmyConsumos != null && !oldFkAmyCanalOfAmyConsumosCollectionNewAmyConsumos.equals(amyCanal)) {
                        oldFkAmyCanalOfAmyConsumosCollectionNewAmyConsumos.getAmyConsumosCollection().remove(amyConsumosCollectionNewAmyConsumos);
                        oldFkAmyCanalOfAmyConsumosCollectionNewAmyConsumos = em.merge(oldFkAmyCanalOfAmyConsumosCollectionNewAmyConsumos);
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
                Long id = amyCanal.getIdCanal();
                if (findAmyCanal(id) == null) {
                    throw new NonexistentEntityException("The amyCanal with id " + id + " no longer exists.");
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
            AmyCanal amyCanal;
            try {
                amyCanal = em.getReference(AmyCanal.class, id);
                amyCanal.getIdCanal();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The amyCanal with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<AmyLecturas> amyLecturasCollectionOrphanCheck = amyCanal.getAmyLecturasCollection();
            for (AmyLecturas amyLecturasCollectionOrphanCheckAmyLecturas : amyLecturasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyCanal (" + amyCanal + ") cannot be destroyed since the AmyLecturas " + amyLecturasCollectionOrphanCheckAmyLecturas + " in its amyLecturasCollection field has a non-nullable fkAmyCanal field.");
            }
            Collection<AmyConsumos> amyConsumosCollectionOrphanCheck = amyCanal.getAmyConsumosCollection();
            for (AmyConsumos amyConsumosCollectionOrphanCheckAmyConsumos : amyConsumosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyCanal (" + amyCanal + ") cannot be destroyed since the AmyConsumos " + amyConsumosCollectionOrphanCheckAmyConsumos + " in its amyConsumosCollection field has a non-nullable fkAmyCanal field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(amyCanal);
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

    public List<AmyCanal> findAmyCanalEntities() {
        return findAmyCanalEntities(true, -1, -1);
    }

    public List<AmyCanal> findAmyCanalEntities(int maxResults, int firstResult) {
        return findAmyCanalEntities(false, maxResults, firstResult);
    }

    private List<AmyCanal> findAmyCanalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AmyCanal.class));
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

    public AmyCanal findAmyCanal(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AmyCanal.class, id);
        } finally {
            em.close();
        }
    }

    public int getAmyCanalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AmyCanal> rt = cq.from(AmyCanal.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
