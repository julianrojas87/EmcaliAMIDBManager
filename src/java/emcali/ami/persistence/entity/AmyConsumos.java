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
@Table(name = "AMY_CONSUMOS", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmyConsumos.findAll", query = "SELECT a FROM AmyConsumos a"),
    @NamedQuery(name = "AmyConsumos.findByIdConsumo", query = "SELECT a FROM AmyConsumos a WHERE a.idConsumo = :idConsumo"),
    @NamedQuery(name = "AmyConsumos.findByConsumo", query = "SELECT a FROM AmyConsumos a WHERE a.consumo = :consumo"),
    @NamedQuery(name = "AmyConsumos.findByFechaConsumo", query = "SELECT a FROM AmyConsumos a WHERE a.fechaConsumo = :fechaConsumo"),
    @NamedQuery(name = "AmyConsumos.findByIntervalo", query = "SELECT a FROM AmyConsumos a WHERE a.intervalo = :intervalo")})
public class AmyConsumos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_CONSUMO")
    private Long idConsumo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "CONSUMO")
    private Double consumo;
    @Column(name = "FECHA_CONSUMO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaConsumo;
    @Column(name = "INTERVALO")
    private Long intervalo;
    @JoinColumn(name = "FK_AMY_TIPOLECTURAS", referencedColumnName = "ID_TIPOLECTURA")
    @ManyToOne(optional = false)
    private AmyTipolecturas fkAmyTipolecturas;
    @JoinColumn(name = "FK_AMY_MEDIDORES", referencedColumnName = "ID_MEDIDORES")
    @ManyToOne(optional = false)
    private AmyMedidores fkAmyMedidores;
    @JoinColumn(name = "FK_AMY_INTERVAL", referencedColumnName = "ID_TIEMPO")
    @ManyToOne(optional = false)
    private AmyInterval fkAmyInterval;
    @JoinColumn(name = "FK_AMY_CANAL", referencedColumnName = "ID_CANAL")
    @ManyToOne(optional = false)
    private AmyCanal fkAmyCanal;

    public AmyConsumos() {
    }

    public AmyConsumos(Long idConsumo) {
        this.idConsumo = idConsumo;
    }

    public Long getIdConsumo() {
        return idConsumo;
    }

    public void setIdConsumo(Long idConsumo) {
        this.idConsumo = idConsumo;
    }

    public Double getConsumo() {
        return consumo;
    }

    public void setConsumo(Double consumo) {
        this.consumo = consumo;
    }

    public Date getFechaConsumo() {
        return fechaConsumo;
    }

    public void setFechaConsumo(Date fechaConsumo) {
        this.fechaConsumo = fechaConsumo;
    }

    public Long getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(Long intervalo) {
        this.intervalo = intervalo;
    }

    public AmyTipolecturas getFkAmyTipolecturas() {
        return fkAmyTipolecturas;
    }

    public void setFkAmyTipolecturas(AmyTipolecturas fkAmyTipolecturas) {
        this.fkAmyTipolecturas = fkAmyTipolecturas;
    }

    public AmyMedidores getFkAmyMedidores() {
        return fkAmyMedidores;
    }

    public void setFkAmyMedidores(AmyMedidores fkAmyMedidores) {
        this.fkAmyMedidores = fkAmyMedidores;
    }

    public AmyInterval getFkAmyInterval() {
        return fkAmyInterval;
    }

    public void setFkAmyInterval(AmyInterval fkAmyInterval) {
        this.fkAmyInterval = fkAmyInterval;
    }

    public AmyCanal getFkAmyCanal() {
        return fkAmyCanal;
    }

    public void setFkAmyCanal(AmyCanal fkAmyCanal) {
        this.fkAmyCanal = fkAmyCanal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConsumo != null ? idConsumo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AmyConsumos)) {
            return false;
        }
        AmyConsumos other = (AmyConsumos) object;
        if ((this.idConsumo == null && other.idConsumo != null) || (this.idConsumo != null && !this.idConsumo.equals(other.idConsumo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.AmyConsumos[ idConsumo=" + idConsumo + " ]";
    }
    
}
