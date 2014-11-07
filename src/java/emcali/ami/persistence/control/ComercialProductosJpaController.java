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
import emcali.ami.persistence.entity.ComercialContratos;
import emcali.ami.persistence.entity.RecargaRecargas;
import java.util.ArrayList;
import java.util.List;
import emcali.ami.persistence.entity.SaldosHistoria;
import emcali.ami.persistence.entity.PrepagoEventos;
import emcali.ami.persistence.entity.AmyMedidores;
import emcali.ami.persistence.entity.ComercialProductos;
import emcali.ami.persistence.entity.PrepagoClientes;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class ComercialProductosJpaController implements Serializable {

    public ComercialProductosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ComercialProductos comercialProductos) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (comercialProductos.getRecargaRecargasList() == null) {
            comercialProductos.setRecargaRecargasList(new ArrayList<RecargaRecargas>());
        }
        if (comercialProductos.getSaldosHistoriaList() == null) {
            comercialProductos.setSaldosHistoriaList(new ArrayList<SaldosHistoria>());
        }
        if (comercialProductos.getPrepagoEventosList() == null) {
            comercialProductos.setPrepagoEventosList(new ArrayList<PrepagoEventos>());
        }
        if (comercialProductos.getAmyMedidoresList() == null) {
            comercialProductos.setAmyMedidoresList(new ArrayList<AmyMedidores>());
        }
        if (comercialProductos.getPrepagoClientesList() == null) {
            comercialProductos.setPrepagoClientesList(new ArrayList<PrepagoClientes>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ComercialContratos fkComercialContratos = comercialProductos.getFkComercialContratos();
            if (fkComercialContratos != null) {
                fkComercialContratos = em.getReference(fkComercialContratos.getClass(), fkComercialContratos.getIdContratos());
                comercialProductos.setFkComercialContratos(fkComercialContratos);
            }
            List<RecargaRecargas> attachedRecargaRecargasList = new ArrayList<RecargaRecargas>();
            for (RecargaRecargas recargaRecargasListRecargaRecargasToAttach : comercialProductos.getRecargaRecargasList()) {
                recargaRecargasListRecargaRecargasToAttach = em.getReference(recargaRecargasListRecargaRecargasToAttach.getClass(), recargaRecargasListRecargaRecargasToAttach.getIdRecargas());
                attachedRecargaRecargasList.add(recargaRecargasListRecargaRecargasToAttach);
            }
            comercialProductos.setRecargaRecargasList(attachedRecargaRecargasList);
            List<SaldosHistoria> attachedSaldosHistoriaList = new ArrayList<SaldosHistoria>();
            for (SaldosHistoria saldosHistoriaListSaldosHistoriaToAttach : comercialProductos.getSaldosHistoriaList()) {
                saldosHistoriaListSaldosHistoriaToAttach = em.getReference(saldosHistoriaListSaldosHistoriaToAttach.getClass(), saldosHistoriaListSaldosHistoriaToAttach.getIdSaldosHistoria());
                attachedSaldosHistoriaList.add(saldosHistoriaListSaldosHistoriaToAttach);
            }
            comercialProductos.setSaldosHistoriaList(attachedSaldosHistoriaList);
            List<PrepagoEventos> attachedPrepagoEventosList = new ArrayList<PrepagoEventos>();
            for (PrepagoEventos prepagoEventosListPrepagoEventosToAttach : comercialProductos.getPrepagoEventosList()) {
                prepagoEventosListPrepagoEventosToAttach = em.getReference(prepagoEventosListPrepagoEventosToAttach.getClass(), prepagoEventosListPrepagoEventosToAttach.getIdEventos());
                attachedPrepagoEventosList.add(prepagoEventosListPrepagoEventosToAttach);
            }
            comercialProductos.setPrepagoEventosList(attachedPrepagoEventosList);
            List<AmyMedidores> attachedAmyMedidoresList = new ArrayList<AmyMedidores>();
            for (AmyMedidores amyMedidoresListAmyMedidoresToAttach : comercialProductos.getAmyMedidoresList()) {
                amyMedidoresListAmyMedidoresToAttach = em.getReference(amyMedidoresListAmyMedidoresToAttach.getClass(), amyMedidoresListAmyMedidoresToAttach.getIdMedidores());
                attachedAmyMedidoresList.add(amyMedidoresListAmyMedidoresToAttach);
            }
            comercialProductos.setAmyMedidoresList(attachedAmyMedidoresList);
            List<PrepagoClientes> attachedPrepagoClientesList = new ArrayList<PrepagoClientes>();
            for (PrepagoClientes prepagoClientesListPrepagoClientesToAttach : comercialProductos.getPrepagoClientesList()) {
                prepagoClientesListPrepagoClientesToAttach = em.getReference(prepagoClientesListPrepagoClientesToAttach.getClass(), prepagoClientesListPrepagoClientesToAttach.getIdClientePrepago());
                attachedPrepagoClientesList.add(prepagoClientesListPrepagoClientesToAttach);
            }
            comercialProductos.setPrepagoClientesList(attachedPrepagoClientesList);
            em.persist(comercialProductos);
            if (fkComercialContratos != null) {
                fkComercialContratos.getComercialProductosList().add(comercialProductos);
                fkComercialContratos = em.merge(fkComercialContratos);
            }
            for (RecargaRecargas recargaRecargasListRecargaRecargas : comercialProductos.getRecargaRecargasList()) {
                ComercialProductos oldFkComercialProductosOfRecargaRecargasListRecargaRecargas = recargaRecargasListRecargaRecargas.getFkComercialProductos();
                recargaRecargasListRecargaRecargas.setFkComercialProductos(comercialProductos);
                recargaRecargasListRecargaRecargas = em.merge(recargaRecargasListRecargaRecargas);
                if (oldFkComercialProductosOfRecargaRecargasListRecargaRecargas != null) {
                    oldFkComercialProductosOfRecargaRecargasListRecargaRecargas.getRecargaRecargasList().remove(recargaRecargasListRecargaRecargas);
                    oldFkComercialProductosOfRecargaRecargasListRecargaRecargas = em.merge(oldFkComercialProductosOfRecargaRecargasListRecargaRecargas);
                }
            }
            for (SaldosHistoria saldosHistoriaListSaldosHistoria : comercialProductos.getSaldosHistoriaList()) {
                ComercialProductos oldFkComercialProductosOfSaldosHistoriaListSaldosHistoria = saldosHistoriaListSaldosHistoria.getFkComercialProductos();
                saldosHistoriaListSaldosHistoria.setFkComercialProductos(comercialProductos);
                saldosHistoriaListSaldosHistoria = em.merge(saldosHistoriaListSaldosHistoria);
                if (oldFkComercialProductosOfSaldosHistoriaListSaldosHistoria != null) {
                    oldFkComercialProductosOfSaldosHistoriaListSaldosHistoria.getSaldosHistoriaList().remove(saldosHistoriaListSaldosHistoria);
                    oldFkComercialProductosOfSaldosHistoriaListSaldosHistoria = em.merge(oldFkComercialProductosOfSaldosHistoriaListSaldosHistoria);
                }
            }
            for (PrepagoEventos prepagoEventosListPrepagoEventos : comercialProductos.getPrepagoEventosList()) {
                ComercialProductos oldFkComercialProductosOfPrepagoEventosListPrepagoEventos = prepagoEventosListPrepagoEventos.getFkComercialProductos();
                prepagoEventosListPrepagoEventos.setFkComercialProductos(comercialProductos);
                prepagoEventosListPrepagoEventos = em.merge(prepagoEventosListPrepagoEventos);
                if (oldFkComercialProductosOfPrepagoEventosListPrepagoEventos != null) {
                    oldFkComercialProductosOfPrepagoEventosListPrepagoEventos.getPrepagoEventosList().remove(prepagoEventosListPrepagoEventos);
                    oldFkComercialProductosOfPrepagoEventosListPrepagoEventos = em.merge(oldFkComercialProductosOfPrepagoEventosListPrepagoEventos);
                }
            }
            for (AmyMedidores amyMedidoresListAmyMedidores : comercialProductos.getAmyMedidoresList()) {
                ComercialProductos oldFkComercialProductosOfAmyMedidoresListAmyMedidores = amyMedidoresListAmyMedidores.getFkComercialProductos();
                amyMedidoresListAmyMedidores.setFkComercialProductos(comercialProductos);
                amyMedidoresListAmyMedidores = em.merge(amyMedidoresListAmyMedidores);
                if (oldFkComercialProductosOfAmyMedidoresListAmyMedidores != null) {
                    oldFkComercialProductosOfAmyMedidoresListAmyMedidores.getAmyMedidoresList().remove(amyMedidoresListAmyMedidores);
                    oldFkComercialProductosOfAmyMedidoresListAmyMedidores = em.merge(oldFkComercialProductosOfAmyMedidoresListAmyMedidores);
                }
            }
            for (PrepagoClientes prepagoClientesListPrepagoClientes : comercialProductos.getPrepagoClientesList()) {
                ComercialProductos oldFkComercialProductosOfPrepagoClientesListPrepagoClientes = prepagoClientesListPrepagoClientes.getFkComercialProductos();
                prepagoClientesListPrepagoClientes.setFkComercialProductos(comercialProductos);
                prepagoClientesListPrepagoClientes = em.merge(prepagoClientesListPrepagoClientes);
                if (oldFkComercialProductosOfPrepagoClientesListPrepagoClientes != null) {
                    oldFkComercialProductosOfPrepagoClientesListPrepagoClientes.getPrepagoClientesList().remove(prepagoClientesListPrepagoClientes);
                    oldFkComercialProductosOfPrepagoClientesListPrepagoClientes = em.merge(oldFkComercialProductosOfPrepagoClientesListPrepagoClientes);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findComercialProductos(comercialProductos.getIdProductos()) != null) {
                throw new PreexistingEntityException("ComercialProductos " + comercialProductos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ComercialProductos comercialProductos) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ComercialProductos persistentComercialProductos = em.find(ComercialProductos.class, comercialProductos.getIdProductos());
            ComercialContratos fkComercialContratosOld = persistentComercialProductos.getFkComercialContratos();
            ComercialContratos fkComercialContratosNew = comercialProductos.getFkComercialContratos();
            List<RecargaRecargas> recargaRecargasListOld = persistentComercialProductos.getRecargaRecargasList();
            List<RecargaRecargas> recargaRecargasListNew = comercialProductos.getRecargaRecargasList();
            List<SaldosHistoria> saldosHistoriaListOld = persistentComercialProductos.getSaldosHistoriaList();
            List<SaldosHistoria> saldosHistoriaListNew = comercialProductos.getSaldosHistoriaList();
            List<PrepagoEventos> prepagoEventosListOld = persistentComercialProductos.getPrepagoEventosList();
            List<PrepagoEventos> prepagoEventosListNew = comercialProductos.getPrepagoEventosList();
            List<AmyMedidores> amyMedidoresListOld = persistentComercialProductos.getAmyMedidoresList();
            List<AmyMedidores> amyMedidoresListNew = comercialProductos.getAmyMedidoresList();
            List<PrepagoClientes> prepagoClientesListOld = persistentComercialProductos.getPrepagoClientesList();
            List<PrepagoClientes> prepagoClientesListNew = comercialProductos.getPrepagoClientesList();
            List<String> illegalOrphanMessages = null;
            for (RecargaRecargas recargaRecargasListOldRecargaRecargas : recargaRecargasListOld) {
                if (!recargaRecargasListNew.contains(recargaRecargasListOldRecargaRecargas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RecargaRecargas " + recargaRecargasListOldRecargaRecargas + " since its fkComercialProductos field is not nullable.");
                }
            }
            for (SaldosHistoria saldosHistoriaListOldSaldosHistoria : saldosHistoriaListOld) {
                if (!saldosHistoriaListNew.contains(saldosHistoriaListOldSaldosHistoria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SaldosHistoria " + saldosHistoriaListOldSaldosHistoria + " since its fkComercialProductos field is not nullable.");
                }
            }
            for (PrepagoEventos prepagoEventosListOldPrepagoEventos : prepagoEventosListOld) {
                if (!prepagoEventosListNew.contains(prepagoEventosListOldPrepagoEventos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PrepagoEventos " + prepagoEventosListOldPrepagoEventos + " since its fkComercialProductos field is not nullable.");
                }
            }
            for (AmyMedidores amyMedidoresListOldAmyMedidores : amyMedidoresListOld) {
                if (!amyMedidoresListNew.contains(amyMedidoresListOldAmyMedidores)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyMedidores " + amyMedidoresListOldAmyMedidores + " since its fkComercialProductos field is not nullable.");
                }
            }
            for (PrepagoClientes prepagoClientesListOldPrepagoClientes : prepagoClientesListOld) {
                if (!prepagoClientesListNew.contains(prepagoClientesListOldPrepagoClientes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PrepagoClientes " + prepagoClientesListOldPrepagoClientes + " since its fkComercialProductos field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (fkComercialContratosNew != null) {
                fkComercialContratosNew = em.getReference(fkComercialContratosNew.getClass(), fkComercialContratosNew.getIdContratos());
                comercialProductos.setFkComercialContratos(fkComercialContratosNew);
            }
            List<RecargaRecargas> attachedRecargaRecargasListNew = new ArrayList<RecargaRecargas>();
            for (RecargaRecargas recargaRecargasListNewRecargaRecargasToAttach : recargaRecargasListNew) {
                recargaRecargasListNewRecargaRecargasToAttach = em.getReference(recargaRecargasListNewRecargaRecargasToAttach.getClass(), recargaRecargasListNewRecargaRecargasToAttach.getIdRecargas());
                attachedRecargaRecargasListNew.add(recargaRecargasListNewRecargaRecargasToAttach);
            }
            recargaRecargasListNew = attachedRecargaRecargasListNew;
            comercialProductos.setRecargaRecargasList(recargaRecargasListNew);
            List<SaldosHistoria> attachedSaldosHistoriaListNew = new ArrayList<SaldosHistoria>();
            for (SaldosHistoria saldosHistoriaListNewSaldosHistoriaToAttach : saldosHistoriaListNew) {
                saldosHistoriaListNewSaldosHistoriaToAttach = em.getReference(saldosHistoriaListNewSaldosHistoriaToAttach.getClass(), saldosHistoriaListNewSaldosHistoriaToAttach.getIdSaldosHistoria());
                attachedSaldosHistoriaListNew.add(saldosHistoriaListNewSaldosHistoriaToAttach);
            }
            saldosHistoriaListNew = attachedSaldosHistoriaListNew;
            comercialProductos.setSaldosHistoriaList(saldosHistoriaListNew);
            List<PrepagoEventos> attachedPrepagoEventosListNew = new ArrayList<PrepagoEventos>();
            for (PrepagoEventos prepagoEventosListNewPrepagoEventosToAttach : prepagoEventosListNew) {
                prepagoEventosListNewPrepagoEventosToAttach = em.getReference(prepagoEventosListNewPrepagoEventosToAttach.getClass(), prepagoEventosListNewPrepagoEventosToAttach.getIdEventos());
                attachedPrepagoEventosListNew.add(prepagoEventosListNewPrepagoEventosToAttach);
            }
            prepagoEventosListNew = attachedPrepagoEventosListNew;
            comercialProductos.setPrepagoEventosList(prepagoEventosListNew);
            List<AmyMedidores> attachedAmyMedidoresListNew = new ArrayList<AmyMedidores>();
            for (AmyMedidores amyMedidoresListNewAmyMedidoresToAttach : amyMedidoresListNew) {
                amyMedidoresListNewAmyMedidoresToAttach = em.getReference(amyMedidoresListNewAmyMedidoresToAttach.getClass(), amyMedidoresListNewAmyMedidoresToAttach.getIdMedidores());
                attachedAmyMedidoresListNew.add(amyMedidoresListNewAmyMedidoresToAttach);
            }
            amyMedidoresListNew = attachedAmyMedidoresListNew;
            comercialProductos.setAmyMedidoresList(amyMedidoresListNew);
            List<PrepagoClientes> attachedPrepagoClientesListNew = new ArrayList<PrepagoClientes>();
            for (PrepagoClientes prepagoClientesListNewPrepagoClientesToAttach : prepagoClientesListNew) {
                prepagoClientesListNewPrepagoClientesToAttach = em.getReference(prepagoClientesListNewPrepagoClientesToAttach.getClass(), prepagoClientesListNewPrepagoClientesToAttach.getIdClientePrepago());
                attachedPrepagoClientesListNew.add(prepagoClientesListNewPrepagoClientesToAttach);
            }
            prepagoClientesListNew = attachedPrepagoClientesListNew;
            comercialProductos.setPrepagoClientesList(prepagoClientesListNew);
            comercialProductos = em.merge(comercialProductos);
            if (fkComercialContratosOld != null && !fkComercialContratosOld.equals(fkComercialContratosNew)) {
                fkComercialContratosOld.getComercialProductosList().remove(comercialProductos);
                fkComercialContratosOld = em.merge(fkComercialContratosOld);
            }
            if (fkComercialContratosNew != null && !fkComercialContratosNew.equals(fkComercialContratosOld)) {
                fkComercialContratosNew.getComercialProductosList().add(comercialProductos);
                fkComercialContratosNew = em.merge(fkComercialContratosNew);
            }
            for (RecargaRecargas recargaRecargasListNewRecargaRecargas : recargaRecargasListNew) {
                if (!recargaRecargasListOld.contains(recargaRecargasListNewRecargaRecargas)) {
                    ComercialProductos oldFkComercialProductosOfRecargaRecargasListNewRecargaRecargas = recargaRecargasListNewRecargaRecargas.getFkComercialProductos();
                    recargaRecargasListNewRecargaRecargas.setFkComercialProductos(comercialProductos);
                    recargaRecargasListNewRecargaRecargas = em.merge(recargaRecargasListNewRecargaRecargas);
                    if (oldFkComercialProductosOfRecargaRecargasListNewRecargaRecargas != null && !oldFkComercialProductosOfRecargaRecargasListNewRecargaRecargas.equals(comercialProductos)) {
                        oldFkComercialProductosOfRecargaRecargasListNewRecargaRecargas.getRecargaRecargasList().remove(recargaRecargasListNewRecargaRecargas);
                        oldFkComercialProductosOfRecargaRecargasListNewRecargaRecargas = em.merge(oldFkComercialProductosOfRecargaRecargasListNewRecargaRecargas);
                    }
                }
            }
            for (SaldosHistoria saldosHistoriaListNewSaldosHistoria : saldosHistoriaListNew) {
                if (!saldosHistoriaListOld.contains(saldosHistoriaListNewSaldosHistoria)) {
                    ComercialProductos oldFkComercialProductosOfSaldosHistoriaListNewSaldosHistoria = saldosHistoriaListNewSaldosHistoria.getFkComercialProductos();
                    saldosHistoriaListNewSaldosHistoria.setFkComercialProductos(comercialProductos);
                    saldosHistoriaListNewSaldosHistoria = em.merge(saldosHistoriaListNewSaldosHistoria);
                    if (oldFkComercialProductosOfSaldosHistoriaListNewSaldosHistoria != null && !oldFkComercialProductosOfSaldosHistoriaListNewSaldosHistoria.equals(comercialProductos)) {
                        oldFkComercialProductosOfSaldosHistoriaListNewSaldosHistoria.getSaldosHistoriaList().remove(saldosHistoriaListNewSaldosHistoria);
                        oldFkComercialProductosOfSaldosHistoriaListNewSaldosHistoria = em.merge(oldFkComercialProductosOfSaldosHistoriaListNewSaldosHistoria);
                    }
                }
            }
            for (PrepagoEventos prepagoEventosListNewPrepagoEventos : prepagoEventosListNew) {
                if (!prepagoEventosListOld.contains(prepagoEventosListNewPrepagoEventos)) {
                    ComercialProductos oldFkComercialProductosOfPrepagoEventosListNewPrepagoEventos = prepagoEventosListNewPrepagoEventos.getFkComercialProductos();
                    prepagoEventosListNewPrepagoEventos.setFkComercialProductos(comercialProductos);
                    prepagoEventosListNewPrepagoEventos = em.merge(prepagoEventosListNewPrepagoEventos);
                    if (oldFkComercialProductosOfPrepagoEventosListNewPrepagoEventos != null && !oldFkComercialProductosOfPrepagoEventosListNewPrepagoEventos.equals(comercialProductos)) {
                        oldFkComercialProductosOfPrepagoEventosListNewPrepagoEventos.getPrepagoEventosList().remove(prepagoEventosListNewPrepagoEventos);
                        oldFkComercialProductosOfPrepagoEventosListNewPrepagoEventos = em.merge(oldFkComercialProductosOfPrepagoEventosListNewPrepagoEventos);
                    }
                }
            }
            for (AmyMedidores amyMedidoresListNewAmyMedidores : amyMedidoresListNew) {
                if (!amyMedidoresListOld.contains(amyMedidoresListNewAmyMedidores)) {
                    ComercialProductos oldFkComercialProductosOfAmyMedidoresListNewAmyMedidores = amyMedidoresListNewAmyMedidores.getFkComercialProductos();
                    amyMedidoresListNewAmyMedidores.setFkComercialProductos(comercialProductos);
                    amyMedidoresListNewAmyMedidores = em.merge(amyMedidoresListNewAmyMedidores);
                    if (oldFkComercialProductosOfAmyMedidoresListNewAmyMedidores != null && !oldFkComercialProductosOfAmyMedidoresListNewAmyMedidores.equals(comercialProductos)) {
                        oldFkComercialProductosOfAmyMedidoresListNewAmyMedidores.getAmyMedidoresList().remove(amyMedidoresListNewAmyMedidores);
                        oldFkComercialProductosOfAmyMedidoresListNewAmyMedidores = em.merge(oldFkComercialProductosOfAmyMedidoresListNewAmyMedidores);
                    }
                }
            }
            for (PrepagoClientes prepagoClientesListNewPrepagoClientes : prepagoClientesListNew) {
                if (!prepagoClientesListOld.contains(prepagoClientesListNewPrepagoClientes)) {
                    ComercialProductos oldFkComercialProductosOfPrepagoClientesListNewPrepagoClientes = prepagoClientesListNewPrepagoClientes.getFkComercialProductos();
                    prepagoClientesListNewPrepagoClientes.setFkComercialProductos(comercialProductos);
                    prepagoClientesListNewPrepagoClientes = em.merge(prepagoClientesListNewPrepagoClientes);
                    if (oldFkComercialProductosOfPrepagoClientesListNewPrepagoClientes != null && !oldFkComercialProductosOfPrepagoClientesListNewPrepagoClientes.equals(comercialProductos)) {
                        oldFkComercialProductosOfPrepagoClientesListNewPrepagoClientes.getPrepagoClientesList().remove(prepagoClientesListNewPrepagoClientes);
                        oldFkComercialProductosOfPrepagoClientesListNewPrepagoClientes = em.merge(oldFkComercialProductosOfPrepagoClientesListNewPrepagoClientes);
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
                Long id = comercialProductos.getIdProductos();
                if (findComercialProductos(id) == null) {
                    throw new NonexistentEntityException("The comercialProductos with id " + id + " no longer exists.");
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
            ComercialProductos comercialProductos;
            try {
                comercialProductos = em.getReference(ComercialProductos.class, id);
                comercialProductos.getIdProductos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comercialProductos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RecargaRecargas> recargaRecargasListOrphanCheck = comercialProductos.getRecargaRecargasList();
            for (RecargaRecargas recargaRecargasListOrphanCheckRecargaRecargas : recargaRecargasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ComercialProductos (" + comercialProductos + ") cannot be destroyed since the RecargaRecargas " + recargaRecargasListOrphanCheckRecargaRecargas + " in its recargaRecargasList field has a non-nullable fkComercialProductos field.");
            }
            List<SaldosHistoria> saldosHistoriaListOrphanCheck = comercialProductos.getSaldosHistoriaList();
            for (SaldosHistoria saldosHistoriaListOrphanCheckSaldosHistoria : saldosHistoriaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ComercialProductos (" + comercialProductos + ") cannot be destroyed since the SaldosHistoria " + saldosHistoriaListOrphanCheckSaldosHistoria + " in its saldosHistoriaList field has a non-nullable fkComercialProductos field.");
            }
            List<PrepagoEventos> prepagoEventosListOrphanCheck = comercialProductos.getPrepagoEventosList();
            for (PrepagoEventos prepagoEventosListOrphanCheckPrepagoEventos : prepagoEventosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ComercialProductos (" + comercialProductos + ") cannot be destroyed since the PrepagoEventos " + prepagoEventosListOrphanCheckPrepagoEventos + " in its prepagoEventosList field has a non-nullable fkComercialProductos field.");
            }
            List<AmyMedidores> amyMedidoresListOrphanCheck = comercialProductos.getAmyMedidoresList();
            for (AmyMedidores amyMedidoresListOrphanCheckAmyMedidores : amyMedidoresListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ComercialProductos (" + comercialProductos + ") cannot be destroyed since the AmyMedidores " + amyMedidoresListOrphanCheckAmyMedidores + " in its amyMedidoresList field has a non-nullable fkComercialProductos field.");
            }
            List<PrepagoClientes> prepagoClientesListOrphanCheck = comercialProductos.getPrepagoClientesList();
            for (PrepagoClientes prepagoClientesListOrphanCheckPrepagoClientes : prepagoClientesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ComercialProductos (" + comercialProductos + ") cannot be destroyed since the PrepagoClientes " + prepagoClientesListOrphanCheckPrepagoClientes + " in its prepagoClientesList field has a non-nullable fkComercialProductos field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            ComercialContratos fkComercialContratos = comercialProductos.getFkComercialContratos();
            if (fkComercialContratos != null) {
                fkComercialContratos.getComercialProductosList().remove(comercialProductos);
                fkComercialContratos = em.merge(fkComercialContratos);
            }
            em.remove(comercialProductos);
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

    public List<ComercialProductos> findComercialProductosEntities() {
        return findComercialProductosEntities(true, -1, -1);
    }

    public List<ComercialProductos> findComercialProductosEntities(int maxResults, int firstResult) {
        return findComercialProductosEntities(false, maxResults, firstResult);
    }

    private List<ComercialProductos> findComercialProductosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ComercialProductos.class));
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

    public ComercialProductos findComercialProductos(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ComercialProductos.class, id);
        } finally {
            em.close();
        }
    }

    public int getComercialProductosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ComercialProductos> rt = cq.from(ComercialProductos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
