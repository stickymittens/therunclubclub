<script setup>
  import { ref, onMounted, onBeforeUnmount } from 'vue'
  import axios from 'axios'
  import { useLocationStore } from '@/stores/LocationStore'


  // state
  const cities = ref([])
  const showDropdown = ref(false)
  const dropdownRef = ref(null)
  const locationStore = useLocationStore()


  //geolocation question
  function enableLocation() {
    if (!navigator.geolocation) {
      alert("Geolocation is not supported by your device or browser");
      return;
    }

    navigator.geolocation.getCurrentPosition((position) => {
          const { latitude, longitude } = position.coords
          locationStore.setLocation(latitude, longitude)
          console.log('Saved location:', locationStore.latitude, locationStore.longitude)
        },
        (error) => {
          console.error("Error getting location:", error);
          alert("Unable to retrieve your location.");
        }
    );
  }


  // toggle the dropdown open/close
  const toggleDropdown = () => {
    showDropdown.value = !showDropdown.value
  }

  // select a city
  const selectCity = (city) => {
    showDropdown.value = false
    locationStore.city = city;
    console.log('Saved city:', locationStore.city)
  }

  // close dropdown when clicking outside
  const handleClickOutside = (event) => {
    if (dropdownRef.value && !dropdownRef.value.contains(event.target)) {
      showDropdown.value = false
    }
  }

  // lifecycle hooks
  onMounted(async () => {
    // fetch cities from backend
    try {
      const res = await axios.get('http://192.168.1.128:8080/cities')
      cities.value = res.data
    } catch (err) {
      console.error('Failed to load cities:', err)
    }

    // listen for clicks outside
    document.addEventListener('click', handleClickOutside)
  })

  onBeforeUnmount(() => {
    document.removeEventListener('click', handleClickOutside)
  })
</script>


<template>
  <div class="container">

    <div class="section">
      <img id="map" src="/Users/karolina/IdeaProjects/runClub/frontend/src/components/icons/location-services-map.png" alt="map picture"/>
      <button @click="enableLocation">ENABLE LOCATION SERVICE</button>
    </div>

    <div id="or">OR</div>

    <div class="section">

      <!-- Dropdown container with ref for outside click -->
      <div class="custom-dropdown" ref="dropdownRef">
        <button @click="toggleDropdown" class="dropdown-btn">

          {{ locationStore.city || '-- Select a city --' }}
        </button>

        <!-- Dropdown list -->
        <ul v-if="showDropdown" class="dropdown-list">
          <li
              v-for="city in cities"
              :key="city"
              @click="selectCity(city)"
          >
            {{ city }}
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<style scoped>
  .container{
    width: 80vw;
    padding: 2rem 1rem;

    background-color: white;
    color: black;
    border-top: 10px solid #FB5624;
    border-radius: 8px;

    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 1rem;
  }

  .section{
    display: flex;
    flex-direction: column;
    justify-content: center;
    gap: 0.5rem;
    width: 100%;
  }


  #or{
    font-size: 12px;
    color: #A6A6A6;
  }

  button{
    background-color: #FB5624;
    color: white;
    font-size: 18px;
    font-weight: 800;
    padding: 0.5rem 1rem;

    border: none;
    border-radius: 8px;
  }

  h2{
    font-size: 18px;
  }

  .custom-dropdown {
    position: relative; /* important: dropdown list is relative to this */
    width: 100%;
  }

  .dropdown-btn {
    width: 100%;
    padding: 0.5rem 1rem;
    border-radius: 8px;
    background-color: #FB5624;
    color: white;
    border: none;
    cursor: pointer;
  }

  .dropdown-list {
    position: absolute; /* floats above other content */
    top: 100%; /* roll out below the button */
    left: 0;
    width: 100%;
    height: 200px;
    max-height: 30vh; /* optional: scroll if too long */
    overflow-y: auto; /*scrollable*/
    background-color: white;
    border: 1px solid #ddd;
    border-radius: 8px;
    z-index: 1000; /* make sure it floats above container */
    list-style: none;
    margin: 0;
    padding: 0;

  }

  .dropdown-list li {
    padding: 0.5rem 1rem;
    cursor: pointer;
  }

  .dropdown-list li:hover {
    background-color: #f3f3f3;
  }

  @media(width<400px) {
    button{
      font-size: 16px;
    }
  }

</style>