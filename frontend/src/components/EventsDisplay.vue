<script setup>
import axios from "axios";
import {token} from "@/auth.js";
import {onMounted, ref} from "vue";

const loading = ref(null)
const error = ref(null)
const events = ref([])
const message = ref(null)


//format pace
function formatPace(pace) {
  const minutes = Math.floor(pace)
  const seconds = Math.round((pace - minutes) * 60)
  return `${minutes}:${seconds.toString().padStart(2, '0')}`
}

const loadEvents= async () => {
  loading.value = true
  error.value = null

  if(token.value){
    try {
      const res = await axios.get('http://192.168.1.128:8080/events/owned-events', {
        headers: {
          Authorization: `Bearer ${token.value}`
        }
      })

      events.value = res.data
      reloadEvents()
    } catch (err) {
      console.error(err)
      error.value = 'Failed to load clubs'
    } finally {
      loading.value = false
    }
  }
}

const deleteEvent = async (id) => {
  try {
    if(token.value){
      await axios.delete(
          `http://localhost:8080/events/owned-events/${id}`,
          {
            headers: {
              Authorization: `Bearer ${token.value}`,
            },
          })
      message.value = `Deleted successfully!`;
    }
    reloadEvents()
  } catch (error) {
    console.error(error);
    message.value = "Error deleting";
  }
};

onMounted(() => {
  loadEvents()
})

function reloadEvents(){
  loadEvents()
}

</script>

<template>
  <div class="container">
    <div v-if="loading">
      <p class="message">Loading events..</p>
    </div>
    <div v-else-if="!loading&&events.length === 0">
      <p>You haven't created any events yet</p>
    </div>
    <div v-else class="ul-container">
      <div v-if="loading">Loading events...</div>
      <div v-else-if="error" class="error">{{ error }}</div>

      <div v-else class="carousel">
        <div v-for="(event, index) in events" :key="event.id || index" class="carousel-item">
          <p>{{ event.distance }} km - {{ formatPace(event.pace) }} min/km</p>
          <p>{{event.meetingPoint}}</p>
          <button @click="deleteEvent(event.id)">delete event</button>
        </div>
      </div>

    </div>
  </div>
</template>

<style scoped>
.carousel::-webkit-scrollbar {
  display: none;             /* Chrome, Safari, Opera */
}

.message{
  background: #181818;
}

.carousel{
  display: flex;
  align-items: center;
  gap: 1rem;

  overflow-x: auto;
  scroll-behavior: smooth;
  scroll-snap-type: x mandatory;
  list-style: none;

  width: 100vw;
  background-color: #181818;
  padding: 1rem;
}

.carousel > * {
  flex: 0 0 auto;
  scroll-snap-align: center;
}

.carousel-item{
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  padding: 2rem;

  flex: 0 0 auto;
  width: 100%;

  border-radius: 12px;
  color: #fff;
  background: linear-gradient(180deg, rgba(255,255,255,0.02), rgba(0,0,0,0.4));
  border: 1px solid rgba(255, 255, 255, 0.05);
  font-weight: 600;
  cursor: pointer;

  transition: box-shadow 0.4s ease;
}

.carousel-item:hover {
  box-shadow: 0 0 12px rgba(46, 139, 255, 0.25);
}

.carousel-item button{
  width: fit-content;
  background-color: inherit;
  color: #2E8BFF;
  border: none;
}
</style>
