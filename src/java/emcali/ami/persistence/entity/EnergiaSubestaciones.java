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
@Table(name = "ENERGIA_SUBESTACIONES", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnergiaSubestaciones.findAll", query = "SELECT e FROM EnergiaSubestaciones e"),
    @NamedQuery(name = "EnergiaSubestaciones.findByIdSubestaciones", query = "SELECT e FROM EnergiaSubestaciones e WHERE e.idSubestaciones = :idSubestaciones"),
    @NamedQuery(name = "EnergiaSubestaciones.findBySubestacion", query = "SELECT e FROM EnergiaSubestaciones e WHERE e.subestacion = :subestacion")})
public class EnergiaSubestaciones implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_SUBESTACIONES")
    private Long idSubestaciones;
    @Size(max = 45)
    @Column(name = "SUBESTACION")
    private String subestacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkEnergiaSubestaciones")
    private List<EnergiaCircuitos> energiaCircuitosList;

    public EnergiaSubestaciones() {
    }

    public EnergiaSubestaciones(Long idSubestaciones) {
        this.idSubestaciones = idSubestaciones;
    }

    public Long getIdSubestaciones() {
        return idSubestaciones;
    }

    public void setIdSubestaciones(Long idSubestaciones) {
        this.idSubestaciones = idSubestaciones;
    }

    public String getSubestacion() {
        return subestacion;
    }

    public void setSubestacion(String subestacion) {
        this.subestacion = subestacion;
    }

    @XmlTransient
    public List<EnergiaCircuitos> getEnergiaCircuitosList() {
        return energiaCircuitosList;
    }

    public void setEnergiaCircuitosList(List<EnergiaCircuitos> energiaCircuitosList) {
        this.energiaCircuitosList = energiaCircuitosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSubestaciones != null ? idSubestaciones.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnergiaSubestaciones)) {
            return false;
        }
        EnergiaSubestaciones other = (EnergiaSubestaciones) object;
        if ((this.idSubestaciones == null && other.idSubestaciones != null) || (this.idSubestaciones != null && !this.idSubestaciones.equals(other.idSubestaciones))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.EnergiaSubestaciones[ idSubestaciones=" + idSubestaciones + " ]";
    }
    
}
