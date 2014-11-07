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
import emcali.ami.persistence.entity.GestionFuncionarios;
import emcali.ami.persistence.entity.GestionUsuarios;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class GestionUsuariosJpaController implements Serializable {

    public GestionUsuariosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GestionUsuarios gestionUsuarios) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GestionPerfiles fkGestionPerfiles = gestionUsuarios.getFkGestionPerfiles();
            if (fkGestionPerfiles != null) {
                fkGestionPerfiles = em.getReference(fkGestionPerfiles.getClass(), fkGestionPerfiles.getIdPerfiles());
                gestionUsuarios.setFkGestionPerfiles(fkGestionPerfiles);
            }
            GestionFuncionarios fkGestionFuncionarios = gestionUsuarios.getFkGestionFuncionarios();
            if (fkGestionFuncionarios != null) {
                fkGestionFuncionarios = em.getReference(fkGestionFuncionarios.getClass(), fkGestionFuncionarios.getIdFuncionarios());
                gestionUsuarios.setFkGestionFuncionarios(fkGestionFuncionarios);
            }
            GestionFuncionarios creadoPor = gestionUsuarios.getCreadoPor();
            if (creadoPor != null) {
                creadoPor = em.getReference(creadoPor.getClass(), creadoPor.getIdFuncionarios());
                gestionUsuarios.setCreadoPor(creadoPor);
            }
            GestionFuncionarios modificadoPor = gestionUsuarios.getModificadoPor();
            if (modificadoPor != null) {
                modificadoPor = em.getReference(modificadoPor.getClass(), modificadoPor.getIdFuncionarios());
                gestionUsuarios.setModificadoPor(modificadoPor);
            }
            em.persist(gestionUsuarios);
            if (fkGestionPerfiles != null) {
                fkGestionPerfiles.getGestionUsuariosList().add(gestionUsuarios);
                fkGestionPerfiles = em.merge(fkGestionPerfiles);
            }
            if (fkGestionFuncionarios != null) {
                fkGestionFuncionarios.getGestionUsuariosList().add(gestionUsuarios);
                fkGestionFuncionarios = em.merge(fkGestionFuncionarios);
            }
            if (creadoPor != null) {
                creadoPor.getGestionUsuariosList().add(gestionUsuarios);
                creadoPor = em.merge(creadoPor);
            }
            if (modificadoPor != null) {
                modificadoPor.getGestionUsuariosList().add(gestionUsuarios);
                modificadoPor = em.merge(modificadoPor);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findGestionUsuarios(gestionUsuarios.getIdUsuarios()) != null) {
                throw new PreexistingEntityException("GestionUsuarios " + gestionUsuarios + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GestionUsuarios gestionUsuarios) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GestionUsuarios persistentGestionUsuarios = em.find(GestionUsuarios.class, gestionUsuarios.getIdUsuarios());
            GestionPerfiles fkGestionPerfilesOld = persistentGestionUsuarios.getFkGestionPerfiles();
            GestionPerfiles fkGestionPerfilesNew = gestionUsuarios.getFkGestionPerfiles();
            GestionFuncionarios fkGestionFuncionariosOld = persistentGestionUsuarios.getFkGestionFuncionarios();
            GestionFuncionarios fkGestionFuncionariosNew = gestionUsuarios.getFkGestionFuncionarios();
            GestionFuncionarios creadoPorOld = persistentGestionUsuarios.getCreadoPor();
            GestionFuncionarios creadoPorNew = gestionUsuarios.getCreadoPor();
            GestionFuncionarios modificadoPorOld = persistentGestionUsuarios.getModificadoPor();
            GestionFuncionarios modificadoPorNew = gestionUsuarios.getModificadoPor();
            if (fkGestionPerfilesNew != null) {
                fkGestionPerfilesNew = em.getReference(fkGestionPerfilesNew.getClass(), fkGestionPerfilesNew.getIdPerfiles());
                gestionUsuarios.setFkGestionPerfiles(fkGestionPerfilesNew);
            }
            if (fkGestionFuncionariosNew != null) {
                fkGestionFuncionariosNew = em.getReference(fkGestionFuncionariosNew.getClass(), fkGestionFuncionariosNew.getIdFuncionarios());
                gestionUsuarios.setFkGestionFuncionarios(fkGestionFuncionariosNew);
            }
            if (creadoPorNew != null) {
                creadoPorNew = em.getReference(creadoPorNew.getClass(), creadoPorNew.getIdFuncionarios());
                gestionUsuarios.setCreadoPor(creadoPorNew);
            }
            if (modificadoPorNew != null) {
                modificadoPorNew = em.getReference(modificadoPorNew.getClass(), modificadoPorNew.getIdFuncionarios());
                gestionUsuarios.setModificadoPor(modificadoPorNew);
            }
            gestionUsuarios = em.merge(gestionUsuarios);
            if (fkGestionPerfilesOld != null && !fkGestionPerfilesOld.equals(fkGestionPerfilesNew)) {
                fkGestionPerfilesOld.getGestionUsuariosList().remove(gestionUsuarios);
                fkGestionPerfilesOld = em.merge(fkGestionPerfilesOld);
            }
            if (fkGestionPerfilesNew != null && !fkGestionPerfilesNew.equals(fkGestionPerfilesOld)) {
                fkGestionPerfilesNew.getGestionUsuariosList().add(gestionUsuarios);
                fkGestionPerfilesNew = em.merge(fkGestionPerfilesNew);
            }
            if (fkGestionFuncionariosOld != null && !fkGestionFuncionariosOld.equals(fkGestionFuncionariosNew)) {
                fkGestionFuncionariosOld.getGestionUsuariosList().remove(gestionUsuarios);
                fkGestionFuncionariosOld = em.merge(fkGestionFuncionariosOld);
            }
            if (fkGestionFuncionariosNew != null && !fkGestionFuncionariosNew.equals(fkGestionFuncionariosOld)) {
                fkGestionFuncionariosNew.getGestionUsuariosList().add(gestionUsuarios);
                fkGestionFuncionariosNew = em.merge(fkGestionFuncionariosNew);
            }
            if (creadoPorOld != null && !creadoPorOld.equals(creadoPorNew)) {
                creadoPorOld.getGestionUsuariosList().remove(gestionUsuarios);
                creadoPorOld = em.merge(creadoPorOld);
            }
            if (creadoPorNew != null && !creadoPorNew.equals(creadoPorOld)) {
                creadoPorNew.getGestionUsuariosList().add(gestionUsuarios);
                creadoPorNew = em.merge(creadoPorNew);
            }
            if (modificadoPorOld != null && !modificadoPorOld.equals(modificadoPorNew)) {
                modificadoPorOld.getGestionUsuariosList().remove(gestionUsuarios);
                modificadoPorOld = em.merge(modificadoPorOld);
            }
            if (modificadoPorNew != null && !modificadoPorNew.equals(modificadoPorOld)) {
                modificadoPorNew.getGestionUsuariosList().add(gestionUsuarios);
                modificadoPorNew = em.merge(modificadoPorNew);
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
                Long id = gestionUsuarios.getIdUsuarios();
                if (findGestionUsuarios(id) == null) {
                    throw new NonexistentEntityException("The gestionUsuarios with id " + id + " no longer exists.");
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
            GestionUsuarios gestionUsuarios;
            try {
                gestionUsuarios = em.getReference(GestionUsuarios.class, id);
                gestionUsuarios.getIdUsuarios();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gestionUsuarios with id " + id + " no longer exists.", enfe);
            }
            GestionPerfiles fkGestionPerfiles = gestionUsuarios.getFkGestionPerfiles();
            if (fkGestionPerfiles != null) {
                fkGestionPerfiles.getGestionUsuariosList().remove(gestionUsuarios);
                fkGestionPerfiles = em.merge(fkGestionPerfiles);
            }
            GestionFuncionarios fkGestionFuncionarios = gestionUsuarios.getFkGestionFuncionarios();
            if (fkGestionFuncionarios != null) {
                fkGestionFuncionarios.getGestionUsuariosList().remove(gestionUsuarios);
                fkGestionFuncionarios = em.merge(fkGestionFuncionarios);
            }
            GestionFuncionarios creadoPor = gestionUsuarios.getCreadoPor();
            if (creadoPor != null) {
                creadoPor.getGestionUsuariosList().remove(gestionUsuarios);
                creadoPor = em.merge(creadoPor);
            }
            GestionFuncionarios modificadoPor = gestionUsuarios.getModificadoPor();
            if (modificadoPor != null) {
                modificadoPor.getGestionUsuariosList().remove(gestionUsuarios);
                modificadoPor = em.merge(modificadoPor);
            }
            em.remove(gestionUsuarios);
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

    public List<GestionUsuarios> findGestionUsuariosEntities() {
        return findGestionUsuariosEntities(true, -1, -1);
    }

    public List<GestionUsuarios> findGestionUsuariosEntities(int maxResults, int firstResult) {
        return findGestionUsuariosEntities(false, maxResults, firstResult);
    }

    private List<GestionUsuarios> findGestionUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GestionUsuarios.class));
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

    public GestionUsuarios findGestionUsuarios(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GestionUsuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getGestionUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GestionUsuarios> rt = cq.from(GestionUsuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
