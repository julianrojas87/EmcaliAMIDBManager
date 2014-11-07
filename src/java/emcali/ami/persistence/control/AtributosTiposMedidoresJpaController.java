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
        if (atributosTiposMedidores.getAmyMedidoresList() == null) {
            atributosTiposMedidores.setAmyMedidoresList(new ArrayList<AmyMedidores>());
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
            List<AmyMedidores> attachedAmyMedidoresList = new ArrayList<AmyMedidores>();
            for (AmyMedidores amyMedidoresListAmyMedidoresToAttach : atributosTiposMedidores.getAmyMedidoresList()) {
                amyMedidoresListAmyMedidoresToAttach = em.getReference(amyMedidoresListAmyMedidoresToAttach.getClass(), amyMedidoresListAmyMedidoresToAttach.getIdMedidores());
                attachedAmyMedidoresList.add(amyMedidoresListAmyMedidoresToAttach);
            }
            atributosTiposMedidores.setAmyMedidoresList(attachedAmyMedidoresList);
            em.persist(atributosTiposMedidores);
            if (fkAtributosFabricantes != null) {
                fkAtributosFabricantes.getAtributosTiposMedidoresList().add(atributosTiposMedidores);
                fkAtributosFabricantes = em.merge(fkAtributosFabricantes);
            }
            for (AmyMedidores amyMedidoresListAmyMedidores : atributosTiposMedidores.getAmyMedidoresList()) {
                AtributosTiposMedidores oldFkAtributosTiposMedidoresOfAmyMedidoresListAmyMedidores = amyMedidoresListAmyMedidores.getFkAtributosTiposMedidores();
                amyMedidoresListAmyMedidores.setFkAtributosTiposMedidores(atributosTiposMedidores);
                amyMedidoresListAmyMedidores = em.merge(amyMedidoresListAmyMedidores);
                if (oldFkAtributosTiposMedidoresOfAmyMedidoresListAmyMedidores != null) {
                    oldFkAtributosTiposMedidoresOfAmyMedidoresListAmyMedidores.getAmyMedidoresList().remove(amyMedidoresListAmyMedidores);
                    oldFkAtributosTiposMedidoresOfAmyMedidoresListAmyMedidores = em.merge(oldFkAtributosTiposMedidoresOfAmyMedidoresListAmyMedidores);
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
            List<AmyMedidores> amyMedidoresListOld = persistentAtributosTiposMedidores.getAmyMedidoresList();
            List<AmyMedidores> amyMedidoresListNew = atributosTiposMedidores.getAmyMedidoresList();
            List<String> illegalOrphanMessages = null;
            for (AmyMedidores amyMedidoresListOldAmyMedidores : amyMedidoresListOld) {
                if (!amyMedidoresListNew.contains(amyMedidoresListOldAmyMedidores)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyMedidores " + amyMedidoresListOldAmyMedidores + " since its fkAtributosTiposMedidores field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (fkAtributosFabricantesNew != null) {
                fkAtributosFabricantesNew = em.getReference(fkAtributosFabricantesNew.getClass(), fkAtributosFabricantesNew.getIdFabricantes());
                atributosTiposMedidores.setFkAtributosFabricantes(fkAtributosFabricantesNew);
            }
            List<AmyMedidores> attachedAmyMedidoresListNew = new ArrayList<AmyMedidores>();
            for (AmyMedidores amyMedidoresListNewAmyMedidoresToAttach : amyMedidoresListNew) {
                amyMedidoresListNewAmyMedidoresToAttach = em.getReference(amyMedidoresListNewAmyMedidoresToAttach.getClass(), amyMedidoresListNewAmyMedidoresToAttach.getIdMedidores());
                attachedAmyMedidoresListNew.add(amyMedidoresListNewAmyMedidoresToAttach);
            }
            amyMedidoresListNew = attachedAmyMedidoresListNew;
            atributosTiposMedidores.setAmyMedidoresList(amyMedidoresListNew);
            atributosTiposMedidores = em.merge(atributosTiposMedidores);
            if (fkAtributosFabricantesOld != null && !fkAtributosFabricantesOld.equals(fkAtributosFabricantesNew)) {
                fkAtributosFabricantesOld.getAtributosTiposMedidoresList().remove(atributosTiposMedidores);
                fkAtributosFabricantesOld = em.merge(fkAtributosFabricantesOld);
            }
            if (fkAtributosFabricantesNew != null && !fkAtributosFabricantesNew.equals(fkAtributosFabricantesOld)) {
                fkAtributosFabricantesNew.getAtributosTiposMedidoresList().add(atributosTiposMedidores);
                fkAtributosFabricantesNew = em.merge(fkAtributosFabricantesNew);
            }
            for (AmyMedidores amyMedidoresListNewAmyMedidores : amyMedidoresListNew) {
                if (!amyMedidoresListOld.contains(amyMedidoresListNewAmyMedidores)) {
                    AtributosTiposMedidores oldFkAtributosTiposMedidoresOfAmyMedidoresListNewAmyMedidores = amyMedidoresListNewAmyMedidores.getFkAtributosTiposMedidores();
                    amyMedidoresListNewAmyMedidores.setFkAtributosTiposMedidores(atributosTiposMedidores);
                    amyMedidoresListNewAmyMedidores = em.merge(amyMedidoresListNewAmyMedidores);
                    if (oldFkAtributosTiposMedidoresOfAmyMedidoresListNewAmyMedidores != null && !oldFkAtributosTiposMedidoresOfAmyMedidoresListNewAmyMedidores.equals(atributosTiposMedidores)) {
                        oldFkAtributosTiposMedidoresOfAmyMedidoresListNewAmyMedidores.getAmyMedidoresList().remove(amyMedidoresListNewAmyMedidores);
                        oldFkAtributosTiposMedidoresOfAmyMedidoresListNewAmyMedidores = em.merge(oldFkAtributosTiposMedidoresOfAmyMedidoresListNewAmyMedidores);
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
            List<AmyMedidores> amyMedidoresListOrphanCheck = atributosTiposMedidores.getAmyMedidoresList();
            for (AmyMedidores amyMedidoresListOrphanCheckAmyMedidores : amyMedidoresListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AtributosTiposMedidores (" + atributosTiposMedidores + ") cannot be destroyed since the AmyMedidores " + amyMedidoresListOrphanCheckAmyMedidores + " in its amyMedidoresList field has a non-nullable fkAtributosTiposMedidores field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            AtributosFabricantes fkAtributosFabricantes = atributosTiposMedidores.getFkAtributosFabricantes();
            if (fkAtributosFabricantes != null) {
                fkAtributosFabricantes.getAtributosTiposMedidoresList().remove(atributosTiposMedidores);
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
