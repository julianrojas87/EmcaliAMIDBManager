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
import emcali.ami.persistence.entity.ComercialClientes;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import emcali.ami.persistence.entity.TelcoInfo;
import java.util.ArrayList;
import java.util.Collection;
import emcali.ami.persistence.entity.ComercialContratos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class ComercialClientesJpaController implements Serializable {

    public ComercialClientesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ComercialClientes comercialClientes) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (comercialClientes.getTelcoInfoCollection() == null) {
            comercialClientes.setTelcoInfoCollection(new ArrayList<TelcoInfo>());
        }
        if (comercialClientes.getComercialContratosCollection() == null) {
            comercialClientes.setComercialContratosCollection(new ArrayList<ComercialContratos>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<TelcoInfo> attachedTelcoInfoCollection = new ArrayList<TelcoInfo>();
            for (TelcoInfo telcoInfoCollectionTelcoInfoToAttach : comercialClientes.getTelcoInfoCollection()) {
                telcoInfoCollectionTelcoInfoToAttach = em.getReference(telcoInfoCollectionTelcoInfoToAttach.getClass(), telcoInfoCollectionTelcoInfoToAttach.getIdSuscriptor());
                attachedTelcoInfoCollection.add(telcoInfoCollectionTelcoInfoToAttach);
            }
            comercialClientes.setTelcoInfoCollection(attachedTelcoInfoCollection);
            Collection<ComercialContratos> attachedComercialContratosCollection = new ArrayList<ComercialContratos>();
            for (ComercialContratos comercialContratosCollectionComercialContratosToAttach : comercialClientes.getComercialContratosCollection()) {
                comercialContratosCollectionComercialContratosToAttach = em.getReference(comercialContratosCollectionComercialContratosToAttach.getClass(), comercialContratosCollectionComercialContratosToAttach.getIdContratos());
                attachedComercialContratosCollection.add(comercialContratosCollectionComercialContratosToAttach);
            }
            comercialClientes.setComercialContratosCollection(attachedComercialContratosCollection);
            em.persist(comercialClientes);
            for (TelcoInfo telcoInfoCollectionTelcoInfo : comercialClientes.getTelcoInfoCollection()) {
                ComercialClientes oldFkComercialClientesOfTelcoInfoCollectionTelcoInfo = telcoInfoCollectionTelcoInfo.getFkComercialClientes();
                telcoInfoCollectionTelcoInfo.setFkComercialClientes(comercialClientes);
                telcoInfoCollectionTelcoInfo = em.merge(telcoInfoCollectionTelcoInfo);
                if (oldFkComercialClientesOfTelcoInfoCollectionTelcoInfo != null) {
                    oldFkComercialClientesOfTelcoInfoCollectionTelcoInfo.getTelcoInfoCollection().remove(telcoInfoCollectionTelcoInfo);
                    oldFkComercialClientesOfTelcoInfoCollectionTelcoInfo = em.merge(oldFkComercialClientesOfTelcoInfoCollectionTelcoInfo);
                }
            }
            for (ComercialContratos comercialContratosCollectionComercialContratos : comercialClientes.getComercialContratosCollection()) {
                ComercialClientes oldFkComercialClientesOfComercialContratosCollectionComercialContratos = comercialContratosCollectionComercialContratos.getFkComercialClientes();
                comercialContratosCollectionComercialContratos.setFkComercialClientes(comercialClientes);
                comercialContratosCollectionComercialContratos = em.merge(comercialContratosCollectionComercialContratos);
                if (oldFkComercialClientesOfComercialContratosCollectionComercialContratos != null) {
                    oldFkComercialClientesOfComercialContratosCollectionComercialContratos.getComercialContratosCollection().remove(comercialContratosCollectionComercialContratos);
                    oldFkComercialClientesOfComercialContratosCollectionComercialContratos = em.merge(oldFkComercialClientesOfComercialContratosCollectionComercialContratos);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findComercialClientes(comercialClientes.getIdClientes()) != null) {
                throw new PreexistingEntityException("ComercialClientes " + comercialClientes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ComercialClientes comercialClientes) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ComercialClientes persistentComercialClientes = em.find(ComercialClientes.class, comercialClientes.getIdClientes());
            Collection<TelcoInfo> telcoInfoCollectionOld = persistentComercialClientes.getTelcoInfoCollection();
            Collection<TelcoInfo> telcoInfoCollectionNew = comercialClientes.getTelcoInfoCollection();
            Collection<ComercialContratos> comercialContratosCollectionOld = persistentComercialClientes.getComercialContratosCollection();
            Collection<ComercialContratos> comercialContratosCollectionNew = comercialClientes.getComercialContratosCollection();
            List<String> illegalOrphanMessages = null;
            for (TelcoInfo telcoInfoCollectionOldTelcoInfo : telcoInfoCollectionOld) {
                if (!telcoInfoCollectionNew.contains(telcoInfoCollectionOldTelcoInfo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TelcoInfo " + telcoInfoCollectionOldTelcoInfo + " since its fkComercialClientes field is not nullable.");
                }
            }
            for (ComercialContratos comercialContratosCollectionOldComercialContratos : comercialContratosCollectionOld) {
                if (!comercialContratosCollectionNew.contains(comercialContratosCollectionOldComercialContratos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ComercialContratos " + comercialContratosCollectionOldComercialContratos + " since its fkComercialClientes field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<TelcoInfo> attachedTelcoInfoCollectionNew = new ArrayList<TelcoInfo>();
            for (TelcoInfo telcoInfoCollectionNewTelcoInfoToAttach : telcoInfoCollectionNew) {
                telcoInfoCollectionNewTelcoInfoToAttach = em.getReference(telcoInfoCollectionNewTelcoInfoToAttach.getClass(), telcoInfoCollectionNewTelcoInfoToAttach.getIdSuscriptor());
                attachedTelcoInfoCollectionNew.add(telcoInfoCollectionNewTelcoInfoToAttach);
            }
            telcoInfoCollectionNew = attachedTelcoInfoCollectionNew;
            comercialClientes.setTelcoInfoCollection(telcoInfoCollectionNew);
            Collection<ComercialContratos> attachedComercialContratosCollectionNew = new ArrayList<ComercialContratos>();
            for (ComercialContratos comercialContratosCollectionNewComercialContratosToAttach : comercialContratosCollectionNew) {
                comercialContratosCollectionNewComercialContratosToAttach = em.getReference(comercialContratosCollectionNewComercialContratosToAttach.getClass(), comercialContratosCollectionNewComercialContratosToAttach.getIdContratos());
                attachedComercialContratosCollectionNew.add(comercialContratosCollectionNewComercialContratosToAttach);
            }
            comercialContratosCollectionNew = attachedComercialContratosCollectionNew;
            comercialClientes.setComercialContratosCollection(comercialContratosCollectionNew);
            comercialClientes = em.merge(comercialClientes);
            for (TelcoInfo telcoInfoCollectionNewTelcoInfo : telcoInfoCollectionNew) {
                if (!telcoInfoCollectionOld.contains(telcoInfoCollectionNewTelcoInfo)) {
                    ComercialClientes oldFkComercialClientesOfTelcoInfoCollectionNewTelcoInfo = telcoInfoCollectionNewTelcoInfo.getFkComercialClientes();
                    telcoInfoCollectionNewTelcoInfo.setFkComercialClientes(comercialClientes);
                    telcoInfoCollectionNewTelcoInfo = em.merge(telcoInfoCollectionNewTelcoInfo);
                    if (oldFkComercialClientesOfTelcoInfoCollectionNewTelcoInfo != null && !oldFkComercialClientesOfTelcoInfoCollectionNewTelcoInfo.equals(comercialClientes)) {
                        oldFkComercialClientesOfTelcoInfoCollectionNewTelcoInfo.getTelcoInfoCollection().remove(telcoInfoCollectionNewTelcoInfo);
                        oldFkComercialClientesOfTelcoInfoCollectionNewTelcoInfo = em.merge(oldFkComercialClientesOfTelcoInfoCollectionNewTelcoInfo);
                    }
                }
            }
            for (ComercialContratos comercialContratosCollectionNewComercialContratos : comercialContratosCollectionNew) {
                if (!comercialContratosCollectionOld.contains(comercialContratosCollectionNewComercialContratos)) {
                    ComercialClientes oldFkComercialClientesOfComercialContratosCollectionNewComercialContratos = comercialContratosCollectionNewComercialContratos.getFkComercialClientes();
                    comercialContratosCollectionNewComercialContratos.setFkComercialClientes(comercialClientes);
                    comercialContratosCollectionNewComercialContratos = em.merge(comercialContratosCollectionNewComercialContratos);
                    if (oldFkComercialClientesOfComercialContratosCollectionNewComercialContratos != null && !oldFkComercialClientesOfComercialContratosCollectionNewComercialContratos.equals(comercialClientes)) {
                        oldFkComercialClientesOfComercialContratosCollectionNewComercialContratos.getComercialContratosCollection().remove(comercialContratosCollectionNewComercialContratos);
                        oldFkComercialClientesOfComercialContratosCollectionNewComercialContratos = em.merge(oldFkComercialClientesOfComercialContratosCollectionNewComercialContratos);
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
                Long id = comercialClientes.getIdClientes();
                if (findComercialClientes(id) == null) {
                    throw new NonexistentEntityException("The comercialClientes with id " + id + " no longer exists.");
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
            ComercialClientes comercialClientes;
            try {
                comercialClientes = em.getReference(ComercialClientes.class, id);
                comercialClientes.getIdClientes();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comercialClientes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<TelcoInfo> telcoInfoCollectionOrphanCheck = comercialClientes.getTelcoInfoCollection();
            for (TelcoInfo telcoInfoCollectionOrphanCheckTelcoInfo : telcoInfoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ComercialClientes (" + comercialClientes + ") cannot be destroyed since the TelcoInfo " + telcoInfoCollectionOrphanCheckTelcoInfo + " in its telcoInfoCollection field has a non-nullable fkComercialClientes field.");
            }
            Collection<ComercialContratos> comercialContratosCollectionOrphanCheck = comercialClientes.getComercialContratosCollection();
            for (ComercialContratos comercialContratosCollectionOrphanCheckComercialContratos : comercialContratosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ComercialClientes (" + comercialClientes + ") cannot be destroyed since the ComercialContratos " + comercialContratosCollectionOrphanCheckComercialContratos + " in its comercialContratosCollection field has a non-nullable fkComercialClientes field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(comercialClientes);
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

    public List<ComercialClientes> findComercialClientesEntities() {
        return findComercialClientesEntities(true, -1, -1);
    }

    public List<ComercialClientes> findComercialClientesEntities(int maxResults, int firstResult) {
        return findComercialClientesEntities(false, maxResults, firstResult);
    }

    private List<ComercialClientes> findComercialClientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ComercialClientes.class));
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

    public ComercialClientes findComercialClientes(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ComercialClientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getComercialClientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ComercialClientes> rt = cq.from(ComercialClientes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
