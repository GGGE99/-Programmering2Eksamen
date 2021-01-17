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
        <Jumbotron className="p-1" style={{ minHeight: 410 + "px" }}>
          <img src={breed.image} height="400px" className="float-left p-1"></img>

          <h1 className="p-4">{breed.breed.charAt(0).toLocaleUpperCase() + breed.breed.substr(1)}</h1>

          <ListGroup>
            <ListGroup.Item action className="mb-2">{breed.info}</ListGroup.Item>
            <ListGroup.Item action className="mb-2">{breed.facts}</ListGroup.Item>
            <ListGroup.Item action className="mb-2" href={breed.wikipedia}>{breed.wikipedia || "Kunne ikke finde et link :("}</ListGroup.Item>
          </ListGroup>
        </Jumbotron>
      ) : (
        <></>
      )}

      <ListGroup>
        {breeds.map((breed) => {
          return (
            <ListGroup.Item action onClick={() => click(breed.breed)}>
              {breed.breed.charAt(0).toLocaleUpperCase() + breed.breed.substr(1)}
            </ListGroup.Item>
          );
        })}
      </ListGroup>
    </div>
  );
}
