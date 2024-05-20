import React from 'react';
import { Translate } from 'react-jhipster';
import { Row, Col } from 'reactstrap';
import { useAppSelector } from 'app/config/store';
import CarouselServicesHome from './CarrouselServicesHome';
import './home.scss';
import './styles.css';

const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <div className="content">
      <div className="panel-home container-fluid">
        <h1 className="display-4">
          <Translate contentKey="home.title">Welcome to PawCare!</Translate>
        </h1>
        <p className="lead">
          <Translate contentKey="home.subtitle">Caring for the paws that fill our lives with joy</Translate>
        </p>
      </div>

      <div className="container-fluid">
        <CarouselServicesHome />
      </div>

      <div className="panel-secondary text-center container-fluid">
        <h2>
          <Translate contentKey="home.title-secondary">
            Take care of your best friend with PawCare!
          </Translate>
        </h2>
        <p>
          <Translate contentKey="home.subtitle-secondary">
            Discover PawCare, the comprehensive veterinary system designed to ensure the well-being of your pet.
          </Translate>
        </p>
      </div>

      {/* Services Section */}
      <div className="services-section container-fluid text-center">
        <h2>
          <Translate contentKey="home.services-title">Our Services</Translate>
        </h2>
        <Row>
          <Col md="4">
            <div className="service-box">
              <h3><Translate contentKey="home.service1-title">General Consultation</Translate></h3>
              <p><Translate contentKey="home.service1-description">Comprehensive care for your pet.</Translate></p>
            </div>
          </Col>
          <Col md="4">
            <div className="service-box">
              <h3><Translate contentKey="home.service2-title">Vaccination</Translate></h3>
              <p><Translate contentKey="home.service2-description">Keep your pet protected.</Translate></p>
            </div>
          </Col>
          <Col md="4">
            <div className="service-box">
              <h3><Translate contentKey="home.service3-title">Surgery</Translate></h3>
              <p><Translate contentKey="home.service3-description">Surgery with the best care.</Translate></p>
            </div>
          </Col>
        </Row>
      </div>

      {/* Testimonials Section */}
      <div className="testimonials-section container-fluid text-center">
        <h2>
          <Translate contentKey="home.testimonials-title">Testimonials</Translate>
        </h2>
        <p><Translate contentKey="home.testimonials-subtitle">What our happy clients say.</Translate></p>
        <Row>
          <Col md="4">
            <div className="testimonial-box">
              <p>"<Translate contentKey="home.testimonial1">The best veterinary service I have found.</Translate>"</p>
              <p>- <Translate contentKey="home.testimonial1-author">John Doe</Translate></p>
            </div>
          </Col>
          <Col md="4">
            <div className="testimonial-box">
              <p>"<Translate contentKey="home.testimonial2">My dog is happier and healthier thanks to PawCare.</Translate>"</p>
              <p>- <Translate contentKey="home.testimonial2-author">Jane Smith</Translate></p>
            </div>
          </Col>
          <Col md="4">
            <div className="testimonial-box">
              <p>"<Translate contentKey="home.testimonial3">Professional and loving with our pets.</Translate>"</p>
              <p>- <Translate contentKey="home.testimonial3-author">Michael Brown</Translate></p>
            </div>
          </Col>
        </Row>
      </div>

      {/* Contact Section */}
      <div className="contact-section container-fluid text-center">
        <h2>
          <Translate contentKey="home.contact-title">Contact</Translate>
        </h2>
        <p><Translate contentKey="home.contact-subtitle">Have a question? We're here to help!</Translate></p>
        <Row>
          <Col md="6">
            <h4><Translate contentKey="home.contact-phone-title">Phone</Translate></h4>
            <p><Translate contentKey="home.contact-phone">+1 234 567 890</Translate></p>
          </Col>
          <Col md="6">
            <h4><Translate contentKey="home.contact-email-title">Email</Translate></h4>
            <p><Translate contentKey="home.contact-email">info@pawcare.com</Translate></p>
          </Col>
        </Row>
      </div>

      {/* FAQ Section */}
      <div className="faq-section container-fluid text-center">
        <h2>
          <Translate contentKey="home.faq-title">Frequently Asked Questions</Translate>
        </h2>
        <Row>
          <Col md="6">
            <h5><Translate contentKey="home.faq1-question">What should I do in case of an emergency?</Translate></h5>
            <p><Translate contentKey="home.faq1-answer">Call our emergency number immediately.</Translate></p>
          </Col>
          <Col md="6">
            <h5><Translate contentKey="home.faq2-question">Do you offer health plans for pets?</Translate></h5>
            <p><Translate contentKey="home.faq2-answer">Yes, we have several plans tailored to your pet's needs.</Translate></p>
          </Col>
        </Row>
      </div>
    </div>
  );
};

export default Home;
