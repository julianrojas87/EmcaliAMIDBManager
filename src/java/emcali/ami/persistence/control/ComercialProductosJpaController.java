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
import java.util.Collection;
import emcali.ami.persistence.entity.SaldosHistoria;
import emcali.ami.persistence.entity.PrepagoEventos;
import emcali.ami.persistence.entity.AmyMedidores;
import emcali.ami.persistence.entity.ComercialProductos;
import emcali.ami.persistence.entity.PrepagoClientes;
import java.util.List;
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
        if (comercialProductos.getRecargaRecargasCollection() == null) {
            comercialProductos.setRecargaRecargasCollection(new ArrayList<RecargaRecargas>());
        }
        if (comercialProductos.getSaldosHistoriaCollection() == null) {
            comercialProductos.setSaldosHistoriaCollection(new ArrayList<SaldosHistoria>());
        }
        if (comercialProductos.getPrepagoEventosCollection() == null) {
            comercialProductos.setPrepagoEventosCollection(new ArrayList<PrepagoEventos>());
        }
        if (comercialProductos.getAmyMedidoresCollection() == null) {
            comercialProductos.setAmyMedidoresCollection(new ArrayList<AmyMedidores>());
        }
        if (comercialProductos.getPrepagoClientesCollection() == null) {
            comercialProductos.setPrepagoClientesCollection(new ArrayList<PrepagoClientes>());
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
            Collection<RecargaRecargas> attachedRecargaRecargasCollection = new ArrayList<RecargaRecargas>();
            for (RecargaRecargas recargaRecargasCollectionRecargaRecargasToAttach : comercialProductos.getRecargaRecargasCollection()) {
                recargaRecargasCollectionRecargaRecargasToAttach = em.getReference(recargaRecargasCollectionRecargaRecargasToAttach.getClass(), recargaRecargasCollectionRecargaRecargasToAttach.getIdRecargas());
                attachedRecargaRecargasCollection.add(recargaRecargasCollectionRecargaRecargasToAttach);
            }
            comercialProductos.setRecargaRecargasCollection(attachedRecargaRecargasCollection);
            Collection<SaldosHistoria> attachedSaldosHistoriaCollection = new ArrayList<SaldosHistoria>();
            for (SaldosHistoria saldosHistoriaCollectionSaldosHistoriaToAttach : comercialProductos.getSaldosHistoriaCollection()) {
                saldosHistoriaCollectionSaldosHistoriaToAttach = em.getReference(saldosHistoriaCollectionSaldosHistoriaToAttach.getClass(), saldosHistoriaCollectionSaldosHistoriaToAttach.getIdSaldosHistoria());
                attachedSaldosHistoriaCollection.add(saldosHistoriaCollectionSaldosHistoriaToAttach);
            }
            comercialProductos.setSaldosHistoriaCollection(attachedSaldosHistoriaCollection);
            Collection<PrepagoEventos> attachedPrepagoEventosCollection = new ArrayList<PrepagoEventos>();
            for (PrepagoEventos prepagoEventosCollectionPrepagoEventosToAttach : comercialProductos.getPrepagoEventosCollection()) {
                prepagoEventosCollectionPrepagoEventosToAttach = em.getReference(prepagoEventosCollectionPrepagoEventosToAttach.getClass(), prepagoEventosCollectionPrepagoEventosToAttach.getIdEventos());
                attachedPrepagoEventosCollection.add(prepagoEventosCollectionPrepagoEventosToAttach);
            }
            comercialProductos.setPrepagoEventosCollection(attachedPrepagoEventosCollection);
            Collection<AmyMedidores> attachedAmyMedidoresCollection = new ArrayList<AmyMedidores>();
            for (AmyMedidores amyMedidoresCollectionAmyMedidoresToAttach : comercialProductos.getAmyMedidoresCollection()) {
                amyMedidoresCollectionAmyMedidoresToAttach = em.getReference(amyMedidoresCollectionAmyMedidoresToAttach.getClass(), amyMedidoresCollectionAmyMedidoresToAttach.getIdMedidores());
                attachedAmyMedidoresCollection.add(amyMedidoresCollectionAmyMedidoresToAttach);
            }
            comercialProductos.setAmyMedidoresCollection(attachedAmyMedidoresCollection);
            Collection<PrepagoClientes> attachedPrepagoClientesCollection = new ArrayList<PrepagoClientes>();
            for (PrepagoClientes prepagoClientesCollectionPrepagoClientesToAttach : comercialProductos.getPrepagoClientesCollection()) {
                prepagoClientesCollectionPrepagoClientesToAttach = em.getReference(prepagoClientesCollectionPrepagoClientesToAttach.getClass(), prepagoClientesCollectionPrepagoClientesToAttach.getIdClientePrepago());
                attachedPrepagoClientesCollection.add(prepagoClientesCollectionPrepagoClientesToAttach);
            }
            comercialProductos.setPrepagoClientesCollection(attachedPrepagoClientesCollection);
            em.persist(comercialProductos);
            if (fkComercialContratos != null) {
                fkComercialContratos.getComercialProductosCollection().add(comercialProductos);
                fkComercialContratos = em.merge(fkComercialContratos);
            }
            for (RecargaRecargas recargaRecargasCollectionRecargaRecargas : comercialProductos.getRecargaRecargasCollection()) {
                ComercialProductos oldFkComercialProductosOfRecargaRecargasCollectionRecargaRecargas = recargaRecargasCollectionRecargaRecargas.getFkComercialProductos();
                recargaRecargasCollectionRecargaRecargas.setFkComercialProductos(comercialProductos);
                recargaRecargasCollectionRecargaRecargas = em.merge(recargaRecargasCollectionRecargaRecargas);
                if (oldFkComercialProductosOfRecargaRecargasCollectionRecargaRecargas != null) {
                    oldFkComercialProductosOfRecargaRecargasCollectionRecargaRecargas.getRecargaRecargasCollection().remove(recargaRecargasCollectionRecargaRecargas);
                    oldFkComercialProductosOfRecargaRecargasCollectionRecargaRecargas = em.merge(oldFkComercialProductosOfRecargaRecargasCollectionRecargaRecargas);
                }
            }
            for (SaldosHistoria saldosHistoriaCollectionSaldosHistoria : comercialProductos.getSaldosHistoriaCollection()) {
                ComercialProductos oldFkComercialProductosOfSaldosHistoriaCollectionSaldosHistoria = saldosHistoriaCollectionSaldosHistoria.getFkComercialProductos();
                saldosHistoriaCollectionSaldosHistoria.setFkComercialProductos(comercialProductos);
                saldosHistoriaCollectionSaldosHistoria = em.merge(saldosHistoriaCollectionSaldosHistoria);
                if (oldFkComercialProductosOfSaldosHistoriaCollectionSaldosHistoria != null) {
                    oldFkComercialProductosOfSaldosHistoriaCollectionSaldosHistoria.getSaldosHistoriaCollection().remove(saldosHistoriaCollectionSaldosHistoria);
                    oldFkComercialProductosOfSaldosHistoriaCollectionSaldosHistoria = em.merge(oldFkComercialProductosOfSaldosHistoriaCollectionSaldosHistoria);
                }
            }
            for (PrepagoEventos prepagoEventosCollectionPrepagoEventos : comercialProductos.getPrepagoEventosCollection()) {
                ComercialProductos oldFkComercialProductosOfPrepagoEventosCollectionPrepagoEventos = prepagoEventosCollectionPrepagoEventos.getFkComercialProductos();
                prepagoEventosCollectionPrepagoEventos.setFkComercialProductos(comercialProductos);
                prepagoEventosCollectionPrepagoEventos = em.merge(prepagoEventosCollectionPrepagoEventos);
                if (oldFkComercialProductosOfPrepagoEventosCollectionPrepagoEventos != null) {
                    oldFkComercialProductosOfPrepagoEventosCollectionPrepagoEventos.getPrepagoEventosCollection().remove(prepagoEventosCollectionPrepagoEventos);
                    oldFkComercialProductosOfPrepagoEventosCollectionPrepagoEventos = em.merge(oldFkComercialProductosOfPrepagoEventosCollectionPrepagoEventos);
                }
            }
            for (AmyMedidores amyMedidoresCollectionAmyMedidores : comercialProductos.getAmyMedidoresCollection()) {
                ComercialProductos oldFkComercialProductosOfAmyMedidoresCollectionAmyMedidores = amyMedidoresCollectionAmyMedidores.getFkComercialProductos();
                amyMedidoresCollectionAmyMedidores.setFkComercialProductos(comercialProductos);
                amyMedidoresCollectionAmyMedidores = em.merge(amyMedidoresCollectionAmyMedidores);
                if (oldFkComercialProductosOfAmyMedidoresCollectionAmyMedidores != null) {
                    oldFkComercialProductosOfAmyMedidoresCollectionAmyMedidores.getAmyMedidoresCollection().remove(amyMedidoresCollectionAmyMedidores);
                    oldFkComercialProductosOfAmyMedidoresCollectionAmyMedidores = em.merge(oldFkComercialProductosOfAmyMedidoresCollectionAmyMedidores);
                }
            }
            for (PrepagoClientes prepagoClientesCollectionPrepagoClientes : comercialProductos.getPrepagoClientesCollection()) {
                ComercialProductos oldFkComercialProductosOfPrepagoClientesCollectionPrepagoClientes = prepagoClientesCollectionPrepagoClientes.getFkComercialProductos();
                prepagoClientesCollectionPrepagoClientes.setFkComercialProductos(comercialProductos);
                prepagoClientesCollectionPrepagoClientes = em.merge(prepagoClientesCollectionPrepagoClientes);
                if (oldFkComercialProductosOfPrepagoClientesCollectionPrepagoClientes != null) {
                    oldFkComercialProductosOfPrepagoClientesCollectionPrepagoClientes.getPrepagoClientesCollection().remove(prepagoClientesCollectionPrepagoClientes);
                    oldFkComercialProductosOfPrepagoClientesCollectionPrepagoClientes = em.merge(oldFkComercialProductosOfPrepagoClientesCollectionPrepagoClientes);
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
            Collection<RecargaRecargas> recargaRecargasCollectionOld = persistentComercialProductos.getRecargaRecargasCollection();
            Collection<RecargaRecargas> recargaRecargasCollectionNew = comercialProductos.getRecargaRecargasCollection();
            Collection<SaldosHistoria> saldosHistoriaCollectionOld = persistentComercialProductos.getSaldosHistoriaCollection();
            Collection<SaldosHistoria> saldosHistoriaCollectionNew = comercialProductos.getSaldosHistoriaCollection();
            Collection<PrepagoEventos> prepagoEventosCollectionOld = persistentComercialProductos.getPrepagoEventosCollection();
            Collection<PrepagoEventos> prepagoEventosCollectionNew = comercialProductos.getPrepagoEventosCollection();
            Collection<AmyMedidores> amyMedidoresCollectionOld = persistentComercialProductos.getAmyMedidoresCollection();
            Collection<AmyMedidores> amyMedidoresCollectionNew = comercialProductos.getAmyMedidoresCollection();
            Collection<PrepagoClientes> prepagoClientesCollectionOld = persistentComercialProductos.getPrepagoClientesCollection();
            Collection<PrepagoClientes> prepagoClientesCollectionNew = comercialProductos.getPrepagoClientesCollection();
            List<String> illegalOrphanMessages = null;
            for (RecargaRecargas recargaRecargasCollectionOldRecargaRecargas : recargaRecargasCollectionOld) {
                if (!recargaRecargasCollectionNew.contains(recargaRecargasCollectionOldRecargaRecargas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RecargaRecargas " + recargaRecargasCollectionOldRecargaRecargas + " since its fkComercialProductos field is not nullable.");
                }
            }
            for (SaldosHistoria saldosHistoriaCollectionOldSaldosHistoria : saldosHistoriaCollectionOld) {
                if (!saldosHistoriaCollectionNew.contains(saldosHistoriaCollectionOldSaldosHistoria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SaldosHistoria " + saldosHistoriaCollectionOldSaldosHistoria + " since its fkComercialProductos field is not nullable.");
                }
            }
            for (PrepagoEventos prepagoEventosCollectionOldPrepagoEventos : prepagoEventosCollectionOld) {
                if (!prepagoEventosCollectionNew.contains(prepagoEventosCollectionOldPrepagoEventos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PrepagoEventos " + prepagoEventosCollectionOldPrepagoEventos + " since its fkComercialProductos field is not nullable.");
                }
            }
            for (AmyMedidores amyMedidoresCollectionOldAmyMedidores : amyMedidoresCollectionOld) {
                if (!amyMedidoresCollectionNew.contains(amyMedidoresCollectionOldAmyMedidores)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyMedidores " + amyMedidoresCollectionOldAmyMedidores + " since its fkComercialProductos field is not nullable.");
                }
            }
            for (PrepagoClientes prepagoClientesCollectionOldPrepagoClientes : prepagoClientesCollectionOld) {
                if (!prepagoClientesCollectionNew.contains(prepagoClientesCollectionOldPrepagoClientes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PrepagoClientes " + prepagoClientesCollectionOldPrepagoClientes + " since its fkComercialProductos field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (fkComercialContratosNew != null) {
                fkComercialContratosNew = em.getReference(fkComercialContratosNew.getClass(), fkComercialContratosNew.getIdContratos());
                comercialProductos.setFkComercialContratos(fkComercialContratosNew);
            }
            Collection<RecargaRecargas> attachedRecargaRecargasCollectionNew = new ArrayList<RecargaRecargas>();
            for (RecargaRecargas recargaRecargasCollectionNewRecargaRecargasToAttach : recargaRecargasCollectionNew) {
                recargaRecargasCollectionNewRecargaRecargasToAttach = em.getReference(recargaRecargasCollectionNewRecargaRecargasToAttach.getClass(), recargaRecargasCollectionNewRecargaRecargasToAttach.getIdRecargas());
                attachedRecargaRecargasCollectionNew.add(recargaRecargasCollectionNewRecargaRecargasToAttach);
            }
            recargaRecargasCollectionNew = attachedRecargaRecargasCollectionNew;
            comercialProductos.setRecargaRecargasCollection(recargaRecargasCollectionNew);
            Collection<SaldosHistoria> attachedSaldosHistoriaCollectionNew = new ArrayList<SaldosHistoria>();
            for (SaldosHistoria saldosHistoriaCollectionNewSaldosHistoriaToAttach : saldosHistoriaCollectionNew) {
                saldosHistoriaCollectionNewSaldosHistoriaToAttach = em.getReference(saldosHistoriaCollectionNewSaldosHistoriaToAttach.getClass(), saldosHistoriaCollectionNewSaldosHistoriaToAttach.getIdSaldosHistoria());
                attachedSaldosHistoriaCollectionNew.add(saldosHistoriaCollectionNewSaldosHistoriaToAttach);
            }
            saldosHistoriaCollectionNew = attachedSaldosHistoriaCollectionNew;
            comercialProductos.setSaldosHistoriaCollection(saldosHistoriaCollectionNew);
            Collection<PrepagoEventos> attachedPrepagoEventosCollectionNew = new ArrayList<PrepagoEventos>();
            for (PrepagoEventos prepagoEventosCollectionNewPrepagoEventosToAttach : prepagoEventosCollectionNew) {
                prepagoEventosCollectionNewPrepagoEventosToAttach = em.getReference(prepagoEventosCollectionNewPrepagoEventosToAttach.getClass(), prepagoEventosCollectionNewPrepagoEventosToAttach.getIdEventos());
                attachedPrepagoEventosCollectionNew.add(prepagoEventosCollectionNewPrepagoEventosToAttach);
            }
            prepagoEventosCollectionNew = attachedPrepagoEventosCollectionNew;
            comercialProductos.setPrepagoEventosCollection(prepagoEventosCollectionNew);
            Collection<AmyMedidores> attachedAmyMedidoresCollectionNew = new ArrayList<AmyMedidores>();
            for (AmyMedidores amyMedidoresCollectionNewAmyMedidoresToAttach : amyMedidoresCollectionNew) {
                amyMedidoresCollectionNewAmyMedidoresToAttach = em.getReference(amyMedidoresCollectionNewAmyMedidoresToAttach.getClass(), amyMedidoresCollectionNewAmyMedidoresToAttach.getIdMedidores());
                attachedAmyMedidoresCollectionNew.add(amyMedidoresCollectionNewAmyMedidoresToAttach);
            }
            amyMedidoresCollectionNew = attachedAmyMedidoresCollectionNew;
            comercialProductos.setAmyMedidoresCollection(amyMedidoresCollectionNew);
            Collection<PrepagoClientes> attachedPrepagoClientesCollectionNew = new ArrayList<PrepagoClientes>();
            for (PrepagoClientes prepagoClientesCollectionNewPrepagoClientesToAttach : prepagoClientesCollectionNew) {
                prepagoClientesCollectionNewPrepagoClientesToAttach = em.getReference(prepagoClientesCollectionNewPrepagoClientesToAttach.getClass(), prepagoClientesCollectionNewPrepagoClientesToAttach.getIdClientePrepago());
                attachedPrepagoClientesCollectionNew.add(prepagoClientesCollectionNewPrepagoClientesToAttach);
            }
            prepagoClientesCollectionNew = attachedPrepagoClientesCollectionNew;
            comercialProductos.setPrepagoClientesCollection(prepagoClientesCollectionNew);
            comercialProductos = em.merge(comercialProductos);
            if (fkComercialContratosOld != null && !fkComercialContratosOld.equals(fkComercialContratosNew)) {
                fkComercialContratosOld.getComercialProductosCollection().remove(comercialProductos);
                fkComercialContratosOld = em.merge(fkComercialContratosOld);
            }
            if (fkComercialContratosNew != null && !fkComercialContratosNew.equals(fkComercialContratosOld)) {
                fkComercialContratosNew.getComercialProductosCollection().add(comercialProductos);
                fkComercialContratosNew = em.merge(fkComercialContratosNew);
            }
            for (RecargaRecargas recargaRecargasCollectionNewRecargaRecargas : recargaRecargasCollectionNew) {
                if (!recargaRecargasCollectionOld.contains(recargaRecargasCollectionNewRecargaRecargas)) {
                    ComercialProductos oldFkComercialProductosOfRecargaRecargasCollectionNewRecargaRecargas = recargaRecargasCollectionNewRecargaRecargas.getFkComercialProductos();
                    recargaRecargasCollectionNewRecargaRecargas.setFkComercialProductos(comercialProductos);
                    recargaRecargasCollectionNewRecargaRecargas = em.merge(recargaRecargasCollectionNewRecargaRecargas);
                    if (oldFkComercialProductosOfRecargaRecargasCollectionNewRecargaRecargas != null && !oldFkComercialProductosOfRecargaRecargasCollectionNewRecargaRecargas.equals(comercialProductos)) {
                        oldFkComercialProductosOfRecargaRecargasCollectionNewRecargaRecargas.getRecargaRecargasCollection().remove(recargaRecargasCollectionNewRecargaRecargas);
                        oldFkComercialProductosOfRecargaRecargasCollectionNewRecargaRecargas = em.merge(oldFkComercialProductosOfRecargaRecargasCollectionNewRecargaRecargas);
                    }
                }
            }
            for (SaldosHistoria saldosHistoriaCollectionNewSaldosHistoria : saldosHistoriaCollectionNew) {
                if (!saldosHistoriaCollectionOld.contains(saldosHistoriaCollectionNewSaldosHistoria)) {
                    ComercialProductos oldFkComercialProductosOfSaldosHistoriaCollectionNewSaldosHistoria = saldosHistoriaCollectionNewSaldosHistoria.getFkComercialProductos();
                    saldosHistoriaCollectionNewSaldosHistoria.setFkComercialProductos(comercialProductos);
                    saldosHistoriaCollectionNewSaldosHistoria = em.merge(saldosHistoriaCollectionNewSaldosHistoria);
                    if (oldFkComercialProductosOfSaldosHistoriaCollectionNewSaldosHistoria != null && !oldFkComercialProductosOfSaldosHistoriaCollectionNewSaldosHistoria.equals(comercialProductos)) {
                        oldFkComercialProductosOfSaldosHistoriaCollectionNewSaldosHistoria.getSaldosHistoriaCollection().remove(saldosHistoriaCollectionNewSaldosHistoria);
                        oldFkComercialProductosOfSaldosHistoriaCollectionNewSaldosHistoria = em.merge(oldFkComercialProductosOfSaldosHistoriaCollectionNewSaldosHistoria);
                    }
                }
            }
            for (PrepagoEventos prepagoEventosCollectionNewPrepagoEventos : prepagoEventosCollectionNew) {
                if (!prepagoEventosCollectionOld.contains(prepagoEventosCollectionNewPrepagoEventos)) {
                    ComercialProductos oldFkComercialProductosOfPrepagoEventosCollectionNewPrepagoEventos = prepagoEventosCollectionNewPrepagoEventos.getFkComercialProductos();
                    prepagoEventosCollectionNewPrepagoEventos.setFkComercialProductos(comercialProductos);
                    prepagoEventosCollectionNewPrepagoEventos = em.merge(prepagoEventosCollectionNewPrepagoEventos);
                    if (oldFkComercialProductosOfPrepagoEventosCollectionNewPrepagoEventos != null && !oldFkComercialProductosOfPrepagoEventosCollectionNewPrepagoEventos.equals(comercialProductos)) {
                        oldFkComercialProductosOfPrepagoEventosCollectionNewPrepagoEventos.getPrepagoEventosCollection().remove(prepagoEventosCollectionNewPrepagoEventos);
                        oldFkComercialProductosOfPrepagoEventosCollectionNewPrepagoEventos = em.merge(oldFkComercialProductosOfPrepagoEventosCollectionNewPrepagoEventos);
                    }
                }
            }
            for (AmyMedidores amyMedidoresCollectionNewAmyMedidores : amyMedidoresCollectionNew) {
                if (!amyMedidoresCollectionOld.contains(amyMedidoresCollectionNewAmyMedidores)) {
                    ComercialProductos oldFkComercialProductosOfAmyMedidoresCollectionNewAmyMedidores = amyMedidoresCollectionNewAmyMedidores.getFkComercialProductos();
                    amyMedidoresCollectionNewAmyMedidores.setFkComercialProductos(comercialProductos);
                    amyMedidoresCollectionNewAmyMedidores = em.merge(amyMedidoresCollectionNewAmyMedidores);
                    if (oldFkComercialProductosOfAmyMedidoresCollectionNewAmyMedidores != null && !oldFkComercialProductosOfAmyMedidoresCollectionNewAmyMedidores.equals(comercialProductos)) {
                        oldFkComercialProductosOfAmyMedidoresCollectionNewAmyMedidores.getAmyMedidoresCollection().remove(amyMedidoresCollectionNewAmyMedidores);
                        oldFkComercialProductosOfAmyMedidoresCollectionNewAmyMedidores = em.merge(oldFkComercialProductosOfAmyMedidoresCollectionNewAmyMedidores);
                    }
                }
            }
            for (PrepagoClientes prepagoClientesCollectionNewPrepagoClientes : prepagoClientesCollectionNew) {
                if (!prepagoClientesCollectionOld.contains(prepagoClientesCollectionNewPrepagoClientes)) {
                    ComercialProductos oldFkComercialProductosOfPrepagoClientesCollectionNewPrepagoClientes = prepagoClientesCollectionNewPrepagoClientes.getFkComercialProductos();
                    prepagoClientesCollectionNewPrepagoClientes.setFkComercialProductos(comercialProductos);
                    prepagoClientesCollectionNewPrepagoClientes = em.merge(prepagoClientesCollectionNewPrepagoClientes);
                    if (oldFkComercialProductosOfPrepagoClientesCollectionNewPrepagoClientes != null && !oldFkComercialProductosOfPrepagoClientesCollectionNewPrepagoClientes.equals(comercialProductos)) {
                        oldFkComercialProductosOfPrepagoClientesCollectionNewPrepagoClientes.getPrepagoClientesCollection().remove(prepagoClientesCollectionNewPrepagoClientes);
                        oldFkComercialProductosOfPrepagoClientesCollectionNewPrepagoClientes = em.merge(oldFkComercialProductosOfPrepagoClientesCollectionNewPrepagoClientes);
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
            Collection<RecargaRecargas> recargaRecargasCollectionOrphanCheck = comercialProductos.getRecargaRecargasCollection();
            for (RecargaRecargas recargaRecargasCollectionOrphanCheckRecargaRecargas : recargaRecargasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ComercialProductos (" + comercialProductos + ") cannot be destroyed since the RecargaRecargas " + recargaRecargasCollectionOrphanCheckRecargaRecargas + " in its recargaRecargasCollection field has a non-nullable fkComercialProductos field.");
            }
            Collection<SaldosHistoria> saldosHistoriaCollectionOrphanCheck = comercialProductos.getSaldosHistoriaCollection();
            for (SaldosHistoria saldosHistoriaCollectionOrphanCheckSaldosHistoria : saldosHistoriaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ComercialProductos (" + comercialProductos + ") cannot be destroyed since the SaldosHistoria " + saldosHistoriaCollectionOrphanCheckSaldosHistoria + " in its saldosHistoriaCollection field has a non-nullable fkComercialProductos field.");
            }
            Collection<PrepagoEventos> prepagoEventosCollectionOrphanCheck = comercialProductos.getPrepagoEventosCollection();
            for (PrepagoEventos prepagoEventosCollectionOrphanCheckPrepagoEventos : prepagoEventosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ComercialProductos (" + comercialProductos + ") cannot be destroyed since the PrepagoEventos " + prepagoEventosCollectionOrphanCheckPrepagoEventos + " in its prepagoEventosCollection field has a non-nullable fkComercialProductos field.");
            }
            Collection<AmyMedidores> amyMedidoresCollectionOrphanCheck = comercialProductos.getAmyMedidoresCollection();
            for (AmyMedidores amyMedidoresCollectionOrphanCheckAmyMedidores : amyMedidoresCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ComercialProductos (" + comercialProductos + ") cannot be destroyed since the AmyMedidores " + amyMedidoresCollectionOrphanCheckAmyMedidores + " in its amyMedidoresCollection field has a non-nullable fkComercialProductos field.");
            }
            Collection<PrepagoClientes> prepagoClientesCollectionOrphanCheck = comercialProductos.getPrepagoClientesCollection();
            for (PrepagoClientes prepagoClientesCollectionOrphanCheckPrepagoClientes : prepagoClientesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ComercialProductos (" + comercialProductos + ") cannot be destroyed since the PrepagoClientes " + prepagoClientesCollectionOrphanCheckPrepagoClientes + " in its prepagoClientesCollection field has a non-nullable fkComercialProductos field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            ComercialContratos fkComercialContratos = comercialProductos.getFkComercialContratos();
            if (fkComercialContratos != null) {
                fkComercialContratos.getComercialProductosCollection().remove(comercialProductos);
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
