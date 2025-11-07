<script setup>
  import {useRouter} from "vue-router";
  import {computed} from "vue";
  import {token} from "@/auth.js";
  import {jwtDecode} from "jwt-decode";

  const router = useRouter();

  function openProfile(){
    router.push('/profile')
  }

  function opeFilteredEvents(){
    router.push('/events')
  }

  //go to log in
  function goToLogIn(){
    router.push("/login")
  }

  const username = computed(() => {
    if (token.value) {
      try {
        const decoded = jwtDecode(token.value);
        return decoded.sub || null;
      } catch (err) {
        console.error("Invalid token", err);
        return null;
      }
    }
    return null;
  });

</script>

<template>
  <div class="container">
    <ul>
      <li @click="opeFilteredEvents">EVENTS</li>
      <li>
        <p v-if="token" @click="openProfile">
          PROFILE
        </p>
        <p v-else @click="goToLogIn">
          LOG IN
        </p>
      </li>
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
  width: 100%;
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