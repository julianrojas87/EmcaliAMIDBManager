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
@Table(name = "ATR_FNCS_ACTV_CAJAS", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AtrFncsActvCajas.findAll", query = "SELECT a FROM AtrFncsActvCajas a"),
    @NamedQuery(name = "AtrFncsActvCajas.findByIdFuncionActivaCaja", query = "SELECT a FROM AtrFncsActvCajas a WHERE a.idFuncionActivaCaja = :idFuncionActivaCaja"),
    @NamedQuery(name = "AtrFncsActvCajas.findByFuncionActiva", query = "SELECT a FROM AtrFncsActvCajas a WHERE a.funcionActiva = :funcionActiva")})
public class AtrFncsActvCajas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_FUNCION_ACTIVA_CAJA")
    private Long idFuncionActivaCaja;
    @Column(name = "FUNCION_ACTIVA")
    private Short funcionActiva;
    @JoinColumn(name = "FK_ATRIBUTOS_FUNCION_CAJAS", referencedColumnName = "ID_FUNCION")
    @ManyToOne(optional = false)
    private AtributosFuncionCajas fkAtributosFuncionCajas;
    @JoinColumn(name = "FK_AMY_CAJAS", referencedColumnName = "ID_CAJAS")
    @ManyToOne(optional = false)
    private AmyCajas fkAmyCajas;

    public AtrFncsActvCajas() {
    }

    public AtrFncsActvCajas(Long idFuncionActivaCaja) {
        this.idFuncionActivaCaja = idFuncionActivaCaja;
    }

    public Long getIdFuncionActivaCaja() {
        return idFuncionActivaCaja;
    }

    public void setIdFuncionActivaCaja(Long idFuncionActivaCaja) {
        this.idFuncionActivaCaja = idFuncionActivaCaja;
    }

    public Short getFuncionActiva() {
        return funcionActiva;
    }

    public void setFuncionActiva(Short funcionActiva) {
        this.funcionActiva = funcionActiva;
    }

    public AtributosFuncionCajas getFkAtributosFuncionCajas() {
        return fkAtributosFuncionCajas;
    }

    public void setFkAtributosFuncionCajas(AtributosFuncionCajas fkAtributosFuncionCajas) {
        this.fkAtributosFuncionCajas = fkAtributosFuncionCajas;
    }

    public AmyCajas getFkAmyCajas() {
        return fkAmyCajas;
    }

    public void setFkAmyCajas(AmyCajas fkAmyCajas) {
        this.fkAmyCajas = fkAmyCajas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFuncionActivaCaja != null ? idFuncionActivaCaja.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AtrFncsActvCajas)) {
            return false;
        }
        AtrFncsActvCajas other = (AtrFncsActvCajas) object;
        if ((this.idFuncionActivaCaja == null && other.idFuncionActivaCaja != null) || (this.idFuncionActivaCaja != null && !this.idFuncionActivaCaja.equals(other.idFuncionActivaCaja))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.AtrFncsActvCajas[ idFuncionActivaCaja=" + idFuncionActivaCaja + " ]";
    }
    
}
