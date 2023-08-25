import { useRef, useState } from "react";
import { Container, Row, Col, Form, Button, Navbar } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { SendRegisterRequest } from "../../Action/api";

const RegisterPage = () => {
  const registerEmail = useRef("");
  const registerPassword = useRef("");
  const registerProfileName = useRef("");
  const registerFullName = useRef("");
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();

  const submitRegister = async (event) => {
    event.preventDefault();
    const reqBody = {
      email: registerEmail.current.value,
      profileName: registerProfileName.current.value,
      fullName: registerFullName.current.value,
      password: registerPassword.current.value,
    };
    console.log(reqBody);
    await SendRegisterRequest(JSON.stringify(reqBody))
      .then((response) => {
        navigate("/main");
      })
      .catch((error) => {
        console.log(error);
        if (error.response) {
          setErrorMessage(error.response.data);
        }
        // show error message at buttom.
      });
  };

  return (
    <Container style={{ height: "100vh" }}>
      <Row
        className="justify-content-center align-items-center"
        style={{ height: "100%" }}
      >
        <Col xs={6}>
          <Form>
            <Form.Group controlId="formBasicEmail">
              <Form.Label>Create Account</Form.Label>
              <Form.Control
                type="email"
                placeholder="email"
                ref={registerEmail}
                style={{ marginBottom: "10px" }}
              />
              <Form.Control
                type="text"
                placeholder="Full Name"
                ref={registerFullName}
                style={{ marginBottom: "10px" }}
              />

              <Form.Control
                type="text"
                placeholder="Profile Name"
                ref={registerProfileName}
                style={{ marginBottom: "10px" }}
              />
              <Form.Control
                type="password"
                placeholder="password"
                ref={registerPassword}
                style={{ marginBottom: "10px" }}
              />
              <Form.Text className="text-danger">{errorMessage}</Form.Text>
            </Form.Group>
            <Button variant="primary" type="submit" onClick={submitRegister}>
              Submit
            </Button>
          </Form>

          {"Have Account? Login   "}
          <Navbar.Brand href="/login" style={{ color: "blue" }}>
            here
          </Navbar.Brand>
          <div style={{ marginTop: "10px" }}>
            {/* <Button
              variant="primary"
              // type="submit"
              onClick={() => {
                setShowForgotPasswordModal(true);
              }}
            >
              Forgot Password
            </Button> */}
          </div>
        </Col>
      </Row>
    </Container>
  );
};

export default RegisterPage;
