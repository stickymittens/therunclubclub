<script setup>
import {ref, onMounted, computed, onUnmounted, watch} from 'vue'
import axios from 'axios'
import { useLocationStore } from '@/stores/LocationStore'
import {useCitiesModalStore} from "@/stores/CitiesModalStore.js";
import CitiesModal from "@/components/CitiesModal.vue";
import {useRouter} from "vue-router";
import FilterInput from './FilterInput.vue'

const events = ref([])
const loading = ref(true)
const error = ref(null)

const locationStore = useLocationStore()
const citiesModalStore = useCitiesModalStore()

const router = useRouter()

const selectedFilter = ref(null);
const paceFilter = ref(null)
const distanceFilter = ref(null)
const filterModal = ref(null)
const filterModalVisible = ref(false)

//FILTERS
let paceActive = ref(false)
let distanceActive = ref(false)
const minPace = ref(null)
const maxPace = ref(null)
const minDistance = ref(null)
const maxDistance = ref(null)


//change city
function openCitiesModal(){
  citiesModalStore.visible = true;
}

watch(
    () => locationStore.city,
    (city) => {
      loadEvents()
    }
)

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
const loadEvents = async () => {
  loading.value = true
  error.value = null

  if (locationStore.city) {
    try {
      const res = await axios.get(`http://192.168.1.128:8080/events/upcoming/${locationStore.city}`)
      events.value = res.data

      let eventsData = []
      distanceActive = minDistance.value !== null && maxDistance.value !== null
      paceActive = minPace.value !== null && maxPace.value !== null

      const baseUrl = `http://192.168.1.128:8080/events/upcoming/${locationStore.city}`

      if (distanceActive && paceActive) {
        const [distanceRes, paceRes] = await Promise.all([
          axios.get(`${baseUrl}/filtered-by-distance?min=${minDistance.value}&max=${maxDistance.value}`),
          axios.get(`${baseUrl}/filtered-by-pace?min=${minPace.value}&max=${maxPace.value}`)
        ])
        const paceIds = new Set(paceRes.data.map(e => e.id))
        eventsData = distanceRes.data.filter(e => paceIds.has(e.id))
      }
      else if (distanceActive) {
        const res = await axios.get(`${baseUrl}/filtered-by-distance?min=${minDistance.value}&max=${maxDistance.value}`)
        eventsData = res.data
      }
      else if (paceActive) {
        const res = await axios.get(`${baseUrl}/filtered-by-pace?min=${minPace.value}&max=${maxPace.value}`)
        eventsData = res.data
      }
      else {
        const res = await axios.get(baseUrl)
        eventsData = res.data
      }

      events.value = eventsData

    } catch (err) {
      console.error(err)
      error.value = 'Failed to load events'
    } finally {
      loading.value = false
    }
  }

}

onMounted(() => {
  loadEvents()
})

//divide days by dates
const groupedEvents = computed(() => {
  // Map of date -> events
  const groups = new Map();

  events.value.forEach(event => {
    const d = new Date(event.dateTime);
    const weekday = d.toLocaleDateString('en-UK', { weekday: 'short' }).toUpperCase(); // MON
    const day = d.getDate();
    const month = d.getMonth() + 1;

    // Use YYYY-MM-DD as key to ensure chronological sorting
    const key = d.toISOString().split('T')[0];
    const label = `${weekday} ${day}.${month}`;

    if (!groups.has(key)) {
      groups.set(key, { label, events: [] });
    }

    groups.get(key).events.push(event);
  });

  // Convert to array and sort by actual date
  return Array.from(groups.entries())
      .sort(([a], [b]) => new Date(a) - new Date(b))
      .map(([_, value]) => value);
});

//open event page by event id
function openEvent(id) {
  router.push(`/events/${id}`)
}


