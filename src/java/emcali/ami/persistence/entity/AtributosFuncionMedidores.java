/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emcali.ami.persistence.entity;

import java.io.Serializable;
import java.util.Collection;
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
@Table(name = "ATRIBUTOS_FUNCION_MEDIDORES", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AtributosFuncionMedidores.findAll", query = "SELECT a FROM AtributosFuncionMedidores a"),
    @NamedQuery(name = "AtributosFuncionMedidores.findByIdFuncionMedidores", query = "SELECT a FROM AtributosFuncionMedidores a WHERE a.idFuncionMedidores = :idFuncionMedidores"),
    @NamedQuery(name = "AtributosFuncionMedidores.findByFuncion", query = "SELECT a FROM AtributosFuncionMedidores a WHERE a.funcion = :funcion"),
    @NamedQuery(name = "AtributosFuncionMedidores.findByParametro1", query = "SELECT a FROM AtributosFuncionMedidores a WHERE a.parametro1 = :parametro1"),
    @NamedQuery(name = "AtributosFuncionMedidores.findByParametro2", query = "SELECT a FROM AtributosFuncionMedidores a WHERE a.parametro2 = :parametro2")})
public class AtributosFuncionMedidores implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_FUNCION_MEDIDORES")
    private Long idFuncionMedidores;
    @Size(max = 45)
    @Column(name = "FUNCION")
    private String funcion;
    @Column(name = "PARAMETRO1")
    private Long parametro1;
    @Column(name = "PARAMETRO2")
    private Long parametro2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAtrFncMedidores")
    private Collection<AtrFncsActvMedidores> atrFncsActvMedidoresCollection;

    public AtributosFuncionMedidores() {
    }

    public AtributosFuncionMedidores(Long idFuncionMedidores) {
        this.idFuncionMedidores = idFuncionMedidores;
    }

    public Long getIdFuncionMedidores() {
        return idFuncionMedidores;
    }

    public void setIdFuncionMedidores(Long idFuncionMedidores) {
        this.idFuncionMedidores = idFuncionMedidores;
    }

    public String getFuncion() {
        return funcion;
    }

    public void setFuncion(String funcion) {
        this.funcion = funcion;
    }

    public Long getParametro1() {
        return parametro1;
    }

    public void setParametro1(Long parametro1) {
        this.parametro1 = parametro1;
    }

    public Long getParametro2() {
        return parametro2;
    }

    public void setParametro2(Long parametro2) {
        this.parametro2 = parametro2;
    }

    @XmlTransient
    public Collection<AtrFncsActvMedidores> getAtrFncsActvMedidoresCollection() {
        return atrFncsActvMedidoresCollection;
    }

    public void setAtrFncsActvMedidoresCollection(Collection<AtrFncsActvMedidores> atrFncsActvMedidoresCollection) {
        this.atrFncsActvMedidoresCollection = atrFncsActvMedidoresCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFuncionMedidores != null ? idFuncionMedidores.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AtributosFuncionMedidores)) {
            return false;
        }
        AtributosFuncionMedidores other = (AtributosFuncionMedidores) object;
        if ((this.idFuncionMedidores == null && other.idFuncionMedidores != null) || (this.idFuncionMedidores != null && !this.idFuncionMedidores.equals(other.idFuncionMedidores))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.AtributosFuncionMedidores[ idFuncionMedidores=" + idFuncionMedidores + " ]";
    }
    
}
