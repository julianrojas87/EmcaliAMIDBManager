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
import emcali.ami.persistence.entity.EnergiaCircuitos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import emcali.ami.persistence.entity.EnergiaSubestaciones;
import emcali.ami.persistence.entity.EnergiaTransformadores;
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
public class EnergiaCircuitosJpaController implements Serializable {

    public EnergiaCircuitosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EnergiaCircuitos energiaCircuitos) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (energiaCircuitos.getEnergiaTransformadoresCollection() == null) {
            energiaCircuitos.setEnergiaTransformadoresCollection(new ArrayList<EnergiaTransformadores>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            EnergiaSubestaciones fkEnergiaSubestaciones = energiaCircuitos.getFkEnergiaSubestaciones();
            if (fkEnergiaSubestaciones != null) {
                fkEnergiaSubestaciones = em.getReference(fkEnergiaSubestaciones.getClass(), fkEnergiaSubestaciones.getIdSubestaciones());
                energiaCircuitos.setFkEnergiaSubestaciones(fkEnergiaSubestaciones);
            }
            Collection<EnergiaTransformadores> attachedEnergiaTransformadoresCollection = new ArrayList<EnergiaTransformadores>();
            for (EnergiaTransformadores energiaTransformadoresCollectionEnergiaTransformadoresToAttach : energiaCircuitos.getEnergiaTransformadoresCollection()) {
                energiaTransformadoresCollectionEnergiaTransformadoresToAttach = em.getReference(energiaTransformadoresCollectionEnergiaTransformadoresToAttach.getClass(), energiaTransformadoresCollectionEnergiaTransformadoresToAttach.getIdTransformadores());
                attachedEnergiaTransformadoresCollection.add(energiaTransformadoresCollectionEnergiaTransformadoresToAttach);
            }
            energiaCircuitos.setEnergiaTransformadoresCollection(attachedEnergiaTransformadoresCollection);
            em.persist(energiaCircuitos);
            if (fkEnergiaSubestaciones != null) {
                fkEnergiaSubestaciones.getEnergiaCircuitosCollection().add(energiaCircuitos);
                fkEnergiaSubestaciones = em.merge(fkEnergiaSubestaciones);
            }
            for (EnergiaTransformadores energiaTransformadoresCollectionEnergiaTransformadores : energiaCircuitos.getEnergiaTransformadoresCollection()) {
                EnergiaCircuitos oldFkEnergiaCircuitosOfEnergiaTransformadoresCollectionEnergiaTransformadores = energiaTransformadoresCollectionEnergiaTransformadores.getFkEnergiaCircuitos();
                energiaTransformadoresCollectionEnergiaTransformadores.setFkEnergiaCircuitos(energiaCircuitos);
                energiaTransformadoresCollectionEnergiaTransformadores = em.merge(energiaTransformadoresCollectionEnergiaTransformadores);
                if (oldFkEnergiaCircuitosOfEnergiaTransformadoresCollectionEnergiaTransformadores != null) {
                    oldFkEnergiaCircuitosOfEnergiaTransformadoresCollectionEnergiaTransformadores.getEnergiaTransformadoresCollection().remove(energiaTransformadoresCollectionEnergiaTransformadores);
                    oldFkEnergiaCircuitosOfEnergiaTransformadoresCollectionEnergiaTransformadores = em.merge(oldFkEnergiaCircuitosOfEnergiaTransformadoresCollectionEnergiaTransformadores);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEnergiaCircuitos(energiaCircuitos.getIdCircuitos()) != null) {
                throw new PreexistingEntityException("EnergiaCircuitos " + energiaCircuitos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EnergiaCircuitos energiaCircuitos) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            EnergiaCircuitos persistentEnergiaCircuitos = em.find(EnergiaCircuitos.class, energiaCircuitos.getIdCircuitos());
            EnergiaSubestaciones fkEnergiaSubestacionesOld = persistentEnergiaCircuitos.getFkEnergiaSubestaciones();
            EnergiaSubestaciones fkEnergiaSubestacionesNew = energiaCircuitos.getFkEnergiaSubestaciones();
            Collection<EnergiaTransformadores> energiaTransformadoresCollectionOld = persistentEnergiaCircuitos.getEnergiaTransformadoresCollection();
            Collection<EnergiaTransformadores> energiaTransformadoresCollectionNew = energiaCircuitos.getEnergiaTransformadoresCollection();
            List<String> illegalOrphanMessages = null;
            for (EnergiaTransformadores energiaTransformadoresCollectionOldEnergiaTransformadores : energiaTransformadoresCollectionOld) {
                if (!energiaTransformadoresCollectionNew.contains(energiaTransformadoresCollectionOldEnergiaTransformadores)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EnergiaTransformadores " + energiaTransformadoresCollectionOldEnergiaTransformadores + " since its fkEnergiaCircuitos field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (fkEnergiaSubestacionesNew != null) {
                fkEnergiaSubestacionesNew = em.getReference(fkEnergiaSubestacionesNew.getClass(), fkEnergiaSubestacionesNew.getIdSubestaciones());
                energiaCircuitos.setFkEnergiaSubestaciones(fkEnergiaSubestacionesNew);
            }
            Collection<EnergiaTransformadores> attachedEnergiaTransformadoresCollectionNew = new ArrayList<EnergiaTransformadores>();
            for (EnergiaTransformadores energiaTransformadoresCollectionNewEnergiaTransformadoresToAttach : energiaTransformadoresCollectionNew) {
                energiaTransformadoresCollectionNewEnergiaTransformadoresToAttach = em.getReference(energiaTransformadoresCollectionNewEnergiaTransformadoresToAttach.getClass(), energiaTransformadoresCollectionNewEnergiaTransformadoresToAttach.getIdTransformadores());
                attachedEnergiaTransformadoresCollectionNew.add(energiaTransformadoresCollectionNewEnergiaTransformadoresToAttach);
            }
            energiaTransformadoresCollectionNew = attachedEnergiaTransformadoresCollectionNew;
            energiaCircuitos.setEnergiaTransformadoresCollection(energiaTransformadoresCollectionNew);
            energiaCircuitos = em.merge(energiaCircuitos);
            if (fkEnergiaSubestacionesOld != null && !fkEnergiaSubestacionesOld.equals(fkEnergiaSubestacionesNew)) {
                fkEnergiaSubestacionesOld.getEnergiaCircuitosCollection().remove(energiaCircuitos);
                fkEnergiaSubestacionesOld = em.merge(fkEnergiaSubestacionesOld);
            }
            if (fkEnergiaSubestacionesNew != null && !fkEnergiaSubestacionesNew.equals(fkEnergiaSubestacionesOld)) {
                fkEnergiaSubestacionesNew.getEnergiaCircuitosCollection().add(energiaCircuitos);
                fkEnergiaSubestacionesNew = em.merge(fkEnergiaSubestacionesNew);
            }
            for (EnergiaTransformadores energiaTransformadoresCollectionNewEnergiaTransformadores : energiaTransformadoresCollectionNew) {
                if (!energiaTransformadoresCollectionOld.contains(energiaTransformadoresCollectionNewEnergiaTransformadores)) {
                    EnergiaCircuitos oldFkEnergiaCircuitosOfEnergiaTransformadoresCollectionNewEnergiaTransformadores = energiaTransformadoresCollectionNewEnergiaTransformadores.getFkEnergiaCircuitos();
                    energiaTransformadoresCollectionNewEnergiaTransformadores.setFkEnergiaCircuitos(energiaCircuitos);
                    energiaTransformadoresCollectionNewEnergiaTransformadores = em.merge(energiaTransformadoresCollectionNewEnergiaTransformadores);
                    if (oldFkEnergiaCircuitosOfEnergiaTransformadoresCollectionNewEnergiaTransformadores != null && !oldFkEnergiaCircuitosOfEnergiaTransformadoresCollectionNewEnergiaTransformadores.equals(energiaCircuitos)) {
                        oldFkEnergiaCircuitosOfEnergiaTransformadoresCollectionNewEnergiaTransformadores.getEnergiaTransformadoresCollection().remove(energiaTransformadoresCollectionNewEnergiaTransformadores);
                        oldFkEnergiaCircuitosOfEnergiaTransformadoresCollectionNewEnergiaTransformadores = em.merge(oldFkEnergiaCircuitosOfEnergiaTransformadoresCollectionNewEnergiaTransformadores);
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
                Long id = energiaCircuitos.getIdCircuitos();
                if (findEnergiaCircuitos(id) == null) {
                    throw new NonexistentEntityException("The energiaCircuitos with id " + id + " no longer exists.");
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
            EnergiaCircuitos energiaCircuitos;
            try {
                energiaCircuitos = em.getReference(EnergiaCircuitos.class, id);
                energiaCircuitos.getIdCircuitos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The energiaCircuitos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<EnergiaTransformadores> energiaTransformadoresCollectionOrphanCheck = energiaCircuitos.getEnergiaTransformadoresCollection();
            for (EnergiaTransformadores energiaTransformadoresCollectionOrphanCheckEnergiaTransformadores : energiaTransformadoresCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EnergiaCircuitos (" + energiaCircuitos + ") cannot be destroyed since the EnergiaTransformadores " + energiaTransformadoresCollectionOrphanCheckEnergiaTransformadores + " in its energiaTransformadoresCollection field has a non-nullable fkEnergiaCircuitos field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            EnergiaSubestaciones fkEnergiaSubestaciones = energiaCircuitos.getFkEnergiaSubestaciones();
            if (fkEnergiaSubestaciones != null) {
                fkEnergiaSubestaciones.getEnergiaCircuitosCollection().remove(energiaCircuitos);
                fkEnergiaSubestaciones = em.merge(fkEnergiaSubestaciones);
            }
            em.remove(energiaCircuitos);
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

    public List<EnergiaCircuitos> findEnergiaCircuitosEntities() {
        return findEnergiaCircuitosEntities(true, -1, -1);
    }

    public List<EnergiaCircuitos> findEnergiaCircuitosEntities(int maxResults, int firstResult) {
        return findEnergiaCircuitosEntities(false, maxResults, firstResult);
    }

    private List<EnergiaCircuitos> findEnergiaCircuitosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EnergiaCircuitos.class));
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

    public EnergiaCircuitos findEnergiaCircuitos(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EnergiaCircuitos.class, id);
        } finally {
            em.close();
        }
    }

    public int getEnergiaCircuitosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EnergiaCircuitos> rt = cq.from(EnergiaCircuitos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
