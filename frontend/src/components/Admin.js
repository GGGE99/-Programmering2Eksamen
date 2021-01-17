import React, { useState, useEffect } from "react";
import { Jumbotron, ListGroup } from "react-bootstrap";
import facade from "../facades/AdminFacade";

export default function Admin({ setError }) {
  const init = { breed: "", image: "", info: "", facts: "", wikipedia: "" };

  const [totalSearches, setTotalSearches] = useState("");


  const [breeds, setBreeds] = useState([]);
  const [breed, setBreed] = useState({ ...init });

  useEffect(() => {
    facade.fetchTotalSearches((data) => setTotalSearches(data), setError);
  }, []);


  return (
    <div className="text-center w-100">
        <h1>Total searches: {totalSearches}</h1>


    </div>
  );
}
