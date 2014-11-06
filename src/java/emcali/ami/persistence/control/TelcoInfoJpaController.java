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
import emcali.ami.persistence.entity.ComercialClientes;
import emcali.ami.persistence.entity.AmyMedidores;
import emcali.ami.persistence.entity.TelcoInfo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class TelcoInfoJpaController implements Serializable {

    public TelcoInfoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TelcoInfo telcoInfo) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ComercialClientes fkComercialClientes = telcoInfo.getFkComercialClientes();
            if (fkComercialClientes != null) {
                fkComercialClientes = em.getReference(fkComercialClientes.getClass(), fkComercialClientes.getIdClientes());
                telcoInfo.setFkComercialClientes(fkComercialClientes);
            }
            AmyMedidores fkAmyMedidores = telcoInfo.getFkAmyMedidores();
            if (fkAmyMedidores != null) {
                fkAmyMedidores = em.getReference(fkAmyMedidores.getClass(), fkAmyMedidores.getIdMedidores());
                telcoInfo.setFkAmyMedidores(fkAmyMedidores);
            }
            em.persist(telcoInfo);
            if (fkComercialClientes != null) {
                fkComercialClientes.getTelcoInfoCollection().add(telcoInfo);
                fkComercialClientes = em.merge(fkComercialClientes);
            }
            if (fkAmyMedidores != null) {
                fkAmyMedidores.getTelcoInfoCollection().add(telcoInfo);
                fkAmyMedidores = em.merge(fkAmyMedidores);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTelcoInfo(telcoInfo.getIdSuscriptor()) != null) {
                throw new PreexistingEntityException("TelcoInfo " + telcoInfo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TelcoInfo telcoInfo) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TelcoInfo persistentTelcoInfo = em.find(TelcoInfo.class, telcoInfo.getIdSuscriptor());
            ComercialClientes fkComercialClientesOld = persistentTelcoInfo.getFkComercialClientes();
            ComercialClientes fkComercialClientesNew = telcoInfo.getFkComercialClientes();
            AmyMedidores fkAmyMedidoresOld = persistentTelcoInfo.getFkAmyMedidores();
            AmyMedidores fkAmyMedidoresNew = telcoInfo.getFkAmyMedidores();
            if (fkComercialClientesNew != null) {
                fkComercialClientesNew = em.getReference(fkComercialClientesNew.getClass(), fkComercialClientesNew.getIdClientes());
                telcoInfo.setFkComercialClientes(fkComercialClientesNew);
            }
            if (fkAmyMedidoresNew != null) {
                fkAmyMedidoresNew = em.getReference(fkAmyMedidoresNew.getClass(), fkAmyMedidoresNew.getIdMedidores());
                telcoInfo.setFkAmyMedidores(fkAmyMedidoresNew);
            }
            telcoInfo = em.merge(telcoInfo);
            if (fkComercialClientesOld != null && !fkComercialClientesOld.equals(fkComercialClientesNew)) {
                fkComercialClientesOld.getTelcoInfoCollection().remove(telcoInfo);
                fkComercialClientesOld = em.merge(fkComercialClientesOld);
            }
            if (fkComercialClientesNew != null && !fkComercialClientesNew.equals(fkComercialClientesOld)) {
                fkComercialClientesNew.getTelcoInfoCollection().add(telcoInfo);
                fkComercialClientesNew = em.merge(fkComercialClientesNew);
            }
            if (fkAmyMedidoresOld != null && !fkAmyMedidoresOld.equals(fkAmyMedidoresNew)) {
                fkAmyMedidoresOld.getTelcoInfoCollection().remove(telcoInfo);
                fkAmyMedidoresOld = em.merge(fkAmyMedidoresOld);
            }
            if (fkAmyMedidoresNew != null && !fkAmyMedidoresNew.equals(fkAmyMedidoresOld)) {
                fkAmyMedidoresNew.getTelcoInfoCollection().add(telcoInfo);
                fkAmyMedidoresNew = em.merge(fkAmyMedidoresNew);
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
                Long id = telcoInfo.getIdSuscriptor();
                if (findTelcoInfo(id) == null) {
                    throw new NonexistentEntityException("The telcoInfo with id " + id + " no longer exists.");
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
            TelcoInfo telcoInfo;
            try {
                telcoInfo = em.getReference(TelcoInfo.class, id);
                telcoInfo.getIdSuscriptor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The telcoInfo with id " + id + " no longer exists.", enfe);
            }
            ComercialClientes fkComercialClientes = telcoInfo.getFkComercialClientes();
            if (fkComercialClientes != null) {
                fkComercialClientes.getTelcoInfoCollection().remove(telcoInfo);
                fkComercialClientes = em.merge(fkComercialClientes);
            }
            AmyMedidores fkAmyMedidores = telcoInfo.getFkAmyMedidores();
            if (fkAmyMedidores != null) {
                fkAmyMedidores.getTelcoInfoCollection().remove(telcoInfo);
                fkAmyMedidores = em.merge(fkAmyMedidores);
            }
            em.remove(telcoInfo);
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

    public List<TelcoInfo> findTelcoInfoEntities() {
        return findTelcoInfoEntities(true, -1, -1);
    }

    public List<TelcoInfo> findTelcoInfoEntities(int maxResults, int firstResult) {
        return findTelcoInfoEntities(false, maxResults, firstResult);
    }

    private List<TelcoInfo> findTelcoInfoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TelcoInfo.class));
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

    public TelcoInfo findTelcoInfo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TelcoInfo.class, id);
        } finally {
            em.close();
        }
    }

    public int getTelcoInfoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TelcoInfo> rt = cq.from(TelcoInfo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
