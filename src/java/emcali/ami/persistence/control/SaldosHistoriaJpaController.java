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
import emcali.ami.persistence.entity.SaldosTipoSaldo;
import emcali.ami.persistence.entity.RecargaTipoRecarga;
import emcali.ami.persistence.entity.ComercialProductos;
import emcali.ami.persistence.entity.SaldosHistoria;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class SaldosHistoriaJpaController implements Serializable {

    public SaldosHistoriaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SaldosHistoria saldosHistoria) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            SaldosTipoSaldo fkSaldosTipoSaldo = saldosHistoria.getFkSaldosTipoSaldo();
            if (fkSaldosTipoSaldo != null) {
                fkSaldosTipoSaldo = em.getReference(fkSaldosTipoSaldo.getClass(), fkSaldosTipoSaldo.getIdTipoSaldo());
                saldosHistoria.setFkSaldosTipoSaldo(fkSaldosTipoSaldo);
            }
            RecargaTipoRecarga fkRecargaTipoRecarga = saldosHistoria.getFkRecargaTipoRecarga();
            if (fkRecargaTipoRecarga != null) {
                fkRecargaTipoRecarga = em.getReference(fkRecargaTipoRecarga.getClass(), fkRecargaTipoRecarga.getIdTipoRecarga());
                saldosHistoria.setFkRecargaTipoRecarga(fkRecargaTipoRecarga);
            }
            ComercialProductos fkComercialProductos = saldosHistoria.getFkComercialProductos();
            if (fkComercialProductos != null) {
                fkComercialProductos = em.getReference(fkComercialProductos.getClass(), fkComercialProductos.getIdProductos());
                saldosHistoria.setFkComercialProductos(fkComercialProductos);
            }
            em.persist(saldosHistoria);
            if (fkSaldosTipoSaldo != null) {
                fkSaldosTipoSaldo.getSaldosHistoriaCollection().add(saldosHistoria);
                fkSaldosTipoSaldo = em.merge(fkSaldosTipoSaldo);
            }
            if (fkRecargaTipoRecarga != null) {
                fkRecargaTipoRecarga.getSaldosHistoriaCollection().add(saldosHistoria);
                fkRecargaTipoRecarga = em.merge(fkRecargaTipoRecarga);
            }
            if (fkComercialProductos != null) {
                fkComercialProductos.getSaldosHistoriaCollection().add(saldosHistoria);
                fkComercialProductos = em.merge(fkComercialProductos);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findSaldosHistoria(saldosHistoria.getIdSaldosHistoria()) != null) {
                throw new PreexistingEntityException("SaldosHistoria " + saldosHistoria + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SaldosHistoria saldosHistoria) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            SaldosHistoria persistentSaldosHistoria = em.find(SaldosHistoria.class, saldosHistoria.getIdSaldosHistoria());
            SaldosTipoSaldo fkSaldosTipoSaldoOld = persistentSaldosHistoria.getFkSaldosTipoSaldo();
            SaldosTipoSaldo fkSaldosTipoSaldoNew = saldosHistoria.getFkSaldosTipoSaldo();
            RecargaTipoRecarga fkRecargaTipoRecargaOld = persistentSaldosHistoria.getFkRecargaTipoRecarga();
            RecargaTipoRecarga fkRecargaTipoRecargaNew = saldosHistoria.getFkRecargaTipoRecarga();
            ComercialProductos fkComercialProductosOld = persistentSaldosHistoria.getFkComercialProductos();
            ComercialProductos fkComercialProductosNew = saldosHistoria.getFkComercialProductos();
            if (fkSaldosTipoSaldoNew != null) {
                fkSaldosTipoSaldoNew = em.getReference(fkSaldosTipoSaldoNew.getClass(), fkSaldosTipoSaldoNew.getIdTipoSaldo());
                saldosHistoria.setFkSaldosTipoSaldo(fkSaldosTipoSaldoNew);
            }
            if (fkRecargaTipoRecargaNew != null) {
                fkRecargaTipoRecargaNew = em.getReference(fkRecargaTipoRecargaNew.getClass(), fkRecargaTipoRecargaNew.getIdTipoRecarga());
                saldosHistoria.setFkRecargaTipoRecarga(fkRecargaTipoRecargaNew);
            }
            if (fkComercialProductosNew != null) {
                fkComercialProductosNew = em.getReference(fkComercialProductosNew.getClass(), fkComercialProductosNew.getIdProductos());
                saldosHistoria.setFkComercialProductos(fkComercialProductosNew);
            }
            saldosHistoria = em.merge(saldosHistoria);
            if (fkSaldosTipoSaldoOld != null && !fkSaldosTipoSaldoOld.equals(fkSaldosTipoSaldoNew)) {
                fkSaldosTipoSaldoOld.getSaldosHistoriaCollection().remove(saldosHistoria);
                fkSaldosTipoSaldoOld = em.merge(fkSaldosTipoSaldoOld);
            }
            if (fkSaldosTipoSaldoNew != null && !fkSaldosTipoSaldoNew.equals(fkSaldosTipoSaldoOld)) {
                fkSaldosTipoSaldoNew.getSaldosHistoriaCollection().add(saldosHistoria);
                fkSaldosTipoSaldoNew = em.merge(fkSaldosTipoSaldoNew);
            }
            if (fkRecargaTipoRecargaOld != null && !fkRecargaTipoRecargaOld.equals(fkRecargaTipoRecargaNew)) {
                fkRecargaTipoRecargaOld.getSaldosHistoriaCollection().remove(saldosHistoria);
                fkRecargaTipoRecargaOld = em.merge(fkRecargaTipoRecargaOld);
            }
            if (fkRecargaTipoRecargaNew != null && !fkRecargaTipoRecargaNew.equals(fkRecargaTipoRecargaOld)) {
                fkRecargaTipoRecargaNew.getSaldosHistoriaCollection().add(saldosHistoria);
                fkRecargaTipoRecargaNew = em.merge(fkRecargaTipoRecargaNew);
            }
            if (fkComercialProductosOld != null && !fkComercialProductosOld.equals(fkComercialProductosNew)) {
                fkComercialProductosOld.getSaldosHistoriaCollection().remove(saldosHistoria);
                fkComercialProductosOld = em.merge(fkComercialProductosOld);
            }
            if (fkComercialProductosNew != null && !fkComercialProductosNew.equals(fkComercialProductosOld)) {
                fkComercialProductosNew.getSaldosHistoriaCollection().add(saldosHistoria);
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
                Long id = saldosHistoria.getIdSaldosHistoria();
                if (findSaldosHistoria(id) == null) {
                    throw new NonexistentEntityException("The saldosHistoria with id " + id + " no longer exists.");
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
            SaldosHistoria saldosHistoria;
            try {
                saldosHistoria = em.getReference(SaldosHistoria.class, id);
                saldosHistoria.getIdSaldosHistoria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The saldosHistoria with id " + id + " no longer exists.", enfe);
            }
            SaldosTipoSaldo fkSaldosTipoSaldo = saldosHistoria.getFkSaldosTipoSaldo();
            if (fkSaldosTipoSaldo != null) {
                fkSaldosTipoSaldo.getSaldosHistoriaCollection().remove(saldosHistoria);
                fkSaldosTipoSaldo = em.merge(fkSaldosTipoSaldo);
            }
            RecargaTipoRecarga fkRecargaTipoRecarga = saldosHistoria.getFkRecargaTipoRecarga();
            if (fkRecargaTipoRecarga != null) {
                fkRecargaTipoRecarga.getSaldosHistoriaCollection().remove(saldosHistoria);
                fkRecargaTipoRecarga = em.merge(fkRecargaTipoRecarga);
            }
            ComercialProductos fkComercialProductos = saldosHistoria.getFkComercialProductos();
            if (fkComercialProductos != null) {
                fkComercialProductos.getSaldosHistoriaCollection().remove(saldosHistoria);
                fkComercialProductos = em.merge(fkComercialProductos);
            }
            em.remove(saldosHistoria);
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

    public List<SaldosHistoria> findSaldosHistoriaEntities() {
        return findSaldosHistoriaEntities(true, -1, -1);
    }

    public List<SaldosHistoria> findSaldosHistoriaEntities(int maxResults, int firstResult) {
        return findSaldosHistoriaEntities(false, maxResults, firstResult);
    }

    private List<SaldosHistoria> findSaldosHistoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SaldosHistoria.class));
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

    public SaldosHistoria findSaldosHistoria(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SaldosHistoria.class, id);
        } finally {
            em.close();
        }
    }

    public int getSaldosHistoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SaldosHistoria> rt = cq.from(SaldosHistoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
