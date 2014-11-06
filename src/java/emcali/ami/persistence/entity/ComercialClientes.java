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
@Table(name = "COMERCIAL_CLIENTES", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComercialClientes.findAll", query = "SELECT c FROM ComercialClientes c"),
    @NamedQuery(name = "ComercialClientes.findByIdClientes", query = "SELECT c FROM ComercialClientes c WHERE c.idClientes = :idClientes"),
    @NamedQuery(name = "ComercialClientes.findByNombreClientes", query = "SELECT c FROM ComercialClientes c WHERE c.nombreClientes = :nombreClientes"),
    @NamedQuery(name = "ComercialClientes.findByTipoIdentificacion", query = "SELECT c FROM ComercialClientes c WHERE c.tipoIdentificacion = :tipoIdentificacion"),
    @NamedQuery(name = "ComercialClientes.findByDireccion", query = "SELECT c FROM ComercialClientes c WHERE c.direccion = :direccion"),
    @NamedQuery(name = "ComercialClientes.findByTelefono", query = "SELECT c FROM ComercialClientes c WHERE c.telefono = :telefono"),
    @NamedQuery(name = "ComercialClientes.findByMail", query = "SELECT c FROM ComercialClientes c WHERE c.mail = :mail"),
    @NamedQuery(name = "ComercialClientes.findByCreadoPor", query = "SELECT c FROM ComercialClientes c WHERE c.creadoPor = :creadoPor"),
    @NamedQuery(name = "ComercialClientes.findByFechaCreacion", query = "SELECT c FROM ComercialClientes c WHERE c.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ComercialClientes.findByModificadoPor", query = "SELECT c FROM ComercialClientes c WHERE c.modificadoPor = :modificadoPor"),
    @NamedQuery(name = "ComercialClientes.findByFechaModificacion", query = "SELECT c FROM ComercialClientes c WHERE c.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "ComercialClientes.findByObservaciones", query = "SELECT c FROM ComercialClientes c WHERE c.observaciones = :observaciones")})
public class ComercialClientes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_CLIENTES")
    private Long idClientes;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NOMBRE_CLIENTES")
    private String nombreClientes;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "TIPO_IDENTIFICACION")
    private String tipoIdentificacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "DIRECCION")
    private String direccion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "TELEFONO")
    private String telefono;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MAIL")
    private String mail;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkComercialClientes")
    private Collection<TelcoInfo> telcoInfoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkComercialClientes")
    private Collection<ComercialContratos> comercialContratosCollection;

    public ComercialClientes() {
    }

    public ComercialClientes(Long idClientes) {
        this.idClientes = idClientes;
    }

    public ComercialClientes(Long idClientes, String nombreClientes, String tipoIdentificacion, String direccion, String telefono, String mail, long creadoPor, Date fechaCreacion, long modificadoPor, Date fechaModificacion) {
        this.idClientes = idClientes;
        this.nombreClientes = nombreClientes;
        this.tipoIdentificacion = tipoIdentificacion;
        this.direccion = direccion;
        this.telefono = telefono;
        this.mail = mail;
        this.creadoPor = creadoPor;
        this.fechaCreacion = fechaCreacion;
        this.modificadoPor = modificadoPor;
        this.fechaModificacion = fechaModificacion;
    }

    public Long getIdClientes() {
        return idClientes;
    }

    public void setIdClientes(Long idClientes) {
        this.idClientes = idClientes;
    }

    public String getNombreClientes() {
        return nombreClientes;
    }

    public void setNombreClientes(String nombreClientes) {
        this.nombreClientes = nombreClientes;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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
    public Collection<TelcoInfo> getTelcoInfoCollection() {
        return telcoInfoCollection;
    }

    public void setTelcoInfoCollection(Collection<TelcoInfo> telcoInfoCollection) {
        this.telcoInfoCollection = telcoInfoCollection;
    }

    @XmlTransient
    public Collection<ComercialContratos> getComercialContratosCollection() {
        return comercialContratosCollection;
    }

    public void setComercialContratosCollection(Collection<ComercialContratos> comercialContratosCollection) {
        this.comercialContratosCollection = comercialContratosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idClientes != null ? idClientes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComercialClientes)) {
            return false;
        }
        ComercialClientes other = (ComercialClientes) object;
        if ((this.idClientes == null && other.idClientes != null) || (this.idClientes != null && !this.idClientes.equals(other.idClientes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.ComercialClientes[ idClientes=" + idClientes + " ]";
    }
    
}
