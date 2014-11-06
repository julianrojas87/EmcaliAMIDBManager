/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emcali.ami.persistence.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author julian
 */
@Entity
@Table(name = "AMY_MEDIDORES", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmyMedidores.findAll", query = "SELECT a FROM AmyMedidores a"),
    @NamedQuery(name = "AmyMedidores.findByIdMedidores", query = "SELECT a FROM AmyMedidores a WHERE a.idMedidores = :idMedidores"),
    @NamedQuery(name = "AmyMedidores.findByMac", query = "SELECT a FROM AmyMedidores a WHERE a.mac = :mac"),
    @NamedQuery(name = "AmyMedidores.findBySerial", query = "SELECT a FROM AmyMedidores a WHERE a.serial = :serial"),
    @NamedQuery(name = "AmyMedidores.findByFactorCorriente", query = "SELECT a FROM AmyMedidores a WHERE a.factorCorriente = :factorCorriente"),
    @NamedQuery(name = "AmyMedidores.findByFactorVoltaje", query = "SELECT a FROM AmyMedidores a WHERE a.factorVoltaje = :factorVoltaje"),
    @NamedQuery(name = "AmyMedidores.findBySlot", query = "SELECT a FROM AmyMedidores a WHERE a.slot = :slot"),
    @NamedQuery(name = "AmyMedidores.findByLatitud", query = "SELECT a FROM AmyMedidores a WHERE a.latitud = :latitud"),
    @NamedQuery(name = "AmyMedidores.findByLongitud", query = "SELECT a FROM AmyMedidores a WHERE a.longitud = :longitud")})
public class AmyMedidores implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_MEDIDORES")
    private Long idMedidores;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "MAC")
    private String mac;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "SERIAL")
    private String serial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FACTOR_CORRIENTE")
    private double factorCorriente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FACTOR_VOLTAJE")
    private double factorVoltaje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SLOT")
    private long slot;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "LATITUD")
    private Double latitud;
    @Column(name = "LONGITUD")
    private Double longitud;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAmyMedidores")
    private Collection<AmyLecturas> amyLecturasCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAmyMedidores")
    private Collection<PrepagoSaldos> prepagoSaldosCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAmyMedidores")
    private Collection<AmyEventos> amyEventosCollection;
    @JoinColumn(name = "FK_COMERCIAL_PRODUCTOS", referencedColumnName = "ID_PRODUCTOS")
    @ManyToOne(optional = false)
    private ComercialProductos fkComercialProductos;
    @JoinColumn(name = "FK_ATRIBUTOS_TIPOS_MEDIDORES", referencedColumnName = "ID_TIPOS_MEDIDORES")
    @ManyToOne(optional = false)
    private AtributosTiposMedidores fkAtributosTiposMedidores;
    @JoinColumn(name = "FK_AMY_CAJAS", referencedColumnName = "ID_CAJAS")
    @ManyToOne(optional = false)
    private AmyCajas fkAmyCajas;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAmyMedidores")
    private Collection<AmyConsumos> amyConsumosCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAmyMedidores")
    private Collection<TelcoInfo> telcoInfoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAmyMedidores")
    private Collection<AmyMedidoresHistorico> amyMedidoresHistoricoCollection;

    public AmyMedidores() {
    }

    public AmyMedidores(Long idMedidores) {
        this.idMedidores = idMedidores;
    }

    public AmyMedidores(Long idMedidores, String mac, String serial, double factorCorriente, double factorVoltaje, long slot) {
        this.idMedidores = idMedidores;
        this.mac = mac;
        this.serial = serial;
        this.factorCorriente = factorCorriente;
        this.factorVoltaje = factorVoltaje;
        this.slot = slot;
    }

    public Long getIdMedidores() {
        return idMedidores;
    }

    public void setIdMedidores(Long idMedidores) {
        this.idMedidores = idMedidores;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public double getFactorCorriente() {
        return factorCorriente;
    }

    public void setFactorCorriente(double factorCorriente) {
        this.factorCorriente = factorCorriente;
    }

    public double getFactorVoltaje() {
        return factorVoltaje;
    }

    public void setFactorVoltaje(double factorVoltaje) {
        this.factorVoltaje = factorVoltaje;
    }

    public long getSlot() {
        return slot;
    }

    public void setSlot(long slot) {
        this.slot = slot;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    @XmlTransient
    public Collection<AmyLecturas> getAmyLecturasCollection() {
        return amyLecturasCollection;
    }

    public void setAmyLecturasCollection(Collection<AmyLecturas> amyLecturasCollection) {
        this.amyLecturasCollection = amyLecturasCollection;
    }

    @XmlTransient
    public Collection<PrepagoSaldos> getPrepagoSaldosCollection() {
        return prepagoSaldosCollection;
    }

    public void setPrepagoSaldosCollection(Collection<PrepagoSaldos> prepagoSaldosCollection) {
        this.prepagoSaldosCollection = prepagoSaldosCollection;
    }

    @XmlTransient
    public Collection<AmyEventos> getAmyEventosCollection() {
        return amyEventosCollection;
    }

    public void setAmyEventosCollection(Collection<AmyEventos> amyEventosCollection) {
        this.amyEventosCollection = amyEventosCollection;
    }

    public ComercialProductos getFkComercialProductos() {
        return fkComercialProductos;
    }

    public void setFkComercialProductos(ComercialProductos fkComercialProductos) {
        this.fkComercialProductos = fkComercialProductos;
    }

    public AtributosTiposMedidores getFkAtributosTiposMedidores() {
        return fkAtributosTiposMedidores;
    }

    public void setFkAtributosTiposMedidores(AtributosTiposMedidores fkAtributosTiposMedidores) {
        this.fkAtributosTiposMedidores = fkAtributosTiposMedidores;
    }

    public AmyCajas getFkAmyCajas() {
        return fkAmyCajas;
    }

    public void setFkAmyCajas(AmyCajas fkAmyCajas) {
        this.fkAmyCajas = fkAmyCajas;
    }

    @XmlTransient
    public Collection<AmyConsumos> getAmyConsumosCollection() {
        return amyConsumosCollection;
    }

    public void setAmyConsumosCollection(Collection<AmyConsumos> amyConsumosCollection) {
        this.amyConsumosCollection = amyConsumosCollection;
    }

    @XmlTransient
    public Collection<TelcoInfo> getTelcoInfoCollection() {
        return telcoInfoCollection;
    }

    public void setTelcoInfoCollection(Collection<TelcoInfo> telcoInfoCollection) {
        this.telcoInfoCollection = telcoInfoCollection;
    }

    @XmlTransient
    public Collection<AmyMedidoresHistorico> getAmyMedidoresHistoricoCollection() {
        return amyMedidoresHistoricoCollection;
    }

    public void setAmyMedidoresHistoricoCollection(Collection<AmyMedidoresHistorico> amyMedidoresHistoricoCollection) {
        this.amyMedidoresHistoricoCollection = amyMedidoresHistoricoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMedidores != null ? idMedidores.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AmyMedidores)) {
            return false;
        }
        AmyMedidores other = (AmyMedidores) object;
        if ((this.idMedidores == null && other.idMedidores != null) || (this.idMedidores != null && !this.idMedidores.equals(other.idMedidores))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.AmyMedidores[ idMedidores=" + idMedidores + " ]";
    }
    
}
