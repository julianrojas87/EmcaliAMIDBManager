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
@Table(name = "SALDOS_SALDO", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SaldosSaldo.findAll", query = "SELECT s FROM SaldosSaldo s"),
    @NamedQuery(name = "SaldosSaldo.findByIdSaldo", query = "SELECT s FROM SaldosSaldo s WHERE s.idSaldo = :idSaldo"),
    @NamedQuery(name = "SaldosSaldo.findByFechaInicioCiclo", query = "SELECT s FROM SaldosSaldo s WHERE s.fechaInicioCiclo = :fechaInicioCiclo"),
    @NamedQuery(name = "SaldosSaldo.findByFechaLectura", query = "SELECT s FROM SaldosSaldo s WHERE s.fechaLectura = :fechaLectura"),
    @NamedQuery(name = "SaldosSaldo.findByLecturaInicio", query = "SELECT s FROM SaldosSaldo s WHERE s.lecturaInicio = :lecturaInicio"),
    @NamedQuery(name = "SaldosSaldo.findByLecturaActual", query = "SELECT s FROM SaldosSaldo s WHERE s.lecturaActual = :lecturaActual"),
    @NamedQuery(name = "SaldosSaldo.findBySaldosSaldocol", query = "SELECT s FROM SaldosSaldo s WHERE s.saldosSaldocol = :saldosSaldocol")})
public class SaldosSaldo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_SALDO")
    private Long idSaldo;
    @Column(name = "FECHA_INICIO_CICLO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicioCiclo;
    @Column(name = "FECHA_LECTURA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaLectura;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "LECTURA_INICIO")
    private Double lecturaInicio;
    @Column(name = "LECTURA_ACTUAL")
    private Double lecturaActual;
    @Size(max = 45)
    @Column(name = "SALDOS_SALDOCOL")
    private String saldosSaldocol;

    public SaldosSaldo() {
    }

    public SaldosSaldo(Long idSaldo) {
        this.idSaldo = idSaldo;
    }

    public Long getIdSaldo() {
        return idSaldo;
    }

    public void setIdSaldo(Long idSaldo) {
        this.idSaldo = idSaldo;
    }

    public Date getFechaInicioCiclo() {
        return fechaInicioCiclo;
    }

    public void setFechaInicioCiclo(Date fechaInicioCiclo) {
        this.fechaInicioCiclo = fechaInicioCiclo;
    }

    public Date getFechaLectura() {
        return fechaLectura;
    }

    public void setFechaLectura(Date fechaLectura) {
        this.fechaLectura = fechaLectura;
    }

    public Double getLecturaInicio() {
        return lecturaInicio;
    }

    public void setLecturaInicio(Double lecturaInicio) {
        this.lecturaInicio = lecturaInicio;
    }

    public Double getLecturaActual() {
        return lecturaActual;
    }

    public void setLecturaActual(Double lecturaActual) {
        this.lecturaActual = lecturaActual;
    }

    public String getSaldosSaldocol() {
        return saldosSaldocol;
    }

    public void setSaldosSaldocol(String saldosSaldocol) {
        this.saldosSaldocol = saldosSaldocol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSaldo != null ? idSaldo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaldosSaldo)) {
            return false;
        }
        SaldosSaldo other = (SaldosSaldo) object;
        if ((this.idSaldo == null && other.idSaldo != null) || (this.idSaldo != null && !this.idSaldo.equals(other.idSaldo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.SaldosSaldo[ idSaldo=" + idSaldo + " ]";
    }
    
}
