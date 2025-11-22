# Console E-commerce System in Java 🛍️

A lightweight, console-based application demonstrating the core logic of a simple e-commerce system. This project is built with a focus on clean object-oriented design, showcasing proper package structure and robust encapsulation.

### Core Features

-   Create and manage products with built-in validation.
-   Simulate a customer adding items to and removing items from a shopping cart.
-   Correctly manage item quantities within the cart.
-   Calculate shopping cart totals.
-   Place an order, which generates a text-based summary.

### Technical Design

-   **Package-Based Architecture**: Code is logically organized into `com.ecommerce` and `com.ecommerce.orders` packages to promote a clear separation of concerns.
-   **Strong Encapsulation**: Utilizes `private` fields and `public` methods to protect data integrity and enforce business rules (e.g., a product's price cannot be negative).
-   **Clean & Readable Code**: Written with a focus on maintainability, clarity, and standard Java conventions.

---

### Getting Started

**Prerequisites:**
-   A Java Development Kit (JDK) must be installed on your system.

**Running the Application:**

1.  **Clone the repository:**
    ```sh
    git clone <your-repository-url>
    ```

2.  **Navigate to the source directory:**
    ```sh
    cd path/to/project/src
    ```

3.  **Compile the Java files:**
    ```sh
    javac com/ecommerce/Product.java com/ecommerce/Customer.java com/ecommerce/orders/Order.java Main.java
    ```

4.  **Run the main application:**
    ```sh
    java Main
    ```
    You will see the full demonstration of the e-commerce system printed to your console.

---

### Project Structure

```
src/
├── com/
│   └── ecommerce/
│       ├── orders/
│       │   └── Order.java
│       ├── Customer.java
│       └── Product.java
└── Main.java
```
