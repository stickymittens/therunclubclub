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
const filterModal = ref(null)
const filterModalVisible = ref(false)

//FILTERS
const minPace = ref(null)
const maxPace = ref(null)
const minDistance = ref(null)
const maxDistance = ref(null)


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
const loadEvents = async () => {
  loading.value = true
  error.value = null

  try {
    const baseUrl = 'http://192.168.1.128:8080/events/upcoming'
    const distanceActive = minDistance.value !== null && maxDistance.value !== null
    const paceActive = minPace.value !== null && maxPace.value !== null

    let eventsData = []

    // 🧩 CASE 1: both filters active → intersect results
    if (distanceActive && paceActive) {
      const [distanceRes, paceRes] = await Promise.all([
        axios.get(`${baseUrl}/filtered-by-distance?min=${minDistance.value}&max=${maxDistance.value}`),
        axios.get(`${baseUrl}/filtered-by-pace?min=${minPace.value}&max=${maxPace.value}`)
      ])
      const paceIds = new Set(paceRes.data.map(e => e.id))
      eventsData = distanceRes.data.filter(e => paceIds.has(e.id))
    }
    // 🧩 CASE 2: only distance
    else if (distanceActive) {
      const res = await axios.get(`${baseUrl}/filtered-by-distance?min=${minDistance.value}&max=${maxDistance.value}`)
      eventsData = res.data
    }
    // 🧩 CASE 3: only pace
    else if (paceActive) {
      const res = await axios.get(`${baseUrl}/filtered-by-pace?min=${minPace.value}&max=${maxPace.value}`)
      eventsData = res.data
    }
    // 🧩 CASE 4: no filters
    else {
      const res = await axios.get(baseUrl)
      eventsData = res.data
    }

    events.value = eventsData

    // Initialize ranges when loading unfiltered data
    if (!distanceActive && !paceActive && eventsData.length > 0) {
      const distances = eventsData.map(e => e.distance)
      minDistance.value = Math.floor(Math.min(...distances))
      maxDistance.value = Math.ceil(Math.max(...distances))

      const paces = eventsData.map(e => e.pace)
      minPace.value = Math.floor(Math.min(...paces) * 100) / 100
      maxPace.value = Math.ceil(Math.max(...paces) * 100) / 100
    }

  } catch (err) {
    console.error(err)
    error.value = 'Failed to load events'
  } finally {
    loading.value = false
  }
}



onMounted(() => {
  loadEvents()
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
function updateDistance(vals) {
  minDistance.value = vals.min
  maxDistance.value = vals.max
  loadEvents()
}

function updatePace(vals) {
  minPace.value = vals.min
  maxPace.value = vals.max
  loadEvents()
}

// function resetFilters() {
//   // Reset distances
//   if (events.value.length > 0) {
//     const distances = events.value.map(e => e.distance)
//     minDistance.value = Math.floor(Math.min(...distances))
//     maxDistance.value = Math.ceil(Math.max(...distances))
//
//     const paces = events.value.map(e => e.pace)
//     minPace.value = Math.floor(Math.min(...paces) * 100) / 100
//     maxPace.value = Math.ceil(Math.max(...paces) * 100) / 100
//   }
//
//   loadEvents() // reload events with full ranges
// }
</script>

<template>
  <CitiesModal v-if="citiesModalStore.visible" class="cities-modal"/>

  <div v-if="filterModalVisible" ref="modalBackground" class="modal-background"></div>
  <div v-if="filterModalVisible" ref="filterModal">

<!--    <div v-if="selectedFilter === 'all'" class="all-options-modal">-->
<!--      <p>showing all options</p>-->
<!--    </div>-->

<!--    <div v-if="selectedFilter === 'time'" class="filter-modal">-->
<!--      <p>showing time options</p>-->
<!--    </div>-->

    <div v-if="selectedFilter === 'pace'" class="filter-modal">
      <p>Currently available paces:</p>
      <FilterInput
          label="Pace (min/km)"
          :min="minPace"
          :max="maxPace"
          :step="0.01"
          :decimalPlaces="2"
          @updateFilters="updatePace"
      />
      <button @click="resetFilters()">Reset filters</button>
    </div>

    <div v-if="selectedFilter === 'distance'" class="filter-modal">
      <p>Currently available distances:</p>
      <FilterInput
          label="Distance (km)"
          :min="minDistance"
          :max="maxDistance"
          :step="1"
          @updateFilters="updateDistance"
      />
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

    <div class="filters-container">
      <ul class="filters">
        <!--      <li class="filter" :class="{ selected: selectedFilter === 'all' }" @click.stop="selectFilter('all')">Icon</li>-->
        <!--      <li class="filter" :class="{ selected: selectedFilter === 'time' }" @click.stop="selectFilter('time')">Start Time</li>-->
        <li class="filter" :class="{ selected: selectedFilter === 'pace' }" @click.stop="selectFilter('pace')">Pace</li>
        <li class="filter" :class="{ selected: selectedFilter === 'distance' }" @click.stop="selectFilter('distance')">Distance</li>
      </ul>

<!--      <button id="reset-filters-btn" @click="resetFilters()">Reset filters</button>-->
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
                  <p class="club">{{ event.club.clubName }}</p>
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

  margin: 0;
}

.cities-modal{
  position: absolute;
  width: 80vw;
  top: 6rem;
  left: 10vw;
  z-index: 101;
}

h1{
  height: 6rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-weight: 800;

  background-color: pink;
}

.filters-container{
  display: flex;
  gap: 1rem;
}

#reset-filters-btn{
  background-color: inherit;
  color: #FB5624;
  font-weight: 200;
  border: none;

  width: fit-content;
}

.filters{
  display: flex;
  gap: 1rem;
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
  padding: 1rem 2rem;

  height: 30vh;
  width: 100vw;

  color: white;
  background-color: #181818;
  z-index: 1001;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.all-options-modal{
  position: fixed;
  left: 0;

  height: 100vh;
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
  opacity: 0.85;
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