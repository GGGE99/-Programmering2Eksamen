/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import DTOs.DogDTO;
import DTOs.DogsDTO;
import entities.Dog;
import entities.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import utils.Env;

/**
 *
 * @author marcg
 */
public class DogFacade {

    private static EntityManagerFactory emf;
    private static DogFacade instance;
    private static Env env = Env.GetEnv();
    public static final DateFacade dateFacade = DateFacade.getDateFacade("dd-MM-yyyy HH:mm:ss");

    public static DogFacade getDogFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new DogFacade();
        }
        return instance;
    }

    public DogDTO addDog(User user, DogDTO dogDTO) {
        EntityManager em = emf.createEntityManager();
        Dog dog = new Dog(dogDTO.getName(), dogDTO.getDateOfBirth(), dogDTO.getInfo(), dogDTO.getBreed());
        em.getTransaction().begin();
        user.addDog(dog);
        em.persist(dog);
        em.getTransaction().commit();

        return new DogDTO(dog);
    }
    
    public DogsDTO getAllDogsFromAUser(User user){
        EntityManager em = emf.createEntityManager();
        
        try{
            Query query = em.createQuery("SELECT d FROM Dog d WHERE d.user = :user");
            query.setParameter("user", user);
            List<Dog> dogs = query.getResultList();
            DogsDTO dogsDTO = new DogsDTO(dogs);
            return dogsDTO;
        } finally {
            em.close();
        }
    }
}
