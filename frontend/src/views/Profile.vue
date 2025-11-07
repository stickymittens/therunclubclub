<script setup>
import {computed, onMounted, ref} from "vue";
import LogOutComponent from "@/components/LogOutComponent.vue";
import axios from "axios";
import {token} from "@/auth.js";
import AddClub from "@/components/AddClub.vue";
import ClubsDisplay from "@/components/ClubsDisplay.vue";
import AddEvent from "@/components/AddEvent.vue";
import EventsDisplay from "@/components/EventsDisplay.vue";
import {useRouter} from "vue-router";

  const activeTab = ref('joined');
  const loading = ref(null)
  const error = ref(null)
  const events = ref([])
  const message = ref(null)
const router = useRouter()

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

//format date
function formatDate(dateTime) {
  return new Date(dateTime).toLocaleDateString([], {
    weekday: 'short',   // e.g., "Sat"
    day: '2-digit',     // e.g., "06"
    month: 'short',     // e.g., "Dec"
  })
}

//format pace
function formatPace(pace) {
  const minutes = Math.floor(pace)
  const seconds = Math.round((pace - minutes) * 60)
  return `${minutes}:${seconds.toString().padStart(2, '0')}`
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
  } catch (error) {
    console.error(error);
    message.value = "Error leaving";
  }
};

function openEvent(id) {
  router.push(`/events/${id}`)
}
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

          <div v-else class="joined-events-container">
            <div v-for="(event, index) in events" :key="event.id || index" class="joined-event" @click="openEvent(event.id)">
              <p class="event-date">{{ formatDate(event.dateTime)}}</p>
              <p>{{ event.meetingPoint }}, {{event.city}}</p>
              <p>{{formatTime(event.dateTime)}} - {{ event.distance }}km - {{formatPace(event.pace)}}min/km</p>
              <button @click="leaveEvent(event.id)" class="leave-btn">Leave event</button>
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
    position: sticky;
    top: 0;
    left: 0;

    display: flex;
    flex-direction: column;
    justify-content: space-between;
    height: 20vh;

    background-color: #000000;
    border-bottom: 1px solid #FB5624;

    z-index: 101;
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
    z-index: 101;
    margin: 1px 1px 0 1px;
  }

  .bookmarks li.active{
    background-color: #181818;
    margin: 0;
    border-top: 1px solid #FB5624;
    border-left: 1px solid #FB5624;
    border-right: 1px solid #FB5624;

    position: relative;
    top: 1.5px;
  }

  .profile-body{
    display: flex;
    flex-direction: column;
    gap: 0.5rem;

    margin-bottom: 15vh;
  }

  .joined-events-container{
    padding: 1rem;
  }

  .joined-event{
    font-size: 16px;

    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;

    margin: 2rem 0;

    border: 1px dotted white;
    border-radius: 8px;
    padding: 1rem 2rem;
  }

  .event-date{
    font-size: 20px;
    text-transform: uppercase;
  }

  .leave-btn {
    margin-top: 0.4rem;
    background-color: #2E8BFF;
    border: none;
    border-radius: 6px;
    color: white;
    font-weight: 600;
    font-size: 0.85rem;
    padding: 0.4rem 0.8rem;
    cursor: pointer;
    transition: background-color 0.2s ease;
  }

  .leave-btn:hover {
    background-color: #5CA5FF;
  }

  h3{
    padding: 1rem 0 0 1rem;
    margin: 0;
    font-size: 16px;
  }

  .carousel-container{
    display: flex;
    flex-direction: column;

    padding-top: 1rem;
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
    width: 100%;
    box-sizing: border-box;

    border-radius: 12px;
    color: #fff;
    background: #181818;
    margin: 0;
    padding: 0;

  }
</style>