function selectFilter(filter) {
  selectedFilter.value = filter
  filterModalVisible.value = true;
  // console.log("modal visible: ", filterModalVisible.value)
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

watch(
    () => filterModalVisible.value,
    (isVisible) => {
        document.body.style.overflow = isVisible ? 'hidden' : 'auto'
    }
)

//FILTERS
function updatePace(vals) {
  if(events.value.length>0){
    minPace.value = vals.min
    maxPace.value = vals.max
  }
  paceFilter.value.style.opacity="1"
  loadEvents()
}

function updateDistance(vals) {
  if(events.value.length>0){
    minDistance.value = vals.min
    maxDistance.value = vals.max
  }
  distanceFilter.value.style.opacity="1"
  loadEvents()
}

function resetPace(){
  if(events.value.length>0){
    minPace.value = null;
    maxPace.value = null;
  }
  paceFilter.value.style.opacity="0.6"
  loadEvents()
}

function resetDistance(){
  if(events.value.length>0){
    minDistance.value = null;
    maxDistance.value = null;
  }
  distanceFilter.value.style.opacity="0.6"
  loadEvents()
}

function resetFilters(){
  resetPace()
  resetDistance()
}
</script>

<template>
  <CitiesModal v-if="citiesModalStore.visible" class="cities-modal"/>

  <div v-if="filterModalVisible" ref="modalBackground" class="modal-background"></div>
  <div v-if="filterModalVisible" ref="filterModal">

    <div v-if="selectedFilter === 'pace'" class="filter-modal">
      <FilterInput
          label="Pace (min/km)"
          :min="minPace"
          :max="maxPace"
          :step="0.01"
          :decimalPlaces="2"
          @updateFilters="updatePace"
      />
      <button @click="resetPace()" class="small-text reset-filters-btn">Reset</button>
    </div>

    <div v-if="selectedFilter === 'distance'" class="filter-modal">
      <FilterInput
          label="Distance (km)"
          :min="minDistance"
          :max="maxDistance"
          :step="0.5"
          :decimalPlaces="1"
          @updateFilters="updateDistance"
      />
      <button @click="resetDistance()" class="small-text reset-filters-btn">Reset</button>
    </div>
  </div>


  <div class="container">
    <div class="profile-head">

      <div>
        <div v-if="locationStore.city" class="title">
          <h1>{{ locationStore.city }}</h1>
          <button @click="openCitiesModal" class="small-text change-city-btn">change city</button>
        </div>
        <div v-else-if="locationStore.longitude" class="title">
          <h1>Events in your area</h1>
        </div>
      </div>

      <div class="filters-container">
        <ul class="filters">
          <li ref="paceFilter" class="filter" :class="{ selected: selectedFilter === 'pace' }" @click.stop="selectFilter('pace')">Pace</li>
          <li ref="distanceFilter" class="filter" :class="{ selected: selectedFilter === 'distance' }" @click.stop="selectFilter('distance')">Distance</li>
        </ul>
        <button class="small-text reset-filters-btn" @click="resetFilters()">reset filters</button>
      </div>
    </div>


    <div class="profile-body">
      <div v-if="loading" class="assisting-text">Loading events...</div>
      <div v-else-if="error" class="error assisting-text">{{ error }}</div>
      <div v-else>
        <div v-for="group in groupedEvents" :key="group.label" class="date-block">
          <h2 class="date-header">{{ group.label }}</h2>
          <ul v-if="!loading">
            <li class="event" v-for="event in group.events" :key="event.id" @click="openEvent(event.id)">
              <div class="hour-club-pace">
                <div class="hour-club">
                  <p>{{ formatTime(event.dateTime) }}</p>
                  <p class="club">{{event.club.clubName}}</p>
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
}

.assisting-text{
  position: fixed;
  top: calc((100vh - 20vh) / 2);
  left: 50%;
  transform: translate(-50%, -50%);
}

.error {
  background-color: #181818;
  color: white;
}

.cities-modal{
  position: fixed;
  top: 10vh;
  left: 1rem;
  width: 80vw;
  z-index: 1001;

  background-color: #181818;
  color: white;
  border: 1px solid #FB5624;
}

.filter-modal{
  position: fixed;
  top: 70vh;
  left: 0;
  padding: 1rem 2rem;

  height: 30vh;
  width: 100vw;

  color: white;
  background-color: #181818;
  z-index: 1001;
  border-top: 1px solid #FB5624;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 1rem;
}

.modal-background{
  position: fixed;
  left: 0;
  bottom: 0;
  height: 100vh;
  width: 100vw;
  background-color: black;
  opacity: 0.85;
  z-index: 1000;
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
  z-index: 101;

  border-bottom: 1px solid #FB5624;
}

.title{
  display: flex;
  align-items: center;
  gap: 1rem;
  height: 10vh;
  padding-top: 1rem;
  padding-left: 1rem;
}

.reset-filters-btn{
  color: #FB5624;
}

.change-city-btn{
  color: white;
}

.filters-container{
  height: 10vh;
  list-style-type: none;

  display: flex;
  justify-content: space-between;
  align-items: center;

  color: white;
  padding: 0 1rem;
}

.filters{
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 0;
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

.profile-body {
  overflow-y: auto;
  margin-bottom: 15vh;
  padding: 0 1rem;

  -ms-overflow-style: none;  /* IE and Edge */
  scrollbar-width: none;
}

.profile-body::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Edge */
}

ul{
  list-style: none;
  padding: 0;
  margin: 0;
}

.date-header{
  font-size: 20px;
  font-weight: 800;
  border-bottom: 1px solid white;
  margin-top: 1rem;
}

.event{
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  margin: 1.5rem 0;
  font-size: 16px;
}

.hour-club-pace{
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.hour-club{
  display: flex;
  gap: 1rem;
  color: white;
}

.club{
  font-weight: 800;
  text-transform: uppercase;
}

.invisible-time{
  visibility: hidden;
}

.small-text{
  font-size: 14px;
  font-weight: 200;
  background-color: inherit;
  border: none;
}
</style>