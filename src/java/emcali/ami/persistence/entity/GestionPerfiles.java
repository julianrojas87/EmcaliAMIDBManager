/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emcali.ami.persistence.entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author julian
 */
@Entity
@Table(name = "GESTION_PERFILES", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GestionPerfiles.findAll", query = "SELECT g FROM GestionPerfiles g"),
    @NamedQuery(name = "GestionPerfiles.findByIdPerfiles", query = "SELECT g FROM GestionPerfiles g WHERE g.idPerfiles = :idPerfiles"),
    @NamedQuery(name = "GestionPerfiles.findByNombrePerfiles", query = "SELECT g FROM GestionPerfiles g WHERE g.nombrePerfiles = :nombrePerfiles"),
    @NamedQuery(name = "GestionPerfiles.findByFechaCreacion", query = "SELECT g FROM GestionPerfiles g WHERE g.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "GestionPerfiles.findByFechaModificacion", query = "SELECT g FROM GestionPerfiles g WHERE g.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "GestionPerfiles.findByObservaciones", query = "SELECT g FROM GestionPerfiles g WHERE g.observaciones = :observaciones")})
public class GestionPerfiles implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_PERFILES")
    private Long idPerfiles;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NOMBRE_PERFILES")
    private String nombrePerfiles;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_MODIFICACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(max = 45)
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @JoinColumn(name = "MODIFICADO_POR", referencedColumnName = "ID_FUNCIONARIOS")
    @ManyToOne(optional = false)
    private GestionFuncionarios modificadoPor;
    @JoinColumn(name = "CREADO_POR", referencedColumnName = "ID_FUNCIONARIOS")
    @ManyToOne(optional = false)
    private GestionFuncionarios creadoPor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkGestionPerfiles")
    private List<GestionUsuarios> gestionUsuariosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkGestionPerfiles")
    private List<GestionPerfilesMenu> gestionPerfilesMenuList;

    public GestionPerfiles() {
    }

    public GestionPerfiles(Long idPerfiles) {
        this.idPerfiles = idPerfiles;
    }

    public GestionPerfiles(Long idPerfiles, String nombrePerfiles, Date fechaCreacion, Date fechaModificacion) {
        this.idPerfiles = idPerfiles;
        this.nombrePerfiles = nombrePerfiles;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }

    public Long getIdPerfiles() {
        return idPerfiles;
    }

    public void setIdPerfiles(Long idPerfiles) {
        this.idPerfiles = idPerfiles;
    }

    public String getNombrePerfiles() {
        return nombrePerfiles;
    }

    public void setNombrePerfiles(String nombrePerfiles) {
        this.nombrePerfiles = nombrePerfiles;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public GestionFuncionarios getModificadoPor() {
        return modificadoPor;
    }

    public void setModificadoPor(GestionFuncionarios modificadoPor) {
        this.modificadoPor = modificadoPor;
    }

    public GestionFuncionarios getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(GestionFuncionarios creadoPor) {
        this.creadoPor = creadoPor;
    }

    @XmlTransient
    public List<GestionUsuarios> getGestionUsuariosList() {
        return gestionUsuariosList;
    }

    public void setGestionUsuariosList(List<GestionUsuarios> gestionUsuariosList) {
        this.gestionUsuariosList = gestionUsuariosList;
    }

    @XmlTransient
    public List<GestionPerfilesMenu> getGestionPerfilesMenuList() {
        return gestionPerfilesMenuList;
    }

    public void setGestionPerfilesMenuList(List<GestionPerfilesMenu> gestionPerfilesMenuList) {
        this.gestionPerfilesMenuList = gestionPerfilesMenuList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPerfiles != null ? idPerfiles.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GestionPerfiles)) {
            return false;
        }
        GestionPerfiles other = (GestionPerfiles) object;
        if ((this.idPerfiles == null && other.idPerfiles != null) || (this.idPerfiles != null && !this.idPerfiles.equals(other.idPerfiles))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.GestionPerfiles[ idPerfiles=" + idPerfiles + " ]";
    }
    
}
