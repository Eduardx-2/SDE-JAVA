/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.util;

import com.util.exceptions.NonexistentEntityException;
import com.util.exceptions.PreexistingEntityException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import paquetes.PequetesData;

/**
 *
 * @author eduardx_2
 */
public class PequetesDataJpaController implements Serializable {

    public PequetesDataJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PequetesData pequetesData) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(pequetesData);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPequetesData(pequetesData.getCodigo()) != null) {
                throw new PreexistingEntityException("PequetesData " + pequetesData + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PequetesData pequetesData) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            pequetesData = em.merge(pequetesData);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = pequetesData.getCodigo();
                if (findPequetesData(id) == null) {
                    throw new NonexistentEntityException("The pequetesData with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PequetesData pequetesData;
            try {
                pequetesData = em.getReference(PequetesData.class, id);
                pequetesData.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pequetesData with id " + id + " no longer exists.", enfe);
            }
            em.remove(pequetesData);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PequetesData> findPequetesDataEntities() {
        return findPequetesDataEntities(true, -1, -1);
    }

    public List<PequetesData> findPequetesDataEntities(int maxResults, int firstResult) {
        return findPequetesDataEntities(false, maxResults, firstResult);
    }

    private List<PequetesData> findPequetesDataEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PequetesData.class));
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

    public PequetesData findPequetesData(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PequetesData.class, id);
        } finally {
            em.close();
        }
    }

    public int getPequetesDataCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PequetesData> rt = cq.from(PequetesData.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
