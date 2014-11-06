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
import emcali.ami.persistence.entity.GestionPerfiles;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import emcali.ami.persistence.entity.GestionUsuarios;
import java.util.ArrayList;
import java.util.Collection;
import emcali.ami.persistence.entity.GestionPerfilesMenu;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class GestionPerfilesJpaController implements Serializable {

    public GestionPerfilesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GestionPerfiles gestionPerfiles) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (gestionPerfiles.getGestionUsuariosCollection() == null) {
            gestionPerfiles.setGestionUsuariosCollection(new ArrayList<GestionUsuarios>());
        }
        if (gestionPerfiles.getGestionPerfilesMenuCollection() == null) {
            gestionPerfiles.setGestionPerfilesMenuCollection(new ArrayList<GestionPerfilesMenu>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<GestionUsuarios> attachedGestionUsuariosCollection = new ArrayList<GestionUsuarios>();
            for (GestionUsuarios gestionUsuariosCollectionGestionUsuariosToAttach : gestionPerfiles.getGestionUsuariosCollection()) {
                gestionUsuariosCollectionGestionUsuariosToAttach = em.getReference(gestionUsuariosCollectionGestionUsuariosToAttach.getClass(), gestionUsuariosCollectionGestionUsuariosToAttach.getIdUsuarios());
                attachedGestionUsuariosCollection.add(gestionUsuariosCollectionGestionUsuariosToAttach);
            }
            gestionPerfiles.setGestionUsuariosCollection(attachedGestionUsuariosCollection);
            Collection<GestionPerfilesMenu> attachedGestionPerfilesMenuCollection = new ArrayList<GestionPerfilesMenu>();
            for (GestionPerfilesMenu gestionPerfilesMenuCollectionGestionPerfilesMenuToAttach : gestionPerfiles.getGestionPerfilesMenuCollection()) {
                gestionPerfilesMenuCollectionGestionPerfilesMenuToAttach = em.getReference(gestionPerfilesMenuCollectionGestionPerfilesMenuToAttach.getClass(), gestionPerfilesMenuCollectionGestionPerfilesMenuToAttach.getIdPerfilesMenu());
                attachedGestionPerfilesMenuCollection.add(gestionPerfilesMenuCollectionGestionPerfilesMenuToAttach);
            }
            gestionPerfiles.setGestionPerfilesMenuCollection(attachedGestionPerfilesMenuCollection);
            em.persist(gestionPerfiles);
            for (GestionUsuarios gestionUsuariosCollectionGestionUsuarios : gestionPerfiles.getGestionUsuariosCollection()) {
                GestionPerfiles oldFkGestionPerfilesOfGestionUsuariosCollectionGestionUsuarios = gestionUsuariosCollectionGestionUsuarios.getFkGestionPerfiles();
                gestionUsuariosCollectionGestionUsuarios.setFkGestionPerfiles(gestionPerfiles);
                gestionUsuariosCollectionGestionUsuarios = em.merge(gestionUsuariosCollectionGestionUsuarios);
                if (oldFkGestionPerfilesOfGestionUsuariosCollectionGestionUsuarios != null) {
                    oldFkGestionPerfilesOfGestionUsuariosCollectionGestionUsuarios.getGestionUsuariosCollection().remove(gestionUsuariosCollectionGestionUsuarios);
                    oldFkGestionPerfilesOfGestionUsuariosCollectionGestionUsuarios = em.merge(oldFkGestionPerfilesOfGestionUsuariosCollectionGestionUsuarios);
                }
            }
            for (GestionPerfilesMenu gestionPerfilesMenuCollectionGestionPerfilesMenu : gestionPerfiles.getGestionPerfilesMenuCollection()) {
                GestionPerfiles oldFkGestionPerfilesOfGestionPerfilesMenuCollectionGestionPerfilesMenu = gestionPerfilesMenuCollectionGestionPerfilesMenu.getFkGestionPerfiles();
                gestionPerfilesMenuCollectionGestionPerfilesMenu.setFkGestionPerfiles(gestionPerfiles);
                gestionPerfilesMenuCollectionGestionPerfilesMenu = em.merge(gestionPerfilesMenuCollectionGestionPerfilesMenu);
                if (oldFkGestionPerfilesOfGestionPerfilesMenuCollectionGestionPerfilesMenu != null) {
                    oldFkGestionPerfilesOfGestionPerfilesMenuCollectionGestionPerfilesMenu.getGestionPerfilesMenuCollection().remove(gestionPerfilesMenuCollectionGestionPerfilesMenu);
                    oldFkGestionPerfilesOfGestionPerfilesMenuCollectionGestionPerfilesMenu = em.merge(oldFkGestionPerfilesOfGestionPerfilesMenuCollectionGestionPerfilesMenu);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findGestionPerfiles(gestionPerfiles.getIdPerfiles()) != null) {
                throw new PreexistingEntityException("GestionPerfiles " + gestionPerfiles + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GestionPerfiles gestionPerfiles) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GestionPerfiles persistentGestionPerfiles = em.find(GestionPerfiles.class, gestionPerfiles.getIdPerfiles());
            Collection<GestionUsuarios> gestionUsuariosCollectionOld = persistentGestionPerfiles.getGestionUsuariosCollection();
            Collection<GestionUsuarios> gestionUsuariosCollectionNew = gestionPerfiles.getGestionUsuariosCollection();
            Collection<GestionPerfilesMenu> gestionPerfilesMenuCollectionOld = persistentGestionPerfiles.getGestionPerfilesMenuCollection();
            Collection<GestionPerfilesMenu> gestionPerfilesMenuCollectionNew = gestionPerfiles.getGestionPerfilesMenuCollection();
            List<String> illegalOrphanMessages = null;
            for (GestionUsuarios gestionUsuariosCollectionOldGestionUsuarios : gestionUsuariosCollectionOld) {
                if (!gestionUsuariosCollectionNew.contains(gestionUsuariosCollectionOldGestionUsuarios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GestionUsuarios " + gestionUsuariosCollectionOldGestionUsuarios + " since its fkGestionPerfiles field is not nullable.");
                }
            }
            for (GestionPerfilesMenu gestionPerfilesMenuCollectionOldGestionPerfilesMenu : gestionPerfilesMenuCollectionOld) {
                if (!gestionPerfilesMenuCollectionNew.contains(gestionPerfilesMenuCollectionOldGestionPerfilesMenu)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GestionPerfilesMenu " + gestionPerfilesMenuCollectionOldGestionPerfilesMenu + " since its fkGestionPerfiles field is not nullable.");
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
            gestionPerfiles.setGestionUsuariosCollection(gestionUsuariosCollectionNew);
            Collection<GestionPerfilesMenu> attachedGestionPerfilesMenuCollectionNew = new ArrayList<GestionPerfilesMenu>();
            for (GestionPerfilesMenu gestionPerfilesMenuCollectionNewGestionPerfilesMenuToAttach : gestionPerfilesMenuCollectionNew) {
                gestionPerfilesMenuCollectionNewGestionPerfilesMenuToAttach = em.getReference(gestionPerfilesMenuCollectionNewGestionPerfilesMenuToAttach.getClass(), gestionPerfilesMenuCollectionNewGestionPerfilesMenuToAttach.getIdPerfilesMenu());
                attachedGestionPerfilesMenuCollectionNew.add(gestionPerfilesMenuCollectionNewGestionPerfilesMenuToAttach);
            }
            gestionPerfilesMenuCollectionNew = attachedGestionPerfilesMenuCollectionNew;
            gestionPerfiles.setGestionPerfilesMenuCollection(gestionPerfilesMenuCollectionNew);
            gestionPerfiles = em.merge(gestionPerfiles);
            for (GestionUsuarios gestionUsuariosCollectionNewGestionUsuarios : gestionUsuariosCollectionNew) {
                if (!gestionUsuariosCollectionOld.contains(gestionUsuariosCollectionNewGestionUsuarios)) {
                    GestionPerfiles oldFkGestionPerfilesOfGestionUsuariosCollectionNewGestionUsuarios = gestionUsuariosCollectionNewGestionUsuarios.getFkGestionPerfiles();
                    gestionUsuariosCollectionNewGestionUsuarios.setFkGestionPerfiles(gestionPerfiles);
                    gestionUsuariosCollectionNewGestionUsuarios = em.merge(gestionUsuariosCollectionNewGestionUsuarios);
                    if (oldFkGestionPerfilesOfGestionUsuariosCollectionNewGestionUsuarios != null && !oldFkGestionPerfilesOfGestionUsuariosCollectionNewGestionUsuarios.equals(gestionPerfiles)) {
                        oldFkGestionPerfilesOfGestionUsuariosCollectionNewGestionUsuarios.getGestionUsuariosCollection().remove(gestionUsuariosCollectionNewGestionUsuarios);
                        oldFkGestionPerfilesOfGestionUsuariosCollectionNewGestionUsuarios = em.merge(oldFkGestionPerfilesOfGestionUsuariosCollectionNewGestionUsuarios);
                    }
                }
            }
            for (GestionPerfilesMenu gestionPerfilesMenuCollectionNewGestionPerfilesMenu : gestionPerfilesMenuCollectionNew) {
                if (!gestionPerfilesMenuCollectionOld.contains(gestionPerfilesMenuCollectionNewGestionPerfilesMenu)) {
                    GestionPerfiles oldFkGestionPerfilesOfGestionPerfilesMenuCollectionNewGestionPerfilesMenu = gestionPerfilesMenuCollectionNewGestionPerfilesMenu.getFkGestionPerfiles();
                    gestionPerfilesMenuCollectionNewGestionPerfilesMenu.setFkGestionPerfiles(gestionPerfiles);
                    gestionPerfilesMenuCollectionNewGestionPerfilesMenu = em.merge(gestionPerfilesMenuCollectionNewGestionPerfilesMenu);
                    if (oldFkGestionPerfilesOfGestionPerfilesMenuCollectionNewGestionPerfilesMenu != null && !oldFkGestionPerfilesOfGestionPerfilesMenuCollectionNewGestionPerfilesMenu.equals(gestionPerfiles)) {
                        oldFkGestionPerfilesOfGestionPerfilesMenuCollectionNewGestionPerfilesMenu.getGestionPerfilesMenuCollection().remove(gestionPerfilesMenuCollectionNewGestionPerfilesMenu);
                        oldFkGestionPerfilesOfGestionPerfilesMenuCollectionNewGestionPerfilesMenu = em.merge(oldFkGestionPerfilesOfGestionPerfilesMenuCollectionNewGestionPerfilesMenu);
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
                Long id = gestionPerfiles.getIdPerfiles();
                if (findGestionPerfiles(id) == null) {
                    throw new NonexistentEntityException("The gestionPerfiles with id " + id + " no longer exists.");
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
            GestionPerfiles gestionPerfiles;
            try {
                gestionPerfiles = em.getReference(GestionPerfiles.class, id);
                gestionPerfiles.getIdPerfiles();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gestionPerfiles with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<GestionUsuarios> gestionUsuariosCollectionOrphanCheck = gestionPerfiles.getGestionUsuariosCollection();
            for (GestionUsuarios gestionUsuariosCollectionOrphanCheckGestionUsuarios : gestionUsuariosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GestionPerfiles (" + gestionPerfiles + ") cannot be destroyed since the GestionUsuarios " + gestionUsuariosCollectionOrphanCheckGestionUsuarios + " in its gestionUsuariosCollection field has a non-nullable fkGestionPerfiles field.");
            }
            Collection<GestionPerfilesMenu> gestionPerfilesMenuCollectionOrphanCheck = gestionPerfiles.getGestionPerfilesMenuCollection();
            for (GestionPerfilesMenu gestionPerfilesMenuCollectionOrphanCheckGestionPerfilesMenu : gestionPerfilesMenuCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GestionPerfiles (" + gestionPerfiles + ") cannot be destroyed since the GestionPerfilesMenu " + gestionPerfilesMenuCollectionOrphanCheckGestionPerfilesMenu + " in its gestionPerfilesMenuCollection field has a non-nullable fkGestionPerfiles field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(gestionPerfiles);
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

    public List<GestionPerfiles> findGestionPerfilesEntities() {
        return findGestionPerfilesEntities(true, -1, -1);
    }

    public List<GestionPerfiles> findGestionPerfilesEntities(int maxResults, int firstResult) {
        return findGestionPerfilesEntities(false, maxResults, firstResult);
    }

    private List<GestionPerfiles> findGestionPerfilesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GestionPerfiles.class));
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

    public GestionPerfiles findGestionPerfiles(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GestionPerfiles.class, id);
        } finally {
            em.close();
        }
    }

    public int getGestionPerfilesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GestionPerfiles> rt = cq.from(GestionPerfiles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
