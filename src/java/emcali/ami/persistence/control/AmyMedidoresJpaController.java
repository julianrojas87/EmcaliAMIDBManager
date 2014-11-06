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
import java.util.Collection;
import emcali.ami.persistence.entity.PrepagoSaldos;
import emcali.ami.persistence.entity.AmyEventos;
import emcali.ami.persistence.entity.AmyConsumos;
import emcali.ami.persistence.entity.AmyMedidores;
import emcali.ami.persistence.entity.TelcoInfo;
import emcali.ami.persistence.entity.AmyMedidoresHistorico;
import java.util.List;
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
        if (amyMedidores.getAmyLecturasCollection() == null) {
            amyMedidores.setAmyLecturasCollection(new ArrayList<AmyLecturas>());
        }
        if (amyMedidores.getPrepagoSaldosCollection() == null) {
            amyMedidores.setPrepagoSaldosCollection(new ArrayList<PrepagoSaldos>());
        }
        if (amyMedidores.getAmyEventosCollection() == null) {
            amyMedidores.setAmyEventosCollection(new ArrayList<AmyEventos>());
        }
        if (amyMedidores.getAmyConsumosCollection() == null) {
            amyMedidores.setAmyConsumosCollection(new ArrayList<AmyConsumos>());
        }
        if (amyMedidores.getTelcoInfoCollection() == null) {
            amyMedidores.setTelcoInfoCollection(new ArrayList<TelcoInfo>());
        }
        if (amyMedidores.getAmyMedidoresHistoricoCollection() == null) {
            amyMedidores.setAmyMedidoresHistoricoCollection(new ArrayList<AmyMedidoresHistorico>());
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
            Collection<AmyLecturas> attachedAmyLecturasCollection = new ArrayList<AmyLecturas>();
            for (AmyLecturas amyLecturasCollectionAmyLecturasToAttach : amyMedidores.getAmyLecturasCollection()) {
                amyLecturasCollectionAmyLecturasToAttach = em.getReference(amyLecturasCollectionAmyLecturasToAttach.getClass(), amyLecturasCollectionAmyLecturasToAttach.getIdLecturas());
                attachedAmyLecturasCollection.add(amyLecturasCollectionAmyLecturasToAttach);
            }
            amyMedidores.setAmyLecturasCollection(attachedAmyLecturasCollection);
            Collection<PrepagoSaldos> attachedPrepagoSaldosCollection = new ArrayList<PrepagoSaldos>();
            for (PrepagoSaldos prepagoSaldosCollectionPrepagoSaldosToAttach : amyMedidores.getPrepagoSaldosCollection()) {
                prepagoSaldosCollectionPrepagoSaldosToAttach = em.getReference(prepagoSaldosCollectionPrepagoSaldosToAttach.getClass(), prepagoSaldosCollectionPrepagoSaldosToAttach.getIdSaldos());
                attachedPrepagoSaldosCollection.add(prepagoSaldosCollectionPrepagoSaldosToAttach);
            }
            amyMedidores.setPrepagoSaldosCollection(attachedPrepagoSaldosCollection);
            Collection<AmyEventos> attachedAmyEventosCollection = new ArrayList<AmyEventos>();
            for (AmyEventos amyEventosCollectionAmyEventosToAttach : amyMedidores.getAmyEventosCollection()) {
                amyEventosCollectionAmyEventosToAttach = em.getReference(amyEventosCollectionAmyEventosToAttach.getClass(), amyEventosCollectionAmyEventosToAttach.getIdEvento());
                attachedAmyEventosCollection.add(amyEventosCollectionAmyEventosToAttach);
            }
            amyMedidores.setAmyEventosCollection(attachedAmyEventosCollection);
            Collection<AmyConsumos> attachedAmyConsumosCollection = new ArrayList<AmyConsumos>();
            for (AmyConsumos amyConsumosCollectionAmyConsumosToAttach : amyMedidores.getAmyConsumosCollection()) {
                amyConsumosCollectionAmyConsumosToAttach = em.getReference(amyConsumosCollectionAmyConsumosToAttach.getClass(), amyConsumosCollectionAmyConsumosToAttach.getIdConsumo());
                attachedAmyConsumosCollection.add(amyConsumosCollectionAmyConsumosToAttach);
            }
            amyMedidores.setAmyConsumosCollection(attachedAmyConsumosCollection);
            Collection<TelcoInfo> attachedTelcoInfoCollection = new ArrayList<TelcoInfo>();
            for (TelcoInfo telcoInfoCollectionTelcoInfoToAttach : amyMedidores.getTelcoInfoCollection()) {
                telcoInfoCollectionTelcoInfoToAttach = em.getReference(telcoInfoCollectionTelcoInfoToAttach.getClass(), telcoInfoCollectionTelcoInfoToAttach.getIdSuscriptor());
                attachedTelcoInfoCollection.add(telcoInfoCollectionTelcoInfoToAttach);
            }
            amyMedidores.setTelcoInfoCollection(attachedTelcoInfoCollection);
            Collection<AmyMedidoresHistorico> attachedAmyMedidoresHistoricoCollection = new ArrayList<AmyMedidoresHistorico>();
            for (AmyMedidoresHistorico amyMedidoresHistoricoCollectionAmyMedidoresHistoricoToAttach : amyMedidores.getAmyMedidoresHistoricoCollection()) {
                amyMedidoresHistoricoCollectionAmyMedidoresHistoricoToAttach = em.getReference(amyMedidoresHistoricoCollectionAmyMedidoresHistoricoToAttach.getClass(), amyMedidoresHistoricoCollectionAmyMedidoresHistoricoToAttach.getIdMedidoresHistorico());
                attachedAmyMedidoresHistoricoCollection.add(amyMedidoresHistoricoCollectionAmyMedidoresHistoricoToAttach);
            }
            amyMedidores.setAmyMedidoresHistoricoCollection(attachedAmyMedidoresHistoricoCollection);
            em.persist(amyMedidores);
            if (fkComercialProductos != null) {
                fkComercialProductos.getAmyMedidoresCollection().add(amyMedidores);
                fkComercialProductos = em.merge(fkComercialProductos);
            }
            if (fkAtributosTiposMedidores != null) {
                fkAtributosTiposMedidores.getAmyMedidoresCollection().add(amyMedidores);
                fkAtributosTiposMedidores = em.merge(fkAtributosTiposMedidores);
            }
            if (fkAmyCajas != null) {
                fkAmyCajas.getAmyMedidoresCollection().add(amyMedidores);
                fkAmyCajas = em.merge(fkAmyCajas);
            }
            for (AmyLecturas amyLecturasCollectionAmyLecturas : amyMedidores.getAmyLecturasCollection()) {
                AmyMedidores oldFkAmyMedidoresOfAmyLecturasCollectionAmyLecturas = amyLecturasCollectionAmyLecturas.getFkAmyMedidores();
                amyLecturasCollectionAmyLecturas.setFkAmyMedidores(amyMedidores);
                amyLecturasCollectionAmyLecturas = em.merge(amyLecturasCollectionAmyLecturas);
                if (oldFkAmyMedidoresOfAmyLecturasCollectionAmyLecturas != null) {
                    oldFkAmyMedidoresOfAmyLecturasCollectionAmyLecturas.getAmyLecturasCollection().remove(amyLecturasCollectionAmyLecturas);
                    oldFkAmyMedidoresOfAmyLecturasCollectionAmyLecturas = em.merge(oldFkAmyMedidoresOfAmyLecturasCollectionAmyLecturas);
                }
            }
            for (PrepagoSaldos prepagoSaldosCollectionPrepagoSaldos : amyMedidores.getPrepagoSaldosCollection()) {
                AmyMedidores oldFkAmyMedidoresOfPrepagoSaldosCollectionPrepagoSaldos = prepagoSaldosCollectionPrepagoSaldos.getFkAmyMedidores();
                prepagoSaldosCollectionPrepagoSaldos.setFkAmyMedidores(amyMedidores);
                prepagoSaldosCollectionPrepagoSaldos = em.merge(prepagoSaldosCollectionPrepagoSaldos);
                if (oldFkAmyMedidoresOfPrepagoSaldosCollectionPrepagoSaldos != null) {
                    oldFkAmyMedidoresOfPrepagoSaldosCollectionPrepagoSaldos.getPrepagoSaldosCollection().remove(prepagoSaldosCollectionPrepagoSaldos);
                    oldFkAmyMedidoresOfPrepagoSaldosCollectionPrepagoSaldos = em.merge(oldFkAmyMedidoresOfPrepagoSaldosCollectionPrepagoSaldos);
                }
            }
            for (AmyEventos amyEventosCollectionAmyEventos : amyMedidores.getAmyEventosCollection()) {
                AmyMedidores oldFkAmyMedidoresOfAmyEventosCollectionAmyEventos = amyEventosCollectionAmyEventos.getFkAmyMedidores();
                amyEventosCollectionAmyEventos.setFkAmyMedidores(amyMedidores);
                amyEventosCollectionAmyEventos = em.merge(amyEventosCollectionAmyEventos);
                if (oldFkAmyMedidoresOfAmyEventosCollectionAmyEventos != null) {
                    oldFkAmyMedidoresOfAmyEventosCollectionAmyEventos.getAmyEventosCollection().remove(amyEventosCollectionAmyEventos);
                    oldFkAmyMedidoresOfAmyEventosCollectionAmyEventos = em.merge(oldFkAmyMedidoresOfAmyEventosCollectionAmyEventos);
                }
            }
            for (AmyConsumos amyConsumosCollectionAmyConsumos : amyMedidores.getAmyConsumosCollection()) {
                AmyMedidores oldFkAmyMedidoresOfAmyConsumosCollectionAmyConsumos = amyConsumosCollectionAmyConsumos.getFkAmyMedidores();
                amyConsumosCollectionAmyConsumos.setFkAmyMedidores(amyMedidores);
                amyConsumosCollectionAmyConsumos = em.merge(amyConsumosCollectionAmyConsumos);
                if (oldFkAmyMedidoresOfAmyConsumosCollectionAmyConsumos != null) {
                    oldFkAmyMedidoresOfAmyConsumosCollectionAmyConsumos.getAmyConsumosCollection().remove(amyConsumosCollectionAmyConsumos);
                    oldFkAmyMedidoresOfAmyConsumosCollectionAmyConsumos = em.merge(oldFkAmyMedidoresOfAmyConsumosCollectionAmyConsumos);
                }
            }
            for (TelcoInfo telcoInfoCollectionTelcoInfo : amyMedidores.getTelcoInfoCollection()) {
                AmyMedidores oldFkAmyMedidoresOfTelcoInfoCollectionTelcoInfo = telcoInfoCollectionTelcoInfo.getFkAmyMedidores();
                telcoInfoCollectionTelcoInfo.setFkAmyMedidores(amyMedidores);
                telcoInfoCollectionTelcoInfo = em.merge(telcoInfoCollectionTelcoInfo);
                if (oldFkAmyMedidoresOfTelcoInfoCollectionTelcoInfo != null) {
                    oldFkAmyMedidoresOfTelcoInfoCollectionTelcoInfo.getTelcoInfoCollection().remove(telcoInfoCollectionTelcoInfo);
                    oldFkAmyMedidoresOfTelcoInfoCollectionTelcoInfo = em.merge(oldFkAmyMedidoresOfTelcoInfoCollectionTelcoInfo);
                }
            }
            for (AmyMedidoresHistorico amyMedidoresHistoricoCollectionAmyMedidoresHistorico : amyMedidores.getAmyMedidoresHistoricoCollection()) {
                AmyMedidores oldFkAmyMedidoresOfAmyMedidoresHistoricoCollectionAmyMedidoresHistorico = amyMedidoresHistoricoCollectionAmyMedidoresHistorico.getFkAmyMedidores();
                amyMedidoresHistoricoCollectionAmyMedidoresHistorico.setFkAmyMedidores(amyMedidores);
                amyMedidoresHistoricoCollectionAmyMedidoresHistorico = em.merge(amyMedidoresHistoricoCollectionAmyMedidoresHistorico);
                if (oldFkAmyMedidoresOfAmyMedidoresHistoricoCollectionAmyMedidoresHistorico != null) {
                    oldFkAmyMedidoresOfAmyMedidoresHistoricoCollectionAmyMedidoresHistorico.getAmyMedidoresHistoricoCollection().remove(amyMedidoresHistoricoCollectionAmyMedidoresHistorico);
                    oldFkAmyMedidoresOfAmyMedidoresHistoricoCollectionAmyMedidoresHistorico = em.merge(oldFkAmyMedidoresOfAmyMedidoresHistoricoCollectionAmyMedidoresHistorico);
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
            Collection<AmyLecturas> amyLecturasCollectionOld = persistentAmyMedidores.getAmyLecturasCollection();
            Collection<AmyLecturas> amyLecturasCollectionNew = amyMedidores.getAmyLecturasCollection();
            Collection<PrepagoSaldos> prepagoSaldosCollectionOld = persistentAmyMedidores.getPrepagoSaldosCollection();
            Collection<PrepagoSaldos> prepagoSaldosCollectionNew = amyMedidores.getPrepagoSaldosCollection();
            Collection<AmyEventos> amyEventosCollectionOld = persistentAmyMedidores.getAmyEventosCollection();
            Collection<AmyEventos> amyEventosCollectionNew = amyMedidores.getAmyEventosCollection();
            Collection<AmyConsumos> amyConsumosCollectionOld = persistentAmyMedidores.getAmyConsumosCollection();
            Collection<AmyConsumos> amyConsumosCollectionNew = amyMedidores.getAmyConsumosCollection();
            Collection<TelcoInfo> telcoInfoCollectionOld = persistentAmyMedidores.getTelcoInfoCollection();
            Collection<TelcoInfo> telcoInfoCollectionNew = amyMedidores.getTelcoInfoCollection();
            Collection<AmyMedidoresHistorico> amyMedidoresHistoricoCollectionOld = persistentAmyMedidores.getAmyMedidoresHistoricoCollection();
            Collection<AmyMedidoresHistorico> amyMedidoresHistoricoCollectionNew = amyMedidores.getAmyMedidoresHistoricoCollection();
            List<String> illegalOrphanMessages = null;
            for (AmyLecturas amyLecturasCollectionOldAmyLecturas : amyLecturasCollectionOld) {
                if (!amyLecturasCollectionNew.contains(amyLecturasCollectionOldAmyLecturas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyLecturas " + amyLecturasCollectionOldAmyLecturas + " since its fkAmyMedidores field is not nullable.");
                }
            }
            for (PrepagoSaldos prepagoSaldosCollectionOldPrepagoSaldos : prepagoSaldosCollectionOld) {
                if (!prepagoSaldosCollectionNew.contains(prepagoSaldosCollectionOldPrepagoSaldos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PrepagoSaldos " + prepagoSaldosCollectionOldPrepagoSaldos + " since its fkAmyMedidores field is not nullable.");
                }
            }
            for (AmyEventos amyEventosCollectionOldAmyEventos : amyEventosCollectionOld) {
                if (!amyEventosCollectionNew.contains(amyEventosCollectionOldAmyEventos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyEventos " + amyEventosCollectionOldAmyEventos + " since its fkAmyMedidores field is not nullable.");
                }
            }
            for (AmyConsumos amyConsumosCollectionOldAmyConsumos : amyConsumosCollectionOld) {
                if (!amyConsumosCollectionNew.contains(amyConsumosCollectionOldAmyConsumos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyConsumos " + amyConsumosCollectionOldAmyConsumos + " since its fkAmyMedidores field is not nullable.");
                }
            }
            for (TelcoInfo telcoInfoCollectionOldTelcoInfo : telcoInfoCollectionOld) {
                if (!telcoInfoCollectionNew.contains(telcoInfoCollectionOldTelcoInfo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TelcoInfo " + telcoInfoCollectionOldTelcoInfo + " since its fkAmyMedidores field is not nullable.");
                }
            }
            for (AmyMedidoresHistorico amyMedidoresHistoricoCollectionOldAmyMedidoresHistorico : amyMedidoresHistoricoCollectionOld) {
                if (!amyMedidoresHistoricoCollectionNew.contains(amyMedidoresHistoricoCollectionOldAmyMedidoresHistorico)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AmyMedidoresHistorico " + amyMedidoresHistoricoCollectionOldAmyMedidoresHistorico + " since its fkAmyMedidores field is not nullable.");
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
            Collection<AmyLecturas> attachedAmyLecturasCollectionNew = new ArrayList<AmyLecturas>();
            for (AmyLecturas amyLecturasCollectionNewAmyLecturasToAttach : amyLecturasCollectionNew) {
                amyLecturasCollectionNewAmyLecturasToAttach = em.getReference(amyLecturasCollectionNewAmyLecturasToAttach.getClass(), amyLecturasCollectionNewAmyLecturasToAttach.getIdLecturas());
                attachedAmyLecturasCollectionNew.add(amyLecturasCollectionNewAmyLecturasToAttach);
            }
            amyLecturasCollectionNew = attachedAmyLecturasCollectionNew;
            amyMedidores.setAmyLecturasCollection(amyLecturasCollectionNew);
            Collection<PrepagoSaldos> attachedPrepagoSaldosCollectionNew = new ArrayList<PrepagoSaldos>();
            for (PrepagoSaldos prepagoSaldosCollectionNewPrepagoSaldosToAttach : prepagoSaldosCollectionNew) {
                prepagoSaldosCollectionNewPrepagoSaldosToAttach = em.getReference(prepagoSaldosCollectionNewPrepagoSaldosToAttach.getClass(), prepagoSaldosCollectionNewPrepagoSaldosToAttach.getIdSaldos());
                attachedPrepagoSaldosCollectionNew.add(prepagoSaldosCollectionNewPrepagoSaldosToAttach);
            }
            prepagoSaldosCollectionNew = attachedPrepagoSaldosCollectionNew;
            amyMedidores.setPrepagoSaldosCollection(prepagoSaldosCollectionNew);
            Collection<AmyEventos> attachedAmyEventosCollectionNew = new ArrayList<AmyEventos>();
            for (AmyEventos amyEventosCollectionNewAmyEventosToAttach : amyEventosCollectionNew) {
                amyEventosCollectionNewAmyEventosToAttach = em.getReference(amyEventosCollectionNewAmyEventosToAttach.getClass(), amyEventosCollectionNewAmyEventosToAttach.getIdEvento());
                attachedAmyEventosCollectionNew.add(amyEventosCollectionNewAmyEventosToAttach);
            }
            amyEventosCollectionNew = attachedAmyEventosCollectionNew;
            amyMedidores.setAmyEventosCollection(amyEventosCollectionNew);
            Collection<AmyConsumos> attachedAmyConsumosCollectionNew = new ArrayList<AmyConsumos>();
            for (AmyConsumos amyConsumosCollectionNewAmyConsumosToAttach : amyConsumosCollectionNew) {
                amyConsumosCollectionNewAmyConsumosToAttach = em.getReference(amyConsumosCollectionNewAmyConsumosToAttach.getClass(), amyConsumosCollectionNewAmyConsumosToAttach.getIdConsumo());
                attachedAmyConsumosCollectionNew.add(amyConsumosCollectionNewAmyConsumosToAttach);
            }
            amyConsumosCollectionNew = attachedAmyConsumosCollectionNew;
            amyMedidores.setAmyConsumosCollection(amyConsumosCollectionNew);
            Collection<TelcoInfo> attachedTelcoInfoCollectionNew = new ArrayList<TelcoInfo>();
            for (TelcoInfo telcoInfoCollectionNewTelcoInfoToAttach : telcoInfoCollectionNew) {
                telcoInfoCollectionNewTelcoInfoToAttach = em.getReference(telcoInfoCollectionNewTelcoInfoToAttach.getClass(), telcoInfoCollectionNewTelcoInfoToAttach.getIdSuscriptor());
                attachedTelcoInfoCollectionNew.add(telcoInfoCollectionNewTelcoInfoToAttach);
            }
            telcoInfoCollectionNew = attachedTelcoInfoCollectionNew;
            amyMedidores.setTelcoInfoCollection(telcoInfoCollectionNew);
            Collection<AmyMedidoresHistorico> attachedAmyMedidoresHistoricoCollectionNew = new ArrayList<AmyMedidoresHistorico>();
            for (AmyMedidoresHistorico amyMedidoresHistoricoCollectionNewAmyMedidoresHistoricoToAttach : amyMedidoresHistoricoCollectionNew) {
                amyMedidoresHistoricoCollectionNewAmyMedidoresHistoricoToAttach = em.getReference(amyMedidoresHistoricoCollectionNewAmyMedidoresHistoricoToAttach.getClass(), amyMedidoresHistoricoCollectionNewAmyMedidoresHistoricoToAttach.getIdMedidoresHistorico());
                attachedAmyMedidoresHistoricoCollectionNew.add(amyMedidoresHistoricoCollectionNewAmyMedidoresHistoricoToAttach);
            }
            amyMedidoresHistoricoCollectionNew = attachedAmyMedidoresHistoricoCollectionNew;
            amyMedidores.setAmyMedidoresHistoricoCollection(amyMedidoresHistoricoCollectionNew);
            amyMedidores = em.merge(amyMedidores);
            if (fkComercialProductosOld != null && !fkComercialProductosOld.equals(fkComercialProductosNew)) {
                fkComercialProductosOld.getAmyMedidoresCollection().remove(amyMedidores);
                fkComercialProductosOld = em.merge(fkComercialProductosOld);
            }
            if (fkComercialProductosNew != null && !fkComercialProductosNew.equals(fkComercialProductosOld)) {
                fkComercialProductosNew.getAmyMedidoresCollection().add(amyMedidores);
                fkComercialProductosNew = em.merge(fkComercialProductosNew);
            }
            if (fkAtributosTiposMedidoresOld != null && !fkAtributosTiposMedidoresOld.equals(fkAtributosTiposMedidoresNew)) {
                fkAtributosTiposMedidoresOld.getAmyMedidoresCollection().remove(amyMedidores);
                fkAtributosTiposMedidoresOld = em.merge(fkAtributosTiposMedidoresOld);
            }
            if (fkAtributosTiposMedidoresNew != null && !fkAtributosTiposMedidoresNew.equals(fkAtributosTiposMedidoresOld)) {
                fkAtributosTiposMedidoresNew.getAmyMedidoresCollection().add(amyMedidores);
                fkAtributosTiposMedidoresNew = em.merge(fkAtributosTiposMedidoresNew);
            }
            if (fkAmyCajasOld != null && !fkAmyCajasOld.equals(fkAmyCajasNew)) {
                fkAmyCajasOld.getAmyMedidoresCollection().remove(amyMedidores);
                fkAmyCajasOld = em.merge(fkAmyCajasOld);
            }
            if (fkAmyCajasNew != null && !fkAmyCajasNew.equals(fkAmyCajasOld)) {
                fkAmyCajasNew.getAmyMedidoresCollection().add(amyMedidores);
                fkAmyCajasNew = em.merge(fkAmyCajasNew);
            }
            for (AmyLecturas amyLecturasCollectionNewAmyLecturas : amyLecturasCollectionNew) {
                if (!amyLecturasCollectionOld.contains(amyLecturasCollectionNewAmyLecturas)) {
                    AmyMedidores oldFkAmyMedidoresOfAmyLecturasCollectionNewAmyLecturas = amyLecturasCollectionNewAmyLecturas.getFkAmyMedidores();
                    amyLecturasCollectionNewAmyLecturas.setFkAmyMedidores(amyMedidores);
                    amyLecturasCollectionNewAmyLecturas = em.merge(amyLecturasCollectionNewAmyLecturas);
                    if (oldFkAmyMedidoresOfAmyLecturasCollectionNewAmyLecturas != null && !oldFkAmyMedidoresOfAmyLecturasCollectionNewAmyLecturas.equals(amyMedidores)) {
                        oldFkAmyMedidoresOfAmyLecturasCollectionNewAmyLecturas.getAmyLecturasCollection().remove(amyLecturasCollectionNewAmyLecturas);
                        oldFkAmyMedidoresOfAmyLecturasCollectionNewAmyLecturas = em.merge(oldFkAmyMedidoresOfAmyLecturasCollectionNewAmyLecturas);
                    }
                }
            }
            for (PrepagoSaldos prepagoSaldosCollectionNewPrepagoSaldos : prepagoSaldosCollectionNew) {
                if (!prepagoSaldosCollectionOld.contains(prepagoSaldosCollectionNewPrepagoSaldos)) {
                    AmyMedidores oldFkAmyMedidoresOfPrepagoSaldosCollectionNewPrepagoSaldos = prepagoSaldosCollectionNewPrepagoSaldos.getFkAmyMedidores();
                    prepagoSaldosCollectionNewPrepagoSaldos.setFkAmyMedidores(amyMedidores);
                    prepagoSaldosCollectionNewPrepagoSaldos = em.merge(prepagoSaldosCollectionNewPrepagoSaldos);
                    if (oldFkAmyMedidoresOfPrepagoSaldosCollectionNewPrepagoSaldos != null && !oldFkAmyMedidoresOfPrepagoSaldosCollectionNewPrepagoSaldos.equals(amyMedidores)) {
                        oldFkAmyMedidoresOfPrepagoSaldosCollectionNewPrepagoSaldos.getPrepagoSaldosCollection().remove(prepagoSaldosCollectionNewPrepagoSaldos);
                        oldFkAmyMedidoresOfPrepagoSaldosCollectionNewPrepagoSaldos = em.merge(oldFkAmyMedidoresOfPrepagoSaldosCollectionNewPrepagoSaldos);
                    }
                }
            }
            for (AmyEventos amyEventosCollectionNewAmyEventos : amyEventosCollectionNew) {
                if (!amyEventosCollectionOld.contains(amyEventosCollectionNewAmyEventos)) {
                    AmyMedidores oldFkAmyMedidoresOfAmyEventosCollectionNewAmyEventos = amyEventosCollectionNewAmyEventos.getFkAmyMedidores();
                    amyEventosCollectionNewAmyEventos.setFkAmyMedidores(amyMedidores);
                    amyEventosCollectionNewAmyEventos = em.merge(amyEventosCollectionNewAmyEventos);
                    if (oldFkAmyMedidoresOfAmyEventosCollectionNewAmyEventos != null && !oldFkAmyMedidoresOfAmyEventosCollectionNewAmyEventos.equals(amyMedidores)) {
                        oldFkAmyMedidoresOfAmyEventosCollectionNewAmyEventos.getAmyEventosCollection().remove(amyEventosCollectionNewAmyEventos);
                        oldFkAmyMedidoresOfAmyEventosCollectionNewAmyEventos = em.merge(oldFkAmyMedidoresOfAmyEventosCollectionNewAmyEventos);
                    }
                }
            }
            for (AmyConsumos amyConsumosCollectionNewAmyConsumos : amyConsumosCollectionNew) {
                if (!amyConsumosCollectionOld.contains(amyConsumosCollectionNewAmyConsumos)) {
                    AmyMedidores oldFkAmyMedidoresOfAmyConsumosCollectionNewAmyConsumos = amyConsumosCollectionNewAmyConsumos.getFkAmyMedidores();
                    amyConsumosCollectionNewAmyConsumos.setFkAmyMedidores(amyMedidores);
                    amyConsumosCollectionNewAmyConsumos = em.merge(amyConsumosCollectionNewAmyConsumos);
                    if (oldFkAmyMedidoresOfAmyConsumosCollectionNewAmyConsumos != null && !oldFkAmyMedidoresOfAmyConsumosCollectionNewAmyConsumos.equals(amyMedidores)) {
                        oldFkAmyMedidoresOfAmyConsumosCollectionNewAmyConsumos.getAmyConsumosCollection().remove(amyConsumosCollectionNewAmyConsumos);
                        oldFkAmyMedidoresOfAmyConsumosCollectionNewAmyConsumos = em.merge(oldFkAmyMedidoresOfAmyConsumosCollectionNewAmyConsumos);
                    }
                }
            }
            for (TelcoInfo telcoInfoCollectionNewTelcoInfo : telcoInfoCollectionNew) {
                if (!telcoInfoCollectionOld.contains(telcoInfoCollectionNewTelcoInfo)) {
                    AmyMedidores oldFkAmyMedidoresOfTelcoInfoCollectionNewTelcoInfo = telcoInfoCollectionNewTelcoInfo.getFkAmyMedidores();
                    telcoInfoCollectionNewTelcoInfo.setFkAmyMedidores(amyMedidores);
                    telcoInfoCollectionNewTelcoInfo = em.merge(telcoInfoCollectionNewTelcoInfo);
                    if (oldFkAmyMedidoresOfTelcoInfoCollectionNewTelcoInfo != null && !oldFkAmyMedidoresOfTelcoInfoCollectionNewTelcoInfo.equals(amyMedidores)) {
                        oldFkAmyMedidoresOfTelcoInfoCollectionNewTelcoInfo.getTelcoInfoCollection().remove(telcoInfoCollectionNewTelcoInfo);
                        oldFkAmyMedidoresOfTelcoInfoCollectionNewTelcoInfo = em.merge(oldFkAmyMedidoresOfTelcoInfoCollectionNewTelcoInfo);
                    }
                }
            }
            for (AmyMedidoresHistorico amyMedidoresHistoricoCollectionNewAmyMedidoresHistorico : amyMedidoresHistoricoCollectionNew) {
                if (!amyMedidoresHistoricoCollectionOld.contains(amyMedidoresHistoricoCollectionNewAmyMedidoresHistorico)) {
                    AmyMedidores oldFkAmyMedidoresOfAmyMedidoresHistoricoCollectionNewAmyMedidoresHistorico = amyMedidoresHistoricoCollectionNewAmyMedidoresHistorico.getFkAmyMedidores();
                    amyMedidoresHistoricoCollectionNewAmyMedidoresHistorico.setFkAmyMedidores(amyMedidores);
                    amyMedidoresHistoricoCollectionNewAmyMedidoresHistorico = em.merge(amyMedidoresHistoricoCollectionNewAmyMedidoresHistorico);
                    if (oldFkAmyMedidoresOfAmyMedidoresHistoricoCollectionNewAmyMedidoresHistorico != null && !oldFkAmyMedidoresOfAmyMedidoresHistoricoCollectionNewAmyMedidoresHistorico.equals(amyMedidores)) {
                        oldFkAmyMedidoresOfAmyMedidoresHistoricoCollectionNewAmyMedidoresHistorico.getAmyMedidoresHistoricoCollection().remove(amyMedidoresHistoricoCollectionNewAmyMedidoresHistorico);
                        oldFkAmyMedidoresOfAmyMedidoresHistoricoCollectionNewAmyMedidoresHistorico = em.merge(oldFkAmyMedidoresOfAmyMedidoresHistoricoCollectionNewAmyMedidoresHistorico);
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
            Collection<AmyLecturas> amyLecturasCollectionOrphanCheck = amyMedidores.getAmyLecturasCollection();
            for (AmyLecturas amyLecturasCollectionOrphanCheckAmyLecturas : amyLecturasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyMedidores (" + amyMedidores + ") cannot be destroyed since the AmyLecturas " + amyLecturasCollectionOrphanCheckAmyLecturas + " in its amyLecturasCollection field has a non-nullable fkAmyMedidores field.");
            }
            Collection<PrepagoSaldos> prepagoSaldosCollectionOrphanCheck = amyMedidores.getPrepagoSaldosCollection();
            for (PrepagoSaldos prepagoSaldosCollectionOrphanCheckPrepagoSaldos : prepagoSaldosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyMedidores (" + amyMedidores + ") cannot be destroyed since the PrepagoSaldos " + prepagoSaldosCollectionOrphanCheckPrepagoSaldos + " in its prepagoSaldosCollection field has a non-nullable fkAmyMedidores field.");
            }
            Collection<AmyEventos> amyEventosCollectionOrphanCheck = amyMedidores.getAmyEventosCollection();
            for (AmyEventos amyEventosCollectionOrphanCheckAmyEventos : amyEventosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyMedidores (" + amyMedidores + ") cannot be destroyed since the AmyEventos " + amyEventosCollectionOrphanCheckAmyEventos + " in its amyEventosCollection field has a non-nullable fkAmyMedidores field.");
            }
            Collection<AmyConsumos> amyConsumosCollectionOrphanCheck = amyMedidores.getAmyConsumosCollection();
            for (AmyConsumos amyConsumosCollectionOrphanCheckAmyConsumos : amyConsumosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyMedidores (" + amyMedidores + ") cannot be destroyed since the AmyConsumos " + amyConsumosCollectionOrphanCheckAmyConsumos + " in its amyConsumosCollection field has a non-nullable fkAmyMedidores field.");
            }
            Collection<TelcoInfo> telcoInfoCollectionOrphanCheck = amyMedidores.getTelcoInfoCollection();
            for (TelcoInfo telcoInfoCollectionOrphanCheckTelcoInfo : telcoInfoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyMedidores (" + amyMedidores + ") cannot be destroyed since the TelcoInfo " + telcoInfoCollectionOrphanCheckTelcoInfo + " in its telcoInfoCollection field has a non-nullable fkAmyMedidores field.");
            }
            Collection<AmyMedidoresHistorico> amyMedidoresHistoricoCollectionOrphanCheck = amyMedidores.getAmyMedidoresHistoricoCollection();
            for (AmyMedidoresHistorico amyMedidoresHistoricoCollectionOrphanCheckAmyMedidoresHistorico : amyMedidoresHistoricoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AmyMedidores (" + amyMedidores + ") cannot be destroyed since the AmyMedidoresHistorico " + amyMedidoresHistoricoCollectionOrphanCheckAmyMedidoresHistorico + " in its amyMedidoresHistoricoCollection field has a non-nullable fkAmyMedidores field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            ComercialProductos fkComercialProductos = amyMedidores.getFkComercialProductos();
            if (fkComercialProductos != null) {
                fkComercialProductos.getAmyMedidoresCollection().remove(amyMedidores);
                fkComercialProductos = em.merge(fkComercialProductos);
            }
            AtributosTiposMedidores fkAtributosTiposMedidores = amyMedidores.getFkAtributosTiposMedidores();
            if (fkAtributosTiposMedidores != null) {
                fkAtributosTiposMedidores.getAmyMedidoresCollection().remove(amyMedidores);
                fkAtributosTiposMedidores = em.merge(fkAtributosTiposMedidores);
            }
            AmyCajas fkAmyCajas = amyMedidores.getFkAmyCajas();
            if (fkAmyCajas != null) {
                fkAmyCajas.getAmyMedidoresCollection().remove(amyMedidores);
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
