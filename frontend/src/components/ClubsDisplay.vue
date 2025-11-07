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
    <div v-if="loading">
      <p class="message">Loading clubs..</p>
    </div>
    <div v-else-if="!loading&&clubs.length === 0">
      <p>You haven't started a club yet</p>
    </div>
    <div v-else class="ul-container">
      <div v-if="loading">Loading events...</div>
      <div v-else-if="error" class="error">{{ error }}</div>

      <div v-else class="carousel">
        <div v-for="(club, index) in clubs" :key="club.id || index" class="carousel-item">
          <p>{{club.clubName}}</p>
          <button @click="deleteClub(club.id)">delete club</button>
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

  width: 100%;
  background-color: #181818;
  padding: 1rem;

  z-index: 10;
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
  padding: 1.5rem;

  flex: 0 0 auto;
  width: 100%;
  min-height: 20vh;

  border-radius: 12px;
  color: #fff;
  border: 1px dotted #FB5624;
  font-weight: 600;
  cursor: pointer;

  transition: box-shadow 0.4s ease;
}

.carousel-item:hover {
  box-shadow: 0 0 12px rgba(251, 86, 36, 0.2);
}

.carousel-item button{
  width: fit-content;
  background-color: inherit;
  color: #FB5624;
  border: none;
}
</style>