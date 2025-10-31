import { ref } from "vue";

// Reactive token initialized from localStorage
export const token = ref(localStorage.getItem("token") || null);

/**
 * Set a new token
 * Updates localStorage and reactive token
 * @param {string} newToken
 */
export function setToken(newToken) {
    localStorage.setItem("token", newToken);
    token.value = newToken;
}

/**
 * Clear the token (logout)
 * Removes from localStorage and updates reactive token
 */
export function clearToken() {
    localStorage.removeItem("token");
    token.value = null;
}

/**
 * Optional: Sync token with other tabs
 * Fires when localStorage changes in another tab
 */
window.addEventListener("storage", (e) => {
    if (e.key === "token") {
        token.value = e.newValue;
    }
});