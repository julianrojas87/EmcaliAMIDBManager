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
import emcali.ami.persistence.entity.ComercialProductos;
import emcali.ami.persistence.entity.AmyTipoEventos;
import emcali.ami.persistence.entity.PrepagoEventos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class PrepagoEventosJpaController implements Serializable {

    public PrepagoEventosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PrepagoEventos prepagoEventos) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ComercialProductos fkComercialProductos = prepagoEventos.getFkComercialProductos();
            if (fkComercialProductos != null) {
                fkComercialProductos = em.getReference(fkComercialProductos.getClass(), fkComercialProductos.getIdProductos());
                prepagoEventos.setFkComercialProductos(fkComercialProductos);
            }
            AmyTipoEventos fkAmyTipoEventos = prepagoEventos.getFkAmyTipoEventos();
            if (fkAmyTipoEventos != null) {
                fkAmyTipoEventos = em.getReference(fkAmyTipoEventos.getClass(), fkAmyTipoEventos.getIdTipoEvento());
                prepagoEventos.setFkAmyTipoEventos(fkAmyTipoEventos);
            }
            em.persist(prepagoEventos);
            if (fkComercialProductos != null) {
                fkComercialProductos.getPrepagoEventosCollection().add(prepagoEventos);
                fkComercialProductos = em.merge(fkComercialProductos);
            }
            if (fkAmyTipoEventos != null) {
                fkAmyTipoEventos.getPrepagoEventosCollection().add(prepagoEventos);
                fkAmyTipoEventos = em.merge(fkAmyTipoEventos);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPrepagoEventos(prepagoEventos.getIdEventos()) != null) {
                throw new PreexistingEntityException("PrepagoEventos " + prepagoEventos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PrepagoEventos prepagoEventos) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PrepagoEventos persistentPrepagoEventos = em.find(PrepagoEventos.class, prepagoEventos.getIdEventos());
            ComercialProductos fkComercialProductosOld = persistentPrepagoEventos.getFkComercialProductos();
            ComercialProductos fkComercialProductosNew = prepagoEventos.getFkComercialProductos();
            AmyTipoEventos fkAmyTipoEventosOld = persistentPrepagoEventos.getFkAmyTipoEventos();
            AmyTipoEventos fkAmyTipoEventosNew = prepagoEventos.getFkAmyTipoEventos();
            if (fkComercialProductosNew != null) {
                fkComercialProductosNew = em.getReference(fkComercialProductosNew.getClass(), fkComercialProductosNew.getIdProductos());
                prepagoEventos.setFkComercialProductos(fkComercialProductosNew);
            }
            if (fkAmyTipoEventosNew != null) {
                fkAmyTipoEventosNew = em.getReference(fkAmyTipoEventosNew.getClass(), fkAmyTipoEventosNew.getIdTipoEvento());
                prepagoEventos.setFkAmyTipoEventos(fkAmyTipoEventosNew);
            }
            prepagoEventos = em.merge(prepagoEventos);
            if (fkComercialProductosOld != null && !fkComercialProductosOld.equals(fkComercialProductosNew)) {
                fkComercialProductosOld.getPrepagoEventosCollection().remove(prepagoEventos);
                fkComercialProductosOld = em.merge(fkComercialProductosOld);
            }
            if (fkComercialProductosNew != null && !fkComercialProductosNew.equals(fkComercialProductosOld)) {
                fkComercialProductosNew.getPrepagoEventosCollection().add(prepagoEventos);
                fkComercialProductosNew = em.merge(fkComercialProductosNew);
            }
            if (fkAmyTipoEventosOld != null && !fkAmyTipoEventosOld.equals(fkAmyTipoEventosNew)) {
                fkAmyTipoEventosOld.getPrepagoEventosCollection().remove(prepagoEventos);
                fkAmyTipoEventosOld = em.merge(fkAmyTipoEventosOld);
            }
            if (fkAmyTipoEventosNew != null && !fkAmyTipoEventosNew.equals(fkAmyTipoEventosOld)) {
                fkAmyTipoEventosNew.getPrepagoEventosCollection().add(prepagoEventos);
                fkAmyTipoEventosNew = em.merge(fkAmyTipoEventosNew);
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
                Long id = prepagoEventos.getIdEventos();
                if (findPrepagoEventos(id) == null) {
                    throw new NonexistentEntityException("The prepagoEventos with id " + id + " no longer exists.");
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
            PrepagoEventos prepagoEventos;
            try {
                prepagoEventos = em.getReference(PrepagoEventos.class, id);
                prepagoEventos.getIdEventos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The prepagoEventos with id " + id + " no longer exists.", enfe);
            }
            ComercialProductos fkComercialProductos = prepagoEventos.getFkComercialProductos();
            if (fkComercialProductos != null) {
                fkComercialProductos.getPrepagoEventosCollection().remove(prepagoEventos);
                fkComercialProductos = em.merge(fkComercialProductos);
            }
            AmyTipoEventos fkAmyTipoEventos = prepagoEventos.getFkAmyTipoEventos();
            if (fkAmyTipoEventos != null) {
                fkAmyTipoEventos.getPrepagoEventosCollection().remove(prepagoEventos);
                fkAmyTipoEventos = em.merge(fkAmyTipoEventos);
            }
            em.remove(prepagoEventos);
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

    public List<PrepagoEventos> findPrepagoEventosEntities() {
        return findPrepagoEventosEntities(true, -1, -1);
    }

    public List<PrepagoEventos> findPrepagoEventosEntities(int maxResults, int firstResult) {
        return findPrepagoEventosEntities(false, maxResults, firstResult);
    }

    private List<PrepagoEventos> findPrepagoEventosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PrepagoEventos.class));
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

    public PrepagoEventos findPrepagoEventos(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PrepagoEventos.class, id);
        } finally {
            em.close();
        }
    }

    public int getPrepagoEventosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PrepagoEventos> rt = cq.from(PrepagoEventos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
