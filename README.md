# **VeterinarySystem**

## **Overview**
**VeterinarySystem** is a web application designed to efficiently manage veterinary clinics. It enables users to handle appointments, clients, pet medical records, and automate administrative processes. The system includes API integrations, online payment processing, and an optimized user interface for an enhanced experience.

---

## **Project Structure**
This project follows a **Full Stack architecture**, utilizing modern technologies to ensure scalability, security, and high performance.

### **Backend**
- **Language:** Java (Spring Boot)
- **Architecture:** Microservices
- **Databases:** PostgreSQL | MongoDB
- **Security:** JWT Authentication
- **API Management:** OpenAPI + Swagger

### **Frontend**
- **Framework:** React.js + Redux
- **Styling:** TailwindCSS / Material UI
- **Integrations:** Google Maps API, PayPal API

### **Infrastructure & DevOps**
- **Containers:** Docker
- **Orchestration:** Kubernetes (GKE)
- **CI/CD:** GitHub Actions
- **Cloud Deployment:** Google Cloud Run

---

## **Installation & Setup**
Before starting, make sure you have the following installed:

1. **[Node.js](https://nodejs.org/)** (for the frontend)
2. **[Java 17+](https://openjdk.org/)**
3. **[Docker](https://www.docker.com/)** (for containers)
4. **[PostgreSQL](https://www.postgresql.org/)** or use the Docker container

### **Installation Steps**
Clone the repository and navigate to the project folder:

```sh
git clone https://github.com/JoakinGC/System-Paw-care.git
cd .\System-Paw-care\
```

### **Start the Backend**
```sh
./mvnw clean install
./mvnw spring-boot:run
```

### **Start the Frontend**
```sh
cd frontend
npm install
npm start
```

The application will be available at **http://localhost:3000**.

---

## **Deploying with Docker**
### **Run the Application in Containers**
To start the entire application with Docker, run:

```sh
docker-compose up -d
```

To stop the containers:

```sh
docker-compose down
```

### **Deploy to Google Cloud Run**
Authenticate with Google Cloud:
```sh
gcloud auth login
```

Build and push the Docker image:
```sh
docker build -t gcr.io/my-project/veterinary-system .
docker push gcr.io/my-project/veterinary-system
```

Deploy to Cloud Run:
```sh
gcloud run deploy veterinary-system \
  --image=gcr.io/my-project/veterinary-system \
  --platform=managed --region=us-central1 \
  --allow-unauthenticated
```

---

## **Testing & Code Quality**
### **Unit and Integration Tests**
To run backend tests with **JUnit and Mockito**:
```sh
./mvnw test
```

For frontend tests with **Jest**:
```sh
cd frontend
npm test
```

### **Code Quality Analysis with SonarQube**
Run code quality analysis:
```sh
docker-compose -f sonar.yml up -d
./mvnw sonar:sonar
```

---

## **Key Features**
✔️ Real-time appointment management  
✔️ Secure authentication system with JWT  
✔️ Role-based access (Admin, Veterinarian, Client)  
✔️ Payment gateway integration (PayPal)  
✔️ Dashboard with metrics and analytics  

