<script setup>
  import { ref, onMounted, computed} from 'vue'
  import axios from 'axios'
  import { useRoute } from 'vue-router'

  const route = useRoute()
  const event = ref(null)

  const loading = ref(true)
  const error = ref(null)

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

  async function fetchEvent(id) {
    try {
      const res = await axios.get(`http://192.168.1.128:8080/events/${id}`)
      event.value = res.data
    } catch (err) {
      console.error('Error fetching event:', err)
    }finally {
      loading.value = false
    }
  }

  onMounted(() => {
    const id = route.params.id
    if (id) fetchEvent(id)
  })
</script>

<template>
  <div class="container">
    <div>
      <div v-if="loading">Loading event...</div>
      <div v-else-if="error" class="error">{{ error }}</div>

      <div v-else>
        <p>{{ formatTime(event.dateTime) }}</p>
        <p>{{event.pace}}</p>
        <p >{{ event.distance }} km - {{ formatPace(event.pace) }} min/km</p>
      </div>
    </div>
  </div>
</template>

<style scoped></style>