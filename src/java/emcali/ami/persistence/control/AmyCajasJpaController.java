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
import java.util.List;
import emcali.ami.persistence.entity.AmyMedidores;
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
        if (amyCajas.getAtrFncsActvCajasList() == null) {
            amyCajas.setAtrFncsActvCajasList(new ArrayList<AtrFncsActvCajas>());
        }
        if (amyCajas.getAmyMedidoresList() == null) {
            amyCajas.setAmyMedidoresList(new ArrayList<AmyMedidores>());
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
            List<AtrFncsActvCajas> attachedAtrFncsActvCajasList = new ArrayList<AtrFncsActvCajas>();
            for (AtrFncsActvCajas atrFncsActvCajasListAtrFncsActvCajasToAttach : amyCajas.getAtrFncsActvCajasList()) {
                atrFncsActvCajasListAtrFncsActvCajasToAttach = em.getReference(atrFncsActvCajasListAtrFncsActvCajasToAttach.getClass(), atrFncsActvCajasListAtrFncsActvCajasToAttach.getIdFuncionActivaCaja());
                attachedAtrFncsActvCajasList.add(atrFncsActvCajasListAtrFncsActvCajasToAttach);
            }
            amyCajas.setAtrFncsActvCajasList(attachedAtrFncsActvCajasList);
            List<AmyMedidores> attachedAmyMedidoresList = new ArrayList<AmyMedidores>();
            for (AmyMedidores amyMedidoresListAmyMedidoresToAttach : amyCajas.getAmyMedidoresList()) {
                amyMedidoresListAmyMedidoresToAttach = em.getReference(amyMedidoresListAmyMedidoresToAttach.getClass(), amyMedidoresListAmyMedidoresToAttach.getIdMedidores());
                attachedAmyMedidoresList.add(amyMedidoresListAmyMedidoresToAttach);
            }
            amyCajas.setAmyMedidoresList(attachedAmyMedidoresList);
            em.persist(amyCajas);
            if (fkEnergiaTransformadores != null) {
                fkEnergiaTransformadores.getAmyCajasList().add(amyCajas);
                fkEnergiaTransformadores = em.merge(fkEnergiaTransformadores);
            }
            if (fkTiposCajas != null) {
                fkTiposCajas.getAmyCajasList().add(amyCajas);
                fkTiposCajas = em.merge(fkTiposCajas);
            }
            for (AtrFncsActvCajas atrFncsActvCajasListAtrFncsActvCajas : amyCajas.getAtrFncsActvCajasList()) {
                AmyCajas oldFkAmyCajasOfAtrFncsActvCajasListAtrFncsActvCajas = atrFncsActvCajasListAtrFncsActvCajas.getFkAmyCajas();
                atrFncsActvCajasListAtrFncsActvCajas.setFkAmyCajas(amyCajas);
                atrFncsActvCajasListAtrFncsActvCajas = em.merge(atrFncsActvCajasListAtrFncsActvCajas);
                if (oldFkAmyCajasOfAtrFncsActvCajasListAtrFncsActvCajas != null) {
                    oldFkAmyCajasOfAtrFncsActvCajasListAtrFncsActvCajas.getAtrFncsActvCajasList().remove(atrFncsActvCajasListAtrFncsActvCajas);
                    oldFkAmyCajasOfAtrFncsActvCajasListAtrFncsActvCajas = em.merge(oldFkAmyCajasOfAtrFncsActvCajasListAtrFncsActvCajas);
                }
            }
            for (AmyMedidores amyMedidoresListAmyMedidores : amyCajas.getAmyMedidoresList()) {
                AmyCajas oldFkAmyCajasOfAmyMedidoresListAmyMedidores = amyMedidoresListAmyMedidores.getFkAmyCajas();
                amyMedidoresListAmyMedidores.setFkAmyCajas(amyCajas);
                amyMedidoresListAmyMedidores = em.merge(amyMedidoresListAmyMedidores);
                if (oldFkAmyCajasOfAmyMedidoresListAmyMedidores != null) {
                    oldFkAmyCajasOfAmyMedidoresListAmyMedidores.getAmyMedidoresList().remove(amyMedidoresListAmyMedidores);
                    oldFkAmyCajasOfAmyMedidoresListAmyMedidores = em.merge(oldFkAmyCajasOfAmyMedidoresListAmyMedidores);
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
            List<AtrFncsActvCajas> atrFncsActvCajasListOld = persistentAmyCajas.getAtrFncsActvCajasList();
            List<AtrFncsActvCajas> atrFncsActvCajasListNew = amyCajas.getAtrFncsActvCajasList();
            List<AmyMedidores> amyMedidoresListOld = persistentAmyCajas.getAmyMedidoresList();
            List<AmyMedidores> amyMedidoresListNew = amyCajas.getAmyMedidoresList();
            List<String> illegalOrphanMessages = null;
            for (AtrFncsActvCajas atrFncsActvCajasListOldAtrFncsActvCajas : atrFncsActvCajasListOld) {
                if (!atrFncsActvCajasListNew.contains(atrFncsActvCajasListOldAtrFncsActvCajas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AtrFncsActvCajas " + atrFncsActvCajasListOldAtrFncsActvCajas + " since its fkAmyCajas field is not nullable.");
                }
            }
            for (AmyMedidores amyMedidoresListOldAmyMedidores : amyMedidoresListOld) {
                if (!amyMedidoresListNew.contains(amyMedidoresListOldAmyMedidores)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyMedidores " + amyMedidoresListOldAmyMedidores + " since its fkAmyCajas field is not nullable.");
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
            List<AtrFncsActvCajas> attachedAtrFncsActvCajasListNew = new ArrayList<AtrFncsActvCajas>();
            for (AtrFncsActvCajas atrFncsActvCajasListNewAtrFncsActvCajasToAttach : atrFncsActvCajasListNew) {
                atrFncsActvCajasListNewAtrFncsActvCajasToAttach = em.getReference(atrFncsActvCajasListNewAtrFncsActvCajasToAttach.getClass(), atrFncsActvCajasListNewAtrFncsActvCajasToAttach.getIdFuncionActivaCaja());
                attachedAtrFncsActvCajasListNew.add(atrFncsActvCajasListNewAtrFncsActvCajasToAttach);
            }
            atrFncsActvCajasListNew = attachedAtrFncsActvCajasListNew;
            amyCajas.setAtrFncsActvCajasList(atrFncsActvCajasListNew);
            List<AmyMedidores> attachedAmyMedidoresListNew = new ArrayList<AmyMedidores>();
            for (AmyMedidores amyMedidoresListNewAmyMedidoresToAttach : amyMedidoresListNew) {
                amyMedidoresListNewAmyMedidoresToAttach = em.getReference(amyMedidoresListNewAmyMedidoresToAttach.getClass(), amyMedidoresListNewAmyMedidoresToAttach.getIdMedidores());
                attachedAmyMedidoresListNew.add(amyMedidoresListNewAmyMedidoresToAttach);
            }
            amyMedidoresListNew = attachedAmyMedidoresListNew;
            amyCajas.setAmyMedidoresList(amyMedidoresListNew);
            amyCajas = em.merge(amyCajas);
            if (fkEnergiaTransformadoresOld != null && !fkEnergiaTransformadoresOld.equals(fkEnergiaTransformadoresNew)) {
                fkEnergiaTransformadoresOld.getAmyCajasList().remove(amyCajas);
                fkEnergiaTransformadoresOld = em.merge(fkEnergiaTransformadoresOld);
            }
            if (fkEnergiaTransformadoresNew != null && !fkEnergiaTransformadoresNew.equals(fkEnergiaTransformadoresOld)) {
                fkEnergiaTransformadoresNew.getAmyCajasList().add(amyCajas);
                fkEnergiaTransformadoresNew = em.merge(fkEnergiaTransformadoresNew);
            }
            if (fkTiposCajasOld != null && !fkTiposCajasOld.equals(fkTiposCajasNew)) {
                fkTiposCajasOld.getAmyCajasList().remove(amyCajas);
                fkTiposCajasOld = em.merge(fkTiposCajasOld);
            }
            if (fkTiposCajasNew != null && !fkTiposCajasNew.equals(fkTiposCajasOld)) {
                fkTiposCajasNew.getAmyCajasList().add(amyCajas);
                fkTiposCajasNew = em.merge(fkTiposCajasNew);
            }
            for (AtrFncsActvCajas atrFncsActvCajasListNewAtrFncsActvCajas : atrFncsActvCajasListNew) {
                if (!atrFncsActvCajasListOld.contains(atrFncsActvCajasListNewAtrFncsActvCajas)) {
                    AmyCajas oldFkAmyCajasOfAtrFncsActvCajasListNewAtrFncsActvCajas = atrFncsActvCajasListNewAtrFncsActvCajas.getFkAmyCajas();
                    atrFncsActvCajasListNewAtrFncsActvCajas.setFkAmyCajas(amyCajas);
                    atrFncsActvCajasListNewAtrFncsActvCajas = em.merge(atrFncsActvCajasListNewAtrFncsActvCajas);
                    if (oldFkAmyCajasOfAtrFncsActvCajasListNewAtrFncsActvCajas != null && !oldFkAmyCajasOfAtrFncsActvCajasListNewAtrFncsActvCajas.equals(amyCajas)) {
                        oldFkAmyCajasOfAtrFncsActvCajasListNewAtrFncsActvCajas.getAtrFncsActvCajasList().remove(atrFncsActvCajasListNewAtrFncsActvCajas);
                        oldFkAmyCajasOfAtrFncsActvCajasListNewAtrFncsActvCajas = em.merge(oldFkAmyCajasOfAtrFncsActvCajasListNewAtrFncsActvCajas);
                    }
                }
            }
            for (AmyMedidores amyMedidoresListNewAmyMedidores : amyMedidoresListNew) {
                if (!amyMedidoresListOld.contains(amyMedidoresListNewAmyMedidores)) {
                    AmyCajas oldFkAmyCajasOfAmyMedidoresListNewAmyMedidores = amyMedidoresListNewAmyMedidores.getFkAmyCajas();
                    amyMedidoresListNewAmyMedidores.setFkAmyCajas(amyCajas);
                    amyMedidoresListNewAmyMedidores = em.merge(amyMedidoresListNewAmyMedidores);
                    if (oldFkAmyCajasOfAmyMedidoresListNewAmyMedidores != null && !oldFkAmyCajasOfAmyMedidoresListNewAmyMedidores.equals(amyCajas)) {
                        oldFkAmyCajasOfAmyMedidoresListNewAmyMedidores.getAmyMedidoresList().remove(amyMedidoresListNewAmyMedidores);
                        oldFkAmyCajasOfAmyMedidoresListNewAmyMedidores = em.merge(oldFkAmyCajasOfAmyMedidoresListNewAmyMedidores);
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
            List<AtrFncsActvCajas> atrFncsActvCajasListOrphanCheck = amyCajas.getAtrFncsActvCajasList();
            for (AtrFncsActvCajas atrFncsActvCajasListOrphanCheckAtrFncsActvCajas : atrFncsActvCajasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyCajas (" + amyCajas + ") cannot be destroyed since the AtrFncsActvCajas " + atrFncsActvCajasListOrphanCheckAtrFncsActvCajas + " in its atrFncsActvCajasList field has a non-nullable fkAmyCajas field.");
            }
            List<AmyMedidores> amyMedidoresListOrphanCheck = amyCajas.getAmyMedidoresList();
            for (AmyMedidores amyMedidoresListOrphanCheckAmyMedidores : amyMedidoresListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyCajas (" + amyCajas + ") cannot be destroyed since the AmyMedidores " + amyMedidoresListOrphanCheckAmyMedidores + " in its amyMedidoresList field has a non-nullable fkAmyCajas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            EnergiaTransformadores fkEnergiaTransformadores = amyCajas.getFkEnergiaTransformadores();
            if (fkEnergiaTransformadores != null) {
                fkEnergiaTransformadores.getAmyCajasList().remove(amyCajas);
                fkEnergiaTransformadores = em.merge(fkEnergiaTransformadores);
            }
            AtributosTiposCajas fkTiposCajas = amyCajas.getFkTiposCajas();
            if (fkTiposCajas != null) {
                fkTiposCajas.getAmyCajasList().remove(amyCajas);
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
