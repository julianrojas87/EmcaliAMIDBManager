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
@Table(name = "ATRIBUTOS_FABRICANTES", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AtributosFabricantes.findAll", query = "SELECT a FROM AtributosFabricantes a"),
    @NamedQuery(name = "AtributosFabricantes.findByIdFabricantes", query = "SELECT a FROM AtributosFabricantes a WHERE a.idFabricantes = :idFabricantes"),
    @NamedQuery(name = "AtributosFabricantes.findByNombreFabricantes", query = "SELECT a FROM AtributosFabricantes a WHERE a.nombreFabricantes = :nombreFabricantes")})
public class AtributosFabricantes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_FABRICANTES")
    private Long idFabricantes;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NOMBRE_FABRICANTES")
    private String nombreFabricantes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAtributosFabricantes")
    private List<AtributosTiposCajas> atributosTiposCajasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAtributosFabricantes")
    private List<AtributosTiposMedidores> atributosTiposMedidoresList;

    public AtributosFabricantes() {
    }

    public AtributosFabricantes(Long idFabricantes) {
        this.idFabricantes = idFabricantes;
    }

    public AtributosFabricantes(Long idFabricantes, String nombreFabricantes) {
        this.idFabricantes = idFabricantes;
        this.nombreFabricantes = nombreFabricantes;
    }

    public Long getIdFabricantes() {
        return idFabricantes;
    }

    public void setIdFabricantes(Long idFabricantes) {
        this.idFabricantes = idFabricantes;
    }

    public String getNombreFabricantes() {
        return nombreFabricantes;
    }

    public void setNombreFabricantes(String nombreFabricantes) {
        this.nombreFabricantes = nombreFabricantes;
    }

    @XmlTransient
    public List<AtributosTiposCajas> getAtributosTiposCajasList() {
        return atributosTiposCajasList;
    }

    public void setAtributosTiposCajasList(List<AtributosTiposCajas> atributosTiposCajasList) {
        this.atributosTiposCajasList = atributosTiposCajasList;
    }

    @XmlTransient
    public List<AtributosTiposMedidores> getAtributosTiposMedidoresList() {
        return atributosTiposMedidoresList;
    }

    public void setAtributosTiposMedidoresList(List<AtributosTiposMedidores> atributosTiposMedidoresList) {
        this.atributosTiposMedidoresList = atributosTiposMedidoresList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFabricantes != null ? idFabricantes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AtributosFabricantes)) {
            return false;
        }
        AtributosFabricantes other = (AtributosFabricantes) object;
        if ((this.idFabricantes == null && other.idFabricantes != null) || (this.idFabricantes != null && !this.idFabricantes.equals(other.idFabricantes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.AtributosFabricantes[ idFabricantes=" + idFabricantes + " ]";
    }
    
}
