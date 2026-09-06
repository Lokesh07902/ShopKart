# ShopKart — E-Commerce Backend (Java + DSA Capstone Project)

A backend system for browsing, searching, and ordering products, where **every core Java, DSA,
and backend concept from the course has a real, working feature built on top of it** — not toy
examples, but pieces of one coherent system.

Two ways to run it:
1. **Console demo** (`Main.java`) — plain Java, no dependencies, runs with `javac`/`java`.
   Exercises every data structure end-to-end so you can see (and explain) each one working.
2. **REST API** (`ShopKartApplication.java`) — the same logic exposed over HTTP using Spring
   Boot, with data persisted via JDBC.

---

## How to run it

### Option A — Console demo (fastest, no setup)
```bash
cd shopkart
javac -d out $(find src/main/java -path "*/web/*" -prune -o -name "ShopKartApplication.java" -prune -o -name "*.java" -print)
java -cp out com.shopkart.Main
```
> This compiles everything **except** the Spring Boot pieces (`web/` folder and
> `ShopKartApplication.java`), which need Maven-managed dependencies to compile.

### Option B — Full REST API (needs Maven + internet for the first build)
```bash
cd shopkart
mvn spring-boot:run
```
Then try in your browser or Postman:
- `GET  http://localhost:8080/api/products`
- `GET  http://localhost:8080/api/products?category=Electronics`
- `POST http://localhost:8080/api/cart/add/P001`
- `GET  http://localhost:8080/api/orders/best-value?budget=2000`

