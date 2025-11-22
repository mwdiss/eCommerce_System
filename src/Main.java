import com.ecommerce.Customer;
import com.ecommerce.Product;
import com.ecommerce.orders.Order;
import java.util.List;

/**
* main app to demonstrate the e-commerce system.
* follows procedural steps from assignment instructions.
* @author Malith Dissanayake
*/
public class Main {
    public static void main(String[] args) {
        System.out.println("\n--------Starting E-commerce System Demo--------\n");

        //--- step: demonstrate input validation ---
        System.out.println("1. Demonstrating Input Validation:");
        try {
            new Product("P101", "Valid Product", -50.0); //invalid price
        } catch (IllegalArgumentException e) {
            System.out.println("   Caught expected error: " + e.getMessage());
        }
        try {
            new Product("", "Invalid Product", 50.0); //invalid id
        } catch (IllegalArgumentException e) {
            System.out.println("   Caught expected error: " + e.getMessage());
        }
        System.out.println();

        //--- step 5a: create product and customer instances ---
        Product laptop = new Product("P001", "Laptop Pro", 1299.99);
        Product mouse = new Product("P002", "Wireless Mouse", 35.50);
        Product mug = new Product("P003", "Java Mug", 15.00);
        List<Product> availableProducts = List.of(laptop, mouse, mug);

        //--- step 5c: display product info ---
        System.out.println("2. Available Products:");
        availableProducts.forEach(product -> System.out.println("   " + product));
        System.out.println();
        
        Customer customer = new Customer("Malith Dissanayake");
        System.out.println("3. Created Customer: " + customer.getName() + " (ID: " + customer.getCustomerID() + ")\n");

        //--- step 5b: customer adds items to cart ---
        System.out.println("4. Simulating Cart Actions:");
        customer.addToCart(laptop);
        customer.addToCart(mug);
        customer.addToCart(mug); //add second mug
        System.out.println("   - Added " + laptop.getName());
        System.out.println("   - Added " + mug.getName() + " (x2)\n");

        //--- step 5c: display cart details ---
        System.out.println("5. Current Shopping Cart:");
        customer.getShoppingCart().forEach((p, q) -> System.out.println("   - " + p.getName() + " | Quantity: " + q));
        System.out.printf("   Cart Total: $%.2f\n\n", customer.calculateTotal());

        //--- step 5b: customer places order ---
        try {
            System.out.println("6. Placing Order...");
            Order order = customer.placeOrder();
            System.out.println("   Order placed successfully!\n");
            //step 5c: display order info
            System.out.println("7. Final Order Summary:");
            System.out.println(order.generateOrderSummary());
        } catch (IllegalStateException e) {
            System.out.println("   Error placing order: " + e.getMessage());
        }

        //--- final verification ---
        System.out.println("8. Verifying cart is empty post-order...");
        if (customer.getShoppingCart().isEmpty()) System.out.println("   Cart is now empty. Correct.");
        else System.out.println("   Cart is not empty. Error.");

        System.out.println("\n--------------- Demo Complete ---------------");
    }
}