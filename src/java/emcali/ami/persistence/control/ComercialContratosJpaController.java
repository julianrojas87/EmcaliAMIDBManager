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
import emcali.ami.persistence.entity.ComercialClientes;
import emcali.ami.persistence.entity.ComercialContratos;
import emcali.ami.persistence.entity.ComercialProductos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class ComercialContratosJpaController implements Serializable {

    public ComercialContratosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ComercialContratos comercialContratos) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (comercialContratos.getComercialProductosList() == null) {
            comercialContratos.setComercialProductosList(new ArrayList<ComercialProductos>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ComercialClientes fkComercialClientes = comercialContratos.getFkComercialClientes();
            if (fkComercialClientes != null) {
                fkComercialClientes = em.getReference(fkComercialClientes.getClass(), fkComercialClientes.getIdClientes());
                comercialContratos.setFkComercialClientes(fkComercialClientes);
            }
            List<ComercialProductos> attachedComercialProductosList = new ArrayList<ComercialProductos>();
            for (ComercialProductos comercialProductosListComercialProductosToAttach : comercialContratos.getComercialProductosList()) {
                comercialProductosListComercialProductosToAttach = em.getReference(comercialProductosListComercialProductosToAttach.getClass(), comercialProductosListComercialProductosToAttach.getIdProductos());
                attachedComercialProductosList.add(comercialProductosListComercialProductosToAttach);
            }
            comercialContratos.setComercialProductosList(attachedComercialProductosList);
            em.persist(comercialContratos);
            if (fkComercialClientes != null) {
                fkComercialClientes.getComercialContratosList().add(comercialContratos);
                fkComercialClientes = em.merge(fkComercialClientes);
            }
            for (ComercialProductos comercialProductosListComercialProductos : comercialContratos.getComercialProductosList()) {
                ComercialContratos oldFkComercialContratosOfComercialProductosListComercialProductos = comercialProductosListComercialProductos.getFkComercialContratos();
                comercialProductosListComercialProductos.setFkComercialContratos(comercialContratos);
                comercialProductosListComercialProductos = em.merge(comercialProductosListComercialProductos);
                if (oldFkComercialContratosOfComercialProductosListComercialProductos != null) {
                    oldFkComercialContratosOfComercialProductosListComercialProductos.getComercialProductosList().remove(comercialProductosListComercialProductos);
                    oldFkComercialContratosOfComercialProductosListComercialProductos = em.merge(oldFkComercialContratosOfComercialProductosListComercialProductos);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findComercialContratos(comercialContratos.getIdContratos()) != null) {
                throw new PreexistingEntityException("ComercialContratos " + comercialContratos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ComercialContratos comercialContratos) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ComercialContratos persistentComercialContratos = em.find(ComercialContratos.class, comercialContratos.getIdContratos());
            ComercialClientes fkComercialClientesOld = persistentComercialContratos.getFkComercialClientes();
            ComercialClientes fkComercialClientesNew = comercialContratos.getFkComercialClientes();
            List<ComercialProductos> comercialProductosListOld = persistentComercialContratos.getComercialProductosList();
            List<ComercialProductos> comercialProductosListNew = comercialContratos.getComercialProductosList();
            List<String> illegalOrphanMessages = null;
            for (ComercialProductos comercialProductosListOldComercialProductos : comercialProductosListOld) {
                if (!comercialProductosListNew.contains(comercialProductosListOldComercialProductos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ComercialProductos " + comercialProductosListOldComercialProductos + " since its fkComercialContratos field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (fkComercialClientesNew != null) {
                fkComercialClientesNew = em.getReference(fkComercialClientesNew.getClass(), fkComercialClientesNew.getIdClientes());
                comercialContratos.setFkComercialClientes(fkComercialClientesNew);
            }
            List<ComercialProductos> attachedComercialProductosListNew = new ArrayList<ComercialProductos>();
            for (ComercialProductos comercialProductosListNewComercialProductosToAttach : comercialProductosListNew) {
                comercialProductosListNewComercialProductosToAttach = em.getReference(comercialProductosListNewComercialProductosToAttach.getClass(), comercialProductosListNewComercialProductosToAttach.getIdProductos());
                attachedComercialProductosListNew.add(comercialProductosListNewComercialProductosToAttach);
            }
            comercialProductosListNew = attachedComercialProductosListNew;
            comercialContratos.setComercialProductosList(comercialProductosListNew);
            comercialContratos = em.merge(comercialContratos);
            if (fkComercialClientesOld != null && !fkComercialClientesOld.equals(fkComercialClientesNew)) {
                fkComercialClientesOld.getComercialContratosList().remove(comercialContratos);
                fkComercialClientesOld = em.merge(fkComercialClientesOld);
            }
            if (fkComercialClientesNew != null && !fkComercialClientesNew.equals(fkComercialClientesOld)) {
                fkComercialClientesNew.getComercialContratosList().add(comercialContratos);
                fkComercialClientesNew = em.merge(fkComercialClientesNew);
            }
            for (ComercialProductos comercialProductosListNewComercialProductos : comercialProductosListNew) {
                if (!comercialProductosListOld.contains(comercialProductosListNewComercialProductos)) {
                    ComercialContratos oldFkComercialContratosOfComercialProductosListNewComercialProductos = comercialProductosListNewComercialProductos.getFkComercialContratos();
                    comercialProductosListNewComercialProductos.setFkComercialContratos(comercialContratos);
                    comercialProductosListNewComercialProductos = em.merge(comercialProductosListNewComercialProductos);
                    if (oldFkComercialContratosOfComercialProductosListNewComercialProductos != null && !oldFkComercialContratosOfComercialProductosListNewComercialProductos.equals(comercialContratos)) {
                        oldFkComercialContratosOfComercialProductosListNewComercialProductos.getComercialProductosList().remove(comercialProductosListNewComercialProductos);
                        oldFkComercialContratosOfComercialProductosListNewComercialProductos = em.merge(oldFkComercialContratosOfComercialProductosListNewComercialProductos);
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
                Long id = comercialContratos.getIdContratos();
                if (findComercialContratos(id) == null) {
                    throw new NonexistentEntityException("The comercialContratos with id " + id + " no longer exists.");
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
            ComercialContratos comercialContratos;
            try {
                comercialContratos = em.getReference(ComercialContratos.class, id);
                comercialContratos.getIdContratos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comercialContratos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ComercialProductos> comercialProductosListOrphanCheck = comercialContratos.getComercialProductosList();
            for (ComercialProductos comercialProductosListOrphanCheckComercialProductos : comercialProductosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ComercialContratos (" + comercialContratos + ") cannot be destroyed since the ComercialProductos " + comercialProductosListOrphanCheckComercialProductos + " in its comercialProductosList field has a non-nullable fkComercialContratos field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            ComercialClientes fkComercialClientes = comercialContratos.getFkComercialClientes();
            if (fkComercialClientes != null) {
                fkComercialClientes.getComercialContratosList().remove(comercialContratos);
                fkComercialClientes = em.merge(fkComercialClientes);
            }
            em.remove(comercialContratos);
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

    public List<ComercialContratos> findComercialContratosEntities() {
        return findComercialContratosEntities(true, -1, -1);
    }

    public List<ComercialContratos> findComercialContratosEntities(int maxResults, int firstResult) {
        return findComercialContratosEntities(false, maxResults, firstResult);
    }

    private List<ComercialContratos> findComercialContratosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ComercialContratos.class));
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

    public ComercialContratos findComercialContratos(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ComercialContratos.class, id);
        } finally {
            em.close();
        }
    }

    public int getComercialContratosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ComercialContratos> rt = cq.from(ComercialContratos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
