/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emcali.ami.persistence.control;

import emcali.ami.persistence.control.exceptions.NonexistentEntityException;
import emcali.ami.persistence.control.exceptions.PreexistingEntityException;
import emcali.ami.persistence.control.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import emcali.ami.persistence.entity.GestionPerfiles;
import emcali.ami.persistence.entity.GestionMenu;
import emcali.ami.persistence.entity.GestionPerfilesMenu;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class GestionPerfilesMenuJpaController implements Serializable {

    public GestionPerfilesMenuJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GestionPerfilesMenu gestionPerfilesMenu) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GestionPerfiles fkGestionPerfiles = gestionPerfilesMenu.getFkGestionPerfiles();
            if (fkGestionPerfiles != null) {
                fkGestionPerfiles = em.getReference(fkGestionPerfiles.getClass(), fkGestionPerfiles.getIdPerfiles());
                gestionPerfilesMenu.setFkGestionPerfiles(fkGestionPerfiles);
            }
            GestionMenu fkGestionMenu = gestionPerfilesMenu.getFkGestionMenu();
            if (fkGestionMenu != null) {
                fkGestionMenu = em.getReference(fkGestionMenu.getClass(), fkGestionMenu.getIdMenu());
                gestionPerfilesMenu.setFkGestionMenu(fkGestionMenu);
            }
            em.persist(gestionPerfilesMenu);
            if (fkGestionPerfiles != null) {
                fkGestionPerfiles.getGestionPerfilesMenuList().add(gestionPerfilesMenu);
                fkGestionPerfiles = em.merge(fkGestionPerfiles);
            }
            if (fkGestionMenu != null) {
                fkGestionMenu.getGestionPerfilesMenuList().add(gestionPerfilesMenu);
                fkGestionMenu = em.merge(fkGestionMenu);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findGestionPerfilesMenu(gestionPerfilesMenu.getIdPerfilesMenu()) != null) {
                throw new PreexistingEntityException("GestionPerfilesMenu " + gestionPerfilesMenu + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GestionPerfilesMenu gestionPerfilesMenu) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GestionPerfilesMenu persistentGestionPerfilesMenu = em.find(GestionPerfilesMenu.class, gestionPerfilesMenu.getIdPerfilesMenu());
            GestionPerfiles fkGestionPerfilesOld = persistentGestionPerfilesMenu.getFkGestionPerfiles();
            GestionPerfiles fkGestionPerfilesNew = gestionPerfilesMenu.getFkGestionPerfiles();
            GestionMenu fkGestionMenuOld = persistentGestionPerfilesMenu.getFkGestionMenu();
            GestionMenu fkGestionMenuNew = gestionPerfilesMenu.getFkGestionMenu();
            if (fkGestionPerfilesNew != null) {
                fkGestionPerfilesNew = em.getReference(fkGestionPerfilesNew.getClass(), fkGestionPerfilesNew.getIdPerfiles());
                gestionPerfilesMenu.setFkGestionPerfiles(fkGestionPerfilesNew);
            }
            if (fkGestionMenuNew != null) {
                fkGestionMenuNew = em.getReference(fkGestionMenuNew.getClass(), fkGestionMenuNew.getIdMenu());
                gestionPerfilesMenu.setFkGestionMenu(fkGestionMenuNew);
            }
            gestionPerfilesMenu = em.merge(gestionPerfilesMenu);
            if (fkGestionPerfilesOld != null && !fkGestionPerfilesOld.equals(fkGestionPerfilesNew)) {
                fkGestionPerfilesOld.getGestionPerfilesMenuList().remove(gestionPerfilesMenu);
                fkGestionPerfilesOld = em.merge(fkGestionPerfilesOld);
            }
            if (fkGestionPerfilesNew != null && !fkGestionPerfilesNew.equals(fkGestionPerfilesOld)) {
                fkGestionPerfilesNew.getGestionPerfilesMenuList().add(gestionPerfilesMenu);
                fkGestionPerfilesNew = em.merge(fkGestionPerfilesNew);
            }
            if (fkGestionMenuOld != null && !fkGestionMenuOld.equals(fkGestionMenuNew)) {
                fkGestionMenuOld.getGestionPerfilesMenuList().remove(gestionPerfilesMenu);
                fkGestionMenuOld = em.merge(fkGestionMenuOld);
            }
            if (fkGestionMenuNew != null && !fkGestionMenuNew.equals(fkGestionMenuOld)) {
                fkGestionMenuNew.getGestionPerfilesMenuList().add(gestionPerfilesMenu);
                fkGestionMenuNew = em.merge(fkGestionMenuNew);
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
                Long id = gestionPerfilesMenu.getIdPerfilesMenu();
                if (findGestionPerfilesMenu(id) == null) {
                    throw new NonexistentEntityException("The gestionPerfilesMenu with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GestionPerfilesMenu gestionPerfilesMenu;
            try {
                gestionPerfilesMenu = em.getReference(GestionPerfilesMenu.class, id);
                gestionPerfilesMenu.getIdPerfilesMenu();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gestionPerfilesMenu with id " + id + " no longer exists.", enfe);
            }
            GestionPerfiles fkGestionPerfiles = gestionPerfilesMenu.getFkGestionPerfiles();
            if (fkGestionPerfiles != null) {
                fkGestionPerfiles.getGestionPerfilesMenuList().remove(gestionPerfilesMenu);
                fkGestionPerfiles = em.merge(fkGestionPerfiles);
            }
            GestionMenu fkGestionMenu = gestionPerfilesMenu.getFkGestionMenu();
            if (fkGestionMenu != null) {
                fkGestionMenu.getGestionPerfilesMenuList().remove(gestionPerfilesMenu);
                fkGestionMenu = em.merge(fkGestionMenu);
            }
            em.remove(gestionPerfilesMenu);
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

    public List<GestionPerfilesMenu> findGestionPerfilesMenuEntities() {
        return findGestionPerfilesMenuEntities(true, -1, -1);
    }

    public List<GestionPerfilesMenu> findGestionPerfilesMenuEntities(int maxResults, int firstResult) {
        return findGestionPerfilesMenuEntities(false, maxResults, firstResult);
    }

    private List<GestionPerfilesMenu> findGestionPerfilesMenuEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GestionPerfilesMenu.class));
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

    public GestionPerfilesMenu findGestionPerfilesMenu(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GestionPerfilesMenu.class, id);
        } finally {
            em.close();
        }
    }

    public int getGestionPerfilesMenuCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GestionPerfilesMenu> rt = cq.from(GestionPerfilesMenu.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
