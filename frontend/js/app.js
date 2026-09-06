// js/app.js
// Renders products, handles category filtering, search, cart, checkout,
// order history (localStorage), profile, and product detail view. Talks to
// the backend only via ShopKartAPI (see api.js).

const productGrid = document.getElementById("productGrid");
const categoryPills = document.getElementById("categoryPills");
const searchBox = document.getElementById("searchBox");
const loadingMsg = document.getElementById("loadingMsg");
const errorMsg = document.getElementById("errorMsg");
const apiUrlDisplay = document.getElementById("apiUrlDisplay");
const cartList = document.getElementById("cartList");
const cartTotal = document.getElementById("cartTotal");
const cartCount = document.getElementById("cartCount");
const undoBtn = document.getElementById("undoBtn");
const budgetInput = document.getElementById("budgetInput");
const findBtn = document.getElementById("findBtn");
const budgetResult = document.getElementById("budgetResult");

apiUrlDisplay.textContent = API_BASE_URL;

let allProducts = [];
let activeCategory = "";
let currentCartItems = [];

function formatPrice(price) {
  return `₹${Number(price).toFixed(2)}`;
}

function formatDate(d) {
  return d.toLocaleDateString("en-IN", { day: "numeric", month: "short", year: "numeric" });
}

function fallbackImage(name) {
  return `https://placehold.co/300x200/2D5A45/FFFFFF?text=${encodeURIComponent(name)}`;
}

function renderProducts(products) {
  productGrid.innerHTML = "";
  if (products.length === 0) {
    productGrid.innerHTML = `<p class="status-msg">No products match that search.</p>`;
    return;
  }

  products.forEach((product) => {
    const card = document.createElement("article");
    card.className = "product-card";

    const outOfStock = product.stock <= 0;
    const imageUrl = product.imageUrl || fallbackImage(product.name);

    card.innerHTML = `
      <img class="product-image" src="${imageUrl}" alt="${product.name}" loading="lazy">
      <div class="product-card-body">
        <span class="product-category">${product.category}</span>
        <h3 class="product-name">${product.name}</h3>
        <span class="product-meta">★ ${product.rating.toFixed(1)} · stock: ${product.stock}</span>
        <div class="product-foot">
          <span class="price-tag">${formatPrice(product.price)}</span>
          <button class="add-btn" data-id="${product.id}" ${outOfStock ? "disabled" : ""}>
            ${outOfStock ? "Sold out" : "Add"}
          </button>
        </div>
      </div>
    `;

    card.addEventListener("click", () => openProductDetail(product));
    card.querySelector(".add-btn").addEventListener("click", (e) => {
      e.stopPropagation();
      handleAddToCart(product.id, e.currentTarget);
    });

    productGrid.appendChild(card);
  });
}

function applyFilters() {
  const term = searchBox.value.trim().toLowerCase();
  const filtered = allProducts.filter((p) => {
    const matchesCategory = !activeCategory || p.category === activeCategory;
    const matchesSearch = !term || p.name.toLowerCase().includes(term);
    return matchesCategory && matchesSearch;
  });
  renderProducts(filtered);
}

async function loadCategories() {
  try {
    const categories = await ShopKartAPI.getCategories();
    categories.forEach((cat) => {
      const pill = document.createElement("button");
      pill.className = "pill";
      pill.dataset.category = cat;
      pill.textContent = cat;
      categoryPills.appendChild(pill);
    });
  } catch (err) {
    console.warn("Could not load categories:", err);
  }
}

async function loadProducts() {
  try {
    loadingMsg.hidden = false;
    errorMsg.hidden = true;
    allProducts = await ShopKartAPI.getProducts();
    applyFilters();
  } catch (err) {
    console.error(err);
    errorMsg.hidden = false;
  } finally {
    loadingMsg.hidden = true;
  }
}

async function handleAddToCart(productId, btn) {
  btn.disabled = true;
  const originalText = btn.textContent;
  btn.textContent = "Adding…";
  try {
    const cart = await ShopKartAPI.addToCart(productId);
    renderCart(cart);
    btn.textContent = "Added ✓";
    setTimeout(() => {
      btn.disabled = false;
      btn.textContent = "Add";
    }, 900);
  } catch (err) {
    btn.textContent = "Out of stock";
    console.warn(err);
  }
}

