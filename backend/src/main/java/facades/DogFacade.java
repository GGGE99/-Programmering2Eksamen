/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import DTOs.DogDTO;
import DTOs.DogsDTO;
import entities.Breed;
import entities.Dog;
import entities.User;
import errorhandling.DatabaseException;
import java.util.Date;
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

    public DogDTO addDog(User user, String name, String breed, Date date) {
        EntityManager em = emf.createEntityManager();

        Dog dog = new Dog(name, date);
        Breed b = em.find(Breed.class, breed);

        em.getTransaction().begin();
        dog.addUser(user);
        b.addDog(dog);
        em.persist(dog);
        em.getTransaction().commit();

        return new DogDTO(dog);
    }

    public DogsDTO getAllDogsFromAUser(User user) {
        EntityManager em = emf.createEntityManager();

       
            Query query = em.createQuery("SELECT d FROM Dog d WHERE d.user = :user");
            query.setParameter("user", user);
            List<Dog> dogs = query.getResultList();
            DogsDTO dogsDTO = new DogsDTO(dogs);
            return dogsDTO;
       
    }

    public DogDTO updateDog(User user, DogDTO dog) {
        EntityManager em = emf.createEntityManager();
        Dog newDog = null;
        
            em.getTransaction().begin();
            newDog = em.find(Dog.class, dog.getId());
            Breed breed = em.find(Breed.class, dog.getBreed());

            newDog.getBreed().removeDog(newDog);

            newDog.setName(dog.getName());
            newDog.setDateOfBirth(dog.getDateOfBirth());
            breed.addDog(newDog);

            em.getTransaction().commit();
         
        return new DogDTO(newDog);
    }

    public DogDTO deleteDog(User user, String id) throws DatabaseException {
        EntityManager em = emf.createEntityManager();
        DogDTO retDog = null;
        try {
            Dog newDog = em.find(Dog.class, Long.parseLong(id));
            retDog = new DogDTO(newDog);
            Breed breed = em.find(Breed.class, newDog.getBreed().getBreed());
            em.getTransaction().begin();

            if (breed != null) {
                breed.removeDog(newDog);
            }
            if (newDog.getUser() != null) {
                newDog.removeUser();
            }
            System.out.println(breed);

            em.merge(user);
            em.merge(breed);
            em.merge(newDog);

            em.remove(newDog);
            Query query = em.createQuery("DELETE FROM Dog d WHERE d.id = :id");
            query.setParameter("id", Long.parseLong(id));
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw new DatabaseException("there was an error on our database");
        } finally {
            em.close();
        }
        return retDog;
    }
}
