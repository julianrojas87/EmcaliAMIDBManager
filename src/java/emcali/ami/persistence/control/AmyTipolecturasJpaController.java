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
import emcali.ami.persistence.entity.ConsumosConsumos;
import emcali.ami.persistence.entity.AmyConsumos;
import emcali.ami.persistence.entity.AmyTipolecturas;
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
        if (amyTipolecturas.getAmyLecturasList() == null) {
            amyTipolecturas.setAmyLecturasList(new ArrayList<AmyLecturas>());
        }
        if (amyTipolecturas.getConsumosConsumosList() == null) {
            amyTipolecturas.setConsumosConsumosList(new ArrayList<ConsumosConsumos>());
        }
        if (amyTipolecturas.getAmyConsumosList() == null) {
            amyTipolecturas.setAmyConsumosList(new ArrayList<AmyConsumos>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<AmyLecturas> attachedAmyLecturasList = new ArrayList<AmyLecturas>();
            for (AmyLecturas amyLecturasListAmyLecturasToAttach : amyTipolecturas.getAmyLecturasList()) {
                amyLecturasListAmyLecturasToAttach = em.getReference(amyLecturasListAmyLecturasToAttach.getClass(), amyLecturasListAmyLecturasToAttach.getIdLecturas());
                attachedAmyLecturasList.add(amyLecturasListAmyLecturasToAttach);
            }
            amyTipolecturas.setAmyLecturasList(attachedAmyLecturasList);
            List<ConsumosConsumos> attachedConsumosConsumosList = new ArrayList<ConsumosConsumos>();
            for (ConsumosConsumos consumosConsumosListConsumosConsumosToAttach : amyTipolecturas.getConsumosConsumosList()) {
                consumosConsumosListConsumosConsumosToAttach = em.getReference(consumosConsumosListConsumosConsumosToAttach.getClass(), consumosConsumosListConsumosConsumosToAttach.getIdConsumo());
                attachedConsumosConsumosList.add(consumosConsumosListConsumosConsumosToAttach);
            }
            amyTipolecturas.setConsumosConsumosList(attachedConsumosConsumosList);
            List<AmyConsumos> attachedAmyConsumosList = new ArrayList<AmyConsumos>();
            for (AmyConsumos amyConsumosListAmyConsumosToAttach : amyTipolecturas.getAmyConsumosList()) {
                amyConsumosListAmyConsumosToAttach = em.getReference(amyConsumosListAmyConsumosToAttach.getClass(), amyConsumosListAmyConsumosToAttach.getIdConsumo());
                attachedAmyConsumosList.add(amyConsumosListAmyConsumosToAttach);
            }
            amyTipolecturas.setAmyConsumosList(attachedAmyConsumosList);
            em.persist(amyTipolecturas);
            for (AmyLecturas amyLecturasListAmyLecturas : amyTipolecturas.getAmyLecturasList()) {
                AmyTipolecturas oldFkAmyTipolecturasOfAmyLecturasListAmyLecturas = amyLecturasListAmyLecturas.getFkAmyTipolecturas();
                amyLecturasListAmyLecturas.setFkAmyTipolecturas(amyTipolecturas);
                amyLecturasListAmyLecturas = em.merge(amyLecturasListAmyLecturas);
                if (oldFkAmyTipolecturasOfAmyLecturasListAmyLecturas != null) {
                    oldFkAmyTipolecturasOfAmyLecturasListAmyLecturas.getAmyLecturasList().remove(amyLecturasListAmyLecturas);
                    oldFkAmyTipolecturasOfAmyLecturasListAmyLecturas = em.merge(oldFkAmyTipolecturasOfAmyLecturasListAmyLecturas);
                }
            }
            for (ConsumosConsumos consumosConsumosListConsumosConsumos : amyTipolecturas.getConsumosConsumosList()) {
                AmyTipolecturas oldFkAmyTipolecturasOfConsumosConsumosListConsumosConsumos = consumosConsumosListConsumosConsumos.getFkAmyTipolecturas();
                consumosConsumosListConsumosConsumos.setFkAmyTipolecturas(amyTipolecturas);
                consumosConsumosListConsumosConsumos = em.merge(consumosConsumosListConsumosConsumos);
                if (oldFkAmyTipolecturasOfConsumosConsumosListConsumosConsumos != null) {
                    oldFkAmyTipolecturasOfConsumosConsumosListConsumosConsumos.getConsumosConsumosList().remove(consumosConsumosListConsumosConsumos);
                    oldFkAmyTipolecturasOfConsumosConsumosListConsumosConsumos = em.merge(oldFkAmyTipolecturasOfConsumosConsumosListConsumosConsumos);
                }
            }
            for (AmyConsumos amyConsumosListAmyConsumos : amyTipolecturas.getAmyConsumosList()) {
                AmyTipolecturas oldFkAmyTipolecturasOfAmyConsumosListAmyConsumos = amyConsumosListAmyConsumos.getFkAmyTipolecturas();
                amyConsumosListAmyConsumos.setFkAmyTipolecturas(amyTipolecturas);
                amyConsumosListAmyConsumos = em.merge(amyConsumosListAmyConsumos);
                if (oldFkAmyTipolecturasOfAmyConsumosListAmyConsumos != null) {
                    oldFkAmyTipolecturasOfAmyConsumosListAmyConsumos.getAmyConsumosList().remove(amyConsumosListAmyConsumos);
                    oldFkAmyTipolecturasOfAmyConsumosListAmyConsumos = em.merge(oldFkAmyTipolecturasOfAmyConsumosListAmyConsumos);
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
            List<AmyLecturas> amyLecturasListOld = persistentAmyTipolecturas.getAmyLecturasList();
            List<AmyLecturas> amyLecturasListNew = amyTipolecturas.getAmyLecturasList();
            List<ConsumosConsumos> consumosConsumosListOld = persistentAmyTipolecturas.getConsumosConsumosList();
            List<ConsumosConsumos> consumosConsumosListNew = amyTipolecturas.getConsumosConsumosList();
            List<AmyConsumos> amyConsumosListOld = persistentAmyTipolecturas.getAmyConsumosList();
            List<AmyConsumos> amyConsumosListNew = amyTipolecturas.getAmyConsumosList();
            List<String> illegalOrphanMessages = null;
            for (AmyLecturas amyLecturasListOldAmyLecturas : amyLecturasListOld) {
                if (!amyLecturasListNew.contains(amyLecturasListOldAmyLecturas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyLecturas " + amyLecturasListOldAmyLecturas + " since its fkAmyTipolecturas field is not nullable.");
                }
            }
            for (ConsumosConsumos consumosConsumosListOldConsumosConsumos : consumosConsumosListOld) {
                if (!consumosConsumosListNew.contains(consumosConsumosListOldConsumosConsumos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ConsumosConsumos " + consumosConsumosListOldConsumosConsumos + " since its fkAmyTipolecturas field is not nullable.");
                }
            }
            for (AmyConsumos amyConsumosListOldAmyConsumos : amyConsumosListOld) {
                if (!amyConsumosListNew.contains(amyConsumosListOldAmyConsumos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyConsumos " + amyConsumosListOldAmyConsumos + " since its fkAmyTipolecturas field is not nullable.");
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
            amyTipolecturas.setAmyLecturasList(amyLecturasListNew);
            List<ConsumosConsumos> attachedConsumosConsumosListNew = new ArrayList<ConsumosConsumos>();
            for (ConsumosConsumos consumosConsumosListNewConsumosConsumosToAttach : consumosConsumosListNew) {
                consumosConsumosListNewConsumosConsumosToAttach = em.getReference(consumosConsumosListNewConsumosConsumosToAttach.getClass(), consumosConsumosListNewConsumosConsumosToAttach.getIdConsumo());
                attachedConsumosConsumosListNew.add(consumosConsumosListNewConsumosConsumosToAttach);
            }
            consumosConsumosListNew = attachedConsumosConsumosListNew;
            amyTipolecturas.setConsumosConsumosList(consumosConsumosListNew);
            List<AmyConsumos> attachedAmyConsumosListNew = new ArrayList<AmyConsumos>();
            for (AmyConsumos amyConsumosListNewAmyConsumosToAttach : amyConsumosListNew) {
                amyConsumosListNewAmyConsumosToAttach = em.getReference(amyConsumosListNewAmyConsumosToAttach.getClass(), amyConsumosListNewAmyConsumosToAttach.getIdConsumo());
                attachedAmyConsumosListNew.add(amyConsumosListNewAmyConsumosToAttach);
            }
            amyConsumosListNew = attachedAmyConsumosListNew;
            amyTipolecturas.setAmyConsumosList(amyConsumosListNew);
            amyTipolecturas = em.merge(amyTipolecturas);
            for (AmyLecturas amyLecturasListNewAmyLecturas : amyLecturasListNew) {
                if (!amyLecturasListOld.contains(amyLecturasListNewAmyLecturas)) {
                    AmyTipolecturas oldFkAmyTipolecturasOfAmyLecturasListNewAmyLecturas = amyLecturasListNewAmyLecturas.getFkAmyTipolecturas();
                    amyLecturasListNewAmyLecturas.setFkAmyTipolecturas(amyTipolecturas);
                    amyLecturasListNewAmyLecturas = em.merge(amyLecturasListNewAmyLecturas);
                    if (oldFkAmyTipolecturasOfAmyLecturasListNewAmyLecturas != null && !oldFkAmyTipolecturasOfAmyLecturasListNewAmyLecturas.equals(amyTipolecturas)) {
                        oldFkAmyTipolecturasOfAmyLecturasListNewAmyLecturas.getAmyLecturasList().remove(amyLecturasListNewAmyLecturas);
                        oldFkAmyTipolecturasOfAmyLecturasListNewAmyLecturas = em.merge(oldFkAmyTipolecturasOfAmyLecturasListNewAmyLecturas);
                    }
                }
            }
            for (ConsumosConsumos consumosConsumosListNewConsumosConsumos : consumosConsumosListNew) {
                if (!consumosConsumosListOld.contains(consumosConsumosListNewConsumosConsumos)) {
                    AmyTipolecturas oldFkAmyTipolecturasOfConsumosConsumosListNewConsumosConsumos = consumosConsumosListNewConsumosConsumos.getFkAmyTipolecturas();
                    consumosConsumosListNewConsumosConsumos.setFkAmyTipolecturas(amyTipolecturas);
                    consumosConsumosListNewConsumosConsumos = em.merge(consumosConsumosListNewConsumosConsumos);
                    if (oldFkAmyTipolecturasOfConsumosConsumosListNewConsumosConsumos != null && !oldFkAmyTipolecturasOfConsumosConsumosListNewConsumosConsumos.equals(amyTipolecturas)) {
                        oldFkAmyTipolecturasOfConsumosConsumosListNewConsumosConsumos.getConsumosConsumosList().remove(consumosConsumosListNewConsumosConsumos);
                        oldFkAmyTipolecturasOfConsumosConsumosListNewConsumosConsumos = em.merge(oldFkAmyTipolecturasOfConsumosConsumosListNewConsumosConsumos);
                    }
                }
            }
            for (AmyConsumos amyConsumosListNewAmyConsumos : amyConsumosListNew) {
                if (!amyConsumosListOld.contains(amyConsumosListNewAmyConsumos)) {
                    AmyTipolecturas oldFkAmyTipolecturasOfAmyConsumosListNewAmyConsumos = amyConsumosListNewAmyConsumos.getFkAmyTipolecturas();
                    amyConsumosListNewAmyConsumos.setFkAmyTipolecturas(amyTipolecturas);
                    amyConsumosListNewAmyConsumos = em.merge(amyConsumosListNewAmyConsumos);
                    if (oldFkAmyTipolecturasOfAmyConsumosListNewAmyConsumos != null && !oldFkAmyTipolecturasOfAmyConsumosListNewAmyConsumos.equals(amyTipolecturas)) {
                        oldFkAmyTipolecturasOfAmyConsumosListNewAmyConsumos.getAmyConsumosList().remove(amyConsumosListNewAmyConsumos);
                        oldFkAmyTipolecturasOfAmyConsumosListNewAmyConsumos = em.merge(oldFkAmyTipolecturasOfAmyConsumosListNewAmyConsumos);
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
            List<AmyLecturas> amyLecturasListOrphanCheck = amyTipolecturas.getAmyLecturasList();
            for (AmyLecturas amyLecturasListOrphanCheckAmyLecturas : amyLecturasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyTipolecturas (" + amyTipolecturas + ") cannot be destroyed since the AmyLecturas " + amyLecturasListOrphanCheckAmyLecturas + " in its amyLecturasList field has a non-nullable fkAmyTipolecturas field.");
            }
            List<ConsumosConsumos> consumosConsumosListOrphanCheck = amyTipolecturas.getConsumosConsumosList();
            for (ConsumosConsumos consumosConsumosListOrphanCheckConsumosConsumos : consumosConsumosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyTipolecturas (" + amyTipolecturas + ") cannot be destroyed since the ConsumosConsumos " + consumosConsumosListOrphanCheckConsumosConsumos + " in its consumosConsumosList field has a non-nullable fkAmyTipolecturas field.");
            }
            List<AmyConsumos> amyConsumosListOrphanCheck = amyTipolecturas.getAmyConsumosList();
            for (AmyConsumos amyConsumosListOrphanCheckAmyConsumos : amyConsumosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyTipolecturas (" + amyTipolecturas + ") cannot be destroyed since the AmyConsumos " + amyConsumosListOrphanCheckAmyConsumos + " in its amyConsumosList field has a non-nullable fkAmyTipolecturas field.");
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