function renderCart(items) {
  currentCartItems = items;
  checkoutBtn.disabled = items.length === 0;
  cartCount.textContent = items.length;
  undoBtn.disabled = items.length === 0;

  if (items.length === 0) {
    cartList.innerHTML = `<li class="cart-empty">Cart's empty — add something from the stall.</li>`;
    cartTotal.textContent = formatPrice(0);
    return;
  }

  cartList.innerHTML = items
    .map((item) => `<li><span>${item.name}</span><span class="mono">${formatPrice(item.price)}</span></li>`)
    .join("");

  const total = items.reduce((sum, item) => sum + item.price, 0);
  cartTotal.textContent = formatPrice(total);
}

async function refreshCart() {
  try {
    const cart = await ShopKartAPI.getCart();
    renderCart(cart);
  } catch (err) {
    console.warn("Could not load cart:", err);
  }
}

async function handleUndo() {
  undoBtn.disabled = true;
  try {
    const cart = await ShopKartAPI.undoCart();
    renderCart(cart);
  } catch (err) {
    console.warn(err);
  } finally {
    undoBtn.disabled = false;
  }
}

async function handleFindBestValue() {
  const budget = Number(budgetInput.value);
  if (!budget || budget <= 0) {
    budgetResult.innerHTML = `<li class="budget-empty">Enter a budget above ₹0 first.</li>`;
    return;
  }

  findBtn.disabled = true;
  findBtn.textContent = "…";
  try {
    const items = await ShopKartAPI.bestValue(budget);
    if (items.length === 0) {
      budgetResult.innerHTML = `<li class="budget-empty">Nothing fits that budget — try a higher amount.</li>`;
    } else {
      budgetResult.innerHTML = items
        .map((item) => `<li><span>${item.name}</span><span class="mono">${formatPrice(item.price)}</span></li>`)
        .join("");
    }
  } catch (err) {
    budgetResult.innerHTML = `<li class="budget-empty">Couldn't reach the server.</li>`;
    console.warn(err);
  } finally {
    findBtn.disabled = false;
    findBtn.textContent = "Find";
  }
}

categoryPills.addEventListener("click", (e) => {
  const pill = e.target.closest(".pill");
  if (!pill) return;
  categoryPills.querySelectorAll(".pill").forEach((p) => p.classList.remove("active"));
  pill.classList.add("active");
  activeCategory = pill.dataset.category;
  applyFilters();
});

searchBox.addEventListener("input", applyFilters);
undoBtn.addEventListener("click", handleUndo);
findBtn.addEventListener("click", handleFindBestValue);
budgetInput.addEventListener("keydown", (e) => {
  if (e.key === "Enter") handleFindBestValue();
});

const detailOverlay = document.getElementById("detailOverlay");
let currentDetailProduct = null;

function generateSpecs(product) {
  const common = [
    { label: "Product ID", value: product.id },
    { label: "Category", value: product.category },
    { label: "Rating", value: `${product.rating.toFixed(1)} / 5.0` },
    { label: "Availability", value: product.stock > 0 ? `${product.stock} in stock` : "Out of stock" },
  ];

  const categorySpecs = {
    Electronics: [
      { label: "Brand", value: "ShopKart Basics" },
      { label: "Warranty", value: "1 Year" },
      { label: "Connectivity", value: "Wired / Bluetooth" },
    ],
    Mobile: [
      { label: "Brand", value: "ShopKart Mobile" },
      { label: "Battery", value: "5000 mAh" },
      { label: "Warranty", value: "1 Year" },
    ],
    Accessories: [
      { label: "Material", value: "Premium ABS / Fabric" },
      { label: "Warranty", value: "6 Months" },
    ],
    Home: [
      { label: "Material", value: "Durable Composite" },
      { label: "Warranty", value: "6 Months" },
    ],
    Stationery: [
      { label: "Material", value: "Recycled Paper" },
      { label: "Pages", value: "200" },
    ],
    Grocery: [
      { label: "Net Weight", value: "As per pack size" },
      { label: "Shelf Life", value: "6 Months" },
      { label: "FSSAI Certified", value: "Yes" },
    ],
    Beauty: [
      { label: "Skin/Hair Type", value: "All Types" },
      { label: "Cruelty-Free", value: "Yes" },
    ],
    Fashion: [
      { label: "Material", value: "Cotton Blend" },
      { label: "Care", value: "Machine Wash" },
    ],
  };

  return [...common, ...(categorySpecs[product.category] || [])];
}

