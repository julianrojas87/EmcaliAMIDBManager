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
@Table(name = "AMY_CAJAS", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmyCajas.findAll", query = "SELECT a FROM AmyCajas a"),
    @NamedQuery(name = "AmyCajas.findByIdCajas", query = "SELECT a FROM AmyCajas a WHERE a.idCajas = :idCajas"),
    @NamedQuery(name = "AmyCajas.findByCodigo", query = "SELECT a FROM AmyCajas a WHERE a.codigo = :codigo"),
    @NamedQuery(name = "AmyCajas.findByDireccionIp", query = "SELECT a FROM AmyCajas a WHERE a.direccionIp = :direccionIp"),
    @NamedQuery(name = "AmyCajas.findByLatitud", query = "SELECT a FROM AmyCajas a WHERE a.latitud = :latitud"),
    @NamedQuery(name = "AmyCajas.findByLongitud", query = "SELECT a FROM AmyCajas a WHERE a.longitud = :longitud")})
public class AmyCajas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_CAJAS")
    private Long idCajas;
    @Size(max = 45)
    @Column(name = "CODIGO")
    private String codigo;
    @Size(max = 15)
    @Column(name = "DIRECCION_IP")
    private String direccionIp;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "LATITUD")
    private Double latitud;
    @Column(name = "LONGITUD")
    private Double longitud;
    @JoinColumn(name = "FK_ENERGIA_TRANSFORMADORES", referencedColumnName = "ID_TRANSFORMADORES")
    @ManyToOne(optional = false)
    private EnergiaTransformadores fkEnergiaTransformadores;
    @JoinColumn(name = "FK_TIPOS_CAJAS", referencedColumnName = "ID_TIPOS_CAJAS")
    @ManyToOne(optional = false)
    private AtributosTiposCajas fkTiposCajas;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAmyCajas")
    private List<AtrFncsActvCajas> atrFncsActvCajasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAmyCajas")
    private List<AmyMedidores> amyMedidoresList;

    public AmyCajas() {
    }

    public AmyCajas(Long idCajas) {
        this.idCajas = idCajas;
    }

    public Long getIdCajas() {
        return idCajas;
    }

    public void setIdCajas(Long idCajas) {
        this.idCajas = idCajas;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDireccionIp() {
        return direccionIp;
    }

    public void setDireccionIp(String direccionIp) {
        this.direccionIp = direccionIp;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public EnergiaTransformadores getFkEnergiaTransformadores() {
        return fkEnergiaTransformadores;
    }

    public void setFkEnergiaTransformadores(EnergiaTransformadores fkEnergiaTransformadores) {
        this.fkEnergiaTransformadores = fkEnergiaTransformadores;
    }

    public AtributosTiposCajas getFkTiposCajas() {
        return fkTiposCajas;
    }

    public void setFkTiposCajas(AtributosTiposCajas fkTiposCajas) {
        this.fkTiposCajas = fkTiposCajas;
    }

    @XmlTransient
    public List<AtrFncsActvCajas> getAtrFncsActvCajasList() {
        return atrFncsActvCajasList;
    }

    public void setAtrFncsActvCajasList(List<AtrFncsActvCajas> atrFncsActvCajasList) {
        this.atrFncsActvCajasList = atrFncsActvCajasList;
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
        hash += (idCajas != null ? idCajas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AmyCajas)) {
            return false;
        }
        AmyCajas other = (AmyCajas) object;
        if ((this.idCajas == null && other.idCajas != null) || (this.idCajas != null && !this.idCajas.equals(other.idCajas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.AmyCajas[ idCajas=" + idCajas + " ]";
    }
    
}
