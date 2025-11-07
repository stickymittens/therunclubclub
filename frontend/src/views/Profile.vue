<script setup>
import {computed, onMounted, ref} from "vue";
import LogOutComponent from "@/components/LogOutComponent.vue";
import axios from "axios";
import {token} from "@/auth.js";
import AddClub from "@/components/AddClub.vue";
import ClubsDisplay from "@/components/ClubsDisplay.vue";
import AddEvent from "@/components/AddEvent.vue";
import EventsDisplay from "@/components/EventsDisplay.vue";

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

    <div class="profile-head">
      <div class="title">
        <h1>PROFILE</h1>
        <LogOutComponent/>
        </div>

      <ul class="bookmarks">
        <li :class="{ active: activeTab === 'joined' }" @click="selectTab('joined')">Joined</li>

        <li :class="{ active: activeTab === 'hosted' }" @click="selectTab('hosted')">Hosted</li>

        <li :class="{ active: activeTab === 'myData' }" @click="selectTab('myData')">My Data</li>
      </ul>
    </div>

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

        <div v-if="activeTab === 'hosted'">
          <div class="carousel-container">
            <h3>Hosted clubs</h3>
            <ul class="clubs-carousel">
              <li><ClubsDisplay/></li>
              <li><AddClub/></li>
            </ul>
          </div>

          <div class="carousel-container">
            <h3>Hosted Events</h3>
            <ul class="clubs-carousel">
              <li><EventsDisplay/></li>
              <li><AddEvent/></li>
            </ul>
          </div>
        </div>
    </div>

  </div>
</template>

<style scoped>
  .container{
    position: relative;
    display: flex;
    flex-direction: column;
  }

  .message{
    background: #181818;
  }

  .profile-head{
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    height: 20vh;

    background-color: #000000;
  }

  .title{
    display: flex;
    align-items: center;
    gap: 1rem;
    height: 10vh;
    padding-top: 1rem;
    padding-left: 1rem;
  }

  .bookmarks{
    height: 10vh;
    list-style-type: none;

    display: flex;
    justify-content: space-between;
    align-items: flex-end;

    color: white;
    padding: 0 2rem;
  }


  .bookmarks li{
    color: white;
    padding: 0.5rem 1rem;
    transition: color .12s ease, backgriund .12s ease, transform .08s ease;
  }

  .bookmarks li.active{
    background-color: #181818;
    text-decoration: underline;
    text-decoration-thickness: 1px;
    text-underline-offset: 0.2rem;
  }

  .profile-body{
    height: 100vw;
    margin: 0;

    display: flex;
    flex-direction: column;
    gap: 2rem;
  }

  .carousel-container{
    display: flex;
    flex-direction: column;
    border: 2px solid red;

    padding-top: 1rem;
  }

  .clubs-carousel::-webkit-scrollbar {
    display: none;             /* Chrome, Safari, Opera */
  }

  .clubs-carousel{
    display: flex;
    gap: 1rem;
    width: 100%;
    max-height: 20vh;
    box-sizing: border-box;

    overflow-x: auto;
    scroll-snap-type: x mandatory;
    scroll-behavior: smooth;
    list-style: none;
    background-color: #181818;
    padding: 1rem 0;
    margin: 0;
  }

  .clubs-carousel > * {
    flex: 0 0 auto;
    scroll-snap-align: center;
  }

  .clubs-carousel li{
    flex: 0 0 auto;
    text-align: center;

    width: clamp(250px, 80vw, 400px);
    min-width: 100%;
    padding: 0;
    margin: 0 2rem;
    box-sizing: border-box;

    border-radius: 12px;
    color: #fff;
    background: #181818;
  }
</style>