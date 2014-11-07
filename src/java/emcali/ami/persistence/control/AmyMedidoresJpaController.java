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
import emcali.ami.persistence.entity.ComercialProductos;
import emcali.ami.persistence.entity.AtributosTiposMedidores;
import emcali.ami.persistence.entity.AmyCajas;
import emcali.ami.persistence.entity.AmyLecturas;
import java.util.ArrayList;
import java.util.List;
import emcali.ami.persistence.entity.PrepagoSaldos;
import emcali.ami.persistence.entity.AmyEventos;
import emcali.ami.persistence.entity.AmyConsumos;
import emcali.ami.persistence.entity.AmyMedidores;
import emcali.ami.persistence.entity.TelcoInfo;
import emcali.ami.persistence.entity.AmyMedidoresHistorico;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author julian
 */
public class AmyMedidoresJpaController implements Serializable {

    public AmyMedidoresJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AmyMedidores amyMedidores) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (amyMedidores.getAmyLecturasList() == null) {
            amyMedidores.setAmyLecturasList(new ArrayList<AmyLecturas>());
        }
        if (amyMedidores.getPrepagoSaldosList() == null) {
            amyMedidores.setPrepagoSaldosList(new ArrayList<PrepagoSaldos>());
        }
        if (amyMedidores.getAmyEventosList() == null) {
            amyMedidores.setAmyEventosList(new ArrayList<AmyEventos>());
        }
        if (amyMedidores.getAmyConsumosList() == null) {
            amyMedidores.setAmyConsumosList(new ArrayList<AmyConsumos>());
        }
        if (amyMedidores.getTelcoInfoList() == null) {
            amyMedidores.setTelcoInfoList(new ArrayList<TelcoInfo>());
        }
        if (amyMedidores.getAmyMedidoresHistoricoList() == null) {
            amyMedidores.setAmyMedidoresHistoricoList(new ArrayList<AmyMedidoresHistorico>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ComercialProductos fkComercialProductos = amyMedidores.getFkComercialProductos();
            if (fkComercialProductos != null) {
                fkComercialProductos = em.getReference(fkComercialProductos.getClass(), fkComercialProductos.getIdProductos());
                amyMedidores.setFkComercialProductos(fkComercialProductos);
            }
            AtributosTiposMedidores fkAtributosTiposMedidores = amyMedidores.getFkAtributosTiposMedidores();
            if (fkAtributosTiposMedidores != null) {
                fkAtributosTiposMedidores = em.getReference(fkAtributosTiposMedidores.getClass(), fkAtributosTiposMedidores.getIdTiposMedidores());
                amyMedidores.setFkAtributosTiposMedidores(fkAtributosTiposMedidores);
            }
            AmyCajas fkAmyCajas = amyMedidores.getFkAmyCajas();
            if (fkAmyCajas != null) {
                fkAmyCajas = em.getReference(fkAmyCajas.getClass(), fkAmyCajas.getIdCajas());
                amyMedidores.setFkAmyCajas(fkAmyCajas);
            }
            List<AmyLecturas> attachedAmyLecturasList = new ArrayList<AmyLecturas>();
            for (AmyLecturas amyLecturasListAmyLecturasToAttach : amyMedidores.getAmyLecturasList()) {
                amyLecturasListAmyLecturasToAttach = em.getReference(amyLecturasListAmyLecturasToAttach.getClass(), amyLecturasListAmyLecturasToAttach.getIdLecturas());
                attachedAmyLecturasList.add(amyLecturasListAmyLecturasToAttach);
            }
            amyMedidores.setAmyLecturasList(attachedAmyLecturasList);
            List<PrepagoSaldos> attachedPrepagoSaldosList = new ArrayList<PrepagoSaldos>();
            for (PrepagoSaldos prepagoSaldosListPrepagoSaldosToAttach : amyMedidores.getPrepagoSaldosList()) {
                prepagoSaldosListPrepagoSaldosToAttach = em.getReference(prepagoSaldosListPrepagoSaldosToAttach.getClass(), prepagoSaldosListPrepagoSaldosToAttach.getIdSaldos());
                attachedPrepagoSaldosList.add(prepagoSaldosListPrepagoSaldosToAttach);
            }
            amyMedidores.setPrepagoSaldosList(attachedPrepagoSaldosList);
            List<AmyEventos> attachedAmyEventosList = new ArrayList<AmyEventos>();
            for (AmyEventos amyEventosListAmyEventosToAttach : amyMedidores.getAmyEventosList()) {
                amyEventosListAmyEventosToAttach = em.getReference(amyEventosListAmyEventosToAttach.getClass(), amyEventosListAmyEventosToAttach.getIdEvento());
                attachedAmyEventosList.add(amyEventosListAmyEventosToAttach);
            }
            amyMedidores.setAmyEventosList(attachedAmyEventosList);
            List<AmyConsumos> attachedAmyConsumosList = new ArrayList<AmyConsumos>();
            for (AmyConsumos amyConsumosListAmyConsumosToAttach : amyMedidores.getAmyConsumosList()) {
                amyConsumosListAmyConsumosToAttach = em.getReference(amyConsumosListAmyConsumosToAttach.getClass(), amyConsumosListAmyConsumosToAttach.getIdConsumo());
                attachedAmyConsumosList.add(amyConsumosListAmyConsumosToAttach);
            }
            amyMedidores.setAmyConsumosList(attachedAmyConsumosList);
            List<TelcoInfo> attachedTelcoInfoList = new ArrayList<TelcoInfo>();
            for (TelcoInfo telcoInfoListTelcoInfoToAttach : amyMedidores.getTelcoInfoList()) {
                telcoInfoListTelcoInfoToAttach = em.getReference(telcoInfoListTelcoInfoToAttach.getClass(), telcoInfoListTelcoInfoToAttach.getIdSuscriptor());
                attachedTelcoInfoList.add(telcoInfoListTelcoInfoToAttach);
            }
            amyMedidores.setTelcoInfoList(attachedTelcoInfoList);
            List<AmyMedidoresHistorico> attachedAmyMedidoresHistoricoList = new ArrayList<AmyMedidoresHistorico>();
            for (AmyMedidoresHistorico amyMedidoresHistoricoListAmyMedidoresHistoricoToAttach : amyMedidores.getAmyMedidoresHistoricoList()) {
                amyMedidoresHistoricoListAmyMedidoresHistoricoToAttach = em.getReference(amyMedidoresHistoricoListAmyMedidoresHistoricoToAttach.getClass(), amyMedidoresHistoricoListAmyMedidoresHistoricoToAttach.getIdMedidoresHistorico());
                attachedAmyMedidoresHistoricoList.add(amyMedidoresHistoricoListAmyMedidoresHistoricoToAttach);
            }
            amyMedidores.setAmyMedidoresHistoricoList(attachedAmyMedidoresHistoricoList);
            em.persist(amyMedidores);
            if (fkComercialProductos != null) {
                fkComercialProductos.getAmyMedidoresList().add(amyMedidores);
                fkComercialProductos = em.merge(fkComercialProductos);
            }
            if (fkAtributosTiposMedidores != null) {
                fkAtributosTiposMedidores.getAmyMedidoresList().add(amyMedidores);
                fkAtributosTiposMedidores = em.merge(fkAtributosTiposMedidores);
            }
            if (fkAmyCajas != null) {
                fkAmyCajas.getAmyMedidoresList().add(amyMedidores);
                fkAmyCajas = em.merge(fkAmyCajas);
            }
            for (AmyLecturas amyLecturasListAmyLecturas : amyMedidores.getAmyLecturasList()) {
                AmyMedidores oldFkAmyMedidoresOfAmyLecturasListAmyLecturas = amyLecturasListAmyLecturas.getFkAmyMedidores();
                amyLecturasListAmyLecturas.setFkAmyMedidores(amyMedidores);
                amyLecturasListAmyLecturas = em.merge(amyLecturasListAmyLecturas);
                if (oldFkAmyMedidoresOfAmyLecturasListAmyLecturas != null) {
                    oldFkAmyMedidoresOfAmyLecturasListAmyLecturas.getAmyLecturasList().remove(amyLecturasListAmyLecturas);
                    oldFkAmyMedidoresOfAmyLecturasListAmyLecturas = em.merge(oldFkAmyMedidoresOfAmyLecturasListAmyLecturas);
                }
            }
            for (PrepagoSaldos prepagoSaldosListPrepagoSaldos : amyMedidores.getPrepagoSaldosList()) {
                AmyMedidores oldFkAmyMedidoresOfPrepagoSaldosListPrepagoSaldos = prepagoSaldosListPrepagoSaldos.getFkAmyMedidores();
                prepagoSaldosListPrepagoSaldos.setFkAmyMedidores(amyMedidores);
                prepagoSaldosListPrepagoSaldos = em.merge(prepagoSaldosListPrepagoSaldos);
                if (oldFkAmyMedidoresOfPrepagoSaldosListPrepagoSaldos != null) {
                    oldFkAmyMedidoresOfPrepagoSaldosListPrepagoSaldos.getPrepagoSaldosList().remove(prepagoSaldosListPrepagoSaldos);
                    oldFkAmyMedidoresOfPrepagoSaldosListPrepagoSaldos = em.merge(oldFkAmyMedidoresOfPrepagoSaldosListPrepagoSaldos);
                }
            }
            for (AmyEventos amyEventosListAmyEventos : amyMedidores.getAmyEventosList()) {
                AmyMedidores oldFkAmyMedidoresOfAmyEventosListAmyEventos = amyEventosListAmyEventos.getFkAmyMedidores();
                amyEventosListAmyEventos.setFkAmyMedidores(amyMedidores);
                amyEventosListAmyEventos = em.merge(amyEventosListAmyEventos);
                if (oldFkAmyMedidoresOfAmyEventosListAmyEventos != null) {
                    oldFkAmyMedidoresOfAmyEventosListAmyEventos.getAmyEventosList().remove(amyEventosListAmyEventos);
                    oldFkAmyMedidoresOfAmyEventosListAmyEventos = em.merge(oldFkAmyMedidoresOfAmyEventosListAmyEventos);
                }
            }
            for (AmyConsumos amyConsumosListAmyConsumos : amyMedidores.getAmyConsumosList()) {
                AmyMedidores oldFkAmyMedidoresOfAmyConsumosListAmyConsumos = amyConsumosListAmyConsumos.getFkAmyMedidores();
                amyConsumosListAmyConsumos.setFkAmyMedidores(amyMedidores);
                amyConsumosListAmyConsumos = em.merge(amyConsumosListAmyConsumos);
                if (oldFkAmyMedidoresOfAmyConsumosListAmyConsumos != null) {
                    oldFkAmyMedidoresOfAmyConsumosListAmyConsumos.getAmyConsumosList().remove(amyConsumosListAmyConsumos);
                    oldFkAmyMedidoresOfAmyConsumosListAmyConsumos = em.merge(oldFkAmyMedidoresOfAmyConsumosListAmyConsumos);
                }
            }
            for (TelcoInfo telcoInfoListTelcoInfo : amyMedidores.getTelcoInfoList()) {
                AmyMedidores oldFkAmyMedidoresOfTelcoInfoListTelcoInfo = telcoInfoListTelcoInfo.getFkAmyMedidores();
                telcoInfoListTelcoInfo.setFkAmyMedidores(amyMedidores);
                telcoInfoListTelcoInfo = em.merge(telcoInfoListTelcoInfo);
                if (oldFkAmyMedidoresOfTelcoInfoListTelcoInfo != null) {
                    oldFkAmyMedidoresOfTelcoInfoListTelcoInfo.getTelcoInfoList().remove(telcoInfoListTelcoInfo);
                    oldFkAmyMedidoresOfTelcoInfoListTelcoInfo = em.merge(oldFkAmyMedidoresOfTelcoInfoListTelcoInfo);
                }
            }
            for (AmyMedidoresHistorico amyMedidoresHistoricoListAmyMedidoresHistorico : amyMedidores.getAmyMedidoresHistoricoList()) {
                AmyMedidores oldFkAmyMedidoresOfAmyMedidoresHistoricoListAmyMedidoresHistorico = amyMedidoresHistoricoListAmyMedidoresHistorico.getFkAmyMedidores();
                amyMedidoresHistoricoListAmyMedidoresHistorico.setFkAmyMedidores(amyMedidores);
                amyMedidoresHistoricoListAmyMedidoresHistorico = em.merge(amyMedidoresHistoricoListAmyMedidoresHistorico);
                if (oldFkAmyMedidoresOfAmyMedidoresHistoricoListAmyMedidoresHistorico != null) {
                    oldFkAmyMedidoresOfAmyMedidoresHistoricoListAmyMedidoresHistorico.getAmyMedidoresHistoricoList().remove(amyMedidoresHistoricoListAmyMedidoresHistorico);
                    oldFkAmyMedidoresOfAmyMedidoresHistoricoListAmyMedidoresHistorico = em.merge(oldFkAmyMedidoresOfAmyMedidoresHistoricoListAmyMedidoresHistorico);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAmyMedidores(amyMedidores.getIdMedidores()) != null) {
                throw new PreexistingEntityException("AmyMedidores " + amyMedidores + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AmyMedidores amyMedidores) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AmyMedidores persistentAmyMedidores = em.find(AmyMedidores.class, amyMedidores.getIdMedidores());
            ComercialProductos fkComercialProductosOld = persistentAmyMedidores.getFkComercialProductos();
            ComercialProductos fkComercialProductosNew = amyMedidores.getFkComercialProductos();
            AtributosTiposMedidores fkAtributosTiposMedidoresOld = persistentAmyMedidores.getFkAtributosTiposMedidores();
            AtributosTiposMedidores fkAtributosTiposMedidoresNew = amyMedidores.getFkAtributosTiposMedidores();
            AmyCajas fkAmyCajasOld = persistentAmyMedidores.getFkAmyCajas();
            AmyCajas fkAmyCajasNew = amyMedidores.getFkAmyCajas();
            List<AmyLecturas> amyLecturasListOld = persistentAmyMedidores.getAmyLecturasList();
            List<AmyLecturas> amyLecturasListNew = amyMedidores.getAmyLecturasList();
            List<PrepagoSaldos> prepagoSaldosListOld = persistentAmyMedidores.getPrepagoSaldosList();
            List<PrepagoSaldos> prepagoSaldosListNew = amyMedidores.getPrepagoSaldosList();
            List<AmyEventos> amyEventosListOld = persistentAmyMedidores.getAmyEventosList();
            List<AmyEventos> amyEventosListNew = amyMedidores.getAmyEventosList();
            List<AmyConsumos> amyConsumosListOld = persistentAmyMedidores.getAmyConsumosList();
            List<AmyConsumos> amyConsumosListNew = amyMedidores.getAmyConsumosList();
            List<TelcoInfo> telcoInfoListOld = persistentAmyMedidores.getTelcoInfoList();
            List<TelcoInfo> telcoInfoListNew = amyMedidores.getTelcoInfoList();
            List<AmyMedidoresHistorico> amyMedidoresHistoricoListOld = persistentAmyMedidores.getAmyMedidoresHistoricoList();
            List<AmyMedidoresHistorico> amyMedidoresHistoricoListNew = amyMedidores.getAmyMedidoresHistoricoList();
            List<String> illegalOrphanMessages = null;
            for (AmyLecturas amyLecturasListOldAmyLecturas : amyLecturasListOld) {
                if (!amyLecturasListNew.contains(amyLecturasListOldAmyLecturas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyLecturas " + amyLecturasListOldAmyLecturas + " since its fkAmyMedidores field is not nullable.");
                }
            }
            for (PrepagoSaldos prepagoSaldosListOldPrepagoSaldos : prepagoSaldosListOld) {
                if (!prepagoSaldosListNew.contains(prepagoSaldosListOldPrepagoSaldos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PrepagoSaldos " + prepagoSaldosListOldPrepagoSaldos + " since its fkAmyMedidores field is not nullable.");
                }
            }
            for (AmyEventos amyEventosListOldAmyEventos : amyEventosListOld) {
                if (!amyEventosListNew.contains(amyEventosListOldAmyEventos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyEventos " + amyEventosListOldAmyEventos + " since its fkAmyMedidores field is not nullable.");
                }
            }
            for (AmyConsumos amyConsumosListOldAmyConsumos : amyConsumosListOld) {
                if (!amyConsumosListNew.contains(amyConsumosListOldAmyConsumos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyConsumos " + amyConsumosListOldAmyConsumos + " since its fkAmyMedidores field is not nullable.");
                }
            }
            for (TelcoInfo telcoInfoListOldTelcoInfo : telcoInfoListOld) {
                if (!telcoInfoListNew.contains(telcoInfoListOldTelcoInfo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TelcoInfo " + telcoInfoListOldTelcoInfo + " since its fkAmyMedidores field is not nullable.");
                }
            }
            for (AmyMedidoresHistorico amyMedidoresHistoricoListOldAmyMedidoresHistorico : amyMedidoresHistoricoListOld) {
                if (!amyMedidoresHistoricoListNew.contains(amyMedidoresHistoricoListOldAmyMedidoresHistorico)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyMedidoresHistorico " + amyMedidoresHistoricoListOldAmyMedidoresHistorico + " since its fkAmyMedidores field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (fkComercialProductosNew != null) {
                fkComercialProductosNew = em.getReference(fkComercialProductosNew.getClass(), fkComercialProductosNew.getIdProductos());
                amyMedidores.setFkComercialProductos(fkComercialProductosNew);
            }
            if (fkAtributosTiposMedidoresNew != null) {
                fkAtributosTiposMedidoresNew = em.getReference(fkAtributosTiposMedidoresNew.getClass(), fkAtributosTiposMedidoresNew.getIdTiposMedidores());
                amyMedidores.setFkAtributosTiposMedidores(fkAtributosTiposMedidoresNew);
            }
            if (fkAmyCajasNew != null) {
                fkAmyCajasNew = em.getReference(fkAmyCajasNew.getClass(), fkAmyCajasNew.getIdCajas());
                amyMedidores.setFkAmyCajas(fkAmyCajasNew);
            }
            List<AmyLecturas> attachedAmyLecturasListNew = new ArrayList<AmyLecturas>();
            for (AmyLecturas amyLecturasListNewAmyLecturasToAttach : amyLecturasListNew) {
                amyLecturasListNewAmyLecturasToAttach = em.getReference(amyLecturasListNewAmyLecturasToAttach.getClass(), amyLecturasListNewAmyLecturasToAttach.getIdLecturas());
                attachedAmyLecturasListNew.add(amyLecturasListNewAmyLecturasToAttach);
            }
            amyLecturasListNew = attachedAmyLecturasListNew;
            amyMedidores.setAmyLecturasList(amyLecturasListNew);
            List<PrepagoSaldos> attachedPrepagoSaldosListNew = new ArrayList<PrepagoSaldos>();
            for (PrepagoSaldos prepagoSaldosListNewPrepagoSaldosToAttach : prepagoSaldosListNew) {
                prepagoSaldosListNewPrepagoSaldosToAttach = em.getReference(prepagoSaldosListNewPrepagoSaldosToAttach.getClass(), prepagoSaldosListNewPrepagoSaldosToAttach.getIdSaldos());
                attachedPrepagoSaldosListNew.add(prepagoSaldosListNewPrepagoSaldosToAttach);
            }
            prepagoSaldosListNew = attachedPrepagoSaldosListNew;
            amyMedidores.setPrepagoSaldosList(prepagoSaldosListNew);
            List<AmyEventos> attachedAmyEventosListNew = new ArrayList<AmyEventos>();
            for (AmyEventos amyEventosListNewAmyEventosToAttach : amyEventosListNew) {
                amyEventosListNewAmyEventosToAttach = em.getReference(amyEventosListNewAmyEventosToAttach.getClass(), amyEventosListNewAmyEventosToAttach.getIdEvento());
                attachedAmyEventosListNew.add(amyEventosListNewAmyEventosToAttach);
            }
            amyEventosListNew = attachedAmyEventosListNew;
            amyMedidores.setAmyEventosList(amyEventosListNew);
            List<AmyConsumos> attachedAmyConsumosListNew = new ArrayList<AmyConsumos>();
            for (AmyConsumos amyConsumosListNewAmyConsumosToAttach : amyConsumosListNew) {
                amyConsumosListNewAmyConsumosToAttach = em.getReference(amyConsumosListNewAmyConsumosToAttach.getClass(), amyConsumosListNewAmyConsumosToAttach.getIdConsumo());
                attachedAmyConsumosListNew.add(amyConsumosListNewAmyConsumosToAttach);
            }
            amyConsumosListNew = attachedAmyConsumosListNew;
            amyMedidores.setAmyConsumosList(amyConsumosListNew);
            List<TelcoInfo> attachedTelcoInfoListNew = new ArrayList<TelcoInfo>();
            for (TelcoInfo telcoInfoListNewTelcoInfoToAttach : telcoInfoListNew) {
                telcoInfoListNewTelcoInfoToAttach = em.getReference(telcoInfoListNewTelcoInfoToAttach.getClass(), telcoInfoListNewTelcoInfoToAttach.getIdSuscriptor());
                attachedTelcoInfoListNew.add(telcoInfoListNewTelcoInfoToAttach);
            }
            telcoInfoListNew = attachedTelcoInfoListNew;
            amyMedidores.setTelcoInfoList(telcoInfoListNew);
            List<AmyMedidoresHistorico> attachedAmyMedidoresHistoricoListNew = new ArrayList<AmyMedidoresHistorico>();
            for (AmyMedidoresHistorico amyMedidoresHistoricoListNewAmyMedidoresHistoricoToAttach : amyMedidoresHistoricoListNew) {
                amyMedidoresHistoricoListNewAmyMedidoresHistoricoToAttach = em.getReference(amyMedidoresHistoricoListNewAmyMedidoresHistoricoToAttach.getClass(), amyMedidoresHistoricoListNewAmyMedidoresHistoricoToAttach.getIdMedidoresHistorico());
                attachedAmyMedidoresHistoricoListNew.add(amyMedidoresHistoricoListNewAmyMedidoresHistoricoToAttach);
            }
            amyMedidoresHistoricoListNew = attachedAmyMedidoresHistoricoListNew;
            amyMedidores.setAmyMedidoresHistoricoList(amyMedidoresHistoricoListNew);
            amyMedidores = em.merge(amyMedidores);
            if (fkComercialProductosOld != null && !fkComercialProductosOld.equals(fkComercialProductosNew)) {
                fkComercialProductosOld.getAmyMedidoresList().remove(amyMedidores);
                fkComercialProductosOld = em.merge(fkComercialProductosOld);
            }
            if (fkComercialProductosNew != null && !fkComercialProductosNew.equals(fkComercialProductosOld)) {
                fkComercialProductosNew.getAmyMedidoresList().add(amyMedidores);
                fkComercialProductosNew = em.merge(fkComercialProductosNew);
            }
            if (fkAtributosTiposMedidoresOld != null && !fkAtributosTiposMedidoresOld.equals(fkAtributosTiposMedidoresNew)) {
                fkAtributosTiposMedidoresOld.getAmyMedidoresList().remove(amyMedidores);
                fkAtributosTiposMedidoresOld = em.merge(fkAtributosTiposMedidoresOld);
            }
            if (fkAtributosTiposMedidoresNew != null && !fkAtributosTiposMedidoresNew.equals(fkAtributosTiposMedidoresOld)) {
                fkAtributosTiposMedidoresNew.getAmyMedidoresList().add(amyMedidores);
                fkAtributosTiposMedidoresNew = em.merge(fkAtributosTiposMedidoresNew);
            }
            if (fkAmyCajasOld != null && !fkAmyCajasOld.equals(fkAmyCajasNew)) {
                fkAmyCajasOld.getAmyMedidoresList().remove(amyMedidores);
                fkAmyCajasOld = em.merge(fkAmyCajasOld);
            }
            if (fkAmyCajasNew != null && !fkAmyCajasNew.equals(fkAmyCajasOld)) {
                fkAmyCajasNew.getAmyMedidoresList().add(amyMedidores);
                fkAmyCajasNew = em.merge(fkAmyCajasNew);
            }
            for (AmyLecturas amyLecturasListNewAmyLecturas : amyLecturasListNew) {
                if (!amyLecturasListOld.contains(amyLecturasListNewAmyLecturas)) {
                    AmyMedidores oldFkAmyMedidoresOfAmyLecturasListNewAmyLecturas = amyLecturasListNewAmyLecturas.getFkAmyMedidores();
                    amyLecturasListNewAmyLecturas.setFkAmyMedidores(amyMedidores);
                    amyLecturasListNewAmyLecturas = em.merge(amyLecturasListNewAmyLecturas);
                    if (oldFkAmyMedidoresOfAmyLecturasListNewAmyLecturas != null && !oldFkAmyMedidoresOfAmyLecturasListNewAmyLecturas.equals(amyMedidores)) {
                        oldFkAmyMedidoresOfAmyLecturasListNewAmyLecturas.getAmyLecturasList().remove(amyLecturasListNewAmyLecturas);
                        oldFkAmyMedidoresOfAmyLecturasListNewAmyLecturas = em.merge(oldFkAmyMedidoresOfAmyLecturasListNewAmyLecturas);
                    }
                }
            }
            for (PrepagoSaldos prepagoSaldosListNewPrepagoSaldos : prepagoSaldosListNew) {
                if (!prepagoSaldosListOld.contains(prepagoSaldosListNewPrepagoSaldos)) {
                    AmyMedidores oldFkAmyMedidoresOfPrepagoSaldosListNewPrepagoSaldos = prepagoSaldosListNewPrepagoSaldos.getFkAmyMedidores();
                    prepagoSaldosListNewPrepagoSaldos.setFkAmyMedidores(amyMedidores);
                    prepagoSaldosListNewPrepagoSaldos = em.merge(prepagoSaldosListNewPrepagoSaldos);
                    if (oldFkAmyMedidoresOfPrepagoSaldosListNewPrepagoSaldos != null && !oldFkAmyMedidoresOfPrepagoSaldosListNewPrepagoSaldos.equals(amyMedidores)) {
                        oldFkAmyMedidoresOfPrepagoSaldosListNewPrepagoSaldos.getPrepagoSaldosList().remove(prepagoSaldosListNewPrepagoSaldos);
                        oldFkAmyMedidoresOfPrepagoSaldosListNewPrepagoSaldos = em.merge(oldFkAmyMedidoresOfPrepagoSaldosListNewPrepagoSaldos);
                    }
                }
            }
            for (AmyEventos amyEventosListNewAmyEventos : amyEventosListNew) {
                if (!amyEventosListOld.contains(amyEventosListNewAmyEventos)) {
                    AmyMedidores oldFkAmyMedidoresOfAmyEventosListNewAmyEventos = amyEventosListNewAmyEventos.getFkAmyMedidores();
                    amyEventosListNewAmyEventos.setFkAmyMedidores(amyMedidores);
                    amyEventosListNewAmyEventos = em.merge(amyEventosListNewAmyEventos);
                    if (oldFkAmyMedidoresOfAmyEventosListNewAmyEventos != null && !oldFkAmyMedidoresOfAmyEventosListNewAmyEventos.equals(amyMedidores)) {
                        oldFkAmyMedidoresOfAmyEventosListNewAmyEventos.getAmyEventosList().remove(amyEventosListNewAmyEventos);
                        oldFkAmyMedidoresOfAmyEventosListNewAmyEventos = em.merge(oldFkAmyMedidoresOfAmyEventosListNewAmyEventos);
                    }
                }
            }
            for (AmyConsumos amyConsumosListNewAmyConsumos : amyConsumosListNew) {
                if (!amyConsumosListOld.contains(amyConsumosListNewAmyConsumos)) {
                    AmyMedidores oldFkAmyMedidoresOfAmyConsumosListNewAmyConsumos = amyConsumosListNewAmyConsumos.getFkAmyMedidores();
                    amyConsumosListNewAmyConsumos.setFkAmyMedidores(amyMedidores);
                    amyConsumosListNewAmyConsumos = em.merge(amyConsumosListNewAmyConsumos);
                    if (oldFkAmyMedidoresOfAmyConsumosListNewAmyConsumos != null && !oldFkAmyMedidoresOfAmyConsumosListNewAmyConsumos.equals(amyMedidores)) {
                        oldFkAmyMedidoresOfAmyConsumosListNewAmyConsumos.getAmyConsumosList().remove(amyConsumosListNewAmyConsumos);
                        oldFkAmyMedidoresOfAmyConsumosListNewAmyConsumos = em.merge(oldFkAmyMedidoresOfAmyConsumosListNewAmyConsumos);
                    }
                }
            }
            for (TelcoInfo telcoInfoListNewTelcoInfo : telcoInfoListNew) {
                if (!telcoInfoListOld.contains(telcoInfoListNewTelcoInfo)) {
                    AmyMedidores oldFkAmyMedidoresOfTelcoInfoListNewTelcoInfo = telcoInfoListNewTelcoInfo.getFkAmyMedidores();
                    telcoInfoListNewTelcoInfo.setFkAmyMedidores(amyMedidores);
                    telcoInfoListNewTelcoInfo = em.merge(telcoInfoListNewTelcoInfo);
                    if (oldFkAmyMedidoresOfTelcoInfoListNewTelcoInfo != null && !oldFkAmyMedidoresOfTelcoInfoListNewTelcoInfo.equals(amyMedidores)) {
                        oldFkAmyMedidoresOfTelcoInfoListNewTelcoInfo.getTelcoInfoList().remove(telcoInfoListNewTelcoInfo);
                        oldFkAmyMedidoresOfTelcoInfoListNewTelcoInfo = em.merge(oldFkAmyMedidoresOfTelcoInfoListNewTelcoInfo);
                    }
                }
            }
            for (AmyMedidoresHistorico amyMedidoresHistoricoListNewAmyMedidoresHistorico : amyMedidoresHistoricoListNew) {
                if (!amyMedidoresHistoricoListOld.contains(amyMedidoresHistoricoListNewAmyMedidoresHistorico)) {
                    AmyMedidores oldFkAmyMedidoresOfAmyMedidoresHistoricoListNewAmyMedidoresHistorico = amyMedidoresHistoricoListNewAmyMedidoresHistorico.getFkAmyMedidores();
                    amyMedidoresHistoricoListNewAmyMedidoresHistorico.setFkAmyMedidores(amyMedidores);
                    amyMedidoresHistoricoListNewAmyMedidoresHistorico = em.merge(amyMedidoresHistoricoListNewAmyMedidoresHistorico);
                    if (oldFkAmyMedidoresOfAmyMedidoresHistoricoListNewAmyMedidoresHistorico != null && !oldFkAmyMedidoresOfAmyMedidoresHistoricoListNewAmyMedidoresHistorico.equals(amyMedidores)) {
                        oldFkAmyMedidoresOfAmyMedidoresHistoricoListNewAmyMedidoresHistorico.getAmyMedidoresHistoricoList().remove(amyMedidoresHistoricoListNewAmyMedidoresHistorico);
                        oldFkAmyMedidoresOfAmyMedidoresHistoricoListNewAmyMedidoresHistorico = em.merge(oldFkAmyMedidoresOfAmyMedidoresHistoricoListNewAmyMedidoresHistorico);
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
                Long id = amyMedidores.getIdMedidores();
                if (findAmyMedidores(id) == null) {
                    throw new NonexistentEntityException("The amyMedidores with id " + id + " no longer exists.");
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
            AmyMedidores amyMedidores;
            try {
                amyMedidores = em.getReference(AmyMedidores.class, id);
                amyMedidores.getIdMedidores();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The amyMedidores with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<AmyLecturas> amyLecturasListOrphanCheck = amyMedidores.getAmyLecturasList();
            for (AmyLecturas amyLecturasListOrphanCheckAmyLecturas : amyLecturasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyMedidores (" + amyMedidores + ") cannot be destroyed since the AmyLecturas " + amyLecturasListOrphanCheckAmyLecturas + " in its amyLecturasList field has a non-nullable fkAmyMedidores field.");
            }
            List<PrepagoSaldos> prepagoSaldosListOrphanCheck = amyMedidores.getPrepagoSaldosList();
            for (PrepagoSaldos prepagoSaldosListOrphanCheckPrepagoSaldos : prepagoSaldosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyMedidores (" + amyMedidores + ") cannot be destroyed since the PrepagoSaldos " + prepagoSaldosListOrphanCheckPrepagoSaldos + " in its prepagoSaldosList field has a non-nullable fkAmyMedidores field.");
            }
            List<AmyEventos> amyEventosListOrphanCheck = amyMedidores.getAmyEventosList();
            for (AmyEventos amyEventosListOrphanCheckAmyEventos : amyEventosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyMedidores (" + amyMedidores + ") cannot be destroyed since the AmyEventos " + amyEventosListOrphanCheckAmyEventos + " in its amyEventosList field has a non-nullable fkAmyMedidores field.");
            }
            List<AmyConsumos> amyConsumosListOrphanCheck = amyMedidores.getAmyConsumosList();
            for (AmyConsumos amyConsumosListOrphanCheckAmyConsumos : amyConsumosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyMedidores (" + amyMedidores + ") cannot be destroyed since the AmyConsumos " + amyConsumosListOrphanCheckAmyConsumos + " in its amyConsumosList field has a non-nullable fkAmyMedidores field.");
            }
            List<TelcoInfo> telcoInfoListOrphanCheck = amyMedidores.getTelcoInfoList();
            for (TelcoInfo telcoInfoListOrphanCheckTelcoInfo : telcoInfoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyMedidores (" + amyMedidores + ") cannot be destroyed since the TelcoInfo " + telcoInfoListOrphanCheckTelcoInfo + " in its telcoInfoList field has a non-nullable fkAmyMedidores field.");
            }
            List<AmyMedidoresHistorico> amyMedidoresHistoricoListOrphanCheck = amyMedidores.getAmyMedidoresHistoricoList();
            for (AmyMedidoresHistorico amyMedidoresHistoricoListOrphanCheckAmyMedidoresHistorico : amyMedidoresHistoricoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyMedidores (" + amyMedidores + ") cannot be destroyed since the AmyMedidoresHistorico " + amyMedidoresHistoricoListOrphanCheckAmyMedidoresHistorico + " in its amyMedidoresHistoricoList field has a non-nullable fkAmyMedidores field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            ComercialProductos fkComercialProductos = amyMedidores.getFkComercialProductos();
            if (fkComercialProductos != null) {
                fkComercialProductos.getAmyMedidoresList().remove(amyMedidores);
                fkComercialProductos = em.merge(fkComercialProductos);
            }
            AtributosTiposMedidores fkAtributosTiposMedidores = amyMedidores.getFkAtributosTiposMedidores();
            if (fkAtributosTiposMedidores != null) {
                fkAtributosTiposMedidores.getAmyMedidoresList().remove(amyMedidores);
                fkAtributosTiposMedidores = em.merge(fkAtributosTiposMedidores);
            }
            AmyCajas fkAmyCajas = amyMedidores.getFkAmyCajas();
            if (fkAmyCajas != null) {
                fkAmyCajas.getAmyMedidoresList().remove(amyMedidores);
                fkAmyCajas = em.merge(fkAmyCajas);
            }
            em.remove(amyMedidores);
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

    public List<AmyMedidores> findAmyMedidoresEntities() {
        return findAmyMedidoresEntities(true, -1, -1);
    }

    public List<AmyMedidores> findAmyMedidoresEntities(int maxResults, int firstResult) {
        return findAmyMedidoresEntities(false, maxResults, firstResult);
    }

    private List<AmyMedidores> findAmyMedidoresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AmyMedidores.class));
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

    public AmyMedidores findAmyMedidores(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AmyMedidores.class, id);
        } finally {
            em.close();
        }
    }

    public int getAmyMedidoresCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AmyMedidores> rt = cq.from(AmyMedidores.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
