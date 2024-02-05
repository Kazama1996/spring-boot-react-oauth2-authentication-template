import Button from "react-bootstrap/Button";
import VerticleModal from "../../component/EmailProviderModal";
import { useState } from "react";
import { Container, Row, Col } from "react-bootstrap";
import { GetUserProfile, Logout } from "../../Action/api";
import { useNavigate } from "react-router-dom";

const ProfilePage = () => {
  const [modalShow, setModalShow] = useState(false);
  const navigate = useNavigate();

  const handleSayMyName = async () => [
    await GetUserProfile()
      .then((response) => {
        alert("Welcome Welcome : " + response.data);
        console.log(response);
      })
      .catch((error) => {
        alert(error.response.data);
        navigate("/login");
      }),
  ];

  const handleLogout = async () => {
    await Logout().then((response) => {
      alert(response.data);
      navigate("/login");
    });
  };

  return (
    <>
      <Container style={{ height: "100vh" }}>
        <Row
          className="justify-content-center align-items-center"
          style={{ height: "100%" }}
        >
          <Col xs={6}>
            <h1>Main Page</h1>

            <div className="d-grid gap-2">
              <Button onClick={handleSayMyName} style={{ marginTop: "10px" }}>
                Say my name !
              </Button>
            </div>
            <div className="d-grid gap-2">
              <Button onClick={handleLogout} style={{ marginTop: "10px" }}>
                Logout
              </Button>
            </div>
          </Col>
        </Row>
      </Container>
    </>
  );
};

export default ProfilePage;
