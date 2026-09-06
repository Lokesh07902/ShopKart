# ShopKart — Viva Preparation Guide

Practice explaining each answer OUT LOUD, pointing to the actual file. Examiners notice when you
can navigate straight to the relevant code without hunting.

---

## 1. Project overview (opening question — always asked)

**Q: What does this project do?**
> "ShopKart is a backend e-commerce system. It has products, customers, a cart, and orders. The
> point of the project is that every data structure and algorithm from the syllabus is used for
> an actual feature — not written in isolation just to prove I know it. It runs two ways: a
> plain console demo with no dependencies, and a Spring Boot REST API backed by a database."

**Q: Why did you pick e-commerce as the domain?**
> "Because it naturally needs almost every structure we studied: fast lookup (HashMap), sorted
> range queries (BST), priority handling (heap/priority queue), relationships between items
> (graph), undo (stack), fair processing (queue), and budget optimization (DP knapsack) — it
> wasn't forced, the domain genuinely needs all of them."

---

## 2. OOP questions

**Q: Where's encapsulation in your project?**
> "`Product.java` — all fields are `private`, accessed only via getters/setters. Nothing outside
> the class can put a `Product` into an invalid state directly."

**Q: Give a real example of polymorphism you used, not just the definition.**
> "`Customer` is abstract with `getDiscountRate()`. `RegularCustomer` returns 0, `PremiumCustomer`
> returns 0.10. In `CartService`/`Main`, I call `customer.applyDiscount(amount)` on a `Customer`
> reference — the actual discount used depends on which subclass the object really is at
> runtime. That's runtime polymorphism, not compile-time overloading."

**Q: Why is `PaymentMethod` an abstract class but `Discountable` an interface?**
> "`Discountable` has no shared state or default behaviour — just a method signature every
> implementer must provide. `PaymentMethod` has shared behaviour (`receipt()` builds a common
> message format) AND an abstract method (`pay()`) that must differ per subclass — that mix is
> exactly when you reach for an abstract class instead of an interface."

**Q: Why is `OutOfStockException` unchecked (extends RuntimeException) and not checked?**
> "Because running out of stock is an expected business condition the caller may or may not
> want to handle explicitly — forcing every caller to declare `throws` everywhere would be
> noisy. Checked exceptions are for conditions callers realistically must recover from, like
> file-not-found."

---

## 3. Collections & Hashing

**Q: Why HashMap instead of just searching the list?**
> "Linear search is O(n) — for every lookup you scan every product. HashMap gives O(1) average
> lookup by hashing the product ID directly to a bucket. `Repository.java` shows both: I keep a
> `List<T>` for ordered iteration AND a `Map<String,T>` for fast lookup — different structures
> for different access patterns."

**Q: What's the time complexity of HashMap operations, and when could it degrade?**
> "O(1) average case for get/put, because a good hash function spreads keys evenly across
> buckets. It degrades to O(n) worst case if many keys collide into the same bucket — Java
> mitigates this by treeifying buckets with 8+ collisions into a mini red-black tree since
> Java 8."

**Q: Why HashSet for categories?**
> "A category should never appear twice in the list of 'all categories'. `HashSet` enforces
> uniqueness automatically — adding a duplicate is a silent no-op instead of something I'd have
> to check manually."

**Q: Explain your two-pointer solution.**
> "In `findPairWithExactTotal`, the list is pre-sorted by price. I keep a `left` pointer at the
> start and `right` at the end. If the sum is too small, I move `left` right (increase sum); if
> too big, I move `right` left (decrease sum). Each comparison eliminates one candidate, so it's
> O(n) total instead of checking every pair, which would be O(n²)."

---

## 4. Linked Lists, Stacks, Queues

**Q: Why build your own doubly linked list instead of using `java.util.LinkedList`?**
> "To actually demonstrate the pointer manipulation — `prev`/`next` references, updating `head`
> and `tail` on insert. Using the built-in class would work but wouldn't show I understand the
> mechanics underneath it."

**Q: Why does order history need a DOUBLY linked list specifically?**
> "I need to walk it both directions — oldest-to-newest for a full history view, and
> newest-to-oldest for 'show my last order.' A singly linked list can only go forward; going
> backward from the tail would require O(n) — the whole point of `prev` pointers is O(1)
> backward traversal."

**Q: How does the circular linked list differ from a normal one, mechanically?**
> "The last node's `next` doesn't point to `null` — it points back to the first node. So
> `next()` never has to check 'am I at the end?' — it just always has a next node, which is
> perfect for something that should loop forever, like a featured-products carousel."

