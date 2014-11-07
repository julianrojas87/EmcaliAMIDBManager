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
import emcali.ami.persistence.entity.GestionFuncionarios;
import emcali.ami.persistence.entity.ComercialClientes;
import java.util.ArrayList;
import java.util.List;
import emcali.ami.persistence.entity.GestionPerfiles;
import emcali.ami.persistence.entity.GestionUsuarios;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class GestionFuncionariosJpaController implements Serializable {

    public GestionFuncionariosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GestionFuncionarios gestionFuncionarios) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (gestionFuncionarios.getComercialClientesList() == null) {
            gestionFuncionarios.setComercialClientesList(new ArrayList<ComercialClientes>());
        }
        if (gestionFuncionarios.getComercialClientesList1() == null) {
            gestionFuncionarios.setComercialClientesList1(new ArrayList<ComercialClientes>());
        }
        if (gestionFuncionarios.getGestionFuncionariosList() == null) {
            gestionFuncionarios.setGestionFuncionariosList(new ArrayList<GestionFuncionarios>());
        }
        if (gestionFuncionarios.getGestionFuncionariosList1() == null) {
            gestionFuncionarios.setGestionFuncionariosList1(new ArrayList<GestionFuncionarios>());
        }
        if (gestionFuncionarios.getGestionPerfilesList() == null) {
            gestionFuncionarios.setGestionPerfilesList(new ArrayList<GestionPerfiles>());
        }
        if (gestionFuncionarios.getGestionPerfilesList1() == null) {
            gestionFuncionarios.setGestionPerfilesList1(new ArrayList<GestionPerfiles>());
        }
        if (gestionFuncionarios.getGestionUsuariosList() == null) {
            gestionFuncionarios.setGestionUsuariosList(new ArrayList<GestionUsuarios>());
        }
        if (gestionFuncionarios.getGestionUsuariosList1() == null) {
            gestionFuncionarios.setGestionUsuariosList1(new ArrayList<GestionUsuarios>());
        }
        if (gestionFuncionarios.getGestionUsuariosList2() == null) {
            gestionFuncionarios.setGestionUsuariosList2(new ArrayList<GestionUsuarios>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GestionFuncionarios modificadoPor = gestionFuncionarios.getModificadoPor();
            if (modificadoPor != null) {
                modificadoPor = em.getReference(modificadoPor.getClass(), modificadoPor.getIdFuncionarios());
                gestionFuncionarios.setModificadoPor(modificadoPor);
            }
            GestionFuncionarios creadoPor = gestionFuncionarios.getCreadoPor();
            if (creadoPor != null) {
                creadoPor = em.getReference(creadoPor.getClass(), creadoPor.getIdFuncionarios());
                gestionFuncionarios.setCreadoPor(creadoPor);
            }
            List<ComercialClientes> attachedComercialClientesList = new ArrayList<ComercialClientes>();
            for (ComercialClientes comercialClientesListComercialClientesToAttach : gestionFuncionarios.getComercialClientesList()) {
                comercialClientesListComercialClientesToAttach = em.getReference(comercialClientesListComercialClientesToAttach.getClass(), comercialClientesListComercialClientesToAttach.getIdClientes());
                attachedComercialClientesList.add(comercialClientesListComercialClientesToAttach);
            }
            gestionFuncionarios.setComercialClientesList(attachedComercialClientesList);
            List<ComercialClientes> attachedComercialClientesList1 = new ArrayList<ComercialClientes>();
            for (ComercialClientes comercialClientesList1ComercialClientesToAttach : gestionFuncionarios.getComercialClientesList1()) {
                comercialClientesList1ComercialClientesToAttach = em.getReference(comercialClientesList1ComercialClientesToAttach.getClass(), comercialClientesList1ComercialClientesToAttach.getIdClientes());
                attachedComercialClientesList1.add(comercialClientesList1ComercialClientesToAttach);
            }
            gestionFuncionarios.setComercialClientesList1(attachedComercialClientesList1);
            List<GestionFuncionarios> attachedGestionFuncionariosList = new ArrayList<GestionFuncionarios>();
            for (GestionFuncionarios gestionFuncionariosListGestionFuncionariosToAttach : gestionFuncionarios.getGestionFuncionariosList()) {
                gestionFuncionariosListGestionFuncionariosToAttach = em.getReference(gestionFuncionariosListGestionFuncionariosToAttach.getClass(), gestionFuncionariosListGestionFuncionariosToAttach.getIdFuncionarios());
                attachedGestionFuncionariosList.add(gestionFuncionariosListGestionFuncionariosToAttach);
            }
            gestionFuncionarios.setGestionFuncionariosList(attachedGestionFuncionariosList);
            List<GestionFuncionarios> attachedGestionFuncionariosList1 = new ArrayList<GestionFuncionarios>();
            for (GestionFuncionarios gestionFuncionariosList1GestionFuncionariosToAttach : gestionFuncionarios.getGestionFuncionariosList1()) {
                gestionFuncionariosList1GestionFuncionariosToAttach = em.getReference(gestionFuncionariosList1GestionFuncionariosToAttach.getClass(), gestionFuncionariosList1GestionFuncionariosToAttach.getIdFuncionarios());
                attachedGestionFuncionariosList1.add(gestionFuncionariosList1GestionFuncionariosToAttach);
            }
            gestionFuncionarios.setGestionFuncionariosList1(attachedGestionFuncionariosList1);
            List<GestionPerfiles> attachedGestionPerfilesList = new ArrayList<GestionPerfiles>();
            for (GestionPerfiles gestionPerfilesListGestionPerfilesToAttach : gestionFuncionarios.getGestionPerfilesList()) {
                gestionPerfilesListGestionPerfilesToAttach = em.getReference(gestionPerfilesListGestionPerfilesToAttach.getClass(), gestionPerfilesListGestionPerfilesToAttach.getIdPerfiles());
                attachedGestionPerfilesList.add(gestionPerfilesListGestionPerfilesToAttach);
            }
            gestionFuncionarios.setGestionPerfilesList(attachedGestionPerfilesList);
            List<GestionPerfiles> attachedGestionPerfilesList1 = new ArrayList<GestionPerfiles>();
            for (GestionPerfiles gestionPerfilesList1GestionPerfilesToAttach : gestionFuncionarios.getGestionPerfilesList1()) {
                gestionPerfilesList1GestionPerfilesToAttach = em.getReference(gestionPerfilesList1GestionPerfilesToAttach.getClass(), gestionPerfilesList1GestionPerfilesToAttach.getIdPerfiles());
                attachedGestionPerfilesList1.add(gestionPerfilesList1GestionPerfilesToAttach);
            }
            gestionFuncionarios.setGestionPerfilesList1(attachedGestionPerfilesList1);
            List<GestionUsuarios> attachedGestionUsuariosList = new ArrayList<GestionUsuarios>();
            for (GestionUsuarios gestionUsuariosListGestionUsuariosToAttach : gestionFuncionarios.getGestionUsuariosList()) {
                gestionUsuariosListGestionUsuariosToAttach = em.getReference(gestionUsuariosListGestionUsuariosToAttach.getClass(), gestionUsuariosListGestionUsuariosToAttach.getIdUsuarios());
                attachedGestionUsuariosList.add(gestionUsuariosListGestionUsuariosToAttach);
            }
            gestionFuncionarios.setGestionUsuariosList(attachedGestionUsuariosList);
            List<GestionUsuarios> attachedGestionUsuariosList1 = new ArrayList<GestionUsuarios>();
            for (GestionUsuarios gestionUsuariosList1GestionUsuariosToAttach : gestionFuncionarios.getGestionUsuariosList1()) {
                gestionUsuariosList1GestionUsuariosToAttach = em.getReference(gestionUsuariosList1GestionUsuariosToAttach.getClass(), gestionUsuariosList1GestionUsuariosToAttach.getIdUsuarios());
                attachedGestionUsuariosList1.add(gestionUsuariosList1GestionUsuariosToAttach);
            }
            gestionFuncionarios.setGestionUsuariosList1(attachedGestionUsuariosList1);
            List<GestionUsuarios> attachedGestionUsuariosList2 = new ArrayList<GestionUsuarios>();
            for (GestionUsuarios gestionUsuariosList2GestionUsuariosToAttach : gestionFuncionarios.getGestionUsuariosList2()) {
                gestionUsuariosList2GestionUsuariosToAttach = em.getReference(gestionUsuariosList2GestionUsuariosToAttach.getClass(), gestionUsuariosList2GestionUsuariosToAttach.getIdUsuarios());
                attachedGestionUsuariosList2.add(gestionUsuariosList2GestionUsuariosToAttach);
            }
            gestionFuncionarios.setGestionUsuariosList2(attachedGestionUsuariosList2);
            em.persist(gestionFuncionarios);
            if (modificadoPor != null) {
                modificadoPor.getGestionFuncionariosList().add(gestionFuncionarios);
                modificadoPor = em.merge(modificadoPor);
            }
            if (creadoPor != null) {
                creadoPor.getGestionFuncionariosList().add(gestionFuncionarios);
                creadoPor = em.merge(creadoPor);
            }
            for (ComercialClientes comercialClientesListComercialClientes : gestionFuncionarios.getComercialClientesList()) {
                GestionFuncionarios oldModificadoPorOfComercialClientesListComercialClientes = comercialClientesListComercialClientes.getModificadoPor();
                comercialClientesListComercialClientes.setModificadoPor(gestionFuncionarios);
                comercialClientesListComercialClientes = em.merge(comercialClientesListComercialClientes);
                if (oldModificadoPorOfComercialClientesListComercialClientes != null) {
                    oldModificadoPorOfComercialClientesListComercialClientes.getComercialClientesList().remove(comercialClientesListComercialClientes);
                    oldModificadoPorOfComercialClientesListComercialClientes = em.merge(oldModificadoPorOfComercialClientesListComercialClientes);
                }
            }
            for (ComercialClientes comercialClientesList1ComercialClientes : gestionFuncionarios.getComercialClientesList1()) {
                GestionFuncionarios oldCreadoPorOfComercialClientesList1ComercialClientes = comercialClientesList1ComercialClientes.getCreadoPor();
                comercialClientesList1ComercialClientes.setCreadoPor(gestionFuncionarios);
                comercialClientesList1ComercialClientes = em.merge(comercialClientesList1ComercialClientes);
                if (oldCreadoPorOfComercialClientesList1ComercialClientes != null) {
                    oldCreadoPorOfComercialClientesList1ComercialClientes.getComercialClientesList1().remove(comercialClientesList1ComercialClientes);
                    oldCreadoPorOfComercialClientesList1ComercialClientes = em.merge(oldCreadoPorOfComercialClientesList1ComercialClientes);
                }
            }
            for (GestionFuncionarios gestionFuncionariosListGestionFuncionarios : gestionFuncionarios.getGestionFuncionariosList()) {
                GestionFuncionarios oldModificadoPorOfGestionFuncionariosListGestionFuncionarios = gestionFuncionariosListGestionFuncionarios.getModificadoPor();
                gestionFuncionariosListGestionFuncionarios.setModificadoPor(gestionFuncionarios);
                gestionFuncionariosListGestionFuncionarios = em.merge(gestionFuncionariosListGestionFuncionarios);
                if (oldModificadoPorOfGestionFuncionariosListGestionFuncionarios != null) {
                    oldModificadoPorOfGestionFuncionariosListGestionFuncionarios.getGestionFuncionariosList().remove(gestionFuncionariosListGestionFuncionarios);
                    oldModificadoPorOfGestionFuncionariosListGestionFuncionarios = em.merge(oldModificadoPorOfGestionFuncionariosListGestionFuncionarios);
                }
            }
            for (GestionFuncionarios gestionFuncionariosList1GestionFuncionarios : gestionFuncionarios.getGestionFuncionariosList1()) {
                GestionFuncionarios oldCreadoPorOfGestionFuncionariosList1GestionFuncionarios = gestionFuncionariosList1GestionFuncionarios.getCreadoPor();
                gestionFuncionariosList1GestionFuncionarios.setCreadoPor(gestionFuncionarios);
                gestionFuncionariosList1GestionFuncionarios = em.merge(gestionFuncionariosList1GestionFuncionarios);
                if (oldCreadoPorOfGestionFuncionariosList1GestionFuncionarios != null) {
                    oldCreadoPorOfGestionFuncionariosList1GestionFuncionarios.getGestionFuncionariosList1().remove(gestionFuncionariosList1GestionFuncionarios);
                    oldCreadoPorOfGestionFuncionariosList1GestionFuncionarios = em.merge(oldCreadoPorOfGestionFuncionariosList1GestionFuncionarios);
                }
            }
            for (GestionPerfiles gestionPerfilesListGestionPerfiles : gestionFuncionarios.getGestionPerfilesList()) {
                GestionFuncionarios oldModificadoPorOfGestionPerfilesListGestionPerfiles = gestionPerfilesListGestionPerfiles.getModificadoPor();
                gestionPerfilesListGestionPerfiles.setModificadoPor(gestionFuncionarios);
                gestionPerfilesListGestionPerfiles = em.merge(gestionPerfilesListGestionPerfiles);
                if (oldModificadoPorOfGestionPerfilesListGestionPerfiles != null) {
                    oldModificadoPorOfGestionPerfilesListGestionPerfiles.getGestionPerfilesList().remove(gestionPerfilesListGestionPerfiles);
                    oldModificadoPorOfGestionPerfilesListGestionPerfiles = em.merge(oldModificadoPorOfGestionPerfilesListGestionPerfiles);
                }
            }
            for (GestionPerfiles gestionPerfilesList1GestionPerfiles : gestionFuncionarios.getGestionPerfilesList1()) {
                GestionFuncionarios oldCreadoPorOfGestionPerfilesList1GestionPerfiles = gestionPerfilesList1GestionPerfiles.getCreadoPor();
                gestionPerfilesList1GestionPerfiles.setCreadoPor(gestionFuncionarios);
                gestionPerfilesList1GestionPerfiles = em.merge(gestionPerfilesList1GestionPerfiles);
                if (oldCreadoPorOfGestionPerfilesList1GestionPerfiles != null) {
                    oldCreadoPorOfGestionPerfilesList1GestionPerfiles.getGestionPerfilesList1().remove(gestionPerfilesList1GestionPerfiles);
                    oldCreadoPorOfGestionPerfilesList1GestionPerfiles = em.merge(oldCreadoPorOfGestionPerfilesList1GestionPerfiles);
                }
            }
            for (GestionUsuarios gestionUsuariosListGestionUsuarios : gestionFuncionarios.getGestionUsuariosList()) {
                GestionFuncionarios oldFkGestionFuncionariosOfGestionUsuariosListGestionUsuarios = gestionUsuariosListGestionUsuarios.getFkGestionFuncionarios();
                gestionUsuariosListGestionUsuarios.setFkGestionFuncionarios(gestionFuncionarios);
                gestionUsuariosListGestionUsuarios = em.merge(gestionUsuariosListGestionUsuarios);
                if (oldFkGestionFuncionariosOfGestionUsuariosListGestionUsuarios != null) {
                    oldFkGestionFuncionariosOfGestionUsuariosListGestionUsuarios.getGestionUsuariosList().remove(gestionUsuariosListGestionUsuarios);
                    oldFkGestionFuncionariosOfGestionUsuariosListGestionUsuarios = em.merge(oldFkGestionFuncionariosOfGestionUsuariosListGestionUsuarios);
                }
            }
            for (GestionUsuarios gestionUsuariosList1GestionUsuarios : gestionFuncionarios.getGestionUsuariosList1()) {
                GestionFuncionarios oldCreadoPorOfGestionUsuariosList1GestionUsuarios = gestionUsuariosList1GestionUsuarios.getCreadoPor();
                gestionUsuariosList1GestionUsuarios.setCreadoPor(gestionFuncionarios);
                gestionUsuariosList1GestionUsuarios = em.merge(gestionUsuariosList1GestionUsuarios);
                if (oldCreadoPorOfGestionUsuariosList1GestionUsuarios != null) {
                    oldCreadoPorOfGestionUsuariosList1GestionUsuarios.getGestionUsuariosList1().remove(gestionUsuariosList1GestionUsuarios);
                    oldCreadoPorOfGestionUsuariosList1GestionUsuarios = em.merge(oldCreadoPorOfGestionUsuariosList1GestionUsuarios);
                }
            }
            for (GestionUsuarios gestionUsuariosList2GestionUsuarios : gestionFuncionarios.getGestionUsuariosList2()) {
                GestionFuncionarios oldModificadoPorOfGestionUsuariosList2GestionUsuarios = gestionUsuariosList2GestionUsuarios.getModificadoPor();
                gestionUsuariosList2GestionUsuarios.setModificadoPor(gestionFuncionarios);
                gestionUsuariosList2GestionUsuarios = em.merge(gestionUsuariosList2GestionUsuarios);
                if (oldModificadoPorOfGestionUsuariosList2GestionUsuarios != null) {
                    oldModificadoPorOfGestionUsuariosList2GestionUsuarios.getGestionUsuariosList2().remove(gestionUsuariosList2GestionUsuarios);
                    oldModificadoPorOfGestionUsuariosList2GestionUsuarios = em.merge(oldModificadoPorOfGestionUsuariosList2GestionUsuarios);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findGestionFuncionarios(gestionFuncionarios.getIdFuncionarios()) != null) {
                throw new PreexistingEntityException("GestionFuncionarios " + gestionFuncionarios + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GestionFuncionarios gestionFuncionarios) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GestionFuncionarios persistentGestionFuncionarios = em.find(GestionFuncionarios.class, gestionFuncionarios.getIdFuncionarios());
            GestionFuncionarios modificadoPorOld = persistentGestionFuncionarios.getModificadoPor();
            GestionFuncionarios modificadoPorNew = gestionFuncionarios.getModificadoPor();
            GestionFuncionarios creadoPorOld = persistentGestionFuncionarios.getCreadoPor();
            GestionFuncionarios creadoPorNew = gestionFuncionarios.getCreadoPor();
            List<ComercialClientes> comercialClientesListOld = persistentGestionFuncionarios.getComercialClientesList();
            List<ComercialClientes> comercialClientesListNew = gestionFuncionarios.getComercialClientesList();
            List<ComercialClientes> comercialClientesList1Old = persistentGestionFuncionarios.getComercialClientesList1();
            List<ComercialClientes> comercialClientesList1New = gestionFuncionarios.getComercialClientesList1();
            List<GestionFuncionarios> gestionFuncionariosListOld = persistentGestionFuncionarios.getGestionFuncionariosList();
            List<GestionFuncionarios> gestionFuncionariosListNew = gestionFuncionarios.getGestionFuncionariosList();
            List<GestionFuncionarios> gestionFuncionariosList1Old = persistentGestionFuncionarios.getGestionFuncionariosList1();
            List<GestionFuncionarios> gestionFuncionariosList1New = gestionFuncionarios.getGestionFuncionariosList1();
            List<GestionPerfiles> gestionPerfilesListOld = persistentGestionFuncionarios.getGestionPerfilesList();
            List<GestionPerfiles> gestionPerfilesListNew = gestionFuncionarios.getGestionPerfilesList();
            List<GestionPerfiles> gestionPerfilesList1Old = persistentGestionFuncionarios.getGestionPerfilesList1();
            List<GestionPerfiles> gestionPerfilesList1New = gestionFuncionarios.getGestionPerfilesList1();
            List<GestionUsuarios> gestionUsuariosListOld = persistentGestionFuncionarios.getGestionUsuariosList();
            List<GestionUsuarios> gestionUsuariosListNew = gestionFuncionarios.getGestionUsuariosList();
            List<GestionUsuarios> gestionUsuariosList1Old = persistentGestionFuncionarios.getGestionUsuariosList1();
            List<GestionUsuarios> gestionUsuariosList1New = gestionFuncionarios.getGestionUsuariosList1();
            List<GestionUsuarios> gestionUsuariosList2Old = persistentGestionFuncionarios.getGestionUsuariosList2();
            List<GestionUsuarios> gestionUsuariosList2New = gestionFuncionarios.getGestionUsuariosList2();
            List<String> illegalOrphanMessages = null;
            for (ComercialClientes comercialClientesListOldComercialClientes : comercialClientesListOld) {
                if (!comercialClientesListNew.contains(comercialClientesListOldComercialClientes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ComercialClientes " + comercialClientesListOldComercialClientes + " since its modificadoPor field is not nullable.");
                }
            }
            for (ComercialClientes comercialClientesList1OldComercialClientes : comercialClientesList1Old) {
                if (!comercialClientesList1New.contains(comercialClientesList1OldComercialClientes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ComercialClientes " + comercialClientesList1OldComercialClientes + " since its creadoPor field is not nullable.");
                }
            }
            for (GestionFuncionarios gestionFuncionariosListOldGestionFuncionarios : gestionFuncionariosListOld) {
                if (!gestionFuncionariosListNew.contains(gestionFuncionariosListOldGestionFuncionarios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GestionFuncionarios " + gestionFuncionariosListOldGestionFuncionarios + " since its modificadoPor field is not nullable.");
                }
            }
            for (GestionFuncionarios gestionFuncionariosList1OldGestionFuncionarios : gestionFuncionariosList1Old) {
                if (!gestionFuncionariosList1New.contains(gestionFuncionariosList1OldGestionFuncionarios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GestionFuncionarios " + gestionFuncionariosList1OldGestionFuncionarios + " since its creadoPor field is not nullable.");
                }
            }
            for (GestionPerfiles gestionPerfilesListOldGestionPerfiles : gestionPerfilesListOld) {
                if (!gestionPerfilesListNew.contains(gestionPerfilesListOldGestionPerfiles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GestionPerfiles " + gestionPerfilesListOldGestionPerfiles + " since its modificadoPor field is not nullable.");
                }
            }
            for (GestionPerfiles gestionPerfilesList1OldGestionPerfiles : gestionPerfilesList1Old) {
                if (!gestionPerfilesList1New.contains(gestionPerfilesList1OldGestionPerfiles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GestionPerfiles " + gestionPerfilesList1OldGestionPerfiles + " since its creadoPor field is not nullable.");
                }
            }
            for (GestionUsuarios gestionUsuariosListOldGestionUsuarios : gestionUsuariosListOld) {
                if (!gestionUsuariosListNew.contains(gestionUsuariosListOldGestionUsuarios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GestionUsuarios " + gestionUsuariosListOldGestionUsuarios + " since its fkGestionFuncionarios field is not nullable.");
                }
            }
            for (GestionUsuarios gestionUsuariosList1OldGestionUsuarios : gestionUsuariosList1Old) {
                if (!gestionUsuariosList1New.contains(gestionUsuariosList1OldGestionUsuarios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GestionUsuarios " + gestionUsuariosList1OldGestionUsuarios + " since its creadoPor field is not nullable.");
                }
            }
            for (GestionUsuarios gestionUsuariosList2OldGestionUsuarios : gestionUsuariosList2Old) {
                if (!gestionUsuariosList2New.contains(gestionUsuariosList2OldGestionUsuarios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GestionUsuarios " + gestionUsuariosList2OldGestionUsuarios + " since its modificadoPor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (modificadoPorNew != null) {
                modificadoPorNew = em.getReference(modificadoPorNew.getClass(), modificadoPorNew.getIdFuncionarios());
                gestionFuncionarios.setModificadoPor(modificadoPorNew);
            }
            if (creadoPorNew != null) {
                creadoPorNew = em.getReference(creadoPorNew.getClass(), creadoPorNew.getIdFuncionarios());
                gestionFuncionarios.setCreadoPor(creadoPorNew);
            }
            List<ComercialClientes> attachedComercialClientesListNew = new ArrayList<ComercialClientes>();
            for (ComercialClientes comercialClientesListNewComercialClientesToAttach : comercialClientesListNew) {
                comercialClientesListNewComercialClientesToAttach = em.getReference(comercialClientesListNewComercialClientesToAttach.getClass(), comercialClientesListNewComercialClientesToAttach.getIdClientes());
                attachedComercialClientesListNew.add(comercialClientesListNewComercialClientesToAttach);
            }
            comercialClientesListNew = attachedComercialClientesListNew;
            gestionFuncionarios.setComercialClientesList(comercialClientesListNew);
            List<ComercialClientes> attachedComercialClientesList1New = new ArrayList<ComercialClientes>();
            for (ComercialClientes comercialClientesList1NewComercialClientesToAttach : comercialClientesList1New) {
                comercialClientesList1NewComercialClientesToAttach = em.getReference(comercialClientesList1NewComercialClientesToAttach.getClass(), comercialClientesList1NewComercialClientesToAttach.getIdClientes());
                attachedComercialClientesList1New.add(comercialClientesList1NewComercialClientesToAttach);
            }
            comercialClientesList1New = attachedComercialClientesList1New;
            gestionFuncionarios.setComercialClientesList1(comercialClientesList1New);
            List<GestionFuncionarios> attachedGestionFuncionariosListNew = new ArrayList<GestionFuncionarios>();
            for (GestionFuncionarios gestionFuncionariosListNewGestionFuncionariosToAttach : gestionFuncionariosListNew) {
                gestionFuncionariosListNewGestionFuncionariosToAttach = em.getReference(gestionFuncionariosListNewGestionFuncionariosToAttach.getClass(), gestionFuncionariosListNewGestionFuncionariosToAttach.getIdFuncionarios());
                attachedGestionFuncionariosListNew.add(gestionFuncionariosListNewGestionFuncionariosToAttach);
            }
            gestionFuncionariosListNew = attachedGestionFuncionariosListNew;
            gestionFuncionarios.setGestionFuncionariosList(gestionFuncionariosListNew);
            List<GestionFuncionarios> attachedGestionFuncionariosList1New = new ArrayList<GestionFuncionarios>();
            for (GestionFuncionarios gestionFuncionariosList1NewGestionFuncionariosToAttach : gestionFuncionariosList1New) {
                gestionFuncionariosList1NewGestionFuncionariosToAttach = em.getReference(gestionFuncionariosList1NewGestionFuncionariosToAttach.getClass(), gestionFuncionariosList1NewGestionFuncionariosToAttach.getIdFuncionarios());
                attachedGestionFuncionariosList1New.add(gestionFuncionariosList1NewGestionFuncionariosToAttach);
            }
            gestionFuncionariosList1New = attachedGestionFuncionariosList1New;
            gestionFuncionarios.setGestionFuncionariosList1(gestionFuncionariosList1New);
            List<GestionPerfiles> attachedGestionPerfilesListNew = new ArrayList<GestionPerfiles>();
            for (GestionPerfiles gestionPerfilesListNewGestionPerfilesToAttach : gestionPerfilesListNew) {
                gestionPerfilesListNewGestionPerfilesToAttach = em.getReference(gestionPerfilesListNewGestionPerfilesToAttach.getClass(), gestionPerfilesListNewGestionPerfilesToAttach.getIdPerfiles());
                attachedGestionPerfilesListNew.add(gestionPerfilesListNewGestionPerfilesToAttach);
            }
            gestionPerfilesListNew = attachedGestionPerfilesListNew;
            gestionFuncionarios.setGestionPerfilesList(gestionPerfilesListNew);
            List<GestionPerfiles> attachedGestionPerfilesList1New = new ArrayList<GestionPerfiles>();
            for (GestionPerfiles gestionPerfilesList1NewGestionPerfilesToAttach : gestionPerfilesList1New) {
                gestionPerfilesList1NewGestionPerfilesToAttach = em.getReference(gestionPerfilesList1NewGestionPerfilesToAttach.getClass(), gestionPerfilesList1NewGestionPerfilesToAttach.getIdPerfiles());
                attachedGestionPerfilesList1New.add(gestionPerfilesList1NewGestionPerfilesToAttach);
            }
            gestionPerfilesList1New = attachedGestionPerfilesList1New;
            gestionFuncionarios.setGestionPerfilesList1(gestionPerfilesList1New);
            List<GestionUsuarios> attachedGestionUsuariosListNew = new ArrayList<GestionUsuarios>();
            for (GestionUsuarios gestionUsuariosListNewGestionUsuariosToAttach : gestionUsuariosListNew) {
                gestionUsuariosListNewGestionUsuariosToAttach = em.getReference(gestionUsuariosListNewGestionUsuariosToAttach.getClass(), gestionUsuariosListNewGestionUsuariosToAttach.getIdUsuarios());
                attachedGestionUsuariosListNew.add(gestionUsuariosListNewGestionUsuariosToAttach);
            }
            gestionUsuariosListNew = attachedGestionUsuariosListNew;
            gestionFuncionarios.setGestionUsuariosList(gestionUsuariosListNew);
            List<GestionUsuarios> attachedGestionUsuariosList1New = new ArrayList<GestionUsuarios>();
            for (GestionUsuarios gestionUsuariosList1NewGestionUsuariosToAttach : gestionUsuariosList1New) {
                gestionUsuariosList1NewGestionUsuariosToAttach = em.getReference(gestionUsuariosList1NewGestionUsuariosToAttach.getClass(), gestionUsuariosList1NewGestionUsuariosToAttach.getIdUsuarios());
                attachedGestionUsuariosList1New.add(gestionUsuariosList1NewGestionUsuariosToAttach);
            }
            gestionUsuariosList1New = attachedGestionUsuariosList1New;
            gestionFuncionarios.setGestionUsuariosList1(gestionUsuariosList1New);
            List<GestionUsuarios> attachedGestionUsuariosList2New = new ArrayList<GestionUsuarios>();
            for (GestionUsuarios gestionUsuariosList2NewGestionUsuariosToAttach : gestionUsuariosList2New) {
                gestionUsuariosList2NewGestionUsuariosToAttach = em.getReference(gestionUsuariosList2NewGestionUsuariosToAttach.getClass(), gestionUsuariosList2NewGestionUsuariosToAttach.getIdUsuarios());
                attachedGestionUsuariosList2New.add(gestionUsuariosList2NewGestionUsuariosToAttach);
            }
            gestionUsuariosList2New = attachedGestionUsuariosList2New;
            gestionFuncionarios.setGestionUsuariosList2(gestionUsuariosList2New);
            gestionFuncionarios = em.merge(gestionFuncionarios);
            if (modificadoPorOld != null && !modificadoPorOld.equals(modificadoPorNew)) {
                modificadoPorOld.getGestionFuncionariosList().remove(gestionFuncionarios);
                modificadoPorOld = em.merge(modificadoPorOld);
            }
            if (modificadoPorNew != null && !modificadoPorNew.equals(modificadoPorOld)) {
                modificadoPorNew.getGestionFuncionariosList().add(gestionFuncionarios);
                modificadoPorNew = em.merge(modificadoPorNew);
            }
            if (creadoPorOld != null && !creadoPorOld.equals(creadoPorNew)) {
                creadoPorOld.getGestionFuncionariosList().remove(gestionFuncionarios);
                creadoPorOld = em.merge(creadoPorOld);
            }
            if (creadoPorNew != null && !creadoPorNew.equals(creadoPorOld)) {
                creadoPorNew.getGestionFuncionariosList().add(gestionFuncionarios);
                creadoPorNew = em.merge(creadoPorNew);
            }
            for (ComercialClientes comercialClientesListNewComercialClientes : comercialClientesListNew) {
                if (!comercialClientesListOld.contains(comercialClientesListNewComercialClientes)) {
                    GestionFuncionarios oldModificadoPorOfComercialClientesListNewComercialClientes = comercialClientesListNewComercialClientes.getModificadoPor();
                    comercialClientesListNewComercialClientes.setModificadoPor(gestionFuncionarios);
                    comercialClientesListNewComercialClientes = em.merge(comercialClientesListNewComercialClientes);
                    if (oldModificadoPorOfComercialClientesListNewComercialClientes != null && !oldModificadoPorOfComercialClientesListNewComercialClientes.equals(gestionFuncionarios)) {
                        oldModificadoPorOfComercialClientesListNewComercialClientes.getComercialClientesList().remove(comercialClientesListNewComercialClientes);
                        oldModificadoPorOfComercialClientesListNewComercialClientes = em.merge(oldModificadoPorOfComercialClientesListNewComercialClientes);
                    }
                }
            }
            for (ComercialClientes comercialClientesList1NewComercialClientes : comercialClientesList1New) {
                if (!comercialClientesList1Old.contains(comercialClientesList1NewComercialClientes)) {
                    GestionFuncionarios oldCreadoPorOfComercialClientesList1NewComercialClientes = comercialClientesList1NewComercialClientes.getCreadoPor();
                    comercialClientesList1NewComercialClientes.setCreadoPor(gestionFuncionarios);
                    comercialClientesList1NewComercialClientes = em.merge(comercialClientesList1NewComercialClientes);
                    if (oldCreadoPorOfComercialClientesList1NewComercialClientes != null && !oldCreadoPorOfComercialClientesList1NewComercialClientes.equals(gestionFuncionarios)) {
                        oldCreadoPorOfComercialClientesList1NewComercialClientes.getComercialClientesList1().remove(comercialClientesList1NewComercialClientes);
                        oldCreadoPorOfComercialClientesList1NewComercialClientes = em.merge(oldCreadoPorOfComercialClientesList1NewComercialClientes);
                    }
                }
            }
            for (GestionFuncionarios gestionFuncionariosListNewGestionFuncionarios : gestionFuncionariosListNew) {
                if (!gestionFuncionariosListOld.contains(gestionFuncionariosListNewGestionFuncionarios)) {
                    GestionFuncionarios oldModificadoPorOfGestionFuncionariosListNewGestionFuncionarios = gestionFuncionariosListNewGestionFuncionarios.getModificadoPor();
                    gestionFuncionariosListNewGestionFuncionarios.setModificadoPor(gestionFuncionarios);
                    gestionFuncionariosListNewGestionFuncionarios = em.merge(gestionFuncionariosListNewGestionFuncionarios);
                    if (oldModificadoPorOfGestionFuncionariosListNewGestionFuncionarios != null && !oldModificadoPorOfGestionFuncionariosListNewGestionFuncionarios.equals(gestionFuncionarios)) {
                        oldModificadoPorOfGestionFuncionariosListNewGestionFuncionarios.getGestionFuncionariosList().remove(gestionFuncionariosListNewGestionFuncionarios);
                        oldModificadoPorOfGestionFuncionariosListNewGestionFuncionarios = em.merge(oldModificadoPorOfGestionFuncionariosListNewGestionFuncionarios);
                    }
                }
            }
            for (GestionFuncionarios gestionFuncionariosList1NewGestionFuncionarios : gestionFuncionariosList1New) {
                if (!gestionFuncionariosList1Old.contains(gestionFuncionariosList1NewGestionFuncionarios)) {
                    GestionFuncionarios oldCreadoPorOfGestionFuncionariosList1NewGestionFuncionarios = gestionFuncionariosList1NewGestionFuncionarios.getCreadoPor();
                    gestionFuncionariosList1NewGestionFuncionarios.setCreadoPor(gestionFuncionarios);
                    gestionFuncionariosList1NewGestionFuncionarios = em.merge(gestionFuncionariosList1NewGestionFuncionarios);
                    if (oldCreadoPorOfGestionFuncionariosList1NewGestionFuncionarios != null && !oldCreadoPorOfGestionFuncionariosList1NewGestionFuncionarios.equals(gestionFuncionarios)) {
                        oldCreadoPorOfGestionFuncionariosList1NewGestionFuncionarios.getGestionFuncionariosList1().remove(gestionFuncionariosList1NewGestionFuncionarios);
                        oldCreadoPorOfGestionFuncionariosList1NewGestionFuncionarios = em.merge(oldCreadoPorOfGestionFuncionariosList1NewGestionFuncionarios);
                    }
                }
            }
            for (GestionPerfiles gestionPerfilesListNewGestionPerfiles : gestionPerfilesListNew) {
                if (!gestionPerfilesListOld.contains(gestionPerfilesListNewGestionPerfiles)) {
                    GestionFuncionarios oldModificadoPorOfGestionPerfilesListNewGestionPerfiles = gestionPerfilesListNewGestionPerfiles.getModificadoPor();
                    gestionPerfilesListNewGestionPerfiles.setModificadoPor(gestionFuncionarios);
                    gestionPerfilesListNewGestionPerfiles = em.merge(gestionPerfilesListNewGestionPerfiles);
                    if (oldModificadoPorOfGestionPerfilesListNewGestionPerfiles != null && !oldModificadoPorOfGestionPerfilesListNewGestionPerfiles.equals(gestionFuncionarios)) {
                        oldModificadoPorOfGestionPerfilesListNewGestionPerfiles.getGestionPerfilesList().remove(gestionPerfilesListNewGestionPerfiles);
                        oldModificadoPorOfGestionPerfilesListNewGestionPerfiles = em.merge(oldModificadoPorOfGestionPerfilesListNewGestionPerfiles);
                    }
                }
            }
            for (GestionPerfiles gestionPerfilesList1NewGestionPerfiles : gestionPerfilesList1New) {
                if (!gestionPerfilesList1Old.contains(gestionPerfilesList1NewGestionPerfiles)) {
                    GestionFuncionarios oldCreadoPorOfGestionPerfilesList1NewGestionPerfiles = gestionPerfilesList1NewGestionPerfiles.getCreadoPor();
                    gestionPerfilesList1NewGestionPerfiles.setCreadoPor(gestionFuncionarios);
                    gestionPerfilesList1NewGestionPerfiles = em.merge(gestionPerfilesList1NewGestionPerfiles);
                    if (oldCreadoPorOfGestionPerfilesList1NewGestionPerfiles != null && !oldCreadoPorOfGestionPerfilesList1NewGestionPerfiles.equals(gestionFuncionarios)) {
                        oldCreadoPorOfGestionPerfilesList1NewGestionPerfiles.getGestionPerfilesList1().remove(gestionPerfilesList1NewGestionPerfiles);
                        oldCreadoPorOfGestionPerfilesList1NewGestionPerfiles = em.merge(oldCreadoPorOfGestionPerfilesList1NewGestionPerfiles);
                    }
                }
            }
            for (GestionUsuarios gestionUsuariosListNewGestionUsuarios : gestionUsuariosListNew) {
                if (!gestionUsuariosListOld.contains(gestionUsuariosListNewGestionUsuarios)) {
                    GestionFuncionarios oldFkGestionFuncionariosOfGestionUsuariosListNewGestionUsuarios = gestionUsuariosListNewGestionUsuarios.getFkGestionFuncionarios();
                    gestionUsuariosListNewGestionUsuarios.setFkGestionFuncionarios(gestionFuncionarios);
                    gestionUsuariosListNewGestionUsuarios = em.merge(gestionUsuariosListNewGestionUsuarios);
                    if (oldFkGestionFuncionariosOfGestionUsuariosListNewGestionUsuarios != null && !oldFkGestionFuncionariosOfGestionUsuariosListNewGestionUsuarios.equals(gestionFuncionarios)) {
                        oldFkGestionFuncionariosOfGestionUsuariosListNewGestionUsuarios.getGestionUsuariosList().remove(gestionUsuariosListNewGestionUsuarios);
                        oldFkGestionFuncionariosOfGestionUsuariosListNewGestionUsuarios = em.merge(oldFkGestionFuncionariosOfGestionUsuariosListNewGestionUsuarios);
                    }
                }
            }
            for (GestionUsuarios gestionUsuariosList1NewGestionUsuarios : gestionUsuariosList1New) {
                if (!gestionUsuariosList1Old.contains(gestionUsuariosList1NewGestionUsuarios)) {
                    GestionFuncionarios oldCreadoPorOfGestionUsuariosList1NewGestionUsuarios = gestionUsuariosList1NewGestionUsuarios.getCreadoPor();
                    gestionUsuariosList1NewGestionUsuarios.setCreadoPor(gestionFuncionarios);
                    gestionUsuariosList1NewGestionUsuarios = em.merge(gestionUsuariosList1NewGestionUsuarios);
                    if (oldCreadoPorOfGestionUsuariosList1NewGestionUsuarios != null && !oldCreadoPorOfGestionUsuariosList1NewGestionUsuarios.equals(gestionFuncionarios)) {
                        oldCreadoPorOfGestionUsuariosList1NewGestionUsuarios.getGestionUsuariosList1().remove(gestionUsuariosList1NewGestionUsuarios);
                        oldCreadoPorOfGestionUsuariosList1NewGestionUsuarios = em.merge(oldCreadoPorOfGestionUsuariosList1NewGestionUsuarios);
                    }
                }
            }
            for (GestionUsuarios gestionUsuariosList2NewGestionUsuarios : gestionUsuariosList2New) {
                if (!gestionUsuariosList2Old.contains(gestionUsuariosList2NewGestionUsuarios)) {
                    GestionFuncionarios oldModificadoPorOfGestionUsuariosList2NewGestionUsuarios = gestionUsuariosList2NewGestionUsuarios.getModificadoPor();
                    gestionUsuariosList2NewGestionUsuarios.setModificadoPor(gestionFuncionarios);
                    gestionUsuariosList2NewGestionUsuarios = em.merge(gestionUsuariosList2NewGestionUsuarios);
                    if (oldModificadoPorOfGestionUsuariosList2NewGestionUsuarios != null && !oldModificadoPorOfGestionUsuariosList2NewGestionUsuarios.equals(gestionFuncionarios)) {
                        oldModificadoPorOfGestionUsuariosList2NewGestionUsuarios.getGestionUsuariosList2().remove(gestionUsuariosList2NewGestionUsuarios);
                        oldModificadoPorOfGestionUsuariosList2NewGestionUsuarios = em.merge(oldModificadoPorOfGestionUsuariosList2NewGestionUsuarios);
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
                Long id = gestionFuncionarios.getIdFuncionarios();
                if (findGestionFuncionarios(id) == null) {
                    throw new NonexistentEntityException("The gestionFuncionarios with id " + id + " no longer exists.");
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
            GestionFuncionarios gestionFuncionarios;
            try {
                gestionFuncionarios = em.getReference(GestionFuncionarios.class, id);
                gestionFuncionarios.getIdFuncionarios();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gestionFuncionarios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ComercialClientes> comercialClientesListOrphanCheck = gestionFuncionarios.getComercialClientesList();
            for (ComercialClientes comercialClientesListOrphanCheckComercialClientes : comercialClientesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GestionFuncionarios (" + gestionFuncionarios + ") cannot be destroyed since the ComercialClientes " + comercialClientesListOrphanCheckComercialClientes + " in its comercialClientesList field has a non-nullable modificadoPor field.");
            }
            List<ComercialClientes> comercialClientesList1OrphanCheck = gestionFuncionarios.getComercialClientesList1();
            for (ComercialClientes comercialClientesList1OrphanCheckComercialClientes : comercialClientesList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GestionFuncionarios (" + gestionFuncionarios + ") cannot be destroyed since the ComercialClientes " + comercialClientesList1OrphanCheckComercialClientes + " in its comercialClientesList1 field has a non-nullable creadoPor field.");
            }
            List<GestionFuncionarios> gestionFuncionariosListOrphanCheck = gestionFuncionarios.getGestionFuncionariosList();
            for (GestionFuncionarios gestionFuncionariosListOrphanCheckGestionFuncionarios : gestionFuncionariosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GestionFuncionarios (" + gestionFuncionarios + ") cannot be destroyed since the GestionFuncionarios " + gestionFuncionariosListOrphanCheckGestionFuncionarios + " in its gestionFuncionariosList field has a non-nullable modificadoPor field.");
            }
            List<GestionFuncionarios> gestionFuncionariosList1OrphanCheck = gestionFuncionarios.getGestionFuncionariosList1();
            for (GestionFuncionarios gestionFuncionariosList1OrphanCheckGestionFuncionarios : gestionFuncionariosList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GestionFuncionarios (" + gestionFuncionarios + ") cannot be destroyed since the GestionFuncionarios " + gestionFuncionariosList1OrphanCheckGestionFuncionarios + " in its gestionFuncionariosList1 field has a non-nullable creadoPor field.");
            }
            List<GestionPerfiles> gestionPerfilesListOrphanCheck = gestionFuncionarios.getGestionPerfilesList();
            for (GestionPerfiles gestionPerfilesListOrphanCheckGestionPerfiles : gestionPerfilesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GestionFuncionarios (" + gestionFuncionarios + ") cannot be destroyed since the GestionPerfiles " + gestionPerfilesListOrphanCheckGestionPerfiles + " in its gestionPerfilesList field has a non-nullable modificadoPor field.");
            }
            List<GestionPerfiles> gestionPerfilesList1OrphanCheck = gestionFuncionarios.getGestionPerfilesList1();
            for (GestionPerfiles gestionPerfilesList1OrphanCheckGestionPerfiles : gestionPerfilesList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GestionFuncionarios (" + gestionFuncionarios + ") cannot be destroyed since the GestionPerfiles " + gestionPerfilesList1OrphanCheckGestionPerfiles + " in its gestionPerfilesList1 field has a non-nullable creadoPor field.");
            }
            List<GestionUsuarios> gestionUsuariosListOrphanCheck = gestionFuncionarios.getGestionUsuariosList();
            for (GestionUsuarios gestionUsuariosListOrphanCheckGestionUsuarios : gestionUsuariosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GestionFuncionarios (" + gestionFuncionarios + ") cannot be destroyed since the GestionUsuarios " + gestionUsuariosListOrphanCheckGestionUsuarios + " in its gestionUsuariosList field has a non-nullable fkGestionFuncionarios field.");
            }
            List<GestionUsuarios> gestionUsuariosList1OrphanCheck = gestionFuncionarios.getGestionUsuariosList1();
            for (GestionUsuarios gestionUsuariosList1OrphanCheckGestionUsuarios : gestionUsuariosList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GestionFuncionarios (" + gestionFuncionarios + ") cannot be destroyed since the GestionUsuarios " + gestionUsuariosList1OrphanCheckGestionUsuarios + " in its gestionUsuariosList1 field has a non-nullable creadoPor field.");
            }
            List<GestionUsuarios> gestionUsuariosList2OrphanCheck = gestionFuncionarios.getGestionUsuariosList2();
            for (GestionUsuarios gestionUsuariosList2OrphanCheckGestionUsuarios : gestionUsuariosList2OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GestionFuncionarios (" + gestionFuncionarios + ") cannot be destroyed since the GestionUsuarios " + gestionUsuariosList2OrphanCheckGestionUsuarios + " in its gestionUsuariosList2 field has a non-nullable modificadoPor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            GestionFuncionarios modificadoPor = gestionFuncionarios.getModificadoPor();
            if (modificadoPor != null) {
                modificadoPor.getGestionFuncionariosList().remove(gestionFuncionarios);
                modificadoPor = em.merge(modificadoPor);
            }
            GestionFuncionarios creadoPor = gestionFuncionarios.getCreadoPor();
            if (creadoPor != null) {
                creadoPor.getGestionFuncionariosList().remove(gestionFuncionarios);
                creadoPor = em.merge(creadoPor);
            }
            em.remove(gestionFuncionarios);
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

    public List<GestionFuncionarios> findGestionFuncionariosEntities() {
        return findGestionFuncionariosEntities(true, -1, -1);
    }

    public List<GestionFuncionarios> findGestionFuncionariosEntities(int maxResults, int firstResult) {
        return findGestionFuncionariosEntities(false, maxResults, firstResult);
    }

    private List<GestionFuncionarios> findGestionFuncionariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GestionFuncionarios.class));
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

    public GestionFuncionarios findGestionFuncionarios(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GestionFuncionarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getGestionFuncionariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GestionFuncionarios> rt = cq.from(GestionFuncionarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
