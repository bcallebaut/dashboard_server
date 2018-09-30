/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.belgiplast.webservices;

import be.belgiplast.webservices.exceptions.NonexistentEntityException;
import be.belgiplast.webservices.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author benoit
 */
public class NotesJpaController implements Serializable {

    public NotesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Notes notes) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(notes);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findNotes(notes.getId()) != null) {
                throw new PreexistingEntityException("Notes " + notes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Notes notes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            notes = em.merge(notes);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = notes.getId();
                if (findNotes(id) == null) {
                    throw new NonexistentEntityException("The notes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Notes notes;
            try {
                notes = em.getReference(Notes.class, id);
                notes.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The notes with id " + id + " no longer exists.", enfe);
            }
            em.remove(notes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Notes> findNotesEntities() {
        return findNotesEntities(true, -1, -1);
    }

    public List<Notes> findNotesEntities(int maxResults, int firstResult) {
        return findNotesEntities(false, maxResults, firstResult);
    }

    private List<Notes> findNotesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Notes.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Notes findNotes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Notes.class, id);
        } finally {
            em.close();
        }
    }

    public int getNotesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Notes> rt = cq.from(Notes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