function openProductDetail(product) {
  currentDetailProduct = product;
  const imageUrl = product.imageUrl || fallbackImage(product.name);

  document.getElementById("detailImage").src = imageUrl;
  document.getElementById("detailImage").alt = product.name;
  document.getElementById("detailCategory").textContent = product.category;
  document.getElementById("detailName").textContent = product.name;
  document.getElementById("detailRating").textContent = `★ ${product.rating.toFixed(1)}`;
  document.getElementById("detailStock").textContent =
    product.stock > 0 ? `${product.stock} in stock` : "Out of stock";
  document.getElementById("detailPrice").textContent = formatPrice(product.price);

  const specsList = document.getElementById("detailSpecs");
  specsList.innerHTML = generateSpecs(product)
    .map((spec) => `<li><strong>${spec.label}</strong><span>${spec.value}</span></li>`)
    .join("");

  const addBtn = document.getElementById("detailAddBtn");
  addBtn.disabled = product.stock <= 0;
  addBtn.textContent = product.stock <= 0 ? "Sold out" : "Add to Cart";

  detailOverlay.hidden = false;
}

document.getElementById("detailAddBtn").addEventListener("click", async (e) => {
  if (!currentDetailProduct) return;
  await handleAddToCart(currentDetailProduct.id, e.currentTarget);
});

document.getElementById("detailCloseBtn").addEventListener("click", () => {
  detailOverlay.hidden = true;
});
detailOverlay.addEventListener("click", (e) => {
  if (e.target === detailOverlay) detailOverlay.hidden = true;
});

const cartViewOverlay = document.getElementById("cartViewOverlay");
const cartToggle = document.getElementById("cartToggle");

function renderCartView() {
  const list = document.getElementById("cartViewList");
  if (currentCartItems.length === 0) {
    list.innerHTML = `<li class="cart-empty">Cart's empty — add something from the stall.</li>`;
    document.getElementById("cartViewTotal").textContent = formatPrice(0);
    document.getElementById("cartViewCheckoutBtn").disabled = true;
    return;
  }
  list.innerHTML = currentCartItems
    .map((item) => `<li><span>${item.name}</span><span class="mono">${formatPrice(item.price)}</span></li>`)
    .join("");
  const total = currentCartItems.reduce((sum, item) => sum + item.price, 0);
  document.getElementById("cartViewTotal").textContent = formatPrice(total);
  document.getElementById("cartViewCheckoutBtn").disabled = false;
}

cartToggle.addEventListener("click", () => {
  renderCartView();
  cartViewOverlay.hidden = false;
});

document.getElementById("cartViewCloseBtn").addEventListener("click", () => {
  cartViewOverlay.hidden = true;
});

cartViewOverlay.addEventListener("click", (e) => {
  if (e.target === cartViewOverlay) cartViewOverlay.hidden = true;
});

document.getElementById("cartViewCheckoutBtn").addEventListener("click", () => {
  if (currentCartItems.length === 0) return;
  cartViewOverlay.hidden = true;
  openCheckout();
});

const profileOverlay = document.getElementById("profileOverlay");

document.getElementById("profileBtn").addEventListener("click", () => {
  profileOverlay.hidden = false;
});
document.getElementById("profileCloseBtn").addEventListener("click", () => {
  profileOverlay.hidden = true;
});
profileOverlay.addEventListener("click", (e) => {
  if (e.target === profileOverlay) profileOverlay.hidden = true;
});

const ORDERS_STORAGE_KEY = "shopkart_orders";
const ordersOverlay = document.getElementById("ordersOverlay");
const trackOverlay = document.getElementById("trackOverlay");
let lastPlacedOrder = null;

function loadOrdersFromStorage() {
  try {
    return JSON.parse(localStorage.getItem(ORDERS_STORAGE_KEY)) || [];
  } catch (err) {
    return [];
  }
}

function saveOrderToStorage(order) {
  const orders = loadOrdersFromStorage();
  orders.unshift(order);
  localStorage.setItem(ORDERS_STORAGE_KEY, JSON.stringify(orders));
}

function renderOrdersList() {
  const orders = loadOrdersFromStorage();
  const container = document.getElementById("ordersList");

  if (orders.length === 0) {
    container.innerHTML = `<p class="orders-empty">No orders yet — place one from your cart!</p>`;
    return;
  }

  container.innerHTML = orders
    .map(
      (order) => `
      <div class="order-card">
        <div class="order-card-head">
          <span class="order-card-id">${order.orderId}</span>
          <span class="order-card-date">${order.dateLabel}</span>
        </div>
        <div class="order-card-items">${order.items.map((i) => i.name).join(", ")}</div>
        <div class="order-card-foot">
          <span class="order-card-total mono">${formatPrice(order.total)}</span>
          <button type="button" class="order-track-btn" data-order-id="${order.orderId}">Track</button>
        </div>
      </div>`
    )
    .join("");

  container.querySelectorAll(".order-track-btn").forEach((btn) => {
    btn.addEventListener("click", () => {
      const order = orders.find((o) => o.orderId === btn.dataset.orderId);
      if (order) openTrackOverlay(order);
    });
  });
}

