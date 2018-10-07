/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.belgiplast.webservices;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * @author benoit
 */
@Entity
@Table(name = "notes")
@XmlRootElement
@NamedQueries({
      @NamedQuery(name = "Notes.findAll", query = "SELECT n FROM Notes n")
    , @NamedQuery(name = "Notes.findById", query = "SELECT n FROM Notes n WHERE n.id = :id")
    , @NamedQuery(name = "Notes.findByTimestamp", query = "SELECT n FROM Notes n WHERE n.timestamp = :timestamp")
    , @NamedQuery(name = "Notes.findByModification", query = "SELECT n FROM Notes n WHERE n.modification= :modification")
    , @NamedQuery(name = "Notes.findByName", query = "SELECT n FROM Notes n WHERE n.name = :name")
    , @NamedQuery(name = "Notes.findByDescription", query = "SELECT n FROM Notes n WHERE n.description = :description")
    , @NamedQuery(name = "Notes.findByTimeDate", query = "SELECT n FROM Notes n WHERE n.timestamp = :timestamp AND n.name = :name")
        
})    
public class Notes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @Column(name = "modification")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modification;
    @Size(max = 25)
    @Column(name = "name")
    private String name;
    @Size(max = 2048)
    @Column(name = "description")
    private String description;

    public Notes() {
    }

    public Notes(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getModification() {
        return modification;
    }

    public void setModification(Date modification) {
        this.modification = modification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notes)) {
            return false;
        }
        Notes other = (Notes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "be.belgiplast.webservices.Notes[ id=" + id + " ]";
    }
    
}
