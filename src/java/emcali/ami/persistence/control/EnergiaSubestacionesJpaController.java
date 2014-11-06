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
import emcali.ami.persistence.entity.EnergiaCircuitos;
import emcali.ami.persistence.entity.EnergiaSubestaciones;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class EnergiaSubestacionesJpaController implements Serializable {

    public EnergiaSubestacionesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EnergiaSubestaciones energiaSubestaciones) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (energiaSubestaciones.getEnergiaCircuitosCollection() == null) {
            energiaSubestaciones.setEnergiaCircuitosCollection(new ArrayList<EnergiaCircuitos>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<EnergiaCircuitos> attachedEnergiaCircuitosCollection = new ArrayList<EnergiaCircuitos>();
            for (EnergiaCircuitos energiaCircuitosCollectionEnergiaCircuitosToAttach : energiaSubestaciones.getEnergiaCircuitosCollection()) {
                energiaCircuitosCollectionEnergiaCircuitosToAttach = em.getReference(energiaCircuitosCollectionEnergiaCircuitosToAttach.getClass(), energiaCircuitosCollectionEnergiaCircuitosToAttach.getIdCircuitos());
                attachedEnergiaCircuitosCollection.add(energiaCircuitosCollectionEnergiaCircuitosToAttach);
            }
            energiaSubestaciones.setEnergiaCircuitosCollection(attachedEnergiaCircuitosCollection);
            em.persist(energiaSubestaciones);
            for (EnergiaCircuitos energiaCircuitosCollectionEnergiaCircuitos : energiaSubestaciones.getEnergiaCircuitosCollection()) {
                EnergiaSubestaciones oldFkEnergiaSubestacionesOfEnergiaCircuitosCollectionEnergiaCircuitos = energiaCircuitosCollectionEnergiaCircuitos.getFkEnergiaSubestaciones();
                energiaCircuitosCollectionEnergiaCircuitos.setFkEnergiaSubestaciones(energiaSubestaciones);
                energiaCircuitosCollectionEnergiaCircuitos = em.merge(energiaCircuitosCollectionEnergiaCircuitos);
                if (oldFkEnergiaSubestacionesOfEnergiaCircuitosCollectionEnergiaCircuitos != null) {
                    oldFkEnergiaSubestacionesOfEnergiaCircuitosCollectionEnergiaCircuitos.getEnergiaCircuitosCollection().remove(energiaCircuitosCollectionEnergiaCircuitos);
                    oldFkEnergiaSubestacionesOfEnergiaCircuitosCollectionEnergiaCircuitos = em.merge(oldFkEnergiaSubestacionesOfEnergiaCircuitosCollectionEnergiaCircuitos);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEnergiaSubestaciones(energiaSubestaciones.getIdSubestaciones()) != null) {
                throw new PreexistingEntityException("EnergiaSubestaciones " + energiaSubestaciones + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EnergiaSubestaciones energiaSubestaciones) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            EnergiaSubestaciones persistentEnergiaSubestaciones = em.find(EnergiaSubestaciones.class, energiaSubestaciones.getIdSubestaciones());
            Collection<EnergiaCircuitos> energiaCircuitosCollectionOld = persistentEnergiaSubestaciones.getEnergiaCircuitosCollection();
            Collection<EnergiaCircuitos> energiaCircuitosCollectionNew = energiaSubestaciones.getEnergiaCircuitosCollection();
            List<String> illegalOrphanMessages = null;
            for (EnergiaCircuitos energiaCircuitosCollectionOldEnergiaCircuitos : energiaCircuitosCollectionOld) {
                if (!energiaCircuitosCollectionNew.contains(energiaCircuitosCollectionOldEnergiaCircuitos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EnergiaCircuitos " + energiaCircuitosCollectionOldEnergiaCircuitos + " since its fkEnergiaSubestaciones field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<EnergiaCircuitos> attachedEnergiaCircuitosCollectionNew = new ArrayList<EnergiaCircuitos>();
            for (EnergiaCircuitos energiaCircuitosCollectionNewEnergiaCircuitosToAttach : energiaCircuitosCollectionNew) {
                energiaCircuitosCollectionNewEnergiaCircuitosToAttach = em.getReference(energiaCircuitosCollectionNewEnergiaCircuitosToAttach.getClass(), energiaCircuitosCollectionNewEnergiaCircuitosToAttach.getIdCircuitos());
                attachedEnergiaCircuitosCollectionNew.add(energiaCircuitosCollectionNewEnergiaCircuitosToAttach);
            }
            energiaCircuitosCollectionNew = attachedEnergiaCircuitosCollectionNew;
            energiaSubestaciones.setEnergiaCircuitosCollection(energiaCircuitosCollectionNew);
            energiaSubestaciones = em.merge(energiaSubestaciones);
            for (EnergiaCircuitos energiaCircuitosCollectionNewEnergiaCircuitos : energiaCircuitosCollectionNew) {
                if (!energiaCircuitosCollectionOld.contains(energiaCircuitosCollectionNewEnergiaCircuitos)) {
                    EnergiaSubestaciones oldFkEnergiaSubestacionesOfEnergiaCircuitosCollectionNewEnergiaCircuitos = energiaCircuitosCollectionNewEnergiaCircuitos.getFkEnergiaSubestaciones();
                    energiaCircuitosCollectionNewEnergiaCircuitos.setFkEnergiaSubestaciones(energiaSubestaciones);
                    energiaCircuitosCollectionNewEnergiaCircuitos = em.merge(energiaCircuitosCollectionNewEnergiaCircuitos);
                    if (oldFkEnergiaSubestacionesOfEnergiaCircuitosCollectionNewEnergiaCircuitos != null && !oldFkEnergiaSubestacionesOfEnergiaCircuitosCollectionNewEnergiaCircuitos.equals(energiaSubestaciones)) {
                        oldFkEnergiaSubestacionesOfEnergiaCircuitosCollectionNewEnergiaCircuitos.getEnergiaCircuitosCollection().remove(energiaCircuitosCollectionNewEnergiaCircuitos);
                        oldFkEnergiaSubestacionesOfEnergiaCircuitosCollectionNewEnergiaCircuitos = em.merge(oldFkEnergiaSubestacionesOfEnergiaCircuitosCollectionNewEnergiaCircuitos);
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
                Long id = energiaSubestaciones.getIdSubestaciones();
                if (findEnergiaSubestaciones(id) == null) {
                    throw new NonexistentEntityException("The energiaSubestaciones with id " + id + " no longer exists.");
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
            EnergiaSubestaciones energiaSubestaciones;
            try {
                energiaSubestaciones = em.getReference(EnergiaSubestaciones.class, id);
                energiaSubestaciones.getIdSubestaciones();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The energiaSubestaciones with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<EnergiaCircuitos> energiaCircuitosCollectionOrphanCheck = energiaSubestaciones.getEnergiaCircuitosCollection();
            for (EnergiaCircuitos energiaCircuitosCollectionOrphanCheckEnergiaCircuitos : energiaCircuitosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EnergiaSubestaciones (" + energiaSubestaciones + ") cannot be destroyed since the EnergiaCircuitos " + energiaCircuitosCollectionOrphanCheckEnergiaCircuitos + " in its energiaCircuitosCollection field has a non-nullable fkEnergiaSubestaciones field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(energiaSubestaciones);
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

    public List<EnergiaSubestaciones> findEnergiaSubestacionesEntities() {
        return findEnergiaSubestacionesEntities(true, -1, -1);
    }

    public List<EnergiaSubestaciones> findEnergiaSubestacionesEntities(int maxResults, int firstResult) {
        return findEnergiaSubestacionesEntities(false, maxResults, firstResult);
    }

    private List<EnergiaSubestaciones> findEnergiaSubestacionesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EnergiaSubestaciones.class));
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

    public EnergiaSubestaciones findEnergiaSubestaciones(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EnergiaSubestaciones.class, id);
        } finally {
            em.close();
        }
    }

    public int getEnergiaSubestacionesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EnergiaSubestaciones> rt = cq.from(EnergiaSubestaciones.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
