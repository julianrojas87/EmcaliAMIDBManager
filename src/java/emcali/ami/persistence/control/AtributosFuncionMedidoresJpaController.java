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
        if (atributosFuncionMedidores.getAtrFncsActvMedidoresList() == null) {
            atributosFuncionMedidores.setAtrFncsActvMedidoresList(new ArrayList<AtrFncsActvMedidores>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<AtrFncsActvMedidores> attachedAtrFncsActvMedidoresList = new ArrayList<AtrFncsActvMedidores>();
            for (AtrFncsActvMedidores atrFncsActvMedidoresListAtrFncsActvMedidoresToAttach : atributosFuncionMedidores.getAtrFncsActvMedidoresList()) {
                atrFncsActvMedidoresListAtrFncsActvMedidoresToAttach = em.getReference(atrFncsActvMedidoresListAtrFncsActvMedidoresToAttach.getClass(), atrFncsActvMedidoresListAtrFncsActvMedidoresToAttach.getIdFuncionActivaMedidor());
                attachedAtrFncsActvMedidoresList.add(atrFncsActvMedidoresListAtrFncsActvMedidoresToAttach);
            }
            atributosFuncionMedidores.setAtrFncsActvMedidoresList(attachedAtrFncsActvMedidoresList);
            em.persist(atributosFuncionMedidores);
            for (AtrFncsActvMedidores atrFncsActvMedidoresListAtrFncsActvMedidores : atributosFuncionMedidores.getAtrFncsActvMedidoresList()) {
                AtributosFuncionMedidores oldFkAtrFncMedidoresOfAtrFncsActvMedidoresListAtrFncsActvMedidores = atrFncsActvMedidoresListAtrFncsActvMedidores.getFkAtrFncMedidores();
                atrFncsActvMedidoresListAtrFncsActvMedidores.setFkAtrFncMedidores(atributosFuncionMedidores);
                atrFncsActvMedidoresListAtrFncsActvMedidores = em.merge(atrFncsActvMedidoresListAtrFncsActvMedidores);
                if (oldFkAtrFncMedidoresOfAtrFncsActvMedidoresListAtrFncsActvMedidores != null) {
                    oldFkAtrFncMedidoresOfAtrFncsActvMedidoresListAtrFncsActvMedidores.getAtrFncsActvMedidoresList().remove(atrFncsActvMedidoresListAtrFncsActvMedidores);
                    oldFkAtrFncMedidoresOfAtrFncsActvMedidoresListAtrFncsActvMedidores = em.merge(oldFkAtrFncMedidoresOfAtrFncsActvMedidoresListAtrFncsActvMedidores);
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
            List<AtrFncsActvMedidores> atrFncsActvMedidoresListOld = persistentAtributosFuncionMedidores.getAtrFncsActvMedidoresList();
            List<AtrFncsActvMedidores> atrFncsActvMedidoresListNew = atributosFuncionMedidores.getAtrFncsActvMedidoresList();
            List<String> illegalOrphanMessages = null;
            for (AtrFncsActvMedidores atrFncsActvMedidoresListOldAtrFncsActvMedidores : atrFncsActvMedidoresListOld) {
                if (!atrFncsActvMedidoresListNew.contains(atrFncsActvMedidoresListOldAtrFncsActvMedidores)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AtrFncsActvMedidores " + atrFncsActvMedidoresListOldAtrFncsActvMedidores + " since its fkAtrFncMedidores field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<AtrFncsActvMedidores> attachedAtrFncsActvMedidoresListNew = new ArrayList<AtrFncsActvMedidores>();
            for (AtrFncsActvMedidores atrFncsActvMedidoresListNewAtrFncsActvMedidoresToAttach : atrFncsActvMedidoresListNew) {
                atrFncsActvMedidoresListNewAtrFncsActvMedidoresToAttach = em.getReference(atrFncsActvMedidoresListNewAtrFncsActvMedidoresToAttach.getClass(), atrFncsActvMedidoresListNewAtrFncsActvMedidoresToAttach.getIdFuncionActivaMedidor());
                attachedAtrFncsActvMedidoresListNew.add(atrFncsActvMedidoresListNewAtrFncsActvMedidoresToAttach);
            }
            atrFncsActvMedidoresListNew = attachedAtrFncsActvMedidoresListNew;
            atributosFuncionMedidores.setAtrFncsActvMedidoresList(atrFncsActvMedidoresListNew);
            atributosFuncionMedidores = em.merge(atributosFuncionMedidores);
            for (AtrFncsActvMedidores atrFncsActvMedidoresListNewAtrFncsActvMedidores : atrFncsActvMedidoresListNew) {
                if (!atrFncsActvMedidoresListOld.contains(atrFncsActvMedidoresListNewAtrFncsActvMedidores)) {
                    AtributosFuncionMedidores oldFkAtrFncMedidoresOfAtrFncsActvMedidoresListNewAtrFncsActvMedidores = atrFncsActvMedidoresListNewAtrFncsActvMedidores.getFkAtrFncMedidores();
                    atrFncsActvMedidoresListNewAtrFncsActvMedidores.setFkAtrFncMedidores(atributosFuncionMedidores);
                    atrFncsActvMedidoresListNewAtrFncsActvMedidores = em.merge(atrFncsActvMedidoresListNewAtrFncsActvMedidores);
                    if (oldFkAtrFncMedidoresOfAtrFncsActvMedidoresListNewAtrFncsActvMedidores != null && !oldFkAtrFncMedidoresOfAtrFncsActvMedidoresListNewAtrFncsActvMedidores.equals(atributosFuncionMedidores)) {
                        oldFkAtrFncMedidoresOfAtrFncsActvMedidoresListNewAtrFncsActvMedidores.getAtrFncsActvMedidoresList().remove(atrFncsActvMedidoresListNewAtrFncsActvMedidores);
                        oldFkAtrFncMedidoresOfAtrFncsActvMedidoresListNewAtrFncsActvMedidores = em.merge(oldFkAtrFncMedidoresOfAtrFncsActvMedidoresListNewAtrFncsActvMedidores);
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
            List<AtrFncsActvMedidores> atrFncsActvMedidoresListOrphanCheck = atributosFuncionMedidores.getAtrFncsActvMedidoresList();
            for (AtrFncsActvMedidores atrFncsActvMedidoresListOrphanCheckAtrFncsActvMedidores : atrFncsActvMedidoresListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AtributosFuncionMedidores (" + atributosFuncionMedidores + ") cannot be destroyed since the AtrFncsActvMedidores " + atrFncsActvMedidoresListOrphanCheckAtrFncsActvMedidores + " in its atrFncsActvMedidoresList field has a non-nullable fkAtrFncMedidores field.");
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
