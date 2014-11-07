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
import emcali.ami.persistence.entity.AmyCajas;
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
public class EnergiaTransformadoresJpaController implements Serializable {

    public EnergiaTransformadoresJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EnergiaTransformadores energiaTransformadores) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (energiaTransformadores.getAmyCajasList() == null) {
            energiaTransformadores.setAmyCajasList(new ArrayList<AmyCajas>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            EnergiaCircuitos fkEnergiaCircuitos = energiaTransformadores.getFkEnergiaCircuitos();
            if (fkEnergiaCircuitos != null) {
                fkEnergiaCircuitos = em.getReference(fkEnergiaCircuitos.getClass(), fkEnergiaCircuitos.getIdCircuitos());
                energiaTransformadores.setFkEnergiaCircuitos(fkEnergiaCircuitos);
            }
            List<AmyCajas> attachedAmyCajasList = new ArrayList<AmyCajas>();
            for (AmyCajas amyCajasListAmyCajasToAttach : energiaTransformadores.getAmyCajasList()) {
                amyCajasListAmyCajasToAttach = em.getReference(amyCajasListAmyCajasToAttach.getClass(), amyCajasListAmyCajasToAttach.getIdCajas());
                attachedAmyCajasList.add(amyCajasListAmyCajasToAttach);
            }
            energiaTransformadores.setAmyCajasList(attachedAmyCajasList);
            em.persist(energiaTransformadores);
            if (fkEnergiaCircuitos != null) {
                fkEnergiaCircuitos.getEnergiaTransformadoresList().add(energiaTransformadores);
                fkEnergiaCircuitos = em.merge(fkEnergiaCircuitos);
            }
            for (AmyCajas amyCajasListAmyCajas : energiaTransformadores.getAmyCajasList()) {
                EnergiaTransformadores oldFkEnergiaTransformadoresOfAmyCajasListAmyCajas = amyCajasListAmyCajas.getFkEnergiaTransformadores();
                amyCajasListAmyCajas.setFkEnergiaTransformadores(energiaTransformadores);
                amyCajasListAmyCajas = em.merge(amyCajasListAmyCajas);
                if (oldFkEnergiaTransformadoresOfAmyCajasListAmyCajas != null) {
                    oldFkEnergiaTransformadoresOfAmyCajasListAmyCajas.getAmyCajasList().remove(amyCajasListAmyCajas);
                    oldFkEnergiaTransformadoresOfAmyCajasListAmyCajas = em.merge(oldFkEnergiaTransformadoresOfAmyCajasListAmyCajas);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEnergiaTransformadores(energiaTransformadores.getIdTransformadores()) != null) {
                throw new PreexistingEntityException("EnergiaTransformadores " + energiaTransformadores + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EnergiaTransformadores energiaTransformadores) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            EnergiaTransformadores persistentEnergiaTransformadores = em.find(EnergiaTransformadores.class, energiaTransformadores.getIdTransformadores());
            EnergiaCircuitos fkEnergiaCircuitosOld = persistentEnergiaTransformadores.getFkEnergiaCircuitos();
            EnergiaCircuitos fkEnergiaCircuitosNew = energiaTransformadores.getFkEnergiaCircuitos();
            List<AmyCajas> amyCajasListOld = persistentEnergiaTransformadores.getAmyCajasList();
            List<AmyCajas> amyCajasListNew = energiaTransformadores.getAmyCajasList();
            List<String> illegalOrphanMessages = null;
            for (AmyCajas amyCajasListOldAmyCajas : amyCajasListOld) {
                if (!amyCajasListNew.contains(amyCajasListOldAmyCajas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyCajas " + amyCajasListOldAmyCajas + " since its fkEnergiaTransformadores field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (fkEnergiaCircuitosNew != null) {
                fkEnergiaCircuitosNew = em.getReference(fkEnergiaCircuitosNew.getClass(), fkEnergiaCircuitosNew.getIdCircuitos());
                energiaTransformadores.setFkEnergiaCircuitos(fkEnergiaCircuitosNew);
            }
            List<AmyCajas> attachedAmyCajasListNew = new ArrayList<AmyCajas>();
            for (AmyCajas amyCajasListNewAmyCajasToAttach : amyCajasListNew) {
                amyCajasListNewAmyCajasToAttach = em.getReference(amyCajasListNewAmyCajasToAttach.getClass(), amyCajasListNewAmyCajasToAttach.getIdCajas());
                attachedAmyCajasListNew.add(amyCajasListNewAmyCajasToAttach);
            }
            amyCajasListNew = attachedAmyCajasListNew;
            energiaTransformadores.setAmyCajasList(amyCajasListNew);
            energiaTransformadores = em.merge(energiaTransformadores);
            if (fkEnergiaCircuitosOld != null && !fkEnergiaCircuitosOld.equals(fkEnergiaCircuitosNew)) {
                fkEnergiaCircuitosOld.getEnergiaTransformadoresList().remove(energiaTransformadores);
                fkEnergiaCircuitosOld = em.merge(fkEnergiaCircuitosOld);
            }
            if (fkEnergiaCircuitosNew != null && !fkEnergiaCircuitosNew.equals(fkEnergiaCircuitosOld)) {
                fkEnergiaCircuitosNew.getEnergiaTransformadoresList().add(energiaTransformadores);
                fkEnergiaCircuitosNew = em.merge(fkEnergiaCircuitosNew);
            }
            for (AmyCajas amyCajasListNewAmyCajas : amyCajasListNew) {
                if (!amyCajasListOld.contains(amyCajasListNewAmyCajas)) {
                    EnergiaTransformadores oldFkEnergiaTransformadoresOfAmyCajasListNewAmyCajas = amyCajasListNewAmyCajas.getFkEnergiaTransformadores();
                    amyCajasListNewAmyCajas.setFkEnergiaTransformadores(energiaTransformadores);
                    amyCajasListNewAmyCajas = em.merge(amyCajasListNewAmyCajas);
                    if (oldFkEnergiaTransformadoresOfAmyCajasListNewAmyCajas != null && !oldFkEnergiaTransformadoresOfAmyCajasListNewAmyCajas.equals(energiaTransformadores)) {
                        oldFkEnergiaTransformadoresOfAmyCajasListNewAmyCajas.getAmyCajasList().remove(amyCajasListNewAmyCajas);
                        oldFkEnergiaTransformadoresOfAmyCajasListNewAmyCajas = em.merge(oldFkEnergiaTransformadoresOfAmyCajasListNewAmyCajas);
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
                Long id = energiaTransformadores.getIdTransformadores();
                if (findEnergiaTransformadores(id) == null) {
                    throw new NonexistentEntityException("The energiaTransformadores with id " + id + " no longer exists.");
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
            EnergiaTransformadores energiaTransformadores;
            try {
                energiaTransformadores = em.getReference(EnergiaTransformadores.class, id);
                energiaTransformadores.getIdTransformadores();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The energiaTransformadores with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<AmyCajas> amyCajasListOrphanCheck = energiaTransformadores.getAmyCajasList();
            for (AmyCajas amyCajasListOrphanCheckAmyCajas : amyCajasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EnergiaTransformadores (" + energiaTransformadores + ") cannot be destroyed since the AmyCajas " + amyCajasListOrphanCheckAmyCajas + " in its amyCajasList field has a non-nullable fkEnergiaTransformadores field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            EnergiaCircuitos fkEnergiaCircuitos = energiaTransformadores.getFkEnergiaCircuitos();
            if (fkEnergiaCircuitos != null) {
                fkEnergiaCircuitos.getEnergiaTransformadoresList().remove(energiaTransformadores);
                fkEnergiaCircuitos = em.merge(fkEnergiaCircuitos);
            }
            em.remove(energiaTransformadores);
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

    public List<EnergiaTransformadores> findEnergiaTransformadoresEntities() {
        return findEnergiaTransformadoresEntities(true, -1, -1);
    }

    public List<EnergiaTransformadores> findEnergiaTransformadoresEntities(int maxResults, int firstResult) {
        return findEnergiaTransformadoresEntities(false, maxResults, firstResult);
    }

    private List<EnergiaTransformadores> findEnergiaTransformadoresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EnergiaTransformadores.class));
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

    public EnergiaTransformadores findEnergiaTransformadores(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EnergiaTransformadores.class, id);
        } finally {
            em.close();
        }
    }

    public int getEnergiaTransformadoresCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EnergiaTransformadores> rt = cq.from(EnergiaTransformadores.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
