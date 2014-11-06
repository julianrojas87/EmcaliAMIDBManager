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
@Table(name = "PREPAGO_CLIENTES", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PrepagoClientes.findAll", query = "SELECT p FROM PrepagoClientes p"),
    @NamedQuery(name = "PrepagoClientes.findByIdClientePrepago", query = "SELECT p FROM PrepagoClientes p WHERE p.idClientePrepago = :idClientePrepago"),
    @NamedQuery(name = "PrepagoClientes.findByFechaInicio", query = "SELECT p FROM PrepagoClientes p WHERE p.fechaInicio = :fechaInicio")})
public class PrepagoClientes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_CLIENTE_PREPAGO")
    private Long idClientePrepago;
    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @JoinColumn(name = "FK_PREPAGO_ESTADO", referencedColumnName = "ID_ESTADO")
    @ManyToOne(optional = false)
    private PrepagoEstado fkPrepagoEstado;
    @JoinColumn(name = "FK_COMERCIAL_PRODUCTOS", referencedColumnName = "ID_PRODUCTOS")
    @ManyToOne(optional = false)
    private ComercialProductos fkComercialProductos;

    public PrepagoClientes() {
    }

    public PrepagoClientes(Long idClientePrepago) {
        this.idClientePrepago = idClientePrepago;
    }

    public Long getIdClientePrepago() {
        return idClientePrepago;
    }

    public void setIdClientePrepago(Long idClientePrepago) {
        this.idClientePrepago = idClientePrepago;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public PrepagoEstado getFkPrepagoEstado() {
        return fkPrepagoEstado;
    }

    public void setFkPrepagoEstado(PrepagoEstado fkPrepagoEstado) {
        this.fkPrepagoEstado = fkPrepagoEstado;
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
        hash += (idClientePrepago != null ? idClientePrepago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrepagoClientes)) {
            return false;
        }
        PrepagoClientes other = (PrepagoClientes) object;
        if ((this.idClientePrepago == null && other.idClientePrepago != null) || (this.idClientePrepago != null && !this.idClientePrepago.equals(other.idClientePrepago))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.PrepagoClientes[ idClientePrepago=" + idClientePrepago + " ]";
    }
    
}
