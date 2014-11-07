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
import java.util.List;
import emcali.ami.persistence.entity.AmyConsumos;
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
        if (amyCanal.getAmyLecturasList() == null) {
            amyCanal.setAmyLecturasList(new ArrayList<AmyLecturas>());
        }
        if (amyCanal.getAmyConsumosList() == null) {
            amyCanal.setAmyConsumosList(new ArrayList<AmyConsumos>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<AmyLecturas> attachedAmyLecturasList = new ArrayList<AmyLecturas>();
            for (AmyLecturas amyLecturasListAmyLecturasToAttach : amyCanal.getAmyLecturasList()) {
                amyLecturasListAmyLecturasToAttach = em.getReference(amyLecturasListAmyLecturasToAttach.getClass(), amyLecturasListAmyLecturasToAttach.getIdLecturas());
                attachedAmyLecturasList.add(amyLecturasListAmyLecturasToAttach);
            }
            amyCanal.setAmyLecturasList(attachedAmyLecturasList);
            List<AmyConsumos> attachedAmyConsumosList = new ArrayList<AmyConsumos>();
            for (AmyConsumos amyConsumosListAmyConsumosToAttach : amyCanal.getAmyConsumosList()) {
                amyConsumosListAmyConsumosToAttach = em.getReference(amyConsumosListAmyConsumosToAttach.getClass(), amyConsumosListAmyConsumosToAttach.getIdConsumo());
                attachedAmyConsumosList.add(amyConsumosListAmyConsumosToAttach);
            }
            amyCanal.setAmyConsumosList(attachedAmyConsumosList);
            em.persist(amyCanal);
            for (AmyLecturas amyLecturasListAmyLecturas : amyCanal.getAmyLecturasList()) {
                AmyCanal oldFkAmyCanalOfAmyLecturasListAmyLecturas = amyLecturasListAmyLecturas.getFkAmyCanal();
                amyLecturasListAmyLecturas.setFkAmyCanal(amyCanal);
                amyLecturasListAmyLecturas = em.merge(amyLecturasListAmyLecturas);
                if (oldFkAmyCanalOfAmyLecturasListAmyLecturas != null) {
                    oldFkAmyCanalOfAmyLecturasListAmyLecturas.getAmyLecturasList().remove(amyLecturasListAmyLecturas);
                    oldFkAmyCanalOfAmyLecturasListAmyLecturas = em.merge(oldFkAmyCanalOfAmyLecturasListAmyLecturas);
                }
            }
            for (AmyConsumos amyConsumosListAmyConsumos : amyCanal.getAmyConsumosList()) {
                AmyCanal oldFkAmyCanalOfAmyConsumosListAmyConsumos = amyConsumosListAmyConsumos.getFkAmyCanal();
                amyConsumosListAmyConsumos.setFkAmyCanal(amyCanal);
                amyConsumosListAmyConsumos = em.merge(amyConsumosListAmyConsumos);
                if (oldFkAmyCanalOfAmyConsumosListAmyConsumos != null) {
                    oldFkAmyCanalOfAmyConsumosListAmyConsumos.getAmyConsumosList().remove(amyConsumosListAmyConsumos);
                    oldFkAmyCanalOfAmyConsumosListAmyConsumos = em.merge(oldFkAmyCanalOfAmyConsumosListAmyConsumos);
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
            List<AmyLecturas> amyLecturasListOld = persistentAmyCanal.getAmyLecturasList();
            List<AmyLecturas> amyLecturasListNew = amyCanal.getAmyLecturasList();
            List<AmyConsumos> amyConsumosListOld = persistentAmyCanal.getAmyConsumosList();
            List<AmyConsumos> amyConsumosListNew = amyCanal.getAmyConsumosList();
            List<String> illegalOrphanMessages = null;
            for (AmyLecturas amyLecturasListOldAmyLecturas : amyLecturasListOld) {
                if (!amyLecturasListNew.contains(amyLecturasListOldAmyLecturas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyLecturas " + amyLecturasListOldAmyLecturas + " since its fkAmyCanal field is not nullable.");
                }
            }
            for (AmyConsumos amyConsumosListOldAmyConsumos : amyConsumosListOld) {
                if (!amyConsumosListNew.contains(amyConsumosListOldAmyConsumos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyConsumos " + amyConsumosListOldAmyConsumos + " since its fkAmyCanal field is not nullable.");
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
            amyCanal.setAmyLecturasList(amyLecturasListNew);
            List<AmyConsumos> attachedAmyConsumosListNew = new ArrayList<AmyConsumos>();
            for (AmyConsumos amyConsumosListNewAmyConsumosToAttach : amyConsumosListNew) {
                amyConsumosListNewAmyConsumosToAttach = em.getReference(amyConsumosListNewAmyConsumosToAttach.getClass(), amyConsumosListNewAmyConsumosToAttach.getIdConsumo());
                attachedAmyConsumosListNew.add(amyConsumosListNewAmyConsumosToAttach);
            }
            amyConsumosListNew = attachedAmyConsumosListNew;
            amyCanal.setAmyConsumosList(amyConsumosListNew);
            amyCanal = em.merge(amyCanal);
            for (AmyLecturas amyLecturasListNewAmyLecturas : amyLecturasListNew) {
                if (!amyLecturasListOld.contains(amyLecturasListNewAmyLecturas)) {
                    AmyCanal oldFkAmyCanalOfAmyLecturasListNewAmyLecturas = amyLecturasListNewAmyLecturas.getFkAmyCanal();
                    amyLecturasListNewAmyLecturas.setFkAmyCanal(amyCanal);
                    amyLecturasListNewAmyLecturas = em.merge(amyLecturasListNewAmyLecturas);
                    if (oldFkAmyCanalOfAmyLecturasListNewAmyLecturas != null && !oldFkAmyCanalOfAmyLecturasListNewAmyLecturas.equals(amyCanal)) {
                        oldFkAmyCanalOfAmyLecturasListNewAmyLecturas.getAmyLecturasList().remove(amyLecturasListNewAmyLecturas);
                        oldFkAmyCanalOfAmyLecturasListNewAmyLecturas = em.merge(oldFkAmyCanalOfAmyLecturasListNewAmyLecturas);
                    }
                }
            }
            for (AmyConsumos amyConsumosListNewAmyConsumos : amyConsumosListNew) {
                if (!amyConsumosListOld.contains(amyConsumosListNewAmyConsumos)) {
                    AmyCanal oldFkAmyCanalOfAmyConsumosListNewAmyConsumos = amyConsumosListNewAmyConsumos.getFkAmyCanal();
                    amyConsumosListNewAmyConsumos.setFkAmyCanal(amyCanal);
                    amyConsumosListNewAmyConsumos = em.merge(amyConsumosListNewAmyConsumos);
                    if (oldFkAmyCanalOfAmyConsumosListNewAmyConsumos != null && !oldFkAmyCanalOfAmyConsumosListNewAmyConsumos.equals(amyCanal)) {
                        oldFkAmyCanalOfAmyConsumosListNewAmyConsumos.getAmyConsumosList().remove(amyConsumosListNewAmyConsumos);
                        oldFkAmyCanalOfAmyConsumosListNewAmyConsumos = em.merge(oldFkAmyCanalOfAmyConsumosListNewAmyConsumos);
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
            List<AmyLecturas> amyLecturasListOrphanCheck = amyCanal.getAmyLecturasList();
            for (AmyLecturas amyLecturasListOrphanCheckAmyLecturas : amyLecturasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyCanal (" + amyCanal + ") cannot be destroyed since the AmyLecturas " + amyLecturasListOrphanCheckAmyLecturas + " in its amyLecturasList field has a non-nullable fkAmyCanal field.");
            }
            List<AmyConsumos> amyConsumosListOrphanCheck = amyCanal.getAmyConsumosList();
            for (AmyConsumos amyConsumosListOrphanCheckAmyConsumos : amyConsumosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyCanal (" + amyCanal + ") cannot be destroyed since the AmyConsumos " + amyConsumosListOrphanCheckAmyConsumos + " in its amyConsumosList field has a non-nullable fkAmyCanal field.");
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