**Q: Why is a stack the right structure for undo?**
> "Undo needs Last-In-First-Out order — the most recent action must be reversed first. That's
> the definition of a stack. I push a `CartAction` (type + product) on every add/remove, and
> `undo()` pops the most recent one and returns its opposite."

**Q: Why two different queues for orders?**
> "Regular orders use a plain FIFO `Queue` — fairness means first-come-first-served. Express
> orders use a `PriorityQueue`, which is a min-heap internally, so they always get pulled out
> before any regular order regardless of when they arrived — I check the express queue first in
> `processNext()`."

---

## 5. Trees, Heaps, Graphs

**Q: Difference between your Binary Tree and BST?**
> "`CategoryTree` is a plain binary tree — I place nodes manually (`insertLeft`/`insertRight`) to
> model a hierarchy, there's no ordering rule. `ProductBST` enforces the BST property: everything
> in a node's left subtree has a smaller price, everything in the right subtree has a larger
> price — that ordering is what makes the range query `rangeQuery(min, max)` efficient."

**Q: Why do you need a BST when you already have a HashMap?**
> "They answer different questions. HashMap answers 'give me the product with THIS exact ID'
> in O(1) — no ordering info at all. BST answers 'give me everything priced between ₹500 and
** ₹1500' by pruning subtrees outside the range — O(k + log n) where k is the result size. A
> HashMap can't do a range query without scanning everything."

**Q: What happens if you insert prices in already-sorted order into your BST?**
> "It degenerates into essentially a linked list — every node only has a right child, so
> operations become O(n) instead of O(log n). That's exactly the problem AVL trees solve, by
> automatically rebalancing with rotations after every insert. I implemented the plain BST and
> documented that limitation rather than the balanced version, since AVL rotations were out of
> this module's scope."

**Q: Why a max-heap for best-sellers instead of just sorting?**
> "Sorting the whole catalog by units sold is O(n log n) every time I want the top few. A heap
> gives O(log n) insert/update and O(1) peek at the current maximum, so if `unitsSold` keeps
> changing (new sales coming in), I don't have to re-sort everything each time — Java's
> `PriorityQueue` is a min-heap by default, so I reverse the comparator to make it a max-heap."

**Q: Why adjacency list instead of adjacency matrix for the product graph?**
> "The graph is sparse — most product pairs are never bought together. A matrix would allocate
> an n×n grid, mostly zeros, wasting O(n²) space. An adjacency list only stores edges that
> actually exist, which for sparse graphs is much closer to O(n + edges)."

**Q: Why BFS for recommendations and DFS for cycle detection — could you swap them?**
> "BFS explores level by level, so the number of hops directly corresponds to how 'closely
> related' a recommendation is — that maps naturally onto relevance. DFS goes deep down one path
> before backtracking, which is exactly what you need to detect a cycle: if you reach a node
> you've already visited via a DIFFERENT path than your immediate parent, you've found a loop.
> You technically could detect a cycle with BFS too, but DFS's recursive structure makes it more
> natural to track the parent-child path."

---

## 6. Sorting & Searching

**Q: Merge Sort vs Quick Sort — why implement both?**
> "To show the tradeoff. Merge Sort is stable and guarantees O(n log n) even in the worst case,
> but needs O(n) extra space for the temporary arrays during merging. Quick Sort is in-place —
> O(log n) extra space for recursion — and faster in practice on average, but worst-case O(n²)
> if the pivot choice is consistently bad (e.g., already-sorted input with a naive pivot)."

**Q: Why Counting Sort for ratings but not for price?**
> "Counting Sort needs the values to fall into a small, known, bounded range — ratings are 1 to
> 5 stars, so I only need 6 buckets. It runs in O(n + k). Price isn't bounded like that — it
> could be any decimal value — so Counting Sort isn't applicable there; a comparison sort like
> Merge/Quick Sort is the right tool for price."

**Q: What's the difference between your `binarySearchByPrice` and `lowerBound`?**
> "`binarySearchByPrice` finds an EXACT match and returns -1 if the price isn't there.
> `lowerBound` finds the first index where price is greater-than-or-equal to the target, even if
> that exact price doesn't exist — useful for 'show me everything from ₹1000 upward,' which is
> the more common real-world query than 'find exactly ₹1000.'"

---

## 7. Dynamic Programming

**Q: Walk me through your knapsack solution.**
> "`bestValueCart` builds a 2D table `dp[i][b]` = best achievable rating-value using the first i
> products within budget b. For each product, I either skip it (`dp[i-1][b]`) or take it
> (`dp[i-1][b-cost] + rating`), and keep whichever is larger. After filling the table, I
> backtrack from `dp[n][budget]` comparing each row to the one above it: if they differ, that
> product was included, so I add it and reduce the remaining budget by its cost."

