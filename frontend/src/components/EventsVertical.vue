<script setup>
import {ref, onMounted, computed, onUnmounted, watch} from 'vue'
import axios from 'axios'
import { useLocationStore } from '@/stores/LocationStore'
import {useCitiesModalStore} from "@/stores/CitiesModalStore.js";
import CitiesModal from "@/components/CitiesModal.vue";
import {useRouter} from "vue-router";

const events = ref([])
const loading = ref(true)
const error = ref(null)
const locationStore = useLocationStore()
const citiesModalStore = useCitiesModalStore()
const router = useRouter()
const selectedFilter = ref(null);
const filterModal = ref(null)
const filterModalVisible = ref(false)

//change city
function openCitiesModal(){
  citiesModalStore.visible = true;
}

// format time
function formatTime(dateTime) {
  return new Date(dateTime).toLocaleTimeString([], {
    hour: '2-digit',
    minute: '2-digit'
  })
}

//format pace
function formatPace(pace) {
  const minutes = Math.floor(pace)
  const seconds = Math.round((pace - minutes) * 60)
  return `${minutes}:${seconds.toString().padStart(2, '0')}`
}


//loading upcoming events (10 days from now)
onMounted(async () => {
  try {
    // fetch from your Spring Boot backend
    const res = await axios.get('http://192.168.1.128:8080/events/upcoming')
    events.value = res.data
  } catch (err) {
    console.error(err)
    error.value = 'Failed to load events'
  } finally {
    loading.value = false
  }
})

//divide days by dates
const groupedEvents = computed(() => {
  const groups = {}

  events.value.forEach(event => {
    const d = new Date(event.dateTime)
    const weekday = d.toLocaleDateString('en-UK', { weekday: 'short' }).toUpperCase() // MON
    const day = d.getDate()
    const month = d.getMonth() + 1
    const date = `${weekday} ${day}.${month}`

    if (!groups[date]) {
      groups[date] = []
    }
    groups[date].push(event)
  })
  return groups
})

//open event page by event id
function openEvent(id) {
  router.push(`/events/${id}`)
}


function selectFilter(filter) {
  selectedFilter.value = filter
  filterModalVisible.value = true;
  console.log("modal visible: ", filterModalVisible.value)
}

// close modal when clicking outside
function handleClickOutside(event) {
  if (!filterModalVisible.value) return
  if (!filterModal.value) return
  if (!filterModal.value.contains(event.target)) {
    selectedFilter.value = null
    filterModalVisible.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<template>
  <CitiesModal v-if="citiesModalStore.visible" class="cities-modal"/>

  <div v-if="filterModalVisible" ref="modalBackground" class="modal-background"></div>
  <div v-if="filterModalVisible" ref="filterModal" class="filter-modal">
    <div v-if="selectedFilter === 'time'">
      <p>showing time options</p>
    </div>

    <div v-if="selectedFilter === 'pace'">
      <p>Showing pace options</p>
    </div>

    <div v-if="selectedFilter === 'distance'">
      <p>showing distance options</p>
    </div>
  </div>


  <div class="container">
    <nav>
      <h1 v-if="locationStore.city">{{ locationStore.city }}
        <button @click="openCitiesModal" id="change-city-btn">change city</button>
      </h1>
      <h1 v-else-if="locationStore.longitude">Events in your area</h1>
      <h1 v-else></h1>
    </nav>

    <p>MODAL VISIBLE {{filterModalVisible}}</p>

    <ul class="filters">
      <li class="filter" :class="{ selected: selectedFilter === 'all' }" @click.stop="selectFilter('all')">Icon</li>
      <li class="filter" :class="{ selected: selectedFilter === 'time' }" @click.stop="selectFilter('time')">Start Time</li>
      <li class="filter" :class="{ selected: selectedFilter === 'pace' }" @click.stop="selectFilter('pace')">Pace</li>
      <li class="filter" :class="{ selected: selectedFilter === 'distance' }" @click.stop="selectFilter('distance')">Distance</li>
    </ul>

    <div id="all-filters-modal">
      <p>showing all filtering options</p>
    </div>

    <div class="ul-container">
      <div v-if="loading">Loading events...</div>
      <div v-else-if="error" class="error">{{ error }}</div>

      <div v-else>
        <div v-for="(dayEvents, date) in groupedEvents" :key="date" class="date-block">
          <h2 class="date-header">{{ date }}</h2>
          <div v-if="dayEvents.length === 0">No events scheduled</div>
          <ul v-else>
            <li class="event" v-for="event in dayEvents" :key="event.id" @click="openEvent(event.id)">
              <div class="hour-club-pace">
                <div class="hour-club">
                  <p>{{ formatTime(event.dateTime) }}</p>
                  <p class="club">CLUBS NAME</p>
                </div>
                <p class="small-text">{{ event.distance }} km - {{ formatPace(event.pace) }} min/km</p>
              </div>

              <div class="hour-club">
                <p class="invisible-time">{{ formatTime(event.dateTime) }}</p>
                <p>{{ event.meetingPoint}}</p>
              </div>

            </li>
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
  gap: 2rem;
}

.cities-modal{
  position: absolute;
  width: 80vw;
  top: 6rem;
  left: 10vw;
  z-index: 101;
}

h1{
  height: 2rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-weight: 800;
}

.filters{
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.filter{
  background-color: #FB5624;
  opacity: 0.6;

  padding: 0.5rem 1rem;
  border-radius: 25px;
}

.filter.selected{
  opacity: 1;
}

.filter-modal{
  position: fixed;
  top: 70vh;
  left: 0;

  height: 30vh;
  width: 100vw;

  color: white;
  background-color: #181818;
  z-index: 1001;

  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-background{
  position: fixed;
  left: 0;
  bottom: 0;
  height: 100vh;
  width: 100vw;
  background-color: black;
  opacity: 0.8;
  z-index: 1000;
}

.ul-container {
  position: relative;
  font-family: Arial, sans-serif;

  overflow-y: auto;
  -webkit-overflow-scrolling: touch;

  margin-bottom: 20vh;
}

::-webkit-scrollbar  {
  display: none;
}

.error {
  background-color: #181818;
  color: rgba(223, 41, 41, 0.85);
}

#change-city-btn{
  color: white;
  font-size: 12px;
  background-color: #181818;
  border: none;
}

.date-header{
  font-size: 20px;
  font-weight: 800;
  border-bottom: 1px solid white;
  margin-bottom: 1rem;
}

ul{
  list-style: none;
  font-size: 16px;
  margin: 0;
  padding: 0;

}

.event{
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding-bottom: 2rem;
}

.hour-club-pace{
  display: flex;
  justify-content: space-between;
}

.hour-club{
  display: flex;
  gap: 1rem;
}

.club{
  font-weight: 800;
}

.invisible-time{
  visibility: hidden;
}

.small-text{
  font-size: 14px;
  font-weight: 200;
}

</style>