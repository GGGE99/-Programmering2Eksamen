import { baseURL } from "../utils/settings";
import { makeOptions, handleHttpErrors, fetcher } from "../utils/fetchUtils";

function userFacade() {
  const URL = baseURL + "api/dog/";

  const fetchAddDog = (action, body, setError) => {
    const options = makeOptions("POST", true, body);
    return fetcher(URL , options, action, setError);
  };

  const fetchAllOfAUSersDog = (action, setError) => {
    const options = makeOptions("GET", true);
    return fetcher(URL , options, action, setError);
  };

  const fetchUpdateDog = (action, body, setError) => {
    const options = makeOptions("PUT", true, body);
    return fetcher(URL , options, action, setError);
  };

  return {
    fetchAddDog,
    fetchAllOfAUSersDog,
    fetchUpdateDog,
  };
}

const facade = userFacade();
export default facade;