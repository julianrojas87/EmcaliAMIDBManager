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
@Table(name = "ENERGIA_TRANSFORMADORES", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnergiaTransformadores.findAll", query = "SELECT e FROM EnergiaTransformadores e"),
    @NamedQuery(name = "EnergiaTransformadores.findByIdTransformadores", query = "SELECT e FROM EnergiaTransformadores e WHERE e.idTransformadores = :idTransformadores"),
    @NamedQuery(name = "EnergiaTransformadores.findByCodTraffo", query = "SELECT e FROM EnergiaTransformadores e WHERE e.codTraffo = :codTraffo")})
public class EnergiaTransformadores implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TRANSFORMADORES")
    private Long idTransformadores;
    @Size(max = 45)
    @Column(name = "COD_TRAFFO")
    private String codTraffo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkEnergiaTransformadores")
    private List<AmyCajas> amyCajasList;
    @JoinColumn(name = "FK_ENERGIA_CIRCUITOS", referencedColumnName = "ID_CIRCUITOS")
    @ManyToOne(optional = false)
    private EnergiaCircuitos fkEnergiaCircuitos;

    public EnergiaTransformadores() {
    }

    public EnergiaTransformadores(Long idTransformadores) {
        this.idTransformadores = idTransformadores;
    }

    public Long getIdTransformadores() {
        return idTransformadores;
    }

    public void setIdTransformadores(Long idTransformadores) {
        this.idTransformadores = idTransformadores;
    }

    public String getCodTraffo() {
        return codTraffo;
    }

    public void setCodTraffo(String codTraffo) {
        this.codTraffo = codTraffo;
    }

    @XmlTransient
    public List<AmyCajas> getAmyCajasList() {
        return amyCajasList;
    }

    public void setAmyCajasList(List<AmyCajas> amyCajasList) {
        this.amyCajasList = amyCajasList;
    }

    public EnergiaCircuitos getFkEnergiaCircuitos() {
        return fkEnergiaCircuitos;
    }

    public void setFkEnergiaCircuitos(EnergiaCircuitos fkEnergiaCircuitos) {
        this.fkEnergiaCircuitos = fkEnergiaCircuitos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTransformadores != null ? idTransformadores.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnergiaTransformadores)) {
            return false;
        }
        EnergiaTransformadores other = (EnergiaTransformadores) object;
        if ((this.idTransformadores == null && other.idTransformadores != null) || (this.idTransformadores != null && !this.idTransformadores.equals(other.idTransformadores))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.EnergiaTransformadores[ idTransformadores=" + idTransformadores + " ]";
    }
    
}
