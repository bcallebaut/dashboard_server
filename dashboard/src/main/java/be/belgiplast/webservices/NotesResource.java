package be.belgiplast.webservices;

import java.util.List;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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

    
    
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    //@GET
    //@Produces(MediaType.TEXT_PLAIN)
    //public String getIt() {
    //    return "Got it!";
    //}
    
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Notes> getNote(@QueryParam("id") int id) {
//        try{
        Query q = em.createNamedQuery("Notes.findById");
        //Query q = em.createNamedQuery("Notes.findAll");
        q.setParameter("id", id);
        List<Notes> list = q.getResultList();
        /*
        StringBuilder builder = new StringBuilder();        
        for (Notes note : list){
            builder.append(String.format("Note : id = %d ; name = %s ; description = %s\n",note.getId(),note.getName(),note.getDescription()));
        }
        return builder.toString();
        }catch (Exception e){
            return "error !" + e.getMessage();
        }
*/
        return list;
    }
    
    /**
     *
     * @param contact
     * @return
     */
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Notes create(Notes note) {
        em.getTransaction().begin();
        Date d = new Date();
        note.setTimestamp(d);
        em.persist(note);
        em.getTransaction().commit();    
        
        Query q = em.createNamedQuery("Notes.findByTimeDate");
        q.setParameter("timestamp", d);
        q.setParameter("name", note.getName());
        
        List<Notes> list = q.getResultList();
        
        return list.get(0);
    }
}
