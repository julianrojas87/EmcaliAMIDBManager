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
@Table(name = "GESTION_MENU", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GestionMenu.findAll", query = "SELECT g FROM GestionMenu g"),
    @NamedQuery(name = "GestionMenu.findByIdMenu", query = "SELECT g FROM GestionMenu g WHERE g.idMenu = :idMenu"),
    @NamedQuery(name = "GestionMenu.findByNombre", query = "SELECT g FROM GestionMenu g WHERE g.nombre = :nombre"),
    @NamedQuery(name = "GestionMenu.findByComentario", query = "SELECT g FROM GestionMenu g WHERE g.comentario = :comentario")})
public class GestionMenu implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_MENU")
    private Long idMenu;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 20)
    @Column(name = "COMENTARIO")
    private String comentario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkGestionMenu")
    private List<GestionPerfilesMenu> gestionPerfilesMenuList;

    public GestionMenu() {
    }

    public GestionMenu(Long idMenu) {
        this.idMenu = idMenu;
    }

    public GestionMenu(Long idMenu, String nombre) {
        this.idMenu = idMenu;
        this.nombre = nombre;
    }

    public Long getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Long idMenu) {
        this.idMenu = idMenu;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @XmlTransient
    public List<GestionPerfilesMenu> getGestionPerfilesMenuList() {
        return gestionPerfilesMenuList;
    }

    public void setGestionPerfilesMenuList(List<GestionPerfilesMenu> gestionPerfilesMenuList) {
        this.gestionPerfilesMenuList = gestionPerfilesMenuList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMenu != null ? idMenu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GestionMenu)) {
            return false;
        }
        GestionMenu other = (GestionMenu) object;
        if ((this.idMenu == null && other.idMenu != null) || (this.idMenu != null && !this.idMenu.equals(other.idMenu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.GestionMenu[ idMenu=" + idMenu + " ]";
    }
    
}
