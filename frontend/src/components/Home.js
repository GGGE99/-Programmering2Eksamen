import React, { useState, useEffect } from "react";
import { Jumbotron, ListGroup } from "react-bootstrap";
import facade from "../facades/BreedFacade";

export default function Home({ setError }) {
  const init = { breed: "", image: "", info: "", facts: "", wikipedia: "" };

  const [breeds, setBreeds] = useState([]);
  const [breed, setBreed] = useState({ ...init });

  useEffect(() => {
    facade.fetchAllBreeds((data) => setBreeds([...data.dogs]), setError);
  }, []);

  const click = (breed) => {
    facade.fetchBreed((data) => setBreed({ ...data }), breed, setError);
  };

  return (
    <div className="text-center w-100">
      <h1>HEJSA</h1>
      {breed.breed ? (
        <Jumbotron className="p-0" style={{minHeight: 400+"px"}}>
          <img src={breed.image} height="400px" className="float-left"></img>

          <h1>{breed.breed}</h1>
          <p>{breed.image}</p>
          <p>{breed.info}</p>
          <p>{breed.facts}</p>
          <p>{breed.wikipedia || "Kunne ikke finde et link :("}</p>
        </Jumbotron>
      ) : (
        <></>
      )}

      <ListGroup>
        {breeds.map((breed) => {
          return (
            <ListGroup.Item action onClick={() => click(breed.breed)}>
              {breed.breed}
            </ListGroup.Item>
          );
        })}
      </ListGroup>
    </div>
  );
}
