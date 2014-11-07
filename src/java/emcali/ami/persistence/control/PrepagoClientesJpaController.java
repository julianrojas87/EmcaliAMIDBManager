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
import emcali.ami.persistence.entity.PrepagoEstado;
import emcali.ami.persistence.entity.ComercialProductos;
import emcali.ami.persistence.entity.PrepagoClientes;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class PrepagoClientesJpaController implements Serializable {

    public PrepagoClientesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PrepagoClientes prepagoClientes) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PrepagoEstado fkPrepagoEstado = prepagoClientes.getFkPrepagoEstado();
            if (fkPrepagoEstado != null) {
                fkPrepagoEstado = em.getReference(fkPrepagoEstado.getClass(), fkPrepagoEstado.getIdEstado());
                prepagoClientes.setFkPrepagoEstado(fkPrepagoEstado);
            }
            ComercialProductos fkComercialProductos = prepagoClientes.getFkComercialProductos();
            if (fkComercialProductos != null) {
                fkComercialProductos = em.getReference(fkComercialProductos.getClass(), fkComercialProductos.getIdProductos());
                prepagoClientes.setFkComercialProductos(fkComercialProductos);
            }
            em.persist(prepagoClientes);
            if (fkPrepagoEstado != null) {
                fkPrepagoEstado.getPrepagoClientesList().add(prepagoClientes);
                fkPrepagoEstado = em.merge(fkPrepagoEstado);
            }
            if (fkComercialProductos != null) {
                fkComercialProductos.getPrepagoClientesList().add(prepagoClientes);
                fkComercialProductos = em.merge(fkComercialProductos);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPrepagoClientes(prepagoClientes.getIdClientePrepago()) != null) {
                throw new PreexistingEntityException("PrepagoClientes " + prepagoClientes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PrepagoClientes prepagoClientes) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PrepagoClientes persistentPrepagoClientes = em.find(PrepagoClientes.class, prepagoClientes.getIdClientePrepago());
            PrepagoEstado fkPrepagoEstadoOld = persistentPrepagoClientes.getFkPrepagoEstado();
            PrepagoEstado fkPrepagoEstadoNew = prepagoClientes.getFkPrepagoEstado();
            ComercialProductos fkComercialProductosOld = persistentPrepagoClientes.getFkComercialProductos();
            ComercialProductos fkComercialProductosNew = prepagoClientes.getFkComercialProductos();
            if (fkPrepagoEstadoNew != null) {
                fkPrepagoEstadoNew = em.getReference(fkPrepagoEstadoNew.getClass(), fkPrepagoEstadoNew.getIdEstado());
                prepagoClientes.setFkPrepagoEstado(fkPrepagoEstadoNew);
            }
            if (fkComercialProductosNew != null) {
                fkComercialProductosNew = em.getReference(fkComercialProductosNew.getClass(), fkComercialProductosNew.getIdProductos());
                prepagoClientes.setFkComercialProductos(fkComercialProductosNew);
            }
            prepagoClientes = em.merge(prepagoClientes);
            if (fkPrepagoEstadoOld != null && !fkPrepagoEstadoOld.equals(fkPrepagoEstadoNew)) {
                fkPrepagoEstadoOld.getPrepagoClientesList().remove(prepagoClientes);
                fkPrepagoEstadoOld = em.merge(fkPrepagoEstadoOld);
            }
            if (fkPrepagoEstadoNew != null && !fkPrepagoEstadoNew.equals(fkPrepagoEstadoOld)) {
                fkPrepagoEstadoNew.getPrepagoClientesList().add(prepagoClientes);
                fkPrepagoEstadoNew = em.merge(fkPrepagoEstadoNew);
            }
            if (fkComercialProductosOld != null && !fkComercialProductosOld.equals(fkComercialProductosNew)) {
                fkComercialProductosOld.getPrepagoClientesList().remove(prepagoClientes);
                fkComercialProductosOld = em.merge(fkComercialProductosOld);
            }
            if (fkComercialProductosNew != null && !fkComercialProductosNew.equals(fkComercialProductosOld)) {
                fkComercialProductosNew.getPrepagoClientesList().add(prepagoClientes);
                fkComercialProductosNew = em.merge(fkComercialProductosNew);
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
                Long id = prepagoClientes.getIdClientePrepago();
                if (findPrepagoClientes(id) == null) {
                    throw new NonexistentEntityException("The prepagoClientes with id " + id + " no longer exists.");
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
            PrepagoClientes prepagoClientes;
            try {
                prepagoClientes = em.getReference(PrepagoClientes.class, id);
                prepagoClientes.getIdClientePrepago();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The prepagoClientes with id " + id + " no longer exists.", enfe);
            }
            PrepagoEstado fkPrepagoEstado = prepagoClientes.getFkPrepagoEstado();
            if (fkPrepagoEstado != null) {
                fkPrepagoEstado.getPrepagoClientesList().remove(prepagoClientes);
                fkPrepagoEstado = em.merge(fkPrepagoEstado);
            }
            ComercialProductos fkComercialProductos = prepagoClientes.getFkComercialProductos();
            if (fkComercialProductos != null) {
                fkComercialProductos.getPrepagoClientesList().remove(prepagoClientes);
                fkComercialProductos = em.merge(fkComercialProductos);
            }
            em.remove(prepagoClientes);
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

    public List<PrepagoClientes> findPrepagoClientesEntities() {
        return findPrepagoClientesEntities(true, -1, -1);
    }

    public List<PrepagoClientes> findPrepagoClientesEntities(int maxResults, int firstResult) {
        return findPrepagoClientesEntities(false, maxResults, firstResult);
    }

    private List<PrepagoClientes> findPrepagoClientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PrepagoClientes.class));
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

    public PrepagoClientes findPrepagoClientes(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PrepagoClientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getPrepagoClientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PrepagoClientes> rt = cq.from(PrepagoClientes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
