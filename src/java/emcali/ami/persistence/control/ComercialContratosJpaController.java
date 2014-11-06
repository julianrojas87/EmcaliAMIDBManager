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
import java.util.Collection;
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
        if (comercialContratos.getComercialProductosCollection() == null) {
            comercialContratos.setComercialProductosCollection(new ArrayList<ComercialProductos>());
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
            Collection<ComercialProductos> attachedComercialProductosCollection = new ArrayList<ComercialProductos>();
            for (ComercialProductos comercialProductosCollectionComercialProductosToAttach : comercialContratos.getComercialProductosCollection()) {
                comercialProductosCollectionComercialProductosToAttach = em.getReference(comercialProductosCollectionComercialProductosToAttach.getClass(), comercialProductosCollectionComercialProductosToAttach.getIdProductos());
                attachedComercialProductosCollection.add(comercialProductosCollectionComercialProductosToAttach);
            }
            comercialContratos.setComercialProductosCollection(attachedComercialProductosCollection);
            em.persist(comercialContratos);
            if (fkComercialClientes != null) {
                fkComercialClientes.getComercialContratosCollection().add(comercialContratos);
                fkComercialClientes = em.merge(fkComercialClientes);
            }
            for (ComercialProductos comercialProductosCollectionComercialProductos : comercialContratos.getComercialProductosCollection()) {
                ComercialContratos oldFkComercialContratosOfComercialProductosCollectionComercialProductos = comercialProductosCollectionComercialProductos.getFkComercialContratos();
                comercialProductosCollectionComercialProductos.setFkComercialContratos(comercialContratos);
                comercialProductosCollectionComercialProductos = em.merge(comercialProductosCollectionComercialProductos);
                if (oldFkComercialContratosOfComercialProductosCollectionComercialProductos != null) {
                    oldFkComercialContratosOfComercialProductosCollectionComercialProductos.getComercialProductosCollection().remove(comercialProductosCollectionComercialProductos);
                    oldFkComercialContratosOfComercialProductosCollectionComercialProductos = em.merge(oldFkComercialContratosOfComercialProductosCollectionComercialProductos);
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
            Collection<ComercialProductos> comercialProductosCollectionOld = persistentComercialContratos.getComercialProductosCollection();
            Collection<ComercialProductos> comercialProductosCollectionNew = comercialContratos.getComercialProductosCollection();
            List<String> illegalOrphanMessages = null;
            for (ComercialProductos comercialProductosCollectionOldComercialProductos : comercialProductosCollectionOld) {
                if (!comercialProductosCollectionNew.contains(comercialProductosCollectionOldComercialProductos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ComercialProductos " + comercialProductosCollectionOldComercialProductos + " since its fkComercialContratos field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (fkComercialClientesNew != null) {
                fkComercialClientesNew = em.getReference(fkComercialClientesNew.getClass(), fkComercialClientesNew.getIdClientes());
                comercialContratos.setFkComercialClientes(fkComercialClientesNew);
            }
            Collection<ComercialProductos> attachedComercialProductosCollectionNew = new ArrayList<ComercialProductos>();
            for (ComercialProductos comercialProductosCollectionNewComercialProductosToAttach : comercialProductosCollectionNew) {
                comercialProductosCollectionNewComercialProductosToAttach = em.getReference(comercialProductosCollectionNewComercialProductosToAttach.getClass(), comercialProductosCollectionNewComercialProductosToAttach.getIdProductos());
                attachedComercialProductosCollectionNew.add(comercialProductosCollectionNewComercialProductosToAttach);
            }
            comercialProductosCollectionNew = attachedComercialProductosCollectionNew;
            comercialContratos.setComercialProductosCollection(comercialProductosCollectionNew);
            comercialContratos = em.merge(comercialContratos);
            if (fkComercialClientesOld != null && !fkComercialClientesOld.equals(fkComercialClientesNew)) {
                fkComercialClientesOld.getComercialContratosCollection().remove(comercialContratos);
                fkComercialClientesOld = em.merge(fkComercialClientesOld);
            }
            if (fkComercialClientesNew != null && !fkComercialClientesNew.equals(fkComercialClientesOld)) {
                fkComercialClientesNew.getComercialContratosCollection().add(comercialContratos);
                fkComercialClientesNew = em.merge(fkComercialClientesNew);
            }
            for (ComercialProductos comercialProductosCollectionNewComercialProductos : comercialProductosCollectionNew) {
                if (!comercialProductosCollectionOld.contains(comercialProductosCollectionNewComercialProductos)) {
                    ComercialContratos oldFkComercialContratosOfComercialProductosCollectionNewComercialProductos = comercialProductosCollectionNewComercialProductos.getFkComercialContratos();
                    comercialProductosCollectionNewComercialProductos.setFkComercialContratos(comercialContratos);
                    comercialProductosCollectionNewComercialProductos = em.merge(comercialProductosCollectionNewComercialProductos);
                    if (oldFkComercialContratosOfComercialProductosCollectionNewComercialProductos != null && !oldFkComercialContratosOfComercialProductosCollectionNewComercialProductos.equals(comercialContratos)) {
                        oldFkComercialContratosOfComercialProductosCollectionNewComercialProductos.getComercialProductosCollection().remove(comercialProductosCollectionNewComercialProductos);
                        oldFkComercialContratosOfComercialProductosCollectionNewComercialProductos = em.merge(oldFkComercialContratosOfComercialProductosCollectionNewComercialProductos);
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
            Collection<ComercialProductos> comercialProductosCollectionOrphanCheck = comercialContratos.getComercialProductosCollection();
            for (ComercialProductos comercialProductosCollectionOrphanCheckComercialProductos : comercialProductosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ComercialContratos (" + comercialContratos + ") cannot be destroyed since the ComercialProductos " + comercialProductosCollectionOrphanCheckComercialProductos + " in its comercialProductosCollection field has a non-nullable fkComercialContratos field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            ComercialClientes fkComercialClientes = comercialContratos.getFkComercialClientes();
            if (fkComercialClientes != null) {
                fkComercialClientes.getComercialContratosCollection().remove(comercialContratos);
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
