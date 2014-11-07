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
        if (energiaSubestaciones.getEnergiaCircuitosList() == null) {
            energiaSubestaciones.setEnergiaCircuitosList(new ArrayList<EnergiaCircuitos>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<EnergiaCircuitos> attachedEnergiaCircuitosList = new ArrayList<EnergiaCircuitos>();
            for (EnergiaCircuitos energiaCircuitosListEnergiaCircuitosToAttach : energiaSubestaciones.getEnergiaCircuitosList()) {
                energiaCircuitosListEnergiaCircuitosToAttach = em.getReference(energiaCircuitosListEnergiaCircuitosToAttach.getClass(), energiaCircuitosListEnergiaCircuitosToAttach.getIdCircuitos());
                attachedEnergiaCircuitosList.add(energiaCircuitosListEnergiaCircuitosToAttach);
            }
            energiaSubestaciones.setEnergiaCircuitosList(attachedEnergiaCircuitosList);
            em.persist(energiaSubestaciones);
            for (EnergiaCircuitos energiaCircuitosListEnergiaCircuitos : energiaSubestaciones.getEnergiaCircuitosList()) {
                EnergiaSubestaciones oldFkEnergiaSubestacionesOfEnergiaCircuitosListEnergiaCircuitos = energiaCircuitosListEnergiaCircuitos.getFkEnergiaSubestaciones();
                energiaCircuitosListEnergiaCircuitos.setFkEnergiaSubestaciones(energiaSubestaciones);
                energiaCircuitosListEnergiaCircuitos = em.merge(energiaCircuitosListEnergiaCircuitos);
                if (oldFkEnergiaSubestacionesOfEnergiaCircuitosListEnergiaCircuitos != null) {
                    oldFkEnergiaSubestacionesOfEnergiaCircuitosListEnergiaCircuitos.getEnergiaCircuitosList().remove(energiaCircuitosListEnergiaCircuitos);
                    oldFkEnergiaSubestacionesOfEnergiaCircuitosListEnergiaCircuitos = em.merge(oldFkEnergiaSubestacionesOfEnergiaCircuitosListEnergiaCircuitos);
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
            List<EnergiaCircuitos> energiaCircuitosListOld = persistentEnergiaSubestaciones.getEnergiaCircuitosList();
            List<EnergiaCircuitos> energiaCircuitosListNew = energiaSubestaciones.getEnergiaCircuitosList();
            List<String> illegalOrphanMessages = null;
            for (EnergiaCircuitos energiaCircuitosListOldEnergiaCircuitos : energiaCircuitosListOld) {
                if (!energiaCircuitosListNew.contains(energiaCircuitosListOldEnergiaCircuitos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EnergiaCircuitos " + energiaCircuitosListOldEnergiaCircuitos + " since its fkEnergiaSubestaciones field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<EnergiaCircuitos> attachedEnergiaCircuitosListNew = new ArrayList<EnergiaCircuitos>();
            for (EnergiaCircuitos energiaCircuitosListNewEnergiaCircuitosToAttach : energiaCircuitosListNew) {
                energiaCircuitosListNewEnergiaCircuitosToAttach = em.getReference(energiaCircuitosListNewEnergiaCircuitosToAttach.getClass(), energiaCircuitosListNewEnergiaCircuitosToAttach.getIdCircuitos());
                attachedEnergiaCircuitosListNew.add(energiaCircuitosListNewEnergiaCircuitosToAttach);
            }
            energiaCircuitosListNew = attachedEnergiaCircuitosListNew;
            energiaSubestaciones.setEnergiaCircuitosList(energiaCircuitosListNew);
            energiaSubestaciones = em.merge(energiaSubestaciones);
            for (EnergiaCircuitos energiaCircuitosListNewEnergiaCircuitos : energiaCircuitosListNew) {
                if (!energiaCircuitosListOld.contains(energiaCircuitosListNewEnergiaCircuitos)) {
                    EnergiaSubestaciones oldFkEnergiaSubestacionesOfEnergiaCircuitosListNewEnergiaCircuitos = energiaCircuitosListNewEnergiaCircuitos.getFkEnergiaSubestaciones();
                    energiaCircuitosListNewEnergiaCircuitos.setFkEnergiaSubestaciones(energiaSubestaciones);
                    energiaCircuitosListNewEnergiaCircuitos = em.merge(energiaCircuitosListNewEnergiaCircuitos);
                    if (oldFkEnergiaSubestacionesOfEnergiaCircuitosListNewEnergiaCircuitos != null && !oldFkEnergiaSubestacionesOfEnergiaCircuitosListNewEnergiaCircuitos.equals(energiaSubestaciones)) {
                        oldFkEnergiaSubestacionesOfEnergiaCircuitosListNewEnergiaCircuitos.getEnergiaCircuitosList().remove(energiaCircuitosListNewEnergiaCircuitos);
                        oldFkEnergiaSubestacionesOfEnergiaCircuitosListNewEnergiaCircuitos = em.merge(oldFkEnergiaSubestacionesOfEnergiaCircuitosListNewEnergiaCircuitos);
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
            List<EnergiaCircuitos> energiaCircuitosListOrphanCheck = energiaSubestaciones.getEnergiaCircuitosList();
            for (EnergiaCircuitos energiaCircuitosListOrphanCheckEnergiaCircuitos : energiaCircuitosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EnergiaSubestaciones (" + energiaSubestaciones + ") cannot be destroyed since the EnergiaCircuitos " + energiaCircuitosListOrphanCheckEnergiaCircuitos + " in its energiaCircuitosList field has a non-nullable fkEnergiaSubestaciones field.");
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