### Option C — Frontend UI (talks to the running REST API)
1. First start the backend: `mvn spring-boot:run` (must stay running in its own terminal).
2. Open the `frontend/` folder in VS Code, right-click `index.html` → **"Open with Live Server"**
   (install the **Live Server** extension by Ritwick Dey if you don't have it).
   > Opening `index.html` directly by double-clicking (a `file://` URL) will NOT work — the
   > browser blocks API calls from `file://` origins. Live Server serves it over `http://` instead.
3. The page loads live product data, lets you filter by category, search, add to cart, undo,
   and run the best-value (knapsack) budget finder — all backed by your real Java data structures.

### VS Code setup
1. Open the `shopkart/` folder in VS Code.
2. Install the **Extension Pack for Java** and **Spring Boot Extension Pack** (both by Microsoft/VMware — VS Code will suggest them automatically when it sees `pom.xml`).
3. For the console demo: open `Main.java` → click **Run** above `main()`.
4. For the REST API: open `ShopKartApplication.java` → click **Run**, or use the Maven side panel.

---

## Project structure
```
shopkart/
├── pom.xml                     Maven build file (Spring Boot + H2 dependencies)
├── data/
│   └── products.csv            Seed data, loaded via Session 7 file I/O
├── frontend/
│   ├── index.html               Single-page UI: catalog, cart, best-value finder
│   ├── css/style.css            Bazaar-inspired design system (see below)
│   └── js/
│       ├── api.js               fetch() wrapper for every backend endpoint
│       └── app.js               DOM rendering + event handling
├── src/main/java/com/shopkart/
│   ├── Main.java                    Plain console demo — runs every module
│   ├── ShopKartApplication.java     Spring Boot entry point (Session 30)
│   ├── model/                       Entities: Product, Customer, Order...
│   ├── exception/                   Custom exceptions (Session 7)
│   ├── payment/                     Interfaces & abstract classes (Session 6)
│   ├── repository/                  Generic Collections-based storage (Sessions 8-11)
│   ├── structures/                  Hand-built DSA: lists, stacks, queues, trees, heap, graph
│   ├── algorithms/                  Searching, sorting, DP (Sessions 3, 21-25)
│   ├── concurrency/                 Thread synchronization demo (Session 26)
│   ├── db/                          JDBC persistence (Session 29)
│   └── web/                         Spring Boot REST controllers (Session 30)
```

---

## Where each concept lives, and WHY that data structure was chosen

| Concept | File | Why this structure, specifically |
|---|---|---|
| **OOP basics** | `model/Product.java` | Private fields + getters/setters = encapsulation. Every other structure in the project stores objects of this one class. |
| **Inheritance & Polymorphism** | `model/Customer.java`, `RegularCustomer`, `PremiumCustomer` | `getDiscountRate()` is overridden differently per subclass — the same call resolves to different behaviour at runtime. |
| **Interface vs Abstract class** | `payment/Discountable.java` vs `payment/PaymentMethod.java` | Interface = pure contract, no shared state (`Discountable`). Abstract class = shared behaviour + some abstract methods (`PaymentMethod`, all payment types share a `receipt()` format but differ in `pay()`). |
| **Custom Exceptions** | `exception/OutOfStockException.java` | Unchecked exception — running out of stock is an expected business condition, not a fatal bug, so the caller chooses whether to catch it. |
| **Generics** | `repository/Repository.java` | One class (`Repository<T>`) serves both `Product` and `Customer` storage without duplicating code. |
| **HashMap (Hashing)** | `repository/Repository.java` (`byId` field) | O(1) lookup by ID, vs O(n) linear scan — the direct fix for the Session 3 baseline. |
| **HashSet** | `repository/ProductRepository.java` (`getAllCategories`) | Automatic de-duplication — categories should never repeat. |
| **Two-Pointer / Sliding Window** | `repository/ProductRepository.java` | O(n) instead of O(n²): two-pointer for "pair summing to exact budget," sliding window for "cheapest k consecutive items" — both require the list to be pre-sorted by price. |
| **Doubly Linked List** | `structures/OrderHistory.java` | Needs O(1) insert-at-tail AND traversal in both directions (view oldest→newest or "my last order" newest-first) — a singly linked list can't go backward, an ArrayList shifts elements on insert. |
| **Circular Linked List** | `structures/FeaturedCarousel.java` | Featured-products banner loops forever — the tail's `next` points back to `head`, so advancing never needs a manual wrap-around check. |
| **Stack** | `structures/CartActionStack.java` | Undo is inherently LIFO — the most recent cart action is the first one reversed. |
| **Queue / Priority Queue** | `structures/OrderQueue.java` | Regular orders: FIFO (fairness = arrival order). Express orders: min-heap-backed `PriorityQueue` so they always jump ahead of regular orders. |
| **Binary Tree + Traversals** | `structures/CategoryTree.java` | Represents a (binary-simplified) category hierarchy; Inorder/Preorder/Postorder each visit it in a different useful order. |
| **Binary Search Tree** | `structures/ProductBST.java` | A SECOND index into the same products, keyed by price instead of ID, purpose-built to answer range queries ("everything ₹500–₹1500") that a HashMap can't do efficiently. |
| **Heap (Priority Queue)** | `structures/BestSellerHeap.java` | "Top N best sellers" needs repeated access to the current maximum — a max-heap gives O(log n) insert and O(1) peek-max without sorting the whole catalog. |
| **Heap Sort** | `structures/BestSellerHeap.java` (`heapSortByRating`) | Implemented by hand (not via `PriorityQueue`) specifically to show the build-heap-then-extract algorithm itself. |
| **Graph (Adjacency List)** | `structures/ProductGraph.java` | Models "frequently bought together." Adjacency LIST chosen over a matrix because the graph is sparse — most product pairs are never co-purchased, so a matrix would waste O(n²) space. |
| **BFS** | `structures/ProductGraph.java` (`recommendBFS`) | Explores level-by-level, so hop-distance directly maps to recommendation relevance. |
| **DFS** | `structures/ProductGraph.java` (`hasCycleDFS`) | Used as a data-integrity check — detects if the category tree accidentally contains a cycle. |
| **Merge Sort** | `algorithms/SortingAlgorithms.java` | Stable, guaranteed O(n log n) regardless of input order — good when worst-case guarantees matter. |
| **Quick Sort** | `algorithms/SortingAlgorithms.java` | In-place (no extra array like Merge Sort needs), average O(n log n), generally faster in practice. |
| **Counting Sort** | `algorithms/SortingAlgorithms.java` | Ratings are bounded to a small known range (1–5 stars) — Counting Sort beats any comparison sort here: O(n + k) instead of O(n log n). |
| **Binary Search** | `algorithms/SearchAlgorithms.java` | O(log n) exact match once sorted by price. |
| **Binary Search variant (Lower Bound)** | `algorithms/SearchAlgorithms.java` | "First product priced ≥ X" — the classic "search on the answer" pattern used in real filtering UIs. |
| **DP — Memoization** | `algorithms/KnapsackSolver.java` (`memoizedDiscount`) | Caches repeated discount calculations for identical cart totals — the core DP idea in miniature. |
| **DP — Tabulation (0/1 Knapsack)** | `algorithms/KnapsackSolver.java` (`bestValueCart`) | "Best combination of products within a budget" is textbook 0/1 Knapsack — each product picked at most once, dp table built bottom-up, then backtracked to recover which items were chosen. |
| **Multithreading & Synchronization** | `concurrency/StockUpdateDemo.java` | Deliberately shows a race condition (two threads both decrement `stock` unsafely) then fixes it with `synchronized`, proving why locking is necessary, not just stating it. |
| **Streams / Lambdas (Java 8+)** | `repository/ProductRepository.java` (`filterByCategory`), `Main.java` | Declarative filter/sort replacing manual loops. |
| **Optional & Records (Modern Java)** | `repository/Repository.java` (`findById` returns `Optional`), `model/OrderSummary.java` (a `record`) | `Optional` avoids null-pointer bugs at call sites; `record` auto-generates constructor/getters/equals/hashCode for a simple immutable DTO. |
| **File I/O** | `util/CatalogLoader.java` | Reads seed data from `data/products.csv` instead of hardcoding it in Java source. |
| **JDBC** | `db/DatabaseConnection.java`, `db/ProductDAO.java` | Uses H2 (file-based, zero server setup) via `PreparedStatement` (prevents SQL injection) — the DAO pattern separates SQL from business logic. |
| **Spring Boot REST API** | `web/ProductController.java`, `CartController.java`, `OrderController.java` | Thin HTTP layer on top of everything above — controllers call into the SAME repositories/algorithms the console demo uses; no logic is duplicated. |

---

## Sample data flow (what actually happens when you call the API)

`GET /api/orders/best-value?budget=2000`
1. `OrderController` receives the HTTP request (Spring Boot, Session 30).
2. Calls `productRepository.getAll()` — backed by the `HashMap`-indexed `Repository<T>` (Session 8-10).
3. Passes the list to `KnapsackSolver.bestValueCart()` (Session 25) — runs the 0/1 Knapsack DP table.
4. Returns the chosen `List<Product>` as JSON.

Every layer reuses the one below it — nothing in `web/` reimplements logic that already exists.

---

## Pushing this to GitHub

```bash
cd shopkart
git init
git add .
git commit -m "Initial commit: ShopKart Java + DSA capstone project"
git branch -M main
git remote add origin https://github.com/<your-username>/shopkart.git
git push -u origin main
```
(Create the empty repo on github.com first, then copy its URL into the `git remote add` command above.)

---

## Viva preparation
See **`VIVA_PREP.md`** for a full set of likely questions and model answers, organized by module.
