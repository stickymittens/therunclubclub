<script setup>
import axios from "axios";
import {token} from "@/auth.js";
import {onMounted, ref} from "vue";

const loading = ref(null)
const error = ref(null)
const clubs = ref([])
const message = ref(null)


const loadClubs= async () => {
  loading.value = true
  error.value = null

  if(token.value){
    try {
      const res = await axios.get('http://192.168.1.128:8080/clubs/owned-clubs', {
        headers: {
          Authorization: `Bearer ${token.value}`
        }
      })

      clubs.value = res.data
      reloadClubs()
    } catch (err) {
      console.error(err)
      error.value = 'Failed to load clubs'
    } finally {
      loading.value = false
    }
  }
}

const deleteClub = async (id) => {
  try {
    if(token.value){
      await axios.delete(
          `http://localhost:8080/clubs/delete-club/${id}`,
          {
            headers: {
              Authorization: `Bearer ${token.value}`,
            },
          })
      message.value = `Left successfully!`;
    }
    reloadClubs()
  } catch (error) {
    console.error(error);
    message.value = "Error leaving";
  }
};

onMounted(() => {
  loadClubs()
})

function reloadClubs(){
  loadClubs()
}

</script>

<template>
  <div class="container">
    <div v-if="clubs.length === 0">
      <p>You haven't started a club yet</p>
    </div>
    <div v-else class="ul-container">
      <div v-if="loading">Loading events...</div>
      <div v-else-if="error" class="error">{{ error }}</div>

      <div v-else>
        <div v-for="(club, index) in clubs" :key="club.id || index">
          <p>{{club.clubName}}</p>
          <button @click="deleteClub(club.id)">X</button>
        </div>
      </div>

    </div>
  </div>
</template>

<style scoped></style>