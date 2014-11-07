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
        if (gestionMenu.getGestionPerfilesMenuList() == null) {
            gestionMenu.setGestionPerfilesMenuList(new ArrayList<GestionPerfilesMenu>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<GestionPerfilesMenu> attachedGestionPerfilesMenuList = new ArrayList<GestionPerfilesMenu>();
            for (GestionPerfilesMenu gestionPerfilesMenuListGestionPerfilesMenuToAttach : gestionMenu.getGestionPerfilesMenuList()) {
                gestionPerfilesMenuListGestionPerfilesMenuToAttach = em.getReference(gestionPerfilesMenuListGestionPerfilesMenuToAttach.getClass(), gestionPerfilesMenuListGestionPerfilesMenuToAttach.getIdPerfilesMenu());
                attachedGestionPerfilesMenuList.add(gestionPerfilesMenuListGestionPerfilesMenuToAttach);
            }
            gestionMenu.setGestionPerfilesMenuList(attachedGestionPerfilesMenuList);
            em.persist(gestionMenu);
            for (GestionPerfilesMenu gestionPerfilesMenuListGestionPerfilesMenu : gestionMenu.getGestionPerfilesMenuList()) {
                GestionMenu oldFkGestionMenuOfGestionPerfilesMenuListGestionPerfilesMenu = gestionPerfilesMenuListGestionPerfilesMenu.getFkGestionMenu();
                gestionPerfilesMenuListGestionPerfilesMenu.setFkGestionMenu(gestionMenu);
                gestionPerfilesMenuListGestionPerfilesMenu = em.merge(gestionPerfilesMenuListGestionPerfilesMenu);
                if (oldFkGestionMenuOfGestionPerfilesMenuListGestionPerfilesMenu != null) {
                    oldFkGestionMenuOfGestionPerfilesMenuListGestionPerfilesMenu.getGestionPerfilesMenuList().remove(gestionPerfilesMenuListGestionPerfilesMenu);
                    oldFkGestionMenuOfGestionPerfilesMenuListGestionPerfilesMenu = em.merge(oldFkGestionMenuOfGestionPerfilesMenuListGestionPerfilesMenu);
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
            List<GestionPerfilesMenu> gestionPerfilesMenuListOld = persistentGestionMenu.getGestionPerfilesMenuList();
            List<GestionPerfilesMenu> gestionPerfilesMenuListNew = gestionMenu.getGestionPerfilesMenuList();
            List<String> illegalOrphanMessages = null;
            for (GestionPerfilesMenu gestionPerfilesMenuListOldGestionPerfilesMenu : gestionPerfilesMenuListOld) {
                if (!gestionPerfilesMenuListNew.contains(gestionPerfilesMenuListOldGestionPerfilesMenu)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GestionPerfilesMenu " + gestionPerfilesMenuListOldGestionPerfilesMenu + " since its fkGestionMenu field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<GestionPerfilesMenu> attachedGestionPerfilesMenuListNew = new ArrayList<GestionPerfilesMenu>();
            for (GestionPerfilesMenu gestionPerfilesMenuListNewGestionPerfilesMenuToAttach : gestionPerfilesMenuListNew) {
                gestionPerfilesMenuListNewGestionPerfilesMenuToAttach = em.getReference(gestionPerfilesMenuListNewGestionPerfilesMenuToAttach.getClass(), gestionPerfilesMenuListNewGestionPerfilesMenuToAttach.getIdPerfilesMenu());
                attachedGestionPerfilesMenuListNew.add(gestionPerfilesMenuListNewGestionPerfilesMenuToAttach);
            }
            gestionPerfilesMenuListNew = attachedGestionPerfilesMenuListNew;
            gestionMenu.setGestionPerfilesMenuList(gestionPerfilesMenuListNew);
            gestionMenu = em.merge(gestionMenu);
            for (GestionPerfilesMenu gestionPerfilesMenuListNewGestionPerfilesMenu : gestionPerfilesMenuListNew) {
                if (!gestionPerfilesMenuListOld.contains(gestionPerfilesMenuListNewGestionPerfilesMenu)) {
                    GestionMenu oldFkGestionMenuOfGestionPerfilesMenuListNewGestionPerfilesMenu = gestionPerfilesMenuListNewGestionPerfilesMenu.getFkGestionMenu();
                    gestionPerfilesMenuListNewGestionPerfilesMenu.setFkGestionMenu(gestionMenu);
                    gestionPerfilesMenuListNewGestionPerfilesMenu = em.merge(gestionPerfilesMenuListNewGestionPerfilesMenu);
                    if (oldFkGestionMenuOfGestionPerfilesMenuListNewGestionPerfilesMenu != null && !oldFkGestionMenuOfGestionPerfilesMenuListNewGestionPerfilesMenu.equals(gestionMenu)) {
                        oldFkGestionMenuOfGestionPerfilesMenuListNewGestionPerfilesMenu.getGestionPerfilesMenuList().remove(gestionPerfilesMenuListNewGestionPerfilesMenu);
                        oldFkGestionMenuOfGestionPerfilesMenuListNewGestionPerfilesMenu = em.merge(oldFkGestionMenuOfGestionPerfilesMenuListNewGestionPerfilesMenu);
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
            List<GestionPerfilesMenu> gestionPerfilesMenuListOrphanCheck = gestionMenu.getGestionPerfilesMenuList();
            for (GestionPerfilesMenu gestionPerfilesMenuListOrphanCheckGestionPerfilesMenu : gestionPerfilesMenuListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GestionMenu (" + gestionMenu + ") cannot be destroyed since the GestionPerfilesMenu " + gestionPerfilesMenuListOrphanCheckGestionPerfilesMenu + " in its gestionPerfilesMenuList field has a non-nullable fkGestionMenu field.");
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
