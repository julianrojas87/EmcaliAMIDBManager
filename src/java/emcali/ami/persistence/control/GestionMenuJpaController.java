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
import emcali.ami.persistence.entity.GestionMenu;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import emcali.ami.persistence.entity.GestionPerfilesMenu;
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
public class GestionMenuJpaController implements Serializable {

    public GestionMenuJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GestionMenu gestionMenu) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (gestionMenu.getGestionPerfilesMenuCollection() == null) {
            gestionMenu.setGestionPerfilesMenuCollection(new ArrayList<GestionPerfilesMenu>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<GestionPerfilesMenu> attachedGestionPerfilesMenuCollection = new ArrayList<GestionPerfilesMenu>();
            for (GestionPerfilesMenu gestionPerfilesMenuCollectionGestionPerfilesMenuToAttach : gestionMenu.getGestionPerfilesMenuCollection()) {
                gestionPerfilesMenuCollectionGestionPerfilesMenuToAttach = em.getReference(gestionPerfilesMenuCollectionGestionPerfilesMenuToAttach.getClass(), gestionPerfilesMenuCollectionGestionPerfilesMenuToAttach.getIdPerfilesMenu());
                attachedGestionPerfilesMenuCollection.add(gestionPerfilesMenuCollectionGestionPerfilesMenuToAttach);
            }
            gestionMenu.setGestionPerfilesMenuCollection(attachedGestionPerfilesMenuCollection);
            em.persist(gestionMenu);
            for (GestionPerfilesMenu gestionPerfilesMenuCollectionGestionPerfilesMenu : gestionMenu.getGestionPerfilesMenuCollection()) {
                GestionMenu oldFkGestionMenuOfGestionPerfilesMenuCollectionGestionPerfilesMenu = gestionPerfilesMenuCollectionGestionPerfilesMenu.getFkGestionMenu();
                gestionPerfilesMenuCollectionGestionPerfilesMenu.setFkGestionMenu(gestionMenu);
                gestionPerfilesMenuCollectionGestionPerfilesMenu = em.merge(gestionPerfilesMenuCollectionGestionPerfilesMenu);
                if (oldFkGestionMenuOfGestionPerfilesMenuCollectionGestionPerfilesMenu != null) {
                    oldFkGestionMenuOfGestionPerfilesMenuCollectionGestionPerfilesMenu.getGestionPerfilesMenuCollection().remove(gestionPerfilesMenuCollectionGestionPerfilesMenu);
                    oldFkGestionMenuOfGestionPerfilesMenuCollectionGestionPerfilesMenu = em.merge(oldFkGestionMenuOfGestionPerfilesMenuCollectionGestionPerfilesMenu);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findGestionMenu(gestionMenu.getIdMenu()) != null) {
                throw new PreexistingEntityException("GestionMenu " + gestionMenu + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GestionMenu gestionMenu) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GestionMenu persistentGestionMenu = em.find(GestionMenu.class, gestionMenu.getIdMenu());
            Collection<GestionPerfilesMenu> gestionPerfilesMenuCollectionOld = persistentGestionMenu.getGestionPerfilesMenuCollection();
            Collection<GestionPerfilesMenu> gestionPerfilesMenuCollectionNew = gestionMenu.getGestionPerfilesMenuCollection();
            List<String> illegalOrphanMessages = null;
            for (GestionPerfilesMenu gestionPerfilesMenuCollectionOldGestionPerfilesMenu : gestionPerfilesMenuCollectionOld) {
                if (!gestionPerfilesMenuCollectionNew.contains(gestionPerfilesMenuCollectionOldGestionPerfilesMenu)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GestionPerfilesMenu " + gestionPerfilesMenuCollectionOldGestionPerfilesMenu + " since its fkGestionMenu field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<GestionPerfilesMenu> attachedGestionPerfilesMenuCollectionNew = new ArrayList<GestionPerfilesMenu>();
            for (GestionPerfilesMenu gestionPerfilesMenuCollectionNewGestionPerfilesMenuToAttach : gestionPerfilesMenuCollectionNew) {
                gestionPerfilesMenuCollectionNewGestionPerfilesMenuToAttach = em.getReference(gestionPerfilesMenuCollectionNewGestionPerfilesMenuToAttach.getClass(), gestionPerfilesMenuCollectionNewGestionPerfilesMenuToAttach.getIdPerfilesMenu());
                attachedGestionPerfilesMenuCollectionNew.add(gestionPerfilesMenuCollectionNewGestionPerfilesMenuToAttach);
            }
            gestionPerfilesMenuCollectionNew = attachedGestionPerfilesMenuCollectionNew;
            gestionMenu.setGestionPerfilesMenuCollection(gestionPerfilesMenuCollectionNew);
            gestionMenu = em.merge(gestionMenu);
            for (GestionPerfilesMenu gestionPerfilesMenuCollectionNewGestionPerfilesMenu : gestionPerfilesMenuCollectionNew) {
                if (!gestionPerfilesMenuCollectionOld.contains(gestionPerfilesMenuCollectionNewGestionPerfilesMenu)) {
                    GestionMenu oldFkGestionMenuOfGestionPerfilesMenuCollectionNewGestionPerfilesMenu = gestionPerfilesMenuCollectionNewGestionPerfilesMenu.getFkGestionMenu();
                    gestionPerfilesMenuCollectionNewGestionPerfilesMenu.setFkGestionMenu(gestionMenu);
                    gestionPerfilesMenuCollectionNewGestionPerfilesMenu = em.merge(gestionPerfilesMenuCollectionNewGestionPerfilesMenu);
                    if (oldFkGestionMenuOfGestionPerfilesMenuCollectionNewGestionPerfilesMenu != null && !oldFkGestionMenuOfGestionPerfilesMenuCollectionNewGestionPerfilesMenu.equals(gestionMenu)) {
                        oldFkGestionMenuOfGestionPerfilesMenuCollectionNewGestionPerfilesMenu.getGestionPerfilesMenuCollection().remove(gestionPerfilesMenuCollectionNewGestionPerfilesMenu);
                        oldFkGestionMenuOfGestionPerfilesMenuCollectionNewGestionPerfilesMenu = em.merge(oldFkGestionMenuOfGestionPerfilesMenuCollectionNewGestionPerfilesMenu);
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
                Long id = gestionMenu.getIdMenu();
                if (findGestionMenu(id) == null) {
                    throw new NonexistentEntityException("The gestionMenu with id " + id + " no longer exists.");
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
            GestionMenu gestionMenu;
            try {
                gestionMenu = em.getReference(GestionMenu.class, id);
                gestionMenu.getIdMenu();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gestionMenu with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<GestionPerfilesMenu> gestionPerfilesMenuCollectionOrphanCheck = gestionMenu.getGestionPerfilesMenuCollection();
            for (GestionPerfilesMenu gestionPerfilesMenuCollectionOrphanCheckGestionPerfilesMenu : gestionPerfilesMenuCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GestionMenu (" + gestionMenu + ") cannot be destroyed since the GestionPerfilesMenu " + gestionPerfilesMenuCollectionOrphanCheckGestionPerfilesMenu + " in its gestionPerfilesMenuCollection field has a non-nullable fkGestionMenu field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(gestionMenu);
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

    public List<GestionMenu> findGestionMenuEntities() {
        return findGestionMenuEntities(true, -1, -1);
    }

    public List<GestionMenu> findGestionMenuEntities(int maxResults, int firstResult) {
        return findGestionMenuEntities(false, maxResults, firstResult);
    }

    private List<GestionMenu> findGestionMenuEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GestionMenu.class));
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

    public GestionMenu findGestionMenu(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GestionMenu.class, id);
        } finally {
            em.close();
        }
    }

    public int getGestionMenuCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GestionMenu> rt = cq.from(GestionMenu.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
