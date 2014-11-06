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
@Table(name = "ATRIBUTOS_TIPOS_CAJAS", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AtributosTiposCajas.findAll", query = "SELECT a FROM AtributosTiposCajas a"),
    @NamedQuery(name = "AtributosTiposCajas.findByIdTiposCajas", query = "SELECT a FROM AtributosTiposCajas a WHERE a.idTiposCajas = :idTiposCajas"),
    @NamedQuery(name = "AtributosTiposCajas.findByNombreTiposCajas", query = "SELECT a FROM AtributosTiposCajas a WHERE a.nombreTiposCajas = :nombreTiposCajas"),
    @NamedQuery(name = "AtributosTiposCajas.findByCantidadMedidores", query = "SELECT a FROM AtributosTiposCajas a WHERE a.cantidadMedidores = :cantidadMedidores")})
public class AtributosTiposCajas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIPOS_CAJAS")
    private Long idTiposCajas;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NOMBRE_TIPOS_CAJAS")
    private String nombreTiposCajas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD_MEDIDORES")
    private short cantidadMedidores;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkTiposCajas")
    private Collection<AmyCajas> amyCajasCollection;
    @JoinColumn(name = "FK_ATRIBUTOS_FABRICANTES", referencedColumnName = "ID_FABRICANTES")
    @ManyToOne(optional = false)
    private AtributosFabricantes fkAtributosFabricantes;

    public AtributosTiposCajas() {
    }

    public AtributosTiposCajas(Long idTiposCajas) {
        this.idTiposCajas = idTiposCajas;
    }

    public AtributosTiposCajas(Long idTiposCajas, String nombreTiposCajas, short cantidadMedidores) {
        this.idTiposCajas = idTiposCajas;
        this.nombreTiposCajas = nombreTiposCajas;
        this.cantidadMedidores = cantidadMedidores;
    }

    public Long getIdTiposCajas() {
        return idTiposCajas;
    }

    public void setIdTiposCajas(Long idTiposCajas) {
        this.idTiposCajas = idTiposCajas;
    }

    public String getNombreTiposCajas() {
        return nombreTiposCajas;
    }

    public void setNombreTiposCajas(String nombreTiposCajas) {
        this.nombreTiposCajas = nombreTiposCajas;
    }

    public short getCantidadMedidores() {
        return cantidadMedidores;
    }

    public void setCantidadMedidores(short cantidadMedidores) {
        this.cantidadMedidores = cantidadMedidores;
    }

    @XmlTransient
    public Collection<AmyCajas> getAmyCajasCollection() {
        return amyCajasCollection;
    }

    public void setAmyCajasCollection(Collection<AmyCajas> amyCajasCollection) {
        this.amyCajasCollection = amyCajasCollection;
    }

    public AtributosFabricantes getFkAtributosFabricantes() {
        return fkAtributosFabricantes;
    }

    public void setFkAtributosFabricantes(AtributosFabricantes fkAtributosFabricantes) {
        this.fkAtributosFabricantes = fkAtributosFabricantes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTiposCajas != null ? idTiposCajas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AtributosTiposCajas)) {
            return false;
        }
        AtributosTiposCajas other = (AtributosTiposCajas) object;
        if ((this.idTiposCajas == null && other.idTiposCajas != null) || (this.idTiposCajas != null && !this.idTiposCajas.equals(other.idTiposCajas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.AtributosTiposCajas[ idTiposCajas=" + idTiposCajas + " ]";
    }
    
}
