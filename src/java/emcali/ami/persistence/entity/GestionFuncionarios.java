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
@Table(name = "GESTION_FUNCIONARIOS", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GestionFuncionarios.findAll", query = "SELECT g FROM GestionFuncionarios g"),
    @NamedQuery(name = "GestionFuncionarios.findByIdFuncionarios", query = "SELECT g FROM GestionFuncionarios g WHERE g.idFuncionarios = :idFuncionarios"),
    @NamedQuery(name = "GestionFuncionarios.findByNombreFuncionarios", query = "SELECT g FROM GestionFuncionarios g WHERE g.nombreFuncionarios = :nombreFuncionarios"),
    @NamedQuery(name = "GestionFuncionarios.findByIdentificacion", query = "SELECT g FROM GestionFuncionarios g WHERE g.identificacion = :identificacion"),
    @NamedQuery(name = "GestionFuncionarios.findByMail", query = "SELECT g FROM GestionFuncionarios g WHERE g.mail = :mail"),
    @NamedQuery(name = "GestionFuncionarios.findByTelefonoContacto", query = "SELECT g FROM GestionFuncionarios g WHERE g.telefonoContacto = :telefonoContacto"),
    @NamedQuery(name = "GestionFuncionarios.findByFechaCreacion", query = "SELECT g FROM GestionFuncionarios g WHERE g.fechaCreacion = :fechaCreacion"),
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modificadoPor")
    private List<ComercialClientes> comercialClientesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creadoPor")
    private List<ComercialClientes> comercialClientesList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modificadoPor")
    private List<GestionFuncionarios> gestionFuncionariosList;
    @JoinColumn(name = "MODIFICADO_POR", referencedColumnName = "ID_FUNCIONARIOS")
    @ManyToOne(optional = false)
    private GestionFuncionarios modificadoPor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creadoPor")
    private List<GestionFuncionarios> gestionFuncionariosList1;
    @JoinColumn(name = "CREADO_POR", referencedColumnName = "ID_FUNCIONARIOS")
    @ManyToOne(optional = false)
    private GestionFuncionarios creadoPor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modificadoPor")
    private List<GestionPerfiles> gestionPerfilesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creadoPor")
    private List<GestionPerfiles> gestionPerfilesList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkGestionFuncionarios")
    private List<GestionUsuarios> gestionUsuariosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creadoPor")
    private List<GestionUsuarios> gestionUsuariosList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modificadoPor")
    private List<GestionUsuarios> gestionUsuariosList2;

    public GestionFuncionarios() {
    }

    public GestionFuncionarios(Long idFuncionarios) {
        this.idFuncionarios = idFuncionarios;
    }

    public GestionFuncionarios(Long idFuncionarios, String nombreFuncionarios, String identificacion, String mail, long telefonoContacto, Date fechaCreacion, Date fechaModificacion) {
        this.idFuncionarios = idFuncionarios;
        this.nombreFuncionarios = nombreFuncionarios;
        this.identificacion = identificacion;
        this.mail = mail;
        this.telefonoContacto = telefonoContacto;
        this.fechaCreacion = fechaCreacion;
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

    @XmlTransient
    public List<ComercialClientes> getComercialClientesList() {
        return comercialClientesList;
    }

    public void setComercialClientesList(List<ComercialClientes> comercialClientesList) {
        this.comercialClientesList = comercialClientesList;
    }

    @XmlTransient
    public List<ComercialClientes> getComercialClientesList1() {
        return comercialClientesList1;
    }

    public void setComercialClientesList1(List<ComercialClientes> comercialClientesList1) {
        this.comercialClientesList1 = comercialClientesList1;
    }

    @XmlTransient
    public List<GestionFuncionarios> getGestionFuncionariosList() {
        return gestionFuncionariosList;
    }

    public void setGestionFuncionariosList(List<GestionFuncionarios> gestionFuncionariosList) {
        this.gestionFuncionariosList = gestionFuncionariosList;
    }

    public GestionFuncionarios getModificadoPor() {
        return modificadoPor;
    }

    public void setModificadoPor(GestionFuncionarios modificadoPor) {
        this.modificadoPor = modificadoPor;
    }

    @XmlTransient
    public List<GestionFuncionarios> getGestionFuncionariosList1() {
        return gestionFuncionariosList1;
    }

    public void setGestionFuncionariosList1(List<GestionFuncionarios> gestionFuncionariosList1) {
        this.gestionFuncionariosList1 = gestionFuncionariosList1;
    }

    public GestionFuncionarios getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(GestionFuncionarios creadoPor) {
        this.creadoPor = creadoPor;
    }

    @XmlTransient
    public List<GestionPerfiles> getGestionPerfilesList() {
        return gestionPerfilesList;
    }

    public void setGestionPerfilesList(List<GestionPerfiles> gestionPerfilesList) {
        this.gestionPerfilesList = gestionPerfilesList;
    }

    @XmlTransient
    public List<GestionPerfiles> getGestionPerfilesList1() {
        return gestionPerfilesList1;
    }

    public void setGestionPerfilesList1(List<GestionPerfiles> gestionPerfilesList1) {
        this.gestionPerfilesList1 = gestionPerfilesList1;
    }

    @XmlTransient
    public List<GestionUsuarios> getGestionUsuariosList() {
        return gestionUsuariosList;
    }

    public void setGestionUsuariosList(List<GestionUsuarios> gestionUsuariosList) {
        this.gestionUsuariosList = gestionUsuariosList;
    }

    @XmlTransient
    public List<GestionUsuarios> getGestionUsuariosList1() {
        return gestionUsuariosList1;
    }

    public void setGestionUsuariosList1(List<GestionUsuarios> gestionUsuariosList1) {
        this.gestionUsuariosList1 = gestionUsuariosList1;
    }

    @XmlTransient
    public List<GestionUsuarios> getGestionUsuariosList2() {
        return gestionUsuariosList2;
    }

    public void setGestionUsuariosList2(List<GestionUsuarios> gestionUsuariosList2) {
        this.gestionUsuariosList2 = gestionUsuariosList2;
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
