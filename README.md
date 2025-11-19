# Taiga Store 🛍️ - A Java Swing E-commerce App

[![Java](https://img.shields.io/badge/Java-17+-blue?logo=java)](https://www.oracle.com/java/technologies/downloads/)

Taiga is a clean, modern, and lightweight desktop e-commerce application built entirely with Java Swing. It showcases a well-structured design pattern and modern Java features, making it a robust and maintainable example of a desktop GUI application.

| Main Interface                                                                                            | Order & Receipt Generation                                                                                       |
| --------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------- |
| ![Taiga Store UI](https://github.com/mwdiss/eCommerce_System/blob/dev/img/ui.png?raw=true)                  | ![Demo GIF](https://github.com/mwdiss/eCommerce_System/blob/dev/img/demo.gif?raw=true)                           |

▶️ **Watch Demo on YouTube:** [:)](:))

---

### ✅ Core Functionality

-   **Browse Products**: View a list of available products with details in a sortable table.
-   **Dynamic Shopping Cart**: Add single or multiple selected products to the cart.
-   **Cart Management**: Update quantities, see running totals, and remove items.
-   **Place Orders**: Complete the checkout process with customer name entry.
-   **Generate Receipts**: View a professionally formatted, itemized receipt after placing an order.

### ✨ Technical & Architectural Highlights

This project emphasizes clean code and modern software design principles.

-   **Excellent Structure**: Follows the **Separation of Concerns** principle by organizing code into distinct packages:
    -   `com.ecommerce`: Core data models (Product, Customer).
    -   `com.ecommerce.orders`: Order processing logic.
    -   `com.ecommerce.gui`: Reusable GUI components (Table Models, Custom Renderers).
-   **Modern Java Features**:
    -   Uses **Java Records** (`Product`) for creating immutable and concise data transfer objects.
    -   Employs **Functional Programming** with Streams and Lambdas for efficient and readable data manipulation.
-   **Robust & Encapsulated Models**: The `Product` record includes a **compact constructor for validation**, ensuring data integrity (e.g., prices cannot be negative).
-   **Reusable GUI Components**: Custom `TableModel` classes and a generic `ButtonColumn` demonstrate a modular approach to building UIs.
-   **Clean and Readable Code**: Adheres to a strict style guide, with meaningful names and minimal boilerplate.

---

### 🚀 Getting Started

**Prerequisites:**
- JDK 17 or higher.

**Running from an IDE (Recommended):**

1.  Clone the repository:
    ```sh
    git clone https://github.com/mwdiss/eCommerce_System.git
    ```
2.  Open the project in your favorite IDE (IntelliJ, Eclipse, etc.).
3.  Locate and run the `Main.java` file.

> **Note**: To see the detailed order summary printed to the console upon checkout, running the application from an IDE is recommended. The pre-built JAR file does not have a visible console attached.

---

### 🛠️ Technology Stack

-   **Language**: `Java 17`
-   **Framework**: `Java Swing` for the graphical user interface.

### 📁 Project Structure

The project is organized into logical packages to ensure maintainability and separation of concerns.

```
src/
├── Main.java                 # Application entry point & UI composition
└── com/ecommerce/
    ├── Customer.java         # Customer data model
    ├── Product.java          # Product data model (Record with validation)
    ├── gui/
    │   ├── ButtonColumn.java   # Reusable JTable button component
    │   ├── CartTableModel.java # Data model for the cart view
    │   └── ProductTableModel.java# Data model for the product view
    └── orders/
        └── Order.java        # Order processing & receipt generation
```
