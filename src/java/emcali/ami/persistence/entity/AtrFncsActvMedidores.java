/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emcali.ami.persistence.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author julian
 */
@Entity
@Table(name = "ATR_FNCS_ACTV_MEDIDORES", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AtrFncsActvMedidores.findAll", query = "SELECT a FROM AtrFncsActvMedidores a"),
    @NamedQuery(name = "AtrFncsActvMedidores.findByIdFuncionActivaMedidor", query = "SELECT a FROM AtrFncsActvMedidores a WHERE a.idFuncionActivaMedidor = :idFuncionActivaMedidor"),
    @NamedQuery(name = "AtrFncsActvMedidores.findByFuncionActiva", query = "SELECT a FROM AtrFncsActvMedidores a WHERE a.funcionActiva = :funcionActiva"),
    @NamedQuery(name = "AtrFncsActvMedidores.findByValor", query = "SELECT a FROM AtrFncsActvMedidores a WHERE a.valor = :valor")})
public class AtrFncsActvMedidores implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_FUNCION_ACTIVA_MEDIDOR")
    private Long idFuncionActivaMedidor;
    @Column(name = "FUNCION_ACTIVA")
    private Short funcionActiva;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "VALOR")
    private Double valor;
    @JoinColumn(name = "FK_ATR_FNC_MEDIDORES", referencedColumnName = "ID_FUNCION_MEDIDORES")
    @ManyToOne(optional = false)
    private AtributosFuncionMedidores fkAtrFncMedidores;

    public AtrFncsActvMedidores() {
    }

    public AtrFncsActvMedidores(Long idFuncionActivaMedidor) {
        this.idFuncionActivaMedidor = idFuncionActivaMedidor;
    }

    public Long getIdFuncionActivaMedidor() {
        return idFuncionActivaMedidor;
    }

    public void setIdFuncionActivaMedidor(Long idFuncionActivaMedidor) {
        this.idFuncionActivaMedidor = idFuncionActivaMedidor;
    }

    public Short getFuncionActiva() {
        return funcionActiva;
    }

    public void setFuncionActiva(Short funcionActiva) {
        this.funcionActiva = funcionActiva;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public AtributosFuncionMedidores getFkAtrFncMedidores() {
        return fkAtrFncMedidores;
    }

    public void setFkAtrFncMedidores(AtributosFuncionMedidores fkAtrFncMedidores) {
        this.fkAtrFncMedidores = fkAtrFncMedidores;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFuncionActivaMedidor != null ? idFuncionActivaMedidor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AtrFncsActvMedidores)) {
            return false;
        }
        AtrFncsActvMedidores other = (AtrFncsActvMedidores) object;
        if ((this.idFuncionActivaMedidor == null && other.idFuncionActivaMedidor != null) || (this.idFuncionActivaMedidor != null && !this.idFuncionActivaMedidor.equals(other.idFuncionActivaMedidor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.AtrFncsActvMedidores[ idFuncionActivaMedidor=" + idFuncionActivaMedidor + " ]";
    }
    
}
