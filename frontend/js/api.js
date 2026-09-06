// js/api.js
// Thin wrapper around fetch() for every ShopKart backend endpoint.
// Change API_BASE_URL if your Spring Boot app runs on a different port.

const API_BASE_URL = "http://localhost:8080/api";

const ShopKartAPI = {
  async getProducts(category) {
    const url = category
      ? `${API_BASE_URL}/products?category=${encodeURIComponent(category)}`
      : `${API_BASE_URL}/products`;
    const res = await fetch(url);
    if (!res.ok) throw new Error(`getProducts failed: ${res.status}`);
    return res.json();
  },

  async getCategories() {
    const res = await fetch(`${API_BASE_URL}/products/categories`);
    if (!res.ok) throw new Error(`getCategories failed: ${res.status}`);
    return res.json();
  },

  async getCart() {
    const res = await fetch(`${API_BASE_URL}/cart`);
    if (!res.ok) throw new Error(`getCart failed: ${res.status}`);
    return res.json();
  },

  async addToCart(productId) {
    const res = await fetch(`${API_BASE_URL}/cart/add/${encodeURIComponent(productId)}`, {
      method: "POST",
    });
    if (!res.ok) {
      const message = await res.text();
      throw new Error(message || `addToCart failed: ${res.status}`);
    }
    return res.json();
  },

  async undoCart() {
    const res = await fetch(`${API_BASE_URL}/cart/undo`, { method: "POST" });
    if (!res.ok) throw new Error(`undoCart failed: ${res.status}`);
    return res.json();
  },

  async bestValue(budget) {
    const res = await fetch(`${API_BASE_URL}/orders/best-value?budget=${encodeURIComponent(budget)}`);
    if (!res.ok) throw new Error(`bestValue failed: ${res.status}`);
    return res.json();
  },
};
