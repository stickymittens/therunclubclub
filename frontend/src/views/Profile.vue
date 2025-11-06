<script setup>
import {computed, onMounted, ref} from "vue";
import LogOutComponent from "@/components/LogOutComponent.vue";
import axios from "axios";
import {token} from "@/auth.js";
import AddClub from "@/components/AddClub.vue";
import ClubsDisplay from "@/components/ClubsDisplay.vue";

  const activeTab = ref('joined');
  const loading = ref(null)
  const error = ref(null)
  const events = ref([])
  const message = ref(null)

  function selectTab(tab) {
    activeTab.value = tab;
  }

// format time
function formatTime(dateTime) {
  return new Date(dateTime).toLocaleTimeString([], {
    hour: '2-digit',
    minute: '2-digit'
  })
}

const loadJoinedEvents = async () => {
  loading.value = true
  error.value = null

  try {
    const res = await axios.get('http://192.168.1.128:8080/api/joined-events', {
      headers: {
        Authorization: `Bearer ${token.value}`
      }
    })

    events.value = res.data
  } catch (err) {
    console.error(err)
    error.value = 'Failed to load events'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadJoinedEvents()
})

function reloadJoinedEvents(){
  loadJoinedEvents()
}

const leaveEvent = async (id) => {
  try {
    if(token.value){
      await axios.delete(
          `http://localhost:8080/events/${id}/leave`,
          {
            headers: {
              Authorization: `Bearer ${token.value}`,
            },
          })
      message.value = `Left successfully!`;
    }
    reloadJoinedEvents()
  } catch (error) {
    console.error(error);
    message.value = "Error leaving";
  }
};
</script>

<template>
  <div class="container">
    <div class="header-container">
      <h1>PROFILE</h1>
      <LogOutComponent/>
    </div>

    <ul class="bookmarks">
      <li :class="{ active: activeTab === 'joined' }" @click="selectTab('joined')">Joined</li>

      <li :class="{ active: activeTab === 'hosted' }" @click="selectTab('hosted')">Hosted</li>

      <li :class="{ active: activeTab === 'myData' }" @click="selectTab('myData')">My Data</li>
    </ul>

    <div class="profile-body">
      <div v-if="activeTab === 'joined'">
        <div v-if="events.length === 0">
          <p class="message">You haven't joined any events</p>
        </div>
        <div v-else class="ul-container">
          <div v-if="loading">Loading events...</div>
          <div v-else-if="error" class="error">{{ error }}</div>
          <div v-else>
            <div v-for="(event, index) in events" :key="event.id || index">
              <p><strong>ID:</strong> {{ event.id }}</p>
              <p><strong>Meeting Point:</strong> {{ event.meetingPoint }}</p>
              <p><strong>Date:</strong> {{ event.dateTime }}</p>
              <p><strong>Distance:</strong> {{ event.distance }} km</p>
              <p><strong>Pace:</strong> {{ event.pace }} min/km</p>
              <button @click="leaveEvent(event.id)">X</button>
              <hr />
            </div>
          </div>

        </div>

      </div>
    </div>

    <div class="profile-body">
      <div v-if="activeTab === 'hosted'">
        <ul class="clubs-carousel">
          <li><ClubsDisplay/></li>
          <li><AddClub/></li>
        </ul>
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

  .message{
    background: #181818;
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
    padding: 1rem 0;
  }

  .bookmarks{
    list-style-type: none;

    font-size: 12px;

    display: flex;
    justify-content: space-between;

    margin: 0;

    background-color: #000000;
    color: white;
    padding: 0 2rem;
  }

  .bookmarks li{
    color: white;
    margin: 0;
    padding: 1rem 2rem;
    transition: color .12s ease, backgriund .12s ease, transform .08s ease;
  }

  .bookmarks li.active{
    background-color: #181818;
    font-weight: 600;
  }

  .clubs-carousel::-webkit-scrollbar {
    display: none;             /* Chrome, Safari, Opera */
  }

  .clubs-carousel{
    display: flex;
    align-items: center;
    gap: 1rem;
    width: 100%;

    overflow-x: auto;
    scroll-behavior: smooth;
    list-style: none;
    background-color: #181818;
    padding: 1rem 0;
    margin: 0;

    height: max-content;
    box-sizing: border-box;

  }

  .clubs-carousel li{
    flex: 0 0 auto;
    text-align: center;

    min-width: 100%;
    height: 100%;
    padding: 0;
    margin: 0;

    border-radius: 12px;
    color: #fff;
  }

  /* AddClub card */
  .clubs-carousel li:nth-child(2) {
    background: linear-gradient(180deg, rgba(251,86,36,0.15), rgba(251,86,36,0.05));
    color: #FB5624;
    border: 1px dashed rgba(251, 86, 36, 0.4);
    font-weight: 600;
    cursor: pointer;
    padding: 1rem;
  }

  .clubs-carousel li:nth-child(2):hover {
    transform: translateY(-4px);
    box-shadow: 0 10px 24px rgba(0, 0, 0, 0.6);
  }
</style>