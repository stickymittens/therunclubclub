<script setup>
import { ref } from "vue";
import {setToken, token} from "@/auth";
import {useLocationStore} from "@/stores/LocationStore.js";
import axios from "axios";

const username = ref("");
const password = ref("");
const message = ref("");
const locationStore = useLocationStore()

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

    setToken(data.access_token);
    message.value = "Logged in successfully!";

    const res = await axios.get('http://192.168.1.128:8080/api/preferred-city', {
      headers: {
        Authorization: `Bearer ${token.value}`
      }
    })

    locationStore.city = res.data;

  } catch (err) {
    message.value = err.message;
  }
};
</script>

<template>
  <div class="login-page">

    <div class="background"></div>

    <div class="overlay"></div>

    <div class="login-container">
      <h1 class="title">Welcome Back</h1>
      <p class="subtitle">Let's run together</p>

      <div class="form">
        <input v-model="username" placeholder="Username" />
        <input v-model="password" type="password" placeholder="Password" />
        <button @click="login">Log In</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  position: relative;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

/* Background image */
.background {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  background-image: url("https://www.shape.com/thmb/XhaeY6hfYXOUEmpvxZKjOi_-H5A=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/running-longer-or-faster-31e97070bda14ffc8afdea52094504c7.jpg");
  background-position: center;
  background-repeat: no-repeat;
  background-size: cover;
  z-index: 1;
}

/* Dark overlay */
.overlay {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  background-color: rgba(24, 24, 24, 0.8);
  z-index: 2;
}

/* Login container */
.login-container {
  position: fixed;
  top: calc((100vh - 20vh) / 2);
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 3;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  color: #ffffff;
  padding: 2rem;
  width: 80%;
}

.title {
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 0.25rem;
}

.subtitle {
  font-size: 0.95rem;
  color: #cccccc;
  margin-bottom: 2rem;
}

/* Form */
.form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  width: 90%;
  max-width: 350px;
}

input {
  padding: 0.75rem 1rem;
  border-radius: 8px;
  border: none;
  background-color: #000000cc;
  color: #ffffff;
  outline: none;
  font-size: 1rem;
}

input::placeholder {
  color: #888;
}

button {
  padding: 0.75rem 1rem;
  border-radius: 8px;
  border: none;
  font-size: 1rem;
  font-weight: 600;
  color: #fff;
  background-color: #FB5624;
  cursor: pointer;
  transition: background-color 0.2s ease, transform 0.1s ease;
}

button:hover {
  background-color: #ff6a3c;
  transform: translateY(-1px);
}

.message {
  margin-top: 0.75rem;
  font-size: 0.9rem;
  color: #FB5624;
}
</style>


