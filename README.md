# FlightReviewBackend

## Overview

This project is a flight review management and consultation application. The backend is developed in Java using Spring Boot, while the frontend uses Angular. Data persistence is handled by JPA/Hibernate, and the application relies on a PostgreSQL relational database for robustness and compatibility with Spring Boot.

## How to Launch

To start the backend, run the `FlightReviewApiApplication` class.  
You must also initialize a PostgreSQL database named `flightReview` and execute the `create_table.sql` script within it to set up the schema.

## Database Design and Justifications

- **PostgreSQL** is chosen for its robustness, reliability, and seamless integration with Spring Boot.
- **JPA/Hibernate** is used for data persistence, simplifying database interactions and reducing boilerplate code.

### Main Entities

- **Flight**: Represents a flight.
- **Review**: A review left by a user for a flight.
- **Response**: An administrator's response to a review.
- **DbUser**: An application user.

### Entity Relationships

- A **Flight** has multiple **Reviews** (`@OneToMany`).  
 This models the real-world scenario where a single flight can receive many reviews.
- A **Review** belongs to one **Flight** and one **DbUser** (`@ManyToOne`).  
Each review is linked to a specific flight and user.
- A **Review** can have a **Response** (`@OneToOne` or `@OneToMany` depending on requirements).  
- A **DbUser** can have multiple **Reviews** (`@OneToMany`).  
A user can write several reviews.

#### Why a Flight Table?

A dedicated `Flight` table was created to automate flight selection for the user. When a user selects their flight reference, the corresponding date and airline are automatically retrieved and filled in, eliminating the need for manual entry. This improves user experience.

#### Use of Enums

Enums are used for certain fields (e.g., airlines, user roles) to restrict values to a predefined set, simplifying validation.

#### JPA/Hibernate Annotations

Annotations like `@Entity`, `@Id`, `@OneToMany`, `@ManyToOne`, etc., are used to define entity relationships and map Java objects to database tables.

#### JSON Serialization

- `@JsonIgnore` is used in the `Flight` entity to prevent infinite recursion during JSON serialization, especially in bidirectional relationships.
- `@JsonManagedReference` and `@JsonBackReference` are used to manage parent/child relationships in JSON output, ensuring correct serialization and avoiding circular references.

## Separation of Concerns (MVC Architecture)

- **Controllers**: Expose REST endpoints (API layer).
- **Services**: Contain business logic.
- **Repositories**: JPA interfaces for data access.

Thanks to JPA, most database queries are handled automatically, reducing the need for manual SQL and improving maintainability.  
This architecture follows the separation of concerns principle, making the codebase easier to maintain and evolve.

## Review Search and Pagination

The `ReviewSearchParameter` class is used to filter reviews on the backend.  
On the frontend, a pagination component is used so that only the reviews for the current page are loaded, rather than all reviews at once.  
Filtering must therefore be performed on the backend; otherwise, filtering on the frontend would only affect the current page's data.

A custom repository implementation is used to handle complex filtering logic.  
In this implementation, I build dynamic queries based on the search parameters to efficiently filter reviews in the database.

## Review Reference Generation

Review references are generated in the backend by a service that retrieves all existing references and creates a new one by incrementing the highest value.  
This ensures uniqueness of review references.

## Use of @Transactional

The `@Transactional` annotation is used when modifying two entities (e.g., `Response` and `Review`) in a single operation.  
If one save operation fails after the other has succeeded, you risk data inconsistency.  
`@Transactional` ensures that both operations succeed or both are rolled back in case of failure.

## Use of @ResponseStatus(HttpStatus.NO_CONTENT)

The annotation `@ResponseStatus(HttpStatus.NO_CONTENT)` is used to indicate that a request was successful but there is no content to return (HTTP 204).  
This is appropriate for operations like DELETE or successful updates where no response body is needed.
