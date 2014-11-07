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
import emcali.ami.persistence.entity.GestionFuncionarios;
import emcali.ami.persistence.entity.GestionPerfiles;
import emcali.ami.persistence.entity.GestionUsuarios;
import java.util.ArrayList;
import java.util.List;
import emcali.ami.persistence.entity.GestionPerfilesMenu;
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
        if (gestionPerfiles.getGestionUsuariosList() == null) {
            gestionPerfiles.setGestionUsuariosList(new ArrayList<GestionUsuarios>());
        }
        if (gestionPerfiles.getGestionPerfilesMenuList() == null) {
            gestionPerfiles.setGestionPerfilesMenuList(new ArrayList<GestionPerfilesMenu>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GestionFuncionarios modificadoPor = gestionPerfiles.getModificadoPor();
            if (modificadoPor != null) {
                modificadoPor = em.getReference(modificadoPor.getClass(), modificadoPor.getIdFuncionarios());
                gestionPerfiles.setModificadoPor(modificadoPor);
            }
            GestionFuncionarios creadoPor = gestionPerfiles.getCreadoPor();
            if (creadoPor != null) {
                creadoPor = em.getReference(creadoPor.getClass(), creadoPor.getIdFuncionarios());
                gestionPerfiles.setCreadoPor(creadoPor);
            }
            List<GestionUsuarios> attachedGestionUsuariosList = new ArrayList<GestionUsuarios>();
            for (GestionUsuarios gestionUsuariosListGestionUsuariosToAttach : gestionPerfiles.getGestionUsuariosList()) {
                gestionUsuariosListGestionUsuariosToAttach = em.getReference(gestionUsuariosListGestionUsuariosToAttach.getClass(), gestionUsuariosListGestionUsuariosToAttach.getIdUsuarios());
                attachedGestionUsuariosList.add(gestionUsuariosListGestionUsuariosToAttach);
            }
            gestionPerfiles.setGestionUsuariosList(attachedGestionUsuariosList);
            List<GestionPerfilesMenu> attachedGestionPerfilesMenuList = new ArrayList<GestionPerfilesMenu>();
            for (GestionPerfilesMenu gestionPerfilesMenuListGestionPerfilesMenuToAttach : gestionPerfiles.getGestionPerfilesMenuList()) {
                gestionPerfilesMenuListGestionPerfilesMenuToAttach = em.getReference(gestionPerfilesMenuListGestionPerfilesMenuToAttach.getClass(), gestionPerfilesMenuListGestionPerfilesMenuToAttach.getIdPerfilesMenu());
                attachedGestionPerfilesMenuList.add(gestionPerfilesMenuListGestionPerfilesMenuToAttach);
            }
            gestionPerfiles.setGestionPerfilesMenuList(attachedGestionPerfilesMenuList);
            em.persist(gestionPerfiles);
            if (modificadoPor != null) {
                modificadoPor.getGestionPerfilesList().add(gestionPerfiles);
                modificadoPor = em.merge(modificadoPor);
            }
            if (creadoPor != null) {
                creadoPor.getGestionPerfilesList().add(gestionPerfiles);
                creadoPor = em.merge(creadoPor);
            }
            for (GestionUsuarios gestionUsuariosListGestionUsuarios : gestionPerfiles.getGestionUsuariosList()) {
                GestionPerfiles oldFkGestionPerfilesOfGestionUsuariosListGestionUsuarios = gestionUsuariosListGestionUsuarios.getFkGestionPerfiles();
                gestionUsuariosListGestionUsuarios.setFkGestionPerfiles(gestionPerfiles);
                gestionUsuariosListGestionUsuarios = em.merge(gestionUsuariosListGestionUsuarios);
                if (oldFkGestionPerfilesOfGestionUsuariosListGestionUsuarios != null) {
                    oldFkGestionPerfilesOfGestionUsuariosListGestionUsuarios.getGestionUsuariosList().remove(gestionUsuariosListGestionUsuarios);
                    oldFkGestionPerfilesOfGestionUsuariosListGestionUsuarios = em.merge(oldFkGestionPerfilesOfGestionUsuariosListGestionUsuarios);
                }
            }
            for (GestionPerfilesMenu gestionPerfilesMenuListGestionPerfilesMenu : gestionPerfiles.getGestionPerfilesMenuList()) {
                GestionPerfiles oldFkGestionPerfilesOfGestionPerfilesMenuListGestionPerfilesMenu = gestionPerfilesMenuListGestionPerfilesMenu.getFkGestionPerfiles();
                gestionPerfilesMenuListGestionPerfilesMenu.setFkGestionPerfiles(gestionPerfiles);
                gestionPerfilesMenuListGestionPerfilesMenu = em.merge(gestionPerfilesMenuListGestionPerfilesMenu);
                if (oldFkGestionPerfilesOfGestionPerfilesMenuListGestionPerfilesMenu != null) {
                    oldFkGestionPerfilesOfGestionPerfilesMenuListGestionPerfilesMenu.getGestionPerfilesMenuList().remove(gestionPerfilesMenuListGestionPerfilesMenu);
                    oldFkGestionPerfilesOfGestionPerfilesMenuListGestionPerfilesMenu = em.merge(oldFkGestionPerfilesOfGestionPerfilesMenuListGestionPerfilesMenu);
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
            GestionFuncionarios modificadoPorOld = persistentGestionPerfiles.getModificadoPor();
            GestionFuncionarios modificadoPorNew = gestionPerfiles.getModificadoPor();
            GestionFuncionarios creadoPorOld = persistentGestionPerfiles.getCreadoPor();
            GestionFuncionarios creadoPorNew = gestionPerfiles.getCreadoPor();
            List<GestionUsuarios> gestionUsuariosListOld = persistentGestionPerfiles.getGestionUsuariosList();
            List<GestionUsuarios> gestionUsuariosListNew = gestionPerfiles.getGestionUsuariosList();
            List<GestionPerfilesMenu> gestionPerfilesMenuListOld = persistentGestionPerfiles.getGestionPerfilesMenuList();
            List<GestionPerfilesMenu> gestionPerfilesMenuListNew = gestionPerfiles.getGestionPerfilesMenuList();
            List<String> illegalOrphanMessages = null;
            for (GestionUsuarios gestionUsuariosListOldGestionUsuarios : gestionUsuariosListOld) {
                if (!gestionUsuariosListNew.contains(gestionUsuariosListOldGestionUsuarios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GestionUsuarios " + gestionUsuariosListOldGestionUsuarios + " since its fkGestionPerfiles field is not nullable.");
                }
            }
            for (GestionPerfilesMenu gestionPerfilesMenuListOldGestionPerfilesMenu : gestionPerfilesMenuListOld) {
                if (!gestionPerfilesMenuListNew.contains(gestionPerfilesMenuListOldGestionPerfilesMenu)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GestionPerfilesMenu " + gestionPerfilesMenuListOldGestionPerfilesMenu + " since its fkGestionPerfiles field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (modificadoPorNew != null) {
                modificadoPorNew = em.getReference(modificadoPorNew.getClass(), modificadoPorNew.getIdFuncionarios());
                gestionPerfiles.setModificadoPor(modificadoPorNew);
            }
            if (creadoPorNew != null) {
                creadoPorNew = em.getReference(creadoPorNew.getClass(), creadoPorNew.getIdFuncionarios());
                gestionPerfiles.setCreadoPor(creadoPorNew);
            }
            List<GestionUsuarios> attachedGestionUsuariosListNew = new ArrayList<GestionUsuarios>();
            for (GestionUsuarios gestionUsuariosListNewGestionUsuariosToAttach : gestionUsuariosListNew) {
                gestionUsuariosListNewGestionUsuariosToAttach = em.getReference(gestionUsuariosListNewGestionUsuariosToAttach.getClass(), gestionUsuariosListNewGestionUsuariosToAttach.getIdUsuarios());
                attachedGestionUsuariosListNew.add(gestionUsuariosListNewGestionUsuariosToAttach);
            }
            gestionUsuariosListNew = attachedGestionUsuariosListNew;
            gestionPerfiles.setGestionUsuariosList(gestionUsuariosListNew);
            List<GestionPerfilesMenu> attachedGestionPerfilesMenuListNew = new ArrayList<GestionPerfilesMenu>();
            for (GestionPerfilesMenu gestionPerfilesMenuListNewGestionPerfilesMenuToAttach : gestionPerfilesMenuListNew) {
                gestionPerfilesMenuListNewGestionPerfilesMenuToAttach = em.getReference(gestionPerfilesMenuListNewGestionPerfilesMenuToAttach.getClass(), gestionPerfilesMenuListNewGestionPerfilesMenuToAttach.getIdPerfilesMenu());
                attachedGestionPerfilesMenuListNew.add(gestionPerfilesMenuListNewGestionPerfilesMenuToAttach);
            }
            gestionPerfilesMenuListNew = attachedGestionPerfilesMenuListNew;
            gestionPerfiles.setGestionPerfilesMenuList(gestionPerfilesMenuListNew);
            gestionPerfiles = em.merge(gestionPerfiles);
            if (modificadoPorOld != null && !modificadoPorOld.equals(modificadoPorNew)) {
                modificadoPorOld.getGestionPerfilesList().remove(gestionPerfiles);
                modificadoPorOld = em.merge(modificadoPorOld);
            }
            if (modificadoPorNew != null && !modificadoPorNew.equals(modificadoPorOld)) {
                modificadoPorNew.getGestionPerfilesList().add(gestionPerfiles);
                modificadoPorNew = em.merge(modificadoPorNew);
            }
            if (creadoPorOld != null && !creadoPorOld.equals(creadoPorNew)) {
                creadoPorOld.getGestionPerfilesList().remove(gestionPerfiles);
                creadoPorOld = em.merge(creadoPorOld);
            }
            if (creadoPorNew != null && !creadoPorNew.equals(creadoPorOld)) {
                creadoPorNew.getGestionPerfilesList().add(gestionPerfiles);
                creadoPorNew = em.merge(creadoPorNew);
            }
            for (GestionUsuarios gestionUsuariosListNewGestionUsuarios : gestionUsuariosListNew) {
                if (!gestionUsuariosListOld.contains(gestionUsuariosListNewGestionUsuarios)) {
                    GestionPerfiles oldFkGestionPerfilesOfGestionUsuariosListNewGestionUsuarios = gestionUsuariosListNewGestionUsuarios.getFkGestionPerfiles();
                    gestionUsuariosListNewGestionUsuarios.setFkGestionPerfiles(gestionPerfiles);
                    gestionUsuariosListNewGestionUsuarios = em.merge(gestionUsuariosListNewGestionUsuarios);
                    if (oldFkGestionPerfilesOfGestionUsuariosListNewGestionUsuarios != null && !oldFkGestionPerfilesOfGestionUsuariosListNewGestionUsuarios.equals(gestionPerfiles)) {
                        oldFkGestionPerfilesOfGestionUsuariosListNewGestionUsuarios.getGestionUsuariosList().remove(gestionUsuariosListNewGestionUsuarios);
                        oldFkGestionPerfilesOfGestionUsuariosListNewGestionUsuarios = em.merge(oldFkGestionPerfilesOfGestionUsuariosListNewGestionUsuarios);
                    }
                }
            }
            for (GestionPerfilesMenu gestionPerfilesMenuListNewGestionPerfilesMenu : gestionPerfilesMenuListNew) {
                if (!gestionPerfilesMenuListOld.contains(gestionPerfilesMenuListNewGestionPerfilesMenu)) {
                    GestionPerfiles oldFkGestionPerfilesOfGestionPerfilesMenuListNewGestionPerfilesMenu = gestionPerfilesMenuListNewGestionPerfilesMenu.getFkGestionPerfiles();
                    gestionPerfilesMenuListNewGestionPerfilesMenu.setFkGestionPerfiles(gestionPerfiles);
                    gestionPerfilesMenuListNewGestionPerfilesMenu = em.merge(gestionPerfilesMenuListNewGestionPerfilesMenu);
                    if (oldFkGestionPerfilesOfGestionPerfilesMenuListNewGestionPerfilesMenu != null && !oldFkGestionPerfilesOfGestionPerfilesMenuListNewGestionPerfilesMenu.equals(gestionPerfiles)) {
                        oldFkGestionPerfilesOfGestionPerfilesMenuListNewGestionPerfilesMenu.getGestionPerfilesMenuList().remove(gestionPerfilesMenuListNewGestionPerfilesMenu);
                        oldFkGestionPerfilesOfGestionPerfilesMenuListNewGestionPerfilesMenu = em.merge(oldFkGestionPerfilesOfGestionPerfilesMenuListNewGestionPerfilesMenu);
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
            List<GestionUsuarios> gestionUsuariosListOrphanCheck = gestionPerfiles.getGestionUsuariosList();
            for (GestionUsuarios gestionUsuariosListOrphanCheckGestionUsuarios : gestionUsuariosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GestionPerfiles (" + gestionPerfiles + ") cannot be destroyed since the GestionUsuarios " + gestionUsuariosListOrphanCheckGestionUsuarios + " in its gestionUsuariosList field has a non-nullable fkGestionPerfiles field.");
            }
            List<GestionPerfilesMenu> gestionPerfilesMenuListOrphanCheck = gestionPerfiles.getGestionPerfilesMenuList();
            for (GestionPerfilesMenu gestionPerfilesMenuListOrphanCheckGestionPerfilesMenu : gestionPerfilesMenuListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GestionPerfiles (" + gestionPerfiles + ") cannot be destroyed since the GestionPerfilesMenu " + gestionPerfilesMenuListOrphanCheckGestionPerfilesMenu + " in its gestionPerfilesMenuList field has a non-nullable fkGestionPerfiles field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            GestionFuncionarios modificadoPor = gestionPerfiles.getModificadoPor();
            if (modificadoPor != null) {
                modificadoPor.getGestionPerfilesList().remove(gestionPerfiles);
                modificadoPor = em.merge(modificadoPor);
            }
            GestionFuncionarios creadoPor = gestionPerfiles.getCreadoPor();
            if (creadoPor != null) {
                creadoPor.getGestionPerfilesList().remove(gestionPerfiles);
                creadoPor = em.merge(creadoPor);
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
