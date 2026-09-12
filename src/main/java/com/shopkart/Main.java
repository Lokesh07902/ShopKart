package com.shopkart;

import com.shopkart.algorithms.KnapsackSolver;
import com.shopkart.algorithms.SearchAlgorithms;
import com.shopkart.algorithms.SortingAlgorithms;
import com.shopkart.concurrency.StockUpdateDemo;
import com.shopkart.exception.OutOfStockException;
import com.shopkart.model.*;
import com.shopkart.payment.CardPayment;
import com.shopkart.payment.PaymentMethod;
import com.shopkart.repository.ProductRepository;
import com.shopkart.structures.*;
import com.shopkart.util.CatalogLoader;

import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=========== ShopKart Console Demo ===========\n");

        
        List<Product> catalog = CatalogLoader.loadFromCsv("data/products.csv");
        ProductRepository productRepo = new ProductRepository();
        catalog.forEach(productRepo::add);
        System.out.println("Loaded " + productRepo.size() + " products from CSV.");
        System.out.println("Categories (HashSet, deduplicated): " + productRepo.getAllCategories());

        System.out.println("\n--- Session 3 vs 10: Linear search vs HashMap lookup ---");
        Product foundLinear = SearchAlgorithms.linearSearch(catalog, "P005");
        Product foundHash = productRepo.findById("P005").orElse(null);
        System.out.println("Linear search found: " + foundLinear);
        System.out.println("HashMap lookup found: " + foundHash + " (same result, O(1) instead of O(n))");

        // ----------  Polymorphism ----------
        System.out.println("\n--- Session 5: Customer polymorphism ---");
        Customer regular = new RegularCustomer("C001", "Rohit", "rohit@example.com");
        Customer premium = new PremiumCustomer("C002", "Anjali", "anjali@example.com");
        System.out.printf("Regular customer discount on ₹1000: ₹%.2f%n", regular.applyDiscount(1000));
        System.out.printf("Premium customer discount on ₹1000: ₹%.2f%n", premium.applyDiscount(1000));

        // ----------  Interfaces / abstract classes ----------
        System.out.println("\n--- Session 6: Payment abstraction ---");
        PaymentMethod payment = new CardPayment("4242");
        payment.pay(1599.00);

        // ----------  Custom exceptions ----------
        System.out.println("\n--- Session 7: Custom exception handling ---");
        try {
            Product ssd = productRepo.findById("P010").orElseThrow();
            if (ssd.getStock() < 9999) { // force the exception for demo purposes
                throw new OutOfStockException(ssd.getName(), 9999, ssd.getStock());
            }
        } catch (OutOfStockException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }

        // ----------  Two-pointer & sliding window ----------
        System.out.println("\n--- Session 11: Two-pointer & sliding window ---");
        List<Product> sortedByPrice = new ArrayList<>(catalog);
        sortedByPrice.sort((a, b) -> Double.compare(a.getPrice(), b.getPrice()));
        productRepo.findPairWithExactTotal(sortedByPrice, 1798.00)
                .ifPresentOrElse(
                        pair -> System.out.println("Pair totalling ₹1798: " + pair[0].getName() + " + " + pair[1].getName()),
                        () -> System.out.println("No exact pair found for ₹1798"));
        System.out.println("Cheapest bundle of 3: " + productRepo.cheapestBundle(sortedByPrice, 3));

        // ----------  Linked lists ----------
        System.out.println("\n--- Session 12: Doubly linked list (order history) ---");
        OrderHistory history = new OrderHistory();
        Order o1 = new Order("ORD1", regular, false);
        Order o2 = new Order("ORD2", premium, true);
        history.addOrder(o1);
        history.addOrder(o2);
        history.printForward();
        history.printBackward();

        System.out.println("\n--- Session 13: Circular linked list (featured carousel) ---");
        FeaturedCarousel carousel = new FeaturedCarousel();
        catalog.subList(0, 3).forEach(carousel::addProduct);
        for (int i = 0; i < 5; i++) System.out.println("Carousel slot " + i + ": " + carousel.next().getName());

        // ----------  Stack & Queue ----------
        System.out.println("\n--- Session 14: Stack (cart undo) ---");
        CartActionStack cartStack = new CartActionStack();
        cartStack.push(CartActionStack.ActionType.ADD, catalog.get(0));
        System.out.println("Undo returns opposite action: " + cartStack.undo());

        System.out.println("\n--- Session 15: Queue (order processing) ---");
        OrderQueue orderQueue = new OrderQueue();
        orderQueue.enqueue(o1); // regular
        orderQueue.enqueue(o2); // express — jumps the line
        System.out.println("Processed first (should be express ORD2): " + orderQueue.processNext().getOrderId());

        // ---------- SESSION 16/17: Trees ----------
        System.out.println("\n--- Session 16: Category tree traversals ---");
        CategoryTree tree = new CategoryTree("Electronics");
        tree.insertLeft("Electronics", "Phones");
        tree.insertRight("Electronics", "Laptops");
        tree.insertLeft("Phones", "Accessories");
        System.out.println("Inorder: " + tree.inorder());
        System.out.println("Preorder: " + tree.preorder());

        System.out.println("\n--- Session 17: BST range query ---");
        ProductBST bst = new ProductBST();
        catalog.forEach(bst::insert);
        System.out.println("Products priced ₹500-₹1500: " + bst.rangeQuery(500, 1500));

        // ---------- SESSION 18: Heap ----------
        System.out.println("\n--- Session 18: Best-seller max-heap ---");
        BestSellerHeap heap = new BestSellerHeap();
        catalog.get(0).incrementUnitsSold(120);
        catalog.get(4).incrementUnitsSold(300);
        catalog.get(9).incrementUnitsSold(75);
        heap.addOrUpdate(catalog.get(0));
        heap.addOrUpdate(catalog.get(4));
        heap.addOrUpdate(catalog.get(9));
        System.out.println("Top 2 sellers: " + heap.topN(2));

        // ---------- SESSION 19/20: Graph ----------
        System.out.println("\n--- Session 19-20: Product graph, BFS recommendation ---");
        ProductGraph graph = new ProductGraph();
        graph.addEdge(catalog.get(0), catalog.get(1));
        graph.addEdge(catalog.get(1), catalog.get(2));
        System.out.println("Recommended (BFS from " + catalog.get(0).getName() + "): "
                + graph.recommendBFS(catalog.get(0), 2));

        // ---------- SESSION 21/22: Sorting ----------
        System.out.println("\n--- Session 21-22: Sorting algorithms ---");
        Product[] toSort = catalog.toArray(new Product[0]);
        SortingAlgorithms.quickSortByPrice(toSort, 0, toSort.length - 1);
        System.out.println("Cheapest after Quick Sort: " + toSort[0].getName());
        List<Product> byRating = SortingAlgorithms.countingSortByRating(catalog);
        System.out.println("Top rated (Counting Sort): " + byRating.get(0));

        // ---------- SESSION 23: Binary search ----------
        System.out.println("\n--- Session 23: Binary search / lower bound ---");
        int idx = SearchAlgorithms.lowerBound(sortedByPrice, 1000);
        System.out.println("First product priced >= ₹1000: " + sortedByPrice.get(idx).getName());

        // ---------- SESSION 24/25: DP ----------
        System.out.println("\n--- Session 25: Knapsack — best value cart under ₹2000 ---");
        KnapsackSolver knapsack = new KnapsackSolver();
        System.out.println("Chosen: " + knapsack.bestValueCart(catalog, 2000));

        // ---------- SESSION 26: Multithreading ----------
        System.out.println("\n--- Session 26: Race condition demo ---");
        Product raceProduct1 = new Product("RACE1", "Race Test Item", 10, "Test", 100, 4.0);
        StockUpdateDemo.runRaceConditionDemo(raceProduct1, false); // unsafe — may not equal 90
        Product raceProduct2 = new Product("RACE2", "Race Test Item", 10, "Test", 100, 4.0);
        StockUpdateDemo.runRaceConditionDemo(raceProduct2, true);  // safe — always 90

        // ---------- SESSION 27: Streams ----------
        System.out.println("\n--- Session 27: Stream API filter ---");
        System.out.println("Electronics under ₹1000: " + productRepo.filterByCategory("Electronics")
                .stream().filter(p -> p.getPrice() < 1000).toList());

        System.out.println("\n=========== Demo complete ===========");
    }
}
