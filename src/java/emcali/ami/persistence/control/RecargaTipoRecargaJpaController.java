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
import java.util.Collection;
import emcali.ami.persistence.entity.SaldosHistoria;
import java.util.List;
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
        if (recargaTipoRecarga.getRecargaRecargasCollection() == null) {
            recargaTipoRecarga.setRecargaRecargasCollection(new ArrayList<RecargaRecargas>());
        }
        if (recargaTipoRecarga.getSaldosHistoriaCollection() == null) {
            recargaTipoRecarga.setSaldosHistoriaCollection(new ArrayList<SaldosHistoria>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<RecargaRecargas> attachedRecargaRecargasCollection = new ArrayList<RecargaRecargas>();
            for (RecargaRecargas recargaRecargasCollectionRecargaRecargasToAttach : recargaTipoRecarga.getRecargaRecargasCollection()) {
                recargaRecargasCollectionRecargaRecargasToAttach = em.getReference(recargaRecargasCollectionRecargaRecargasToAttach.getClass(), recargaRecargasCollectionRecargaRecargasToAttach.getIdRecargas());
                attachedRecargaRecargasCollection.add(recargaRecargasCollectionRecargaRecargasToAttach);
            }
            recargaTipoRecarga.setRecargaRecargasCollection(attachedRecargaRecargasCollection);
            Collection<SaldosHistoria> attachedSaldosHistoriaCollection = new ArrayList<SaldosHistoria>();
            for (SaldosHistoria saldosHistoriaCollectionSaldosHistoriaToAttach : recargaTipoRecarga.getSaldosHistoriaCollection()) {
                saldosHistoriaCollectionSaldosHistoriaToAttach = em.getReference(saldosHistoriaCollectionSaldosHistoriaToAttach.getClass(), saldosHistoriaCollectionSaldosHistoriaToAttach.getIdSaldosHistoria());
                attachedSaldosHistoriaCollection.add(saldosHistoriaCollectionSaldosHistoriaToAttach);
            }
            recargaTipoRecarga.setSaldosHistoriaCollection(attachedSaldosHistoriaCollection);
            em.persist(recargaTipoRecarga);
            for (RecargaRecargas recargaRecargasCollectionRecargaRecargas : recargaTipoRecarga.getRecargaRecargasCollection()) {
                RecargaTipoRecarga oldFkPrepagoTipoRecargaOfRecargaRecargasCollectionRecargaRecargas = recargaRecargasCollectionRecargaRecargas.getFkPrepagoTipoRecarga();
                recargaRecargasCollectionRecargaRecargas.setFkPrepagoTipoRecarga(recargaTipoRecarga);
                recargaRecargasCollectionRecargaRecargas = em.merge(recargaRecargasCollectionRecargaRecargas);
                if (oldFkPrepagoTipoRecargaOfRecargaRecargasCollectionRecargaRecargas != null) {
                    oldFkPrepagoTipoRecargaOfRecargaRecargasCollectionRecargaRecargas.getRecargaRecargasCollection().remove(recargaRecargasCollectionRecargaRecargas);
                    oldFkPrepagoTipoRecargaOfRecargaRecargasCollectionRecargaRecargas = em.merge(oldFkPrepagoTipoRecargaOfRecargaRecargasCollectionRecargaRecargas);
                }
            }
            for (SaldosHistoria saldosHistoriaCollectionSaldosHistoria : recargaTipoRecarga.getSaldosHistoriaCollection()) {
                RecargaTipoRecarga oldFkRecargaTipoRecargaOfSaldosHistoriaCollectionSaldosHistoria = saldosHistoriaCollectionSaldosHistoria.getFkRecargaTipoRecarga();
                saldosHistoriaCollectionSaldosHistoria.setFkRecargaTipoRecarga(recargaTipoRecarga);
                saldosHistoriaCollectionSaldosHistoria = em.merge(saldosHistoriaCollectionSaldosHistoria);
                if (oldFkRecargaTipoRecargaOfSaldosHistoriaCollectionSaldosHistoria != null) {
                    oldFkRecargaTipoRecargaOfSaldosHistoriaCollectionSaldosHistoria.getSaldosHistoriaCollection().remove(saldosHistoriaCollectionSaldosHistoria);
                    oldFkRecargaTipoRecargaOfSaldosHistoriaCollectionSaldosHistoria = em.merge(oldFkRecargaTipoRecargaOfSaldosHistoriaCollectionSaldosHistoria);
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
            Collection<RecargaRecargas> recargaRecargasCollectionOld = persistentRecargaTipoRecarga.getRecargaRecargasCollection();
            Collection<RecargaRecargas> recargaRecargasCollectionNew = recargaTipoRecarga.getRecargaRecargasCollection();
            Collection<SaldosHistoria> saldosHistoriaCollectionOld = persistentRecargaTipoRecarga.getSaldosHistoriaCollection();
            Collection<SaldosHistoria> saldosHistoriaCollectionNew = recargaTipoRecarga.getSaldosHistoriaCollection();
            List<String> illegalOrphanMessages = null;
            for (RecargaRecargas recargaRecargasCollectionOldRecargaRecargas : recargaRecargasCollectionOld) {
                if (!recargaRecargasCollectionNew.contains(recargaRecargasCollectionOldRecargaRecargas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RecargaRecargas " + recargaRecargasCollectionOldRecargaRecargas + " since its fkPrepagoTipoRecarga field is not nullable.");
                }
            }
            for (SaldosHistoria saldosHistoriaCollectionOldSaldosHistoria : saldosHistoriaCollectionOld) {
                if (!saldosHistoriaCollectionNew.contains(saldosHistoriaCollectionOldSaldosHistoria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SaldosHistoria " + saldosHistoriaCollectionOldSaldosHistoria + " since its fkRecargaTipoRecarga field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<RecargaRecargas> attachedRecargaRecargasCollectionNew = new ArrayList<RecargaRecargas>();
            for (RecargaRecargas recargaRecargasCollectionNewRecargaRecargasToAttach : recargaRecargasCollectionNew) {
                recargaRecargasCollectionNewRecargaRecargasToAttach = em.getReference(recargaRecargasCollectionNewRecargaRecargasToAttach.getClass(), recargaRecargasCollectionNewRecargaRecargasToAttach.getIdRecargas());
                attachedRecargaRecargasCollectionNew.add(recargaRecargasCollectionNewRecargaRecargasToAttach);
            }
            recargaRecargasCollectionNew = attachedRecargaRecargasCollectionNew;
            recargaTipoRecarga.setRecargaRecargasCollection(recargaRecargasCollectionNew);
            Collection<SaldosHistoria> attachedSaldosHistoriaCollectionNew = new ArrayList<SaldosHistoria>();
            for (SaldosHistoria saldosHistoriaCollectionNewSaldosHistoriaToAttach : saldosHistoriaCollectionNew) {
                saldosHistoriaCollectionNewSaldosHistoriaToAttach = em.getReference(saldosHistoriaCollectionNewSaldosHistoriaToAttach.getClass(), saldosHistoriaCollectionNewSaldosHistoriaToAttach.getIdSaldosHistoria());
                attachedSaldosHistoriaCollectionNew.add(saldosHistoriaCollectionNewSaldosHistoriaToAttach);
            }
            saldosHistoriaCollectionNew = attachedSaldosHistoriaCollectionNew;
            recargaTipoRecarga.setSaldosHistoriaCollection(saldosHistoriaCollectionNew);
            recargaTipoRecarga = em.merge(recargaTipoRecarga);
            for (RecargaRecargas recargaRecargasCollectionNewRecargaRecargas : recargaRecargasCollectionNew) {
                if (!recargaRecargasCollectionOld.contains(recargaRecargasCollectionNewRecargaRecargas)) {
                    RecargaTipoRecarga oldFkPrepagoTipoRecargaOfRecargaRecargasCollectionNewRecargaRecargas = recargaRecargasCollectionNewRecargaRecargas.getFkPrepagoTipoRecarga();
                    recargaRecargasCollectionNewRecargaRecargas.setFkPrepagoTipoRecarga(recargaTipoRecarga);
                    recargaRecargasCollectionNewRecargaRecargas = em.merge(recargaRecargasCollectionNewRecargaRecargas);
                    if (oldFkPrepagoTipoRecargaOfRecargaRecargasCollectionNewRecargaRecargas != null && !oldFkPrepagoTipoRecargaOfRecargaRecargasCollectionNewRecargaRecargas.equals(recargaTipoRecarga)) {
                        oldFkPrepagoTipoRecargaOfRecargaRecargasCollectionNewRecargaRecargas.getRecargaRecargasCollection().remove(recargaRecargasCollectionNewRecargaRecargas);
                        oldFkPrepagoTipoRecargaOfRecargaRecargasCollectionNewRecargaRecargas = em.merge(oldFkPrepagoTipoRecargaOfRecargaRecargasCollectionNewRecargaRecargas);
                    }
                }
            }
            for (SaldosHistoria saldosHistoriaCollectionNewSaldosHistoria : saldosHistoriaCollectionNew) {
                if (!saldosHistoriaCollectionOld.contains(saldosHistoriaCollectionNewSaldosHistoria)) {
                    RecargaTipoRecarga oldFkRecargaTipoRecargaOfSaldosHistoriaCollectionNewSaldosHistoria = saldosHistoriaCollectionNewSaldosHistoria.getFkRecargaTipoRecarga();
                    saldosHistoriaCollectionNewSaldosHistoria.setFkRecargaTipoRecarga(recargaTipoRecarga);
                    saldosHistoriaCollectionNewSaldosHistoria = em.merge(saldosHistoriaCollectionNewSaldosHistoria);
                    if (oldFkRecargaTipoRecargaOfSaldosHistoriaCollectionNewSaldosHistoria != null && !oldFkRecargaTipoRecargaOfSaldosHistoriaCollectionNewSaldosHistoria.equals(recargaTipoRecarga)) {
                        oldFkRecargaTipoRecargaOfSaldosHistoriaCollectionNewSaldosHistoria.getSaldosHistoriaCollection().remove(saldosHistoriaCollectionNewSaldosHistoria);
                        oldFkRecargaTipoRecargaOfSaldosHistoriaCollectionNewSaldosHistoria = em.merge(oldFkRecargaTipoRecargaOfSaldosHistoriaCollectionNewSaldosHistoria);
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
            Collection<RecargaRecargas> recargaRecargasCollectionOrphanCheck = recargaTipoRecarga.getRecargaRecargasCollection();
            for (RecargaRecargas recargaRecargasCollectionOrphanCheckRecargaRecargas : recargaRecargasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This RecargaTipoRecarga (" + recargaTipoRecarga + ") cannot be destroyed since the RecargaRecargas " + recargaRecargasCollectionOrphanCheckRecargaRecargas + " in its recargaRecargasCollection field has a non-nullable fkPrepagoTipoRecarga field.");
            }
            Collection<SaldosHistoria> saldosHistoriaCollectionOrphanCheck = recargaTipoRecarga.getSaldosHistoriaCollection();
            for (SaldosHistoria saldosHistoriaCollectionOrphanCheckSaldosHistoria : saldosHistoriaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This RecargaTipoRecarga (" + recargaTipoRecarga + ") cannot be destroyed since the SaldosHistoria " + saldosHistoriaCollectionOrphanCheckSaldosHistoria + " in its saldosHistoriaCollection field has a non-nullable fkRecargaTipoRecarga field.");
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
