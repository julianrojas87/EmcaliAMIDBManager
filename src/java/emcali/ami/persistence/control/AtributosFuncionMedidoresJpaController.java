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
import emcali.ami.persistence.entity.AtrFncsActvMedidores;
import emcali.ami.persistence.entity.AtributosFuncionMedidores;
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
public class AtributosFuncionMedidoresJpaController implements Serializable {

    public AtributosFuncionMedidoresJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AtributosFuncionMedidores atributosFuncionMedidores) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (atributosFuncionMedidores.getAtrFncsActvMedidoresCollection() == null) {
            atributosFuncionMedidores.setAtrFncsActvMedidoresCollection(new ArrayList<AtrFncsActvMedidores>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<AtrFncsActvMedidores> attachedAtrFncsActvMedidoresCollection = new ArrayList<AtrFncsActvMedidores>();
            for (AtrFncsActvMedidores atrFncsActvMedidoresCollectionAtrFncsActvMedidoresToAttach : atributosFuncionMedidores.getAtrFncsActvMedidoresCollection()) {
                atrFncsActvMedidoresCollectionAtrFncsActvMedidoresToAttach = em.getReference(atrFncsActvMedidoresCollectionAtrFncsActvMedidoresToAttach.getClass(), atrFncsActvMedidoresCollectionAtrFncsActvMedidoresToAttach.getIdFuncionActivaMedidor());
                attachedAtrFncsActvMedidoresCollection.add(atrFncsActvMedidoresCollectionAtrFncsActvMedidoresToAttach);
            }
            atributosFuncionMedidores.setAtrFncsActvMedidoresCollection(attachedAtrFncsActvMedidoresCollection);
            em.persist(atributosFuncionMedidores);
            for (AtrFncsActvMedidores atrFncsActvMedidoresCollectionAtrFncsActvMedidores : atributosFuncionMedidores.getAtrFncsActvMedidoresCollection()) {
                AtributosFuncionMedidores oldFkAtrFncMedidoresOfAtrFncsActvMedidoresCollectionAtrFncsActvMedidores = atrFncsActvMedidoresCollectionAtrFncsActvMedidores.getFkAtrFncMedidores();
                atrFncsActvMedidoresCollectionAtrFncsActvMedidores.setFkAtrFncMedidores(atributosFuncionMedidores);
                atrFncsActvMedidoresCollectionAtrFncsActvMedidores = em.merge(atrFncsActvMedidoresCollectionAtrFncsActvMedidores);
                if (oldFkAtrFncMedidoresOfAtrFncsActvMedidoresCollectionAtrFncsActvMedidores != null) {
                    oldFkAtrFncMedidoresOfAtrFncsActvMedidoresCollectionAtrFncsActvMedidores.getAtrFncsActvMedidoresCollection().remove(atrFncsActvMedidoresCollectionAtrFncsActvMedidores);
                    oldFkAtrFncMedidoresOfAtrFncsActvMedidoresCollectionAtrFncsActvMedidores = em.merge(oldFkAtrFncMedidoresOfAtrFncsActvMedidoresCollectionAtrFncsActvMedidores);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAtributosFuncionMedidores(atributosFuncionMedidores.getIdFuncionMedidores()) != null) {
                throw new PreexistingEntityException("AtributosFuncionMedidores " + atributosFuncionMedidores + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AtributosFuncionMedidores atributosFuncionMedidores) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AtributosFuncionMedidores persistentAtributosFuncionMedidores = em.find(AtributosFuncionMedidores.class, atributosFuncionMedidores.getIdFuncionMedidores());
            Collection<AtrFncsActvMedidores> atrFncsActvMedidoresCollectionOld = persistentAtributosFuncionMedidores.getAtrFncsActvMedidoresCollection();
            Collection<AtrFncsActvMedidores> atrFncsActvMedidoresCollectionNew = atributosFuncionMedidores.getAtrFncsActvMedidoresCollection();
            List<String> illegalOrphanMessages = null;
            for (AtrFncsActvMedidores atrFncsActvMedidoresCollectionOldAtrFncsActvMedidores : atrFncsActvMedidoresCollectionOld) {
                if (!atrFncsActvMedidoresCollectionNew.contains(atrFncsActvMedidoresCollectionOldAtrFncsActvMedidores)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AtrFncsActvMedidores " + atrFncsActvMedidoresCollectionOldAtrFncsActvMedidores + " since its fkAtrFncMedidores field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<AtrFncsActvMedidores> attachedAtrFncsActvMedidoresCollectionNew = new ArrayList<AtrFncsActvMedidores>();
            for (AtrFncsActvMedidores atrFncsActvMedidoresCollectionNewAtrFncsActvMedidoresToAttach : atrFncsActvMedidoresCollectionNew) {
                atrFncsActvMedidoresCollectionNewAtrFncsActvMedidoresToAttach = em.getReference(atrFncsActvMedidoresCollectionNewAtrFncsActvMedidoresToAttach.getClass(), atrFncsActvMedidoresCollectionNewAtrFncsActvMedidoresToAttach.getIdFuncionActivaMedidor());
                attachedAtrFncsActvMedidoresCollectionNew.add(atrFncsActvMedidoresCollectionNewAtrFncsActvMedidoresToAttach);
            }
            atrFncsActvMedidoresCollectionNew = attachedAtrFncsActvMedidoresCollectionNew;
            atributosFuncionMedidores.setAtrFncsActvMedidoresCollection(atrFncsActvMedidoresCollectionNew);
            atributosFuncionMedidores = em.merge(atributosFuncionMedidores);
            for (AtrFncsActvMedidores atrFncsActvMedidoresCollectionNewAtrFncsActvMedidores : atrFncsActvMedidoresCollectionNew) {
                if (!atrFncsActvMedidoresCollectionOld.contains(atrFncsActvMedidoresCollectionNewAtrFncsActvMedidores)) {
                    AtributosFuncionMedidores oldFkAtrFncMedidoresOfAtrFncsActvMedidoresCollectionNewAtrFncsActvMedidores = atrFncsActvMedidoresCollectionNewAtrFncsActvMedidores.getFkAtrFncMedidores();
                    atrFncsActvMedidoresCollectionNewAtrFncsActvMedidores.setFkAtrFncMedidores(atributosFuncionMedidores);
                    atrFncsActvMedidoresCollectionNewAtrFncsActvMedidores = em.merge(atrFncsActvMedidoresCollectionNewAtrFncsActvMedidores);
                    if (oldFkAtrFncMedidoresOfAtrFncsActvMedidoresCollectionNewAtrFncsActvMedidores != null && !oldFkAtrFncMedidoresOfAtrFncsActvMedidoresCollectionNewAtrFncsActvMedidores.equals(atributosFuncionMedidores)) {
                        oldFkAtrFncMedidoresOfAtrFncsActvMedidoresCollectionNewAtrFncsActvMedidores.getAtrFncsActvMedidoresCollection().remove(atrFncsActvMedidoresCollectionNewAtrFncsActvMedidores);
                        oldFkAtrFncMedidoresOfAtrFncsActvMedidoresCollectionNewAtrFncsActvMedidores = em.merge(oldFkAtrFncMedidoresOfAtrFncsActvMedidoresCollectionNewAtrFncsActvMedidores);
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
                Long id = atributosFuncionMedidores.getIdFuncionMedidores();
                if (findAtributosFuncionMedidores(id) == null) {
                    throw new NonexistentEntityException("The atributosFuncionMedidores with id " + id + " no longer exists.");
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
            AtributosFuncionMedidores atributosFuncionMedidores;
            try {
                atributosFuncionMedidores = em.getReference(AtributosFuncionMedidores.class, id);
                atributosFuncionMedidores.getIdFuncionMedidores();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The atributosFuncionMedidores with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<AtrFncsActvMedidores> atrFncsActvMedidoresCollectionOrphanCheck = atributosFuncionMedidores.getAtrFncsActvMedidoresCollection();
            for (AtrFncsActvMedidores atrFncsActvMedidoresCollectionOrphanCheckAtrFncsActvMedidores : atrFncsActvMedidoresCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AtributosFuncionMedidores (" + atributosFuncionMedidores + ") cannot be destroyed since the AtrFncsActvMedidores " + atrFncsActvMedidoresCollectionOrphanCheckAtrFncsActvMedidores + " in its atrFncsActvMedidoresCollection field has a non-nullable fkAtrFncMedidores field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(atributosFuncionMedidores);
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

    public List<AtributosFuncionMedidores> findAtributosFuncionMedidoresEntities() {
        return findAtributosFuncionMedidoresEntities(true, -1, -1);
    }

    public List<AtributosFuncionMedidores> findAtributosFuncionMedidoresEntities(int maxResults, int firstResult) {
        return findAtributosFuncionMedidoresEntities(false, maxResults, firstResult);
    }

    private List<AtributosFuncionMedidores> findAtributosFuncionMedidoresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AtributosFuncionMedidores.class));
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

    public AtributosFuncionMedidores findAtributosFuncionMedidores(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AtributosFuncionMedidores.class, id);
        } finally {
            em.close();
        }
    }

    public int getAtributosFuncionMedidoresCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AtributosFuncionMedidores> rt = cq.from(AtributosFuncionMedidores.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
