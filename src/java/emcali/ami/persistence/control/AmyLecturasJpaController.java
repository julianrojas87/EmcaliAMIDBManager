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
import emcali.ami.persistence.entity.AmyTipolecturas;
import emcali.ami.persistence.entity.AmyMedidores;
import emcali.ami.persistence.entity.AmyInterval;
import emcali.ami.persistence.entity.AmyCanal;
import emcali.ami.persistence.entity.AmyLecturas;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class AmyLecturasJpaController implements Serializable {

    public AmyLecturasJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AmyLecturas amyLecturas) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AmyTipolecturas fkAmyTipolecturas = amyLecturas.getFkAmyTipolecturas();
            if (fkAmyTipolecturas != null) {
                fkAmyTipolecturas = em.getReference(fkAmyTipolecturas.getClass(), fkAmyTipolecturas.getIdTipolectura());
                amyLecturas.setFkAmyTipolecturas(fkAmyTipolecturas);
            }
            AmyMedidores fkAmyMedidores = amyLecturas.getFkAmyMedidores();
            if (fkAmyMedidores != null) {
                fkAmyMedidores = em.getReference(fkAmyMedidores.getClass(), fkAmyMedidores.getIdMedidores());
                amyLecturas.setFkAmyMedidores(fkAmyMedidores);
            }
            AmyInterval fkAmyInterval = amyLecturas.getFkAmyInterval();
            if (fkAmyInterval != null) {
                fkAmyInterval = em.getReference(fkAmyInterval.getClass(), fkAmyInterval.getIdTiempo());
                amyLecturas.setFkAmyInterval(fkAmyInterval);
            }
            AmyCanal fkAmyCanal = amyLecturas.getFkAmyCanal();
            if (fkAmyCanal != null) {
                fkAmyCanal = em.getReference(fkAmyCanal.getClass(), fkAmyCanal.getIdCanal());
                amyLecturas.setFkAmyCanal(fkAmyCanal);
            }
            em.persist(amyLecturas);
            if (fkAmyTipolecturas != null) {
                fkAmyTipolecturas.getAmyLecturasCollection().add(amyLecturas);
                fkAmyTipolecturas = em.merge(fkAmyTipolecturas);
            }
            if (fkAmyMedidores != null) {
                fkAmyMedidores.getAmyLecturasCollection().add(amyLecturas);
                fkAmyMedidores = em.merge(fkAmyMedidores);
            }
            if (fkAmyInterval != null) {
                fkAmyInterval.getAmyLecturasCollection().add(amyLecturas);
                fkAmyInterval = em.merge(fkAmyInterval);
            }
            if (fkAmyCanal != null) {
                fkAmyCanal.getAmyLecturasCollection().add(amyLecturas);
                fkAmyCanal = em.merge(fkAmyCanal);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAmyLecturas(amyLecturas.getIdLecturas()) != null) {
                throw new PreexistingEntityException("AmyLecturas " + amyLecturas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AmyLecturas amyLecturas) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AmyLecturas persistentAmyLecturas = em.find(AmyLecturas.class, amyLecturas.getIdLecturas());
            AmyTipolecturas fkAmyTipolecturasOld = persistentAmyLecturas.getFkAmyTipolecturas();
            AmyTipolecturas fkAmyTipolecturasNew = amyLecturas.getFkAmyTipolecturas();
            AmyMedidores fkAmyMedidoresOld = persistentAmyLecturas.getFkAmyMedidores();
            AmyMedidores fkAmyMedidoresNew = amyLecturas.getFkAmyMedidores();
            AmyInterval fkAmyIntervalOld = persistentAmyLecturas.getFkAmyInterval();
            AmyInterval fkAmyIntervalNew = amyLecturas.getFkAmyInterval();
            AmyCanal fkAmyCanalOld = persistentAmyLecturas.getFkAmyCanal();
            AmyCanal fkAmyCanalNew = amyLecturas.getFkAmyCanal();
            if (fkAmyTipolecturasNew != null) {
                fkAmyTipolecturasNew = em.getReference(fkAmyTipolecturasNew.getClass(), fkAmyTipolecturasNew.getIdTipolectura());
                amyLecturas.setFkAmyTipolecturas(fkAmyTipolecturasNew);
            }
            if (fkAmyMedidoresNew != null) {
                fkAmyMedidoresNew = em.getReference(fkAmyMedidoresNew.getClass(), fkAmyMedidoresNew.getIdMedidores());
                amyLecturas.setFkAmyMedidores(fkAmyMedidoresNew);
            }
            if (fkAmyIntervalNew != null) {
                fkAmyIntervalNew = em.getReference(fkAmyIntervalNew.getClass(), fkAmyIntervalNew.getIdTiempo());
                amyLecturas.setFkAmyInterval(fkAmyIntervalNew);
            }
            if (fkAmyCanalNew != null) {
                fkAmyCanalNew = em.getReference(fkAmyCanalNew.getClass(), fkAmyCanalNew.getIdCanal());
                amyLecturas.setFkAmyCanal(fkAmyCanalNew);
            }
            amyLecturas = em.merge(amyLecturas);
            if (fkAmyTipolecturasOld != null && !fkAmyTipolecturasOld.equals(fkAmyTipolecturasNew)) {
                fkAmyTipolecturasOld.getAmyLecturasCollection().remove(amyLecturas);
                fkAmyTipolecturasOld = em.merge(fkAmyTipolecturasOld);
            }
            if (fkAmyTipolecturasNew != null && !fkAmyTipolecturasNew.equals(fkAmyTipolecturasOld)) {
                fkAmyTipolecturasNew.getAmyLecturasCollection().add(amyLecturas);
                fkAmyTipolecturasNew = em.merge(fkAmyTipolecturasNew);
            }
            if (fkAmyMedidoresOld != null && !fkAmyMedidoresOld.equals(fkAmyMedidoresNew)) {
                fkAmyMedidoresOld.getAmyLecturasCollection().remove(amyLecturas);
                fkAmyMedidoresOld = em.merge(fkAmyMedidoresOld);
            }
            if (fkAmyMedidoresNew != null && !fkAmyMedidoresNew.equals(fkAmyMedidoresOld)) {
                fkAmyMedidoresNew.getAmyLecturasCollection().add(amyLecturas);
                fkAmyMedidoresNew = em.merge(fkAmyMedidoresNew);
            }
            if (fkAmyIntervalOld != null && !fkAmyIntervalOld.equals(fkAmyIntervalNew)) {
                fkAmyIntervalOld.getAmyLecturasCollection().remove(amyLecturas);
                fkAmyIntervalOld = em.merge(fkAmyIntervalOld);
            }
            if (fkAmyIntervalNew != null && !fkAmyIntervalNew.equals(fkAmyIntervalOld)) {
                fkAmyIntervalNew.getAmyLecturasCollection().add(amyLecturas);
                fkAmyIntervalNew = em.merge(fkAmyIntervalNew);
            }
            if (fkAmyCanalOld != null && !fkAmyCanalOld.equals(fkAmyCanalNew)) {
                fkAmyCanalOld.getAmyLecturasCollection().remove(amyLecturas);
                fkAmyCanalOld = em.merge(fkAmyCanalOld);
            }
            if (fkAmyCanalNew != null && !fkAmyCanalNew.equals(fkAmyCanalOld)) {
                fkAmyCanalNew.getAmyLecturasCollection().add(amyLecturas);
                fkAmyCanalNew = em.merge(fkAmyCanalNew);
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
                Long id = amyLecturas.getIdLecturas();
                if (findAmyLecturas(id) == null) {
                    throw new NonexistentEntityException("The amyLecturas with id " + id + " no longer exists.");
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
            AmyLecturas amyLecturas;
            try {
                amyLecturas = em.getReference(AmyLecturas.class, id);
                amyLecturas.getIdLecturas();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The amyLecturas with id " + id + " no longer exists.", enfe);
            }
            AmyTipolecturas fkAmyTipolecturas = amyLecturas.getFkAmyTipolecturas();
            if (fkAmyTipolecturas != null) {
                fkAmyTipolecturas.getAmyLecturasCollection().remove(amyLecturas);
                fkAmyTipolecturas = em.merge(fkAmyTipolecturas);
            }
            AmyMedidores fkAmyMedidores = amyLecturas.getFkAmyMedidores();
            if (fkAmyMedidores != null) {
                fkAmyMedidores.getAmyLecturasCollection().remove(amyLecturas);
                fkAmyMedidores = em.merge(fkAmyMedidores);
            }
            AmyInterval fkAmyInterval = amyLecturas.getFkAmyInterval();
            if (fkAmyInterval != null) {
                fkAmyInterval.getAmyLecturasCollection().remove(amyLecturas);
                fkAmyInterval = em.merge(fkAmyInterval);
            }
            AmyCanal fkAmyCanal = amyLecturas.getFkAmyCanal();
            if (fkAmyCanal != null) {
                fkAmyCanal.getAmyLecturasCollection().remove(amyLecturas);
                fkAmyCanal = em.merge(fkAmyCanal);
            }
            em.remove(amyLecturas);
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

    public List<AmyLecturas> findAmyLecturasEntities() {
        return findAmyLecturasEntities(true, -1, -1);
    }

    public List<AmyLecturas> findAmyLecturasEntities(int maxResults, int firstResult) {
        return findAmyLecturasEntities(false, maxResults, firstResult);
    }

    private List<AmyLecturas> findAmyLecturasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AmyLecturas.class));
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

    public AmyLecturas findAmyLecturas(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AmyLecturas.class, id);
        } finally {
            em.close();
        }
    }

    public int getAmyLecturasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AmyLecturas> rt = cq.from(AmyLecturas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
