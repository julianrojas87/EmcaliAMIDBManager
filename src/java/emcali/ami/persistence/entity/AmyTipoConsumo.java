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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author julian
 */
@Entity
@Table(name = "AMY_TIPO_CONSUMO", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmyTipoConsumo.findAll", query = "SELECT a FROM AmyTipoConsumo a"),
    @NamedQuery(name = "AmyTipoConsumo.findByIdTipoConsumo", query = "SELECT a FROM AmyTipoConsumo a WHERE a.idTipoConsumo = :idTipoConsumo"),
    @NamedQuery(name = "AmyTipoConsumo.findByTipoConsumo", query = "SELECT a FROM AmyTipoConsumo a WHERE a.tipoConsumo = :tipoConsumo")})
public class AmyTipoConsumo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIPO_CONSUMO")
    private Long idTipoConsumo;
    @Size(max = 45)
    @Column(name = "TIPO_CONSUMO")
    private String tipoConsumo;

    public AmyTipoConsumo() {
    }

    public AmyTipoConsumo(Long idTipoConsumo) {
        this.idTipoConsumo = idTipoConsumo;
    }

    public Long getIdTipoConsumo() {
        return idTipoConsumo;
    }

    public void setIdTipoConsumo(Long idTipoConsumo) {
        this.idTipoConsumo = idTipoConsumo;
    }

    public String getTipoConsumo() {
        return tipoConsumo;
    }

    public void setTipoConsumo(String tipoConsumo) {
        this.tipoConsumo = tipoConsumo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoConsumo != null ? idTipoConsumo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AmyTipoConsumo)) {
            return false;
        }
        AmyTipoConsumo other = (AmyTipoConsumo) object;
        if ((this.idTipoConsumo == null && other.idTipoConsumo != null) || (this.idTipoConsumo != null && !this.idTipoConsumo.equals(other.idTipoConsumo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.AmyTipoConsumo[ idTipoConsumo=" + idTipoConsumo + " ]";
    }
    
}
