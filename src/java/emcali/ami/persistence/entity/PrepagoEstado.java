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
@Table(name = "PREPAGO_ESTADO", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PrepagoEstado.findAll", query = "SELECT p FROM PrepagoEstado p"),
    @NamedQuery(name = "PrepagoEstado.findByIdEstado", query = "SELECT p FROM PrepagoEstado p WHERE p.idEstado = :idEstado"),
    @NamedQuery(name = "PrepagoEstado.findByEstado", query = "SELECT p FROM PrepagoEstado p WHERE p.estado = :estado")})
public class PrepagoEstado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_ESTADO")
    private Long idEstado;
    @Size(max = 45)
    @Column(name = "ESTADO")
    private String estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkPrepagoEstado")
    private Collection<PrepagoClientes> prepagoClientesCollection;

    public PrepagoEstado() {
    }

    public PrepagoEstado(Long idEstado) {
        this.idEstado = idEstado;
    }

    public Long getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Long idEstado) {
        this.idEstado = idEstado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlTransient
    public Collection<PrepagoClientes> getPrepagoClientesCollection() {
        return prepagoClientesCollection;
    }

    public void setPrepagoClientesCollection(Collection<PrepagoClientes> prepagoClientesCollection) {
        this.prepagoClientesCollection = prepagoClientesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstado != null ? idEstado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrepagoEstado)) {
            return false;
        }
        PrepagoEstado other = (PrepagoEstado) object;
        if ((this.idEstado == null && other.idEstado != null) || (this.idEstado != null && !this.idEstado.equals(other.idEstado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.PrepagoEstado[ idEstado=" + idEstado + " ]";
    }
    
}
