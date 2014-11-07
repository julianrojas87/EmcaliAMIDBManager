/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emcali.ami.persistence.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author julian
 */
@Entity
@Table(name = "GESTION_USUARIOS", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GestionUsuarios.findAll", query = "SELECT g FROM GestionUsuarios g"),
    @NamedQuery(name = "GestionUsuarios.findByIdUsuarios", query = "SELECT g FROM GestionUsuarios g WHERE g.idUsuarios = :idUsuarios"),
    @NamedQuery(name = "GestionUsuarios.findByLogin", query = "SELECT g FROM GestionUsuarios g WHERE g.login = :login"),
    @NamedQuery(name = "GestionUsuarios.findByPassword", query = "SELECT g FROM GestionUsuarios g WHERE g.password = :password"),
    @NamedQuery(name = "GestionUsuarios.findByUltimoAcceso", query = "SELECT g FROM GestionUsuarios g WHERE g.ultimoAcceso = :ultimoAcceso"),
    @NamedQuery(name = "GestionUsuarios.findByFechaCreacion", query = "SELECT g FROM GestionUsuarios g WHERE g.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "GestionUsuarios.findByFechaModificacion", query = "SELECT g FROM GestionUsuarios g WHERE g.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "GestionUsuarios.findByObservaciones", query = "SELECT g FROM GestionUsuarios g WHERE g.observaciones = :observaciones")})
public class GestionUsuarios implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_USUARIOS")
    private Long idUsuarios;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "LOGIN")
    private String login;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "PASSWORD")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ULTIMO_ACCESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimoAcceso;
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
    @JoinColumn(name = "FK_GESTION_PERFILES", referencedColumnName = "ID_PERFILES")
    @ManyToOne(optional = false)
    private GestionPerfiles fkGestionPerfiles;
    @JoinColumn(name = "FK_GESTION_FUNCIONARIOS", referencedColumnName = "ID_FUNCIONARIOS")
    @ManyToOne(optional = false)
    private GestionFuncionarios fkGestionFuncionarios;
    @JoinColumn(name = "CREADO_POR", referencedColumnName = "ID_FUNCIONARIOS")
    @ManyToOne(optional = false)
    private GestionFuncionarios creadoPor;
    @JoinColumn(name = "MODIFICADO_POR", referencedColumnName = "ID_FUNCIONARIOS")
    @ManyToOne(optional = false)
    private GestionFuncionarios modificadoPor;

    public GestionUsuarios() {
    }

    public GestionUsuarios(Long idUsuarios) {
        this.idUsuarios = idUsuarios;
    }

    public GestionUsuarios(Long idUsuarios, String login, String password, Date ultimoAcceso, Date fechaCreacion, Date fechaModificacion) {
        this.idUsuarios = idUsuarios;
        this.login = login;
        this.password = password;
        this.ultimoAcceso = ultimoAcceso;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }

    public Long getIdUsuarios() {
        return idUsuarios;
    }

    public void setIdUsuarios(Long idUsuarios) {
        this.idUsuarios = idUsuarios;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(Date ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
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

    public GestionPerfiles getFkGestionPerfiles() {
        return fkGestionPerfiles;
    }

    public void setFkGestionPerfiles(GestionPerfiles fkGestionPerfiles) {
        this.fkGestionPerfiles = fkGestionPerfiles;
    }

    public GestionFuncionarios getFkGestionFuncionarios() {
        return fkGestionFuncionarios;
    }

    public void setFkGestionFuncionarios(GestionFuncionarios fkGestionFuncionarios) {
        this.fkGestionFuncionarios = fkGestionFuncionarios;
    }

    public GestionFuncionarios getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(GestionFuncionarios creadoPor) {
        this.creadoPor = creadoPor;
    }

    public GestionFuncionarios getModificadoPor() {
        return modificadoPor;
    }

    public void setModificadoPor(GestionFuncionarios modificadoPor) {
        this.modificadoPor = modificadoPor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuarios != null ? idUsuarios.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GestionUsuarios)) {
            return false;
        }
        GestionUsuarios other = (GestionUsuarios) object;
        if ((this.idUsuarios == null && other.idUsuarios != null) || (this.idUsuarios != null && !this.idUsuarios.equals(other.idUsuarios))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.GestionUsuarios[ idUsuarios=" + idUsuarios + " ]";
    }
    
}
