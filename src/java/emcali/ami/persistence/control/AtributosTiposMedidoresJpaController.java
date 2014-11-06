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
import emcali.ami.persistence.entity.AmyMedidores;
import emcali.ami.persistence.entity.AtributosTiposMedidores;
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
public class AtributosTiposMedidoresJpaController implements Serializable {

    public AtributosTiposMedidoresJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AtributosTiposMedidores atributosTiposMedidores) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (atributosTiposMedidores.getAmyMedidoresCollection() == null) {
            atributosTiposMedidores.setAmyMedidoresCollection(new ArrayList<AmyMedidores>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AtributosFabricantes fkAtributosFabricantes = atributosTiposMedidores.getFkAtributosFabricantes();
            if (fkAtributosFabricantes != null) {
                fkAtributosFabricantes = em.getReference(fkAtributosFabricantes.getClass(), fkAtributosFabricantes.getIdFabricantes());
                atributosTiposMedidores.setFkAtributosFabricantes(fkAtributosFabricantes);
            }
            Collection<AmyMedidores> attachedAmyMedidoresCollection = new ArrayList<AmyMedidores>();
            for (AmyMedidores amyMedidoresCollectionAmyMedidoresToAttach : atributosTiposMedidores.getAmyMedidoresCollection()) {
                amyMedidoresCollectionAmyMedidoresToAttach = em.getReference(amyMedidoresCollectionAmyMedidoresToAttach.getClass(), amyMedidoresCollectionAmyMedidoresToAttach.getIdMedidores());
                attachedAmyMedidoresCollection.add(amyMedidoresCollectionAmyMedidoresToAttach);
            }
            atributosTiposMedidores.setAmyMedidoresCollection(attachedAmyMedidoresCollection);
            em.persist(atributosTiposMedidores);
            if (fkAtributosFabricantes != null) {
                fkAtributosFabricantes.getAtributosTiposMedidoresCollection().add(atributosTiposMedidores);
                fkAtributosFabricantes = em.merge(fkAtributosFabricantes);
            }
            for (AmyMedidores amyMedidoresCollectionAmyMedidores : atributosTiposMedidores.getAmyMedidoresCollection()) {
                AtributosTiposMedidores oldFkAtributosTiposMedidoresOfAmyMedidoresCollectionAmyMedidores = amyMedidoresCollectionAmyMedidores.getFkAtributosTiposMedidores();
                amyMedidoresCollectionAmyMedidores.setFkAtributosTiposMedidores(atributosTiposMedidores);
                amyMedidoresCollectionAmyMedidores = em.merge(amyMedidoresCollectionAmyMedidores);
                if (oldFkAtributosTiposMedidoresOfAmyMedidoresCollectionAmyMedidores != null) {
                    oldFkAtributosTiposMedidoresOfAmyMedidoresCollectionAmyMedidores.getAmyMedidoresCollection().remove(amyMedidoresCollectionAmyMedidores);
                    oldFkAtributosTiposMedidoresOfAmyMedidoresCollectionAmyMedidores = em.merge(oldFkAtributosTiposMedidoresOfAmyMedidoresCollectionAmyMedidores);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAtributosTiposMedidores(atributosTiposMedidores.getIdTiposMedidores()) != null) {
                throw new PreexistingEntityException("AtributosTiposMedidores " + atributosTiposMedidores + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AtributosTiposMedidores atributosTiposMedidores) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AtributosTiposMedidores persistentAtributosTiposMedidores = em.find(AtributosTiposMedidores.class, atributosTiposMedidores.getIdTiposMedidores());
            AtributosFabricantes fkAtributosFabricantesOld = persistentAtributosTiposMedidores.getFkAtributosFabricantes();
            AtributosFabricantes fkAtributosFabricantesNew = atributosTiposMedidores.getFkAtributosFabricantes();
            Collection<AmyMedidores> amyMedidoresCollectionOld = persistentAtributosTiposMedidores.getAmyMedidoresCollection();
            Collection<AmyMedidores> amyMedidoresCollectionNew = atributosTiposMedidores.getAmyMedidoresCollection();
            List<String> illegalOrphanMessages = null;
            for (AmyMedidores amyMedidoresCollectionOldAmyMedidores : amyMedidoresCollectionOld) {
                if (!amyMedidoresCollectionNew.contains(amyMedidoresCollectionOldAmyMedidores)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyMedidores " + amyMedidoresCollectionOldAmyMedidores + " since its fkAtributosTiposMedidores field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (fkAtributosFabricantesNew != null) {
                fkAtributosFabricantesNew = em.getReference(fkAtributosFabricantesNew.getClass(), fkAtributosFabricantesNew.getIdFabricantes());
                atributosTiposMedidores.setFkAtributosFabricantes(fkAtributosFabricantesNew);
            }
            Collection<AmyMedidores> attachedAmyMedidoresCollectionNew = new ArrayList<AmyMedidores>();
            for (AmyMedidores amyMedidoresCollectionNewAmyMedidoresToAttach : amyMedidoresCollectionNew) {
                amyMedidoresCollectionNewAmyMedidoresToAttach = em.getReference(amyMedidoresCollectionNewAmyMedidoresToAttach.getClass(), amyMedidoresCollectionNewAmyMedidoresToAttach.getIdMedidores());
                attachedAmyMedidoresCollectionNew.add(amyMedidoresCollectionNewAmyMedidoresToAttach);
            }
            amyMedidoresCollectionNew = attachedAmyMedidoresCollectionNew;
            atributosTiposMedidores.setAmyMedidoresCollection(amyMedidoresCollectionNew);
            atributosTiposMedidores = em.merge(atributosTiposMedidores);
            if (fkAtributosFabricantesOld != null && !fkAtributosFabricantesOld.equals(fkAtributosFabricantesNew)) {
                fkAtributosFabricantesOld.getAtributosTiposMedidoresCollection().remove(atributosTiposMedidores);
                fkAtributosFabricantesOld = em.merge(fkAtributosFabricantesOld);
            }
            if (fkAtributosFabricantesNew != null && !fkAtributosFabricantesNew.equals(fkAtributosFabricantesOld)) {
                fkAtributosFabricantesNew.getAtributosTiposMedidoresCollection().add(atributosTiposMedidores);
                fkAtributosFabricantesNew = em.merge(fkAtributosFabricantesNew);
            }
            for (AmyMedidores amyMedidoresCollectionNewAmyMedidores : amyMedidoresCollectionNew) {
                if (!amyMedidoresCollectionOld.contains(amyMedidoresCollectionNewAmyMedidores)) {
                    AtributosTiposMedidores oldFkAtributosTiposMedidoresOfAmyMedidoresCollectionNewAmyMedidores = amyMedidoresCollectionNewAmyMedidores.getFkAtributosTiposMedidores();
                    amyMedidoresCollectionNewAmyMedidores.setFkAtributosTiposMedidores(atributosTiposMedidores);
                    amyMedidoresCollectionNewAmyMedidores = em.merge(amyMedidoresCollectionNewAmyMedidores);
                    if (oldFkAtributosTiposMedidoresOfAmyMedidoresCollectionNewAmyMedidores != null && !oldFkAtributosTiposMedidoresOfAmyMedidoresCollectionNewAmyMedidores.equals(atributosTiposMedidores)) {
                        oldFkAtributosTiposMedidoresOfAmyMedidoresCollectionNewAmyMedidores.getAmyMedidoresCollection().remove(amyMedidoresCollectionNewAmyMedidores);
                        oldFkAtributosTiposMedidoresOfAmyMedidoresCollectionNewAmyMedidores = em.merge(oldFkAtributosTiposMedidoresOfAmyMedidoresCollectionNewAmyMedidores);
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
                Long id = atributosTiposMedidores.getIdTiposMedidores();
                if (findAtributosTiposMedidores(id) == null) {
                    throw new NonexistentEntityException("The atributosTiposMedidores with id " + id + " no longer exists.");
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
            AtributosTiposMedidores atributosTiposMedidores;
            try {
                atributosTiposMedidores = em.getReference(AtributosTiposMedidores.class, id);
                atributosTiposMedidores.getIdTiposMedidores();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The atributosTiposMedidores with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<AmyMedidores> amyMedidoresCollectionOrphanCheck = atributosTiposMedidores.getAmyMedidoresCollection();
            for (AmyMedidores amyMedidoresCollectionOrphanCheckAmyMedidores : amyMedidoresCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AtributosTiposMedidores (" + atributosTiposMedidores + ") cannot be destroyed since the AmyMedidores " + amyMedidoresCollectionOrphanCheckAmyMedidores + " in its amyMedidoresCollection field has a non-nullable fkAtributosTiposMedidores field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            AtributosFabricantes fkAtributosFabricantes = atributosTiposMedidores.getFkAtributosFabricantes();
            if (fkAtributosFabricantes != null) {
                fkAtributosFabricantes.getAtributosTiposMedidoresCollection().remove(atributosTiposMedidores);
                fkAtributosFabricantes = em.merge(fkAtributosFabricantes);
            }
            em.remove(atributosTiposMedidores);
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

    public List<AtributosTiposMedidores> findAtributosTiposMedidoresEntities() {
        return findAtributosTiposMedidoresEntities(true, -1, -1);
    }

    public List<AtributosTiposMedidores> findAtributosTiposMedidoresEntities(int maxResults, int firstResult) {
        return findAtributosTiposMedidoresEntities(false, maxResults, firstResult);
    }

    private List<AtributosTiposMedidores> findAtributosTiposMedidoresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AtributosTiposMedidores.class));
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

    public AtributosTiposMedidores findAtributosTiposMedidores(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AtributosTiposMedidores.class, id);
        } finally {
            em.close();
        }
    }

    public int getAtributosTiposMedidoresCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AtributosTiposMedidores> rt = cq.from(AtributosTiposMedidores.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
