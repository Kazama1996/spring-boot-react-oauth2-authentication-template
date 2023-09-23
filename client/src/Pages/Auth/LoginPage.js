import {
  Container,
  Row,
  Col,
  Form,
  Button,
  Navbar,
  Image,
} from "react-bootstrap";
import { useRef, useState, useEffect } from "react";
import EmailProviderModal from "../../component/EmailProviderModal";
import { SendForgotPasswordRequest, SendLoginRequest } from "../../Action/api";
import { useNavigate, useLocation } from "react-router-dom";
import googleLogo from "../../material/img/google-logo.png";
import fbLogo from "../../material/img/fb-logo.png";
import { GOOGLE_AUTH_URL, FACEBOOK_AUTH_URL } from "../../constants/index.js";
const LoginPage = () => {
  const loginEmail = useRef("");
  const loginPassword = useRef("");
  const forgotPasswordEmail = useRef("");
  const [showForgotPasswordModal, setShowForgotPasswordModal] = useState(false);
  const [forgotPasswordMessage, setForgotPasswordMessage] = useState("");
  const [loginMessage, setLoginMessage] = useState("");
  const localtion = useLocation();
  const queryParam = new URLSearchParams(localtion.search);
  const tokenInvalid = queryParam.get("tokenInvalid");
  const navigate = useNavigate();

  useEffect(() => {
    if (tokenInvalid) {
      alert(
        "Token is invalid Please fill your email to get a password reset link"
      );
    }
  }, [tokenInvalid]);

  const submitForgotPasswordReset = async () => {
    const reqBody = {
      email: forgotPasswordEmail.current.value,
    };

    await SendForgotPasswordRequest(JSON.stringify(reqBody))
      .then((response) => {
        setShowForgotPasswordModal(false);
      })
      .catch((error) => {
        console.log(error);
        if (error.response) {
          setForgotPasswordMessage(error.response.data);
        }
      });
  };

  const submitLogin = async (event) => {
    console.log("Handle Spring Login");

    event.preventDefault();
    const reqBody = {
      email: loginEmail.current.value,
      password: loginPassword.current.value,
    };

    await SendLoginRequest(JSON.stringify(reqBody))
      .then((response) => {
        navigate("/profile");
      })
      .catch((error) => {
        if (error.response) {
          setLoginMessage(error.response.data);
        }
      });
  };

  const handleGoogleLogin = (event) => {
    console.log("Handle Google Login");
    event.preventDefault();
    window.location.href = GOOGLE_AUTH_URL;
  };

  const handleFacebookLogin = (event) => {
    console.log("Handle Facebook Login");
    event.preventDefault();
    window.location.href = FACEBOOK_AUTH_URL;
  };

  const handleChangeValue = (event) => {
    event.preventDefault();
    setLoginMessage("");
  };

  return (
    <Container style={{ height: "100vh" }}>
      <Row
        className="justify-content-center align-items-center"
        style={{ height: "100%" }}
      >
        <Col xs={6}>
          <Form>
            <Form.Label>Login</Form.Label>
            <Button
              onClick={handleGoogleLogin}
              variant="light"
              style={{
                border: "1px #dee2e6 solid",
                margin: "10px 0px",
                width: "100%",
              }}
            >
              <Image
                src={googleLogo}
                alt="Button Image"
                style={{ width: "30px", height: "30px" }}
              />
              Login with google
            </Button>

            <Button
              onClick={handleFacebookLogin}
              variant="light"
              style={{
                border: "1px #dee2e6 solid",
                margin: "10px 0px",
                width: "100%",
              }}
            >
              <Image
                src={fbLogo}
                alt="Button Image"
                style={{ width: "30px", height: "30px" }}
              />
              Login with Facebook
            </Button>
            <Form.Group controlId="formGroupEmail">
              <Form.Control
                type="email"
                placeholder="email"
                ref={loginEmail}
                onChange={handleChangeValue}
              />
            </Form.Group>

            <Form.Group controlId="formGroupPassword">
              <Form.Control
                type="password"
                placeholder="password"
                ref={loginPassword}
                onChange={handleChangeValue}
              />
              <Form.Text className="text-danger">{loginMessage}</Form.Text>
            </Form.Group>

            <div className="d-grid gap-2">
              <Button
                variant="primary"
                type="button"
                onClick={submitLogin}
                style={{ marginTop: "10px" }}
              >
                Submit
              </Button>
            </div>
          </Form>
          <div className="d-grid gap-2">
            <Button
              variant="primary"
              type="button"
              onClick={() => {
                setShowForgotPasswordModal(true);
              }}
              style={{ margin: "10px  0px" }}
            >
              Forgot Password
            </Button>
          </div>
          {"Don't have an account ?   "}
          <Navbar.Brand href="/" style={{ color: "blue" }}>
            Sign up
          </Navbar.Brand>
        </Col>
      </Row>
      <EmailProviderModal
        show={showForgotPasswordModal}
        onHide={() => {
          setForgotPasswordMessage("");
          setShowForgotPasswordModal(false);
        }}
        modal_title={"Forgot password ?"}
        response_message={forgotPasswordMessage}
        reference={forgotPasswordEmail}
        submit={submitForgotPasswordReset}
      ></EmailProviderModal>
    </Container>
  );
};

export default LoginPage;
