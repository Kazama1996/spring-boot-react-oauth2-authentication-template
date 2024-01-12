import { useEffect } from "react";
import { GetPasswordToken } from "../Action/api";
import { useSearchParams, useNavigate } from "react-router-dom";
import { SendPasswordResetToken } from "../Action/api";

function PasswordResetRedirectHandler() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  const verifyPasswordResetToken = async () => {
    const reqBody = {
      token: searchParams.get("token"),
    };

    await SendPasswordResetToken(reqBody)
      .then((response) => {
        navigate("/updatePassword?token=" + response.data.token);
      })
      .catch((error) => {
        console.log(error.response.data);
        alert(error.response.data);
        navigate("/login");
      });
  };

  useEffect(() => {
    verifyPasswordResetToken();
  });
}

export default PasswordResetRedirectHandler;
