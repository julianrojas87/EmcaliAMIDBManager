/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emcali.ami.persistence.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author julian
 */
@Entity
@Table(name = "RECARGA_RECARGAS", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RecargaRecargas.findAll", query = "SELECT r FROM RecargaRecargas r"),
    @NamedQuery(name = "RecargaRecargas.findByIdRecargas", query = "SELECT r FROM RecargaRecargas r WHERE r.idRecargas = :idRecargas"),
    @NamedQuery(name = "RecargaRecargas.findByProducto", query = "SELECT r FROM RecargaRecargas r WHERE r.producto = :producto"),
    @NamedQuery(name = "RecargaRecargas.findByFechaRecarga", query = "SELECT r FROM RecargaRecargas r WHERE r.fechaRecarga = :fechaRecarga"),
    @NamedQuery(name = "RecargaRecargas.findByProcesada", query = "SELECT r FROM RecargaRecargas r WHERE r.procesada = :procesada"),
    @NamedQuery(name = "RecargaRecargas.findByCantidad", query = "SELECT r FROM RecargaRecargas r WHERE r.cantidad = :cantidad")})
public class RecargaRecargas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_RECARGAS")
    private Long idRecargas;
    @Column(name = "PRODUCTO")
    private Long producto;
    @Column(name = "FECHA_RECARGA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRecarga;
    @Column(name = "PROCESADA")
    private Short procesada;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "CANTIDAD")
    private Double cantidad;
    @JoinColumn(name = "FK_PREPAGO_TIPO_RECARGA", referencedColumnName = "ID_TIPO_RECARGA")
    @ManyToOne(optional = false)
    private RecargaTipoRecarga fkPrepagoTipoRecarga;
    @JoinColumn(name = "FK_COMERCIAL_PRODUCTOS", referencedColumnName = "ID_PRODUCTOS")
    @ManyToOne(optional = false)
    private ComercialProductos fkComercialProductos;

    public RecargaRecargas() {
    }

    public RecargaRecargas(Long idRecargas) {
        this.idRecargas = idRecargas;
    }

    public Long getIdRecargas() {
        return idRecargas;
    }

    public void setIdRecargas(Long idRecargas) {
        this.idRecargas = idRecargas;
    }

    public Long getProducto() {
        return producto;
    }

    public void setProducto(Long producto) {
        this.producto = producto;
    }

    public Date getFechaRecarga() {
        return fechaRecarga;
    }

    public void setFechaRecarga(Date fechaRecarga) {
        this.fechaRecarga = fechaRecarga;
    }

    public Short getProcesada() {
        return procesada;
    }

    public void setProcesada(Short procesada) {
        this.procesada = procesada;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public RecargaTipoRecarga getFkPrepagoTipoRecarga() {
        return fkPrepagoTipoRecarga;
    }

    public void setFkPrepagoTipoRecarga(RecargaTipoRecarga fkPrepagoTipoRecarga) {
        this.fkPrepagoTipoRecarga = fkPrepagoTipoRecarga;
    }

    public ComercialProductos getFkComercialProductos() {
        return fkComercialProductos;
    }

    public void setFkComercialProductos(ComercialProductos fkComercialProductos) {
        this.fkComercialProductos = fkComercialProductos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRecargas != null ? idRecargas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecargaRecargas)) {
            return false;
        }
        RecargaRecargas other = (RecargaRecargas) object;
        if ((this.idRecargas == null && other.idRecargas != null) || (this.idRecargas != null && !this.idRecargas.equals(other.idRecargas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.RecargaRecargas[ idRecargas=" + idRecargas + " ]";
    }
    
}
