<script setup>
  import { ref, onMounted, computed} from 'vue'
  import axios from 'axios'
  import { useRoute } from 'vue-router'
  import { jwtDecode } from "jwt-decode";
  import {token} from "@/auth.js";

  const route = useRoute()
  const event = ref(null)
  const joinRun = ref(null)

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


  //fetching event by id
  async function fetchEvent(id) {
    try {
      const res = await axios.get(`http://172.20.10.7:8080/events/${id}`)
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


  //sign up for event if user logged in


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

    <button v-if="username" ref="joinRun" @click="joinEvent(event.id)">Join the run</button>
  </div>
</template>

<style scoped></style>