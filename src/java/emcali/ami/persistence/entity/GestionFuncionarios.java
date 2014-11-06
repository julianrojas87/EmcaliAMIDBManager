/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emcali.ami.persistence.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "GESTION_FUNCIONARIOS", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GestionFuncionarios.findAll", query = "SELECT g FROM GestionFuncionarios g"),
    @NamedQuery(name = "GestionFuncionarios.findByIdFuncionarios", query = "SELECT g FROM GestionFuncionarios g WHERE g.idFuncionarios = :idFuncionarios"),
    @NamedQuery(name = "GestionFuncionarios.findByNombreFuncionarios", query = "SELECT g FROM GestionFuncionarios g WHERE g.nombreFuncionarios = :nombreFuncionarios"),
    @NamedQuery(name = "GestionFuncionarios.findByIdentificacion", query = "SELECT g FROM GestionFuncionarios g WHERE g.identificacion = :identificacion"),
    @NamedQuery(name = "GestionFuncionarios.findByMail", query = "SELECT g FROM GestionFuncionarios g WHERE g.mail = :mail"),
    @NamedQuery(name = "GestionFuncionarios.findByTelefonoContacto", query = "SELECT g FROM GestionFuncionarios g WHERE g.telefonoContacto = :telefonoContacto"),
    @NamedQuery(name = "GestionFuncionarios.findByCreadoPor", query = "SELECT g FROM GestionFuncionarios g WHERE g.creadoPor = :creadoPor"),
    @NamedQuery(name = "GestionFuncionarios.findByFechaCreacion", query = "SELECT g FROM GestionFuncionarios g WHERE g.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "GestionFuncionarios.findByModificadoPor", query = "SELECT g FROM GestionFuncionarios g WHERE g.modificadoPor = :modificadoPor"),
    @NamedQuery(name = "GestionFuncionarios.findByFechaModificacion", query = "SELECT g FROM GestionFuncionarios g WHERE g.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "GestionFuncionarios.findByObservaciones", query = "SELECT g FROM GestionFuncionarios g WHERE g.observaciones = :observaciones")})
public class GestionFuncionarios implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_FUNCIONARIOS")
    private Long idFuncionarios;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NOMBRE_FUNCIONARIOS")
    private String nombreFuncionarios;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "IDENTIFICACION")
    private String identificacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MAIL")
    private String mail;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TELEFONO_CONTACTO")
    private long telefonoContacto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREADO_POR")
    private long creadoPor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MODIFICADO_POR")
    private long modificadoPor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_MODIFICACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(max = 45)
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkGestionFuncionarios")
    private Collection<GestionUsuarios> gestionUsuariosCollection;

    public GestionFuncionarios() {
    }

    public GestionFuncionarios(Long idFuncionarios) {
        this.idFuncionarios = idFuncionarios;
    }

    public GestionFuncionarios(Long idFuncionarios, String nombreFuncionarios, String identificacion, String mail, long telefonoContacto, long creadoPor, Date fechaCreacion, long modificadoPor, Date fechaModificacion) {
        this.idFuncionarios = idFuncionarios;
        this.nombreFuncionarios = nombreFuncionarios;
        this.identificacion = identificacion;
        this.mail = mail;
        this.telefonoContacto = telefonoContacto;
        this.creadoPor = creadoPor;
        this.fechaCreacion = fechaCreacion;
        this.modificadoPor = modificadoPor;
        this.fechaModificacion = fechaModificacion;
    }

    public Long getIdFuncionarios() {
        return idFuncionarios;
    }

    public void setIdFuncionarios(Long idFuncionarios) {
        this.idFuncionarios = idFuncionarios;
    }

    public String getNombreFuncionarios() {
        return nombreFuncionarios;
    }

    public void setNombreFuncionarios(String nombreFuncionarios) {
        this.nombreFuncionarios = nombreFuncionarios;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public long getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(long telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public long getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(long creadoPor) {
        this.creadoPor = creadoPor;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public long getModificadoPor() {
        return modificadoPor;
    }

    public void setModificadoPor(long modificadoPor) {
        this.modificadoPor = modificadoPor;
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

    @XmlTransient
    public Collection<GestionUsuarios> getGestionUsuariosCollection() {
        return gestionUsuariosCollection;
    }

    public void setGestionUsuariosCollection(Collection<GestionUsuarios> gestionUsuariosCollection) {
        this.gestionUsuariosCollection = gestionUsuariosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFuncionarios != null ? idFuncionarios.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GestionFuncionarios)) {
            return false;
        }
        GestionFuncionarios other = (GestionFuncionarios) object;
        if ((this.idFuncionarios == null && other.idFuncionarios != null) || (this.idFuncionarios != null && !this.idFuncionarios.equals(other.idFuncionarios))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.GestionFuncionarios[ idFuncionarios=" + idFuncionarios + " ]";
    }
    
}
