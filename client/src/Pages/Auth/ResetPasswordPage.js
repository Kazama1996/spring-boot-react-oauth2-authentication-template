import { useRef, useState } from "react";
import { Container, Col, Row, Form, Button } from "react-bootstrap";
import { useNavigate, useSearchParams } from "react-router-dom";
import { SendUpdatePasswordRequest } from "../../Action/api";

const ResetPasswordPage = () => {
  const password = useRef("");
  const passwordConfirm = useRef("");
  const [errorMessage, setErrorMessage] = useState("");

  const [searchParams] = useSearchParams();

  const navigate = useNavigate();

  const submitUpdatePassword = async (event) => {
    event.preventDefault();
    const reqBody = {
      newPassword: password.current.value,
      passwordResetToken: searchParams.get("token"),
    };
    if (password.current.value === passwordConfirm.current.value) {
      await SendUpdatePasswordRequest(reqBody)
        .then((response) => {
          alert(response.data);
        })
        .catch((error) => {
          alert(error.response.data);
        });
      navigate("/login");
    } else {
      setErrorMessage("Password and confirm password does not match !");
    }
  };

  return (
    <Container style={{ height: "100vh" }}>
      <Row
        className="justify-content-center align-items-center"
        style={{ height: "100%" }}
      >
        <Form>
          <Form.Label>Reset Your Password</Form.Label>
          <Form.Group controlId="formGroupPassword">
            <Form.Control
              type="password"
              placeholder="Password"
              ref={password}
            />
          </Form.Group>
          <Form.Group controlId="formGroupPasswordConfirm">
            <Form.Control
              type="password"
              placeholder="Password Confirm"
              ref={passwordConfirm}
            />
          </Form.Group>
          <Form.Text className="text-danger">{errorMessage}</Form.Text>

          <div className="d-grid gap-2">
            <Button
              variant="primary"
              type="submit"
              onClick={submitUpdatePassword}
              style={{ marginTop: "10px" }}
            >
              Submit
            </Button>
          </div>
        </Form>
      </Row>
    </Container>
  );
};

export default ResetPasswordPage;