**Q: Why is this 0/1 Knapsack and not fractional?**
> "Because you can't buy half a product — each item is either fully in the cart or not at all.
> Fractional Knapsack (greedy, sort by value/weight ratio) only works when items are divisible,
> which doesn't make sense here."

**Q: What's the time and space complexity?**
> "O(n × budget) for both time and space, where n is the number of products and budget is the
> budget amount (treated as an integer). That's why it's called 'pseudo-polynomial' — it's
> efficient for reasonable budget sizes but would blow up for a very large budget value."

---

## 8. Multithreading

**Q: What race condition are you demonstrating, and why does it happen?**
> "Two threads both call `unsafeDecrement` on the same `Product`. Each reads the current stock
> (say, 5), then — because I added a small `Thread.sleep(1)` to widen the timing window — both
> threads decide the stock is still available before either writes back the decremented value.
> Both write `stock - 1 = 4`, when really two units were sold, so it should be 3. One decrement
> gets silently lost."

**Q: How does `synchronized` fix it?**
> "It makes the read-then-write sequence atomic with respect to other threads — only one thread
> can execute a `synchronized` method on the same object at a time. The next thread has to wait
> until the current one finishes reading AND writing before it can start, so no interleaving can
> happen."

---

## 9. Modern Java & Streams

**Q: Why does `findById` return `Optional<Product>` instead of `Product`?**
> "So the caller is forced to handle the 'not found' case explicitly — either via
> `.orElse(default)`, `.ifPresent(...)`, or `.orElseThrow()` — instead of getting back `null` and
> potentially causing a `NullPointerException` somewhere downstream that's hard to trace back."

**Q: What's a `record` and why use it for `OrderSummary`?**
> "A `record` is a Java 16+ feature for an immutable data carrier — it auto-generates the
> constructor, getters, `equals()`, `hashCode()`, and `toString()` from the field list, so I
> don't hand-write boilerplate for a class that's just meant to hold data, like a lightweight
> response object for the REST API."

---

## 10. Database & REST API

**Q: Why H2 instead of MySQL?**
> "H2 is a file-based database that needs zero server installation — perfect for a college
> project and a viva demo where I can't guarantee a MySQL server is running on the examiner's
> machine. The JDBC code itself (`ProductDAO`) doesn't care which database it's talking to — I
> could swap the connection URL for MySQL without changing a single query."

**Q: Why `PreparedStatement` instead of building the SQL string directly?**
> "Two reasons: it prevents SQL injection, because parameter values are sent separately from the
> query structure instead of being concatenated into it; and it lets the JDBC driver handle
> type conversion (Java `double` → SQL `DOUBLE`, etc.) correctly."

**Q: How does the Spring Boot layer relate to everything else?**
> "It's a thin HTTP wrapper. Look at `ProductController` — it has almost no logic of its own; it
> just calls `productRepository.getAll()` or `.filterByCategory()`, which is the exact same
> `ProductRepository` class the console demo uses. Spring Boot's job is only to turn an HTTP
> request into a method call and the return value into JSON — all the actual DSA work happens
> below it, unchanged."

**Q: What does `@RestController` do, in your own words?**
> "It tells Spring this class handles HTTP requests and that every method's return value should
> be serialized straight to the response body (as JSON, via Jackson) instead of being treated as
> the name of a view template to render."

---

## Likely "trick" / conceptual questions

**Q: Which single change would most improve this project's real-world readiness?**
> "Right now `ProductBST` isn't self-balancing, and errors from the REST controllers return
> generic responses instead of proper HTTP status codes (404, 400) with a global exception
> handler. Both are natural next steps — an AVL/Red-Black tree for guaranteed O(log n), and a
> `@ControllerAdvice` class to map `OutOfStockException` to a 409 Conflict response."

**Q: If your catalog had 10 million products, which structures would you reconsider?**
> "The BST would need to be self-balancing (AVL/Red-Black) or I'd move range queries to the
> database with an index instead of an in-memory tree. The knapsack DP table is O(n × budget) —
> for a huge budget range that table gets too large to hold in memory, so I'd need a different
> approach, like a greedy approximation or bounding the budget granularity."

**Q: Why not just use Spring Data JPA instead of writing JDBC by hand?**
> "JPA would be less code in a production app, but the point of this module was to actually
> understand what's happening at the SQL/connection level — `Connection`, `PreparedStatement`,
> `ResultSet` — rather than have an ORM hide it. I can explain exactly what SQL runs for every
> operation, which JPA would abstract away."
