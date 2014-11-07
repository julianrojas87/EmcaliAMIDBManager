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
import emcali.ami.persistence.entity.ComercialClientes;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import emcali.ami.persistence.entity.GestionFuncionarios;
import emcali.ami.persistence.entity.TelcoInfo;
import java.util.ArrayList;
import java.util.List;
import emcali.ami.persistence.entity.ComercialContratos;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class ComercialClientesJpaController implements Serializable {

    public ComercialClientesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ComercialClientes comercialClientes) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (comercialClientes.getTelcoInfoList() == null) {
            comercialClientes.setTelcoInfoList(new ArrayList<TelcoInfo>());
        }
        if (comercialClientes.getComercialContratosList() == null) {
            comercialClientes.setComercialContratosList(new ArrayList<ComercialContratos>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GestionFuncionarios modificadoPor = comercialClientes.getModificadoPor();
            if (modificadoPor != null) {
                modificadoPor = em.getReference(modificadoPor.getClass(), modificadoPor.getIdFuncionarios());
                comercialClientes.setModificadoPor(modificadoPor);
            }
            GestionFuncionarios creadoPor = comercialClientes.getCreadoPor();
            if (creadoPor != null) {
                creadoPor = em.getReference(creadoPor.getClass(), creadoPor.getIdFuncionarios());
                comercialClientes.setCreadoPor(creadoPor);
            }
            List<TelcoInfo> attachedTelcoInfoList = new ArrayList<TelcoInfo>();
            for (TelcoInfo telcoInfoListTelcoInfoToAttach : comercialClientes.getTelcoInfoList()) {
                telcoInfoListTelcoInfoToAttach = em.getReference(telcoInfoListTelcoInfoToAttach.getClass(), telcoInfoListTelcoInfoToAttach.getIdSuscriptor());
                attachedTelcoInfoList.add(telcoInfoListTelcoInfoToAttach);
            }
            comercialClientes.setTelcoInfoList(attachedTelcoInfoList);
            List<ComercialContratos> attachedComercialContratosList = new ArrayList<ComercialContratos>();
            for (ComercialContratos comercialContratosListComercialContratosToAttach : comercialClientes.getComercialContratosList()) {
                comercialContratosListComercialContratosToAttach = em.getReference(comercialContratosListComercialContratosToAttach.getClass(), comercialContratosListComercialContratosToAttach.getIdContratos());
                attachedComercialContratosList.add(comercialContratosListComercialContratosToAttach);
            }
            comercialClientes.setComercialContratosList(attachedComercialContratosList);
            em.persist(comercialClientes);
            if (modificadoPor != null) {
                modificadoPor.getComercialClientesList().add(comercialClientes);
                modificadoPor = em.merge(modificadoPor);
            }
            if (creadoPor != null) {
                creadoPor.getComercialClientesList().add(comercialClientes);
                creadoPor = em.merge(creadoPor);
            }
            for (TelcoInfo telcoInfoListTelcoInfo : comercialClientes.getTelcoInfoList()) {
                ComercialClientes oldFkComercialClientesOfTelcoInfoListTelcoInfo = telcoInfoListTelcoInfo.getFkComercialClientes();
                telcoInfoListTelcoInfo.setFkComercialClientes(comercialClientes);
                telcoInfoListTelcoInfo = em.merge(telcoInfoListTelcoInfo);
                if (oldFkComercialClientesOfTelcoInfoListTelcoInfo != null) {
                    oldFkComercialClientesOfTelcoInfoListTelcoInfo.getTelcoInfoList().remove(telcoInfoListTelcoInfo);
                    oldFkComercialClientesOfTelcoInfoListTelcoInfo = em.merge(oldFkComercialClientesOfTelcoInfoListTelcoInfo);
                }
            }
            for (ComercialContratos comercialContratosListComercialContratos : comercialClientes.getComercialContratosList()) {
                ComercialClientes oldFkComercialClientesOfComercialContratosListComercialContratos = comercialContratosListComercialContratos.getFkComercialClientes();
                comercialContratosListComercialContratos.setFkComercialClientes(comercialClientes);
                comercialContratosListComercialContratos = em.merge(comercialContratosListComercialContratos);
                if (oldFkComercialClientesOfComercialContratosListComercialContratos != null) {
                    oldFkComercialClientesOfComercialContratosListComercialContratos.getComercialContratosList().remove(comercialContratosListComercialContratos);
                    oldFkComercialClientesOfComercialContratosListComercialContratos = em.merge(oldFkComercialClientesOfComercialContratosListComercialContratos);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findComercialClientes(comercialClientes.getIdClientes()) != null) {
                throw new PreexistingEntityException("ComercialClientes " + comercialClientes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ComercialClientes comercialClientes) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ComercialClientes persistentComercialClientes = em.find(ComercialClientes.class, comercialClientes.getIdClientes());
            GestionFuncionarios modificadoPorOld = persistentComercialClientes.getModificadoPor();
            GestionFuncionarios modificadoPorNew = comercialClientes.getModificadoPor();
            GestionFuncionarios creadoPorOld = persistentComercialClientes.getCreadoPor();
            GestionFuncionarios creadoPorNew = comercialClientes.getCreadoPor();
            List<TelcoInfo> telcoInfoListOld = persistentComercialClientes.getTelcoInfoList();
            List<TelcoInfo> telcoInfoListNew = comercialClientes.getTelcoInfoList();
            List<ComercialContratos> comercialContratosListOld = persistentComercialClientes.getComercialContratosList();
            List<ComercialContratos> comercialContratosListNew = comercialClientes.getComercialContratosList();
            List<String> illegalOrphanMessages = null;
            for (TelcoInfo telcoInfoListOldTelcoInfo : telcoInfoListOld) {
                if (!telcoInfoListNew.contains(telcoInfoListOldTelcoInfo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TelcoInfo " + telcoInfoListOldTelcoInfo + " since its fkComercialClientes field is not nullable.");
                }
            }
            for (ComercialContratos comercialContratosListOldComercialContratos : comercialContratosListOld) {
                if (!comercialContratosListNew.contains(comercialContratosListOldComercialContratos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ComercialContratos " + comercialContratosListOldComercialContratos + " since its fkComercialClientes field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (modificadoPorNew != null) {
                modificadoPorNew = em.getReference(modificadoPorNew.getClass(), modificadoPorNew.getIdFuncionarios());
                comercialClientes.setModificadoPor(modificadoPorNew);
            }
            if (creadoPorNew != null) {
                creadoPorNew = em.getReference(creadoPorNew.getClass(), creadoPorNew.getIdFuncionarios());
                comercialClientes.setCreadoPor(creadoPorNew);
            }
            List<TelcoInfo> attachedTelcoInfoListNew = new ArrayList<TelcoInfo>();
            for (TelcoInfo telcoInfoListNewTelcoInfoToAttach : telcoInfoListNew) {
                telcoInfoListNewTelcoInfoToAttach = em.getReference(telcoInfoListNewTelcoInfoToAttach.getClass(), telcoInfoListNewTelcoInfoToAttach.getIdSuscriptor());
                attachedTelcoInfoListNew.add(telcoInfoListNewTelcoInfoToAttach);
            }
            telcoInfoListNew = attachedTelcoInfoListNew;
            comercialClientes.setTelcoInfoList(telcoInfoListNew);
            List<ComercialContratos> attachedComercialContratosListNew = new ArrayList<ComercialContratos>();
            for (ComercialContratos comercialContratosListNewComercialContratosToAttach : comercialContratosListNew) {
                comercialContratosListNewComercialContratosToAttach = em.getReference(comercialContratosListNewComercialContratosToAttach.getClass(), comercialContratosListNewComercialContratosToAttach.getIdContratos());
                attachedComercialContratosListNew.add(comercialContratosListNewComercialContratosToAttach);
            }
            comercialContratosListNew = attachedComercialContratosListNew;
            comercialClientes.setComercialContratosList(comercialContratosListNew);
            comercialClientes = em.merge(comercialClientes);
            if (modificadoPorOld != null && !modificadoPorOld.equals(modificadoPorNew)) {
                modificadoPorOld.getComercialClientesList().remove(comercialClientes);
                modificadoPorOld = em.merge(modificadoPorOld);
            }
            if (modificadoPorNew != null && !modificadoPorNew.equals(modificadoPorOld)) {
                modificadoPorNew.getComercialClientesList().add(comercialClientes);
                modificadoPorNew = em.merge(modificadoPorNew);
            }
            if (creadoPorOld != null && !creadoPorOld.equals(creadoPorNew)) {
                creadoPorOld.getComercialClientesList().remove(comercialClientes);
                creadoPorOld = em.merge(creadoPorOld);
            }
            if (creadoPorNew != null && !creadoPorNew.equals(creadoPorOld)) {
                creadoPorNew.getComercialClientesList().add(comercialClientes);
                creadoPorNew = em.merge(creadoPorNew);
            }
            for (TelcoInfo telcoInfoListNewTelcoInfo : telcoInfoListNew) {
                if (!telcoInfoListOld.contains(telcoInfoListNewTelcoInfo)) {
                    ComercialClientes oldFkComercialClientesOfTelcoInfoListNewTelcoInfo = telcoInfoListNewTelcoInfo.getFkComercialClientes();
                    telcoInfoListNewTelcoInfo.setFkComercialClientes(comercialClientes);
                    telcoInfoListNewTelcoInfo = em.merge(telcoInfoListNewTelcoInfo);
                    if (oldFkComercialClientesOfTelcoInfoListNewTelcoInfo != null && !oldFkComercialClientesOfTelcoInfoListNewTelcoInfo.equals(comercialClientes)) {
                        oldFkComercialClientesOfTelcoInfoListNewTelcoInfo.getTelcoInfoList().remove(telcoInfoListNewTelcoInfo);
                        oldFkComercialClientesOfTelcoInfoListNewTelcoInfo = em.merge(oldFkComercialClientesOfTelcoInfoListNewTelcoInfo);
                    }
                }
            }
            for (ComercialContratos comercialContratosListNewComercialContratos : comercialContratosListNew) {
                if (!comercialContratosListOld.contains(comercialContratosListNewComercialContratos)) {
                    ComercialClientes oldFkComercialClientesOfComercialContratosListNewComercialContratos = comercialContratosListNewComercialContratos.getFkComercialClientes();
                    comercialContratosListNewComercialContratos.setFkComercialClientes(comercialClientes);
                    comercialContratosListNewComercialContratos = em.merge(comercialContratosListNewComercialContratos);
                    if (oldFkComercialClientesOfComercialContratosListNewComercialContratos != null && !oldFkComercialClientesOfComercialContratosListNewComercialContratos.equals(comercialClientes)) {
                        oldFkComercialClientesOfComercialContratosListNewComercialContratos.getComercialContratosList().remove(comercialContratosListNewComercialContratos);
                        oldFkComercialClientesOfComercialContratosListNewComercialContratos = em.merge(oldFkComercialClientesOfComercialContratosListNewComercialContratos);
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
                Long id = comercialClientes.getIdClientes();
                if (findComercialClientes(id) == null) {
                    throw new NonexistentEntityException("The comercialClientes with id " + id + " no longer exists.");
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
            ComercialClientes comercialClientes;
            try {
                comercialClientes = em.getReference(ComercialClientes.class, id);
                comercialClientes.getIdClientes();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comercialClientes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<TelcoInfo> telcoInfoListOrphanCheck = comercialClientes.getTelcoInfoList();
            for (TelcoInfo telcoInfoListOrphanCheckTelcoInfo : telcoInfoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ComercialClientes (" + comercialClientes + ") cannot be destroyed since the TelcoInfo " + telcoInfoListOrphanCheckTelcoInfo + " in its telcoInfoList field has a non-nullable fkComercialClientes field.");
            }
            List<ComercialContratos> comercialContratosListOrphanCheck = comercialClientes.getComercialContratosList();
            for (ComercialContratos comercialContratosListOrphanCheckComercialContratos : comercialContratosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ComercialClientes (" + comercialClientes + ") cannot be destroyed since the ComercialContratos " + comercialContratosListOrphanCheckComercialContratos + " in its comercialContratosList field has a non-nullable fkComercialClientes field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            GestionFuncionarios modificadoPor = comercialClientes.getModificadoPor();
            if (modificadoPor != null) {
                modificadoPor.getComercialClientesList().remove(comercialClientes);
                modificadoPor = em.merge(modificadoPor);
            }
            GestionFuncionarios creadoPor = comercialClientes.getCreadoPor();
            if (creadoPor != null) {
                creadoPor.getComercialClientesList().remove(comercialClientes);
                creadoPor = em.merge(creadoPor);
            }
            em.remove(comercialClientes);
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

    public List<ComercialClientes> findComercialClientesEntities() {
        return findComercialClientesEntities(true, -1, -1);
    }

    public List<ComercialClientes> findComercialClientesEntities(int maxResults, int firstResult) {
        return findComercialClientesEntities(false, maxResults, firstResult);
    }

    private List<ComercialClientes> findComercialClientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ComercialClientes.class));
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

    public ComercialClientes findComercialClientes(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ComercialClientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getComercialClientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ComercialClientes> rt = cq.from(ComercialClientes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
