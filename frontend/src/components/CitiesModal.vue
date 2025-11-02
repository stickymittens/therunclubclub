<script setup>
import {ref, onMounted, onBeforeUnmount} from 'vue'
  import axios from 'axios'
  import { useLocationStore } from '@/stores/LocationStore'
  import {useCitiesModalStore} from "@/stores/CitiesModalStore.js";

  const cities = ref([])
  const citiesModal = ref(null)
  const locationStore = useLocationStore()
  const citiesModalStore = useCitiesModalStore()

  //selected city
  const selectCity = (city) => {
    citiesModalStore.visible = false;
    locationStore.city = city;
    console.log('Saved city:', locationStore.city)
  }

  // close modal when clicking outside
  const handleClickOutside = (event) => {
    if (citiesModal.value && !citiesModal.value.contains(event.target)) {
      citiesModalStore.visible = false;
    }
  }

  onMounted(async () => {
    try {
      citiesModalStore.loading = true
      const res = await axios.get('http://192.168.1.128:8080/cities')
      cities.value = res.data
      citiesModalStore.visible = true
    } catch (err) {
      console.error('Failed to load cities:', err)
    } finally{
      citiesModalStore.loading = false
    }

    // listen for clicks outside
    document.addEventListener('click', handleClickOutside)
  })

  onBeforeUnmount(() => {
    document.removeEventListener('click', handleClickOutside)
  });
</script>


<template>
  <div v-if="citiesModalStore.visible && !citiesModalStore.loading" ref="citiesModal" class="container">
    <ul>
      <li
          v-for="city in cities"
          :key="city"
          @click="selectCity(city)"
      >
        {{ city }}
      </li>
    </ul>
  </div>
</template>

<style scoped>
.container{
  max-height: 60vh;
  width: 80vw;
  padding: 2rem;
  margin: 0;

  background-color: white;
  color: black;
  border-radius: 8px;

  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;

  overflow-y: auto;
}

ul{
  list-style-type: none;

  margin:0;
  padding:0;

  width: 100%;
}

li{
  padding: 0.5rem 0;
  border-bottom: 0.5px solid #A6A6A6;
}
</style>