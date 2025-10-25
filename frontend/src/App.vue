<script setup>
  import NavBarComponent from "@/components/NavBarComponent.vue";
</script>

<template>
  <header>
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
