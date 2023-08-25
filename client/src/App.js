import logo from "./logo.svg";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import MainPage from "./Pages/Profile/Profile";
import LoginPage from "./Pages/Auth/LoginPage";
import RegisterPage from "./Pages/Auth/RegisterPage";
import ResetPasswordPage from "./Pages/Auth/ResetPasswordPage";

import OAuth2RedirectHandler from "./component/OAuthRedirectHandler";
import { useEffect, useState } from "react";
import Profile from "./Pages/Profile/Profile";
import ProfilePage from "./Pages/Profile/Profile";
function App() {
  const [authenticated, setAuthenticated] = useState(false);
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(true);

  const loadCurrentlyLoggedInUser = () => {
    // getCurrentUser()
    //   .then((response) => {
    //     setCurrentUser(response);
    //     setAuthenticated(true);
    //     setLoading(false);
    //   })
    //   .catch((error) => {
    //     setLoading(false);
    //   });
  };

  useEffect(() => {
    loadCurrentlyLoggedInUser();
  }, []);

  return (
    <div className="App">
      <Router>
        <Routes>
          <Route path="/login" Component={LoginPage} />
          <Route path="/" Component={RegisterPage} />
          <Route path="/resetPassword" Component={ResetPasswordPage} />
          <Route path="/profile" Component={ProfilePage} />
          <Route path="/oauth2/redirect" Component={OAuth2RedirectHandler} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
