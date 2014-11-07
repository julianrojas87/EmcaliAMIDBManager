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
import emcali.ami.persistence.entity.PrepagoClientes;
import emcali.ami.persistence.entity.PrepagoEstado;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class PrepagoEstadoJpaController implements Serializable {

    public PrepagoEstadoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PrepagoEstado prepagoEstado) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (prepagoEstado.getPrepagoClientesList() == null) {
            prepagoEstado.setPrepagoClientesList(new ArrayList<PrepagoClientes>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<PrepagoClientes> attachedPrepagoClientesList = new ArrayList<PrepagoClientes>();
            for (PrepagoClientes prepagoClientesListPrepagoClientesToAttach : prepagoEstado.getPrepagoClientesList()) {
                prepagoClientesListPrepagoClientesToAttach = em.getReference(prepagoClientesListPrepagoClientesToAttach.getClass(), prepagoClientesListPrepagoClientesToAttach.getIdClientePrepago());
                attachedPrepagoClientesList.add(prepagoClientesListPrepagoClientesToAttach);
            }
            prepagoEstado.setPrepagoClientesList(attachedPrepagoClientesList);
            em.persist(prepagoEstado);
            for (PrepagoClientes prepagoClientesListPrepagoClientes : prepagoEstado.getPrepagoClientesList()) {
                PrepagoEstado oldFkPrepagoEstadoOfPrepagoClientesListPrepagoClientes = prepagoClientesListPrepagoClientes.getFkPrepagoEstado();
                prepagoClientesListPrepagoClientes.setFkPrepagoEstado(prepagoEstado);
                prepagoClientesListPrepagoClientes = em.merge(prepagoClientesListPrepagoClientes);
                if (oldFkPrepagoEstadoOfPrepagoClientesListPrepagoClientes != null) {
                    oldFkPrepagoEstadoOfPrepagoClientesListPrepagoClientes.getPrepagoClientesList().remove(prepagoClientesListPrepagoClientes);
                    oldFkPrepagoEstadoOfPrepagoClientesListPrepagoClientes = em.merge(oldFkPrepagoEstadoOfPrepagoClientesListPrepagoClientes);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPrepagoEstado(prepagoEstado.getIdEstado()) != null) {
                throw new PreexistingEntityException("PrepagoEstado " + prepagoEstado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PrepagoEstado prepagoEstado) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PrepagoEstado persistentPrepagoEstado = em.find(PrepagoEstado.class, prepagoEstado.getIdEstado());
            List<PrepagoClientes> prepagoClientesListOld = persistentPrepagoEstado.getPrepagoClientesList();
            List<PrepagoClientes> prepagoClientesListNew = prepagoEstado.getPrepagoClientesList();
            List<String> illegalOrphanMessages = null;
            for (PrepagoClientes prepagoClientesListOldPrepagoClientes : prepagoClientesListOld) {
                if (!prepagoClientesListNew.contains(prepagoClientesListOldPrepagoClientes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PrepagoClientes " + prepagoClientesListOldPrepagoClientes + " since its fkPrepagoEstado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<PrepagoClientes> attachedPrepagoClientesListNew = new ArrayList<PrepagoClientes>();
            for (PrepagoClientes prepagoClientesListNewPrepagoClientesToAttach : prepagoClientesListNew) {
                prepagoClientesListNewPrepagoClientesToAttach = em.getReference(prepagoClientesListNewPrepagoClientesToAttach.getClass(), prepagoClientesListNewPrepagoClientesToAttach.getIdClientePrepago());
                attachedPrepagoClientesListNew.add(prepagoClientesListNewPrepagoClientesToAttach);
            }
            prepagoClientesListNew = attachedPrepagoClientesListNew;
            prepagoEstado.setPrepagoClientesList(prepagoClientesListNew);
            prepagoEstado = em.merge(prepagoEstado);
            for (PrepagoClientes prepagoClientesListNewPrepagoClientes : prepagoClientesListNew) {
                if (!prepagoClientesListOld.contains(prepagoClientesListNewPrepagoClientes)) {
                    PrepagoEstado oldFkPrepagoEstadoOfPrepagoClientesListNewPrepagoClientes = prepagoClientesListNewPrepagoClientes.getFkPrepagoEstado();
                    prepagoClientesListNewPrepagoClientes.setFkPrepagoEstado(prepagoEstado);
                    prepagoClientesListNewPrepagoClientes = em.merge(prepagoClientesListNewPrepagoClientes);
                    if (oldFkPrepagoEstadoOfPrepagoClientesListNewPrepagoClientes != null && !oldFkPrepagoEstadoOfPrepagoClientesListNewPrepagoClientes.equals(prepagoEstado)) {
                        oldFkPrepagoEstadoOfPrepagoClientesListNewPrepagoClientes.getPrepagoClientesList().remove(prepagoClientesListNewPrepagoClientes);
                        oldFkPrepagoEstadoOfPrepagoClientesListNewPrepagoClientes = em.merge(oldFkPrepagoEstadoOfPrepagoClientesListNewPrepagoClientes);
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
                Long id = prepagoEstado.getIdEstado();
                if (findPrepagoEstado(id) == null) {
                    throw new NonexistentEntityException("The prepagoEstado with id " + id + " no longer exists.");
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
            PrepagoEstado prepagoEstado;
            try {
                prepagoEstado = em.getReference(PrepagoEstado.class, id);
                prepagoEstado.getIdEstado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The prepagoEstado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PrepagoClientes> prepagoClientesListOrphanCheck = prepagoEstado.getPrepagoClientesList();
            for (PrepagoClientes prepagoClientesListOrphanCheckPrepagoClientes : prepagoClientesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PrepagoEstado (" + prepagoEstado + ") cannot be destroyed since the PrepagoClientes " + prepagoClientesListOrphanCheckPrepagoClientes + " in its prepagoClientesList field has a non-nullable fkPrepagoEstado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(prepagoEstado);
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

    public List<PrepagoEstado> findPrepagoEstadoEntities() {
        return findPrepagoEstadoEntities(true, -1, -1);
    }

    public List<PrepagoEstado> findPrepagoEstadoEntities(int maxResults, int firstResult) {
        return findPrepagoEstadoEntities(false, maxResults, firstResult);
    }

    private List<PrepagoEstado> findPrepagoEstadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PrepagoEstado.class));
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

    public PrepagoEstado findPrepagoEstado(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PrepagoEstado.class, id);
        } finally {
            em.close();
        }
    }

    public int getPrepagoEstadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PrepagoEstado> rt = cq.from(PrepagoEstado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
