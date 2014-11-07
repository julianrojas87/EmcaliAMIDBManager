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
        if (energiaCircuitos.getEnergiaTransformadoresList() == null) {
            energiaCircuitos.setEnergiaTransformadoresList(new ArrayList<EnergiaTransformadores>());
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
            List<EnergiaTransformadores> attachedEnergiaTransformadoresList = new ArrayList<EnergiaTransformadores>();
            for (EnergiaTransformadores energiaTransformadoresListEnergiaTransformadoresToAttach : energiaCircuitos.getEnergiaTransformadoresList()) {
                energiaTransformadoresListEnergiaTransformadoresToAttach = em.getReference(energiaTransformadoresListEnergiaTransformadoresToAttach.getClass(), energiaTransformadoresListEnergiaTransformadoresToAttach.getIdTransformadores());
                attachedEnergiaTransformadoresList.add(energiaTransformadoresListEnergiaTransformadoresToAttach);
            }
            energiaCircuitos.setEnergiaTransformadoresList(attachedEnergiaTransformadoresList);
            em.persist(energiaCircuitos);
            if (fkEnergiaSubestaciones != null) {
                fkEnergiaSubestaciones.getEnergiaCircuitosList().add(energiaCircuitos);
                fkEnergiaSubestaciones = em.merge(fkEnergiaSubestaciones);
            }
            for (EnergiaTransformadores energiaTransformadoresListEnergiaTransformadores : energiaCircuitos.getEnergiaTransformadoresList()) {
                EnergiaCircuitos oldFkEnergiaCircuitosOfEnergiaTransformadoresListEnergiaTransformadores = energiaTransformadoresListEnergiaTransformadores.getFkEnergiaCircuitos();
                energiaTransformadoresListEnergiaTransformadores.setFkEnergiaCircuitos(energiaCircuitos);
                energiaTransformadoresListEnergiaTransformadores = em.merge(energiaTransformadoresListEnergiaTransformadores);
                if (oldFkEnergiaCircuitosOfEnergiaTransformadoresListEnergiaTransformadores != null) {
                    oldFkEnergiaCircuitosOfEnergiaTransformadoresListEnergiaTransformadores.getEnergiaTransformadoresList().remove(energiaTransformadoresListEnergiaTransformadores);
                    oldFkEnergiaCircuitosOfEnergiaTransformadoresListEnergiaTransformadores = em.merge(oldFkEnergiaCircuitosOfEnergiaTransformadoresListEnergiaTransformadores);
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
            List<EnergiaTransformadores> energiaTransformadoresListOld = persistentEnergiaCircuitos.getEnergiaTransformadoresList();
            List<EnergiaTransformadores> energiaTransformadoresListNew = energiaCircuitos.getEnergiaTransformadoresList();
            List<String> illegalOrphanMessages = null;
            for (EnergiaTransformadores energiaTransformadoresListOldEnergiaTransformadores : energiaTransformadoresListOld) {
                if (!energiaTransformadoresListNew.contains(energiaTransformadoresListOldEnergiaTransformadores)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EnergiaTransformadores " + energiaTransformadoresListOldEnergiaTransformadores + " since its fkEnergiaCircuitos field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (fkEnergiaSubestacionesNew != null) {
                fkEnergiaSubestacionesNew = em.getReference(fkEnergiaSubestacionesNew.getClass(), fkEnergiaSubestacionesNew.getIdSubestaciones());
                energiaCircuitos.setFkEnergiaSubestaciones(fkEnergiaSubestacionesNew);
            }
            List<EnergiaTransformadores> attachedEnergiaTransformadoresListNew = new ArrayList<EnergiaTransformadores>();
            for (EnergiaTransformadores energiaTransformadoresListNewEnergiaTransformadoresToAttach : energiaTransformadoresListNew) {
                energiaTransformadoresListNewEnergiaTransformadoresToAttach = em.getReference(energiaTransformadoresListNewEnergiaTransformadoresToAttach.getClass(), energiaTransformadoresListNewEnergiaTransformadoresToAttach.getIdTransformadores());
                attachedEnergiaTransformadoresListNew.add(energiaTransformadoresListNewEnergiaTransformadoresToAttach);
            }
            energiaTransformadoresListNew = attachedEnergiaTransformadoresListNew;
            energiaCircuitos.setEnergiaTransformadoresList(energiaTransformadoresListNew);
            energiaCircuitos = em.merge(energiaCircuitos);
            if (fkEnergiaSubestacionesOld != null && !fkEnergiaSubestacionesOld.equals(fkEnergiaSubestacionesNew)) {
                fkEnergiaSubestacionesOld.getEnergiaCircuitosList().remove(energiaCircuitos);
                fkEnergiaSubestacionesOld = em.merge(fkEnergiaSubestacionesOld);
            }
            if (fkEnergiaSubestacionesNew != null && !fkEnergiaSubestacionesNew.equals(fkEnergiaSubestacionesOld)) {
                fkEnergiaSubestacionesNew.getEnergiaCircuitosList().add(energiaCircuitos);
                fkEnergiaSubestacionesNew = em.merge(fkEnergiaSubestacionesNew);
            }
            for (EnergiaTransformadores energiaTransformadoresListNewEnergiaTransformadores : energiaTransformadoresListNew) {
                if (!energiaTransformadoresListOld.contains(energiaTransformadoresListNewEnergiaTransformadores)) {
                    EnergiaCircuitos oldFkEnergiaCircuitosOfEnergiaTransformadoresListNewEnergiaTransformadores = energiaTransformadoresListNewEnergiaTransformadores.getFkEnergiaCircuitos();
                    energiaTransformadoresListNewEnergiaTransformadores.setFkEnergiaCircuitos(energiaCircuitos);
                    energiaTransformadoresListNewEnergiaTransformadores = em.merge(energiaTransformadoresListNewEnergiaTransformadores);
                    if (oldFkEnergiaCircuitosOfEnergiaTransformadoresListNewEnergiaTransformadores != null && !oldFkEnergiaCircuitosOfEnergiaTransformadoresListNewEnergiaTransformadores.equals(energiaCircuitos)) {
                        oldFkEnergiaCircuitosOfEnergiaTransformadoresListNewEnergiaTransformadores.getEnergiaTransformadoresList().remove(energiaTransformadoresListNewEnergiaTransformadores);
                        oldFkEnergiaCircuitosOfEnergiaTransformadoresListNewEnergiaTransformadores = em.merge(oldFkEnergiaCircuitosOfEnergiaTransformadoresListNewEnergiaTransformadores);
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
            List<EnergiaTransformadores> energiaTransformadoresListOrphanCheck = energiaCircuitos.getEnergiaTransformadoresList();
            for (EnergiaTransformadores energiaTransformadoresListOrphanCheckEnergiaTransformadores : energiaTransformadoresListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EnergiaCircuitos (" + energiaCircuitos + ") cannot be destroyed since the EnergiaTransformadores " + energiaTransformadoresListOrphanCheckEnergiaTransformadores + " in its energiaTransformadoresList field has a non-nullable fkEnergiaCircuitos field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            EnergiaSubestaciones fkEnergiaSubestaciones = energiaCircuitos.getFkEnergiaSubestaciones();
            if (fkEnergiaSubestaciones != null) {
                fkEnergiaSubestaciones.getEnergiaCircuitosList().remove(energiaCircuitos);
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
