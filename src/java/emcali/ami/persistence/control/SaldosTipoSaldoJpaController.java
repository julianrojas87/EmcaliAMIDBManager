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
import emcali.ami.persistence.entity.SaldosHistoria;
import emcali.ami.persistence.entity.SaldosTipoSaldo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class SaldosTipoSaldoJpaController implements Serializable {

    public SaldosTipoSaldoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SaldosTipoSaldo saldosTipoSaldo) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (saldosTipoSaldo.getSaldosHistoriaList() == null) {
            saldosTipoSaldo.setSaldosHistoriaList(new ArrayList<SaldosHistoria>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<SaldosHistoria> attachedSaldosHistoriaList = new ArrayList<SaldosHistoria>();
            for (SaldosHistoria saldosHistoriaListSaldosHistoriaToAttach : saldosTipoSaldo.getSaldosHistoriaList()) {
                saldosHistoriaListSaldosHistoriaToAttach = em.getReference(saldosHistoriaListSaldosHistoriaToAttach.getClass(), saldosHistoriaListSaldosHistoriaToAttach.getIdSaldosHistoria());
                attachedSaldosHistoriaList.add(saldosHistoriaListSaldosHistoriaToAttach);
            }
            saldosTipoSaldo.setSaldosHistoriaList(attachedSaldosHistoriaList);
            em.persist(saldosTipoSaldo);
            for (SaldosHistoria saldosHistoriaListSaldosHistoria : saldosTipoSaldo.getSaldosHistoriaList()) {
                SaldosTipoSaldo oldFkSaldosTipoSaldoOfSaldosHistoriaListSaldosHistoria = saldosHistoriaListSaldosHistoria.getFkSaldosTipoSaldo();
                saldosHistoriaListSaldosHistoria.setFkSaldosTipoSaldo(saldosTipoSaldo);
                saldosHistoriaListSaldosHistoria = em.merge(saldosHistoriaListSaldosHistoria);
                if (oldFkSaldosTipoSaldoOfSaldosHistoriaListSaldosHistoria != null) {
                    oldFkSaldosTipoSaldoOfSaldosHistoriaListSaldosHistoria.getSaldosHistoriaList().remove(saldosHistoriaListSaldosHistoria);
                    oldFkSaldosTipoSaldoOfSaldosHistoriaListSaldosHistoria = em.merge(oldFkSaldosTipoSaldoOfSaldosHistoriaListSaldosHistoria);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findSaldosTipoSaldo(saldosTipoSaldo.getIdTipoSaldo()) != null) {
                throw new PreexistingEntityException("SaldosTipoSaldo " + saldosTipoSaldo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SaldosTipoSaldo saldosTipoSaldo) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            SaldosTipoSaldo persistentSaldosTipoSaldo = em.find(SaldosTipoSaldo.class, saldosTipoSaldo.getIdTipoSaldo());
            List<SaldosHistoria> saldosHistoriaListOld = persistentSaldosTipoSaldo.getSaldosHistoriaList();
            List<SaldosHistoria> saldosHistoriaListNew = saldosTipoSaldo.getSaldosHistoriaList();
            List<String> illegalOrphanMessages = null;
            for (SaldosHistoria saldosHistoriaListOldSaldosHistoria : saldosHistoriaListOld) {
                if (!saldosHistoriaListNew.contains(saldosHistoriaListOldSaldosHistoria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SaldosHistoria " + saldosHistoriaListOldSaldosHistoria + " since its fkSaldosTipoSaldo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<SaldosHistoria> attachedSaldosHistoriaListNew = new ArrayList<SaldosHistoria>();
            for (SaldosHistoria saldosHistoriaListNewSaldosHistoriaToAttach : saldosHistoriaListNew) {
                saldosHistoriaListNewSaldosHistoriaToAttach = em.getReference(saldosHistoriaListNewSaldosHistoriaToAttach.getClass(), saldosHistoriaListNewSaldosHistoriaToAttach.getIdSaldosHistoria());
                attachedSaldosHistoriaListNew.add(saldosHistoriaListNewSaldosHistoriaToAttach);
            }
            saldosHistoriaListNew = attachedSaldosHistoriaListNew;
            saldosTipoSaldo.setSaldosHistoriaList(saldosHistoriaListNew);
            saldosTipoSaldo = em.merge(saldosTipoSaldo);
            for (SaldosHistoria saldosHistoriaListNewSaldosHistoria : saldosHistoriaListNew) {
                if (!saldosHistoriaListOld.contains(saldosHistoriaListNewSaldosHistoria)) {
                    SaldosTipoSaldo oldFkSaldosTipoSaldoOfSaldosHistoriaListNewSaldosHistoria = saldosHistoriaListNewSaldosHistoria.getFkSaldosTipoSaldo();
                    saldosHistoriaListNewSaldosHistoria.setFkSaldosTipoSaldo(saldosTipoSaldo);
                    saldosHistoriaListNewSaldosHistoria = em.merge(saldosHistoriaListNewSaldosHistoria);
                    if (oldFkSaldosTipoSaldoOfSaldosHistoriaListNewSaldosHistoria != null && !oldFkSaldosTipoSaldoOfSaldosHistoriaListNewSaldosHistoria.equals(saldosTipoSaldo)) {
                        oldFkSaldosTipoSaldoOfSaldosHistoriaListNewSaldosHistoria.getSaldosHistoriaList().remove(saldosHistoriaListNewSaldosHistoria);
                        oldFkSaldosTipoSaldoOfSaldosHistoriaListNewSaldosHistoria = em.merge(oldFkSaldosTipoSaldoOfSaldosHistoriaListNewSaldosHistoria);
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
                Long id = saldosTipoSaldo.getIdTipoSaldo();
                if (findSaldosTipoSaldo(id) == null) {
                    throw new NonexistentEntityException("The saldosTipoSaldo with id " + id + " no longer exists.");
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
            SaldosTipoSaldo saldosTipoSaldo;
            try {
                saldosTipoSaldo = em.getReference(SaldosTipoSaldo.class, id);
                saldosTipoSaldo.getIdTipoSaldo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The saldosTipoSaldo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<SaldosHistoria> saldosHistoriaListOrphanCheck = saldosTipoSaldo.getSaldosHistoriaList();
            for (SaldosHistoria saldosHistoriaListOrphanCheckSaldosHistoria : saldosHistoriaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SaldosTipoSaldo (" + saldosTipoSaldo + ") cannot be destroyed since the SaldosHistoria " + saldosHistoriaListOrphanCheckSaldosHistoria + " in its saldosHistoriaList field has a non-nullable fkSaldosTipoSaldo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(saldosTipoSaldo);
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

    public List<SaldosTipoSaldo> findSaldosTipoSaldoEntities() {
        return findSaldosTipoSaldoEntities(true, -1, -1);
    }

    public List<SaldosTipoSaldo> findSaldosTipoSaldoEntities(int maxResults, int firstResult) {
        return findSaldosTipoSaldoEntities(false, maxResults, firstResult);
    }

    private List<SaldosTipoSaldo> findSaldosTipoSaldoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SaldosTipoSaldo.class));
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

    public SaldosTipoSaldo findSaldosTipoSaldo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SaldosTipoSaldo.class, id);
        } finally {
            em.close();
        }
    }

    public int getSaldosTipoSaldoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SaldosTipoSaldo> rt = cq.from(SaldosTipoSaldo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
