import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import Form from "react-bootstrap/Form";
import { useRef, useState } from "react";

const EmailProviderModal = (props) => {
  return (
    <Modal
      {...props}
      size="lg"
      aria-labelledby="contained-modal-title-vcenter"
      centered
      onHide={props.onHide}
    >
      <Modal.Header closeButton>
        <Modal.Title id="contained-modal-title-vcenter">
          {props.modal_title}
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
          <Form.Group controlId="formBasicEmail">
            <Form.Label>
              Please provide your email to get a password reset link!
            </Form.Label>
            <Form.Control
              type="email"
              placeholder="email"
              ref={props.reference}
            />
          </Form.Group>
          {/* Display the error message conditionally */}
          <p className="text-danger">{props.response_message}</p>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button onClick={props.submit}>Submit</Button>
      </Modal.Footer>
    </Modal>
  );
};

export default EmailProviderModal;
