<script setup>
import { ref } from "vue";
import { setToken } from "@/auth";

const username = ref("");
const password = ref("");
const message = ref("");

// Login function
const login = async () => {
  try {
    const response = await fetch("http://localhost:8080/api/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username: username.value, password: password.value }),
    });

    if (!response.ok) throw new Error("Login failed");

    const data = await response.json();

    if (!data.access_token) throw new Error("No token returned from backend");

    // Update reactive token in auth.js
    setToken(data.access_token);

    message.value = "Logged in successfully!";
  } catch (err) {
    message.value = err.message;
  }
};
</script>

<template>
  <div>
    <input v-model="username" placeholder="Username" />
    <input v-model="password" type="password" placeholder="Password" />
    <button @click="login">Log In</button>
    <p>{{ message }}</p>
  </div>
</template>


<style scoped>
</style>

