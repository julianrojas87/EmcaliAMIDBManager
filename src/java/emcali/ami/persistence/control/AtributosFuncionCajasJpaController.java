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
import emcali.ami.persistence.entity.AtrFncsActvCajas;
import emcali.ami.persistence.entity.AtributosFuncionCajas;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class AtributosFuncionCajasJpaController implements Serializable {

    public AtributosFuncionCajasJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AtributosFuncionCajas atributosFuncionCajas) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (atributosFuncionCajas.getAtrFncsActvCajasCollection() == null) {
            atributosFuncionCajas.setAtrFncsActvCajasCollection(new ArrayList<AtrFncsActvCajas>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<AtrFncsActvCajas> attachedAtrFncsActvCajasCollection = new ArrayList<AtrFncsActvCajas>();
            for (AtrFncsActvCajas atrFncsActvCajasCollectionAtrFncsActvCajasToAttach : atributosFuncionCajas.getAtrFncsActvCajasCollection()) {
                atrFncsActvCajasCollectionAtrFncsActvCajasToAttach = em.getReference(atrFncsActvCajasCollectionAtrFncsActvCajasToAttach.getClass(), atrFncsActvCajasCollectionAtrFncsActvCajasToAttach.getIdFuncionActivaCaja());
                attachedAtrFncsActvCajasCollection.add(atrFncsActvCajasCollectionAtrFncsActvCajasToAttach);
            }
            atributosFuncionCajas.setAtrFncsActvCajasCollection(attachedAtrFncsActvCajasCollection);
            em.persist(atributosFuncionCajas);
            for (AtrFncsActvCajas atrFncsActvCajasCollectionAtrFncsActvCajas : atributosFuncionCajas.getAtrFncsActvCajasCollection()) {
                AtributosFuncionCajas oldFkAtributosFuncionCajasOfAtrFncsActvCajasCollectionAtrFncsActvCajas = atrFncsActvCajasCollectionAtrFncsActvCajas.getFkAtributosFuncionCajas();
                atrFncsActvCajasCollectionAtrFncsActvCajas.setFkAtributosFuncionCajas(atributosFuncionCajas);
                atrFncsActvCajasCollectionAtrFncsActvCajas = em.merge(atrFncsActvCajasCollectionAtrFncsActvCajas);
                if (oldFkAtributosFuncionCajasOfAtrFncsActvCajasCollectionAtrFncsActvCajas != null) {
                    oldFkAtributosFuncionCajasOfAtrFncsActvCajasCollectionAtrFncsActvCajas.getAtrFncsActvCajasCollection().remove(atrFncsActvCajasCollectionAtrFncsActvCajas);
                    oldFkAtributosFuncionCajasOfAtrFncsActvCajasCollectionAtrFncsActvCajas = em.merge(oldFkAtributosFuncionCajasOfAtrFncsActvCajasCollectionAtrFncsActvCajas);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAtributosFuncionCajas(atributosFuncionCajas.getIdFuncion()) != null) {
                throw new PreexistingEntityException("AtributosFuncionCajas " + atributosFuncionCajas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AtributosFuncionCajas atributosFuncionCajas) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AtributosFuncionCajas persistentAtributosFuncionCajas = em.find(AtributosFuncionCajas.class, atributosFuncionCajas.getIdFuncion());
            Collection<AtrFncsActvCajas> atrFncsActvCajasCollectionOld = persistentAtributosFuncionCajas.getAtrFncsActvCajasCollection();
            Collection<AtrFncsActvCajas> atrFncsActvCajasCollectionNew = atributosFuncionCajas.getAtrFncsActvCajasCollection();
            List<String> illegalOrphanMessages = null;
            for (AtrFncsActvCajas atrFncsActvCajasCollectionOldAtrFncsActvCajas : atrFncsActvCajasCollectionOld) {
                if (!atrFncsActvCajasCollectionNew.contains(atrFncsActvCajasCollectionOldAtrFncsActvCajas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AtrFncsActvCajas " + atrFncsActvCajasCollectionOldAtrFncsActvCajas + " since its fkAtributosFuncionCajas field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<AtrFncsActvCajas> attachedAtrFncsActvCajasCollectionNew = new ArrayList<AtrFncsActvCajas>();
            for (AtrFncsActvCajas atrFncsActvCajasCollectionNewAtrFncsActvCajasToAttach : atrFncsActvCajasCollectionNew) {
                atrFncsActvCajasCollectionNewAtrFncsActvCajasToAttach = em.getReference(atrFncsActvCajasCollectionNewAtrFncsActvCajasToAttach.getClass(), atrFncsActvCajasCollectionNewAtrFncsActvCajasToAttach.getIdFuncionActivaCaja());
                attachedAtrFncsActvCajasCollectionNew.add(atrFncsActvCajasCollectionNewAtrFncsActvCajasToAttach);
            }
            atrFncsActvCajasCollectionNew = attachedAtrFncsActvCajasCollectionNew;
            atributosFuncionCajas.setAtrFncsActvCajasCollection(atrFncsActvCajasCollectionNew);
            atributosFuncionCajas = em.merge(atributosFuncionCajas);
            for (AtrFncsActvCajas atrFncsActvCajasCollectionNewAtrFncsActvCajas : atrFncsActvCajasCollectionNew) {
                if (!atrFncsActvCajasCollectionOld.contains(atrFncsActvCajasCollectionNewAtrFncsActvCajas)) {
                    AtributosFuncionCajas oldFkAtributosFuncionCajasOfAtrFncsActvCajasCollectionNewAtrFncsActvCajas = atrFncsActvCajasCollectionNewAtrFncsActvCajas.getFkAtributosFuncionCajas();
                    atrFncsActvCajasCollectionNewAtrFncsActvCajas.setFkAtributosFuncionCajas(atributosFuncionCajas);
                    atrFncsActvCajasCollectionNewAtrFncsActvCajas = em.merge(atrFncsActvCajasCollectionNewAtrFncsActvCajas);
                    if (oldFkAtributosFuncionCajasOfAtrFncsActvCajasCollectionNewAtrFncsActvCajas != null && !oldFkAtributosFuncionCajasOfAtrFncsActvCajasCollectionNewAtrFncsActvCajas.equals(atributosFuncionCajas)) {
                        oldFkAtributosFuncionCajasOfAtrFncsActvCajasCollectionNewAtrFncsActvCajas.getAtrFncsActvCajasCollection().remove(atrFncsActvCajasCollectionNewAtrFncsActvCajas);
                        oldFkAtributosFuncionCajasOfAtrFncsActvCajasCollectionNewAtrFncsActvCajas = em.merge(oldFkAtributosFuncionCajasOfAtrFncsActvCajasCollectionNewAtrFncsActvCajas);
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
                Long id = atributosFuncionCajas.getIdFuncion();
                if (findAtributosFuncionCajas(id) == null) {
                    throw new NonexistentEntityException("The atributosFuncionCajas with id " + id + " no longer exists.");
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
            AtributosFuncionCajas atributosFuncionCajas;
            try {
                atributosFuncionCajas = em.getReference(AtributosFuncionCajas.class, id);
                atributosFuncionCajas.getIdFuncion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The atributosFuncionCajas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<AtrFncsActvCajas> atrFncsActvCajasCollectionOrphanCheck = atributosFuncionCajas.getAtrFncsActvCajasCollection();
            for (AtrFncsActvCajas atrFncsActvCajasCollectionOrphanCheckAtrFncsActvCajas : atrFncsActvCajasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AtributosFuncionCajas (" + atributosFuncionCajas + ") cannot be destroyed since the AtrFncsActvCajas " + atrFncsActvCajasCollectionOrphanCheckAtrFncsActvCajas + " in its atrFncsActvCajasCollection field has a non-nullable fkAtributosFuncionCajas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(atributosFuncionCajas);
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

    public List<AtributosFuncionCajas> findAtributosFuncionCajasEntities() {
        return findAtributosFuncionCajasEntities(true, -1, -1);
    }

    public List<AtributosFuncionCajas> findAtributosFuncionCajasEntities(int maxResults, int firstResult) {
        return findAtributosFuncionCajasEntities(false, maxResults, firstResult);
    }

    private List<AtributosFuncionCajas> findAtributosFuncionCajasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AtributosFuncionCajas.class));
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

    public AtributosFuncionCajas findAtributosFuncionCajas(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AtributosFuncionCajas.class, id);
        } finally {
            em.close();
        }
    }

    public int getAtributosFuncionCajasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AtributosFuncionCajas> rt = cq.from(AtributosFuncionCajas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
