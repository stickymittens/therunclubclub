<script setup>
import {ref} from "vue";
import AddEvent from "@/components/AddEvent.vue";
import AddClub from "@/components/AddClub.vue";
import EventsVertical from "@/components/EventsVertical.vue";

  const activeTab = ref('joined');

  function selectTab(tab) {
    activeTab.value = tab;
  }
</script>

<template>
  <div class="container">
    <h1>Profile</h1>

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