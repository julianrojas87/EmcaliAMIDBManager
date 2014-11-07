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
import java.util.List;
import emcali.ami.persistence.entity.AtributosTiposMedidores;
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
        if (atributosFabricantes.getAtributosTiposCajasList() == null) {
            atributosFabricantes.setAtributosTiposCajasList(new ArrayList<AtributosTiposCajas>());
        }
        if (atributosFabricantes.getAtributosTiposMedidoresList() == null) {
            atributosFabricantes.setAtributosTiposMedidoresList(new ArrayList<AtributosTiposMedidores>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<AtributosTiposCajas> attachedAtributosTiposCajasList = new ArrayList<AtributosTiposCajas>();
            for (AtributosTiposCajas atributosTiposCajasListAtributosTiposCajasToAttach : atributosFabricantes.getAtributosTiposCajasList()) {
                atributosTiposCajasListAtributosTiposCajasToAttach = em.getReference(atributosTiposCajasListAtributosTiposCajasToAttach.getClass(), atributosTiposCajasListAtributosTiposCajasToAttach.getIdTiposCajas());
                attachedAtributosTiposCajasList.add(atributosTiposCajasListAtributosTiposCajasToAttach);
            }
            atributosFabricantes.setAtributosTiposCajasList(attachedAtributosTiposCajasList);
            List<AtributosTiposMedidores> attachedAtributosTiposMedidoresList = new ArrayList<AtributosTiposMedidores>();
            for (AtributosTiposMedidores atributosTiposMedidoresListAtributosTiposMedidoresToAttach : atributosFabricantes.getAtributosTiposMedidoresList()) {
                atributosTiposMedidoresListAtributosTiposMedidoresToAttach = em.getReference(atributosTiposMedidoresListAtributosTiposMedidoresToAttach.getClass(), atributosTiposMedidoresListAtributosTiposMedidoresToAttach.getIdTiposMedidores());
                attachedAtributosTiposMedidoresList.add(atributosTiposMedidoresListAtributosTiposMedidoresToAttach);
            }
            atributosFabricantes.setAtributosTiposMedidoresList(attachedAtributosTiposMedidoresList);
            em.persist(atributosFabricantes);
            for (AtributosTiposCajas atributosTiposCajasListAtributosTiposCajas : atributosFabricantes.getAtributosTiposCajasList()) {
                AtributosFabricantes oldFkAtributosFabricantesOfAtributosTiposCajasListAtributosTiposCajas = atributosTiposCajasListAtributosTiposCajas.getFkAtributosFabricantes();
                atributosTiposCajasListAtributosTiposCajas.setFkAtributosFabricantes(atributosFabricantes);
                atributosTiposCajasListAtributosTiposCajas = em.merge(atributosTiposCajasListAtributosTiposCajas);
                if (oldFkAtributosFabricantesOfAtributosTiposCajasListAtributosTiposCajas != null) {
                    oldFkAtributosFabricantesOfAtributosTiposCajasListAtributosTiposCajas.getAtributosTiposCajasList().remove(atributosTiposCajasListAtributosTiposCajas);
                    oldFkAtributosFabricantesOfAtributosTiposCajasListAtributosTiposCajas = em.merge(oldFkAtributosFabricantesOfAtributosTiposCajasListAtributosTiposCajas);
                }
            }
            for (AtributosTiposMedidores atributosTiposMedidoresListAtributosTiposMedidores : atributosFabricantes.getAtributosTiposMedidoresList()) {
                AtributosFabricantes oldFkAtributosFabricantesOfAtributosTiposMedidoresListAtributosTiposMedidores = atributosTiposMedidoresListAtributosTiposMedidores.getFkAtributosFabricantes();
                atributosTiposMedidoresListAtributosTiposMedidores.setFkAtributosFabricantes(atributosFabricantes);
                atributosTiposMedidoresListAtributosTiposMedidores = em.merge(atributosTiposMedidoresListAtributosTiposMedidores);
                if (oldFkAtributosFabricantesOfAtributosTiposMedidoresListAtributosTiposMedidores != null) {
                    oldFkAtributosFabricantesOfAtributosTiposMedidoresListAtributosTiposMedidores.getAtributosTiposMedidoresList().remove(atributosTiposMedidoresListAtributosTiposMedidores);
                    oldFkAtributosFabricantesOfAtributosTiposMedidoresListAtributosTiposMedidores = em.merge(oldFkAtributosFabricantesOfAtributosTiposMedidoresListAtributosTiposMedidores);
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
            List<AtributosTiposCajas> atributosTiposCajasListOld = persistentAtributosFabricantes.getAtributosTiposCajasList();
            List<AtributosTiposCajas> atributosTiposCajasListNew = atributosFabricantes.getAtributosTiposCajasList();
            List<AtributosTiposMedidores> atributosTiposMedidoresListOld = persistentAtributosFabricantes.getAtributosTiposMedidoresList();
            List<AtributosTiposMedidores> atributosTiposMedidoresListNew = atributosFabricantes.getAtributosTiposMedidoresList();
            List<String> illegalOrphanMessages = null;
            for (AtributosTiposCajas atributosTiposCajasListOldAtributosTiposCajas : atributosTiposCajasListOld) {
                if (!atributosTiposCajasListNew.contains(atributosTiposCajasListOldAtributosTiposCajas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AtributosTiposCajas " + atributosTiposCajasListOldAtributosTiposCajas + " since its fkAtributosFabricantes field is not nullable.");
                }
            }
            for (AtributosTiposMedidores atributosTiposMedidoresListOldAtributosTiposMedidores : atributosTiposMedidoresListOld) {
                if (!atributosTiposMedidoresListNew.contains(atributosTiposMedidoresListOldAtributosTiposMedidores)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AtributosTiposMedidores " + atributosTiposMedidoresListOldAtributosTiposMedidores + " since its fkAtributosFabricantes field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<AtributosTiposCajas> attachedAtributosTiposCajasListNew = new ArrayList<AtributosTiposCajas>();
            for (AtributosTiposCajas atributosTiposCajasListNewAtributosTiposCajasToAttach : atributosTiposCajasListNew) {
                atributosTiposCajasListNewAtributosTiposCajasToAttach = em.getReference(atributosTiposCajasListNewAtributosTiposCajasToAttach.getClass(), atributosTiposCajasListNewAtributosTiposCajasToAttach.getIdTiposCajas());
                attachedAtributosTiposCajasListNew.add(atributosTiposCajasListNewAtributosTiposCajasToAttach);
            }
            atributosTiposCajasListNew = attachedAtributosTiposCajasListNew;
            atributosFabricantes.setAtributosTiposCajasList(atributosTiposCajasListNew);
            List<AtributosTiposMedidores> attachedAtributosTiposMedidoresListNew = new ArrayList<AtributosTiposMedidores>();
            for (AtributosTiposMedidores atributosTiposMedidoresListNewAtributosTiposMedidoresToAttach : atributosTiposMedidoresListNew) {
                atributosTiposMedidoresListNewAtributosTiposMedidoresToAttach = em.getReference(atributosTiposMedidoresListNewAtributosTiposMedidoresToAttach.getClass(), atributosTiposMedidoresListNewAtributosTiposMedidoresToAttach.getIdTiposMedidores());
                attachedAtributosTiposMedidoresListNew.add(atributosTiposMedidoresListNewAtributosTiposMedidoresToAttach);
            }
            atributosTiposMedidoresListNew = attachedAtributosTiposMedidoresListNew;
            atributosFabricantes.setAtributosTiposMedidoresList(atributosTiposMedidoresListNew);
            atributosFabricantes = em.merge(atributosFabricantes);
            for (AtributosTiposCajas atributosTiposCajasListNewAtributosTiposCajas : atributosTiposCajasListNew) {
                if (!atributosTiposCajasListOld.contains(atributosTiposCajasListNewAtributosTiposCajas)) {
                    AtributosFabricantes oldFkAtributosFabricantesOfAtributosTiposCajasListNewAtributosTiposCajas = atributosTiposCajasListNewAtributosTiposCajas.getFkAtributosFabricantes();
                    atributosTiposCajasListNewAtributosTiposCajas.setFkAtributosFabricantes(atributosFabricantes);
                    atributosTiposCajasListNewAtributosTiposCajas = em.merge(atributosTiposCajasListNewAtributosTiposCajas);
                    if (oldFkAtributosFabricantesOfAtributosTiposCajasListNewAtributosTiposCajas != null && !oldFkAtributosFabricantesOfAtributosTiposCajasListNewAtributosTiposCajas.equals(atributosFabricantes)) {
                        oldFkAtributosFabricantesOfAtributosTiposCajasListNewAtributosTiposCajas.getAtributosTiposCajasList().remove(atributosTiposCajasListNewAtributosTiposCajas);
                        oldFkAtributosFabricantesOfAtributosTiposCajasListNewAtributosTiposCajas = em.merge(oldFkAtributosFabricantesOfAtributosTiposCajasListNewAtributosTiposCajas);
                    }
                }
            }
            for (AtributosTiposMedidores atributosTiposMedidoresListNewAtributosTiposMedidores : atributosTiposMedidoresListNew) {
                if (!atributosTiposMedidoresListOld.contains(atributosTiposMedidoresListNewAtributosTiposMedidores)) {
                    AtributosFabricantes oldFkAtributosFabricantesOfAtributosTiposMedidoresListNewAtributosTiposMedidores = atributosTiposMedidoresListNewAtributosTiposMedidores.getFkAtributosFabricantes();
                    atributosTiposMedidoresListNewAtributosTiposMedidores.setFkAtributosFabricantes(atributosFabricantes);
                    atributosTiposMedidoresListNewAtributosTiposMedidores = em.merge(atributosTiposMedidoresListNewAtributosTiposMedidores);
                    if (oldFkAtributosFabricantesOfAtributosTiposMedidoresListNewAtributosTiposMedidores != null && !oldFkAtributosFabricantesOfAtributosTiposMedidoresListNewAtributosTiposMedidores.equals(atributosFabricantes)) {
                        oldFkAtributosFabricantesOfAtributosTiposMedidoresListNewAtributosTiposMedidores.getAtributosTiposMedidoresList().remove(atributosTiposMedidoresListNewAtributosTiposMedidores);
                        oldFkAtributosFabricantesOfAtributosTiposMedidoresListNewAtributosTiposMedidores = em.merge(oldFkAtributosFabricantesOfAtributosTiposMedidoresListNewAtributosTiposMedidores);
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
            List<AtributosTiposCajas> atributosTiposCajasListOrphanCheck = atributosFabricantes.getAtributosTiposCajasList();
            for (AtributosTiposCajas atributosTiposCajasListOrphanCheckAtributosTiposCajas : atributosTiposCajasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AtributosFabricantes (" + atributosFabricantes + ") cannot be destroyed since the AtributosTiposCajas " + atributosTiposCajasListOrphanCheckAtributosTiposCajas + " in its atributosTiposCajasList field has a non-nullable fkAtributosFabricantes field.");
            }
            List<AtributosTiposMedidores> atributosTiposMedidoresListOrphanCheck = atributosFabricantes.getAtributosTiposMedidoresList();
            for (AtributosTiposMedidores atributosTiposMedidoresListOrphanCheckAtributosTiposMedidores : atributosTiposMedidoresListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AtributosFabricantes (" + atributosFabricantes + ") cannot be destroyed since the AtributosTiposMedidores " + atributosTiposMedidoresListOrphanCheckAtributosTiposMedidores + " in its atributosTiposMedidoresList field has a non-nullable fkAtributosFabricantes field.");
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
