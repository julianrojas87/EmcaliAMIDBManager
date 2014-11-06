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
@Table(name = "PREPAGO_SALDOS", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PrepagoSaldos.findAll", query = "SELECT p FROM PrepagoSaldos p"),
    @NamedQuery(name = "PrepagoSaldos.findByIdSaldos", query = "SELECT p FROM PrepagoSaldos p WHERE p.idSaldos = :idSaldos")})
public class PrepagoSaldos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_SALDOS")
    private Long idSaldos;
    @JoinColumn(name = "FK_AMY_MEDIDORES", referencedColumnName = "ID_MEDIDORES")
    @ManyToOne(optional = false)
    private AmyMedidores fkAmyMedidores;

    public PrepagoSaldos() {
    }

    public PrepagoSaldos(Long idSaldos) {
        this.idSaldos = idSaldos;
    }

    public Long getIdSaldos() {
        return idSaldos;
    }

    public void setIdSaldos(Long idSaldos) {
        this.idSaldos = idSaldos;
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
        hash += (idSaldos != null ? idSaldos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrepagoSaldos)) {
            return false;
        }
        PrepagoSaldos other = (PrepagoSaldos) object;
        if ((this.idSaldos == null && other.idSaldos != null) || (this.idSaldos != null && !this.idSaldos.equals(other.idSaldos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.PrepagoSaldos[ idSaldos=" + idSaldos + " ]";
    }
    
}
