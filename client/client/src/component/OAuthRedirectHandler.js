import React, { useEffect } from "react";
import { ACCESS_TOKEN } from "../constants/index";
import { useLocation, useNavigate } from "react-router-dom";

function OAuth2RedirectHandler() {
  const location = useLocation();
  const navigate = useNavigate();

  const getUrlParameter = (name) => {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)");

    var results = regex.exec(location.search);
    return results === null
      ? ""
      : decodeURIComponent(results[1].replace(/\+/g, " "));
  };

  const isSuccess = getUrlParameter("success");
  const error = getUrlParameter("error");

  useEffect(() => {
    if (isSuccess) {
      localStorage.setItem(ACCESS_TOKEN, isSuccess);
      navigate("/profile", { state: { from: location } });
    } else {
      navigate("/login", { state: { from: location, error: error } });
    }
  }, [isSuccess, error, navigate, location]);

  // Return null or any loading indicator if needed
  return null;
}

export default OAuth2RedirectHandler;
