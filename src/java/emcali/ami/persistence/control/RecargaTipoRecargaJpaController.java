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
import emcali.ami.persistence.entity.RecargaRecargas;
import emcali.ami.persistence.entity.RecargaTipoRecarga;
import java.util.ArrayList;
import java.util.List;
import emcali.ami.persistence.entity.SaldosHistoria;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class RecargaTipoRecargaJpaController implements Serializable {

    public RecargaTipoRecargaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RecargaTipoRecarga recargaTipoRecarga) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (recargaTipoRecarga.getRecargaRecargasList() == null) {
            recargaTipoRecarga.setRecargaRecargasList(new ArrayList<RecargaRecargas>());
        }
        if (recargaTipoRecarga.getSaldosHistoriaList() == null) {
            recargaTipoRecarga.setSaldosHistoriaList(new ArrayList<SaldosHistoria>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<RecargaRecargas> attachedRecargaRecargasList = new ArrayList<RecargaRecargas>();
            for (RecargaRecargas recargaRecargasListRecargaRecargasToAttach : recargaTipoRecarga.getRecargaRecargasList()) {
                recargaRecargasListRecargaRecargasToAttach = em.getReference(recargaRecargasListRecargaRecargasToAttach.getClass(), recargaRecargasListRecargaRecargasToAttach.getIdRecargas());
                attachedRecargaRecargasList.add(recargaRecargasListRecargaRecargasToAttach);
            }
            recargaTipoRecarga.setRecargaRecargasList(attachedRecargaRecargasList);
            List<SaldosHistoria> attachedSaldosHistoriaList = new ArrayList<SaldosHistoria>();
            for (SaldosHistoria saldosHistoriaListSaldosHistoriaToAttach : recargaTipoRecarga.getSaldosHistoriaList()) {
                saldosHistoriaListSaldosHistoriaToAttach = em.getReference(saldosHistoriaListSaldosHistoriaToAttach.getClass(), saldosHistoriaListSaldosHistoriaToAttach.getIdSaldosHistoria());
                attachedSaldosHistoriaList.add(saldosHistoriaListSaldosHistoriaToAttach);
            }
            recargaTipoRecarga.setSaldosHistoriaList(attachedSaldosHistoriaList);
            em.persist(recargaTipoRecarga);
            for (RecargaRecargas recargaRecargasListRecargaRecargas : recargaTipoRecarga.getRecargaRecargasList()) {
                RecargaTipoRecarga oldFkPrepagoTipoRecargaOfRecargaRecargasListRecargaRecargas = recargaRecargasListRecargaRecargas.getFkPrepagoTipoRecarga();
                recargaRecargasListRecargaRecargas.setFkPrepagoTipoRecarga(recargaTipoRecarga);
                recargaRecargasListRecargaRecargas = em.merge(recargaRecargasListRecargaRecargas);
                if (oldFkPrepagoTipoRecargaOfRecargaRecargasListRecargaRecargas != null) {
                    oldFkPrepagoTipoRecargaOfRecargaRecargasListRecargaRecargas.getRecargaRecargasList().remove(recargaRecargasListRecargaRecargas);
                    oldFkPrepagoTipoRecargaOfRecargaRecargasListRecargaRecargas = em.merge(oldFkPrepagoTipoRecargaOfRecargaRecargasListRecargaRecargas);
                }
            }
            for (SaldosHistoria saldosHistoriaListSaldosHistoria : recargaTipoRecarga.getSaldosHistoriaList()) {
                RecargaTipoRecarga oldFkRecargaTipoRecargaOfSaldosHistoriaListSaldosHistoria = saldosHistoriaListSaldosHistoria.getFkRecargaTipoRecarga();
                saldosHistoriaListSaldosHistoria.setFkRecargaTipoRecarga(recargaTipoRecarga);
                saldosHistoriaListSaldosHistoria = em.merge(saldosHistoriaListSaldosHistoria);
                if (oldFkRecargaTipoRecargaOfSaldosHistoriaListSaldosHistoria != null) {
                    oldFkRecargaTipoRecargaOfSaldosHistoriaListSaldosHistoria.getSaldosHistoriaList().remove(saldosHistoriaListSaldosHistoria);
                    oldFkRecargaTipoRecargaOfSaldosHistoriaListSaldosHistoria = em.merge(oldFkRecargaTipoRecargaOfSaldosHistoriaListSaldosHistoria);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRecargaTipoRecarga(recargaTipoRecarga.getIdTipoRecarga()) != null) {
                throw new PreexistingEntityException("RecargaTipoRecarga " + recargaTipoRecarga + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RecargaTipoRecarga recargaTipoRecarga) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            RecargaTipoRecarga persistentRecargaTipoRecarga = em.find(RecargaTipoRecarga.class, recargaTipoRecarga.getIdTipoRecarga());
            List<RecargaRecargas> recargaRecargasListOld = persistentRecargaTipoRecarga.getRecargaRecargasList();
            List<RecargaRecargas> recargaRecargasListNew = recargaTipoRecarga.getRecargaRecargasList();
            List<SaldosHistoria> saldosHistoriaListOld = persistentRecargaTipoRecarga.getSaldosHistoriaList();
            List<SaldosHistoria> saldosHistoriaListNew = recargaTipoRecarga.getSaldosHistoriaList();
            List<String> illegalOrphanMessages = null;
            for (RecargaRecargas recargaRecargasListOldRecargaRecargas : recargaRecargasListOld) {
                if (!recargaRecargasListNew.contains(recargaRecargasListOldRecargaRecargas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RecargaRecargas " + recargaRecargasListOldRecargaRecargas + " since its fkPrepagoTipoRecarga field is not nullable.");
                }
            }
            for (SaldosHistoria saldosHistoriaListOldSaldosHistoria : saldosHistoriaListOld) {
                if (!saldosHistoriaListNew.contains(saldosHistoriaListOldSaldosHistoria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SaldosHistoria " + saldosHistoriaListOldSaldosHistoria + " since its fkRecargaTipoRecarga field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<RecargaRecargas> attachedRecargaRecargasListNew = new ArrayList<RecargaRecargas>();
            for (RecargaRecargas recargaRecargasListNewRecargaRecargasToAttach : recargaRecargasListNew) {
                recargaRecargasListNewRecargaRecargasToAttach = em.getReference(recargaRecargasListNewRecargaRecargasToAttach.getClass(), recargaRecargasListNewRecargaRecargasToAttach.getIdRecargas());
                attachedRecargaRecargasListNew.add(recargaRecargasListNewRecargaRecargasToAttach);
            }
            recargaRecargasListNew = attachedRecargaRecargasListNew;
            recargaTipoRecarga.setRecargaRecargasList(recargaRecargasListNew);
            List<SaldosHistoria> attachedSaldosHistoriaListNew = new ArrayList<SaldosHistoria>();
            for (SaldosHistoria saldosHistoriaListNewSaldosHistoriaToAttach : saldosHistoriaListNew) {
                saldosHistoriaListNewSaldosHistoriaToAttach = em.getReference(saldosHistoriaListNewSaldosHistoriaToAttach.getClass(), saldosHistoriaListNewSaldosHistoriaToAttach.getIdSaldosHistoria());
                attachedSaldosHistoriaListNew.add(saldosHistoriaListNewSaldosHistoriaToAttach);
            }
            saldosHistoriaListNew = attachedSaldosHistoriaListNew;
            recargaTipoRecarga.setSaldosHistoriaList(saldosHistoriaListNew);
            recargaTipoRecarga = em.merge(recargaTipoRecarga);
            for (RecargaRecargas recargaRecargasListNewRecargaRecargas : recargaRecargasListNew) {
                if (!recargaRecargasListOld.contains(recargaRecargasListNewRecargaRecargas)) {
                    RecargaTipoRecarga oldFkPrepagoTipoRecargaOfRecargaRecargasListNewRecargaRecargas = recargaRecargasListNewRecargaRecargas.getFkPrepagoTipoRecarga();
                    recargaRecargasListNewRecargaRecargas.setFkPrepagoTipoRecarga(recargaTipoRecarga);
                    recargaRecargasListNewRecargaRecargas = em.merge(recargaRecargasListNewRecargaRecargas);
                    if (oldFkPrepagoTipoRecargaOfRecargaRecargasListNewRecargaRecargas != null && !oldFkPrepagoTipoRecargaOfRecargaRecargasListNewRecargaRecargas.equals(recargaTipoRecarga)) {
                        oldFkPrepagoTipoRecargaOfRecargaRecargasListNewRecargaRecargas.getRecargaRecargasList().remove(recargaRecargasListNewRecargaRecargas);
                        oldFkPrepagoTipoRecargaOfRecargaRecargasListNewRecargaRecargas = em.merge(oldFkPrepagoTipoRecargaOfRecargaRecargasListNewRecargaRecargas);
                    }
                }
            }
            for (SaldosHistoria saldosHistoriaListNewSaldosHistoria : saldosHistoriaListNew) {
                if (!saldosHistoriaListOld.contains(saldosHistoriaListNewSaldosHistoria)) {
                    RecargaTipoRecarga oldFkRecargaTipoRecargaOfSaldosHistoriaListNewSaldosHistoria = saldosHistoriaListNewSaldosHistoria.getFkRecargaTipoRecarga();
                    saldosHistoriaListNewSaldosHistoria.setFkRecargaTipoRecarga(recargaTipoRecarga);
                    saldosHistoriaListNewSaldosHistoria = em.merge(saldosHistoriaListNewSaldosHistoria);
                    if (oldFkRecargaTipoRecargaOfSaldosHistoriaListNewSaldosHistoria != null && !oldFkRecargaTipoRecargaOfSaldosHistoriaListNewSaldosHistoria.equals(recargaTipoRecarga)) {
                        oldFkRecargaTipoRecargaOfSaldosHistoriaListNewSaldosHistoria.getSaldosHistoriaList().remove(saldosHistoriaListNewSaldosHistoria);
                        oldFkRecargaTipoRecargaOfSaldosHistoriaListNewSaldosHistoria = em.merge(oldFkRecargaTipoRecargaOfSaldosHistoriaListNewSaldosHistoria);
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
                Long id = recargaTipoRecarga.getIdTipoRecarga();
                if (findRecargaTipoRecarga(id) == null) {
                    throw new NonexistentEntityException("The recargaTipoRecarga with id " + id + " no longer exists.");
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
            RecargaTipoRecarga recargaTipoRecarga;
            try {
                recargaTipoRecarga = em.getReference(RecargaTipoRecarga.class, id);
                recargaTipoRecarga.getIdTipoRecarga();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recargaTipoRecarga with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RecargaRecargas> recargaRecargasListOrphanCheck = recargaTipoRecarga.getRecargaRecargasList();
            for (RecargaRecargas recargaRecargasListOrphanCheckRecargaRecargas : recargaRecargasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This RecargaTipoRecarga (" + recargaTipoRecarga + ") cannot be destroyed since the RecargaRecargas " + recargaRecargasListOrphanCheckRecargaRecargas + " in its recargaRecargasList field has a non-nullable fkPrepagoTipoRecarga field.");
            }
            List<SaldosHistoria> saldosHistoriaListOrphanCheck = recargaTipoRecarga.getSaldosHistoriaList();
            for (SaldosHistoria saldosHistoriaListOrphanCheckSaldosHistoria : saldosHistoriaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This RecargaTipoRecarga (" + recargaTipoRecarga + ") cannot be destroyed since the SaldosHistoria " + saldosHistoriaListOrphanCheckSaldosHistoria + " in its saldosHistoriaList field has a non-nullable fkRecargaTipoRecarga field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(recargaTipoRecarga);
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

    public List<RecargaTipoRecarga> findRecargaTipoRecargaEntities() {
        return findRecargaTipoRecargaEntities(true, -1, -1);
    }

    public List<RecargaTipoRecarga> findRecargaTipoRecargaEntities(int maxResults, int firstResult) {
        return findRecargaTipoRecargaEntities(false, maxResults, firstResult);
    }

    private List<RecargaTipoRecarga> findRecargaTipoRecargaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RecargaTipoRecarga.class));
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

    public RecargaTipoRecarga findRecargaTipoRecarga(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RecargaTipoRecarga.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecargaTipoRecargaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RecargaTipoRecarga> rt = cq.from(RecargaTipoRecarga.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
