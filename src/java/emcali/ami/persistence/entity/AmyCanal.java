/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emcali.ami.persistence.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "AMY_CANAL", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmyCanal.findAll", query = "SELECT a FROM AmyCanal a"),
    @NamedQuery(name = "AmyCanal.findByIdCanal", query = "SELECT a FROM AmyCanal a WHERE a.idCanal = :idCanal"),
    @NamedQuery(name = "AmyCanal.findByTipoCanal", query = "SELECT a FROM AmyCanal a WHERE a.tipoCanal = :tipoCanal")})
public class AmyCanal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_CANAL")
    private Long idCanal;
    @Size(max = 45)
    @Column(name = "TIPO_CANAL")
    private String tipoCanal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAmyCanal")
    private List<AmyLecturas> amyLecturasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAmyCanal")
    private List<AmyConsumos> amyConsumosList;

    public AmyCanal() {
    }

    public AmyCanal(Long idCanal) {
        this.idCanal = idCanal;
    }

    public Long getIdCanal() {
        return idCanal;
    }

    public void setIdCanal(Long idCanal) {
        this.idCanal = idCanal;
    }

    public String getTipoCanal() {
        return tipoCanal;
    }

    public void setTipoCanal(String tipoCanal) {
        this.tipoCanal = tipoCanal;
    }

    @XmlTransient
    public List<AmyLecturas> getAmyLecturasList() {
        return amyLecturasList;
    }

    public void setAmyLecturasList(List<AmyLecturas> amyLecturasList) {
        this.amyLecturasList = amyLecturasList;
    }

    @XmlTransient
    public List<AmyConsumos> getAmyConsumosList() {
        return amyConsumosList;
    }

    public void setAmyConsumosList(List<AmyConsumos> amyConsumosList) {
        this.amyConsumosList = amyConsumosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCanal != null ? idCanal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AmyCanal)) {
            return false;
        }
        AmyCanal other = (AmyCanal) object;
        if ((this.idCanal == null && other.idCanal != null) || (this.idCanal != null && !this.idCanal.equals(other.idCanal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.AmyCanal[ idCanal=" + idCanal + " ]";
    }
    
}
