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
@Table(name = "COMERCIAL_PRODUCTOS", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComercialProductos.findAll", query = "SELECT c FROM ComercialProductos c"),
    @NamedQuery(name = "ComercialProductos.findByIdProductos", query = "SELECT c FROM ComercialProductos c WHERE c.idProductos = :idProductos"),
    @NamedQuery(name = "ComercialProductos.findByNumeroProducto", query = "SELECT c FROM ComercialProductos c WHERE c.numeroProducto = :numeroProducto"),
    @NamedQuery(name = "ComercialProductos.findBySuscripcionTelco", query = "SELECT c FROM ComercialProductos c WHERE c.suscripcionTelco = :suscripcionTelco")})
public class ComercialProductos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_PRODUCTOS")
    private Long idProductos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMERO_PRODUCTO")
    private long numeroProducto;
    @Size(max = 45)
    @Column(name = "SUSCRIPCION_TELCO")
    private String suscripcionTelco;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkComercialProductos")
    private Collection<RecargaRecargas> recargaRecargasCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkComercialProductos")
    private Collection<SaldosHistoria> saldosHistoriaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkComercialProductos")
    private Collection<PrepagoEventos> prepagoEventosCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkComercialProductos")
    private Collection<AmyMedidores> amyMedidoresCollection;
    @JoinColumn(name = "FK_COMERCIAL_CONTRATOS", referencedColumnName = "ID_CONTRATOS")
    @ManyToOne(optional = false)
    private ComercialContratos fkComercialContratos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkComercialProductos")
    private Collection<PrepagoClientes> prepagoClientesCollection;

    public ComercialProductos() {
    }

    public ComercialProductos(Long idProductos) {
        this.idProductos = idProductos;
    }

    public ComercialProductos(Long idProductos, long numeroProducto) {
        this.idProductos = idProductos;
        this.numeroProducto = numeroProducto;
    }

    public Long getIdProductos() {
        return idProductos;
    }

    public void setIdProductos(Long idProductos) {
        this.idProductos = idProductos;
    }

    public long getNumeroProducto() {
        return numeroProducto;
    }

    public void setNumeroProducto(long numeroProducto) {
        this.numeroProducto = numeroProducto;
    }

    public String getSuscripcionTelco() {
        return suscripcionTelco;
    }

    public void setSuscripcionTelco(String suscripcionTelco) {
        this.suscripcionTelco = suscripcionTelco;
    }

    @XmlTransient
    public Collection<RecargaRecargas> getRecargaRecargasCollection() {
        return recargaRecargasCollection;
    }

    public void setRecargaRecargasCollection(Collection<RecargaRecargas> recargaRecargasCollection) {
        this.recargaRecargasCollection = recargaRecargasCollection;
    }

    @XmlTransient
    public Collection<SaldosHistoria> getSaldosHistoriaCollection() {
        return saldosHistoriaCollection;
    }

    public void setSaldosHistoriaCollection(Collection<SaldosHistoria> saldosHistoriaCollection) {
        this.saldosHistoriaCollection = saldosHistoriaCollection;
    }

    @XmlTransient
    public Collection<PrepagoEventos> getPrepagoEventosCollection() {
        return prepagoEventosCollection;
    }

    public void setPrepagoEventosCollection(Collection<PrepagoEventos> prepagoEventosCollection) {
        this.prepagoEventosCollection = prepagoEventosCollection;
    }

    @XmlTransient
    public Collection<AmyMedidores> getAmyMedidoresCollection() {
        return amyMedidoresCollection;
    }

    public void setAmyMedidoresCollection(Collection<AmyMedidores> amyMedidoresCollection) {
        this.amyMedidoresCollection = amyMedidoresCollection;
    }

    public ComercialContratos getFkComercialContratos() {
        return fkComercialContratos;
    }

    public void setFkComercialContratos(ComercialContratos fkComercialContratos) {
        this.fkComercialContratos = fkComercialContratos;
    }

    @XmlTransient
    public Collection<PrepagoClientes> getPrepagoClientesCollection() {
        return prepagoClientesCollection;
    }

    public void setPrepagoClientesCollection(Collection<PrepagoClientes> prepagoClientesCollection) {
        this.prepagoClientesCollection = prepagoClientesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProductos != null ? idProductos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComercialProductos)) {
            return false;
        }
        ComercialProductos other = (ComercialProductos) object;
        if ((this.idProductos == null && other.idProductos != null) || (this.idProductos != null && !this.idProductos.equals(other.idProductos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.ComercialProductos[ idProductos=" + idProductos + " ]";
    }
    
}
