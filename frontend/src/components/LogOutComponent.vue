<script setup>
import { token, clearToken } from "@/auth"; // use shared reactive token
import { useRouter } from "vue-router";
const router = useRouter();

function logout() {
  clearToken();
  router.push("/events");
}
</script>

<template>
  <div class="container">
    <!-- button shows only if user is logged in -->
    <button v-if="token" @click="logout">Log out</button>
  </div>
</template>

<style scoped>
  button{
    background-color: inherit;
    color: #FB5624;
    font-weight: 200;
    border: none;
  }
</style>