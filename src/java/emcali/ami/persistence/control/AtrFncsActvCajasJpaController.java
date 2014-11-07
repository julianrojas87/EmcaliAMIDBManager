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
import emcali.ami.persistence.entity.AtributosFuncionCajas;
import emcali.ami.persistence.entity.AmyCajas;
import emcali.ami.persistence.entity.AtrFncsActvCajas;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class AtrFncsActvCajasJpaController implements Serializable {

    public AtrFncsActvCajasJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AtrFncsActvCajas atrFncsActvCajas) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AtributosFuncionCajas fkAtributosFuncionCajas = atrFncsActvCajas.getFkAtributosFuncionCajas();
            if (fkAtributosFuncionCajas != null) {
                fkAtributosFuncionCajas = em.getReference(fkAtributosFuncionCajas.getClass(), fkAtributosFuncionCajas.getIdFuncion());
                atrFncsActvCajas.setFkAtributosFuncionCajas(fkAtributosFuncionCajas);
            }
            AmyCajas fkAmyCajas = atrFncsActvCajas.getFkAmyCajas();
            if (fkAmyCajas != null) {
                fkAmyCajas = em.getReference(fkAmyCajas.getClass(), fkAmyCajas.getIdCajas());
                atrFncsActvCajas.setFkAmyCajas(fkAmyCajas);
            }
            em.persist(atrFncsActvCajas);
            if (fkAtributosFuncionCajas != null) {
                fkAtributosFuncionCajas.getAtrFncsActvCajasList().add(atrFncsActvCajas);
                fkAtributosFuncionCajas = em.merge(fkAtributosFuncionCajas);
            }
            if (fkAmyCajas != null) {
                fkAmyCajas.getAtrFncsActvCajasList().add(atrFncsActvCajas);
                fkAmyCajas = em.merge(fkAmyCajas);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAtrFncsActvCajas(atrFncsActvCajas.getIdFuncionActivaCaja()) != null) {
                throw new PreexistingEntityException("AtrFncsActvCajas " + atrFncsActvCajas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AtrFncsActvCajas atrFncsActvCajas) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AtrFncsActvCajas persistentAtrFncsActvCajas = em.find(AtrFncsActvCajas.class, atrFncsActvCajas.getIdFuncionActivaCaja());
            AtributosFuncionCajas fkAtributosFuncionCajasOld = persistentAtrFncsActvCajas.getFkAtributosFuncionCajas();
            AtributosFuncionCajas fkAtributosFuncionCajasNew = atrFncsActvCajas.getFkAtributosFuncionCajas();
            AmyCajas fkAmyCajasOld = persistentAtrFncsActvCajas.getFkAmyCajas();
            AmyCajas fkAmyCajasNew = atrFncsActvCajas.getFkAmyCajas();
            if (fkAtributosFuncionCajasNew != null) {
                fkAtributosFuncionCajasNew = em.getReference(fkAtributosFuncionCajasNew.getClass(), fkAtributosFuncionCajasNew.getIdFuncion());
                atrFncsActvCajas.setFkAtributosFuncionCajas(fkAtributosFuncionCajasNew);
            }
            if (fkAmyCajasNew != null) {
                fkAmyCajasNew = em.getReference(fkAmyCajasNew.getClass(), fkAmyCajasNew.getIdCajas());
                atrFncsActvCajas.setFkAmyCajas(fkAmyCajasNew);
            }
            atrFncsActvCajas = em.merge(atrFncsActvCajas);
            if (fkAtributosFuncionCajasOld != null && !fkAtributosFuncionCajasOld.equals(fkAtributosFuncionCajasNew)) {
                fkAtributosFuncionCajasOld.getAtrFncsActvCajasList().remove(atrFncsActvCajas);
                fkAtributosFuncionCajasOld = em.merge(fkAtributosFuncionCajasOld);
            }
            if (fkAtributosFuncionCajasNew != null && !fkAtributosFuncionCajasNew.equals(fkAtributosFuncionCajasOld)) {
                fkAtributosFuncionCajasNew.getAtrFncsActvCajasList().add(atrFncsActvCajas);
                fkAtributosFuncionCajasNew = em.merge(fkAtributosFuncionCajasNew);
            }
            if (fkAmyCajasOld != null && !fkAmyCajasOld.equals(fkAmyCajasNew)) {
                fkAmyCajasOld.getAtrFncsActvCajasList().remove(atrFncsActvCajas);
                fkAmyCajasOld = em.merge(fkAmyCajasOld);
            }
            if (fkAmyCajasNew != null && !fkAmyCajasNew.equals(fkAmyCajasOld)) {
                fkAmyCajasNew.getAtrFncsActvCajasList().add(atrFncsActvCajas);
                fkAmyCajasNew = em.merge(fkAmyCajasNew);
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
                Long id = atrFncsActvCajas.getIdFuncionActivaCaja();
                if (findAtrFncsActvCajas(id) == null) {
                    throw new NonexistentEntityException("The atrFncsActvCajas with id " + id + " no longer exists.");
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
            AtrFncsActvCajas atrFncsActvCajas;
            try {
                atrFncsActvCajas = em.getReference(AtrFncsActvCajas.class, id);
                atrFncsActvCajas.getIdFuncionActivaCaja();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The atrFncsActvCajas with id " + id + " no longer exists.", enfe);
            }
            AtributosFuncionCajas fkAtributosFuncionCajas = atrFncsActvCajas.getFkAtributosFuncionCajas();
            if (fkAtributosFuncionCajas != null) {
                fkAtributosFuncionCajas.getAtrFncsActvCajasList().remove(atrFncsActvCajas);
                fkAtributosFuncionCajas = em.merge(fkAtributosFuncionCajas);
            }
            AmyCajas fkAmyCajas = atrFncsActvCajas.getFkAmyCajas();
            if (fkAmyCajas != null) {
                fkAmyCajas.getAtrFncsActvCajasList().remove(atrFncsActvCajas);
                fkAmyCajas = em.merge(fkAmyCajas);
            }
            em.remove(atrFncsActvCajas);
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

    public List<AtrFncsActvCajas> findAtrFncsActvCajasEntities() {
        return findAtrFncsActvCajasEntities(true, -1, -1);
    }

    public List<AtrFncsActvCajas> findAtrFncsActvCajasEntities(int maxResults, int firstResult) {
        return findAtrFncsActvCajasEntities(false, maxResults, firstResult);
    }

    private List<AtrFncsActvCajas> findAtrFncsActvCajasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AtrFncsActvCajas.class));
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

    public AtrFncsActvCajas findAtrFncsActvCajas(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AtrFncsActvCajas.class, id);
        } finally {
            em.close();
        }
    }

    public int getAtrFncsActvCajasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AtrFncsActvCajas> rt = cq.from(AtrFncsActvCajas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
