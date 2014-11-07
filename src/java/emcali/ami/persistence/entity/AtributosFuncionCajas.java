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
@Table(name = "ATRIBUTOS_FUNCION_CAJAS", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AtributosFuncionCajas.findAll", query = "SELECT a FROM AtributosFuncionCajas a"),
    @NamedQuery(name = "AtributosFuncionCajas.findByIdFuncion", query = "SELECT a FROM AtributosFuncionCajas a WHERE a.idFuncion = :idFuncion"),
    @NamedQuery(name = "AtributosFuncionCajas.findByFuncion", query = "SELECT a FROM AtributosFuncionCajas a WHERE a.funcion = :funcion")})
public class AtributosFuncionCajas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_FUNCION")
    private Long idFuncion;
    @Size(max = 60)
    @Column(name = "FUNCION")
    private String funcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAtributosFuncionCajas")
    private List<AtrFncsActvCajas> atrFncsActvCajasList;

    public AtributosFuncionCajas() {
    }

    public AtributosFuncionCajas(Long idFuncion) {
        this.idFuncion = idFuncion;
    }

    public Long getIdFuncion() {
        return idFuncion;
    }

    public void setIdFuncion(Long idFuncion) {
        this.idFuncion = idFuncion;
    }

    public String getFuncion() {
        return funcion;
    }

    public void setFuncion(String funcion) {
        this.funcion = funcion;
    }

    @XmlTransient
    public List<AtrFncsActvCajas> getAtrFncsActvCajasList() {
        return atrFncsActvCajasList;
    }

    public void setAtrFncsActvCajasList(List<AtrFncsActvCajas> atrFncsActvCajasList) {
        this.atrFncsActvCajasList = atrFncsActvCajasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFuncion != null ? idFuncion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AtributosFuncionCajas)) {
            return false;
        }
        AtributosFuncionCajas other = (AtributosFuncionCajas) object;
        if ((this.idFuncion == null && other.idFuncion != null) || (this.idFuncion != null && !this.idFuncion.equals(other.idFuncion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.AtributosFuncionCajas[ idFuncion=" + idFuncion + " ]";
    }
    
}
