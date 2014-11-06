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
import emcali.ami.persistence.entity.AtributosFabricantes;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import emcali.ami.persistence.entity.AtributosTiposCajas;
import java.util.ArrayList;
import java.util.Collection;
import emcali.ami.persistence.entity.AtributosTiposMedidores;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class AtributosFabricantesJpaController implements Serializable {

    public AtributosFabricantesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AtributosFabricantes atributosFabricantes) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (atributosFabricantes.getAtributosTiposCajasCollection() == null) {
            atributosFabricantes.setAtributosTiposCajasCollection(new ArrayList<AtributosTiposCajas>());
        }
        if (atributosFabricantes.getAtributosTiposMedidoresCollection() == null) {
            atributosFabricantes.setAtributosTiposMedidoresCollection(new ArrayList<AtributosTiposMedidores>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<AtributosTiposCajas> attachedAtributosTiposCajasCollection = new ArrayList<AtributosTiposCajas>();
            for (AtributosTiposCajas atributosTiposCajasCollectionAtributosTiposCajasToAttach : atributosFabricantes.getAtributosTiposCajasCollection()) {
                atributosTiposCajasCollectionAtributosTiposCajasToAttach = em.getReference(atributosTiposCajasCollectionAtributosTiposCajasToAttach.getClass(), atributosTiposCajasCollectionAtributosTiposCajasToAttach.getIdTiposCajas());
                attachedAtributosTiposCajasCollection.add(atributosTiposCajasCollectionAtributosTiposCajasToAttach);
            }
            atributosFabricantes.setAtributosTiposCajasCollection(attachedAtributosTiposCajasCollection);
            Collection<AtributosTiposMedidores> attachedAtributosTiposMedidoresCollection = new ArrayList<AtributosTiposMedidores>();
            for (AtributosTiposMedidores atributosTiposMedidoresCollectionAtributosTiposMedidoresToAttach : atributosFabricantes.getAtributosTiposMedidoresCollection()) {
                atributosTiposMedidoresCollectionAtributosTiposMedidoresToAttach = em.getReference(atributosTiposMedidoresCollectionAtributosTiposMedidoresToAttach.getClass(), atributosTiposMedidoresCollectionAtributosTiposMedidoresToAttach.getIdTiposMedidores());
                attachedAtributosTiposMedidoresCollection.add(atributosTiposMedidoresCollectionAtributosTiposMedidoresToAttach);
            }
            atributosFabricantes.setAtributosTiposMedidoresCollection(attachedAtributosTiposMedidoresCollection);
            em.persist(atributosFabricantes);
            for (AtributosTiposCajas atributosTiposCajasCollectionAtributosTiposCajas : atributosFabricantes.getAtributosTiposCajasCollection()) {
                AtributosFabricantes oldFkAtributosFabricantesOfAtributosTiposCajasCollectionAtributosTiposCajas = atributosTiposCajasCollectionAtributosTiposCajas.getFkAtributosFabricantes();
                atributosTiposCajasCollectionAtributosTiposCajas.setFkAtributosFabricantes(atributosFabricantes);
                atributosTiposCajasCollectionAtributosTiposCajas = em.merge(atributosTiposCajasCollectionAtributosTiposCajas);
                if (oldFkAtributosFabricantesOfAtributosTiposCajasCollectionAtributosTiposCajas != null) {
                    oldFkAtributosFabricantesOfAtributosTiposCajasCollectionAtributosTiposCajas.getAtributosTiposCajasCollection().remove(atributosTiposCajasCollectionAtributosTiposCajas);
                    oldFkAtributosFabricantesOfAtributosTiposCajasCollectionAtributosTiposCajas = em.merge(oldFkAtributosFabricantesOfAtributosTiposCajasCollectionAtributosTiposCajas);
                }
            }
            for (AtributosTiposMedidores atributosTiposMedidoresCollectionAtributosTiposMedidores : atributosFabricantes.getAtributosTiposMedidoresCollection()) {
                AtributosFabricantes oldFkAtributosFabricantesOfAtributosTiposMedidoresCollectionAtributosTiposMedidores = atributosTiposMedidoresCollectionAtributosTiposMedidores.getFkAtributosFabricantes();
                atributosTiposMedidoresCollectionAtributosTiposMedidores.setFkAtributosFabricantes(atributosFabricantes);
                atributosTiposMedidoresCollectionAtributosTiposMedidores = em.merge(atributosTiposMedidoresCollectionAtributosTiposMedidores);
                if (oldFkAtributosFabricantesOfAtributosTiposMedidoresCollectionAtributosTiposMedidores != null) {
                    oldFkAtributosFabricantesOfAtributosTiposMedidoresCollectionAtributosTiposMedidores.getAtributosTiposMedidoresCollection().remove(atributosTiposMedidoresCollectionAtributosTiposMedidores);
                    oldFkAtributosFabricantesOfAtributosTiposMedidoresCollectionAtributosTiposMedidores = em.merge(oldFkAtributosFabricantesOfAtributosTiposMedidoresCollectionAtributosTiposMedidores);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAtributosFabricantes(atributosFabricantes.getIdFabricantes()) != null) {
                throw new PreexistingEntityException("AtributosFabricantes " + atributosFabricantes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AtributosFabricantes atributosFabricantes) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AtributosFabricantes persistentAtributosFabricantes = em.find(AtributosFabricantes.class, atributosFabricantes.getIdFabricantes());
            Collection<AtributosTiposCajas> atributosTiposCajasCollectionOld = persistentAtributosFabricantes.getAtributosTiposCajasCollection();
            Collection<AtributosTiposCajas> atributosTiposCajasCollectionNew = atributosFabricantes.getAtributosTiposCajasCollection();
            Collection<AtributosTiposMedidores> atributosTiposMedidoresCollectionOld = persistentAtributosFabricantes.getAtributosTiposMedidoresCollection();
            Collection<AtributosTiposMedidores> atributosTiposMedidoresCollectionNew = atributosFabricantes.getAtributosTiposMedidoresCollection();
            List<String> illegalOrphanMessages = null;
            for (AtributosTiposCajas atributosTiposCajasCollectionOldAtributosTiposCajas : atributosTiposCajasCollectionOld) {
                if (!atributosTiposCajasCollectionNew.contains(atributosTiposCajasCollectionOldAtributosTiposCajas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AtributosTiposCajas " + atributosTiposCajasCollectionOldAtributosTiposCajas + " since its fkAtributosFabricantes field is not nullable.");
                }
            }
            for (AtributosTiposMedidores atributosTiposMedidoresCollectionOldAtributosTiposMedidores : atributosTiposMedidoresCollectionOld) {
                if (!atributosTiposMedidoresCollectionNew.contains(atributosTiposMedidoresCollectionOldAtributosTiposMedidores)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AtributosTiposMedidores " + atributosTiposMedidoresCollectionOldAtributosTiposMedidores + " since its fkAtributosFabricantes field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<AtributosTiposCajas> attachedAtributosTiposCajasCollectionNew = new ArrayList<AtributosTiposCajas>();
            for (AtributosTiposCajas atributosTiposCajasCollectionNewAtributosTiposCajasToAttach : atributosTiposCajasCollectionNew) {
                atributosTiposCajasCollectionNewAtributosTiposCajasToAttach = em.getReference(atributosTiposCajasCollectionNewAtributosTiposCajasToAttach.getClass(), atributosTiposCajasCollectionNewAtributosTiposCajasToAttach.getIdTiposCajas());
                attachedAtributosTiposCajasCollectionNew.add(atributosTiposCajasCollectionNewAtributosTiposCajasToAttach);
            }
            atributosTiposCajasCollectionNew = attachedAtributosTiposCajasCollectionNew;
            atributosFabricantes.setAtributosTiposCajasCollection(atributosTiposCajasCollectionNew);
            Collection<AtributosTiposMedidores> attachedAtributosTiposMedidoresCollectionNew = new ArrayList<AtributosTiposMedidores>();
            for (AtributosTiposMedidores atributosTiposMedidoresCollectionNewAtributosTiposMedidoresToAttach : atributosTiposMedidoresCollectionNew) {
                atributosTiposMedidoresCollectionNewAtributosTiposMedidoresToAttach = em.getReference(atributosTiposMedidoresCollectionNewAtributosTiposMedidoresToAttach.getClass(), atributosTiposMedidoresCollectionNewAtributosTiposMedidoresToAttach.getIdTiposMedidores());
                attachedAtributosTiposMedidoresCollectionNew.add(atributosTiposMedidoresCollectionNewAtributosTiposMedidoresToAttach);
            }
            atributosTiposMedidoresCollectionNew = attachedAtributosTiposMedidoresCollectionNew;
            atributosFabricantes.setAtributosTiposMedidoresCollection(atributosTiposMedidoresCollectionNew);
            atributosFabricantes = em.merge(atributosFabricantes);
            for (AtributosTiposCajas atributosTiposCajasCollectionNewAtributosTiposCajas : atributosTiposCajasCollectionNew) {
                if (!atributosTiposCajasCollectionOld.contains(atributosTiposCajasCollectionNewAtributosTiposCajas)) {
                    AtributosFabricantes oldFkAtributosFabricantesOfAtributosTiposCajasCollectionNewAtributosTiposCajas = atributosTiposCajasCollectionNewAtributosTiposCajas.getFkAtributosFabricantes();
                    atributosTiposCajasCollectionNewAtributosTiposCajas.setFkAtributosFabricantes(atributosFabricantes);
                    atributosTiposCajasCollectionNewAtributosTiposCajas = em.merge(atributosTiposCajasCollectionNewAtributosTiposCajas);
                    if (oldFkAtributosFabricantesOfAtributosTiposCajasCollectionNewAtributosTiposCajas != null && !oldFkAtributosFabricantesOfAtributosTiposCajasCollectionNewAtributosTiposCajas.equals(atributosFabricantes)) {
                        oldFkAtributosFabricantesOfAtributosTiposCajasCollectionNewAtributosTiposCajas.getAtributosTiposCajasCollection().remove(atributosTiposCajasCollectionNewAtributosTiposCajas);
                        oldFkAtributosFabricantesOfAtributosTiposCajasCollectionNewAtributosTiposCajas = em.merge(oldFkAtributosFabricantesOfAtributosTiposCajasCollectionNewAtributosTiposCajas);
                    }
                }
            }
            for (AtributosTiposMedidores atributosTiposMedidoresCollectionNewAtributosTiposMedidores : atributosTiposMedidoresCollectionNew) {
                if (!atributosTiposMedidoresCollectionOld.contains(atributosTiposMedidoresCollectionNewAtributosTiposMedidores)) {
                    AtributosFabricantes oldFkAtributosFabricantesOfAtributosTiposMedidoresCollectionNewAtributosTiposMedidores = atributosTiposMedidoresCollectionNewAtributosTiposMedidores.getFkAtributosFabricantes();
                    atributosTiposMedidoresCollectionNewAtributosTiposMedidores.setFkAtributosFabricantes(atributosFabricantes);
                    atributosTiposMedidoresCollectionNewAtributosTiposMedidores = em.merge(atributosTiposMedidoresCollectionNewAtributosTiposMedidores);
                    if (oldFkAtributosFabricantesOfAtributosTiposMedidoresCollectionNewAtributosTiposMedidores != null && !oldFkAtributosFabricantesOfAtributosTiposMedidoresCollectionNewAtributosTiposMedidores.equals(atributosFabricantes)) {
                        oldFkAtributosFabricantesOfAtributosTiposMedidoresCollectionNewAtributosTiposMedidores.getAtributosTiposMedidoresCollection().remove(atributosTiposMedidoresCollectionNewAtributosTiposMedidores);
                        oldFkAtributosFabricantesOfAtributosTiposMedidoresCollectionNewAtributosTiposMedidores = em.merge(oldFkAtributosFabricantesOfAtributosTiposMedidoresCollectionNewAtributosTiposMedidores);
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
                Long id = atributosFabricantes.getIdFabricantes();
                if (findAtributosFabricantes(id) == null) {
                    throw new NonexistentEntityException("The atributosFabricantes with id " + id + " no longer exists.");
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
            AtributosFabricantes atributosFabricantes;
            try {
                atributosFabricantes = em.getReference(AtributosFabricantes.class, id);
                atributosFabricantes.getIdFabricantes();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The atributosFabricantes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<AtributosTiposCajas> atributosTiposCajasCollectionOrphanCheck = atributosFabricantes.getAtributosTiposCajasCollection();
            for (AtributosTiposCajas atributosTiposCajasCollectionOrphanCheckAtributosTiposCajas : atributosTiposCajasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AtributosFabricantes (" + atributosFabricantes + ") cannot be destroyed since the AtributosTiposCajas " + atributosTiposCajasCollectionOrphanCheckAtributosTiposCajas + " in its atributosTiposCajasCollection field has a non-nullable fkAtributosFabricantes field.");
            }
            Collection<AtributosTiposMedidores> atributosTiposMedidoresCollectionOrphanCheck = atributosFabricantes.getAtributosTiposMedidoresCollection();
            for (AtributosTiposMedidores atributosTiposMedidoresCollectionOrphanCheckAtributosTiposMedidores : atributosTiposMedidoresCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AtributosFabricantes (" + atributosFabricantes + ") cannot be destroyed since the AtributosTiposMedidores " + atributosTiposMedidoresCollectionOrphanCheckAtributosTiposMedidores + " in its atributosTiposMedidoresCollection field has a non-nullable fkAtributosFabricantes field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(atributosFabricantes);
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

    public List<AtributosFabricantes> findAtributosFabricantesEntities() {
        return findAtributosFabricantesEntities(true, -1, -1);
    }

    public List<AtributosFabricantes> findAtributosFabricantesEntities(int maxResults, int firstResult) {
        return findAtributosFabricantesEntities(false, maxResults, firstResult);
    }

    private List<AtributosFabricantes> findAtributosFabricantesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AtributosFabricantes.class));
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

    public AtributosFabricantes findAtributosFabricantes(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AtributosFabricantes.class, id);
        } finally {
            em.close();
        }
    }

    public int getAtributosFabricantesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AtributosFabricantes> rt = cq.from(AtributosFabricantes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
