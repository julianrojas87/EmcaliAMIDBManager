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
@Table(name = "AMY_TIPOLECTURAS", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmyTipolecturas.findAll", query = "SELECT a FROM AmyTipolecturas a"),
    @NamedQuery(name = "AmyTipolecturas.findByIdTipolectura", query = "SELECT a FROM AmyTipolecturas a WHERE a.idTipolectura = :idTipolectura"),
    @NamedQuery(name = "AmyTipolecturas.findByNombreTipolectura", query = "SELECT a FROM AmyTipolecturas a WHERE a.nombreTipolectura = :nombreTipolectura"),
    @NamedQuery(name = "AmyTipolecturas.findByUnidadMedida", query = "SELECT a FROM AmyTipolecturas a WHERE a.unidadMedida = :unidadMedida")})
public class AmyTipolecturas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIPOLECTURA")
    private Long idTipolectura;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NOMBRE_TIPOLECTURA")
    private String nombreTipolectura;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UNIDAD_MEDIDA")
    private String unidadMedida;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAmyTipolecturas")
    private List<AmyLecturas> amyLecturasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAmyTipolecturas")
    private List<ConsumosConsumos> consumosConsumosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAmyTipolecturas")
    private List<AmyConsumos> amyConsumosList;

    public AmyTipolecturas() {
    }

    public AmyTipolecturas(Long idTipolectura) {
        this.idTipolectura = idTipolectura;
    }

    public AmyTipolecturas(Long idTipolectura, String nombreTipolectura, String unidadMedida) {
        this.idTipolectura = idTipolectura;
        this.nombreTipolectura = nombreTipolectura;
        this.unidadMedida = unidadMedida;
    }

    public Long getIdTipolectura() {
        return idTipolectura;
    }

    public void setIdTipolectura(Long idTipolectura) {
        this.idTipolectura = idTipolectura;
    }

    public String getNombreTipolectura() {
        return nombreTipolectura;
    }

    public void setNombreTipolectura(String nombreTipolectura) {
        this.nombreTipolectura = nombreTipolectura;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    @XmlTransient
    public List<AmyLecturas> getAmyLecturasList() {
        return amyLecturasList;
    }

    public void setAmyLecturasList(List<AmyLecturas> amyLecturasList) {
        this.amyLecturasList = amyLecturasList;
    }

    @XmlTransient
    public List<ConsumosConsumos> getConsumosConsumosList() {
        return consumosConsumosList;
    }

    public void setConsumosConsumosList(List<ConsumosConsumos> consumosConsumosList) {
        this.consumosConsumosList = consumosConsumosList;
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
        hash += (idTipolectura != null ? idTipolectura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AmyTipolecturas)) {
            return false;
        }
        AmyTipolecturas other = (AmyTipolecturas) object;
        if ((this.idTipolectura == null && other.idTipolectura != null) || (this.idTipolectura != null && !this.idTipolectura.equals(other.idTipolectura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.AmyTipolecturas[ idTipolectura=" + idTipolectura + " ]";
    }
    
}
