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
import emcali.ami.persistence.entity.AmyCajas;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import emcali.ami.persistence.entity.EnergiaTransformadores;
import emcali.ami.persistence.entity.AtributosTiposCajas;
import emcali.ami.persistence.entity.AtrFncsActvCajas;
import java.util.ArrayList;
import java.util.Collection;
import emcali.ami.persistence.entity.AmyMedidores;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class AmyCajasJpaController implements Serializable {

    public AmyCajasJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AmyCajas amyCajas) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (amyCajas.getAtrFncsActvCajasCollection() == null) {
            amyCajas.setAtrFncsActvCajasCollection(new ArrayList<AtrFncsActvCajas>());
        }
        if (amyCajas.getAmyMedidoresCollection() == null) {
            amyCajas.setAmyMedidoresCollection(new ArrayList<AmyMedidores>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            EnergiaTransformadores fkEnergiaTransformadores = amyCajas.getFkEnergiaTransformadores();
            if (fkEnergiaTransformadores != null) {
                fkEnergiaTransformadores = em.getReference(fkEnergiaTransformadores.getClass(), fkEnergiaTransformadores.getIdTransformadores());
                amyCajas.setFkEnergiaTransformadores(fkEnergiaTransformadores);
            }
            AtributosTiposCajas fkTiposCajas = amyCajas.getFkTiposCajas();
            if (fkTiposCajas != null) {
                fkTiposCajas = em.getReference(fkTiposCajas.getClass(), fkTiposCajas.getIdTiposCajas());
                amyCajas.setFkTiposCajas(fkTiposCajas);
            }
            Collection<AtrFncsActvCajas> attachedAtrFncsActvCajasCollection = new ArrayList<AtrFncsActvCajas>();
            for (AtrFncsActvCajas atrFncsActvCajasCollectionAtrFncsActvCajasToAttach : amyCajas.getAtrFncsActvCajasCollection()) {
                atrFncsActvCajasCollectionAtrFncsActvCajasToAttach = em.getReference(atrFncsActvCajasCollectionAtrFncsActvCajasToAttach.getClass(), atrFncsActvCajasCollectionAtrFncsActvCajasToAttach.getIdFuncionActivaCaja());
                attachedAtrFncsActvCajasCollection.add(atrFncsActvCajasCollectionAtrFncsActvCajasToAttach);
            }
            amyCajas.setAtrFncsActvCajasCollection(attachedAtrFncsActvCajasCollection);
            Collection<AmyMedidores> attachedAmyMedidoresCollection = new ArrayList<AmyMedidores>();
            for (AmyMedidores amyMedidoresCollectionAmyMedidoresToAttach : amyCajas.getAmyMedidoresCollection()) {
                amyMedidoresCollectionAmyMedidoresToAttach = em.getReference(amyMedidoresCollectionAmyMedidoresToAttach.getClass(), amyMedidoresCollectionAmyMedidoresToAttach.getIdMedidores());
                attachedAmyMedidoresCollection.add(amyMedidoresCollectionAmyMedidoresToAttach);
            }
            amyCajas.setAmyMedidoresCollection(attachedAmyMedidoresCollection);
            em.persist(amyCajas);
            if (fkEnergiaTransformadores != null) {
                fkEnergiaTransformadores.getAmyCajasCollection().add(amyCajas);
                fkEnergiaTransformadores = em.merge(fkEnergiaTransformadores);
            }
            if (fkTiposCajas != null) {
                fkTiposCajas.getAmyCajasCollection().add(amyCajas);
                fkTiposCajas = em.merge(fkTiposCajas);
            }
            for (AtrFncsActvCajas atrFncsActvCajasCollectionAtrFncsActvCajas : amyCajas.getAtrFncsActvCajasCollection()) {
                AmyCajas oldFkAmyCajasOfAtrFncsActvCajasCollectionAtrFncsActvCajas = atrFncsActvCajasCollectionAtrFncsActvCajas.getFkAmyCajas();
                atrFncsActvCajasCollectionAtrFncsActvCajas.setFkAmyCajas(amyCajas);
                atrFncsActvCajasCollectionAtrFncsActvCajas = em.merge(atrFncsActvCajasCollectionAtrFncsActvCajas);
                if (oldFkAmyCajasOfAtrFncsActvCajasCollectionAtrFncsActvCajas != null) {
                    oldFkAmyCajasOfAtrFncsActvCajasCollectionAtrFncsActvCajas.getAtrFncsActvCajasCollection().remove(atrFncsActvCajasCollectionAtrFncsActvCajas);
                    oldFkAmyCajasOfAtrFncsActvCajasCollectionAtrFncsActvCajas = em.merge(oldFkAmyCajasOfAtrFncsActvCajasCollectionAtrFncsActvCajas);
                }
            }
            for (AmyMedidores amyMedidoresCollectionAmyMedidores : amyCajas.getAmyMedidoresCollection()) {
                AmyCajas oldFkAmyCajasOfAmyMedidoresCollectionAmyMedidores = amyMedidoresCollectionAmyMedidores.getFkAmyCajas();
                amyMedidoresCollectionAmyMedidores.setFkAmyCajas(amyCajas);
                amyMedidoresCollectionAmyMedidores = em.merge(amyMedidoresCollectionAmyMedidores);
                if (oldFkAmyCajasOfAmyMedidoresCollectionAmyMedidores != null) {
                    oldFkAmyCajasOfAmyMedidoresCollectionAmyMedidores.getAmyMedidoresCollection().remove(amyMedidoresCollectionAmyMedidores);
                    oldFkAmyCajasOfAmyMedidoresCollectionAmyMedidores = em.merge(oldFkAmyCajasOfAmyMedidoresCollectionAmyMedidores);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAmyCajas(amyCajas.getIdCajas()) != null) {
                throw new PreexistingEntityException("AmyCajas " + amyCajas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AmyCajas amyCajas) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AmyCajas persistentAmyCajas = em.find(AmyCajas.class, amyCajas.getIdCajas());
            EnergiaTransformadores fkEnergiaTransformadoresOld = persistentAmyCajas.getFkEnergiaTransformadores();
            EnergiaTransformadores fkEnergiaTransformadoresNew = amyCajas.getFkEnergiaTransformadores();
            AtributosTiposCajas fkTiposCajasOld = persistentAmyCajas.getFkTiposCajas();
            AtributosTiposCajas fkTiposCajasNew = amyCajas.getFkTiposCajas();
            Collection<AtrFncsActvCajas> atrFncsActvCajasCollectionOld = persistentAmyCajas.getAtrFncsActvCajasCollection();
            Collection<AtrFncsActvCajas> atrFncsActvCajasCollectionNew = amyCajas.getAtrFncsActvCajasCollection();
            Collection<AmyMedidores> amyMedidoresCollectionOld = persistentAmyCajas.getAmyMedidoresCollection();
            Collection<AmyMedidores> amyMedidoresCollectionNew = amyCajas.getAmyMedidoresCollection();
            List<String> illegalOrphanMessages = null;
            for (AtrFncsActvCajas atrFncsActvCajasCollectionOldAtrFncsActvCajas : atrFncsActvCajasCollectionOld) {
                if (!atrFncsActvCajasCollectionNew.contains(atrFncsActvCajasCollectionOldAtrFncsActvCajas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AtrFncsActvCajas " + atrFncsActvCajasCollectionOldAtrFncsActvCajas + " since its fkAmyCajas field is not nullable.");
                }
            }
            for (AmyMedidores amyMedidoresCollectionOldAmyMedidores : amyMedidoresCollectionOld) {
                if (!amyMedidoresCollectionNew.contains(amyMedidoresCollectionOldAmyMedidores)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyMedidores " + amyMedidoresCollectionOldAmyMedidores + " since its fkAmyCajas field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (fkEnergiaTransformadoresNew != null) {
                fkEnergiaTransformadoresNew = em.getReference(fkEnergiaTransformadoresNew.getClass(), fkEnergiaTransformadoresNew.getIdTransformadores());
                amyCajas.setFkEnergiaTransformadores(fkEnergiaTransformadoresNew);
            }
            if (fkTiposCajasNew != null) {
                fkTiposCajasNew = em.getReference(fkTiposCajasNew.getClass(), fkTiposCajasNew.getIdTiposCajas());
                amyCajas.setFkTiposCajas(fkTiposCajasNew);
            }
            Collection<AtrFncsActvCajas> attachedAtrFncsActvCajasCollectionNew = new ArrayList<AtrFncsActvCajas>();
            for (AtrFncsActvCajas atrFncsActvCajasCollectionNewAtrFncsActvCajasToAttach : atrFncsActvCajasCollectionNew) {
                atrFncsActvCajasCollectionNewAtrFncsActvCajasToAttach = em.getReference(atrFncsActvCajasCollectionNewAtrFncsActvCajasToAttach.getClass(), atrFncsActvCajasCollectionNewAtrFncsActvCajasToAttach.getIdFuncionActivaCaja());
                attachedAtrFncsActvCajasCollectionNew.add(atrFncsActvCajasCollectionNewAtrFncsActvCajasToAttach);
            }
            atrFncsActvCajasCollectionNew = attachedAtrFncsActvCajasCollectionNew;
            amyCajas.setAtrFncsActvCajasCollection(atrFncsActvCajasCollectionNew);
            Collection<AmyMedidores> attachedAmyMedidoresCollectionNew = new ArrayList<AmyMedidores>();
            for (AmyMedidores amyMedidoresCollectionNewAmyMedidoresToAttach : amyMedidoresCollectionNew) {
                amyMedidoresCollectionNewAmyMedidoresToAttach = em.getReference(amyMedidoresCollectionNewAmyMedidoresToAttach.getClass(), amyMedidoresCollectionNewAmyMedidoresToAttach.getIdMedidores());
                attachedAmyMedidoresCollectionNew.add(amyMedidoresCollectionNewAmyMedidoresToAttach);
            }
            amyMedidoresCollectionNew = attachedAmyMedidoresCollectionNew;
            amyCajas.setAmyMedidoresCollection(amyMedidoresCollectionNew);
            amyCajas = em.merge(amyCajas);
            if (fkEnergiaTransformadoresOld != null && !fkEnergiaTransformadoresOld.equals(fkEnergiaTransformadoresNew)) {
                fkEnergiaTransformadoresOld.getAmyCajasCollection().remove(amyCajas);
                fkEnergiaTransformadoresOld = em.merge(fkEnergiaTransformadoresOld);
            }
            if (fkEnergiaTransformadoresNew != null && !fkEnergiaTransformadoresNew.equals(fkEnergiaTransformadoresOld)) {
                fkEnergiaTransformadoresNew.getAmyCajasCollection().add(amyCajas);
                fkEnergiaTransformadoresNew = em.merge(fkEnergiaTransformadoresNew);
            }
            if (fkTiposCajasOld != null && !fkTiposCajasOld.equals(fkTiposCajasNew)) {
                fkTiposCajasOld.getAmyCajasCollection().remove(amyCajas);
                fkTiposCajasOld = em.merge(fkTiposCajasOld);
            }
            if (fkTiposCajasNew != null && !fkTiposCajasNew.equals(fkTiposCajasOld)) {
                fkTiposCajasNew.getAmyCajasCollection().add(amyCajas);
                fkTiposCajasNew = em.merge(fkTiposCajasNew);
            }
            for (AtrFncsActvCajas atrFncsActvCajasCollectionNewAtrFncsActvCajas : atrFncsActvCajasCollectionNew) {
                if (!atrFncsActvCajasCollectionOld.contains(atrFncsActvCajasCollectionNewAtrFncsActvCajas)) {
                    AmyCajas oldFkAmyCajasOfAtrFncsActvCajasCollectionNewAtrFncsActvCajas = atrFncsActvCajasCollectionNewAtrFncsActvCajas.getFkAmyCajas();
                    atrFncsActvCajasCollectionNewAtrFncsActvCajas.setFkAmyCajas(amyCajas);
                    atrFncsActvCajasCollectionNewAtrFncsActvCajas = em.merge(atrFncsActvCajasCollectionNewAtrFncsActvCajas);
                    if (oldFkAmyCajasOfAtrFncsActvCajasCollectionNewAtrFncsActvCajas != null && !oldFkAmyCajasOfAtrFncsActvCajasCollectionNewAtrFncsActvCajas.equals(amyCajas)) {
                        oldFkAmyCajasOfAtrFncsActvCajasCollectionNewAtrFncsActvCajas.getAtrFncsActvCajasCollection().remove(atrFncsActvCajasCollectionNewAtrFncsActvCajas);
                        oldFkAmyCajasOfAtrFncsActvCajasCollectionNewAtrFncsActvCajas = em.merge(oldFkAmyCajasOfAtrFncsActvCajasCollectionNewAtrFncsActvCajas);
                    }
                }
            }
            for (AmyMedidores amyMedidoresCollectionNewAmyMedidores : amyMedidoresCollectionNew) {
                if (!amyMedidoresCollectionOld.contains(amyMedidoresCollectionNewAmyMedidores)) {
                    AmyCajas oldFkAmyCajasOfAmyMedidoresCollectionNewAmyMedidores = amyMedidoresCollectionNewAmyMedidores.getFkAmyCajas();
                    amyMedidoresCollectionNewAmyMedidores.setFkAmyCajas(amyCajas);
                    amyMedidoresCollectionNewAmyMedidores = em.merge(amyMedidoresCollectionNewAmyMedidores);
                    if (oldFkAmyCajasOfAmyMedidoresCollectionNewAmyMedidores != null && !oldFkAmyCajasOfAmyMedidoresCollectionNewAmyMedidores.equals(amyCajas)) {
                        oldFkAmyCajasOfAmyMedidoresCollectionNewAmyMedidores.getAmyMedidoresCollection().remove(amyMedidoresCollectionNewAmyMedidores);
                        oldFkAmyCajasOfAmyMedidoresCollectionNewAmyMedidores = em.merge(oldFkAmyCajasOfAmyMedidoresCollectionNewAmyMedidores);
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
                Long id = amyCajas.getIdCajas();
                if (findAmyCajas(id) == null) {
                    throw new NonexistentEntityException("The amyCajas with id " + id + " no longer exists.");
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
            AmyCajas amyCajas;
            try {
                amyCajas = em.getReference(AmyCajas.class, id);
                amyCajas.getIdCajas();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The amyCajas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<AtrFncsActvCajas> atrFncsActvCajasCollectionOrphanCheck = amyCajas.getAtrFncsActvCajasCollection();
            for (AtrFncsActvCajas atrFncsActvCajasCollectionOrphanCheckAtrFncsActvCajas : atrFncsActvCajasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyCajas (" + amyCajas + ") cannot be destroyed since the AtrFncsActvCajas " + atrFncsActvCajasCollectionOrphanCheckAtrFncsActvCajas + " in its atrFncsActvCajasCollection field has a non-nullable fkAmyCajas field.");
            }
            Collection<AmyMedidores> amyMedidoresCollectionOrphanCheck = amyCajas.getAmyMedidoresCollection();
            for (AmyMedidores amyMedidoresCollectionOrphanCheckAmyMedidores : amyMedidoresCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyCajas (" + amyCajas + ") cannot be destroyed since the AmyMedidores " + amyMedidoresCollectionOrphanCheckAmyMedidores + " in its amyMedidoresCollection field has a non-nullable fkAmyCajas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            EnergiaTransformadores fkEnergiaTransformadores = amyCajas.getFkEnergiaTransformadores();
            if (fkEnergiaTransformadores != null) {
                fkEnergiaTransformadores.getAmyCajasCollection().remove(amyCajas);
                fkEnergiaTransformadores = em.merge(fkEnergiaTransformadores);
            }
            AtributosTiposCajas fkTiposCajas = amyCajas.getFkTiposCajas();
            if (fkTiposCajas != null) {
                fkTiposCajas.getAmyCajasCollection().remove(amyCajas);
                fkTiposCajas = em.merge(fkTiposCajas);
            }
            em.remove(amyCajas);
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

    public List<AmyCajas> findAmyCajasEntities() {
        return findAmyCajasEntities(true, -1, -1);
    }

    public List<AmyCajas> findAmyCajasEntities(int maxResults, int firstResult) {
        return findAmyCajasEntities(false, maxResults, firstResult);
    }

    private List<AmyCajas> findAmyCajasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AmyCajas.class));
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

    public AmyCajas findAmyCajas(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AmyCajas.class, id);
        } finally {
            em.close();
        }
    }

    public int getAmyCajasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AmyCajas> rt = cq.from(AmyCajas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
