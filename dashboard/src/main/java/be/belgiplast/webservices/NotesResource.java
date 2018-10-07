package be.belgiplast.webservices;

import java.util.List;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * Root resource (exposed at "myresource" path)
 */
@Path("notes")
public class NotesResource {

    private static final String PERSISTENCE_UNIT_NAME = "be.belgiplast.webservices_dashboard_jar_1.0-SNAPSHOTPU";
    private static EntityManagerFactory factory;
    private EntityManager em;
    
    public NotesResource() {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();
    }
        
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Notes> getNewNotes(@QueryParam("from") Date date) {
        if (date == null){
            Query q = em.createNamedQuery("Notes.findAll");        
            List<Notes> list = q.getResultList();    
            return list;
        }else{
            Query q = em.createNamedQuery("Notes.findByModification");
            q.setParameter("modification", date, TemporalType.TIMESTAMP);
            List<Notes> list = q.getResultList();    
            return list;
        }
    }
    
    /**
     *
     * @param contact
     * @return
     */
    @Path("new")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Notes create(Notes note) {
        em.getTransaction().begin();
        Date d = new Date();
        note.setTimestamp(d);
        note.setModification(d);
        em.persist(note);
        em.getTransaction().commit();    
        
        Query q = em.createNamedQuery("Notes.findByTimeDate");
        q.setParameter("timestamp", d);
        q.setParameter("name", note.getName());
        
        List<Notes> list = q.getResultList();
        
        return list.get(0);
    }
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Notes update(Notes note,@PathParam("id") long id) {
        Notes n = em.find(Notes.class, id);
        em.getTransaction().begin();        
        n.setModification(new Date());
        n.setName(note.getName());
        n.setDescription(note.getDescription());
        em.persist(note);
        em.getTransaction().commit();    ;
        
        return note;
    }
    
    @DELETE
    public void delete(Notes note){
        Notes n = em.find(Notes.class, note.getId());
        em.getTransaction().begin();                
        em.remove(n);
        em.getTransaction().commit();    ;
    }
}
