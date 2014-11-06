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
import emcali.ami.persistence.entity.AmyEventos;
import emcali.ami.persistence.entity.AmyTipoEventos;
import java.util.ArrayList;
import java.util.Collection;
import emcali.ami.persistence.entity.PrepagoEventos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class AmyTipoEventosJpaController implements Serializable {

    public AmyTipoEventosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AmyTipoEventos amyTipoEventos) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (amyTipoEventos.getAmyEventosCollection() == null) {
            amyTipoEventos.setAmyEventosCollection(new ArrayList<AmyEventos>());
        }
        if (amyTipoEventos.getPrepagoEventosCollection() == null) {
            amyTipoEventos.setPrepagoEventosCollection(new ArrayList<PrepagoEventos>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<AmyEventos> attachedAmyEventosCollection = new ArrayList<AmyEventos>();
            for (AmyEventos amyEventosCollectionAmyEventosToAttach : amyTipoEventos.getAmyEventosCollection()) {
                amyEventosCollectionAmyEventosToAttach = em.getReference(amyEventosCollectionAmyEventosToAttach.getClass(), amyEventosCollectionAmyEventosToAttach.getIdEvento());
                attachedAmyEventosCollection.add(amyEventosCollectionAmyEventosToAttach);
            }
            amyTipoEventos.setAmyEventosCollection(attachedAmyEventosCollection);
            Collection<PrepagoEventos> attachedPrepagoEventosCollection = new ArrayList<PrepagoEventos>();
            for (PrepagoEventos prepagoEventosCollectionPrepagoEventosToAttach : amyTipoEventos.getPrepagoEventosCollection()) {
                prepagoEventosCollectionPrepagoEventosToAttach = em.getReference(prepagoEventosCollectionPrepagoEventosToAttach.getClass(), prepagoEventosCollectionPrepagoEventosToAttach.getIdEventos());
                attachedPrepagoEventosCollection.add(prepagoEventosCollectionPrepagoEventosToAttach);
            }
            amyTipoEventos.setPrepagoEventosCollection(attachedPrepagoEventosCollection);
            em.persist(amyTipoEventos);
            for (AmyEventos amyEventosCollectionAmyEventos : amyTipoEventos.getAmyEventosCollection()) {
                AmyTipoEventos oldFkAmyTipoEventosOfAmyEventosCollectionAmyEventos = amyEventosCollectionAmyEventos.getFkAmyTipoEventos();
                amyEventosCollectionAmyEventos.setFkAmyTipoEventos(amyTipoEventos);
                amyEventosCollectionAmyEventos = em.merge(amyEventosCollectionAmyEventos);
                if (oldFkAmyTipoEventosOfAmyEventosCollectionAmyEventos != null) {
                    oldFkAmyTipoEventosOfAmyEventosCollectionAmyEventos.getAmyEventosCollection().remove(amyEventosCollectionAmyEventos);
                    oldFkAmyTipoEventosOfAmyEventosCollectionAmyEventos = em.merge(oldFkAmyTipoEventosOfAmyEventosCollectionAmyEventos);
                }
            }
            for (PrepagoEventos prepagoEventosCollectionPrepagoEventos : amyTipoEventos.getPrepagoEventosCollection()) {
                AmyTipoEventos oldFkAmyTipoEventosOfPrepagoEventosCollectionPrepagoEventos = prepagoEventosCollectionPrepagoEventos.getFkAmyTipoEventos();
                prepagoEventosCollectionPrepagoEventos.setFkAmyTipoEventos(amyTipoEventos);
                prepagoEventosCollectionPrepagoEventos = em.merge(prepagoEventosCollectionPrepagoEventos);
                if (oldFkAmyTipoEventosOfPrepagoEventosCollectionPrepagoEventos != null) {
                    oldFkAmyTipoEventosOfPrepagoEventosCollectionPrepagoEventos.getPrepagoEventosCollection().remove(prepagoEventosCollectionPrepagoEventos);
                    oldFkAmyTipoEventosOfPrepagoEventosCollectionPrepagoEventos = em.merge(oldFkAmyTipoEventosOfPrepagoEventosCollectionPrepagoEventos);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAmyTipoEventos(amyTipoEventos.getIdTipoEvento()) != null) {
                throw new PreexistingEntityException("AmyTipoEventos " + amyTipoEventos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AmyTipoEventos amyTipoEventos) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AmyTipoEventos persistentAmyTipoEventos = em.find(AmyTipoEventos.class, amyTipoEventos.getIdTipoEvento());
            Collection<AmyEventos> amyEventosCollectionOld = persistentAmyTipoEventos.getAmyEventosCollection();
            Collection<AmyEventos> amyEventosCollectionNew = amyTipoEventos.getAmyEventosCollection();
            Collection<PrepagoEventos> prepagoEventosCollectionOld = persistentAmyTipoEventos.getPrepagoEventosCollection();
            Collection<PrepagoEventos> prepagoEventosCollectionNew = amyTipoEventos.getPrepagoEventosCollection();
            List<String> illegalOrphanMessages = null;
            for (AmyEventos amyEventosCollectionOldAmyEventos : amyEventosCollectionOld) {
                if (!amyEventosCollectionNew.contains(amyEventosCollectionOldAmyEventos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyEventos " + amyEventosCollectionOldAmyEventos + " since its fkAmyTipoEventos field is not nullable.");
                }
            }
            for (PrepagoEventos prepagoEventosCollectionOldPrepagoEventos : prepagoEventosCollectionOld) {
                if (!prepagoEventosCollectionNew.contains(prepagoEventosCollectionOldPrepagoEventos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PrepagoEventos " + prepagoEventosCollectionOldPrepagoEventos + " since its fkAmyTipoEventos field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<AmyEventos> attachedAmyEventosCollectionNew = new ArrayList<AmyEventos>();
            for (AmyEventos amyEventosCollectionNewAmyEventosToAttach : amyEventosCollectionNew) {
                amyEventosCollectionNewAmyEventosToAttach = em.getReference(amyEventosCollectionNewAmyEventosToAttach.getClass(), amyEventosCollectionNewAmyEventosToAttach.getIdEvento());
                attachedAmyEventosCollectionNew.add(amyEventosCollectionNewAmyEventosToAttach);
            }
            amyEventosCollectionNew = attachedAmyEventosCollectionNew;
            amyTipoEventos.setAmyEventosCollection(amyEventosCollectionNew);
            Collection<PrepagoEventos> attachedPrepagoEventosCollectionNew = new ArrayList<PrepagoEventos>();
            for (PrepagoEventos prepagoEventosCollectionNewPrepagoEventosToAttach : prepagoEventosCollectionNew) {
                prepagoEventosCollectionNewPrepagoEventosToAttach = em.getReference(prepagoEventosCollectionNewPrepagoEventosToAttach.getClass(), prepagoEventosCollectionNewPrepagoEventosToAttach.getIdEventos());
                attachedPrepagoEventosCollectionNew.add(prepagoEventosCollectionNewPrepagoEventosToAttach);
            }
            prepagoEventosCollectionNew = attachedPrepagoEventosCollectionNew;
            amyTipoEventos.setPrepagoEventosCollection(prepagoEventosCollectionNew);
            amyTipoEventos = em.merge(amyTipoEventos);
            for (AmyEventos amyEventosCollectionNewAmyEventos : amyEventosCollectionNew) {
                if (!amyEventosCollectionOld.contains(amyEventosCollectionNewAmyEventos)) {
                    AmyTipoEventos oldFkAmyTipoEventosOfAmyEventosCollectionNewAmyEventos = amyEventosCollectionNewAmyEventos.getFkAmyTipoEventos();
                    amyEventosCollectionNewAmyEventos.setFkAmyTipoEventos(amyTipoEventos);
                    amyEventosCollectionNewAmyEventos = em.merge(amyEventosCollectionNewAmyEventos);
                    if (oldFkAmyTipoEventosOfAmyEventosCollectionNewAmyEventos != null && !oldFkAmyTipoEventosOfAmyEventosCollectionNewAmyEventos.equals(amyTipoEventos)) {
                        oldFkAmyTipoEventosOfAmyEventosCollectionNewAmyEventos.getAmyEventosCollection().remove(amyEventosCollectionNewAmyEventos);
                        oldFkAmyTipoEventosOfAmyEventosCollectionNewAmyEventos = em.merge(oldFkAmyTipoEventosOfAmyEventosCollectionNewAmyEventos);
                    }
                }
            }
            for (PrepagoEventos prepagoEventosCollectionNewPrepagoEventos : prepagoEventosCollectionNew) {
                if (!prepagoEventosCollectionOld.contains(prepagoEventosCollectionNewPrepagoEventos)) {
                    AmyTipoEventos oldFkAmyTipoEventosOfPrepagoEventosCollectionNewPrepagoEventos = prepagoEventosCollectionNewPrepagoEventos.getFkAmyTipoEventos();
                    prepagoEventosCollectionNewPrepagoEventos.setFkAmyTipoEventos(amyTipoEventos);
                    prepagoEventosCollectionNewPrepagoEventos = em.merge(prepagoEventosCollectionNewPrepagoEventos);
                    if (oldFkAmyTipoEventosOfPrepagoEventosCollectionNewPrepagoEventos != null && !oldFkAmyTipoEventosOfPrepagoEventosCollectionNewPrepagoEventos.equals(amyTipoEventos)) {
                        oldFkAmyTipoEventosOfPrepagoEventosCollectionNewPrepagoEventos.getPrepagoEventosCollection().remove(prepagoEventosCollectionNewPrepagoEventos);
                        oldFkAmyTipoEventosOfPrepagoEventosCollectionNewPrepagoEventos = em.merge(oldFkAmyTipoEventosOfPrepagoEventosCollectionNewPrepagoEventos);
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
                Long id = amyTipoEventos.getIdTipoEvento();
                if (findAmyTipoEventos(id) == null) {
                    throw new NonexistentEntityException("The amyTipoEventos with id " + id + " no longer exists.");
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
            AmyTipoEventos amyTipoEventos;
            try {
                amyTipoEventos = em.getReference(AmyTipoEventos.class, id);
                amyTipoEventos.getIdTipoEvento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The amyTipoEventos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<AmyEventos> amyEventosCollectionOrphanCheck = amyTipoEventos.getAmyEventosCollection();
            for (AmyEventos amyEventosCollectionOrphanCheckAmyEventos : amyEventosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyTipoEventos (" + amyTipoEventos + ") cannot be destroyed since the AmyEventos " + amyEventosCollectionOrphanCheckAmyEventos + " in its amyEventosCollection field has a non-nullable fkAmyTipoEventos field.");
            }
            Collection<PrepagoEventos> prepagoEventosCollectionOrphanCheck = amyTipoEventos.getPrepagoEventosCollection();
            for (PrepagoEventos prepagoEventosCollectionOrphanCheckPrepagoEventos : prepagoEventosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyTipoEventos (" + amyTipoEventos + ") cannot be destroyed since the PrepagoEventos " + prepagoEventosCollectionOrphanCheckPrepagoEventos + " in its prepagoEventosCollection field has a non-nullable fkAmyTipoEventos field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(amyTipoEventos);
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

    public List<AmyTipoEventos> findAmyTipoEventosEntities() {
        return findAmyTipoEventosEntities(true, -1, -1);
    }

    public List<AmyTipoEventos> findAmyTipoEventosEntities(int maxResults, int firstResult) {
        return findAmyTipoEventosEntities(false, maxResults, firstResult);
    }

    private List<AmyTipoEventos> findAmyTipoEventosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AmyTipoEventos.class));
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

    public AmyTipoEventos findAmyTipoEventos(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AmyTipoEventos.class, id);
        } finally {
            em.close();
        }
    }

    public int getAmyTipoEventosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AmyTipoEventos> rt = cq.from(AmyTipoEventos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
