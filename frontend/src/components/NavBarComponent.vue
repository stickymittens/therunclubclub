<script setup lang="ts">
  import {useRouter} from "vue-router";

  const router = useRouter();

  function openProfile(){
    router.push('/profile')
  }

  function opeFilteredEvents(){
    router.push('/events')
  }
</script>

<template>
  <div class="container">
    <ul>
      <li @click="opeFilteredEvents">EVENTS</li>
      <li @click="openProfile">PROFILE</li>
    </ul>

    <img id="glass"
         src="/Users/karolina/IdeaProjects/runClub/frontend/src/components/icons/loupe.png"
         alt="loupe icon"/>

    <div class="dot"></div>
  </div>
</template>

<style scoped>
.container{
  position: relative;
  background-color: #FB5624;
  width: 100vw;
}

ul{
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 100%;
  padding: 1rem 2rem;
}

li{
  list-style-type: none;
  padding: 0 1rem;
}

#glass{
  position: absolute;
  padding: 0 1rem;
  right: 0;
  bottom: 1.4rem;
}

.dot {
  position: absolute;
  bottom: 1rem;
  left: 38vw;

  height: 24vw;
  width: 24vw;
  border-radius: 50%;

  border: 2px solid #FB5624;
  background-color: #181818;
}

img{
  height: 1rem;
}
</style>