function openTrackOverlay(order) {
  document.getElementById("trackOrderIdLabel").textContent = order.orderId;
  document.getElementById("trackDatePlaced").textContent = order.dateLabel;
  document.getElementById("trackDateExpected").textContent = `Expected ${order.expectedDateLabel}`;
  trackOverlay.hidden = false;
}

document.getElementById("ordersBtn").addEventListener("click", () => {
  renderOrdersList();
  ordersOverlay.hidden = false;
});
document.getElementById("ordersCloseBtn").addEventListener("click", () => {
  ordersOverlay.hidden = true;
});
ordersOverlay.addEventListener("click", (e) => {
  if (e.target === ordersOverlay) ordersOverlay.hidden = true;
});

document.getElementById("trackCloseBtn").addEventListener("click", () => {
  trackOverlay.hidden = true;
});
document.getElementById("trackCloseBtn2").addEventListener("click", () => {
  trackOverlay.hidden = true;
});
trackOverlay.addEventListener("click", (e) => {
  if (e.target === trackOverlay) trackOverlay.hidden = true;
});

let checkoutAddress = {};
let checkoutPayment = { method: "COD" };

const COD_FEE = 10;

const checkoutOverlay = document.getElementById("checkoutOverlay");
const checkoutBtn = document.getElementById("checkoutBtn");
const modalCloseBtn = document.getElementById("modalCloseBtn");

function cartSubtotal() {
  return currentCartItems.reduce((sum, item) => sum + item.price, 0);
}

function showStep(stepName) {
  document.querySelectorAll(".modal-step").forEach((el) => (el.hidden = true));
  const idMap = { done: "stepDone" };
  const target = document.getElementById(idMap[stepName] || `step${stepName}`);
  target.hidden = false;

  document.querySelectorAll(".step-dot").forEach((dot) => {
    const dotStep = Number(dot.dataset.step);
    dot.classList.remove("active", "done");
    if (stepName === "done" || dotStep < Number(stepName)) {
      dot.classList.add("done");
    } else if (dotStep === Number(stepName)) {
      dot.classList.add("active");
    }
  });
}

function openCheckout() {
  if (currentCartItems.length === 0) return;

  const list = document.getElementById("checkoutCartList");
  list.innerHTML = currentCartItems
    .map((item) => `<li><span>${item.name}</span><span class="mono">${formatPrice(item.price)}</span></li>`)
    .join("");
  document.getElementById("checkoutTotal").textContent = formatPrice(cartSubtotal());

  showStep("1");
  checkoutOverlay.hidden = false;
}

function closeCheckout() {
  checkoutOverlay.hidden = true;
}

document.getElementById("toStep2Btn").addEventListener("click", () => showStep("2"));
document.getElementById("backTo1Btn").addEventListener("click", () => showStep("1"));

document.getElementById("addressForm").addEventListener("submit", (e) => {
  e.preventDefault();
  checkoutAddress = {
    name: document.getElementById("addrName").value,
    phone: document.getElementById("addrPhone").value,
    line: document.getElementById("addrLine").value,
    city: document.getElementById("addrCity").value,
    pincode: document.getElementById("addrPincode").value,
  };
  showStep("3");
});
document.getElementById("backTo2Btn").addEventListener("click", () => showStep("2"));

document.querySelectorAll('input[name="paymentMethod"]').forEach((radio) => {
  radio.addEventListener("change", (e) => {
    document.getElementById("cardFields").hidden = e.target.value !== "CARD";
    document.getElementById("upiFields").hidden = e.target.value !== "UPI";
    document.getElementById("step3Buttons").hidden = e.target.value === "UPI";
  });
});

