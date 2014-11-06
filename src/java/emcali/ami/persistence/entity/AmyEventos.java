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
@Table(name = "AMY_EVENTOS", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmyEventos.findAll", query = "SELECT a FROM AmyEventos a"),
    @NamedQuery(name = "AmyEventos.findByIdEvento", query = "SELECT a FROM AmyEventos a WHERE a.idEvento = :idEvento"),
    @NamedQuery(name = "AmyEventos.findByFechaEvento", query = "SELECT a FROM AmyEventos a WHERE a.fechaEvento = :fechaEvento"),
    @NamedQuery(name = "AmyEventos.findByEstado", query = "SELECT a FROM AmyEventos a WHERE a.estado = :estado")})
public class AmyEventos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_EVENTO")
    private Long idEvento;
    @Column(name = "FECHA_EVENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEvento;
    @Column(name = "ESTADO")
    private Short estado;
    @JoinColumn(name = "FK_AMY_TIPO_EVENTOS", referencedColumnName = "ID_TIPO_EVENTO")
    @ManyToOne(optional = false)
    private AmyTipoEventos fkAmyTipoEventos;
    @JoinColumn(name = "FK_AMY_MEDIDORES", referencedColumnName = "ID_MEDIDORES")
    @ManyToOne(optional = false)
    private AmyMedidores fkAmyMedidores;

    public AmyEventos() {
    }

    public AmyEventos(Long idEvento) {
        this.idEvento = idEvento;
    }

    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public Date getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(Date fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public Short getEstado() {
        return estado;
    }

    public void setEstado(Short estado) {
        this.estado = estado;
    }

    public AmyTipoEventos getFkAmyTipoEventos() {
        return fkAmyTipoEventos;
    }

    public void setFkAmyTipoEventos(AmyTipoEventos fkAmyTipoEventos) {
        this.fkAmyTipoEventos = fkAmyTipoEventos;
    }

    public AmyMedidores getFkAmyMedidores() {
        return fkAmyMedidores;
    }

    public void setFkAmyMedidores(AmyMedidores fkAmyMedidores) {
        this.fkAmyMedidores = fkAmyMedidores;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEvento != null ? idEvento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AmyEventos)) {
            return false;
        }
        AmyEventos other = (AmyEventos) object;
        if ((this.idEvento == null && other.idEvento != null) || (this.idEvento != null && !this.idEvento.equals(other.idEvento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.AmyEventos[ idEvento=" + idEvento + " ]";
    }
    
}
