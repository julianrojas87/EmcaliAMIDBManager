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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "ENERGIA_CIRCUITOS", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnergiaCircuitos.findAll", query = "SELECT e FROM EnergiaCircuitos e"),
    @NamedQuery(name = "EnergiaCircuitos.findByIdCircuitos", query = "SELECT e FROM EnergiaCircuitos e WHERE e.idCircuitos = :idCircuitos"),
    @NamedQuery(name = "EnergiaCircuitos.findByCircuito", query = "SELECT e FROM EnergiaCircuitos e WHERE e.circuito = :circuito")})
public class EnergiaCircuitos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_CIRCUITOS")
    private Long idCircuitos;
    @Size(max = 45)
    @Column(name = "CIRCUITO")
    private String circuito;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkEnergiaCircuitos")
    private List<EnergiaTransformadores> energiaTransformadoresList;
    @JoinColumn(name = "FK_ENERGIA_SUBESTACIONES", referencedColumnName = "ID_SUBESTACIONES")
    @ManyToOne(optional = false)
    private EnergiaSubestaciones fkEnergiaSubestaciones;

    public EnergiaCircuitos() {
    }

    public EnergiaCircuitos(Long idCircuitos) {
        this.idCircuitos = idCircuitos;
    }

    public Long getIdCircuitos() {
        return idCircuitos;
    }

    public void setIdCircuitos(Long idCircuitos) {
        this.idCircuitos = idCircuitos;
    }

    public String getCircuito() {
        return circuito;
    }

    public void setCircuito(String circuito) {
        this.circuito = circuito;
    }

    @XmlTransient
    public List<EnergiaTransformadores> getEnergiaTransformadoresList() {
        return energiaTransformadoresList;
    }

    public void setEnergiaTransformadoresList(List<EnergiaTransformadores> energiaTransformadoresList) {
        this.energiaTransformadoresList = energiaTransformadoresList;
    }

    public EnergiaSubestaciones getFkEnergiaSubestaciones() {
        return fkEnergiaSubestaciones;
    }

    public void setFkEnergiaSubestaciones(EnergiaSubestaciones fkEnergiaSubestaciones) {
        this.fkEnergiaSubestaciones = fkEnergiaSubestaciones;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCircuitos != null ? idCircuitos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnergiaCircuitos)) {
            return false;
        }
        EnergiaCircuitos other = (EnergiaCircuitos) object;
        if ((this.idCircuitos == null && other.idCircuitos != null) || (this.idCircuitos != null && !this.idCircuitos.equals(other.idCircuitos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.EnergiaCircuitos[ idCircuitos=" + idCircuitos + " ]";
    }
    
}
