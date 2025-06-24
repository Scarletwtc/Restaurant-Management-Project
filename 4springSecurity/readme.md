# Restaurant Inventory Management System

A Spring Boot web application for managing restaurant inventory, recipes, purchase orders, and waste reports, featuring robust role-based access control.

## Features

- **Role-based access control:** Chef, Supplier, and Owner roles
- **Ingredient Management:** Track ingredients, quantities, prices, and expiry dates
- **Recipe Management:** Create and manage recipes using available ingredients
- **Purchase Orders:** Place and manage orders for ingredients, with supplier approval workflow
- **Waste Management:** Track and report wasted ingredients
- **Dashboard:** Owner dashboard with analytics and reports

## User Roles

- **Chef:**  
  Manage ingredients and recipes, generate purchase orders, and report waste.
- **Supplier:**  
  View and respond to purchase orders, track order history.
- **Owner:**  
  Access all inventory, recipes, orders, waste reports, and analytics dashboard.

## Getting Started

### Prerequisites

- Java 17+
- Maven
- MariaDB/MySQL

### Setup

1. Clone the repository
2. Configure the database connection in `src/main/resources/application.properties`
3. Run the application:

   ```bash
   mvn spring-boot:run
   ```

### Default Users

- Chef: `chef` / `password`
- Supplier: `supplier` / `password`
- Owner: `owner` / `password`

## Technologies Used

- Spring Boot
- Spring Security
- Spring Data JPA
- Thymeleaf
- MariaDB/MySQL
- Bootstrap 