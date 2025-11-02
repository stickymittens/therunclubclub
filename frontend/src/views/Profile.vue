<script setup>
import {ref} from "vue";
import LogOutComponent from "@/components/LogOutComponent.vue";

  const activeTab = ref('joined');

  function selectTab(tab) {
    activeTab.value = tab;
  }
</script>

<template>
  <div class="container">
    <div class="header-container">
      <h1>PROFILE</h1>
      <LogOutComponent/>
    </div>

    <ul>
      <li :class="{ active: activeTab === 'joined' }" @click="selectTab('joined')">Joined Events</li>

      <li :class="{ active: activeTab === 'hosted' }" @click="selectTab('hosted')">Hosted Clubs&Events</li>

      <li :class="{ active: activeTab === 'myData' }" @click="selectTab('myData')">My Data</li>
    </ul>

    <div class="profile-body">
      <div v-if="activeTab === 'hosted'">
        <div>+ Add a Club or Event</div>
        <div>Hosted Clubs</div>
        <div>Hosted Events</div>
      </div>
    </div>

<!--    <AddEvent></AddEvent>-->
<!--    <AddClub></AddClub>-->
  </div>
</template>

<style scoped>
  .container{
    color: white;
    background-color: #000000;
  }

  .header-container{
    display: flex;
    align-items: center;
  }

  h1{
    padding: 2rem 1rem;
  }

  .profile-body{
    background-color: #181818;
    margin: 0;
  }

  ul{
    list-style-type: none;

    font-size: 12px;

    display: flex;
    justify-content: space-between;

    margin: 0;

    background-color: #000000;
    padding: 0 1rem;
  }

  li{
    margin: 0;
    padding: 1rem;
  }

  li.active{
    background-color: #181818;
  }
</style>