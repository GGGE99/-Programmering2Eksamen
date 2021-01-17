import { baseURL } from "../utils/settings";
import { makeOptions, handleHttpErrors, fetcher } from "../utils/fetchUtils";

function userFacade() {
  const URL = baseURL + "api/admin/";

  const fetchTotalSearches = (action, setError) => {
    const options = makeOptions("GET", true);
    return fetcher(URL + "count" , options, action, setError);
  };

  const fetchTotalSearchesForABreed = (action, breed, setError) => {
    const options = makeOptions("GET", true);
    return fetcher(URL + "count/" + breed , options, action, setError);
  };

  return {
    fetchTotalSearches,
    fetchTotalSearchesForABreed
  };
}

const facade = userFacade();
export default facade;