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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author julian
 */
@Entity
@Table(name = "AMY_LECTURAS", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmyLecturas.findAll", query = "SELECT a FROM AmyLecturas a"),
    @NamedQuery(name = "AmyLecturas.findByIdLecturas", query = "SELECT a FROM AmyLecturas a WHERE a.idLecturas = :idLecturas"),
    @NamedQuery(name = "AmyLecturas.findByFechaLectura", query = "SELECT a FROM AmyLecturas a WHERE a.fechaLectura = :fechaLectura"),
    @NamedQuery(name = "AmyLecturas.findByLectura", query = "SELECT a FROM AmyLecturas a WHERE a.lectura = :lectura"),
    @NamedQuery(name = "AmyLecturas.findByValido", query = "SELECT a FROM AmyLecturas a WHERE a.valido = :valido"),
    @NamedQuery(name = "AmyLecturas.findByIntervalo", query = "SELECT a FROM AmyLecturas a WHERE a.intervalo = :intervalo")})
public class AmyLecturas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_LECTURAS")
    private Long idLecturas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_LECTURA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaLectura;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LECTURA")
    private double lectura;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VALIDO")
    private short valido;
    @Column(name = "INTERVALO")
    private Long intervalo;
    @JoinColumn(name = "FK_AMY_TIPOLECTURAS", referencedColumnName = "ID_TIPOLECTURA")
    @ManyToOne(optional = false)
    private AmyTipolecturas fkAmyTipolecturas;
    @JoinColumn(name = "FK_AMY_MEDIDORES", referencedColumnName = "ID_MEDIDORES")
    @ManyToOne(optional = false)
    private AmyMedidores fkAmyMedidores;
    @JoinColumn(name = "FK_AMY_INTERVAL", referencedColumnName = "ID_TIEMPO")
    @ManyToOne(optional = false)
    private AmyInterval fkAmyInterval;
    @JoinColumn(name = "FK_AMY_CANAL", referencedColumnName = "ID_CANAL")
    @ManyToOne(optional = false)
    private AmyCanal fkAmyCanal;

    public AmyLecturas() {
    }

    public AmyLecturas(Long idLecturas) {
        this.idLecturas = idLecturas;
    }

    public AmyLecturas(Long idLecturas, Date fechaLectura, double lectura, short valido) {
        this.idLecturas = idLecturas;
        this.fechaLectura = fechaLectura;
        this.lectura = lectura;
        this.valido = valido;
    }

    public Long getIdLecturas() {
        return idLecturas;
    }

    public void setIdLecturas(Long idLecturas) {
        this.idLecturas = idLecturas;
    }

    public Date getFechaLectura() {
        return fechaLectura;
    }

    public void setFechaLectura(Date fechaLectura) {
        this.fechaLectura = fechaLectura;
    }

    public double getLectura() {
        return lectura;
    }

    public void setLectura(double lectura) {
        this.lectura = lectura;
    }

    public short getValido() {
        return valido;
    }

    public void setValido(short valido) {
        this.valido = valido;
    }

    public Long getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(Long intervalo) {
        this.intervalo = intervalo;
    }

    public AmyTipolecturas getFkAmyTipolecturas() {
        return fkAmyTipolecturas;
    }

    public void setFkAmyTipolecturas(AmyTipolecturas fkAmyTipolecturas) {
        this.fkAmyTipolecturas = fkAmyTipolecturas;
    }

    public AmyMedidores getFkAmyMedidores() {
        return fkAmyMedidores;
    }

    public void setFkAmyMedidores(AmyMedidores fkAmyMedidores) {
        this.fkAmyMedidores = fkAmyMedidores;
    }

    public AmyInterval getFkAmyInterval() {
        return fkAmyInterval;
    }

    public void setFkAmyInterval(AmyInterval fkAmyInterval) {
        this.fkAmyInterval = fkAmyInterval;
    }

    public AmyCanal getFkAmyCanal() {
        return fkAmyCanal;
    }

    public void setFkAmyCanal(AmyCanal fkAmyCanal) {
        this.fkAmyCanal = fkAmyCanal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLecturas != null ? idLecturas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AmyLecturas)) {
            return false;
        }
        AmyLecturas other = (AmyLecturas) object;
        if ((this.idLecturas == null && other.idLecturas != null) || (this.idLecturas != null && !this.idLecturas.equals(other.idLecturas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.AmyLecturas[ idLecturas=" + idLecturas + " ]";
    }
    
}
