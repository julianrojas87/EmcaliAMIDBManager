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
import emcali.ami.persistence.entity.GestionFuncionarios;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import emcali.ami.persistence.entity.GestionUsuarios;
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
public class GestionFuncionariosJpaController implements Serializable {

    public GestionFuncionariosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GestionFuncionarios gestionFuncionarios) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (gestionFuncionarios.getGestionUsuariosCollection() == null) {
            gestionFuncionarios.setGestionUsuariosCollection(new ArrayList<GestionUsuarios>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<GestionUsuarios> attachedGestionUsuariosCollection = new ArrayList<GestionUsuarios>();
            for (GestionUsuarios gestionUsuariosCollectionGestionUsuariosToAttach : gestionFuncionarios.getGestionUsuariosCollection()) {
                gestionUsuariosCollectionGestionUsuariosToAttach = em.getReference(gestionUsuariosCollectionGestionUsuariosToAttach.getClass(), gestionUsuariosCollectionGestionUsuariosToAttach.getIdUsuarios());
                attachedGestionUsuariosCollection.add(gestionUsuariosCollectionGestionUsuariosToAttach);
            }
            gestionFuncionarios.setGestionUsuariosCollection(attachedGestionUsuariosCollection);
            em.persist(gestionFuncionarios);
            for (GestionUsuarios gestionUsuariosCollectionGestionUsuarios : gestionFuncionarios.getGestionUsuariosCollection()) {
                GestionFuncionarios oldFkGestionFuncionariosOfGestionUsuariosCollectionGestionUsuarios = gestionUsuariosCollectionGestionUsuarios.getFkGestionFuncionarios();
                gestionUsuariosCollectionGestionUsuarios.setFkGestionFuncionarios(gestionFuncionarios);
                gestionUsuariosCollectionGestionUsuarios = em.merge(gestionUsuariosCollectionGestionUsuarios);
                if (oldFkGestionFuncionariosOfGestionUsuariosCollectionGestionUsuarios != null) {
                    oldFkGestionFuncionariosOfGestionUsuariosCollectionGestionUsuarios.getGestionUsuariosCollection().remove(gestionUsuariosCollectionGestionUsuarios);
                    oldFkGestionFuncionariosOfGestionUsuariosCollectionGestionUsuarios = em.merge(oldFkGestionFuncionariosOfGestionUsuariosCollectionGestionUsuarios);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findGestionFuncionarios(gestionFuncionarios.getIdFuncionarios()) != null) {
                throw new PreexistingEntityException("GestionFuncionarios " + gestionFuncionarios + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GestionFuncionarios gestionFuncionarios) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GestionFuncionarios persistentGestionFuncionarios = em.find(GestionFuncionarios.class, gestionFuncionarios.getIdFuncionarios());
            Collection<GestionUsuarios> gestionUsuariosCollectionOld = persistentGestionFuncionarios.getGestionUsuariosCollection();
            Collection<GestionUsuarios> gestionUsuariosCollectionNew = gestionFuncionarios.getGestionUsuariosCollection();
            List<String> illegalOrphanMessages = null;
            for (GestionUsuarios gestionUsuariosCollectionOldGestionUsuarios : gestionUsuariosCollectionOld) {
                if (!gestionUsuariosCollectionNew.contains(gestionUsuariosCollectionOldGestionUsuarios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GestionUsuarios " + gestionUsuariosCollectionOldGestionUsuarios + " since its fkGestionFuncionarios field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<GestionUsuarios> attachedGestionUsuariosCollectionNew = new ArrayList<GestionUsuarios>();
            for (GestionUsuarios gestionUsuariosCollectionNewGestionUsuariosToAttach : gestionUsuariosCollectionNew) {
                gestionUsuariosCollectionNewGestionUsuariosToAttach = em.getReference(gestionUsuariosCollectionNewGestionUsuariosToAttach.getClass(), gestionUsuariosCollectionNewGestionUsuariosToAttach.getIdUsuarios());
                attachedGestionUsuariosCollectionNew.add(gestionUsuariosCollectionNewGestionUsuariosToAttach);
            }
            gestionUsuariosCollectionNew = attachedGestionUsuariosCollectionNew;
            gestionFuncionarios.setGestionUsuariosCollection(gestionUsuariosCollectionNew);
            gestionFuncionarios = em.merge(gestionFuncionarios);
            for (GestionUsuarios gestionUsuariosCollectionNewGestionUsuarios : gestionUsuariosCollectionNew) {
                if (!gestionUsuariosCollectionOld.contains(gestionUsuariosCollectionNewGestionUsuarios)) {
                    GestionFuncionarios oldFkGestionFuncionariosOfGestionUsuariosCollectionNewGestionUsuarios = gestionUsuariosCollectionNewGestionUsuarios.getFkGestionFuncionarios();
                    gestionUsuariosCollectionNewGestionUsuarios.setFkGestionFuncionarios(gestionFuncionarios);
                    gestionUsuariosCollectionNewGestionUsuarios = em.merge(gestionUsuariosCollectionNewGestionUsuarios);
                    if (oldFkGestionFuncionariosOfGestionUsuariosCollectionNewGestionUsuarios != null && !oldFkGestionFuncionariosOfGestionUsuariosCollectionNewGestionUsuarios.equals(gestionFuncionarios)) {
                        oldFkGestionFuncionariosOfGestionUsuariosCollectionNewGestionUsuarios.getGestionUsuariosCollection().remove(gestionUsuariosCollectionNewGestionUsuarios);
                        oldFkGestionFuncionariosOfGestionUsuariosCollectionNewGestionUsuarios = em.merge(oldFkGestionFuncionariosOfGestionUsuariosCollectionNewGestionUsuarios);
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
                Long id = gestionFuncionarios.getIdFuncionarios();
                if (findGestionFuncionarios(id) == null) {
                    throw new NonexistentEntityException("The gestionFuncionarios with id " + id + " no longer exists.");
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
            GestionFuncionarios gestionFuncionarios;
            try {
                gestionFuncionarios = em.getReference(GestionFuncionarios.class, id);
                gestionFuncionarios.getIdFuncionarios();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gestionFuncionarios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<GestionUsuarios> gestionUsuariosCollectionOrphanCheck = gestionFuncionarios.getGestionUsuariosCollection();
            for (GestionUsuarios gestionUsuariosCollectionOrphanCheckGestionUsuarios : gestionUsuariosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GestionFuncionarios (" + gestionFuncionarios + ") cannot be destroyed since the GestionUsuarios " + gestionUsuariosCollectionOrphanCheckGestionUsuarios + " in its gestionUsuariosCollection field has a non-nullable fkGestionFuncionarios field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(gestionFuncionarios);
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

    public List<GestionFuncionarios> findGestionFuncionariosEntities() {
        return findGestionFuncionariosEntities(true, -1, -1);
    }

    public List<GestionFuncionarios> findGestionFuncionariosEntities(int maxResults, int firstResult) {
        return findGestionFuncionariosEntities(false, maxResults, firstResult);
    }

    private List<GestionFuncionarios> findGestionFuncionariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GestionFuncionarios.class));
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

    public GestionFuncionarios findGestionFuncionarios(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GestionFuncionarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getGestionFuncionariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GestionFuncionarios> rt = cq.from(GestionFuncionarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
