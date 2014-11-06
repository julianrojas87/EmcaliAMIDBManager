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
@Table(name = "AMY_TIPO_EVENTOS", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmyTipoEventos.findAll", query = "SELECT a FROM AmyTipoEventos a"),
    @NamedQuery(name = "AmyTipoEventos.findByIdTipoEvento", query = "SELECT a FROM AmyTipoEventos a WHERE a.idTipoEvento = :idTipoEvento"),
    @NamedQuery(name = "AmyTipoEventos.findByDescripcionTipoEvento", query = "SELECT a FROM AmyTipoEventos a WHERE a.descripcionTipoEvento = :descripcionTipoEvento")})
public class AmyTipoEventos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIPO_EVENTO")
    private Long idTipoEvento;
    @Size(max = 60)
    @Column(name = "DESCRIPCION_TIPO_EVENTO")
    private String descripcionTipoEvento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAmyTipoEventos")
    private Collection<AmyEventos> amyEventosCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAmyTipoEventos")
    private Collection<PrepagoEventos> prepagoEventosCollection;

    public AmyTipoEventos() {
    }

    public AmyTipoEventos(Long idTipoEvento) {
        this.idTipoEvento = idTipoEvento;
    }

    public Long getIdTipoEvento() {
        return idTipoEvento;
    }

    public void setIdTipoEvento(Long idTipoEvento) {
        this.idTipoEvento = idTipoEvento;
    }

    public String getDescripcionTipoEvento() {
        return descripcionTipoEvento;
    }

    public void setDescripcionTipoEvento(String descripcionTipoEvento) {
        this.descripcionTipoEvento = descripcionTipoEvento;
    }

    @XmlTransient
    public Collection<AmyEventos> getAmyEventosCollection() {
        return amyEventosCollection;
    }

    public void setAmyEventosCollection(Collection<AmyEventos> amyEventosCollection) {
        this.amyEventosCollection = amyEventosCollection;
    }

    @XmlTransient
    public Collection<PrepagoEventos> getPrepagoEventosCollection() {
        return prepagoEventosCollection;
    }

    public void setPrepagoEventosCollection(Collection<PrepagoEventos> prepagoEventosCollection) {
        this.prepagoEventosCollection = prepagoEventosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoEvento != null ? idTipoEvento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AmyTipoEventos)) {
            return false;
        }
        AmyTipoEventos other = (AmyTipoEventos) object;
        if ((this.idTipoEvento == null && other.idTipoEvento != null) || (this.idTipoEvento != null && !this.idTipoEvento.equals(other.idTipoEvento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.AmyTipoEventos[ idTipoEvento=" + idTipoEvento + " ]";
    }
    
}
