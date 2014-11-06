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
import emcali.ami.persistence.entity.AtributosFabricantes;
import emcali.ami.persistence.entity.AmyCajas;
import emcali.ami.persistence.entity.AtributosTiposCajas;
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
public class AtributosTiposCajasJpaController implements Serializable {

    public AtributosTiposCajasJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AtributosTiposCajas atributosTiposCajas) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (atributosTiposCajas.getAmyCajasCollection() == null) {
            atributosTiposCajas.setAmyCajasCollection(new ArrayList<AmyCajas>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AtributosFabricantes fkAtributosFabricantes = atributosTiposCajas.getFkAtributosFabricantes();
            if (fkAtributosFabricantes != null) {
                fkAtributosFabricantes = em.getReference(fkAtributosFabricantes.getClass(), fkAtributosFabricantes.getIdFabricantes());
                atributosTiposCajas.setFkAtributosFabricantes(fkAtributosFabricantes);
            }
            Collection<AmyCajas> attachedAmyCajasCollection = new ArrayList<AmyCajas>();
            for (AmyCajas amyCajasCollectionAmyCajasToAttach : atributosTiposCajas.getAmyCajasCollection()) {
                amyCajasCollectionAmyCajasToAttach = em.getReference(amyCajasCollectionAmyCajasToAttach.getClass(), amyCajasCollectionAmyCajasToAttach.getIdCajas());
                attachedAmyCajasCollection.add(amyCajasCollectionAmyCajasToAttach);
            }
            atributosTiposCajas.setAmyCajasCollection(attachedAmyCajasCollection);
            em.persist(atributosTiposCajas);
            if (fkAtributosFabricantes != null) {
                fkAtributosFabricantes.getAtributosTiposCajasCollection().add(atributosTiposCajas);
                fkAtributosFabricantes = em.merge(fkAtributosFabricantes);
            }
            for (AmyCajas amyCajasCollectionAmyCajas : atributosTiposCajas.getAmyCajasCollection()) {
                AtributosTiposCajas oldFkTiposCajasOfAmyCajasCollectionAmyCajas = amyCajasCollectionAmyCajas.getFkTiposCajas();
                amyCajasCollectionAmyCajas.setFkTiposCajas(atributosTiposCajas);
                amyCajasCollectionAmyCajas = em.merge(amyCajasCollectionAmyCajas);
                if (oldFkTiposCajasOfAmyCajasCollectionAmyCajas != null) {
                    oldFkTiposCajasOfAmyCajasCollectionAmyCajas.getAmyCajasCollection().remove(amyCajasCollectionAmyCajas);
                    oldFkTiposCajasOfAmyCajasCollectionAmyCajas = em.merge(oldFkTiposCajasOfAmyCajasCollectionAmyCajas);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAtributosTiposCajas(atributosTiposCajas.getIdTiposCajas()) != null) {
                throw new PreexistingEntityException("AtributosTiposCajas " + atributosTiposCajas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AtributosTiposCajas atributosTiposCajas) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AtributosTiposCajas persistentAtributosTiposCajas = em.find(AtributosTiposCajas.class, atributosTiposCajas.getIdTiposCajas());
            AtributosFabricantes fkAtributosFabricantesOld = persistentAtributosTiposCajas.getFkAtributosFabricantes();
            AtributosFabricantes fkAtributosFabricantesNew = atributosTiposCajas.getFkAtributosFabricantes();
            Collection<AmyCajas> amyCajasCollectionOld = persistentAtributosTiposCajas.getAmyCajasCollection();
            Collection<AmyCajas> amyCajasCollectionNew = atributosTiposCajas.getAmyCajasCollection();
            List<String> illegalOrphanMessages = null;
            for (AmyCajas amyCajasCollectionOldAmyCajas : amyCajasCollectionOld) {
                if (!amyCajasCollectionNew.contains(amyCajasCollectionOldAmyCajas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyCajas " + amyCajasCollectionOldAmyCajas + " since its fkTiposCajas field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (fkAtributosFabricantesNew != null) {
                fkAtributosFabricantesNew = em.getReference(fkAtributosFabricantesNew.getClass(), fkAtributosFabricantesNew.getIdFabricantes());
                atributosTiposCajas.setFkAtributosFabricantes(fkAtributosFabricantesNew);
            }
            Collection<AmyCajas> attachedAmyCajasCollectionNew = new ArrayList<AmyCajas>();
            for (AmyCajas amyCajasCollectionNewAmyCajasToAttach : amyCajasCollectionNew) {
                amyCajasCollectionNewAmyCajasToAttach = em.getReference(amyCajasCollectionNewAmyCajasToAttach.getClass(), amyCajasCollectionNewAmyCajasToAttach.getIdCajas());
                attachedAmyCajasCollectionNew.add(amyCajasCollectionNewAmyCajasToAttach);
            }
            amyCajasCollectionNew = attachedAmyCajasCollectionNew;
            atributosTiposCajas.setAmyCajasCollection(amyCajasCollectionNew);
            atributosTiposCajas = em.merge(atributosTiposCajas);
            if (fkAtributosFabricantesOld != null && !fkAtributosFabricantesOld.equals(fkAtributosFabricantesNew)) {
                fkAtributosFabricantesOld.getAtributosTiposCajasCollection().remove(atributosTiposCajas);
                fkAtributosFabricantesOld = em.merge(fkAtributosFabricantesOld);
            }
            if (fkAtributosFabricantesNew != null && !fkAtributosFabricantesNew.equals(fkAtributosFabricantesOld)) {
                fkAtributosFabricantesNew.getAtributosTiposCajasCollection().add(atributosTiposCajas);
                fkAtributosFabricantesNew = em.merge(fkAtributosFabricantesNew);
            }
            for (AmyCajas amyCajasCollectionNewAmyCajas : amyCajasCollectionNew) {
                if (!amyCajasCollectionOld.contains(amyCajasCollectionNewAmyCajas)) {
                    AtributosTiposCajas oldFkTiposCajasOfAmyCajasCollectionNewAmyCajas = amyCajasCollectionNewAmyCajas.getFkTiposCajas();
                    amyCajasCollectionNewAmyCajas.setFkTiposCajas(atributosTiposCajas);
                    amyCajasCollectionNewAmyCajas = em.merge(amyCajasCollectionNewAmyCajas);
                    if (oldFkTiposCajasOfAmyCajasCollectionNewAmyCajas != null && !oldFkTiposCajasOfAmyCajasCollectionNewAmyCajas.equals(atributosTiposCajas)) {
                        oldFkTiposCajasOfAmyCajasCollectionNewAmyCajas.getAmyCajasCollection().remove(amyCajasCollectionNewAmyCajas);
                        oldFkTiposCajasOfAmyCajasCollectionNewAmyCajas = em.merge(oldFkTiposCajasOfAmyCajasCollectionNewAmyCajas);
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
                Long id = atributosTiposCajas.getIdTiposCajas();
                if (findAtributosTiposCajas(id) == null) {
                    throw new NonexistentEntityException("The atributosTiposCajas with id " + id + " no longer exists.");
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
            AtributosTiposCajas atributosTiposCajas;
            try {
                atributosTiposCajas = em.getReference(AtributosTiposCajas.class, id);
                atributosTiposCajas.getIdTiposCajas();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The atributosTiposCajas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<AmyCajas> amyCajasCollectionOrphanCheck = atributosTiposCajas.getAmyCajasCollection();
            for (AmyCajas amyCajasCollectionOrphanCheckAmyCajas : amyCajasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AtributosTiposCajas (" + atributosTiposCajas + ") cannot be destroyed since the AmyCajas " + amyCajasCollectionOrphanCheckAmyCajas + " in its amyCajasCollection field has a non-nullable fkTiposCajas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            AtributosFabricantes fkAtributosFabricantes = atributosTiposCajas.getFkAtributosFabricantes();
            if (fkAtributosFabricantes != null) {
                fkAtributosFabricantes.getAtributosTiposCajasCollection().remove(atributosTiposCajas);
                fkAtributosFabricantes = em.merge(fkAtributosFabricantes);
            }
            em.remove(atributosTiposCajas);
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

    public List<AtributosTiposCajas> findAtributosTiposCajasEntities() {
        return findAtributosTiposCajasEntities(true, -1, -1);
    }

    public List<AtributosTiposCajas> findAtributosTiposCajasEntities(int maxResults, int firstResult) {
        return findAtributosTiposCajasEntities(false, maxResults, firstResult);
    }

    private List<AtributosTiposCajas> findAtributosTiposCajasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AtributosTiposCajas.class));
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

    public AtributosTiposCajas findAtributosTiposCajas(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AtributosTiposCajas.class, id);
        } finally {
            em.close();
        }
    }

    public int getAtributosTiposCajasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AtributosTiposCajas> rt = cq.from(AtributosTiposCajas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
