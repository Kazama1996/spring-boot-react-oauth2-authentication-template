import Button from "react-bootstrap/Button";
import VerticleModal from "../../component/EmailProviderModal";
import { useState } from "react";
import { Container } from "react-bootstrap";
import { GetUserProfile } from "../../Action/api";

const ProfilePage = () => {
  const [modalShow, setModalShow] = useState(false);

  const handleClick = async () => [
    await GetUserProfile()
      .then((response) => {
        alert("Welcome Welcome : " + response.data);
        console.log(response);
      })
      .catch((error) => {
        console.log(error);
      }),
  ];

  return (
    <>
      <h1>Main Page</h1>
      <Container>
        <Button onClick={handleClick}>Test</Button>
      </Container>
    </>
  );
};

export default ProfilePage;
