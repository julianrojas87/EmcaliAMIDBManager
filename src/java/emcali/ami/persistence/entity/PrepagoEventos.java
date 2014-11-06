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
@Table(name = "PREPAGO_EVENTOS", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PrepagoEventos.findAll", query = "SELECT p FROM PrepagoEventos p"),
    @NamedQuery(name = "PrepagoEventos.findByIdEventos", query = "SELECT p FROM PrepagoEventos p WHERE p.idEventos = :idEventos"),
    @NamedQuery(name = "PrepagoEventos.findByFechaEvento", query = "SELECT p FROM PrepagoEventos p WHERE p.fechaEvento = :fechaEvento")})
public class PrepagoEventos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_EVENTOS")
    private Long idEventos;
    @Column(name = "FECHA_EVENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEvento;
    @JoinColumn(name = "FK_COMERCIAL_PRODUCTOS", referencedColumnName = "ID_PRODUCTOS")
    @ManyToOne(optional = false)
    private ComercialProductos fkComercialProductos;
    @JoinColumn(name = "FK_AMY_TIPO_EVENTOS", referencedColumnName = "ID_TIPO_EVENTO")
    @ManyToOne(optional = false)
    private AmyTipoEventos fkAmyTipoEventos;

    public PrepagoEventos() {
    }

    public PrepagoEventos(Long idEventos) {
        this.idEventos = idEventos;
    }

    public Long getIdEventos() {
        return idEventos;
    }

    public void setIdEventos(Long idEventos) {
        this.idEventos = idEventos;
    }

    public Date getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(Date fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public ComercialProductos getFkComercialProductos() {
        return fkComercialProductos;
    }

    public void setFkComercialProductos(ComercialProductos fkComercialProductos) {
        this.fkComercialProductos = fkComercialProductos;
    }

    public AmyTipoEventos getFkAmyTipoEventos() {
        return fkAmyTipoEventos;
    }

    public void setFkAmyTipoEventos(AmyTipoEventos fkAmyTipoEventos) {
        this.fkAmyTipoEventos = fkAmyTipoEventos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEventos != null ? idEventos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrepagoEventos)) {
            return false;
        }
        PrepagoEventos other = (PrepagoEventos) object;
        if ((this.idEventos == null && other.idEventos != null) || (this.idEventos != null && !this.idEventos.equals(other.idEventos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.PrepagoEventos[ idEventos=" + idEventos + " ]";
    }
    
}
