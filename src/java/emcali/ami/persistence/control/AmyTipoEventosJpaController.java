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
import java.util.List;
import emcali.ami.persistence.entity.PrepagoEventos;
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
        if (amyTipoEventos.getAmyEventosList() == null) {
            amyTipoEventos.setAmyEventosList(new ArrayList<AmyEventos>());
        }
        if (amyTipoEventos.getPrepagoEventosList() == null) {
            amyTipoEventos.setPrepagoEventosList(new ArrayList<PrepagoEventos>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<AmyEventos> attachedAmyEventosList = new ArrayList<AmyEventos>();
            for (AmyEventos amyEventosListAmyEventosToAttach : amyTipoEventos.getAmyEventosList()) {
                amyEventosListAmyEventosToAttach = em.getReference(amyEventosListAmyEventosToAttach.getClass(), amyEventosListAmyEventosToAttach.getIdEvento());
                attachedAmyEventosList.add(amyEventosListAmyEventosToAttach);
            }
            amyTipoEventos.setAmyEventosList(attachedAmyEventosList);
            List<PrepagoEventos> attachedPrepagoEventosList = new ArrayList<PrepagoEventos>();
            for (PrepagoEventos prepagoEventosListPrepagoEventosToAttach : amyTipoEventos.getPrepagoEventosList()) {
                prepagoEventosListPrepagoEventosToAttach = em.getReference(prepagoEventosListPrepagoEventosToAttach.getClass(), prepagoEventosListPrepagoEventosToAttach.getIdEventos());
                attachedPrepagoEventosList.add(prepagoEventosListPrepagoEventosToAttach);
            }
            amyTipoEventos.setPrepagoEventosList(attachedPrepagoEventosList);
            em.persist(amyTipoEventos);
            for (AmyEventos amyEventosListAmyEventos : amyTipoEventos.getAmyEventosList()) {
                AmyTipoEventos oldFkAmyTipoEventosOfAmyEventosListAmyEventos = amyEventosListAmyEventos.getFkAmyTipoEventos();
                amyEventosListAmyEventos.setFkAmyTipoEventos(amyTipoEventos);
                amyEventosListAmyEventos = em.merge(amyEventosListAmyEventos);
                if (oldFkAmyTipoEventosOfAmyEventosListAmyEventos != null) {
                    oldFkAmyTipoEventosOfAmyEventosListAmyEventos.getAmyEventosList().remove(amyEventosListAmyEventos);
                    oldFkAmyTipoEventosOfAmyEventosListAmyEventos = em.merge(oldFkAmyTipoEventosOfAmyEventosListAmyEventos);
                }
            }
            for (PrepagoEventos prepagoEventosListPrepagoEventos : amyTipoEventos.getPrepagoEventosList()) {
                AmyTipoEventos oldFkAmyTipoEventosOfPrepagoEventosListPrepagoEventos = prepagoEventosListPrepagoEventos.getFkAmyTipoEventos();
                prepagoEventosListPrepagoEventos.setFkAmyTipoEventos(amyTipoEventos);
                prepagoEventosListPrepagoEventos = em.merge(prepagoEventosListPrepagoEventos);
                if (oldFkAmyTipoEventosOfPrepagoEventosListPrepagoEventos != null) {
                    oldFkAmyTipoEventosOfPrepagoEventosListPrepagoEventos.getPrepagoEventosList().remove(prepagoEventosListPrepagoEventos);
                    oldFkAmyTipoEventosOfPrepagoEventosListPrepagoEventos = em.merge(oldFkAmyTipoEventosOfPrepagoEventosListPrepagoEventos);
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
            List<AmyEventos> amyEventosListOld = persistentAmyTipoEventos.getAmyEventosList();
            List<AmyEventos> amyEventosListNew = amyTipoEventos.getAmyEventosList();
            List<PrepagoEventos> prepagoEventosListOld = persistentAmyTipoEventos.getPrepagoEventosList();
            List<PrepagoEventos> prepagoEventosListNew = amyTipoEventos.getPrepagoEventosList();
            List<String> illegalOrphanMessages = null;
            for (AmyEventos amyEventosListOldAmyEventos : amyEventosListOld) {
                if (!amyEventosListNew.contains(amyEventosListOldAmyEventos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyEventos " + amyEventosListOldAmyEventos + " since its fkAmyTipoEventos field is not nullable.");
                }
            }
            for (PrepagoEventos prepagoEventosListOldPrepagoEventos : prepagoEventosListOld) {
                if (!prepagoEventosListNew.contains(prepagoEventosListOldPrepagoEventos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PrepagoEventos " + prepagoEventosListOldPrepagoEventos + " since its fkAmyTipoEventos field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<AmyEventos> attachedAmyEventosListNew = new ArrayList<AmyEventos>();
            for (AmyEventos amyEventosListNewAmyEventosToAttach : amyEventosListNew) {
                amyEventosListNewAmyEventosToAttach = em.getReference(amyEventosListNewAmyEventosToAttach.getClass(), amyEventosListNewAmyEventosToAttach.getIdEvento());
                attachedAmyEventosListNew.add(amyEventosListNewAmyEventosToAttach);
            }
            amyEventosListNew = attachedAmyEventosListNew;
            amyTipoEventos.setAmyEventosList(amyEventosListNew);
            List<PrepagoEventos> attachedPrepagoEventosListNew = new ArrayList<PrepagoEventos>();
            for (PrepagoEventos prepagoEventosListNewPrepagoEventosToAttach : prepagoEventosListNew) {
                prepagoEventosListNewPrepagoEventosToAttach = em.getReference(prepagoEventosListNewPrepagoEventosToAttach.getClass(), prepagoEventosListNewPrepagoEventosToAttach.getIdEventos());
                attachedPrepagoEventosListNew.add(prepagoEventosListNewPrepagoEventosToAttach);
            }
            prepagoEventosListNew = attachedPrepagoEventosListNew;
            amyTipoEventos.setPrepagoEventosList(prepagoEventosListNew);
            amyTipoEventos = em.merge(amyTipoEventos);
            for (AmyEventos amyEventosListNewAmyEventos : amyEventosListNew) {
                if (!amyEventosListOld.contains(amyEventosListNewAmyEventos)) {
                    AmyTipoEventos oldFkAmyTipoEventosOfAmyEventosListNewAmyEventos = amyEventosListNewAmyEventos.getFkAmyTipoEventos();
                    amyEventosListNewAmyEventos.setFkAmyTipoEventos(amyTipoEventos);
                    amyEventosListNewAmyEventos = em.merge(amyEventosListNewAmyEventos);
                    if (oldFkAmyTipoEventosOfAmyEventosListNewAmyEventos != null && !oldFkAmyTipoEventosOfAmyEventosListNewAmyEventos.equals(amyTipoEventos)) {
                        oldFkAmyTipoEventosOfAmyEventosListNewAmyEventos.getAmyEventosList().remove(amyEventosListNewAmyEventos);
                        oldFkAmyTipoEventosOfAmyEventosListNewAmyEventos = em.merge(oldFkAmyTipoEventosOfAmyEventosListNewAmyEventos);
                    }
                }
            }
            for (PrepagoEventos prepagoEventosListNewPrepagoEventos : prepagoEventosListNew) {
                if (!prepagoEventosListOld.contains(prepagoEventosListNewPrepagoEventos)) {
                    AmyTipoEventos oldFkAmyTipoEventosOfPrepagoEventosListNewPrepagoEventos = prepagoEventosListNewPrepagoEventos.getFkAmyTipoEventos();
                    prepagoEventosListNewPrepagoEventos.setFkAmyTipoEventos(amyTipoEventos);
                    prepagoEventosListNewPrepagoEventos = em.merge(prepagoEventosListNewPrepagoEventos);
                    if (oldFkAmyTipoEventosOfPrepagoEventosListNewPrepagoEventos != null && !oldFkAmyTipoEventosOfPrepagoEventosListNewPrepagoEventos.equals(amyTipoEventos)) {
                        oldFkAmyTipoEventosOfPrepagoEventosListNewPrepagoEventos.getPrepagoEventosList().remove(prepagoEventosListNewPrepagoEventos);
                        oldFkAmyTipoEventosOfPrepagoEventosListNewPrepagoEventos = em.merge(oldFkAmyTipoEventosOfPrepagoEventosListNewPrepagoEventos);
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
            List<AmyEventos> amyEventosListOrphanCheck = amyTipoEventos.getAmyEventosList();
            for (AmyEventos amyEventosListOrphanCheckAmyEventos : amyEventosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyTipoEventos (" + amyTipoEventos + ") cannot be destroyed since the AmyEventos " + amyEventosListOrphanCheckAmyEventos + " in its amyEventosList field has a non-nullable fkAmyTipoEventos field.");
            }
            List<PrepagoEventos> prepagoEventosListOrphanCheck = amyTipoEventos.getPrepagoEventosList();
            for (PrepagoEventos prepagoEventosListOrphanCheckPrepagoEventos : prepagoEventosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyTipoEventos (" + amyTipoEventos + ") cannot be destroyed since the PrepagoEventos " + prepagoEventosListOrphanCheckPrepagoEventos + " in its prepagoEventosList field has a non-nullable fkAmyTipoEventos field.");
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
