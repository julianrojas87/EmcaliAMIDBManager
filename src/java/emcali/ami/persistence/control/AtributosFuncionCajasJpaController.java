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
        if (atributosFuncionCajas.getAtrFncsActvCajasList() == null) {
            atributosFuncionCajas.setAtrFncsActvCajasList(new ArrayList<AtrFncsActvCajas>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<AtrFncsActvCajas> attachedAtrFncsActvCajasList = new ArrayList<AtrFncsActvCajas>();
            for (AtrFncsActvCajas atrFncsActvCajasListAtrFncsActvCajasToAttach : atributosFuncionCajas.getAtrFncsActvCajasList()) {
                atrFncsActvCajasListAtrFncsActvCajasToAttach = em.getReference(atrFncsActvCajasListAtrFncsActvCajasToAttach.getClass(), atrFncsActvCajasListAtrFncsActvCajasToAttach.getIdFuncionActivaCaja());
                attachedAtrFncsActvCajasList.add(atrFncsActvCajasListAtrFncsActvCajasToAttach);
            }
            atributosFuncionCajas.setAtrFncsActvCajasList(attachedAtrFncsActvCajasList);
            em.persist(atributosFuncionCajas);
            for (AtrFncsActvCajas atrFncsActvCajasListAtrFncsActvCajas : atributosFuncionCajas.getAtrFncsActvCajasList()) {
                AtributosFuncionCajas oldFkAtributosFuncionCajasOfAtrFncsActvCajasListAtrFncsActvCajas = atrFncsActvCajasListAtrFncsActvCajas.getFkAtributosFuncionCajas();
                atrFncsActvCajasListAtrFncsActvCajas.setFkAtributosFuncionCajas(atributosFuncionCajas);
                atrFncsActvCajasListAtrFncsActvCajas = em.merge(atrFncsActvCajasListAtrFncsActvCajas);
                if (oldFkAtributosFuncionCajasOfAtrFncsActvCajasListAtrFncsActvCajas != null) {
                    oldFkAtributosFuncionCajasOfAtrFncsActvCajasListAtrFncsActvCajas.getAtrFncsActvCajasList().remove(atrFncsActvCajasListAtrFncsActvCajas);
                    oldFkAtributosFuncionCajasOfAtrFncsActvCajasListAtrFncsActvCajas = em.merge(oldFkAtributosFuncionCajasOfAtrFncsActvCajasListAtrFncsActvCajas);
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
            List<AtrFncsActvCajas> atrFncsActvCajasListOld = persistentAtributosFuncionCajas.getAtrFncsActvCajasList();
            List<AtrFncsActvCajas> atrFncsActvCajasListNew = atributosFuncionCajas.getAtrFncsActvCajasList();
            List<String> illegalOrphanMessages = null;
            for (AtrFncsActvCajas atrFncsActvCajasListOldAtrFncsActvCajas : atrFncsActvCajasListOld) {
                if (!atrFncsActvCajasListNew.contains(atrFncsActvCajasListOldAtrFncsActvCajas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AtrFncsActvCajas " + atrFncsActvCajasListOldAtrFncsActvCajas + " since its fkAtributosFuncionCajas field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<AtrFncsActvCajas> attachedAtrFncsActvCajasListNew = new ArrayList<AtrFncsActvCajas>();
            for (AtrFncsActvCajas atrFncsActvCajasListNewAtrFncsActvCajasToAttach : atrFncsActvCajasListNew) {
                atrFncsActvCajasListNewAtrFncsActvCajasToAttach = em.getReference(atrFncsActvCajasListNewAtrFncsActvCajasToAttach.getClass(), atrFncsActvCajasListNewAtrFncsActvCajasToAttach.getIdFuncionActivaCaja());
                attachedAtrFncsActvCajasListNew.add(atrFncsActvCajasListNewAtrFncsActvCajasToAttach);
            }
            atrFncsActvCajasListNew = attachedAtrFncsActvCajasListNew;
            atributosFuncionCajas.setAtrFncsActvCajasList(atrFncsActvCajasListNew);
            atributosFuncionCajas = em.merge(atributosFuncionCajas);
            for (AtrFncsActvCajas atrFncsActvCajasListNewAtrFncsActvCajas : atrFncsActvCajasListNew) {
                if (!atrFncsActvCajasListOld.contains(atrFncsActvCajasListNewAtrFncsActvCajas)) {
                    AtributosFuncionCajas oldFkAtributosFuncionCajasOfAtrFncsActvCajasListNewAtrFncsActvCajas = atrFncsActvCajasListNewAtrFncsActvCajas.getFkAtributosFuncionCajas();
                    atrFncsActvCajasListNewAtrFncsActvCajas.setFkAtributosFuncionCajas(atributosFuncionCajas);
                    atrFncsActvCajasListNewAtrFncsActvCajas = em.merge(atrFncsActvCajasListNewAtrFncsActvCajas);
                    if (oldFkAtributosFuncionCajasOfAtrFncsActvCajasListNewAtrFncsActvCajas != null && !oldFkAtributosFuncionCajasOfAtrFncsActvCajasListNewAtrFncsActvCajas.equals(atributosFuncionCajas)) {
                        oldFkAtributosFuncionCajasOfAtrFncsActvCajasListNewAtrFncsActvCajas.getAtrFncsActvCajasList().remove(atrFncsActvCajasListNewAtrFncsActvCajas);
                        oldFkAtributosFuncionCajasOfAtrFncsActvCajasListNewAtrFncsActvCajas = em.merge(oldFkAtributosFuncionCajasOfAtrFncsActvCajasListNewAtrFncsActvCajas);
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
            List<AtrFncsActvCajas> atrFncsActvCajasListOrphanCheck = atributosFuncionCajas.getAtrFncsActvCajasList();
            for (AtrFncsActvCajas atrFncsActvCajasListOrphanCheckAtrFncsActvCajas : atrFncsActvCajasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AtributosFuncionCajas (" + atributosFuncionCajas + ") cannot be destroyed since the AtrFncsActvCajas " + atrFncsActvCajasListOrphanCheckAtrFncsActvCajas + " in its atrFncsActvCajasList field has a non-nullable fkAtributosFuncionCajas field.");
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
