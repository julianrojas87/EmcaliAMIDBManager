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
import emcali.ami.persistence.entity.RecargaTipoRecarga;
import emcali.ami.persistence.entity.ComercialProductos;
import emcali.ami.persistence.entity.RecargaRecargas;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class RecargaRecargasJpaController implements Serializable {

    public RecargaRecargasJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RecargaRecargas recargaRecargas) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            RecargaTipoRecarga fkPrepagoTipoRecarga = recargaRecargas.getFkPrepagoTipoRecarga();
            if (fkPrepagoTipoRecarga != null) {
                fkPrepagoTipoRecarga = em.getReference(fkPrepagoTipoRecarga.getClass(), fkPrepagoTipoRecarga.getIdTipoRecarga());
                recargaRecargas.setFkPrepagoTipoRecarga(fkPrepagoTipoRecarga);
            }
            ComercialProductos fkComercialProductos = recargaRecargas.getFkComercialProductos();
            if (fkComercialProductos != null) {
                fkComercialProductos = em.getReference(fkComercialProductos.getClass(), fkComercialProductos.getIdProductos());
                recargaRecargas.setFkComercialProductos(fkComercialProductos);
            }
            em.persist(recargaRecargas);
            if (fkPrepagoTipoRecarga != null) {
                fkPrepagoTipoRecarga.getRecargaRecargasCollection().add(recargaRecargas);
                fkPrepagoTipoRecarga = em.merge(fkPrepagoTipoRecarga);
            }
            if (fkComercialProductos != null) {
                fkComercialProductos.getRecargaRecargasCollection().add(recargaRecargas);
                fkComercialProductos = em.merge(fkComercialProductos);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRecargaRecargas(recargaRecargas.getIdRecargas()) != null) {
                throw new PreexistingEntityException("RecargaRecargas " + recargaRecargas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RecargaRecargas recargaRecargas) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            RecargaRecargas persistentRecargaRecargas = em.find(RecargaRecargas.class, recargaRecargas.getIdRecargas());
            RecargaTipoRecarga fkPrepagoTipoRecargaOld = persistentRecargaRecargas.getFkPrepagoTipoRecarga();
            RecargaTipoRecarga fkPrepagoTipoRecargaNew = recargaRecargas.getFkPrepagoTipoRecarga();
            ComercialProductos fkComercialProductosOld = persistentRecargaRecargas.getFkComercialProductos();
            ComercialProductos fkComercialProductosNew = recargaRecargas.getFkComercialProductos();
            if (fkPrepagoTipoRecargaNew != null) {
                fkPrepagoTipoRecargaNew = em.getReference(fkPrepagoTipoRecargaNew.getClass(), fkPrepagoTipoRecargaNew.getIdTipoRecarga());
                recargaRecargas.setFkPrepagoTipoRecarga(fkPrepagoTipoRecargaNew);
            }
            if (fkComercialProductosNew != null) {
                fkComercialProductosNew = em.getReference(fkComercialProductosNew.getClass(), fkComercialProductosNew.getIdProductos());
                recargaRecargas.setFkComercialProductos(fkComercialProductosNew);
            }
            recargaRecargas = em.merge(recargaRecargas);
            if (fkPrepagoTipoRecargaOld != null && !fkPrepagoTipoRecargaOld.equals(fkPrepagoTipoRecargaNew)) {
                fkPrepagoTipoRecargaOld.getRecargaRecargasCollection().remove(recargaRecargas);
                fkPrepagoTipoRecargaOld = em.merge(fkPrepagoTipoRecargaOld);
            }
            if (fkPrepagoTipoRecargaNew != null && !fkPrepagoTipoRecargaNew.equals(fkPrepagoTipoRecargaOld)) {
                fkPrepagoTipoRecargaNew.getRecargaRecargasCollection().add(recargaRecargas);
                fkPrepagoTipoRecargaNew = em.merge(fkPrepagoTipoRecargaNew);
            }
            if (fkComercialProductosOld != null && !fkComercialProductosOld.equals(fkComercialProductosNew)) {
                fkComercialProductosOld.getRecargaRecargasCollection().remove(recargaRecargas);
                fkComercialProductosOld = em.merge(fkComercialProductosOld);
            }
            if (fkComercialProductosNew != null && !fkComercialProductosNew.equals(fkComercialProductosOld)) {
                fkComercialProductosNew.getRecargaRecargasCollection().add(recargaRecargas);
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
                Long id = recargaRecargas.getIdRecargas();
                if (findRecargaRecargas(id) == null) {
                    throw new NonexistentEntityException("The recargaRecargas with id " + id + " no longer exists.");
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
            RecargaRecargas recargaRecargas;
            try {
                recargaRecargas = em.getReference(RecargaRecargas.class, id);
                recargaRecargas.getIdRecargas();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recargaRecargas with id " + id + " no longer exists.", enfe);
            }
            RecargaTipoRecarga fkPrepagoTipoRecarga = recargaRecargas.getFkPrepagoTipoRecarga();
            if (fkPrepagoTipoRecarga != null) {
                fkPrepagoTipoRecarga.getRecargaRecargasCollection().remove(recargaRecargas);
                fkPrepagoTipoRecarga = em.merge(fkPrepagoTipoRecarga);
            }
            ComercialProductos fkComercialProductos = recargaRecargas.getFkComercialProductos();
            if (fkComercialProductos != null) {
                fkComercialProductos.getRecargaRecargasCollection().remove(recargaRecargas);
                fkComercialProductos = em.merge(fkComercialProductos);
            }
            em.remove(recargaRecargas);
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

    public List<RecargaRecargas> findRecargaRecargasEntities() {
        return findRecargaRecargasEntities(true, -1, -1);
    }

    public List<RecargaRecargas> findRecargaRecargasEntities(int maxResults, int firstResult) {
        return findRecargaRecargasEntities(false, maxResults, firstResult);
    }

    private List<RecargaRecargas> findRecargaRecargasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RecargaRecargas.class));
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

    public RecargaRecargas findRecargaRecargas(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RecargaRecargas.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecargaRecargasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RecargaRecargas> rt = cq.from(RecargaRecargas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