document.getElementById("toStep4Btn").addEventListener("click", () => {
  const method = document.querySelector('input[name="paymentMethod"]:checked').value;
  checkoutPayment = { method };
  if (method === "CARD") {
    checkoutPayment.cardLast4 = (document.getElementById("cardNumber").value || "").slice(-4);
  }

  const methodLabel = { COD: "Cash on Delivery", CARD: "Card" }[method];
  const subtotal = cartSubtotal();
  const fee = method === "COD" ? COD_FEE : 0;
  const total = subtotal + fee;

  document.getElementById("orderSummary").innerHTML = `
    <div><strong>Deliver to:</strong> ${checkoutAddress.name}, ${checkoutAddress.line}, ${checkoutAddress.city} - ${checkoutAddress.pincode}</div>
    <div><strong>Phone:</strong> ${checkoutAddress.phone}</div>
    <div><strong>Payment:</strong> ${methodLabel}${checkoutPayment.cardLast4 ? " ending " + checkoutPayment.cardLast4 : ""}</div>
    <div><strong>Items:</strong> ${currentCartItems.length}</div>
    <div><strong>Subtotal:</strong> ${formatPrice(subtotal)}</div>
    ${fee > 0 ? `<div><strong>COD Fee:</strong> ${formatPrice(fee)}</div>` : ""}
    <div><strong>Total:</strong> ${formatPrice(total)}</div>
  `;
  showStep("4");
});
document.getElementById("backTo3Btn").addEventListener("click", () => showStep("3"));

async function finalizeOrder(methodLabel) {
  const itemsSnapshot = currentCartItems.map((item) => ({ name: item.name, price: item.price }));
  const subtotal = itemsSnapshot.reduce((sum, item) => sum + item.price, 0);
  const fee = checkoutPayment.method === "COD" ? COD_FEE : 0;

  const itemCount = currentCartItems.length;
  for (let i = 0; i < itemCount; i++) {
    await ShopKartAPI.undoCart();
  }
  await refreshCart();

  const orderId = "ORD" + Date.now().toString().slice(-8);
  const placedDate = new Date();
  const expectedDate = new Date();
  expectedDate.setDate(placedDate.getDate() + 5);

  const orderRecord = {
    orderId,
    dateLabel: formatDate(placedDate),
    expectedDateLabel: formatDate(expectedDate),
    items: itemsSnapshot,
    total: subtotal + fee,
    paymentMethod: methodLabel,
  };
  saveOrderToStorage(orderRecord);
  lastPlacedOrder = orderRecord;

  document.getElementById("confirmOrderId").textContent = `Order ID: ${orderId}`;
  document.getElementById("confirmDelivery").textContent =
    `Estimated delivery: ${orderRecord.expectedDateLabel} (via ${methodLabel})`;

  showStep("done");
}

document.getElementById("placeOrderBtn").addEventListener("click", async () => {
  const methodLabel = { COD: "Cash on Delivery", CARD: "Card" }[checkoutPayment.method];
  const fee = checkoutPayment.method === "COD" ? COD_FEE : 0;
  const total = cartSubtotal() + fee;

  const confirmed = window.confirm(
    `Confirm your order?\n\nPayment: ${methodLabel}\nTotal: ${formatPrice(total)}\n\nClick OK to place the order.`
  );
  if (!confirmed) return;

  const btn = document.getElementById("placeOrderBtn");
  btn.disabled = true;
  btn.textContent = "Placing order…";
  try {
    await finalizeOrder(methodLabel);
  } catch (err) {
    console.error(err);
    alert("Something went wrong placing the order. Check the console for details.");
  } finally {
    btn.disabled = false;
    btn.textContent = "Place Order";
  }
});

document.querySelectorAll(".upi-app-btn").forEach((appBtn) => {
  appBtn.addEventListener("click", async () => {
    const total = cartSubtotal();
    const confirmed = window.confirm(
      `Pay ${formatPrice(total)} via ${appBtn.dataset.app}?\n\nClick OK to confirm this payment.`
    );
    if (!confirmed) return;

    appBtn.disabled = true;
    try {
      await finalizeOrder(appBtn.dataset.app);
    } catch (err) {
      console.error(err);
      alert("Something went wrong placing the order. Check the console for details.");
    } finally {
      appBtn.disabled = false;
    }
  });
});

document.getElementById("trackOrderBtn").addEventListener("click", () => {
  closeCheckout();
  if (lastPlacedOrder) openTrackOverlay(lastPlacedOrder);
});

document.getElementById("continueShoppingBtn").addEventListener("click", closeCheckout);
modalCloseBtn.addEventListener("click", closeCheckout);
checkoutOverlay.addEventListener("click", (e) => {
  if (e.target === checkoutOverlay) closeCheckout();
});

checkoutBtn.addEventListener("click", openCheckout);

loadCategories();
loadProducts();
refreshCart();