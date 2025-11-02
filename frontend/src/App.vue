<script setup>
  import NavBarComponent from "@/components/NavBarComponent.vue";
  import {ref, onMounted, watch} from "vue";
  import {jwtDecode} from "jwt-decode";
  import { token } from "@/auth";

  const username = ref(null);

  /**
   * Decode JWT token to extract username
   */
  function decodeToken() {
    if (token.value) {
      try {
        const decoded = jwtDecode(token.value);
        username.value = decoded.sub  || null;
      } catch (err) {x
        console.error("Invalid token", err);
        username.value = null;
      }
    } else {
      username.value = null;
    }
  }

  // Decode token when the component mounts
  onMounted(() => {
    decodeToken();
  });

  // Watch the reactive token from auth.js to update username
  watch(token, () => {
    decodeToken();
  });
</script>

<template>
  <header>
    <div v-if="username">Logged in as: {{username}} </div>
    <div v-else>Not logged in</div>
  </header>

  <main>
    <router-view></router-view>

    <div class="container">

      <NavBarComponent ref="navbar" class="navbar"/>

    </div>
  </main>
</template>

<style scoped>
  .container{
    position: relative;
  }

  .navbar{
    position: fixed;
    bottom: 0;
    left: 0;
    z-index: 10;
  }
</style>
