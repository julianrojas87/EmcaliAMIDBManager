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
@Table(name = "ATRIBUTOS_TIPOS_MEDIDORES", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AtributosTiposMedidores.findAll", query = "SELECT a FROM AtributosTiposMedidores a"),
    @NamedQuery(name = "AtributosTiposMedidores.findByIdTiposMedidores", query = "SELECT a FROM AtributosTiposMedidores a WHERE a.idTiposMedidores = :idTiposMedidores"),
    @NamedQuery(name = "AtributosTiposMedidores.findByNombreTiposMedidores", query = "SELECT a FROM AtributosTiposMedidores a WHERE a.nombreTiposMedidores = :nombreTiposMedidores"),
    @NamedQuery(name = "AtributosTiposMedidores.findByClase", query = "SELECT a FROM AtributosTiposMedidores a WHERE a.clase = :clase"),
    @NamedQuery(name = "AtributosTiposMedidores.findByAtributosTiposMedidorescol", query = "SELECT a FROM AtributosTiposMedidores a WHERE a.atributosTiposMedidorescol = :atributosTiposMedidorescol")})
public class AtributosTiposMedidores implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIPOS_MEDIDORES")
    private Long idTiposMedidores;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NOMBRE_TIPOS_MEDIDORES")
    private String nombreTiposMedidores;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "CLASE")
    private String clase;
    @Size(max = 45)
    @Column(name = "ATRIBUTOS_TIPOS_MEDIDORESCOL")
    private String atributosTiposMedidorescol;
    @JoinColumn(name = "FK_ATRIBUTOS_FABRICANTES", referencedColumnName = "ID_FABRICANTES")
    @ManyToOne(optional = false)
    private AtributosFabricantes fkAtributosFabricantes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAtributosTiposMedidores")
    private List<AmyMedidores> amyMedidoresList;

    public AtributosTiposMedidores() {
    }

    public AtributosTiposMedidores(Long idTiposMedidores) {
        this.idTiposMedidores = idTiposMedidores;
    }

    public AtributosTiposMedidores(Long idTiposMedidores, String nombreTiposMedidores, String clase) {
        this.idTiposMedidores = idTiposMedidores;
        this.nombreTiposMedidores = nombreTiposMedidores;
        this.clase = clase;
    }

    public Long getIdTiposMedidores() {
        return idTiposMedidores;
    }

    public void setIdTiposMedidores(Long idTiposMedidores) {
        this.idTiposMedidores = idTiposMedidores;
    }

    public String getNombreTiposMedidores() {
        return nombreTiposMedidores;
    }

    public void setNombreTiposMedidores(String nombreTiposMedidores) {
        this.nombreTiposMedidores = nombreTiposMedidores;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getAtributosTiposMedidorescol() {
        return atributosTiposMedidorescol;
    }

    public void setAtributosTiposMedidorescol(String atributosTiposMedidorescol) {
        this.atributosTiposMedidorescol = atributosTiposMedidorescol;
    }

    public AtributosFabricantes getFkAtributosFabricantes() {
        return fkAtributosFabricantes;
    }

    public void setFkAtributosFabricantes(AtributosFabricantes fkAtributosFabricantes) {
        this.fkAtributosFabricantes = fkAtributosFabricantes;
    }

    @XmlTransient
    public List<AmyMedidores> getAmyMedidoresList() {
        return amyMedidoresList;
    }

    public void setAmyMedidoresList(List<AmyMedidores> amyMedidoresList) {
        this.amyMedidoresList = amyMedidoresList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTiposMedidores != null ? idTiposMedidores.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AtributosTiposMedidores)) {
            return false;
        }
        AtributosTiposMedidores other = (AtributosTiposMedidores) object;
        if ((this.idTiposMedidores == null && other.idTiposMedidores != null) || (this.idTiposMedidores != null && !this.idTiposMedidores.equals(other.idTiposMedidores))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.AtributosTiposMedidores[ idTiposMedidores=" + idTiposMedidores + " ]";
    }
    
}
