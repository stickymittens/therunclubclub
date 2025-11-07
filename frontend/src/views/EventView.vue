<script setup>
import {ref, onMounted, computed, watch} from 'vue'
  import axios from 'axios'
  import { useRoute } from 'vue-router'
  import { jwtDecode } from "jwt-decode";
  import {token} from "@/auth.js";

  const route = useRoute()
  const event = ref(null)
  const joinRun = ref(null)

  const loading = ref(true)
  const error = ref(null)

  const username = ref(null);
  const message = ref("");

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


  //get username if user logged in
  function decodeToken() {
    if (token.value) {
      try {
        const decoded = jwtDecode(token.value);
        username.value = decoded.sub  || null;
      } catch (err) {
        console.error("Invalid token", err);
        username.value = null;
      }
    } else {
      username.value = null;
    }
  }

    // Decode token when the component mounts
    onMounted(() => {
      decodeToken();
    });

    // Watch the reactive token from auth.js to update username
    watch(token, () => {
      decodeToken();
    });

  //   //BIG FUNCTION TO JOIN lets gooo
  const checkIfUserSignedUp = () => {

    if(event.value){
      if(event.value.signedUpUsers.length > 0){
        const signedUpUsers = event.value.signedUpUsers;
        let userSignedUp = false;

        for (let i = 0; i < signedUpUsers.length; i++) {
          if (username.value === signedUpUsers[i].username) {
            userSignedUp = true;
            break;
          }
        }
        return userSignedUp;
      }
    }
  };

  onMounted(() => {
    checkIfUserSignedUp();
  });

  const joinEvent = async (id) => {
    try {
      if(token.value){
        await axios.post(
            `http://localhost:8080/events/${id}/sign-up`,
            null,
            {
              headers: {
                Authorization: `Bearer ${token.value}`,
              },
            })
        message.value = `Added successfully!`;
      }

    } catch (error) {
      console.error(error);
      message.value = "Error signing up";
    }
  };

const leaveEvent = async (id) => {
  try {
    if(token.value){
      await axios.delete(
          `http://localhost:8080/events/${id}/leave`,
          {
            headers: {
              Authorization: `Bearer ${token.value}`,
            },
          })
      message.value = `Left successfully!`;
    }

  } catch (error) {
    console.error(error);
    message.value = "Error leaving";
  }
};
</script>

<template>
  <div class="container">
    <div v-if="event">
      <div v-if="loading">Loading event...</div>
      <div v-else-if="error" class="error">{{ error }}</div>

      <div v-else>
        <h1 class="header-container">
          {{event.club.clubName}}
        </h1>

        <div class="body-container">
          <p>{{ formatTime(event.dateTime) }}</p>
          <p>-</p>
          <p>{{ event.distance }}km</p>
          <p>-</p>
          <p >{{ formatPace(event.pace) }}min/km</p>
        </div>
      </div>
    </div>

    <div v-if="username">
      <button v-if="checkIfUserSignedUp()" @click="leaveEvent(event.id)">Leave the run</button>
      <button v-else ref="joinRun" @click="joinEvent(event.id)">Join the run</button>
    </div>

    <p>{{message}}</p>
  </div>
</template>

<style scoped>
.container{
  position: relative;

  display: flex;
  flex-direction: column;
  gap: 2rem;
  padding: 0 1rem;

  width: 100%;
}

h1{
  height: 2rem;
  display: flex;
  align-items: center;
  font-weight: 800;

  background-color: pink;
}


.header-container{
  display: flex;
  align-items: center;

  height: 6rem;
}

.body-container{
  display: flex;
  justify-content: space-between;
  align-items: center;

  height: 6rem;

  font-size: 20px;
}

</style>