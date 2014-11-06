/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emcali.ami.persistence.entity;

import java.io.Serializable;
import java.util.Collection;
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
@Table(name = "AMY_INTERVAL", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmyInterval.findAll", query = "SELECT a FROM AmyInterval a"),
    @NamedQuery(name = "AmyInterval.findByIdTiempo", query = "SELECT a FROM AmyInterval a WHERE a.idTiempo = :idTiempo"),
    @NamedQuery(name = "AmyInterval.findByIntervalo", query = "SELECT a FROM AmyInterval a WHERE a.intervalo = :intervalo")})
public class AmyInterval implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIEMPO")
    private Long idTiempo;
    @Size(max = 20)
    @Column(name = "INTERVALO")
    private String intervalo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAmyInterval")
    private Collection<AmyLecturas> amyLecturasCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAmyInterval")
    private Collection<AmyConsumos> amyConsumosCollection;

    public AmyInterval() {
    }

    public AmyInterval(Long idTiempo) {
        this.idTiempo = idTiempo;
    }

    public Long getIdTiempo() {
        return idTiempo;
    }

    public void setIdTiempo(Long idTiempo) {
        this.idTiempo = idTiempo;
    }

    public String getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(String intervalo) {
        this.intervalo = intervalo;
    }

    @XmlTransient
    public Collection<AmyLecturas> getAmyLecturasCollection() {
        return amyLecturasCollection;
    }

    public void setAmyLecturasCollection(Collection<AmyLecturas> amyLecturasCollection) {
        this.amyLecturasCollection = amyLecturasCollection;
    }

    @XmlTransient
    public Collection<AmyConsumos> getAmyConsumosCollection() {
        return amyConsumosCollection;
    }

    public void setAmyConsumosCollection(Collection<AmyConsumos> amyConsumosCollection) {
        this.amyConsumosCollection = amyConsumosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTiempo != null ? idTiempo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AmyInterval)) {
            return false;
        }
        AmyInterval other = (AmyInterval) object;
        if ((this.idTiempo == null && other.idTiempo != null) || (this.idTiempo != null && !this.idTiempo.equals(other.idTiempo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.AmyInterval[ idTiempo=" + idTiempo + " ]";
    }
    
}
