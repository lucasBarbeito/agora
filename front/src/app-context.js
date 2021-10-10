import React from "react";

export const AppContext = React.createContext({
  token: null,
  setToken: () => {},
  labels: []
});
