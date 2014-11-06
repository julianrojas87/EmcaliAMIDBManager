/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emcali.ami.persistence.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author julian
 */
@Entity
@Table(name = "AMY_MEDIDORES_HISTORICO", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmyMedidoresHistorico.findAll", query = "SELECT a FROM AmyMedidoresHistorico a"),
    @NamedQuery(name = "AmyMedidoresHistorico.findByIdMedidoresHistorico", query = "SELECT a FROM AmyMedidoresHistorico a WHERE a.idMedidoresHistorico = :idMedidoresHistorico")})
public class AmyMedidoresHistorico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_MEDIDORES_HISTORICO")
    private Long idMedidoresHistorico;
    @JoinColumn(name = "FK_AMY_MEDIDORES", referencedColumnName = "ID_MEDIDORES")
    @ManyToOne(optional = false)
    private AmyMedidores fkAmyMedidores;

    public AmyMedidoresHistorico() {
    }

    public AmyMedidoresHistorico(Long idMedidoresHistorico) {
        this.idMedidoresHistorico = idMedidoresHistorico;
    }

    public Long getIdMedidoresHistorico() {
        return idMedidoresHistorico;
    }

    public void setIdMedidoresHistorico(Long idMedidoresHistorico) {
        this.idMedidoresHistorico = idMedidoresHistorico;
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
        hash += (idMedidoresHistorico != null ? idMedidoresHistorico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AmyMedidoresHistorico)) {
            return false;
        }
        AmyMedidoresHistorico other = (AmyMedidoresHistorico) object;
        if ((this.idMedidoresHistorico == null && other.idMedidoresHistorico != null) || (this.idMedidoresHistorico != null && !this.idMedidoresHistorico.equals(other.idMedidoresHistorico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.AmyMedidoresHistorico[ idMedidoresHistorico=" + idMedidoresHistorico + " ]";
    }
    
}
